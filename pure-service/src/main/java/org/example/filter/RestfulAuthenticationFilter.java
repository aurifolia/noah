package org.example.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.service.SharedData;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * TODO
 *
 * @author VNElinpe
 * @since 2023/5/28
 **/
public class RestfulAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    private static final AntPathRequestMatcher matcher = new AntPathRequestMatcher("/user/login", "POST");
    public RestfulAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(matcher, authenticationManager);
        setAuthenticationSuccessHandler(new AuthenticationSuccessHandler() {
            @Override
            public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                String sessionId = UUID.randomUUID().toString().replace("-", "");
                SecurityContextImpl securityContext = new SecurityContextImpl();
                securityContext.setAuthentication(authentication);
                SharedData.map.put(sessionId, securityContext);
                ObjectMapper objectMapper = new ObjectMapper();
                Map<String, String> data = new HashMap<>();
                data.put("sessionId", sessionId);
                objectMapper.writeValue(response.getOutputStream(), data);
            }
        });
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        ObjectMapper mapper = new ObjectMapper();
        Map map = mapper.readValue(request.getInputStream().readAllBytes(), Map.class);
        Object username = map.get("username");
        Object password = map.get("password");
        UsernamePasswordAuthenticationToken authRequest = UsernamePasswordAuthenticationToken.unauthenticated(username,
                password);
        // Allow subclasses to set the "details" property
        authRequest.setDetails(this.authenticationDetailsSource.buildDetails(request));
        return getAuthenticationManager().authenticate(authRequest);
    }
}
