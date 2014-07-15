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
		
		//��¿� �ؽ�Ʈ��
		m_check_view = (TextView)findViewById(R.id.check_tv);
		m_display_view = (TextView)findViewById(R.id.display_tv);
		
		//�ý��� ���񽺷κ��� SensorManager ��ü�� ��´�.
		m_sensor_manager = (SensorManager)getSystemService(SENSOR_SERVICE);
		//SensorManager�� �̿��Ͽ� �������� ��ü�� ��´�
		m_light_sensor = m_sensor_manager.getDefaultSensor(Sensor.TYPE_LIGHT);
		
	}
	
	//�ش� ��Ƽ��Ƽ�� ��Ŀ���� ������ ���� �����͸� ���� �� �ֵ��� �����ʸ� ���
	protected void onResume(){
		super.onResume();
		m_check_count = 0;
		
		//���� ���� �� ���ؽ�Ʈ�� �޾� �� �� �ֵ��� �����ʸ� ���
		m_sensor_manager.registerListener(this,m_light_sensor,SensorManager.SENSOR_DELAY_UI);
		
	}
	
	//�ش� ��Ƽ��Ƽ�� ��Ŀ���� ������ ���� �����͸� �� �ҿ��� �����Ƿ� �����ʸ� ����
	protected void onPause(){
		super.onPause();
		
		//�������� �ʿ����� �ʴ� ������ �����ʸ� ����
		m_sensor_manager.unregisterListener(this);
	}


	@Override//��Ȯ�� ����� ȣ��Ǵ� �޼ҵ�, ������ ��� ���� ȣ����� ����
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		//��Ȯ���� ���� �������� ���
		if(event.accuracy == SensorManager.SENSOR_STATUS_UNRELIABLE){
			//��� ����� ��� accuracy�� SENSOR_STATUS_UNRELIABLE ����
			//������ �������� ������� ���ϴ� ��찡 �ֱ� ������ ���Ƿ� return�� ���´�.
			//return;
		}
		//�������� ������ ������ ������ ���������� ���
		if(event.sensor.getType() == Sensor.TYPE_LIGHT){
			m_check_count++;
			String str;
			//�޼ҵ� ȣ�� Ƚ���� ���
			str = "üũ Ƚ�� : " + m_check_count;
			m_check_view.setText(str);
			
			//������ ���� ���
			str = "���� ����: " + event.values[0] + "lux";
			m_display_view.setText(str);			
		}
	}
}


