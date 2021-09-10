package app.core.exceptions;

public class CompanyNameUpdateException extends AdminServiceException {

	private static final long serialVersionUID = 1L;

	public CompanyNameUpdateException() {
		super();
	}

	public CompanyNameUpdateException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public CompanyNameUpdateException(String message, Throwable cause) {
		super(message, cause);
	}

	public CompanyNameUpdateException(String message) {
		super(message);
	}

	public CompanyNameUpdateException(Throwable cause) {
		super(cause);
	}

	
	
}
