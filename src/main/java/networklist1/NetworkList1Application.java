package networklist1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {
        "networklist1",
        "controllers",
        "dto",
        "services",
        "security",
        "repositories",
        "config"
})
@EnableJpaRepositories(basePackages = "repositories")
public class NetworkList1Application {

    public static void main(String[] args) {
        SpringApplication.run(NetworkList1Application.class, args);
    }
}



