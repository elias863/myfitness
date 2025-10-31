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

    // Inject UserInfoService here directly if it doesn't cause a direct cycle
    // If UserInfoService itself needs a PasswordEncoder from this config,
    // then getPasswordEncoder() is correctly a @Bean method.
    // The key is to break the cycle.

    // This method provides your custom UserDetailsService
    @Bean
    public UserDetailsService userDetailsService() {
        // Assuming UserInfoService is your implementation of UserDetailsService
        // If UserInfoService has no *direct* dependencies on SecurityConfig,
        // then this is fine. If it depends on PasswordEncoder, the @Bean for
        // PasswordEncoder will be created first.
        return new UserInfoService(); // Spring will handle injecting its dependencies
    }

    // This method provides your JwtAuthFilter
    // IMPORTANT: Make this a @Bean method, so Spring manages its creation.
    // It will be fully initialized when SecurityFilterChain needs it.
    @Bean
    public JwtAuthFilter jwtAuthFilter() {
        // Spring will inject UserDetailsService and JwtService into JwtAuthFilter's constructor
        // assuming they are @Beans.
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
