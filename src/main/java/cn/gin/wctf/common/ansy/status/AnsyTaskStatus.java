package cn.gin.wctf.common.ansy.status;

/**
 * <p>异步任务状态描述类</p>
 * 
 * @author Gintoki
 * @version 2017-11-20
 */
public class AnsyTaskStatus {

	/**
	 * 当前任务的状态条件
	 */
	private AnsyTaskStatusCondition condition;
	
	public AnsyTaskStatus() {
		this.condition = AnsyTaskStatusCondition.WAITTING;
	}
	
	public AnsyTaskStatusCondition getCondition() {
		return condition;
	}

	public void setCondition(AnsyTaskStatusCondition condition) {
		this.condition = condition;
	}

	/**
	 * 返回当前任务是否已经完成
	 */
	public boolean isCompleted() {
		return condition == AnsyTaskStatusCondition.SUCCESS ||
				condition == AnsyTaskStatusCondition.FAILD;
	}
}
