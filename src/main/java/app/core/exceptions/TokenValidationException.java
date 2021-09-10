package app.core.exceptions;

public class TokenValidationException extends CouponSystemException {

	private static final long serialVersionUID = 1L;

	public TokenValidationException() {
		super();
	}

	public TokenValidationException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public TokenValidationException(String message, Throwable cause) {
		super(message, cause);
	}

	public TokenValidationException(String message) {
		super(message);
	}

	public TokenValidationException(Throwable cause) {
		super(cause);
	}

	
	
}
