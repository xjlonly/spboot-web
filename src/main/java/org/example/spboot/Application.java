package org.example.spboot;

import org.example.spboot.config.MasterDataSourceConfiguration;
import org.example.spboot.config.RedisConfiguration;
import org.example.spboot.config.RoutingDataSourceConfiguration;
import org.example.spboot.config.SlaveDataSourceConfiguration;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@Import({MasterDataSourceConfiguration.class,
        SlaveDataSourceConfiguration.class,
        RoutingDataSourceConfiguration.class})
public class Application
{
    public static void main(String[] args ) throws Exception
    {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    WebMvcConfigurer createWebMvcConfigurer(@Autowired HandlerInterceptor[] interceptors){
        return new WebMvcConfigurer() {
            @Override
            public void addResourceHandlers(ResourceHandlerRegistry registry) {
                registry.addResourceHandler("/static/**")
                        .addResourceLocations("classpath:/static/");
            }
        };
    }

    @Bean
    MessageConverter createMessageConverter(){
        return new Jackson2JsonMessageConverter();
    }
}
