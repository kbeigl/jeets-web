package org.jeets.web.vaadin.ui;

import org.jeets.web.spring.traccar.TraccarAuthentication;
import org.jeets.web.spring.traccar.TraccarModel;
import org.jeets.web.vaadin.ui.views.DashboardView;
import org.jeets.web.vaadin.ui.views.ListView;
import org.openapitools.client.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.HighlightConditions;
import com.vaadin.flow.router.RouterLink;

import com.vaadin.flow.component.orderedlayout.FlexComponent;

@CssImport("./styles/shared-styles.css")
public class MainLayout extends AppLayout {

	private static final long serialVersionUID = 1L;
//	vaadin.com/directory/component/googleanalyticstracker

	private TraccarModel traccarModel;

	@Autowired // required in constructor
	public MainLayout( TraccarModel model) {
		traccarModel = model;
		createHeader();
		createDrawer();
	}

//	TODO create label only once per session and re-use in views
	private void createHeader() {
		// Component #1
		H1 logo = new H1("JeeTS Web Traccar");
		logo.addClassName("logo");
		// #2
		String userName = TraccarAuthentication.getTraccarUser().getName();
		String userRole = traccarModel.getRoleString();
		H1 loggedUser = new H1(userRole + ": " + userName);
		loggedUser.addClassName("user");
		// #3
		Anchor logout = new Anchor("logout", "Log out");

		HorizontalLayout header = new HorizontalLayout(new DrawerToggle(), logo, loggedUser, logout);
//		make the logo take up all the extra space and push the logout button to the far right
		header.expand(logo);
		header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
		header.setWidth("100%");
		header.addClassName("header");

		addToNavbar(header);
	}

//	TODO: i18n
	private void createDrawer() {
		RouterLink listLink = new RouterLink("AZ Modul", ListView.class);
		listLink.setHighlightCondition(HighlightConditions.sameLocation());
		RouterLink dashLink = new RouterLink("Dashboard", DashboardView.class);
		addToDrawer(new VerticalLayout(dashLink, listLink));
	}

}
