package it.nextworks.nfvmano.elm;

import it.nextworks.nfvmano.elm.auth.AuthorizationFilter;
import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver;
import org.keycloak.adapters.springsecurity.KeycloakSecurityComponents;
import org.keycloak.adapters.springsecurity.authentication.KeycloakAuthenticationProvider;
import org.keycloak.adapters.springsecurity.client.KeycloakClientRequestFactory;
import org.keycloak.adapters.springsecurity.client.KeycloakRestTemplate;
import org.keycloak.adapters.springsecurity.config.KeycloakWebSecurityConfigurerAdapter;
import org.keycloak.adapters.springsecurity.filter.KeycloakPreAuthActionsFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.annotation.RequestScope;


import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;

import javax.servlet.http.HttpServletRequest;

@Configuration
@EnableWebSecurity
@ConditionalOnProperty(value = "keycloak.enabled", matchIfMissing = true)
@ComponentScan(basePackageClasses = KeycloakSecurityComponents.class)
@Order(Ordered.HIGHEST_PRECEDENCE)
public class SecurityConfig extends KeycloakWebSecurityConfigurerAdapter {




    @Autowired
    public KeycloakClientRequestFactory keycloakClientRequestFactory;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        KeycloakAuthenticationProvider keycloakAuthenticationProvider = keycloakAuthenticationProvider();
        keycloakAuthenticationProvider.setGrantedAuthoritiesMapper(new SimpleAuthorityMapper());
        auth.authenticationProvider(keycloakAuthenticationProvider);
    }

    @Bean
    public KeycloakSpringBootConfigResolver KeycloakConfigResolver() {
        return new KeycloakSpringBootConfigResolver();
    }

    @Bean
    @Override
    protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {
        return new RegisterSessionAuthenticationStrategy(new SessionRegistryImpl());
    }



    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/v2/api-docs",  "/swagger-resources/**",  "/swagger-ui.html", "/elm/notification/eem");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        super.configure(http);
        http
                .csrf().disable()
                .cors().and()
                .addFilterBefore(new AuthorizationFilter(), KeycloakPreAuthActionsFilter.class)
                .authorizeRequests()
                .antMatchers(HttpMethod.POST,"/portal/elm/experiment").hasAnyRole("Experimenter")
                .antMatchers(HttpMethod.GET,"/portal/elm/experiment").hasAnyRole("Experimenter", "SiteManager")
                .antMatchers(HttpMethod.PUT,"/portal/elm/experiment/{^[\\\\d]}/status").hasAnyRole("SiteManager")
                .antMatchers(HttpMethod.PUT,"/portal/elm/experiment/{^[\\\\d]}/timeslot").hasAnyRole("Experimenter","SiteManager")
                .antMatchers(HttpMethod.POST,"/portal/elm/experiment/{^[\\\\d]}/action/deploy").hasAnyRole("Experimenter")
                .antMatchers(HttpMethod.POST,"/portal/elm/experiment/{^[\\\\d]}/action/execute").hasAnyRole("Experimenter")
                .antMatchers(HttpMethod.POST,"/portal/elm/experiment/{^[\\\\d]}/action/terminate").hasAnyRole("Experimenter")
                .antMatchers(HttpMethod.POST,"/portal/elm/experiment/{^[\\\\d]}/action/abort").hasAnyRole("Experimenter")
                .antMatchers(HttpMethod.GET,"/portal/elm/experiment/{^[\\\\d]$}").hasAnyRole("Experimenter", "SiteManager")
                .antMatchers(HttpMethod.DELETE,"/portal/elm/experiment/{^[\\\\d]$}").hasAnyRole("Experimenter")
                .anyRequest()
                .permitAll();
    }



}


