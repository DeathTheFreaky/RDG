package at.RDG.network;

public class UnableToStartConnectionExecption extends Exception {

	private static final long serialVersionUID = -9088223805398193343L;

	public UnableToStartConnectionExecption(){
		super();
	}
	
	public UnableToStartConnectionExecption(String msg){
		super(msg);
	}
	
	public UnableToStartConnectionExecption(Throwable ex){
		super(ex);
	}
	
	public UnableToStartConnectionExecption(String msg, Throwable ex){
		super(msg, ex);
	}
}
