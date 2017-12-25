package cn.gin.wctf.common.ansy.task;

/**
 * <p>没有结果集的异步任务类</p>
 * 
 * @author Gintoki
 * @version 2017-11-20
 */
public abstract class AnsyTaskWithNoResult extends AnsyTask {

	/**
	 * <p>当前任务的逻辑代码实现，都应该放到此方法中。此方法会由全局的异步任务管理器执行</p>
	 */
	public abstract void execute();
}
