package hello.aimju;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing // timestamp 사용하기 위함
public class AiMjuApplication {

    public static void main(String[] args) {
        SpringApplication.run(AiMjuApplication.class, args);
    }

}
