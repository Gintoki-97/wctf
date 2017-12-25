package cn.gin.wctf.module.sys.util;

import java.util.Date;

import org.springframework.core.convert.converter.Converter;

import cn.gin.wctf.common.util.DateUtils;

/**
 * <p>Springmvc 日期类型参数绑定的转换器，用于将普通字符串形式的在参数绑定过程中自动转为 java.util.Date 类型。
 * 当前转换器支持的文本日起字符串格式为：yyyy年MM月dd日，此种类型的字符串请求参数绑定到日期对象时，均会使用当前转换器。</p>
 * 
 * @author Gintoki
 * @version 2017-11-20
 */
public class SimpleDateConverter implements Converter<String, Date> {

	public Date convert(String source) {
		try {
			return DateUtils.parseDate(source, "yyyy年MM月dd日");
		} catch(NumberFormatException e) {
			e.printStackTrace();
		}
		return null;
	}

}
