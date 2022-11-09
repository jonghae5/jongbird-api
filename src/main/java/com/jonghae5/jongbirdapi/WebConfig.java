package com.jonghae5.jongbirdapi;

import com.jonghae5.jongbirdapi.web.argumentResolver.LoginUserArgumentResolver;
import com.jonghae5.jongbirdapi.web.interceptor.LogInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@EnableJpaAuditing
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new LoginUserArgumentResolver());
    }

    public static final String ALLOWED_METHOD_NAMES = "GET,HEAD,POST,PUT,DELETE,TRACE,OPTIONS,PATCH";

    @Override
    public void addCorsMappings(final CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3060")
                .allowedMethods(ALLOWED_METHOD_NAMES.split(","))
                .allowCredentials(true);
    }


    // 동적 이미지 로딩
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

//        if (key.equals("prod")) {
//            registry.addResourceHandler("/images/**")
//                    .addResourceLocations("file:///home/images/");
//        } else {

        registry.addResourceHandler("/images/**")
                .addResourceLocations("file:/Users/ojh/jongbird-api/src/main/resources/static/images/");
//        }

    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LogInterceptor())
                .order(1)
                .addPathPatterns("/**")
                .excludePathPatterns("/css/**", "/fonts/**", "/images/**", "/js/**", "/template/**", "/*.ico", "/error", "/error-page", "/**/*.otf");
    }
}
