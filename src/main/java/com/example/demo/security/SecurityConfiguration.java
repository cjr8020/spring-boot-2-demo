package com.example.demo.security;

import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;


/**
 * Provides overall Security configuration for a typical SpringBoot
 * microservice by implementing the following:
 * 1. management (actuator) endpoint is secured with  Basic Authentication.
 * 2. application endpoint resources are secured with OAuth2/JWT-based authentication.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

  private static final String ACTUATOR_SECURITY_ROLE = "ACTUATOR";


  /* =================  Management Endpoint Security ==================== */

  /* management.security.user.name */
  @Value("${management.security.user.name}")
  private String actuatorUserName;
  /* management.security.user.password */
  @Value("${management.security.user.password}")
  private String actuatorUserPassword;

  /**
   * Actuator Security Config.
   * Important: this configuration load order is higher than the default of 100,
   * and is also higher than the Resource Server Config with the wild card pattern
   * matching.  This order allows us to match any actuator requests first and
   * apply system user basic auth to those requests.
   * For all other requests a higher order Resource Server configuration with its
   * wild card pattern matching will pick up all other requests.
   * The reason the order number is not 0 or 1 is to leave room for projects to create
   * higher order adapters if needed.
   */
  @Order(1)
  @Configuration
  public static class ActuatorSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Bean
    AuthenticationEntryPoint authenticationEntryPoint() {
      BasicAuthenticationEntryPoint entryPoint = new BasicAuthenticationEntryPoint();
      entryPoint.setRealmName("actuator realm");
      return entryPoint;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
      //@formatter:off
      http
          .antMatcher("/actuator/**")
          .authorizeRequests()
          .anyRequest().hasRole(ACTUATOR_SECURITY_ROLE)
          .and()
          .httpBasic()
          .authenticationEntryPoint(authenticationEntryPoint());
      //@formatter:on
    }
  }


  /**
   * User Details Service for the Actuator WebSecurityConfigurerAdapter.
   *
   * @return UserDetailsService
   */
  @Bean
  public UserDetailsService userDetailsService() {
    InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();

    // actuator authorized user
    manager.createUser(
        User
            .withUsername(actuatorUserName)
            .password("{noop}"+actuatorUserPassword)
            .roles(ACTUATOR_SECURITY_ROLE)
            .build()
    );

    return manager;
  }


  /* =================  Application Endpoint Security ==================== */

  @Value("${oauth.signingKey}")
  private String signingKey;

  /**
   * JWT token converter allows us to apply our signing key to enable token validation.
   * @return JwtAccessTokenConverter token converter
   */
  @Bean
  public JwtAccessTokenConverter accessTokenConverter() {
    JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
    converter.setSigningKey(signingKey);
    converter.setAccessTokenConverter(new SecurityConfiguration.JwtConverter());
    return converter;
  }

  /**
   * Token store with our JWT token converter.
   * @return TokenStore
   */
  @Bean
  public TokenStore tokenStore() {
    return new JwtTokenStore(accessTokenConverter());
  }

  /**
   * Resource Server Tokens Services with our TokenStore injected.
   * @return ResourceServerTokenServices
   */
  @Bean
  public ResourceServerTokenServices tokenServices() {
    DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
    defaultTokenServices.setTokenStore(tokenStore());
    defaultTokenServices.setSupportRefreshToken(true);
    return defaultTokenServices;
  }

  /**
   * JWT Converter whose primary purpose is to copy JWT content into
   * OAuth2Authentication enabling access to JWT claims and other details.
   */
  public static class JwtConverter
      extends DefaultAccessTokenConverter {

    @Override
    public OAuth2Authentication extractAuthentication(Map<String, ?> map) {
      OAuth2Authentication auth = super.extractAuthentication(map);
      auth.setDetails(map); // copy JWT content into Authentication
      return auth;
    }
  }

  // TODO: disable resource server
//  @Configuration
//  @EnableResourceServer
//  @EnableGlobalMethodSecurity(prePostEnabled = true)
  public static class OAuthResourceServerConfiguration extends ResourceServerConfigurerAdapter {

    @Autowired
    private TokenStore tokenStore;

    @Autowired
    private JwtAccessTokenConverter accessTokenConverter;

    @Autowired
    private ResourceServerTokenServices tokenServices;

    @Override
    public void configure(HttpSecurity http) throws Exception {
      http
          .antMatcher("/**")
          .authorizeRequests()
          .anyRequest().authenticated()
          .and()
          .sessionManagement()
          .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
          .and()
          .csrf().disable()
          .exceptionHandling()
          .authenticationEntryPoint(
              (request, response, exception)
                  -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED));
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
      resources.tokenServices(tokenServices);
      resources.resourceId(null);  // OrchIS generated JWT tokens do not contain resourceId
    }

  }



}
