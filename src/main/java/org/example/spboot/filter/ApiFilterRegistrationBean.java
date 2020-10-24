package org.example.spboot.filter;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Component
@Order(20)
public class ApiFilterRegistrationBean extends FilterRegistrationBean<Filter> {

    @PostConstruct
    public void init(){
        this.setFilter(new ApiFilter());
        setUrlPatterns(List.of("/api/*"));
    }

    @Override
    public Filter getFilter() {
        return super.getFilter();
    }

    class ApiFilter implements Filter{
        @Override
        public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
            HttpServletResponse resp = (HttpServletResponse) servletResponse;
            resp.setHeader("X-Api-Version", "1.0");
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }
}
