package cn.gin.wctf.common.util;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class HttpUtilsTest {

	@Test
	public void test() {
		Map<String, String> params = new HashMap<String, String>();
		params.put("callback", "loc");
		params.put("location", "47.350211612561,128.04106548154");
		params.put("output", "json");
		params.put("pois", "1");
		params.put("ak", "x4oKzLRB63EMPvwH3PHRYvOleaEiTd60");
		String res = HttpUtils.sendHttpGetRequest("http://api.map.baidu.com/geocoder/v2/", params);
		System.out.println(res);
	}

}
