package me.ujuin81.user.sqlservice;

public class SqlRetrievalFailureException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public SqlRetrievalFailureException(String message) {
		super(message);
	}
	
	public SqlRetrievalFailureException(String message, Throwable cause) {
		super(message, cause);
	}
}
