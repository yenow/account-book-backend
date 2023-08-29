//package com.ysy.accountbook.global.config.jpa;
//
//import lombok.Data;
//import org.hibernate.jpa.boot.spi.EntityManagerFactoryBuilder;
//import org.springframework.beans.factory.ObjectProvider;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
//import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
//import org.springframework.boot.autoconfigure.transaction.TransactionManagerCustomizers;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
//import org.springframework.stereotype.Component;
//import org.springframework.transaction.jta.JtaTransactionManager;
//
//import javax.sql.DataSource;
//
//@Configuration
//public class JpaConfig extends HibernateJpaAutoConfiguration {
//
//    @Data
//    @Component
//    @ConfigurationProperties("spring.jpa.orm")
//    public class OrmProps {
//        private String[] queries;
//    }
//
//    @Autowired
//    private OrmProps ormProps;
//
//    public JpaConfig(DataSource dataSource, JpaProperties jpaProperties, ObjectProvider<JtaTransactionManager> jtaTransactionManager, ObjectProvider<TransactionManagerCustomizers> transactionManagerCustomizers) {
//        //super(dataSource, jpaProperties, jtaTransactionManager, transactionManagerCustomizers);
//    }
//
//    @Bean
//    @Override
//    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
//            EntityManagerFactoryBuilder factoryBuilder)
//    {
//        final LocalContainerEntityManagerFactoryBean ret = super.entityManagerFactory(factoryBuilder);
//        ret.setMappingResources(ormProps.getQueries());
//        return ret;
//    }
//}
