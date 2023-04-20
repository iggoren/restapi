package com.example.restapi.configs;

import org.apache.catalina.security.SecurityConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Set;


@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private final SuccessUserHandler successUserHandler;
    private final UserDetailsService userDetailsService;


    public WebSecurityConfig(SuccessUserHandler successUserHandler,
                             UserDetailsService userDetailsService) {
        this.successUserHandler = successUserHandler;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                //  .antMatchers("/**").permitAll()

                .antMatchers("/admin/**").hasRole("ADMIN")
                .antMatchers("/user/**").hasAnyRole("USER", "ADMIN")

                .anyRequest().authenticated()
                .and()
                .formLogin().successHandler(successUserHandler)
                .permitAll()
                .and()
                .logout();
           //     .and()
            //    .oauth2Login()
            //    .userInfoEndpoint(userInfo -> userInfo.oidcUserService(oidcUserService()));
        ;
    }

    private OAuth2UserService<OidcUserRequest, OidcUser> oidcUserService() {

        return userRequest -> {
            String email = userRequest.getIdToken().getClaim("email");
            UserDetails userDetails = userDetailsService.loadUserByUsername(email);
            Set<Method> methods = Set.of(UserDetails.class.getMethods());
            //   new OidcUserService().loadUser()
            DefaultOidcUser oidcUser = new DefaultOidcUser(userDetails.getAuthorities(), userRequest.getIdToken());

            return (OidcUser) Proxy.newProxyInstance(SecurityConfig.class.getClassLoader(),
                    new Class[]{UserDetails.class, OidcUser.class},
                    (proxy, method, args) ->
                            methods.contains(method)
                                    ? method.invoke(userDetails, args)
                                    : method.invoke(oidcUser, args));
        };
    }


    @Override
    protected void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(getPasswordEncoder());
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder(10);
    }
}