package cn.gin.wctf.common.ansy;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.gin.wctf.common.ansy.status.AnsyTaskStatusCondition;
import cn.gin.wctf.common.ansy.task.AnsyTask;
import cn.gin.wctf.common.ansy.task.AnsyTaskWithNoResult;
import cn.gin.wctf.common.ansy.task.AnsyTaskWithResult;

/**
 * <p>应用中的异步任务管理器，用于执行非主要业务逻辑且相对耗时较长的异步任务。此管理器的服务范围是相对于整个 WEB 应用的，
 * 因此它被设计为单例模式而不能初始化。</p>
 * <p>应用启动时，程序会创建两个守护线程，一个是异步任务执行器，用于执行那些逻辑上优先级较低的任务；另一个是异步任务后处理器，
 * 用于执行异步任务的回调函数。两个线程在没有任务使均会阻塞直到有任务添加到任务队列中。</p>
 * 
 * @author Gintoki
 * @version 2017-11-20
 */
public class AnsyTaskManager {

	/**
	 * The default root logger.
	 */
	private final Logger logger = LoggerFactory.getLogger(AnsyTaskManager.class);
	
	/**
	 * 默认异步任务管理器线程的优先级
	 */
	static final int DEFAULT_PRIORITY = 1;
	
	/**
	 * 默认初始化时线程池的大小
	 */
	static final int DEFAULT_INITIAL_CAPACITY = 1 << 3;
	
	/**
	 * 默认支持的最大任务数
	 */
	static final int DEFAULT_SUPPORT_CAPACITY = 1 << 5;
	
	/**
	 * 任务队列支持
	 */
	private final Set<String> processKeys = new HashSet<String>(DEFAULT_SUPPORT_CAPACITY);
	
	/**
	 * 任务队列
	 */
	private final LinkedBlockingQueue<AnsyTask> processQueue = new LinkedBlockingQueue<AnsyTask>(DEFAULT_SUPPORT_CAPACITY);
	
	/**
	 *后处理队列支持
	 */
	private final Set<String> postProcessKeys = new HashSet<String>(DEFAULT_SUPPORT_CAPACITY);
	
	/**
	 * 后处理队列
	 */
	private final LinkedBlockingQueue<Runnable> postProcessQueue = new LinkedBlockingQueue<Runnable>(DEFAULT_SUPPORT_CAPACITY);
	
	/**
	 * 异步任务执行器，使用线程池实现
	 */
	private final ExecutorService processor = Executors.newFixedThreadPool(DEFAULT_INITIAL_CAPACITY, DaemonThreadFactory.getInstance());
	
	/**
	 * 异步任务后置处理器
	 */
	private final ExecutorService postProcessor = Executors.newFixedThreadPool(DEFAULT_INITIAL_CAPACITY, DaemonThreadFactory.getInstance());;
	
	/**
	 * Singleton instance of AnsyTaskManager
	 */
	private static final AnsyTaskManager manager = new AnsyTaskManager();
	
	/**
	 * The AnsyTaskManager is no public constructor
	 */
	private AnsyTaskManager() {}
	
	/**
	 * The static factory method of AnsyTaskManager
	 * 
	 * @return Singleton instance of AnsyTaskManager
	 */
	public static AnsyTaskManager getAnsyTaskManager() {
		return manager;
	}
	
	
	/*
	 * 构造代码块，开启异步任务管理器的守护线程
	 * */
	{
		if(processor != null) {
			Thread processScheduledThread = new Thread(new Runnable() {
				public void run() {
					while(true) {
						synchronized (processor) {
							if(processQueue.isEmpty()) {
								try {
									processor.wait();
								} catch (InterruptedException e1) {
									e1.printStackTrace();
								}
							} else {
								scheduledTask();
							}
						}
					}
				}
			});
			processScheduledThread.setName("ANSYTASK-PROCESSOR");
			processScheduledThread.setDaemon(true);
			processScheduledThread.setPriority(DEFAULT_PRIORITY);
			if(logger.isDebugEnabled()) {
				logger.debug("{} start, waitting for ansy task post process", processScheduledThread.toString());
			}
			processScheduledThread.start();
		}
		if(postProcessor != null) {
			Thread postProcessScheduledThread = new Thread(new Runnable() {
				public void run() {
					while(true) {
						synchronized (postProcessor) {
							if(postProcessQueue.isEmpty()) {
								try {
									postProcessor.wait();
								} catch (InterruptedException e1) {
									e1.printStackTrace();
								}
							} else {
								scheduledTaskPostProcessor();
							}
						}
					}
				}
			});
			postProcessScheduledThread.setName("ANSYTASK-POST-PROCESSOR");
			postProcessScheduledThread.setDaemon(true);
			postProcessScheduledThread.setPriority(DEFAULT_PRIORITY);
			if(logger.isDebugEnabled()) {
				logger.debug("{} start, waitting for ansy task post process", postProcessScheduledThread.toString());
			}
			postProcessScheduledThread.start();
		}
	}
	
