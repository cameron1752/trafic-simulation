package com.biy.social.traffic_sim.config;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class MDCFilter implements Filter {

    private String MDCKey = "traceId";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        try{

            // Cast to HttpServletRequest to access HTTP-specific methods like getHeader
            HttpServletRequest httpRequest = (HttpServletRequest) request;

            String traceId = ((HttpServletRequest) request).getHeader("traceId");

            org.slf4j.MDC.put(MDCKey, traceId);
            chain.doFilter(request, response);
        } finally {
            org.slf4j.MDC.remove(MDCKey);
        }
    }
}
