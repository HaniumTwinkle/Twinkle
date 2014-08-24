package hanium.twinkle3;

import java.util.ArrayList;
import java.util.List;

import hanium.twinkle3.Bulb;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

public class BulbAdapter extends ArrayAdapter<Bulb>{
    // private ViewHolder viewHolder = null;
 	private LayoutInflater inflater = null;
 	private ArrayList<Bulb> items = null;
     private boolean[] isCheckedConfirm;
     private boolean[] isOnOffConfirm;
 	
     
		public BulbAdapter(Context context, int resource, ArrayList<Bulb> items) {
			super(context, resource, items);
			this.items = items;
			//checkbox의 체크 유뮤 확인용
			this.isCheckedConfirm = new boolean[items.size()];
			//on off 확인용(on==1 off==0)
			this.isOnOffConfirm = new boolean[items.size()];
				
			// TODO Auto-generated constructor stub
		}

		//체크박스를 모두 선택
     public void setAllChecked(boolean ischeked) {
         int tempSize = isCheckedConfirm.length;
         for(int i=0 ; i<tempSize ; i++){
             isCheckedConfirm[i] = ischeked;
         }
     }

     public void setChecked(int position) {
         isCheckedConfirm[position] = !isCheckedConfirm[position];
     }/*
     public ArrayList<Bulb> getChecked(){
         int tempSize = isCheckedConfirm.length;
         ArrayList<Bulb> mArrayList = new ArrayList<Bulb>();
         for(int b=0 ; b<tempSize ; b++){
             if(isCheckedConfirm[b]){
                 mArrayList.add(b);
             }
         }
         return mArrayList;
     }*/
     

     public int getCount() { 
         return items.size();
     }
     
     
		/*
 	@Override
		public View getView(int position, View convertView, ViewGroup parent){
			View v = convertView;
			if(v==null){
				LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
             v = vi.inflate(R.layout.message, null);
			}
			Bulb b = items.get(position);
			if(b != null){
				TextView name_text = (TextView)v.findViewById(R.id.bulb_name);
				TextView id_text = (TextView)v.findViewById(R.id.bulb_id);
				CheckBox check = (CheckBox)v.findViewById(R.id.checkBox1);
				
				if(name_text != null){
					name_text.setText(b.getName());
				}
				if(id_text != null){
					id_text.setText(b.getId());
				}
				//if(check != null){
				//}
				check.setClickable(false);
				check.setFocusable(false);
			}
			
			return v;
		}*/
     
 }