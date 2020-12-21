package org.jeets.web.spring.traccar;

import java.util.ArrayList;
import java.util.List;
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.api.DefaultApi;
import org.openapitools.client.auth.HttpBasicAuth;
import org.openapitools.client.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

/**
 * Authenticate against Traccar Server via REST API and provide Authentication for Spring Security
 * and Spring MVC.
 *
 * <p>The Authentication creates an instantiated Open API Client with an authorized User for a
 * session. The method also sets the three possible Traccar roles for Spring authorities management.
 *
 * <p>see: www.marcobehler.com/guides/spring-security
 *
 * <p>Note that this class can be used in plain Spring projects and does not require Vaadin or other
 * frontend designs (may be moved to a separate project)!
 */
public class TraccarAuthenticationProvider implements AuthenticationProvider {

  @Value("${rest.protocol}${rest.server}${rest.api}")
  private String restServerApi;

  /**
   * Since we can not rely on having a Session (to set @SessionAttributes and @ModelAttribute-s) at
   * this point (via Spring Configuration) we store the Traccar User Entity as Principal and attach
   * its authenticated and authorized Traccar instance to the Authentication which is managed in the
   * normal Spring life cycle and destroyed properly at the end of a session.
   */
  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {

    // Server must be running
    System.out.println("create REST API for " + restServerApi);
    DefaultApi traccarApi = createTraccarApi(restServerApi);

    String username = authentication.getPrincipal().toString();
    //		credentials / password are visible at this point
    String password = authentication.getCredentials().toString();
    System.out.println("authenticate " + username + ", " + password);
    //		TODO define explicit vaadin user, in case Traccar API is unreachable .. implement
    // UserDetailsService ?
    //		if ((!username.equals("vaadin")) || (!password.equals("kristof")))
    //			throw new BadCredentialsException("could not authenticate " + username + ":" + password);

    User user = authenticateHttpBasic(traccarApi, username, password);
    if (user == null) return null;
    //		throw AuthenticationException > BadCredentialsException

    //		credentials / password will be null-ed internally and cannot be recalled
    UsernamePasswordAuthenticationToken authenticationToken =
        new UsernamePasswordAuthenticationToken(user, password, authorize(user));
    authenticationToken.setDetails(traccarApi);
    return authenticationToken;
  }

  /**
   * Instantiate Open API client to manage the connection to the Traccar backend during the complete
   * session. Note that the user has not been authenticated yet and must be done in a consecutive
   * step.
   */
  private DefaultApi createTraccarApi(String serverUrl) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath(serverUrl);
    return new DefaultApi(defaultClient);
  }

  /**
   * Authenticate user and password against Traccar Backend and start a valid session user after
   * authorization.
   *
   * @return valid Traccar User on success or null
   */
  private User authenticateHttpBasic(DefaultApi traccarApi, String username, String password) {

    HttpBasicAuth basicAuth =
        (HttpBasicAuth) traccarApi.getApiClient().getAuthentication("basicAuth");
    basicAuth.setUsername(username);
    basicAuth.setPassword(password);

    try {
      User user = traccarApi.sessionPost(username, password);
      System.out.println("Logged in Traccar User " + user.getName());
      return user; // authenticated and authorized

    } catch (ApiException e) { // Unauthorized
      System.err.println("TODO: ApiException on Open API authentication.");
      //			e.printStackTrace();
      //  		branch api exception
      //  	    new AuthenticationServiceException("problems with OpenAPI");
      //  		InsufficientAuthenticationException
      //  		ProviderNotFoundException
      //  		SessionAuthenticationException
      //			throw new BadCredentialsException("could not authenticate " + username + ":" + password);
      //			throw new UsernameNotFoundException("could not find " + username);
      //			AuthenticationCredentialsNotFoundException
    }
    return null; // NOT authenticated > 'incorrect user or password'
  }

  /**
   * Evaluate the Traccar User to grant Spring permissions. Only three roles available as described
   * at www.traccar.org/documentation/user-management
   */
  private List<SimpleGrantedAuthority> authorize(User user) {
    List<SimpleGrantedAuthority> grantedAuthorities = new ArrayList<SimpleGrantedAuthority>();

    if (user.getAdministrator()) {
      grantedAuthorities.add(new SimpleGrantedAuthority(TraccarAuthentication.traccarAdmin));
    }
    //		If user limit is set to 0, it means that user is not a manager.
    //		Difference between manager and regular user is the user limit.
    //		else !?
    if (user.getUserLimit() != 0) {
      grantedAuthorities.add(new SimpleGrantedAuthority(TraccarAuthentication.traccarManager));
    }
    //		every authenticated and authorized user is also a ROLE_USER - optional!
    grantedAuthorities.add(new SimpleGrantedAuthority(TraccarAuthentication.traccarUser));

    System.out.println("grant authorities " + grantedAuthorities);
    return grantedAuthorities; // at least one entry: ROLE_USER
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return authentication.equals(UsernamePasswordAuthenticationToken.class);
  }
}
