package com.decagon.rewardyourteacherapi;

import com.decagon.rewardyourteacherapi.serviceImpl.SchoolServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;


@SpringBootApplication
@RequiredArgsConstructor
public class RewardyourteacherapiApplication  implements CommandLineRunner{

	private final SchoolServiceImpl schoolService;
	public static void main(String[] args) {
		SpringApplication.run(RewardyourteacherapiApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		schoolService.addSchool("src/main/resources/ListOfSchools.csv");
	}

}
