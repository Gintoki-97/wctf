package cn.gin.wctf.module.sys.entity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

/**
 * <p>应用（通常是服务器端）与外部（通常是客户端）或者应用内部（通常是三层架构之间）进行数据流通的载体，服务器对
 * 客户端进行非直接跳转的响应时，使用此实体对象进行业务数据的传递。在文档中我会将其称为 “应用数据流载体” 。</p>
 * <p>使用者在使用此对象的之前，应特别注意此对象的 status 字段，status 标识了程序返回的状态码。这是程序运行时
 * 出现的所有情况的总标识。建议使用者在编写程序之前，先根于业务需求建立好与 status 字段相对应的方法返回值文档。</p>
 * 
 * @author Gintoki
 * @version 2017-11-26
 */
public class ApplicationDataSupport implements Serializable {

	/**
	 * The default serilizable id.
	 */
	private static final long serialVersionUID = -4881786442157155915L;
	
	private boolean success;			// 程序是否执行成功
	private int status;					// 程序执行结果对应的状态码
	private String msg;					// 程序返回的消息
	private Map<String, Object> param;	// 程序中可能会用到的参数集
	
	public ApplicationDataSupport() {}
	
	public ApplicationDataSupport(boolean success) {
		this.success = success;
	}

	public ApplicationDataSupport(boolean success, int status, String msg) {
		super();
		this.success = success;
		this.status = status;
		this.msg = msg;
	}
	
	public ApplicationDataSupport(boolean success, int status, String msg, Map<String, Object> param) {
		super();
		this.success = success;
		this.status = status;
		this.msg = msg;
		this.param = param;
	}
	
	public ApplicationDataSupport(Builder builder) {
		this.success = builder.success;
		this.status = builder.status;
		this.msg = builder.msg;
		this.param = builder.param;
	}

	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		setSuccess((status / 10) * 10 == status);
		this.status = status;
		if(status == 12) {
			setMsg("登录状态异常");
		}
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public Map<String, Object> getParam() {
		if(param == null)
			param = new HashMap<String, Object> ();
		return param;
	}
	public void setParam(Map<String, Object> param) {
		this.param = param;
	}
	
	// -------------------------
	// Support
	// -------------------------
	
	/**
	 * <p>用于创建 ApplicationDataSupport 对象的构建器。</p>
	 * <p>因为 ApplicationDataSupport 的参数较多，使用 JavaBean 模式会造成大量的 set 语句堆积，代码比较冗杂。
	 * 使用构建器的构造链可以简化代码，而且使得创建对象的过程更加安全。</p>
	 * 
	 * @author Gintoki
	 * @version 2017-11-26
	 */
	public static class Builder {
		
		private boolean success;			// 程序是否执行成功
		private int status;					// 程序执行结果对应的状态码
		private String msg;					// 程序返回的消息
		private Map<String, Object> param;	// 程序中可能会用到的参数集
		
		public Builder() {}
		
		public Builder success(boolean success) {
			this.success = success;
			return this;
		}
		
		public Builder status(int status) {
			this.status = status;
			return this;
		}
		
		public Builder msg(String msg) {
			this.msg = msg;
			return this;
		}
		
		public Builder param(Map<String, Object> param) {
			this.param = param;
			return this;
		}
		
		public ApplicationDataSupport build() {
			return new ApplicationDataSupport(this);
		}
		
	}
	
	/**
	 * 将 JSON 格式的字符串转换为应用数据流载体。
	 * 
	 * @param json - 当前外部流对象对应的 JSON 格式的字符串
	 * @return 应用数据流载体
	 */
	public static ApplicationDataSupport fromJson(String json) {
		ApplicationDataSupport data = null;
		try {
			data = new Gson().fromJson(json, ApplicationDataSupport.class);
		} catch(JsonSyntaxException e) {
			e.printStackTrace();
		}
		return data;
	}
	
	// -------------------------
	// Service
	// -------------------------
	
	/**
	 * 向应用数据流载体中添加一个键值对格式的参数。
	 * 
	 * @param key - 参数名
	 * @param value - 参数值
	 */
	public void setParameter(String key, Object value) {
		this.getParam().put(key, value);
	}
	
	/**
	 * 从应用数据流载体中获取与指定键对应的参数值。
	 * 
	 * @param key - 参数名
	 * @return 外部数据流通对象中与指定键对应的参数值
	 */
	@SuppressWarnings("unchecked")
	public <T> T getParameter(String key) {
		return (T) this.getParam().get(key);
	}
	
	/**
	 * 清除所有参数信息
	 */
	public void clearParams() {
		this.getParam().clear();
	}
	
	/**
	 * 将应用数据流载体转换为 JSON 格式的字符串。
	 * 
	 * @return 当前外部流对象对应的 JSON 格式的字符串
	 */
	public String toJson() {
		return new Gson().toJson(this);
	}
	
	@Override
	public String toString() {
		return "ExternalDataSupport [success=" + success + ", status=" + status + ", msg=" + msg + ", param=" + param + "]";
	}

}
