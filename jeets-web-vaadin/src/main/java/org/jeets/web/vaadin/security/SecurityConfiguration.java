package org.jeets.web.vaadin.security;

import org.jeets.web.spring.traccar.TraccarAuthenticationProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

  private static final String LOGIN_URL = "/login";
  private static final String LOGOUT_SUCCESS_URL = LOGIN_URL;
  private static final String LOGIN_PROCESSING_URL = LOGIN_URL;
  private static final String LOGIN_FAILURE_URL = LOGIN_URL + "?error";

  /** Method to block unauthenticated requests to all pages, except the login page. */
  @Override
  protected void configure(HttpSecurity http) throws Exception {

    // Disables cross-site request forgery protection, which Vaadin already has
    http.csrf()
        .disable()
        // Uses CustomRequestCache to track unauthorized requests
        // so that users are redirected appropriately after login.
        .requestCache()
        .requestCache(new CustomRequestCache())
        .and()
        // Turn on authorization.
        .authorizeRequests()
        // Allow all internal traffic from the Vaadin framework.
        .requestMatchers(SecurityUtils::isFrameworkInternalRequest)
        .permitAll()
        // use for exclusive admin or manager views
        //    .antMatchers("/admin").hasAuthority("ROLE_ADMIN")
        //    .antMatchers("/manager").hasAnyAuthority("ROLE_ADMIN", "ROLE_MANAGER")
        // OR .antMatchers("/admin").hasRole("ADMIN")
        //    .antMatchers("/callcenter").hasAnyRole("ADMIN", "MANAGER")
        // Allows all authenticated traffic.
        .anyRequest()
        .authenticated()
        .and()
        // Enables form-based login and permits unauthenticated access to it.
        .formLogin()
        .loginPage(LOGIN_URL)
        .permitAll()
        // Configures the login page URLs.
        .loginProcessingUrl(LOGIN_PROCESSING_URL)
        .failureUrl(LOGIN_FAILURE_URL)
        .and()
        // Configures the logout URL.
        .logout()
        .logoutSuccessUrl(LOGOUT_SUCCESS_URL)
        .and()
        .httpBasic() // UNCLEAR doesn't authorize original Traccar UI? not required as is!
    //		.httpBasic(httpBasicCustomizer)
    ;
  }

  /**
   * Method to configure test users.
   *
   * <p>Defines a single user with the username "user" and password "password" in an in-memory
   * DetailsManager. @Bean @Override public UserDetailsService userDetailsService() { UserDetails
   * user = User.withUsername("user") .password("{noop}password") .roles("USER").build();
   *
   * <p>return new InMemoryUserDetailsManager(user); }
   */

  /** Exclude Vaadin-framework communication and static assets from Spring Security. */
  @Override
  public void configure(WebSecurity web) {
    web.ignoring()
        .antMatchers(
            "/VAADIN/**",
            "/favicon.ico",
            "/robots.txt",
            "/manifest.webmanifest",
            "/sw.js",
            "/offline.html",
            "/icons/**",
            "/images/**",
            "/styles/**",
            "/h2-console/**");
  }

  /** apply TraccarAuthenticationProvider in Spring. */
  @Bean
  public AuthenticationProvider authenticationProvider() {
    System.out.println("return new TraccarAuthenticationProvider ...");
    return new TraccarAuthenticationProvider();
  }

  //    @Bean
  //    public BCryptPasswordEncoder passwordEncoder() {
  //        return new BCryptPasswordEncoder();
  //    }

}
