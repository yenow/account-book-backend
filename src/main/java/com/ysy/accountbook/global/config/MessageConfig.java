package com.ysy.accountbook.global.config;

import com.querydsl.core.annotations.Config;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ResourceBundleMessageSource;

@Config
public class MessageConfig {

    @Bean
    public MessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("classpath:/message/message");
        messageSource.setDefaultEncoding("utf-8");
        return messageSource;
    }
}
