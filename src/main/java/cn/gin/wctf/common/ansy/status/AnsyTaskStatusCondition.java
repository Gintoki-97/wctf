package cn.gin.wctf.common.ansy.status;

/**
 * 异步任务的状态
 * @author Gintoki
 * @version 2017-11-20
 */
public enum AnsyTaskStatusCondition {

	/**
	 * 任务是新建状态
	 */
	NEW,
	
	/**
	 * 任务在任务队列里等待执行
	 */
	WAITTING,
	
	/**
	 * 任务处于执行状态
	 */
	RUNNING,
	
	/**
	 * 执行成功
	 */
	SUCCESS,
	
	/**
	 * 执行失败
	 */
	FAILD,
	
	/**
	 * 任务超时
	 */
	TIMEOUT;
}
