package ${package};
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@ComponentScan("${package}")
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
