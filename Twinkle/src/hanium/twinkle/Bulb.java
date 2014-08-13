package hanium.twinkle;

public class Bulb{
	private String Name;
	private String Id;
	private boolean Check;
	
	public Bulb(boolean _Check, String _Name, String _Id){
		this.Name = _Name;
		this.Id	= _Id;
		this.Check = _Check;
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
}