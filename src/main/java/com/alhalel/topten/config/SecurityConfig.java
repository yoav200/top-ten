package com.alhalel.topten.config;


import com.alhalel.topten.security.oauth2.CustomOAuth2UserService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomOAuth2UserService customOAuth2UserService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                .cors()
                    .and()
                .csrf()
                    .disable();

        http
                .authorizeRequests()
                    .antMatchers("/", "/test", "/images/**", "/static/**")
                        .permitAll()
                    .antMatchers("/terms", "/about", "/privacy", "/contact")
                        .permitAll()
                    .antMatchers("/auth/**", "/oauth2/**")
                        .permitAll()
                    .antMatchers(HttpMethod.GET, "/comments/**", "/voting/**")
                        .permitAll()
                    .antMatchers(HttpMethod.GET, "/players", "/players/**")
                        .permitAll()
                    .antMatchers(HttpMethod.GET, "/ranking/lists")
                        .permitAll()
                    .anyRequest()
                        .authenticated();

        http
                .oauth2Login()
                    .loginPage("/")
                    .authorizationEndpoint()
                        .baseUri("/oauth2/authorization")
                        .and()
                    .redirectionEndpoint()
                        .baseUri("/oauth2/callback/*")
                        .and()
                    .userInfoEndpoint()
                        .userService(customOAuth2UserService);


        http
                .logout()
                .logoutSuccessUrl("/");
    }
}
