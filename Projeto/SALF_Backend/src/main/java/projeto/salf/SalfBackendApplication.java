package projeto.salf;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import projeto.salf.controller.bd.SingletonDB;

@SpringBootApplication
public class SalfBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(SalfBackendApplication.class, args);
    }

}
