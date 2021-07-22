package com.hourfun.cashexchange.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;

import com.hourfun.cashexchange.handler.ReadableRequestBodyWrapper;

import java.io.IOException;

public class RequestBodyLoggingFilter implements Filter {

    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            ReadableRequestBodyWrapper wrapper = new ReadableRequestBodyWrapper((HttpServletRequest) request);
            wrapper.setAttribute("requestBody", wrapper.getRequestBody());
            chain.doFilter(wrapper, response);
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    @Override
    public void init(FilterConfig filterConfig) {
    }

}

