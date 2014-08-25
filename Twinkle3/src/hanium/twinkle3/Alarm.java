package hanium.twinkle3;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class Alarm extends Fragment {
	
	private ListView alarm_list = null;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState){
		View view = inflater.inflate(R.layout.fragment_alarm, container, false);
		//textview = (TextView)view.findViewById(R.id.textView1);
 
		alarm_list = (ListView)view.findViewById(R.id.alarm_list);
		alarm_list.setAdapter(MainActivity.m_adapter);
		return view;
	}
}
