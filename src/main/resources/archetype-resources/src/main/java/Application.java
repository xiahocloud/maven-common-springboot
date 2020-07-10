package ${package};
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
	    System.setProperty("mail.mime.splitlongparameters", "false");
        SpringApplication.run(Application.class, args);
    }
}
