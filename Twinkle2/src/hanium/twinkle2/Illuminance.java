package hanium.twinkle2;





import java.util.ArrayList;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class Illuminance extends Fragment {

    // Message types sent from the BluetoothChatService Handler
    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;

    // Key names received from the BluetoothChatService Handler
    public static final String DEVICE_NAME = "device_name";
    public static final String TOAST = "toast";

    // Array adapter for the conversation thread
    private ArrayAdapter<String> mListArrayAdapter;
    
    public BulbAdapter m_adapter = null;
    
    
	View view = null;
	TextView textview = null;
	private ListView mListView;
	private EditText mOutEditText;
	private Button mSendButton;
	private BluetoothChatService mChatService;
	private StringBuffer mOutStringBuffer;
	
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState){
		view = inflater.inflate(R.layout.activity_illuminance, container, false);
		//textview = (TextView)view.findViewById(R.id.textView1);
		return view;
	}

    public void setupChat() {
       // Log.d(TAG, "setupChat()");

        // Initialize the array adapter for the conversation thread
       // mListArrayAdapter = new ArrayAdapter<String>(this, R.layout.message);
        mListView = (ListView)view.findViewById(R.id.in);
        //mListView.setAdapter(mListArrayAdapter);

        // Initialize the compose field with a listener for the return key
        mOutEditText = (EditText)view.findViewById(R.id.edit_text_out);
        mOutEditText.setOnEditorActionListener(mWriteListener);

        // Initialize the send button with a listener that for click events
        mSendButton = (Button)view.findViewById(R.id.button_send);
        mSendButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                // Send a message using content of the edit text widget
                TextView tview = (TextView)view.findViewById(R.id.edit_text_out);
                String message = tview.getText().toString();
                sendMessage(message);
            }
        });

        // Initialize the BluetoothChatService to perform bluetooth connections
        mChatService = new BluetoothChatService(getActivity(), mHandler);

        // Initialize the buffer for outgoing messages
        mOutStringBuffer = new StringBuffer("");
    }
    private void sendMessage(String message) {
        // Check that we're actually connected before trying anything
        if (mChatService.getState() != BluetoothChatService.STATE_CONNECTED) {
            //Toast.makeText(this, R.string.not_connected, Toast.LENGTH_SHORT).show();
            return;
        }

        // Check that there's actually something to send
        if (message.length() > 0) {
            // Get the message bytes and tell the BluetoothChatService to write
            byte[] send = message.getBytes();
            mChatService.write(send);

            // Reset out string buffer to zero and clear the edit text field
            mOutStringBuffer.setLength(0);
            mOutEditText.setText(mOutStringBuffer);
        }
    }
	private TextView.OnEditorActionListener mWriteListener =
        new TextView.OnEditorActionListener() {
        public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
            // If the action is a key-up event on the return key, send the message
            if (actionId == EditorInfo.IME_NULL && event.getAction() == KeyEvent.ACTION_UP) {
                String message = view.getText().toString();
                sendMessage(message);
            }
            return true;
        }
    };

    private final void setStatus(int resId) {
        //final ActionBar actionBar = getActionBar();
        //actionBar.setSubtitle(resId);
    }

    private final void setStatus(CharSequence subTitle) {
        //final ActionBar actionBar = getActionBar();
        //actionBar.setSubtitle(subTitle);
    } 
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case MESSAGE_STATE_CHANGE:
                //if(D) Log.i(TAG, "MESSAGE_STATE_CHANGE: " + msg.arg1);
                switch (msg.arg1) {
                case BluetoothChatService.STATE_CONNECTED:
                    //setStatus(getString(R.string.title_connected_to, mConnectedDeviceName));
                    mListArrayAdapter.clear();
                    break;
                case BluetoothChatService.STATE_CONNECTING:
                    //setStatus(R.string.title_connecting);
                    break;
                case BluetoothChatService.STATE_LISTEN:
                case BluetoothChatService.STATE_NONE:
                    //setStatus(R.string.title_not_connected);
                    break;
                }
                break;
            case MESSAGE_WRITE:
                byte[] writeBuf = (byte[]) msg.obj;
                /// construct a string from the buffer
                String writeMessage = new String(writeBuf);
                //mListArrayAdapter.add("Me:  " + writeMessage);
                break;
            case MESSAGE_READ:
            	//Illuminance ill = (Illuminance)getFragmentManager().findFragmentById(R.id.Illuminance);
            	Bulb b = null;
            	String[] b_info = null;
            	ArrayList<Bulb> m_list = new ArrayList<Bulb>();
            	
                byte[] readBuf = (byte[]) msg.obj;
                // construct a string from the valid bytes in the buffer
                String readMessage = new String(readBuf, 0, msg.arg1);
                //mListArrayAdapter.add(mConnectedDeviceName+":  " + readMessage);
                if(readMessage.startsWith("LIST ")){
                	b_info = readMessage.split(" ");
                	
                	for(int i=1 ; i<b_info.length ; i+=2){
                		b = new Bulb(false, b_info[i],b_info[i+1]);
                    	m_list.add(b);
                    	
                	}
                	
                	//Illumiminance illu = (Illuminance)getFragmentManager().findFragmentById(arg0)
                	m_adapter = new BulbAdapter(getActivity(),R.layout.message,m_list);
               
                	mListView.setAdapter(m_adapter);
                	//mListArrayAdapter.add(readMessage.substring(5));
                }
                break;
            case MESSAGE_DEVICE_NAME:
                // save the connected device's name
                //mConnectedDeviceName = msg.getData().getString(DEVICE_NAME);
                //Toast.makeText(getApplicationContext(), "Connected to "
                //               + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
                break;
            case MESSAGE_TOAST:
                //Toast.makeText(getApplicationContext(), msg.getData().getString(TOAST),
                //               Toast.LENGTH_SHORT).show();
                break;
            }
        }
        private OnItemClickListener mItemClickLitner = new OnItemClickListener(){
        	@Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                Toast.makeText(getActivity(), ""+(position+1), 
                        Toast.LENGTH_SHORT).show();
     
                //m_adapter.setChecked(position);
                // Data 변경시 호출 Adapter에 Data 변경 사실을 알려줘서 Update 함.
                m_adapter.notifyDataSetChanged();
     
            }
        };
    };
    


    private class BulbAdapter extends ArrayAdapter<Bulb>{
       // private ViewHolder viewHolder = null;
    	private LayoutInflater inflater = null;
    	private ArrayList<Bulb> items = null;
        private boolean[] isCheckedConfirm;
        private boolean[] isOnOffConfirm;
    	
        
		public BulbAdapter(Context context, int resource, ArrayList<Bulb> items) {
			super(context, resource, items);
			this.items = items;
			//checkbox의 체크 유뮤 확인용
			this.isCheckedConfirm = new boolean[items.size()];
			//on off 확인용(on==1 off==0)
			this.isOnOffConfirm = new boolean[items.size()];
				
			// TODO Auto-generated constructor stub
		}

		//체크박스를 모두 선택
        public void setAllChecked(boolean ischeked) {
            int tempSize = isCheckedConfirm.length;
            for(int i=0 ; i<tempSize ; i++){
                isCheckedConfirm[i] = ischeked;
            }
        }

        public void setChecked(int position) {
            isCheckedConfirm[position] = !isCheckedConfirm[position];
        }/*
        public ArrayList<Bulb> getChecked(){
            int tempSize = isCheckedConfirm.length;
            ArrayList<Bulb> mArrayList = new ArrayList<Bulb>();
            for(int b=0 ; b<tempSize ; b++){
                if(isCheckedConfirm[b]){
                    mArrayList.add(b);
                }
            }
            return mArrayList;
        }*/
        

        public int getCount() { 
            return items.size();
        }
        
        
		
    	@Override
		public View getView(int position, View convertView, ViewGroup parent){
			View v = convertView;
			if(v==null){
				LayoutInflater vi = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.message, null);
			}
			Bulb b = items.get(position);
			if(b != null){
				TextView name_text = (TextView)v.findViewById(R.id.bulb_name);
				TextView id_text = (TextView)v.findViewById(R.id.bulb_id);
				CheckBox check = (CheckBox)v.findViewById(R.id.checkBox1);
				
				if(name_text != null){
					name_text.setText(b.getName());
				}
				if(id_text != null){
					id_text.setText(b.getId());
				}
				//if(check != null){
				//}
				check.setClickable(false);
				check.setFocusable(false);
			}
			
			return v;
		}
    }

}
