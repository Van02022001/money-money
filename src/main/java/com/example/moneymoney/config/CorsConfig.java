package com.example.moneymoney.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
				.allowedMethods(HttpMethod.GET.name(), HttpMethod.POST.name(), HttpMethod.PUT.name(),
						HttpMethod.OPTIONS.name(), HttpMethod.DELETE.name())
				.allowedHeaders("*")
				.allowedOrigins("*", "https://money-money.vercel.app/", "https://money-money.vercel.app/**", "http://localhost:3000/**",

						"https://money-money.azurewebsites.net/**", "https://money-money.azurewebsites.net/", "http://localhost:3000/");

	}


}
