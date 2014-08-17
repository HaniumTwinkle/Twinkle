package hanium.twinkle3;

import java.util.ArrayList;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends Activity {
    // Key names received from the BluetoothChatService Handler
    public static final String DEVICE_NAME = "device_name";
    public static final String TOAST = "toast";

    // Message types sent from the BluetoothChatService Handler
    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;
    
	public static BluetoothAdapter mBluetoothAdapter = null;
    public static BluetoothChatService mChatService = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public static Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case MESSAGE_STATE_CHANGE:
                //if(D) Log.i(TAG, "MESSAGE_STATE_CHANGE: " + msg.arg1);
                switch (msg.arg1) {
                case BluetoothChatService.STATE_CONNECTED:
                   // setStatus(getString(R.string.title_connected_to, mConnectedDeviceName));
                   // mConversationArrayAdapter.clear();
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
                //mConversationArrayAdapter.add("Me:  " + writeMessage);
                break;
            case MESSAGE_READ:
            	Bulb b = null;
            	String[] b_info = null;
            	ArrayList<Bulb> m_list = new ArrayList<Bulb>();
            	
                byte[] readBuf = (byte[]) msg.obj;
                // construct a string from the valid bytes in the buffer
                String readMessage = new String(readBuf, 0, msg.arg1);
                //mConversationArrayAdapter.add(mConnectedDeviceName+":  " + readMessage);
                if(readMessage.startsWith("LIST ")){
                	b_info = readMessage.split(" ");
                	
                	for(int i=1 ; i<b_info.length ; i+=2){
                		b = new Bulb(false, b_info[i],b_info[i+1]);
                    	m_list.add(b);
                    	
                	}
                	
                	//m_adapter = new BulbAdapter(getApplicationContext(),R.layout.message,m_list);
                	
                	//mConversationView.setAdapter(m_adapter);
                	//mConversationArrayAdapter.add(readMessage.substring(5));
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
    };
}
