package cn.gin.wctf.common.ansy;

import java.util.concurrent.ThreadFactory;

/**
 * <p>用于异步任务管理器创建守护线程的线程工厂，所有需要交由异步管理器的线程都需要此工厂来创建，因为提交到异步任务管理器中的
 * 任务默认都是优先级较低的守护任务。</p>
 * 
 * @author Gintoki
 * @version 2017-11-20
 */
public class DaemonThreadFactory implements ThreadFactory {

	/**
	 * The default root logger.
	 */
	private static final ThreadFactory factory = new DaemonThreadFactory();
	
	private DaemonThreadFactory() {}
	
	/**
	 * <p>获取线程工厂的实例，使用静态工厂实现单例模式。</p>
	 * 
	 * @return <code>DaemonThreadFactory</code> 的唯一实例
	 */
	public static ThreadFactory getInstance() {
		return factory;
	}

	/**
	 * <p>使用线程工厂创建一个 Thread 对象，并将其设置为守护线程，用于在后台运行低优先级的异步任务。</p>
	 */
	public Thread newThread(Runnable r) {
		Thread t = new Thread(r);
		t.setDaemon(true);
		t.setPriority(AnsyTaskManager.DEFAULT_PRIORITY);
		return t;
	}
}
