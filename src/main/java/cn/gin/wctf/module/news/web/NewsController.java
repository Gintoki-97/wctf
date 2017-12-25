package cn.gin.wctf.module.news.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.gin.wctf.common.util.JedisUtils;

/**
 * <p>发贴控制器</p>
 * 
 * @author Gintoki
 * @version 2017-10-04
 */
@Controller
public class NewsController {

	@RequestMapping("/news/jedis")
	public void testJedis(String name, String value) {
		JedisUtils.set("wctf.test.jedis", "hello.world", 0);
	}
	
	
}
