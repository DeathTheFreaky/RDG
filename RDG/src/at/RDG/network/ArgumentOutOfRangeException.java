package at.RDG.network;

public class ArgumentOutOfRangeException extends Exception {
	
	private static final long serialVersionUID = 7155848811526182777L;

	public ArgumentOutOfRangeException(){
		super();
	}
	
	public ArgumentOutOfRangeException(String msg){
		super(msg);
	}
	
	public ArgumentOutOfRangeException(Throwable ex){
		super(ex);
	}
	
	public ArgumentOutOfRangeException(String msg, Throwable ex){
		super(msg, ex);
	}
}
