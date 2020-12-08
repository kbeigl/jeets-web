package org.jeets.web.vaadin.ui.views;

import java.time.LocalDateTime;
import java.time.Month;

import org.jeets.web.spring.traccar.TraccarModel;
import org.jeets.web.vaadin.ui.MainLayout;
import org.springframework.beans.factory.annotation.Autowired;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

/**
 * A sample Vaadin view class.
 * <p>
 * To implement a Vaadin view just extend any Vaadin component and
 * use @Route annotation to announce it in a URL as a Spring managed
 * bean.
 * Use the @PWA annotation make the application installable on phones,
 * tablets and some desktop browsers.
 * <p>
 * A new instance of this class is created for every new user and every
 * browser tab/window.
 */
//@PWA(name = "Vaadin Jee T.S.",
//        shortName = "Vaadin JeeTS App",
//        description = "This is a basic Vaadin frontend for the Traccar backend server.",
//        enableInstallPrompt = false)
//@CssImport("./styles/shared-styles.css")
@Route(value="list", layout = MainLayout.class)
// analog to Spring @Controller @RequestMapping("/")
@PageTitle("AZ Modul | Traccar Virtex")
public class ListView extends VerticalLayout {

	private static final long serialVersionUID = 1L;
	@Autowired
	private TraccarModel traccarModel;
	private DateTimePicker fromDate = new DateTimePicker("fromDate");
	private DateTimePicker toDate = new DateTimePicker("toDate");

	/**
     * The main window of the application as Vaadin view.
     * <p>
     * Build the initial UI state for the user accessing the application.
     */
	public ListView() {
//		public ListView(@Autowired TraccarModel model) {
//		traccarModel = model;

		Button button = new Button("Arbeitszeiten berechnen");
		button.addClickListener(clickEvent -> calculateWorkingTimes());
//		button.addClickListener(clickEvent -> this::calculateWorkingTimes);

//		LocalDateTime now = LocalDateTime.now();
//		toDate.setValue(now);

//		Day and duration for initial analysis
		fromDate.setValue(LocalDateTime.of(2020, Month.OCTOBER, 12, 9, 7));
		toDate.setValue(LocalDateTime.of(2020, Month.OCTOBER, 12, 18, 40));

		VerticalLayout layout = new VerticalLayout(fromDate, toDate, button);
//		layout.setDefaultVerticalComponentAlignment(Alignment.END);
		add(layout);
	}

	/**
	 * implicitly add results to UI
	 */
	private void calculateWorkingTimes( /* from and to DateTime */ ) {

		LocalDateTime fromTime = fromDate.getValue();
		LocalDateTime toTime = toDate.getValue();
		
//		TODO add TimeZone Berlin !! or times can deviate some hours
		Span result = new Span("Arbeitszeiten von " + fromTime + " bis " + toTime);
		add(result);

		result = new Span("calculate for " + traccarModel.getDevices().size() + " devices.");
		add(result);
		
//		result = new Span("Geräte: " + getAllDevices().size());
//		add(result);
	}

}
