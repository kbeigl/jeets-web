package org.jeets.web.vaadin.security;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.VaadinServiceInitListener;
import org.jeets.web.vaadin.ui.views.LoginView;
import org.springframework.stereotype.Component;

/**
 * Restricting access to Vaadin views
 *
 * <p>Spring Security restricts access to content based on paths. Vaadin applications are
 * single-page applications. This means that they do not trigger a full browser refresh when you
 * navigate between views, even though the path does change. To secure a Vaadin application, we need
 * to wire Spring Security to the Vaadin navigation system.
 */
@Component
// Registers the listener Vaadin will pick up on startup.
public class ConfigureUIServiceInitListener implements VaadinServiceInitListener {

  /**
   * Listen for the initialization of the UI (the internal root component in Vaadin) and then add a
   * listener before every view transition.
   */
  @Override
  public void serviceInit(ServiceInitEvent event) {
    event
        .getSource()
        .addUIInitListener(
            uiEvent -> {
              final UI ui = uiEvent.getUI();
              ui.addBeforeEnterListener(this::authenticateNavigation);
            });
  }

  /** Reroute all requests to the login, if the user is not logged in. */
  private void authenticateNavigation(BeforeEnterEvent event) {
    if (!org.jeets.web.vaadin.ui.views.LoginView.class.equals(event.getNavigationTarget())
        && !SecurityUtils.isUserLoggedIn()) {
      event.rerouteTo(LoginView.class);
    }
  }
}
