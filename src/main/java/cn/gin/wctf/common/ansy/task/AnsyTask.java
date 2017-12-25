package cn.gin.wctf.common.ansy.task;

import cn.gin.wctf.common.ansy.status.AnsyTaskStatus;
import cn.gin.wctf.common.util.IdentityGenerator;

/**
 * <p>异步任务顶层抽象类。</p>
 * 
 * @author Gintoki
 * @version 2017-11-20
 */ 
public abstract class AnsyTask {
	
	/**
	 * 任务的唯一键
	 */
	private String id;
	
	/**
	 * 任务的描述信息
	 */
	private String describe;
	
	/**
	 * 设置异步任务的超时时间
	 */
	private long timeout;
	
	/**
	 * 异步任务状态
	 */
	private AnsyTaskStatus status = new AnsyTaskStatus();
	
	/**
	 * 默认异步任务构造方法，使用 UUID 生成默认的任务 id
	 */
	public AnsyTask() {
		this.id = IdentityGenerator.uuid();
	}
	
	public String getId() {
		return id;
	}

	public String getDescribe() {
		return describe;
	}

	public void setDescribe(String describe) {
		this.describe = describe;
	}

	public long getTimeout() {
		return timeout;
	}

	public void setTimeout(long timeout) {
		this.timeout = timeout;
	}

	public AnsyTaskStatus getStatus() {
		if(status == null) {
			status = new AnsyTaskStatus();
		}
		return status;
	}

	public void setStatus(AnsyTaskStatus status) {
		this.status = status;
	}
	
	@Override
	public String toString() {
		return "AnsyTask [id=" + id + ", describe=" + describe + "]";
	}
}
