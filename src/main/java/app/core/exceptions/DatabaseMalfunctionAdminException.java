package app.core.exceptions;

public class DatabaseMalfunctionAdminException extends AdminServiceException {

	private static final long serialVersionUID = 1L;

	public DatabaseMalfunctionAdminException() {
		super();
	}

	public DatabaseMalfunctionAdminException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public DatabaseMalfunctionAdminException(String message, Throwable cause) {
		super(message, cause);
	}

	public DatabaseMalfunctionAdminException(String message) {
		super(message);
	}

	public DatabaseMalfunctionAdminException(Throwable cause) {
		super(cause);
	}

	
	
}
