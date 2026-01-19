package com.example.employeemanagement.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/** This class represents the security configuration. */
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtRequestFilter jwtRequestFilter;  // ← Đảm bảo bạn đã inject filter JWT

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
protected void configure(HttpSecurity http) throws Exception {
    http
        .cors().and()
        .csrf().disable()

        .authorizeRequests()
            // PUBLIC
            .antMatchers("/", "/login", "/register", "/v3/api-docs/**", "/swagger-ui/**").permitAll()

            // PAGES (cho load HTML)
            .antMatchers(
                "/Admin",
                "/schedule",
                "/dashboard",
                "/employees",
                "/employee-form",
                "/contracts",
                "/departments",
                "/department-form",
                "/profile",
                "/forgotPassword",
                "/workscheduleview/**"
                
            ).permitAll()
            .antMatchers( "/api/employees", "/api/departments").permitAll()
            

           .antMatchers("/api/auth/**").permitAll()

// ADMIN
.antMatchers("/api/admin/**").hasRole("ADMIN")

// USER + ADMIN
.antMatchers("/api/users/**").authenticated()

// các API khác
.antMatchers("/api/**").authenticated()
            

            .anyRequest().authenticated()

        .and()
        .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

        .and()
        .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
}
@Bean
public org.springframework.web.cors.CorsConfigurationSource corsConfigurationSource() {
    org.springframework.web.cors.CorsConfiguration config =
            new org.springframework.web.cors.CorsConfiguration();

    // Cho phép gửi Authorization (JWT)
    config.setAllowCredentials(true);

    // Frontend chạy cùng port
    config.setAllowedOrigins(java.util.List.of(
        "http://localhost:8080"
    ));

    config.setAllowedMethods(java.util.List.of(
        "GET", "POST", "PUT", "DELETE", "OPTIONS"
    ));

    config.setAllowedHeaders(java.util.List.of("*"));
    config.setExposedHeaders(java.util.List.of("Authorization"));

    org.springframework.web.cors.UrlBasedCorsConfigurationSource source =
            new org.springframework.web.cors.UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", config);

    return source;
}


}