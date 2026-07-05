package com.sahan.quizapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

/**
 * CorsConfig — Tells Spring Boot to accept requests from the
 * React dev server (http://localhost:5173).
 *
 * WHY IS THIS NEEDED?
 * Browsers enforce the "Same-Origin Policy": a web page at
 * origin A is blocked from making HTTP requests to origin B
 * unless origin B explicitly allows it via CORS headers.
 *
 * Our React app runs on  http://localhost:5173  (different origin)
 * Our Spring Boot runs on http://localhost:8080  (this server)
 *
 * Without this config, the browser will block every API call
 * from React with a CORS error, even though the request reaches
 * the server fine — the browser refuses to give the response
 * to JavaScript.
 *
 * NOTE: Vite's proxy (vite.config.js) already solves CORS in
 * development. This class is the production / direct-call safety net.
 */
@Configuration  // Marks this as a Spring configuration class (like @Bean factory)
public class CorsConfig {

    @Bean  // Registers this CorsFilter as a Spring-managed bean (component)
    public CorsFilter corsFilter() {

        CorsConfiguration config = new CorsConfiguration();

        // allowCredentials(true) allows the browser to send/receive
        // cookies and Authorization headers cross-origin.
        config.setAllowCredentials(true);

        // Which origins (protocol + host + port) are allowed to call us.
        // Add your production frontend URL here when you deploy.
        config.setAllowedOrigins(List.of(
                "http://127.0.0.1:3000", // Vite React dev server (IPv4)
                "http://localhost:3000",  // Vite React dev server (localhost alias)
                "http://127.0.0.1:3050", // Vite React dev server (current config)
                "http://localhost:3050"  // Vite React dev server (localhost alias)
        ));

        // Which HTTP headers the browser is allowed to send.
        // "Authorization" is essential for JWT tokens.
        config.setAllowedHeaders(List.of(
                "Authorization",
                "Content-Type",
                "Accept"
        ));

        // Which HTTP methods we allow. "*" means all (GET, POST, PUT, DELETE…).
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        // Apply this CORS config to ALL endpoints ("/**")
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return new CorsFilter(source);
    }
}
