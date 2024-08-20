package com.tinqinacademy.authentication.rest.config;

import com.tinqinacademy.authentication.api.apimapping.RestApiMappingAuthentication;
import com.tinqinacademy.authentication.rest.interceptors.UserSecurityInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class SpringMvcConfig implements WebMvcConfigurer {
    private final UserSecurityInterceptor userSecurityInterceptor;

    @Override
    public void addInterceptors(final InterceptorRegistry registry) {
        registry.addInterceptor(userSecurityInterceptor)
                .addPathPatterns(
                        RestApiMappingAuthentication.POST_CHANGEPASSWORD_PATH,
                        RestApiMappingAuthentication.POST_DEMOTE_PATH,
                        RestApiMappingAuthentication.POST_PROMOTE_PATH);
    }
}