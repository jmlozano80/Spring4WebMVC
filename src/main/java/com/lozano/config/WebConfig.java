package com.lozano.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * Created by jose on 14/05/16.
 */
@EnableWebMvc /* Equivalent to the xml version of <mvc:annotation-driven /> */
@ComponentScan(basePackages = {"com.lozano.controller"})
@Configuration
public class WebConfig {
}
