package org.matsim.tutorial.class2016.withinday;

import java.util.Random;

import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.Scenario;
import org.matsim.api.core.v01.TransportMode;
import org.matsim.api.core.v01.network.Link;
import org.matsim.api.core.v01.population.Person;
import org.matsim.core.api.experimental.events.EventsManager;
import org.matsim.core.gbl.MatsimRandom;
import org.matsim.core.mobsim.framework.MobsimAgent;
import org.matsim.core.mobsim.framework.MobsimDriverAgent;
import org.matsim.core.mobsim.qsim.QSim;
import org.matsim.core.mobsim.qsim.interfaces.MobsimVehicle;
import org.matsim.facilities.Facility;
import org.matsim.vehicles.Vehicle;

public class MyAgent implements MobsimDriverAgent {

	Scenario sc;
	EventsManager ev;
	QSim qsim;
	Id<Link> currentLinkId;
	MobsimVehicle veh;
	State currentState;
	Random rand = MatsimRandom.getLocalInstance();
	public MyAgent(Scenario sc, EventsManager ev, QSim qsim, Id<Link> startLinkId, MobsimVehicle veh) {
		this.sc=sc;
		this.ev=ev;
		this.qsim=qsim;
		this.currentLinkId = startLinkId;
		this.veh = veh;
		this.currentState=State.ACTIVITY;
	}

	@Override
	public Id<Link> getCurrentLinkId() {
		// TODO Auto-generated method stub
		return currentLinkId;
	}

	@Override
	public Id<Link> getDestinationLinkId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getMode() {
		// TODO Auto-generated method stub
		return TransportMode.car;
	}

	@Override
	public Id<Person> getId() {
		// TODO Auto-generated method stub
		return Id.createPersonId("testPerson");
	}

	@Override
	public State getState() {
		// TODO Auto-generated method stub
		return currentState;
	}

	@Override
	public double getActivityEndTime() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void endActivityAndComputeNextState(double now) {
		this.currentState = State.LEG;
	}

	@Override
	public void endLegAndComputeNextState(double now) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setStateToAbort(double now) {
		// TODO Auto-generated method stub

	}

	@Override
	public Double getExpectedTravelTime() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Double getExpectedTravelDistance() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void notifyArrivalOnLinkByNonNetworkMode(Id<Link> linkId) {
		// TODO Auto-generated method stub

	}

	@Override
	public Facility<? extends Facility<?>> getCurrentFacility() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Facility<? extends Facility<?>> getDestinationFacility() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Id<Link> chooseNextLinkId() {
		Link currentLink = sc.getNetwork().getLinks().get(currentLinkId);
		int noOfOutLinks = currentLink.getToNode().getOutLinks().size();
		int idx;
		if (noOfOutLinks == 1){
			idx = 0;
		}
		else {
			Link possibleNextLink;
			int i = 0;
			do {
				
			idx = rand.nextInt(noOfOutLinks);
			possibleNextLink= (Link) currentLink.getToNode().getOutLinks().values().toArray()[idx];
			i++;
			if (i>100){
				break;
			}
			} while (possibleNextLink.getToNode().equals(currentLink.getFromNode()));
			
		}

		
		Id<Link> nextLinkId = (Id<Link>) currentLink.getToNode().getOutLinks().keySet().toArray()[idx];
		return nextLinkId;
	}

	@Override
	public void notifyMoveOverNode(Id<Link> newLinkId) {
		this.currentLinkId = newLinkId;
	}

	@Override
	public boolean isWantingToArriveOnCurrentLink() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setVehicle(MobsimVehicle veh) {
		this.veh = veh;
	}

	@Override
	public MobsimVehicle getVehicle() {
		// TODO Auto-generated method stub
		return this.veh;
	}

	@Override
	public Id<Vehicle> getPlannedVehicleId() {
		// TODO Auto-generated method stub
		return this.veh.getId();
	}

}
