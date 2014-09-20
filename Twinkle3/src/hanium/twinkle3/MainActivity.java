package hanium.twinkle3;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity implements ActionBar.TabListener{
	SectionsPagerAdapter mSectionsPagerAdapter;

	ViewPager mViewPager;
	
    // Key names received from the BluetoothChatService Handler
    public static final String DEVICE_NAME = "device_name";
    public static final String TOAST = "toast";

    // Message types sent from the BluetoothChatService Handler
    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;
    
    // fragmnet section number
    public static final int SECTION_ILLUMINANCE = 1;
    public static final int SECTION_ALARM = 2;
    
	public static BluetoothAdapter mBluetoothAdapter = null;
    public static BluetoothChatService mChatService = null;
    public static String OutBuffer = null; 
    
    public static boolean RECEIVED_EVER = false;
    public static ArrayList<Bulb> m_list = null;
    public static Context mContext = null;
    
    public static BulbAdapter m_adapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    	m_adapter = new BulbAdapter(getApplicationContext(),R.layout.list_format,m_list);
    	
        mContext = getApplicationContext();
		// Set up the action bar.
		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the activity.
		mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		// When swiping between different sections, select the corresponding
		// tab. We can also use ActionBar.Tab#select() to do this if we have
		// a reference to the Tab.
		mViewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						actionBar.setSelectedNavigationItem(position);
					}
				});

		// For each of the sections in the app, add a tab to the action bar.
		for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
			// Create a tab with text corresponding to the page title defined by
			// the adapter. Also specify this Activity object, which implements
			// the TabListener interface, as the callback (listener) for when
			// this tab is selected.
			actionBar.addTab(actionBar.newTab()
					.setText(mSectionsPagerAdapter.getPageTitle(i))
					.setTabListener(this));
		}
		//Timer timer = new Timer();
		//timer.schedule(task, 2000);

    	
    }/*
    private TimerTask task = new TimerTask(){

		@Override
		public void run() {
			MainActivity.OutBuffer = "LIST/";
			MainActivity.sendMessage(MainActivity.OutBuffer);
			//Toast.makeText(getApplicationContext(),R.string.send_toast, Toast.LENGTH_SHORT).show();
		}
    	
    };*/
