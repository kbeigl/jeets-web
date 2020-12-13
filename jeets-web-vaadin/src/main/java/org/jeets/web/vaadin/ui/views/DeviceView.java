package org.jeets.web.vaadin.ui.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.jeets.web.spring.traccar.TraccarAuthentication;
import org.jeets.web.spring.traccar.TraccarModel;
import org.jeets.web.vaadin.ui.MainLayout;
import org.openapitools.client.model.Device;

@Route(value = "", layout = MainLayout.class)
@PageTitle("Traccar Devices | JeeTS Web")
public class DeviceView extends VerticalLayout {

	private static final long serialVersionUID = 1L;
	private TraccarModel traccarModel;
//	TODO DeviceForm form;
    Grid<Device> grid = new Grid<>(Device.class);

//	TODO: i18n

	public DeviceView(TraccarModel model) { // service
		traccarModel = model;
//		setDefaultHorizontalComponentAlignment(Alignment.CENTER);
//		addClassName("dashboard-view");
//		addClassName("list-view");
        setSizeFull();
        configureGrid();

//      form = new DeviceForm(devices);
//      form.addListener(DeviceForm.NewEvent.class, this::newDevice);
//      form.addListener(DeviceForm.SaveEvent.class, this::saveDevice);
//      form.addListener(DeviceForm.DeleteEvent.class, this::deleteDevice);
//      form.addListener(DeviceForm.CloseEvent.class, e -> closeEditor());

        Div content = new Div(grid); // add form
//      content.addClassName("content");
        content.setSizeFull();

        add(createDeviceBar(), content);
//		updateList();
		grid.setItems(traccarModel.getDevices());

//		closeEditor();
	}

	private void configureGrid() {
//        grid.addClassName("contact-grid");
//        grid.setSizeFull();
        grid.setColumns("name", "status", "phone", "uniqueId", "model");
//      grid.removeColumnByKey("company");
//        grid.addColumn(contact -> { analog Traccar 'group'
////      	models  company 2 contact Entity Relation
//        	Company company = contact.getCompany();
//        	return company == null ? "-" : company.getName();
//        }).setHeader("Company");

        grid.getColumns().forEach(col -> col.setAutoWidth(true));

//        grid.asSingleSelect().addValueChangeListener(evt -> editContact(evt.getValue()));
	}
	
	/**
	 * Symmetric positioning in one Component
	 */
	private HorizontalLayout createDeviceBar() {
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
		Button button = new Button("All Devices");
		button.setWidth("150px");
		button.addClickListener(clickEvent -> setAllDevices());
		return button;
	}

	/**
	 * Button to retrieve all devices of the logged in user.
	 */
	private Button createMyDevicesButton() {
		Button button = new Button("My Devices");
		button.setWidth("150px");
		button.addClickListener(clickEvent -> setUserDevices());
		return button;
	}

	/**
	 * Retrieve all devices of the logged in user.
	 */
	private void setUserDevices() {
		traccarModel.setUserDevices();
		grid.setItems(traccarModel.getDevices());
	}

//	@IsTraccarAdminOrManager doesn't work here ?? !!
	private void setAllDevices() {
		traccarModel.setAllDevices();
		grid.setItems(traccarModel.getDevices());
	}

}
