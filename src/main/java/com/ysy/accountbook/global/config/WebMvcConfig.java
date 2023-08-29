package com.ysy.accountbook.global.config;

import com.ysy.accountbook.global.common.converter.LocalDateConverter;
import com.ysy.accountbook.domain.trade.converter.TradeConverter;
import com.ysy.accountbook.global.common.interceptor.LogInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.format.FormatterRegistry;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

    final private LogInterceptor logInterceptor;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(logInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/vendor/**", "/css/*", "/img/*");
    }

    /**
     * Converter 등록
     *
     * @param registry
     */
    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new TradeConverter());
        registry.addConverter(new LocalDateConverter());
    }

    /**
     * Conversion 등록
     *
     * @return
     */
    @Bean
    public DefaultConversionService defaultConversionService() {
        DefaultConversionService defaultConversionService = new DefaultConversionService();
        defaultConversionService.addConverter(new TradeConverter());
        defaultConversionService.addConverter(new LocalDateConverter());
        return defaultConversionService;
    }
}
