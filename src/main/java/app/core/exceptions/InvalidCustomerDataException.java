package app.core.exceptions;

public class InvalidCustomerDataException extends AdminServiceException {

	private static final long serialVersionUID = 1L;

	public InvalidCustomerDataException() {
		super();
	}

	public InvalidCustomerDataException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public InvalidCustomerDataException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidCustomerDataException(String message) {
		super(message);
	}

	public InvalidCustomerDataException(Throwable cause) {
		super(cause);
	}

	
	
}
