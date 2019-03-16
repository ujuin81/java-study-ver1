package me.ujuin81.user.sqlservice;

public class SqlRetrievalFailureException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	public SqlRetrievalFailureException() {
		super();
	}

	public SqlRetrievalFailureException(String message) {
		super(message);
	}
	
	public SqlRetrievalFailureException(Throwable cause) {
		super(cause);
	}
	
	public SqlRetrievalFailureException(String message, Throwable cause) {
		super(message, cause);
	}

}
