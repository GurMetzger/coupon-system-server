package app.core.exceptions;

public class CompanyNameAlreadyExistsException extends AdminServiceException {

	private static final long serialVersionUID = 1L;

	public CompanyNameAlreadyExistsException() {
		super();
	}

	public CompanyNameAlreadyExistsException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public CompanyNameAlreadyExistsException(String message, Throwable cause) {
		super(message, cause);
	}

	public CompanyNameAlreadyExistsException(String message) {
		super(message);
	}

	public CompanyNameAlreadyExistsException(Throwable cause) {
		super(cause);
	}

	
	
}
