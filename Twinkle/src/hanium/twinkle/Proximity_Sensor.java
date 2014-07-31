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
    // ���� ���� ��ü
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
        
        // ��¿� �ؽ�Ʈ�並 ��´�.
        m_check_view = (TextView) findViewById(R.id.check_tv);
        m_display_view = (TextView) findViewById(R.id.display_tv);
        
        // �ý��ۼ��񽺷κ��� SensorManager ��ü�� ��´�.
        m_sensor_manager = (SensorManager)getSystemService(SENSOR_SERVICE);
        // SensorManager �� �̿��ؼ� ���� ���� ��ü�� ��´�.
        m_sensor = m_sensor_manager.getDefaultSensor(Sensor.TYPE_PROXIMITY);

    }
    
    // �ش� ��Ƽ��Ƽ�� ���۵Ǹ� ���� �����͸� ���� �� �ֵ��� �����ʸ� ����Ѵ�.    
    protected void onStart() {
        super.onStart();

        m_check_count = 0;
        // ���� ���� �� ���ؽ�Ʈ���� �޾ƺ� �� �ֵ��� �����ʸ� ����Ѵ�.
        m_sensor_manager.registerListener(this, m_sensor, SensorManager.SENSOR_DELAY_UI );
    }

    // �ش� ��Ƽ��Ƽ�� ���߸� ���� �����͸� �� �ҿ��� �����Ƿ� �����ʸ� �����Ѵ�.
    protected void onStop() {
        super.onStop();
        // ���� ���� �ʿ����� �ʴ� ������ �����ʸ� �������ش�.
        m_sensor_manager.unregisterListener(this);
    }
    
    // ��Ȯ�� ����� ȣ��Ǵ� �޼ҵ�. ������ ��� ���� ȣ����� �ʴ´�.
    public void onAccuracyChanged(Sensor sensor, int accuracy) 
    {     
    }
    
    // ������ ���� �������ִ� �޼ҵ�. 
    public void onSensorChanged(SensorEvent event) 
    {
        // ��Ȯ���� ���� �������� ���
        if (event.accuracy == SensorManager.SENSOR_STATUS_UNRELIABLE) {
            // ��� ����� ��� accuracy �� SENSOR_STATUS_UNRELIABLE ����
            // ������ �������� ������� ���ϴ� ��찡 �ֱ⶧���� ���Ƿ� return ; �� ���´�.
            //return;
        }
        // �������� ������ ������ ������ ���� ������ ���
        if(event.sensor.getType() == Sensor.TYPE_PROXIMITY) {
            m_check_count++;
            String str;

            // �ش� ������ ��ȯ���ִ� �ִ밪�� �޼ҵ� ȣ�� Ƚ���� ����Ѵ�.
            str = "�ִ�ġ : " + event.sensor.getMaximumRange() + ", üũ Ƚ�� : " + m_check_count;
            m_check_view.setText(str);
            // ������ ���� ����Ѵ�.
            str = "���� �Ÿ� : " + event.values[0];
            m_display_view.setText(str);
        }
    }
}