/*
    private AdapterView.OnItemClickListener mItemClickListener = new AdapterView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			m_adapter.getItmes().get(arg2).getName();
			
		}
	};*/

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
            	
            	byte[] readBuf = (byte[]) msg.obj;
                // construct a string from the valid bytes in the buffer
                String readMessage = new String(readBuf, 0, msg.arg1);
                //mConversationArrayAdapter.add(mConnectedDeviceName+":  " + readMessage);
                //if(readMessage.startsWith("LIST")){
                	
                	if(RECEIVED_EVER == false){
                		RECEIVED_EVER = true;
            			MainActivity.OutBuffer = "Received/";
            			MainActivity.sendMessage(MainActivity.OutBuffer);
            			//int temp = readMessage.charAt(0);
                    	int temp = Integer.valueOf(readMessage);
                    			
            			char id = 'A';
            			byte num = 0;
                    	for(int i=0 ; i<temp ; i++){
                    		b = new Bulb(false,String.valueOf((char)(id+i)),String.valueOf((char)(id+i)),String.valueOf(0));
                        	m_list.add(b);
                        	num +=1;
                    	}
                	
                	}
                	
                	//b_info = readMessage.split(" ");
                	
                	
                	//m_adapter.notifyDataSetChanged();
                	
                	//mConversationView.setAdapter(m_adapter);
                	//mConversationArrayAdapter.add(readMessage.substring(5));
                //}
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

	@Override
	public void onTabReselected(Tab arg0, FragmentTransaction arg1) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onTabSelected(Tab tab, FragmentTransaction arg1) {
		// TODO Auto-generated method stub
		mViewPager.setCurrentItem(tab.getPosition());
		
	}


	@Override
	public void onTabUnselected(Tab arg0, FragmentTransaction arg1) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a PlaceholderFragment (defined as a static inner class
			// below).
			return PlaceholderFragment.newInstance(position + 1);
		}

		@Override
		public int getCount() {
			// Show 3 total pages.
			return 2;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.title_illuminance).toUpperCase(l);
			case 1:
				return getString(R.string.title_alarm).toUpperCase(l);
			}
			return null;
		}
		
		//public Object instantiateItem
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		private static final String ARG_SECTION_NUMBER = "section_number";

		/**
		 * Returns a new instance of this fragment for the given section number.
		 */
		public static Fragment newInstance(int sectionNumber) {
			Fragment change = null;
			switch(sectionNumber){
			case SECTION_ILLUMINANCE:
				change = new Illuminance();
				break;
			case SECTION_ALARM:
				change = new Alarm();
				break;
			}
			return change;
		}

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_illuminance, container,
					false);
			return rootView;
		}
	}
	
    public static void sendMessage(String message) {
        if (message.length() > 0) {
            // Get the message bytes and tell the BluetoothChatService to write
            byte[] send = message.getBytes();
            mChatService.write(send);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Stop the Bluetooth chat services
        if (mChatService != null) mChatService.stop();
    }


	public class BulbAdapter extends ArrayAdapter<Bulb>{
	    // private ViewHolder viewHolder = null;
	 	 private LayoutInflater inflater = null;
	     private ArrayList<Bulb> items = null;
	     private boolean[] isCheckedConfirm;
	     private boolean[] isOnOffConfirm;
	     
	     private String buffer;
	 	
	     
	     public BulbAdapter(Context context, int resource, ArrayList<Bulb> items) {
				super(context, resource, items);
				this.items = items;
				//checkbox��泥댄겕 �좊� �뺤씤��				this.isCheckedConfirm = new boolean[items.size()];
				//on off �뺤씤��on==1 off==0)
				this.isOnOffConfirm = new boolean[items.size()];
					
				// TODO Auto-generated constructor stub
			}
	
	     public ArrayList<Bulb> getItmes(){
	    	 return items;
	     }
	     
	
	     public int getCount() { 
	         return items.size();
	     }
	     
	     
			
	 	@Override
			public View getView(final int position, View convertView, ViewGroup parent){
				View v = convertView;
				final Bulb Current_item = m_adapter.getItem(position);
				
				if(v==null){
					LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					v = vi.inflate(R.layout.list_format, null);
				}
				Bulb b = items.get(position);
				if(b != null){
					TextView name_text = (TextView)v.findViewById(R.id.bulb_name);
					TextView id_text = (TextView)v.findViewById(R.id.bulb_id);
					final CheckBox check = (CheckBox)v.findViewById(R.id.checkBox1);
					final SeekBar seek = (SeekBar)v.findViewById(R.id.seekBar1);
					final Switch swc = (Switch)v.findViewById(R.id.switch1);
					//swc.setChecked(true);
					
					if(Current_item.getOnOff()==false){
							seek.setEnabled(false);
							check.setEnabled(false);
					}
							
					
					
					if(name_text != null){
						name_text.setText(b.getName());
					}
					if(id_text != null){
						id_text.setText(b.getId());
					}
					//if(check != null){
					//}
					//check.setClickable(false);
					//check.setFocusable(false);
					swc.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {

						@Override
						public void onCheckedChanged(CompoundButton arg0,
								boolean arg1) {
							// TODO Auto-generated method stub
							Current_item.setOnOff(!Current_item.getOnOff());
							buffer = null;
							
							if(Current_item.getOnOff()==true){
								//swc.setChecked(true);
								buffer =Current_item.getId();
								seek.setEnabled(true);
								check.setEnabled(true);
								sendMessage(buffer);
								buffer = Current_item.getStatus();
								SystemClock.sleep(150);
								sendMessage(buffer);
							}
							else{
								//swc.setChecked(false);
								buffer =Current_item.getId();
								seek.setEnabled(false);
								check.setEnabled(false);
								sendMessage(buffer);
								buffer = "0";
								SystemClock.sleep(150);
								sendMessage(buffer);
							}
							m_adapter.notifyDataSetChanged();
							
							
							
						}
					/*	
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							Current_item.setCheck(!Current_item.getCheck());
							buffer = null;
							if(Current_item.getCheck()==true)
								buffer ="On "+Current_item.getId();
							elsedj
								buffer ="Off "+Current_item.getId();
							
							sendMessage(buffer);
							m_adapter.notifyDataSetChanged();
						}*/
					});
					
					seek.setProgress(Current_item.getIntStatus());
					seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
						
						@Override
						public void onStopTrackingTouch(SeekBar seekBar) {
							// TODO Auto-generated method stub
							
						}
						
						@Override
						public void onStartTrackingTouch(SeekBar seekBar) {
							// TODO Auto-generated method stub
							
						}
						
						@Override
						public void onProgressChanged(SeekBar seekBar, int progress,
								boolean fromUser) {
							// TODO Auto-generated method stub
							Current_item.setStatus(String.valueOf(progress));
							buffer = null;
							buffer = Current_item.getId();
							sendMessage(buffer);
							buffer = String.valueOf(progress);
							SystemClock.sleep(150);
							sendMessage(buffer);
							SystemClock.sleep(150);

							m_adapter.notifyDataSetChanged();
						}
					});
				}
				
				return v;
			}
	     
	 }

}
