package app.core.exceptions;

public class CredentialsException extends CouponSystemException {

	private static final long serialVersionUID = 1L;

	public CredentialsException() {
		super();
	}

	public CredentialsException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public CredentialsException(String message, Throwable cause) {
		super(message, cause);
	}

	public CredentialsException(String message) {
		super(message);
	}

	public CredentialsException(Throwable cause) {
		super(cause);
	}

	
	
}
