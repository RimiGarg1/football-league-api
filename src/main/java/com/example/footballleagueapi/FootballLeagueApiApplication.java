package com.example.footballleagueapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.http.HttpClient;

/**
 * Application class for Foortabll league Service API
 */
@SpringBootApplication
public class FootballLeagueApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(FootballLeagueApiApplication.class, args);
	}

}
