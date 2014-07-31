package hanium.twinkle;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity implements OnClickListener{

	Button btn_light;
	Button btn_prox;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		btn_light = (Button)findViewById(R.id.button1);
		btn_light.setOnClickListener(this);
		
		btn_prox = (Button)findViewById(R.id.button2);
		btn_prox.setOnClickListener(this);
	}

	@Override
	public void onClick(View v){
		int id = v.getId();
		switch(id){
		case R.id.button1:
			Intent intent = new Intent(this,Light_Sensor.class);
			startActivity(intent);
			finish();
			break;
		case R.id.button2:
			Intent intent2 = new Intent(this,Proximity_Sensor.class);
			startActivity(intent2);
			finish();
			break;
		}
		
		
	}

}
