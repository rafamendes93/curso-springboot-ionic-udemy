package com.rafael.cursomc.cursomc.config;


import com.rafael.cursomc.cursomc.services.EmailService;
import com.rafael.cursomc.cursomc.services.SmtpEmailService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("prod")
public class ProdConfig {

    @Bean
    public EmailService emailService(){
        return new SmtpEmailService();
    }
}
