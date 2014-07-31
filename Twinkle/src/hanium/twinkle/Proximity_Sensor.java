/**
 * 
 */
package hanium.twinkle;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

public class Proximity_Sensor extends Activity implements SensorEventListener
{    
    // 센서 관련 객체
    SensorManager m_sensor_manager;
    Sensor m_sensor;
    
    int m_check_count = 0;
    TextView m_check_view;
    TextView m_display_view;

    
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.proximity_sensor);
        
        // 출력용 텍스트뷰를 얻는다.
        m_check_view = (TextView) findViewById(R.id.check_tv);
        m_display_view = (TextView) findViewById(R.id.display_tv);
        
        // 시스템서비스로부터 SensorManager 객체를 얻는다.
        m_sensor_manager = (SensorManager)getSystemService(SENSOR_SERVICE);
        // SensorManager 를 이용해서 근접 센서 객체를 얻는다.
        m_sensor = m_sensor_manager.getDefaultSensor(Sensor.TYPE_PROXIMITY);

    }
    
    // 해당 액티비티가 시작되면 근접 데이터를 얻을 수 있도록 리스너를 등록한다.    
    protected void onStart() {
        super.onStart();

        m_check_count = 0;
        // 센서 값을 이 컨텍스트에서 받아볼 수 있도록 리스너를 등록한다.
        m_sensor_manager.registerListener(this, m_sensor, SensorManager.SENSOR_DELAY_UI );
    }

    // 해당 액티비티가 멈추면 근접 데이터를 얻어도 소용이 없으므로 리스너를 해제한다.
    protected void onStop() {
        super.onStop();
        // 센서 값이 필요하지 않는 시점에 리스너를 해제해준다.
        m_sensor_manager.unregisterListener(this);
    }
    
    // 정확도 변경시 호출되는 메소드. 센서의 경우 거의 호출되지 않는다.
    public void onAccuracyChanged(Sensor sensor, int accuracy) 
    {     
    }
    
    // 측정한 값을 전달해주는 메소드. 
    public void onSensorChanged(SensorEvent event) 
    {
        // 정확도가 낮은 측정값인 경우
        if (event.accuracy == SensorManager.SENSOR_STATUS_UNRELIABLE) {
            // 몇몇 기기의 경우 accuracy 가 SENSOR_STATUS_UNRELIABLE 값을
            // 가져서 측정값을 사용하지 못하는 경우가 있기때문에 임의로 return ; 을 막는다.
            //return;
        }
        // 센서값을 측정한 센서의 종류가 근접 센서인 경우
        if(event.sensor.getType() == Sensor.TYPE_PROXIMITY) {
            m_check_count++;
            String str;

            // 해당 센서가 반환해주는 최대값과 메소드 호출 횟수를 출력한다.
            str = "최대치 : " + event.sensor.getMaximumRange() + ", 체크 횟수 : " + m_check_count;
            m_check_view.setText(str);
            // 데이터 값을 출력한다.
            str = "현재 거리 : " + event.values[0];
            m_display_view.setText(str);
        }
    }
}

