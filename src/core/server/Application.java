package core.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import db.Configuration;


@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        Configuration.load("config.properties");
        SpringApplication.run(Application.class, args);
    }
    
}
