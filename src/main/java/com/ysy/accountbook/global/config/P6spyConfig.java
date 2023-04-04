package com.ysy.accountbook.global.config;

import com.p6spy.engine.spy.P6SpyOptions;
import com.ysy.accountbook.global.config.p6y.P6spyPrettySqlFormatter;
import javax.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;



@Configuration
public class P6spyConfig {

    @PostConstruct
    public void setLogMessageFormat() {
        P6SpyOptions.getActiveInstance().setLogMessageFormat(P6spyPrettySqlFormatter.class.getName());
    }
}
