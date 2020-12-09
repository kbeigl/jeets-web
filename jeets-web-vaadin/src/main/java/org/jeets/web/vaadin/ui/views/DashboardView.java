package org.jeets.web.vaadin.ui.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
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
	private TraccarModel traccarModel;

//	TODO: i18n for ALL strings

	@Autowired
	public DashboardView(TraccarModel model) {
		traccarModel = model;
//		addClassName("dashboard-view");
//		setDefaultHorizontalComponentAlignment(Alignment.CENTER);
		add(createDeviceButtons());
	}

	private Component createDeviceButtons() {
		HorizontalLayout header = new HorizontalLayout(createMyDevicesButton());
		header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
		header.setWidth("100%");
		if (TraccarAuthentication.isAdminOrManager())
			header.add(createAllDevicesButton());
		return header;
	}

	/**
	 * Button to retrieve all devices. Can only be applied by Managers and Admins.
	 */
	private Button createAllDevicesButton() {
		Button button = new Button("Alle Geräte");
		button.setWidth("150px");
		button.addClickListener(clickEvent -> getAllDevices());
		return button;
	}

	/**
	 * Button to retrieve all devices of the logged in user.
	 */
	private Button createMyDevicesButton() {
		Button button = new Button("Meine Geräte");
		button.setWidth("150px");
		button.addClickListener(clickEvent -> getDevices());
		return button;
	}

	/**
	 * Retrieve all devices of the logged in user.
	 */
	private Object getDevices() {
		traccarModel.setUserDevices(); // !!
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
