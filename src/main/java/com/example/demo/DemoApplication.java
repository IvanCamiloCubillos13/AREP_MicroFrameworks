package com.example.demo;

import java.net.URI;
import java.net.URL;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);

		/**URL myUrl = new URI("https://ldbn.is.escuelaing.edu.co:786/arep/respuestas.txt?val=36").toURL();*/
	}

}
