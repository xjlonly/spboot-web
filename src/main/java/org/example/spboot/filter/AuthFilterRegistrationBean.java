package org.example.spboot.filter;

import org.example.spboot.entity.User;
import org.example.spboot.service.UserService;
import org.example.spboot.web.UserController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Component
@Order(10)
public class AuthFilterRegistrationBean extends FilterRegistrationBean<Filter> {
    @Autowired
    UserService userService;

    @Override
    public Filter getFilter() {
        return new AuthFilter();
    }

    class AuthFilter implements Filter{

        final Logger logger = LoggerFactory.getLogger(getClass());
        @Override
        public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
            HttpServletRequest req = (HttpServletRequest) servletRequest;
            try {
                authenticateByHeader(req);
            } catch (RuntimeException e) {
                logger.warn("login by authorization header failed.", e);
            }
            filterChain.doFilter(servletRequest, servletResponse);
        }
        private void authenticateByHeader(HttpServletRequest req) {
            String authHeader = req.getHeader("Authorization");
            if (authHeader != null && authHeader.startsWith("Basic ")) {
                logger.info("try authenticate by authorization header...");
                String up = new String(Base64.getDecoder().decode(authHeader.substring(6)), StandardCharsets.UTF_8);
                int pos = up.indexOf(':');
                if (pos > 0) {
                    String email = URLDecoder.decode(up.substring(0, pos), StandardCharsets.UTF_8);
                    String password = URLDecoder.decode(up.substring(pos + 1), StandardCharsets.UTF_8);
                    User user = userService.signin(email, password);
                    req.getSession().setAttribute(UserController.KEY_USER, user);
                    logger.info("user {} login by authorization header ok.", email);
                }
            }
        }
    }
}
