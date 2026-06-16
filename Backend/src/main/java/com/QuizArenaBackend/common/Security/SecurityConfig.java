package com.QuizArenaBackend.common.Security;

import com.QuizArenaBackend.auth.Service.AppUserDetailsService;
import com.QuizArenaBackend.common.Security.jwt.JwtAuthFilter;
import com.QuizArenaBackend.common.Security.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final AppUserDetailsService appUserDetailsService;
    private final JwtAuthFilter jwtFilter;

    //custom authentication manager bean , with dao authentication...
    @Bean
    public AuthenticationManager authenticationProvider(){
        DaoAuthenticationProvider daoauthenticationProvider= new DaoAuthenticationProvider(appUserDetailsService);
        daoauthenticationProvider.setPasswordEncoder(passwordEncoder());
        return new ProviderManager(daoauthenticationProvider);
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    // custom cors filter configuration or else default is provided....
    @Bean
    public CorsFilter corsFilter(){
        return new CorsFilter(corsConfigurationSource());
    }

    private CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config =  new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:5173"));
        config.setAllowedMethods(List.of("GET","POST","PUT","DELETE","PATCH","OPTIONS"));

//  Allow common + multimedia headers
        config.setAllowedHeaders(List.of(
                "Authorization",
                "Content-Type",
                "Accept",
                "Origin",
                "X-Requested-With",
                "Range"                 // Needed for video streaming
        ));

        //  Headers browser can READ from response
        config.setExposedHeaders(List.of(
                "Authorization",
                "Content-Type",
                "Content-Length",
                "Content-Range",        //  Required for video/audio streaming
                "Accept-Ranges"
        ));

        config.setAllowCredentials(true);


        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**",config);
        return source;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(Customizer.withDefaults())

                //authorization for http requests....
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers( "/api/v1/auth/register").permitAll()
                )

                //dealing with session management
                .sessionManagement( session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                //disabling the logout feature...
                .logout(AbstractHttpConfigurer::disable)

                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)

                // add custom exception handling....
                .exceptionHandling(ex -> ex.authenticationEntryPoint(customAuthenticationEntryPoint));



        return http.build();
    }

}
