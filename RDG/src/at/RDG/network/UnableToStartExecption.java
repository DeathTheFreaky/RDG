package at.RDG.network;

public class UnableToStartExecption extends Exception {

	private static final long serialVersionUID = -9088223805398193343L;

	public UnableToStartExecption(){
		super();
	}
	
	public UnableToStartExecption(String msg){
		super(msg);
	}
	
	public UnableToStartExecption(Throwable ex){
		super(ex);
	}
	
	public UnableToStartExecption(String msg, Throwable ex){
		super(msg, ex);
	}
}
