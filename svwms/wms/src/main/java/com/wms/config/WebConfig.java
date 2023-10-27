package com.wms.config;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // 配置 CORS 参数
        registry.addMapping("/**") // 指定应用此CORS配置的路径模式
                .allowedOriginPatterns("http://localhost:8082", "http://wmsvue") // 允许跨域访问的源
                .allowedMethods("GET", "POST", "PUT", "DELETE", "HEAD", "OPTIONS") // 允许请求的方法
                .allowedHeaders("*") // 允许的HTTP请求头
                .allowCredentials(true); // 是否允许证书（如cookies）
    }
}
