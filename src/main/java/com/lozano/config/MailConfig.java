package com.lozano.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import com.google.common.base.Preconditions;
import java.util.Properties;

/**
 * Created by jose on 14/05/16.
 */
@Configuration
@PropertySource({"classpath:properties/mail.properties"})
public class MailConfig {

    @Autowired
    private Environment env;

    @Bean
    public MessageSource messageSource(){
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:properties/messages");
        messageSource.setCacheSeconds(5);
        messageSource.setDefaultEncoding("UTF-8");


        return messageSource;
    }

    @Bean
    public JavaMailSender mailSender(){

        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(env.getProperty("mail.sender.host"));
        mailSender.setPort(Integer.parseInt(env.getProperty("mail.sender.port")));
        mailSender.setUsername(env.getProperty("mail.sender.username"));
        mailSender.setPassword(env.getProperty("mail.sender.password"));
        mailSender.setJavaMailProperties(mailProperties());

        return mailSender;
    }

    Properties mailProperties(){

        return new Properties(){
            {
                setProperty("mail.transport.protocol", Preconditions.checkNotNull(env.getProperty("mail.sender.transport.protocol")));
                setProperty("mail.smtp.auth", Preconditions.checkNotNull(env.getProperty("mail.sender.smtp.auth")));
                setProperty("mail.smtp.starttls.enable", env.getProperty("mail.sender.smtp.starttls.enable"));
                setProperty("mail.debug", env.getProperty("mail.sender.debug"));
            }
        };
    }
}
