/**
 * @author: Riccardo_Bruno
 * @project: repo-ai
 */


package ai.example.restfulapi.bookstore_crud.security;

import ai.example.restfulapi.bookstore_crud.services.authentication.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SecurityConfig {



    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeRequests(authorizeRequests ->
                        authorizeRequests
                                //.requestMatchers("/api/books/**").authenticated() // Secure all book API endpoints
                                .requestMatchers("/api/books/**").hasRole("USER") // Require USER role for API books endpoints
                                .requestMatchers("/admin/**").hasRole("ADMIN") // Require ADMIN role for admin endpoints
                                .anyRequest().permitAll()
                )
                .httpBasic(withDefaults()) // Enable Basic Authentication
                .csrf(csrf -> csrf.disable()); // Disable CSRF for simplicity (not recommended for production)
        return http.build();
    }

   /* @Bean
    public UserDetailsService userDetailsService() {
        UserDetails user = User.withDefaultPasswordEncoder() // Use NoOp encoder for simplicity (not recommended for production)
                .username("user")
                .password("password")
                .roles("USER")
                .build();

        UserDetails admin = User.withDefaultPasswordEncoder() // Use NoOp encoder for simplicity (not recommended for production)
                .username("admin")
                .password("admin")
                .roles("ADMIN")
                .build();

        return new InMemoryUserDetailsManager(user, admin);
    }*/


/*    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsServiceImpl();
    }*/

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    /*@Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, PasswordEncoder passwordEncoder, UserDetailsService userDetailsService)
            throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder)
                .and()
                .build();
    }*/

/*    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, PasswordEncoder passwordEncoder, UserDetailsService userDetailsService)
            throws Exception {
        AuthenticationManagerBuilder builder = http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder);
        return builder.build();
    }*/


    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
                .requestMatchers("/resources/**"); // Example of resources you might want to ignore
    }
}
