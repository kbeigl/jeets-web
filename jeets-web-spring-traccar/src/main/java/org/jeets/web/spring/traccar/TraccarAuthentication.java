package org.jeets.web.spring.traccar;

import java.util.Collection;
import org.openapitools.client.model.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * A Spring User bridges the Traccar User to Spring Security and supplies authentication states
 * live.
 *
 * <p>see www.traccar.org/documentation/user-management/
 */
public class TraccarAuthentication {

  //	for production ready login, password: rules reset encode verify etc.
  //	check out www.baeldung.com/security-spring
  //	      and www.baeldung.com/spring-security-registration

  //	"ROLE_" strings should not be used externally
  public static final String traccarAdmin = "ROLE_ADMIN";
  public static final String traccarManager = "ROLE_MANAGER";
  public static final String traccarUser = "ROLE_USER";

  /** Retrieve User from Spring Security and use as key to access role based methods. */
  public static User getTraccarUser() {
    return (User) getAuthentication().getPrincipal();
  }

  public static boolean isAdmin() {
    return hasRole(traccarAdmin);
  }

  public static boolean isAdminOrManager() {
    return (hasRole(traccarAdmin) || hasRole(traccarManager));
  }

  public static boolean isManager() {
    return hasRole(traccarManager);
  }

  /**
   * Method for completeness. Only makes sense to distinguish user from unregistered user on
   * unprotected pages. Otherwise every unauthorized method can be accessed by a normal user.
   */
  public static boolean isUser() {
    return hasRole(traccarUser);
  }

  //	public static boolean isGuest() { .. }

  private static boolean hasRole(String role) {
    if (getAuthorities().stream().anyMatch(a -> a.getAuthority().equals(role))) {
      return true;
    }
    return false;
  }

  private static Collection<? extends GrantedAuthority> getAuthorities() {
    return getAuthentication().getAuthorities();
  }

  /** Always retrieve the current authentication live. */
  public static Authentication getAuthentication() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null) {
      System.err.println("Spring Security does not provide authentication");
      //			now what ? continue here
    }
    return authentication; // can be null
  }
}
