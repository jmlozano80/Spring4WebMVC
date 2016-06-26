package com.lozano.config;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * Created by jose on 14/05/16.
 */

@Configuration
@EnableTransactionManagement
@PropertySource({"classpath:properties/datasource.properties"})
@ComponentScan({"com.lozano.dao.impl","com.lozano.service"})
public class PersistenceConfig {

    @Autowired
    private Environment env;

    public PersistenceConfig(){
        super();
    }

    @Bean
    public DataSource dataSource(){

        final DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(env.getProperty("dataSource.driverClassName"));
        dataSource.setUrl(env.getProperty("dataSource.url"));
        dataSource.setUsername(env.getProperty("dataSource.username"));
        dataSource.setPassword(env.getProperty("dataSource.password"));

        return dataSource;
    }

    @Bean
    public LocalSessionFactoryBean sessionFactory(){

        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource());
        sessionFactory.setPackagesToScan(new String[]{"com.lozano.model"});
        sessionFactory.setHibernateProperties(hibernateProperties());

        return sessionFactory;
    }

    @Bean
    @Autowired
    public HibernateTransactionManager transactionManager(SessionFactory sessionFactory){
        HibernateTransactionManager txManager = new HibernateTransactionManager();
        txManager.setSessionFactory(sessionFactory);

        return  txManager;
    }

    @Bean
    public PersistenceExceptionTranslationPostProcessor exceptionTranslation(){

        return  new PersistenceExceptionTranslationPostProcessor();
    }

    Properties hibernateProperties() {
        return new Properties() {
            {
                setProperty("hibernate.order_inserts", env.getProperty("hibernate.order_inserts"));
                setProperty("hibernate.order_updates", env.getProperty("hibernate.order_updates"));
                setProperty("hibernate.jdbc.batch_size", env.getProperty("hibernate.jdbc.batch_size"));
                setProperty("hibernate.validator.apply_to_ddl", env.getProperty("hibernate.validator.apply_to_ddl"));
                setProperty("hibernate.validator.autoregister_listeners", env.getProperty("hibernate.validator.autoregister_listeners"));
                setProperty("javax.persistence.validation.mode", env.getProperty("javax.persistence.validation.mode"));
                setProperty("hibernate.hbm2ddl.auto", env.getProperty("hibernate.hbm2ddl.auto"));
                setProperty("hibernate.show_sql", env.getProperty("hibernate.show_sql"));
                setProperty("hibernate.format_sql", env.getProperty("hibernate.format_sql"));
                setProperty("hibernate.use_sql_comments", env.getProperty("hibernate.use_sql_comments"));
                setProperty("hibernate.generate.statistics", env.getProperty("hibernate.generate.statistics"));
                setProperty("hibernate.dialect", env.getProperty("hibernate.dialect"));
                setProperty("connection.pool_size", env.getProperty("connection.pool_size"));

            }
        };
    }

}
