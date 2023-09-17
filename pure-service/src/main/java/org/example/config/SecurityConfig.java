package org.example.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.service.SharedData;
import org.example.filter.RestfulAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.context.HttpRequestResponseHolder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.util.StringUtils;

import java.util.UUID;

/**
 * TODO
 *
 * @author VNElinpe
 * @since 2023/5/28
 **/
@Configuration
@EnableMethodSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain web(HttpSecurity http, RestfulAuthenticationFilter restfulAuthenticationFilter) throws Exception {
        http.authorizeHttpRequests(authorize ->
                authorize.requestMatchers("/test/002").hasRole("ADMIN")
                        .requestMatchers("/test/003").permitAll()
                        .anyRequest().authenticated());
        http.formLogin(form -> form.disable());
        http.csrf(csrf -> csrf.disable());
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.addFilterAfter(getRestfulAuthenticationFilter(), LogoutFilter.class);
        http.securityContext(context -> context.securityContextRepository(getSecurityContextRepository()));
        return http.build();
    }

    @Bean
    public RestfulAuthenticationFilter getRestfulAuthenticationFilter() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(users());
        ProviderManager providerManager = new ProviderManager(daoAuthenticationProvider);
        return new RestfulAuthenticationFilter(providerManager);
    }

    @Bean
    public SecurityContextRepository getSecurityContextRepository() {
        return new SecurityContextRepository() {
            @Override
            public SecurityContext loadContext(HttpRequestResponseHolder requestResponseHolder) {
                String sessionId = requestResponseHolder.getRequest().getHeader("sessionId");
                return StringUtils.hasText(sessionId) ? SharedData.map.get(sessionId) : null;
            }

            @Override
            public void saveContext(SecurityContext context, HttpServletRequest request, HttpServletResponse response) {
                String sessionId = UUID.randomUUID().toString().replace("-", "");
                response.setHeader("sessionId", sessionId);
                SharedData.map.put(sessionId, context);
            }

            @Override
            public boolean containsContext(HttpServletRequest request) {
                String sessionId = request.getHeader("sessionId");
                return StringUtils.hasText(sessionId) && SharedData.map.containsKey(sessionId);
            }
        };
    }

    @Bean
    public UserDetailsService users() {
        UserDetails user = User.builder().username("user").password("{bcrypt}$2a$10$Utxx/J74MYthjaoCeiYYXO.qrjPz2BOGbF9msMOa76K7TqO62LdyC").roles("USER").build();
        UserDetails admin = User.builder().username("admin").password("{bcrypt}$2a$10$IpRX0BlAmtwX3xp4ySiCvOLljyDEUmjJQpBpcEP5DS4KLdaF/TA2i").roles("USER", "ADMIN").build();
        return new InMemoryUserDetailsManager(user, admin);
    }
}
