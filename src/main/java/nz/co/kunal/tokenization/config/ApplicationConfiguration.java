package nz.co.kunal.tokenization.config;

import nz.co.kunal.tokenization.domain.SecureRandomTokenGenerator;
import nz.co.kunal.tokenization.domain.TokenGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfiguration {

    @Bean
    public TokenGenerator createTokenGenerator() {
        return new SecureRandomTokenGenerator();
    }
}
