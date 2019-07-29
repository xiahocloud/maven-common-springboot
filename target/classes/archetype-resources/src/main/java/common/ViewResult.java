package ${package}.common;

/**
 * @author Andy
 * @version 1.0
 * @date 02/28/2019 16:40
 * @description 返回页面的数据内容
 */
public class ViewResult<T> {
	/**
	 * 状态码
	 **/
	private int code;
	/**
	 * 返回信息
	 **/
	private String msg;
	/**
	 * 返回数据
	 **/
	private Object data;

	private ViewResult() {

	}

	public static ViewResult instance() {
		return new ViewResult();
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public ViewResult code(int code) {
		this.code = code;
		return this;
	}

	public ViewResult msg(String msg) {
		this.msg = msg;
		return this;
	}

	public ViewResult<T> data(Object data) {
		if (data == null) {
			this.data = "";
		} else {
			this.data = data;
		}
		return this;
	}
}
