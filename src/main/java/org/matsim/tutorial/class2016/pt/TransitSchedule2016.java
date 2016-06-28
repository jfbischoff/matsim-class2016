package org.matsim.tutorial.class2016.pt;

import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.Scenario;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.scenario.ScenarioUtils;
import org.matsim.pt.transitSchedule.api.TransitLine;
import org.matsim.pt.transitSchedule.api.TransitRoute;
import org.matsim.pt.transitSchedule.api.TransitSchedule;

public class TransitSchedule2016 {

	public TransitSchedule2016() {

}
	public static void main(String[] args) {
		Scenario scenario = ScenarioUtils.loadScenario(ConfigUtils.loadConfig("config.xml"));
		TransitSchedule schedule = scenario.getTransitSchedule();
		System.out.println(schedule.getTransitLines().keySet());
		TransitLine re2 = schedule.getTransitLines().get(Id.create("re2",TransitLine.class)); 
		TransitRoute route_re2 = re2.getRoutes().get(Id.create("re2", TransitRoute.class));

	}

}
