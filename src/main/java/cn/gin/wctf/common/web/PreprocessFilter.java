package cn.gin.wctf.common.web;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.common.net.HttpHeaders;

public class PreprocessFilter implements Filter {

    @Override
	public void destroy() {
        // TODO Auto-generated method stub

    }

    @Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        // 1. Set the encoding.
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");

        // 2. Allow across the global scope.
        resp.addHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "*");

        chain.doFilter(req, resp);
    }

    @Override
	public void init(FilterConfig arg0) throws ServletException {
        // TODO Auto-generated method stub

}

}
