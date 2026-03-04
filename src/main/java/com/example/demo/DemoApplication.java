package com.example.demo;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) throws URISyntaxException, MalformedURLException{
		SpringApplication.run(DemoApplication.class, args);

		URL nyurl = new URI("https://ldbn.is.escuelaing.edu.co").toURL();
	}

}
