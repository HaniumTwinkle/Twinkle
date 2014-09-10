package hanium.twinkle3;

public class Bulb{
	private boolean Check;
	private String Name;
	private String Id;
	private String Status;
	private int IntStatus;
	private boolean OnOff;
	
	public Bulb(boolean _Check, String _Name, String _Id, String _Status){
		Check = _Check;
		Name = _Name;
		Id	= _Id;
		Status = _Status;
		IntStatus = Integer.parseInt(_Status);
		OnOff = false;
	}
	
	public String getName(){
		return Name;
	}

	public String getId(){
		return Id;
	}
	
	public boolean getCheck(){
		return Check;
	}
	
	public String getStatus(){
		return Status;
	}
	
	public int getIntStatus(){
		return IntStatus;
	}
	
	public void setCheck(boolean _Check){
		Check = _Check;
	}
	
	public void setStatus(String _Status){
		Status = _Status;
		setIntStatus(Integer.parseInt(_Status));
	}
	
	public void setIntStatus(int _Int){
		IntStatus = _Int;
	}
	
	public void setOnOff(boolean onoff){
		OnOff = onoff;		
	}
	
	public boolean getOnOff(){
		return OnOff;
	}
}