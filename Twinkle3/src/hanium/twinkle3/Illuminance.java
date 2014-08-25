package hanium.twinkle3;





import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
public class Illuminance extends Fragment {
	
	public static ListView illu_list = null;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState){
		View view = inflater.inflate(R.layout.fragment_illuminance, container, false);
		//textview = (TextView)view.findViewById(R.id.textView1);
 
		illu_list = (ListView)view.findViewById(R.id.illuminance_list);
		illu_list.setAdapter(MainActivity.m_adapter);
		
		return view;
	}

	
}
