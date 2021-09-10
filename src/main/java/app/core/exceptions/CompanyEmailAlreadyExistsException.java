package app.core.exceptions;

public class CompanyEmailAlreadyExistsException extends AdminServiceException {

	private static final long serialVersionUID = 1L;

	public CompanyEmailAlreadyExistsException() {
		super();
	}

	public CompanyEmailAlreadyExistsException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public CompanyEmailAlreadyExistsException(String message, Throwable cause) {
		super(message, cause);
	}

	public CompanyEmailAlreadyExistsException(String message) {
		super(message);
	}

	public CompanyEmailAlreadyExistsException(Throwable cause) {
		super(cause);
	}

	
	
}
