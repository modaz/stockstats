package com.stockstats.filter;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

@Component
public class KibotLoginFilter implements Filter {

    private static final String KIBOT_LOGIN_URL = "http://api.kibot.com?action=login&user=guest&password=guest";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        try {
            doKibotLogin();
            chain.doFilter(request, response);
        } catch (Exception ex) {
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("UTF-8");
            ((jakarta.servlet.http.HttpServletResponse) response).setStatus(HttpStatus.SERVICE_UNAVAILABLE.value());

            Map<String, Object> errorBody = Map.of(
                    "status", 503,
                    "message", "Kibot login failed: " + ex.getMessage()
            );
            new ObjectMapper().writeValue(response.getWriter(), errorBody);
        }
    }

    private void doKibotLogin() throws URISyntaxException {
        RestTemplate restTemplate = new RestTemplate();
        URI url = new URI(KIBOT_LOGIN_URL);
        restTemplate.getForEntity(url, String.class);
    }

    @Override
    public void destroy() {
        // no-op
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // no-op
    }
}
