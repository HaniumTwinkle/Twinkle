package hanium.twinkle3;


import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothProfile;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class Logo extends Activity {
    // Debugging
    public static final String TAG = "Logo";
    public static final boolean D = true;


    // Intent request codes
    private static final int REQUEST_CONNECT_DEVICE_SECURE = 1;
    private static final int REQUEST_CONNECT_DEVICE_INSECURE = 2;
    private static final int REQUEST_ENABLE_BT = 3;
    // Intent variables
    private Intent enableIntent = null;
    private Intent MainIntent = null;
    private Intent serverIntent = null;
    
    // for checking connection
	private boolean isConnected;
	
	private Timer timer = null;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_logo);
		
        // Get local Bluetooth adapter
        MainActivity.mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // If the adapter is null, then Bluetooth is not supported
        if (MainActivity.mBluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        else{ 

        	MainActivity.m_list = new ArrayList<Bulb>();
            
        }
	}
	public void onStart() {
        super.onStart();
        if (!MainActivity.mBluetoothAdapter.isEnabled()) {
            enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);

        	}
            MainActivity.mChatService = new BluetoothChatService(this, MainActivity.mHandler);

            //do{

            serverIntent = new Intent(this, DeviceListActivity.class);
            startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE_INSECURE);
            
        }
        private TimerTask task = new TimerTask(){

    		@Override
    		public void run() {
    			if(MainActivity.RECEIVED_EVER == false){
        			MainActivity.OutBuffer = "L";
        			MainActivity.sendMessage(MainActivity.OutBuffer);
    			}
    			else{
    				intentMain();
    			}    				
    		}
        	
        };
	 public void onActivityResult(int requestCode, int resultCode, Intent data) {
	    if(D) Log.d(TAG, "onActivityResult " + resultCode);
	    switch (requestCode) {
	    case REQUEST_CONNECT_DEVICE_SECURE:
	            // When DeviceListActivity returns with a device to connect
	        if (resultCode == Activity.RESULT_OK) {
	                connectDevice(data, true);
	        }
	        break;
	    case REQUEST_CONNECT_DEVICE_INSECURE:
	        // When DeviceListActivity returns with a device to connect
	        if (resultCode == Activity.RESULT_OK) {
	                connectDevice(data, false);	     	                
	        }
	        
	        isConnected = MainActivity.mChatService.getState() == BluetoothProfile.STATE_CONNECTED;
            if(isConnected==true){
    			//MainActivity.OutBuffer = "LIST/";
    			//MainActivity.sendMessage(MainActivity.OutBuffer);
        		timer = new Timer();
        		timer.schedule(task, 1000,1000);
        		//isConnected=false;
            }
            //else
                //startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE_INSECURE);
	        break;
	    case REQUEST_ENABLE_BT:
	        // When the request to enable Bluetooth returns
	        if (resultCode == Activity.RESULT_OK) {
	                // Bluetooth is now enabled, so set up a chat session
	                ////////////////////////setupChat();
	        } 
	        else {
	                // User did not enable Bluetooth or an error occurred
	            Log.d(TAG, "BT not enabled");
	            Toast.makeText(this, R.string.bt_not_enabled_leaving, Toast.LENGTH_SHORT).show();
	            finish();
	        }
	    }
	}
	 
	private void intentMain(){

    	MainIntent = new Intent(this, MainActivity.class);
    	timer.cancel();
		MainActivity.RECEIVED_EVER = false;
      	startActivity(MainIntent);
       	finish();
	}

	private void connectDevice(Intent data, boolean secure) {
	        // Get the device MAC address
	    String address = data.getExtras()
	        .getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
	        // Get the BluetoothDevice object
	    BluetoothDevice device = MainActivity.mBluetoothAdapter.getRemoteDevice(address);
	        // Attempt to connect to the device
	    MainActivity.mChatService.connect(device, secure);
	}
	 
	 /*
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.logo, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
            startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE_INSECURE);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}*/
}
