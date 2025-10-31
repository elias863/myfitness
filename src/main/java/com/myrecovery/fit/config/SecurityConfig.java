package com.myrecovery.fit.config;

import com.myrecovery.fit.filter.JwtAuthFilter;
import com.myrecovery.fit.service.UserInfoService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // Bean sostiutivo all'injection per evitare la dipendenza circolare
    @Bean
    public UserDetailsService userDetailsService() {
        return new UserInfoService(); // Spring will handle injecting its dependencies
    }

    // Bean sostiutivo all'injection per evitare la dipendenza circolare
    @Bean
    public JwtAuthFilter jwtAuthFilter() {
        return new JwtAuthFilter();
    }

    /*
     * Main security configuration
     * Defines endpoint access rules and JWT filter setup
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http
                // Disable CSRF (not needed for stateless JWT) -->
                // non serve la protezione per il CSRF perchè non viene usato il cookie di sessione e quindi
                // non è possibile un attacco di tipo CSRF
                .csrf(csrf -> csrf.disable())

                // configure endpoint authorization
                .authorizeHttpRequests(auth -> auth
                        // Public endpoints
                        .requestMatchers("/auth/welcome","/auth/addNewUser", "/auth/generateToken")
                        .permitAll()

                        // endpoint Esercizio e Parametri (l'utente può solo visualizzare, per le altre operazioni serve essere ADMIN), l'admin può fare tutto
                        .requestMatchers("/api/parametri/all", "/api/esercizi/all").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")

                        .requestMatchers("/api/parametri/create", "/api/parametri/update/{id}","/api/parametri/delete/{id}","/api/parametri/deleteall").hasAuthority("ROLE_ADMIN")
                        .requestMatchers("/api/esercizi/create", "/api/esercizi/update/{id}","/api/esercizi/delete/{id}","/api/esercizi/deleteall").hasAuthority("ROLE_ADMIN")

                        // role-based endpoints
                        .requestMatchers("/auth/user/**").hasAuthority("ROLE_USER")
                        .requestMatchers("/auth/admin/**").hasAuthority("ROLE_ADMIN")

                        // All other endpoints require authentication
                        .anyRequest().authenticated()
                )

                // Stateless session (required for JWT)
                .sessionManagement(sess ->
                        sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // set custom authentication provider
                .authenticationProvider(authenticationProvider())

                // Add JWT filter before Spring Security's default filter
                .addFilterBefore(jwtAuthFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /*
     * Password encoder bean (uses BCrypt hashing)
     * Critical for secure password storage
     */
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    /*
     * Authentication provider configuration
     * Links UserDetailsService and PasswordEncoder
     */
    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService());
        provider.setPasswordEncoder(passwordEncoder());

        return provider;
    }

    /*
     * Authentication manager bean
     * Required for programmatic authentication (e.g., in /generateToken)
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception{
        return config.getAuthenticationManager();
    }
}
