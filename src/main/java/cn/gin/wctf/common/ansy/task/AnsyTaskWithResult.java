package cn.gin.wctf.common.ansy.task;

/**
 * <p>带有返回值的异步任务</p>
 * 
 * @author Gintoki
 * @version 2017-11-20
 */
public abstract class AnsyTaskWithResult<T> extends AnsyTask {

	/**
	 * <p>当前任务的逻辑代码实现，都应该放到此方法中。此方法会由全局的异步任务管理器执行。</p>
	 * 
	 * @return 此类任务会产生一个返回值，并交由回调函数处理此返回值
	 */
	public abstract T execute();
	
	/**
	 * <p>在异步任务管理器执行过当前 {@link AnsyTaskWithResult} 之后，会将此任务的 execute() 方法的返回值传递给回调函数并调用回调函数处理返回结果。</p>
	 * 
	 * @param res - 当前任务执行的返回值
	 */
	public abstract void callback(T res);

}
