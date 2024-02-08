package com.hgb.gssbe.common.config;

import com.hgb.gssbe.common.security.JwtFilter;
import com.hgb.gssbe.common.security.SecuritySvc;
import com.hgb.gssbe.common.security.TokenProvider;
import com.hgb.gssbe.common.security.handler.JwtAccessDeniedHandler;
import com.hgb.gssbe.common.security.handler.JwtAuthenticationEntryPoint;
import com.hgb.gssbe.common.security.model.ApiUrlAuth;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final SecuritySvc securitySvc;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        // 허용되어야 할 경로들, 특히 정적파일들(필요한경우만 설정)
        return web -> web.ignoring().requestMatchers("/**");
//        return web -> web.ignoring().requestMatchers("/sw", "/swagger-ui/**", "v3/**", "/api/login");
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity
            , JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint
            , JwtAccessDeniedHandler jwtAccessDeniedHandler
            , TokenProvider tokenProvider) throws Exception {
        httpSecurity
                .formLogin().disable()
                .csrf().disable()
                .exceptionHandling()
                .accessDeniedHandler(jwtAccessDeniedHandler)
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilterBefore(new JwtFilter(tokenProvider, securitySvc), UsernamePasswordAuthenticationFilter.class);

//        setAuthorizeHttpRequests(httpSecurity);
        return httpSecurity.build();

    }

    private void setAuthorizeHttpRequests(HttpSecurity httpSecurity) throws Exception {
        List<ApiUrlAuth> auth = securitySvc.getApiUrlAuth();
        for (ApiUrlAuth apiUrlAuth : auth) {
            httpSecurity.authorizeHttpRequests().requestMatchers(apiUrlAuth.getApiUrl()).hasAnyAuthority(apiUrlAuth.getAuthList().toArray(new String[0]));
        }
    }

}
