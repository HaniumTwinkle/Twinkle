package hanium.twinkle;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

public class Light_Sensor extends Activity implements SensorEventListener{

	SensorManager m_sensor_manager;
	Sensor m_light_sensor;
	
	int m_check_count = 0;
	TextView m_check_view;
	TextView m_display_view;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.light_sensor);
		
		//출력용 텍스트뷰
		m_check_view = (TextView)findViewById(R.id.check_tv);
		m_display_view = (TextView)findViewById(R.id.display_tv);
		
		//시스템 서비스로부터 SensorManager 객체를 얻는다.
		m_sensor_manager = (SensorManager)getSystemService(SENSOR_SERVICE);
		//SensorManager를 이용하여 조도센서 객체를 얻는다
		m_light_sensor = m_sensor_manager.getDefaultSensor(Sensor.TYPE_LIGHT);
		
	}
	
	//해당 액티비티가 포커스를 얻으면 조도 데이터를 얻을 수 있도록 리스너를 등록
	protected void onResume(){
		super.onResume();
		m_check_count = 0;
		
		//센서 값을 이 컨텍스트에 받아 볼 수 있도록 리스너를 등록
		m_sensor_manager.registerListener(this,m_light_sensor,SensorManager.SENSOR_DELAY_UI);
		
	}
	
	//해당 액티비티가 포커스를 잃으면 조도 데이터를 얻어도 소용이 없으므로 리스너를 해제
	protected void onPause(){
		super.onPause();
		
		//센서값이 필요하지 않는 시점에 리스너를 해제
		m_sensor_manager.unregisterListener(this);
	}


	@Override//정확도 변경시 호출되는 메소드, 센서의 경우 거의 호출되지 않음
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		//정확도가 낮은 측정값의 경우
		if(event.accuracy == SensorManager.SENSOR_STATUS_UNRELIABLE){
			//몇몇 기기의 경우 accuracy가 SENSOR_STATUS_UNRELIABLE 값을
			//가져서 측정값을 사용하지 못하는 경우가 있기 때문에 임의로 return을 막는다.
			//return;
		}
		//센서값을 측정한 센서의 종류가 조도센서인 경우
		if(event.sensor.getType() == Sensor.TYPE_LIGHT){
			m_check_count++;
			String str;
			//메소드 호출 횟수를 출력
			str = "체크 횟수 : " + m_check_count;
			m_check_view.setText(str);
			
			//데이터 값을 출력
			str = "현재 조도: " + event.values[0] + "lux";
			m_display_view.setText(str);			
		}
	}
}


