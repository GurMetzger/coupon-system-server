package app.core.exceptions;

public class InvalidCompanyDataException extends AdminServiceException {

	private static final long serialVersionUID = 1L;

	public InvalidCompanyDataException() {
		super();
	}

	public InvalidCompanyDataException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public InvalidCompanyDataException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidCompanyDataException(String message) {
		super(message);
	}

	public InvalidCompanyDataException(Throwable cause) {
		super(cause);
	}

	
	
}
