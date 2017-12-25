package cn.gin.wctf.module.sys.listener;

import javax.servlet.ServletContext;

import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;

import cn.gin.wctf.common.config.Global;
import cn.gin.wctf.module.sys.service.SystemService;

/**
 * <p>用于监听 Spring 上下文加载事件，以便在 Spring ApplicationContext 加载前预先加载应用全局配置文件。</p>
 * 
 * @author Gintoki
 * @version 2017-10-10
 */
public class WebContextLoaderListener extends ContextLoaderListener {

	@Override
	public WebApplicationContext initWebApplicationContext(ServletContext servletContext) {
		if(!SystemService.loadProperties()) {
			return null;
		}
		String server = (String) servletContext.getAttribute("server");
		String root = (String) servletContext.getAttribute("root");
		if(root == null || "".equals(root)) {
			root = Global.getConfig("web.root.uri");
			servletContext.setAttribute("root", root);
		}
		if(server == null || "".equals(server)) {
			server = Global.getConfig("web.server");
			servletContext.setAttribute("server", server);
		}
		String realPath = servletContext.getRealPath("/");
		Global.putConfig("web.root", realPath);
		
		// 初始化 Spring 上下文
		return super.initWebApplicationContext(servletContext);
	}

	
}
