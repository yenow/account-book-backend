package com.ysy.accountbook.global.config;

import com.ysy.accountbook.global.common.converter.LocalDateConverter;
import com.ysy.accountbook.global.common.converter.TradeConverter;
import com.ysy.accountbook.global.common.interceptor.LogInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

    final private LogInterceptor logInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(logInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/vendor/**", "/css/*", "/img/*");
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new TradeConverter());
        registry.addConverter(new LocalDateConverter());
    }

    @Bean
    public DefaultConversionService defaultConversionService() {
        DefaultConversionService defaultConversionService = new DefaultConversionService();
        defaultConversionService.addConverter(new TradeConverter());
        defaultConversionService.addConverter(new LocalDateConverter());
        return defaultConversionService;
    }
}
