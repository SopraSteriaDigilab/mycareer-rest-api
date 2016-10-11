package services;

import java.util.Arrays;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
    	System.out.println("Welcome! :)");
    	System.out.println("MyCareer is booting... It won't take a while!");
        ApplicationContext ctx = SpringApplication.run(Application.class, args);
        System.out.println("MyCareer is up and running! Enjoy ;)");
    }

}
