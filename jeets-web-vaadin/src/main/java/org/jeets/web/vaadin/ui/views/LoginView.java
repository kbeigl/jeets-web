package org.jeets.web.vaadin.ui.views;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route("login")
@PageTitle("Anmeldung | Traccar Virtex")
public class LoginView extends VerticalLayout implements BeforeEnterObserver {

  // see vaadin.com/components/vaadin-login/java-examples for i18n etc.
  private LoginForm login = new LoginForm();

  public LoginView() {
    addClassName("login-view");
    setSizeFull();
    setAlignItems(Alignment.CENTER);
    setJustifyContentMode(JustifyContentMode.CENTER);

    login.setAction("login");

    add(new H1("jeets.web.traccar v4.10"), login); // getVersion
  }

  // TODO: error NPEx TraccarAuthenticationProvider.authorize
  //        org.openapitools.client.ApiException: Unauthorized
  @Override
  public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
    // inform the user about an authentication error
    if (beforeEnterEvent.getLocation().getQueryParameters().getParameters().containsKey("error")) {
      login.setError(true);
    }
  }
}
