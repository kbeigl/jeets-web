package org.jeets.web.vaadin.ui;

import org.jeets.web.vaadin.ui.views.DashboardView;
import org.jeets.web.vaadin.ui.views.ListView;
import org.openapitools.client.model.User;
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

//	vaadin.com/directory/component/googleanalyticstracker

//	@Autowired
//	private TraccarModel traccarModel;

//	@Autowired // required in constructor
//	public MainLayout( TraccarModel model) {
//		traccarModel = model;
	public MainLayout() {
		createHeader();
		createDrawer();
	}

	private void createHeader() {
		User user = (User) // store once in @Model
				SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String userName = user.getName();
		H1 logo = new H1("Logged in: " + userName); // and role
		logo.addClassName("logo");

		Anchor logout = new Anchor("logout", "Log out");

		HorizontalLayout header = new HorizontalLayout(new DrawerToggle(), logo, logout);
//		make the logo take up all the extra space and push the logout button to the far right
		header.expand(logo);
		header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
		header.setWidth("100%");
		header.addClassName("header");

		addToNavbar(header);
	}

	private void createDrawer() {
		RouterLink listLink = new RouterLink("AZ Modul", ListView.class);
		listLink.setHighlightCondition(HighlightConditions.sameLocation());
		RouterLink dashLink = new RouterLink("Dashboard", DashboardView.class);
		addToDrawer(new VerticalLayout(dashLink, listLink));
	}

}
