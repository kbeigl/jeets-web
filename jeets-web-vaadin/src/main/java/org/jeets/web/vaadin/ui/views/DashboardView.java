package org.jeets.web.vaadin.ui.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import org.jeets.web.spring.traccar.TraccarAuthentication;
import org.jeets.web.spring.traccar.TraccarModel;
import org.jeets.web.vaadin.ui.MainLayout;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "", layout = MainLayout.class)
@PageTitle("Dashboard | Traccar Virtex")
public class DashboardView extends VerticalLayout {

	private static final long serialVersionUID = 1L;
//	@Autowire here > available after construction !!
	private TraccarModel traccarModel;

	@Autowired // for construction
	public DashboardView( TraccarModel model) {
//		move code to @Postconstruct ?
		traccarModel = model;
		addClassName("dashboard-view");
//		setDefaultHorizontalComponentAlignment(Alignment.CENTER);
		add(createUserLabel());
		add(createMyDevicesButton());
		if (TraccarAuthentication.isAdminOrManager())
			add(createAllDevicesButton());
	}

	//	TODO create label only once per session and re-use in views
//	OR create all infos in the Model with ModelAttributes !!
	private Component createUserLabel() {
//		TODO set user in @Model !?
		String userInfos = traccarModel.getRoleString() + " " 
				+ TraccarAuthentication.getTraccarUser().getName();
		H3 userLabel = new H3(userInfos);
		userLabel.addClassName("contact-stats");
		return userLabel;
	}

	/**
	 * Button to retrieve all devices. Can only be applied by Managers and Admins.
	 */
	private Component createAllDevicesButton() {
		Button button = new Button("Alle Geräte");
		button.addClickListener(clickEvent -> getAllDevices());
		return button;
	}

	/**
	 * Button to retrieve all devices of the logged in user.
	 */
	private Component createMyDevicesButton() {
		Button button = new Button("Meine Geräte");
		button.addClickListener(clickEvent -> getDevices());
		return button;
	}

	/**
	 * Retrieve all devices of the logged in user.
	 */
	private Object getDevices() {
		traccarModel.setUserDevices();
		System.out.println("Found " + traccarModel.getDevices().size() + " user Devices");
		return null;
	}

//	doesn#t work here ?? !!
//	@IsTraccarAdminOrManager
	private Object getAllDevices() {
//		as above
		traccarModel.setAllDevices();
		return null;
	}

}
