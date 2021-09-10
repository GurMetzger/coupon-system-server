package app.core.exceptions;

public class InvalidCouponDataException extends CompanyServiceException {

	private static final long serialVersionUID = 1L;

	public InvalidCouponDataException() {
		super();
	}

	public InvalidCouponDataException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public InvalidCouponDataException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidCouponDataException(String message) {
		super(message);
	}

	public InvalidCouponDataException(Throwable cause) {
		super(cause);
	}

	
	
}
