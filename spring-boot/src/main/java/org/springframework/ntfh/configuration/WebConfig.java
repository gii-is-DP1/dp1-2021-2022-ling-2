package org.springframework.ntfh.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.ntfh.converters.StringToUserConverter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private StringToUserConverter stringToUserConverter;

    @Override
    public void addFormatters(FormatterRegistry registry) {
        // Add custom formatter/converters below ⬇️
        registry.addConverter(stringToUserConverter);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedOrigins("http://localhost:3000").allowCredentials(true);
    }
}
