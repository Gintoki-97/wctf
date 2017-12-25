package cn.gin.wctf.module.sys.entity;

/**
 * <p>用户动态的实体支持类，所有可以被插入到动态表中的实体对象数据，都需要实现此接口。</p>
 * 
 * @author Gintoki
 * @version 2017-12-24
 */
public interface TrendSupport {

	/**
	 * <p>对于所有实现了此接口的方法，程序都允许它们被插入到数据库中用户的动态表里，但该对象也必须提供一个将自己本身转换为
	 * 一条用户动态信息的方法。当前方法即为二者进行转化的规定。</p>
	 * @return
	 */
	Trend buildTrend();
}
