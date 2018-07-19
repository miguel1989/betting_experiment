package com.neotech;

import com.neotech.config.MainConfig;
import com.neotech.config.SecurityConfig;
import com.neotech.config.WebConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication()
public class Application  {
    private static final Logger log = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(
                MainConfig.class,
                Application.class,
                SecurityConfig.class,
                WebConfig.class
        );
        app.run(args);
//        SpringApplication.run(Application.class, args);
    }
}
