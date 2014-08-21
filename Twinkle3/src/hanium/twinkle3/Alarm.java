package hanium.twinkle3;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class Alarm extends Fragment {

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState){
		View view = inflater.inflate(R.layout.fragment_alarm, container, false);
		//textview = (TextView)view.findViewById(R.id.textView1);
		return view;
	}
}
