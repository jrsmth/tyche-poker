package com.tyche.poker;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;



@Configuration
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        auth.inMemoryAuthentication()
                .withUser("god").password("{noop}createdtheworldinsevendays").roles("GOD");

    }


    // Secure the endpoins with HTTP Basic authentication
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                //HTTP Basic authentication
                .httpBasic()
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.GET, "/flood").hasRole("GOD")
                .antMatchers(HttpMethod.GET, "/users").hasRole("GOD")
                .antMatchers(HttpMethod.GET, "/users/delete").hasRole("GOD")
                .antMatchers(HttpMethod.GET, "/users/delete/**").hasRole("GOD")
                .antMatchers(HttpMethod.GET, "/tables").hasRole("GOD")
                .antMatchers(HttpMethod.GET, "/tables/delete").hasRole("GOD")
                .and()
                .csrf().disable()
                .formLogin().disable();
    }

}
