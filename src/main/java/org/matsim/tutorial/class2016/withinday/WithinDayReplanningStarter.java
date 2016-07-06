/* *********************************************************************** *
 * project: org.matsim.*
 *                                                                         *
 * *********************************************************************** *
 *                                                                         *
 * copyright       : (C) 2012 by the members listed in the COPYING,        *
 *                   LICENSE and WARRANTY file.                            *
 * email           : info at matsim dot org                                *
 *                                                                         *
 * *********************************************************************** *
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU General Public License as published by  *
 *   the Free Software Foundation; either version 2 of the License, or     *
 *   (at your option) any later version.                                   *
 *   See also COPYING, LICENSE and WARRANTY file                           *
 *                                                                         *
 * *********************************************************************** */

package org.matsim.tutorial.class2016.withinday;

import com.google.inject.Provider;
import org.apache.log4j.Logger;
import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.Scenario;
import org.matsim.api.core.v01.TransportMode;
import org.matsim.api.core.v01.network.Link;
import org.matsim.api.core.v01.population.Person;
import org.matsim.contrib.otfvis.OTFVis;
import org.matsim.core.api.experimental.events.EventsManager;
import org.matsim.core.config.Config;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.config.groups.QSimConfigGroup;import org.matsim.core.config.groups.QSimConfigGroup.VehiclesSource;
import org.matsim.core.controler.AbstractModule;
import org.matsim.core.controler.Controler;
import org.matsim.core.controler.OutputDirectoryHierarchy.OverwriteFileSetting;
import org.matsim.core.gbl.MatsimRandom;
import org.matsim.core.mobsim.framework.AgentSource;
import org.matsim.core.mobsim.framework.Mobsim;
import org.matsim.core.mobsim.framework.MobsimDriverAgent;
import org.matsim.core.mobsim.framework.MobsimFactory;
import org.matsim.core.mobsim.qsim.QSim;
import org.matsim.core.mobsim.qsim.QSimUtils;
import org.matsim.core.mobsim.qsim.interfaces.MobsimVehicle;
import org.matsim.core.mobsim.qsim.interfaces.Netsim;
import org.matsim.core.mobsim.qsim.qnetsimengine.QVehicle;
import org.matsim.facilities.Facility;
import org.matsim.vehicles.Vehicle;
import org.matsim.vehicles.VehicleImpl;
import org.matsim.vehicles.VehicleType;
import org.matsim.vehicles.VehicleTypeImpl;
import org.matsim.vis.otfvis.OTFClientLive;

import javax.inject.Inject;

public class WithinDayReplanningStarter {

	public static void main(String[] args) {

		// I want a config out of nothing:
		Config config = ConfigUtils.createConfig() ;

		// set some config stuff:
		config.network().setInputFile("input/network.xml") ;
		config.controler().setLastIteration(0) ;
		config.qsim().setEndTime(26.*3600) ;
		config.qsim().setSnapshotStyle( QSimConfigGroup.SnapshotStyle.queue ) ;
		config.controler().setOutputDirectory("output/withinday");
		config.controler().setOverwriteFileSetting(OverwriteFileSetting.deleteDirectoryIfExists);
		// base the controler on that:
		Controler ctrl = new Controler( config ) ;
		final VehicleType car = new VehicleTypeImpl(Id.create("car", VehicleType.class));
		car.setMaximumVelocity(60.0/3.6);
		car.setPcuEquivalents(1.0);
		
		
		final VehicleType bike = new VehicleTypeImpl(Id.create("bike", VehicleType.class));
		bike.setMaximumVelocity(20.0/3.6);
		bike.setPcuEquivalents(0.2);
		
		
		ctrl.addOverridingModule(new AbstractModule() {
			@Override
			public void install() {
				bindMobsim().toProvider(new Provider<Mobsim>() {

					@Inject Scenario sc;
					@Inject EventsManager ev;

					@Override
					public Mobsim get() {
						// take the default mobsim, but since the population is empty, it will not be filled with demand:
						final QSim qsim = QSimUtils.createDefaultQSim(sc, ev);

						// add my own agent(s):
						qsim.addAgentSource(new AgentSource() {
							@Override
							public void insertAgentsIntoMobsim() {
								final Id<Link> startLinkId = (Id<Link>) (sc.getNetwork().getLinks().keySet().toArray())[0];
								final MobsimVehicle veh1 = new QVehicle(new VehicleImpl(Id.create("testVehicle", Vehicle.class), car));
								final MobsimVehicle bike1 = new QVehicle(new VehicleImpl(Id.create("testBike", Vehicle.class), bike));
								qsim.addParkedVehicle(veh1, startLinkId);
								qsim.addParkedVehicle(bike1, startLinkId);
								qsim.insertAgentIntoMobsim(new MyAgent(sc, ev, qsim, startLinkId, veh1));
								qsim.insertAgentIntoMobsim(new MyAgent(sc, ev, qsim, startLinkId, bike1));
								// (the Id of the parked vehicle needs to be known to the agent, otherwise it will not work!)
							}
						});

						// add otfvis live.  Can't do this in core since otfvis is not accessible from there.
						 OTFClientLive.run(sc.getConfig(), OTFVis.startServerAndRegisterWithQSim(sc.getConfig(), sc, ev, qsim));

						return qsim;
					}
				});
			}
		});
		ctrl.run();

	}

}

