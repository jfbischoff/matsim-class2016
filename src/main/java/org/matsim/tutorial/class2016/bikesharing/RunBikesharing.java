package org.matsim.tutorial.class2016.bikesharing;

import org.matsim.api.core.v01.Scenario;
import org.matsim.contrib.otfvis.OTFVisLiveModule;
import org.matsim.core.config.Config;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.controler.AbstractModule;
import org.matsim.core.controler.Controler;
import org.matsim.core.controler.OutputDirectoryHierarchy.OverwriteFileSetting;
import org.matsim.core.scenario.ScenarioUtils;

import com.google.inject.Binder;

public class RunBikesharing {

	public static void main(String[] args) {
		Config config = ConfigUtils.loadConfig("config.xml");
		
		config.controler().setOverwriteFileSetting(OverwriteFileSetting.deleteDirectoryIfExists);
		Scenario scenario = ScenarioUtils.loadScenario(config);
		Controler controler = new Controler(scenario);
		final BikeSharingStations stations = new BikeSharingStations(scenario.getNetwork());
		
		controler.addOverridingModule(new AbstractModule() {
		
			@Override
			public void install() {
				bind(BikeSharingStations.class).toInstance(stations);
				this.install(new BikesharingQsimModule());
			}
		});
		controler.addOverridingModule(new OTFVisLiveModule());

		
		controler.run();
	}
	
}
