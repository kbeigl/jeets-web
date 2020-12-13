package org.jeets.web.vaadin.ui.views;

import java.time.LocalDateTime;
import java.time.Month;

import org.jeets.web.spring.traccar.TraccarModel;
import org.jeets.web.vaadin.ui.MainLayout;
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
@PageTitle("Traccar Selections | JeeTS Web")
public class ListView extends VerticalLayout {

	private static final long serialVersionUID = 1L;

	private TraccarModel traccarModel;
	private DateTimePicker fromDate = new DateTimePicker("fromDate");
	private DateTimePicker toDate = new DateTimePicker("toDate");

	public ListView(TraccarModel model) {
			traccarModel = model;

//		TODO create ParameterBar
		
		Button button = new Button("calculate");
		button.addClickListener(clickEvent -> calculateWithParameters());

//		LocalDateTime now = LocalDateTime.now();
//		Day and duration for initial analysis
		fromDate.setValue(LocalDateTime.of(2020, Month.OCTOBER, 12,  9,  7));
		  toDate.setValue(LocalDateTime.of(2020, Month.OCTOBER, 12, 18, 40));
//		toDate.setValue(now);

		VerticalLayout layout = new VerticalLayout(fromDate, toDate, button);
//		layout.setDefaultVerticalComponentAlignment(Alignment.END);
		add(layout);
	}

	private void calculateWithParameters( /* from and to DateTime */ ) {

		LocalDateTime fromTime = fromDate.getValue();
		LocalDateTime toTime = toDate.getValue();

//		TODO add TimeZone Berlin !! or times can deviate some hours
//		fromDate.getDatePickerI18n();
//		fromDate.getLocale();
//		fromDate.getTranslation(key, params);
//		fromDate.isWeekNumbersVisible();
		
		Span result = new Span("Arbeitszeiten von " + fromTime + " bis " + toTime);
//		add(result);

		result = new Span("calculate for " + traccarModel.getDevices().size() + " devices.");
		add(result);
		
//		result = new Span("Geräte: " + getAllDevices().size());
//		add(result);
	}

}
