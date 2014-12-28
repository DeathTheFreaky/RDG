package at.RDG.network;

public class UnableToStartConnectionException extends Exception {

	private static final long serialVersionUID = -9088223805398193343L;

	public UnableToStartConnectionException(){
		super();
	}
	
	public UnableToStartConnectionException(String msg){
		super(msg);
	}
	
	public UnableToStartConnectionException(Throwable ex){
		super(ex);
	}
	
	public UnableToStartConnectionException(String msg, Throwable ex){
		super(msg, ex);
	}
}
