package ch.rdev.config;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.*;
import org.thymeleaf.spring6.view.ThymeleafViewResolver;

import java.util.Arrays;

@Configuration
@EnableWebMvc
public class MvcConfig  implements WebMvcConfigurer, ApplicationContextAware {

    private ApplicationContext applicationContext;

    Environment env;

    public MvcConfig(Environment env) {
        this.env = env;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        boolean local = Arrays.stream(env.getActiveProfiles()).anyMatch(s -> s.equals("local"));
        if(local){
            // Load files from disk for editing while the debug server is running
            registry.addResourceHandler("/").addResourceLocations("file:./src/main/resources/static/index.html");
            registry.addResourceHandler("/**").addResourceLocations("file:./src/main/resources/static/");
        } else {
            registry.addResourceHandler("/**").addResourceLocations("classpath:/static/");
        }
//        registry.addResourceHandler("/").addResourceLocations("index.html");
    }


    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        ThymeleafViewResolver resolver = new ThymeleafViewResolver();

        registry.viewResolver(resolver);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }


//    @Override
//    public void addViewControllers(ViewControllerRegistry registry) {
//        registry.addViewController("/").
//    }
}
