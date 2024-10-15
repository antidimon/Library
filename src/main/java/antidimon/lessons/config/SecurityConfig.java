package antidimon.lessons.config;

import antidimon.lessons.security.AuthFailureHandler;
import antidimon.lessons.security.AuthSuccessHandler;
import antidimon.lessons.services.PersonDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractAuthenticationFilterConfigurer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final PersonDetailsService userDetailsService;

    @Autowired
    public SecurityConfig(PersonDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(registry -> {
                    registry.requestMatchers(HttpMethod.POST, "/books/search", "/register").permitAll();
                    registry.requestMatchers(HttpMethod.POST).hasRole("ADMIN");
                    registry.requestMatchers(HttpMethod.PATCH).hasRole("ADMIN");
                    registry.requestMatchers(HttpMethod.DELETE).hasRole("ADMIN");
                    registry.requestMatchers(HttpMethod.GET, "/books/new", "/books/*/edit", "/admin/home").hasRole("ADMIN");
                    registry.requestMatchers(HttpMethod.GET,"/books", "/books/*", "/register", "/login").permitAll();
                    registry.anyRequest().authenticated();})
                .formLogin(httpSecurityFormLoginConfigurer -> httpSecurityFormLoginConfigurer
                        .loginPage("/login")
                        .successHandler(new AuthSuccessHandler())
                        .permitAll()
                        .failureHandler(new AuthFailureHandler()))

                .build();

    }

    @Bean
    public UserDetailsService userDetailsService(){
        return userDetailsService;
    }

    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
