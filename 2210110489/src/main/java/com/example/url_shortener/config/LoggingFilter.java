package com.example.url_shortener.config;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;

@Component
public class LoggingFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        long start = System.currentTimeMillis();
        String method = req.getMethod();
        String uri = req.getRequestURI();
        String query = (req.getQueryString() != null) ? "?" + req.getQueryString() : "";

        // Before request
        System.out.printf("[%s] 🚦 Received a new %s request for: %s%s%n",
                Instant.now(), method, uri, query);

        chain.doFilter(request, response);

        // After response
        long duration = System.currentTimeMillis() - start;
        int status = res.getStatus();
        System.out.printf("[%s] ✅ Request to %s%s finished with status %d in %d ms.%n",
                Instant.now(), uri, query, status, duration);
    }

    @Override
    public void destroy() {
    }
}
