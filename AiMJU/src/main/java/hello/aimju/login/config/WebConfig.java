package hello.aimju.login.config;

import hello.aimju.login.argumentresolver.LoginMemberArgumentResolver;
import hello.aimju.login.interceptor.LogInterceptor;
import hello.aimju.login.interceptor.LoginCheckInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {
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
                .excludePathPatterns("/", "/api/signup", "/api/login", "/api/logout", "/api/user-info/**",
                        "/css/**", "/*.ico", "/error");
    }
}