	/**
	 * 从任务队列里进行任务调度
	 */
	private <T> void scheduledTask() {
		while(!processQueue.isEmpty()) {
			final AnsyTask task = processQueue.remove();
			if(task instanceof AnsyTaskWithResult) {
				@SuppressWarnings("unchecked")
				final AnsyTaskWithResult<T> t = (AnsyTaskWithResult<T>) task;
				if(logger.isDebugEnabled()) {
					logger.debug("Scheduled execute AnsyTaskWithResult {} from AnsyTaskQueue.", t.toString());
				}
				final Future<T> process = processor.submit(new Callable<T>() {
					public T call() {
						try {
							t.getStatus().setCondition(AnsyTaskStatusCondition.RUNNING);
							System.out.println("Thread.currentThread().isDaemon()" + Thread.currentThread().isDaemon());
							T res = t.execute();
							t.getStatus().setCondition(AnsyTaskStatusCondition.SUCCESS);
							return res;
						} catch (Exception e) {
							t.getStatus().setCondition(AnsyTaskStatusCondition.FAILD);
							e.printStackTrace();
						}
						return null;
					}
				});
				postProcessQueue.add(new Runnable() {
					public void run() {
						String tId = t.getId();
						try {
							T res = process.get();
							if(logger.isDebugEnabled()) {
								logger.debug("Execute AnsyTaskWithResult {} finished, start post process.", t.toString());
							}
							processKeys.remove(tId);
							postProcessKeys.add(tId);
							System.out.println("Thread.currentThread().isDaemon()" + Thread.currentThread().isDaemon());
							t.callback(res);
							postProcessKeys.remove(tId);
							t.getStatus().setCondition(AnsyTaskStatusCondition.SUCCESS);
						} catch (Exception e) {
							t.getStatus().setCondition(AnsyTaskStatusCondition.FAILD);
							e.printStackTrace();
						}
					}
				});
				synchronized(postProcessor) {
					postProcessor.notifyAll();
				}
			} else {
				final AnsyTaskWithNoResult t = (AnsyTaskWithNoResult) task;
				processor.execute(new Runnable() {
					public void run() {
						try {
							if(logger.isDebugEnabled()) {
								logger.debug("Scheduled execute AnsyTask {} from AnsyTaskQueue.", t.toString());
							}
							t.getStatus().setCondition(AnsyTaskStatusCondition.RUNNING);
							System.out.println("Thread.currentThread().isDaemon()" + Thread.currentThread().isDaemon());
							t.execute();
							t.getStatus().setCondition(AnsyTaskStatusCondition.SUCCESS);
						} catch (Exception e) {
							t.getStatus().setCondition(AnsyTaskStatusCondition.FAILD);
							e.printStackTrace();
						}
					}
				});
			}
		}
	}
	
	/**
	 * 从异步任务后处理队列中调度任务
	 */
	private void scheduledTaskPostProcessor() {
		while(!postProcessQueue.isEmpty()) {
			final Runnable r = postProcessQueue.remove();
			postProcessor.execute(r);
		}
	}
	
	/**
	 * 向任务管理器里加入一项任务，此项任务交由异步任务管理器顺序执行
	 */
	public void submit(AnsyTask task) {
		if(task == null || task.getId() == null) {
			throw new IllegalArgumentException("Ansy task can not be null: " + task);
		}
		synchronized (processor) {
			boolean exists = processKeys.contains(task.getId());
			if(!exists) {
				processKeys.add(task.getId());
				task.getStatus().setCondition(AnsyTaskStatusCondition.WAITTING);
				processQueue.add(task);
				processor.notifyAll();
			} else {
				if(logger.isDebugEnabled()) {
					logger.debug("Ansy task is exists: " + task);
				}
			}
		}
	}
	
	/**
	 * 判断当前管理器是否可以接收新的任务
	 * @return
	 */
	public boolean isOccupied() {
		return processKeys.size() >= 0 && processKeys.size() < DEFAULT_SUPPORT_CAPACITY;
	}
	
}
