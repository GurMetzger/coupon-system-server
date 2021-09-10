package app.core.exceptions;

public class CustomerEmailAlreadyExistsException extends AdminServiceException {

	private static final long serialVersionUID = 1L;

	public CustomerEmailAlreadyExistsException() {
		super();
	}

	public CustomerEmailAlreadyExistsException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public CustomerEmailAlreadyExistsException(String message, Throwable cause) {
		super(message, cause);
	}

	public CustomerEmailAlreadyExistsException(String message) {
		super(message);
	}

	public CustomerEmailAlreadyExistsException(Throwable cause) {
		super(cause);
	}

	
	
}
