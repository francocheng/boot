package com.gdczwlkj.common;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Stream;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

@Component
public class MDCFilter implements Filter {

    private Logger logger = LoggerFactory.getLogger(this.getClass());


    private Instant begin = null;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
        ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String uri = httpRequest.getRequestURI();
        if (uri.indexOf(".json") != -1 || uri.indexOf(".html") != -1){
            chain.doFilter(request, response);
        } else {
            begin = Instant.now();
            String traceId = httpRequest.getHeader("traceId");

            if (traceId == null) {
                traceId = TraceIdGenerator.create(null);
            }
            MDC.put("traceId", traceId);
            httpResponse.setHeader("traceId", traceId);

            try {
                chain.doFilter(request, response);
            } finally {
                String token = "";
                Cookie[] cookies = httpRequest.getCookies();
                String userId = "";
                if (cookies != null) {
                    userId = Stream.of(cookies).filter(cookie -> "userId".equals(cookie.getName())).findFirst()
                        .map(Cookie::getValue).orElse("");
                }
                token = Optional.ofNullable(httpRequest.getHeader("X-Auth-Token")).orElse("");

                Instant end = Instant.now();
                Duration elapsed = Duration.between(begin, end);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS", Locale.CHINESE)
                    .withZone(ZoneId.systemDefault());

                logger.info("TOKEN=" + token + ",USER_ID=" + userId + ",URI=" + uri + ",REQUEST_TIME=" + formatter
                    .format(begin) + ",ELAPSED=" + elapsed.toMillis());
                MDC.remove("traceId");
            }
        }
    }

    @Override
    public void destroy() {

    }

}
