package hello.aimju.login.config;

import hello.aimju.login.argumentresolver.LoginMemberArgumentResolver;
import hello.aimju.login.interceptor.LogInterceptor;
import hello.aimju.login.interceptor.LoginCheckInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Value("${origin}")
    private String origin;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new LoginMemberArgumentResolver());
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LogInterceptor())
                .order(1)
                .addPathPatterns("/**")
                .excludePathPatterns("/css/**", "/*.ico", "/error");

        registry.addInterceptor(new LoginCheckInterceptor())
                .order(2)
                .addPathPatterns("/**")//아래 경로 빼고 모든 경로 체크
                .excludePathPatterns("/", "/api/signup", "/api/login",
                        "/css/**", "/*.ico", "/error");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")  // 모든 요청에 대해 CORS 허용
                .allowedOrigins(origin) // 모든 Origin(도메인) 허용. 실제 운영 환경에서는 특정 Origin으로 제한할 것을 권장
                .allowedMethods("*") // 모든 HTTP 메서드 허용 (GET, POST, PUT, DELETE 등)
                .allowedHeaders("*"); // 모든 요청 헤더 허용
    }
}
