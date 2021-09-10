package app.core.exceptions;

public class DatabaseMalfunctionCompanyException extends CompanyServiceException {

	private static final long serialVersionUID = 1L;

	public DatabaseMalfunctionCompanyException() {
		super();
	}

	public DatabaseMalfunctionCompanyException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public DatabaseMalfunctionCompanyException(String message, Throwable cause) {
		super(message, cause);
	}

	public DatabaseMalfunctionCompanyException(String message) {
		super(message);
	}

	public DatabaseMalfunctionCompanyException(Throwable cause) {
		super(cause);
	}

	
	
}
