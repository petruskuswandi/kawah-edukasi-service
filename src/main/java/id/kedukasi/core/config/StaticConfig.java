package id.kedukasi.core.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class StaticConfig implements WebMvcConfigurer {
    /*
        Static Resource Handler
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/upload/**").addResourceLocations("file:///" + System.getProperty("user.dir") + "/src/main/upload/");
        //registry.addResourceHandler("/templates/**").addResourceLocations("classpath:/templates/");
    }
}

