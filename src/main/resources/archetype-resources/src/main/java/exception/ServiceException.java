package ${package}.exception;

/**
 * description: 业务发生异常
 *
 * @author Andy
 * @version 1.0
 * @date 07/11/2020 16:39
 */
public class ServiceException extends Exception {
	public ServiceException(String message, Throwable cause) {
		super(message, cause);
	}
}
