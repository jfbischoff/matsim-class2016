package org.matsim.tutorial.class2016.bikesharing;

import org.matsim.api.core.v01.Coord;
import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.TransportMode;
import org.matsim.api.core.v01.events.ActivityEndEvent;
import org.matsim.api.core.v01.events.PersonDepartureEvent;
import org.matsim.api.core.v01.network.Link;
import org.matsim.api.core.v01.population.Activity;
import org.matsim.api.core.v01.population.Leg;
import org.matsim.api.core.v01.population.Person;
import org.matsim.api.core.v01.population.Plan;
import org.matsim.api.core.v01.population.PlanElement;
import org.matsim.api.core.v01.population.Route;
import org.matsim.core.api.experimental.events.EventsManager;
import org.matsim.core.mobsim.framework.MobsimDriverAgent;
import org.matsim.core.mobsim.qsim.QSim;
import org.matsim.core.mobsim.qsim.interfaces.MobsimVehicle;
import org.matsim.core.population.LegImpl;
import org.matsim.core.population.PopulationUtils;
import org.matsim.core.population.routes.GenericRouteImpl;
import org.matsim.core.utils.geometry.CoordUtils;
import org.matsim.facilities.Facility;
import org.matsim.vehicles.Vehicle;

public class BikeSharingAgent implements MobsimDriverAgent {

	private QSim qsim;
	private BikeSharingStations stations;
	private EventsManager events;
	
	private Person p;
	enum BikesharingState  {ACTIVITY, WALKTOBIKE, UNPARKBIKE, RIDE, PARKBIKE, WALKFROMBIKE};
	
	private BikesharingState currentBikesharingState = BikesharingState.ACTIVITY;
	private State currentState = State.ACTIVITY;
	private String currentMode;
	private Plan plan;
	private PlanElement currentPlanElement; 
	private int currentPlanElementIndex = 0;
	private double currentExpectedArrivalTime ;

	
	
	
	public BikeSharingAgent(QSim qsim, BikeSharingStations stations, Person p) {
		this.qsim = qsim;
		this.stations = stations;
		this.p = p;
		
		this.plan = p.getSelectedPlan();
		this.currentPlanElement = plan.getPlanElements().get(currentPlanElementIndex);
		
	}

	@Override
	public State getState() {
		// TODO Auto-generated method stub
		return currentState;
	}

	@Override
	public double getActivityEndTime() {
		if (currentPlanElement instanceof Activity){
			return ((Activity) currentPlanElement).getEndTime();
		}
		return 0;
	}

	@Override
	public void endActivityAndComputeNextState(double now) {
		if (this.currentBikesharingState.equals(BikesharingState.ACTIVITY)){
			// Route suchen & agent losschicken
			Activity oldActivity = (Activity) this.currentPlanElement;
			Activity nextActivity = (Activity) this.plan.getPlanElements().get(currentPlanElementIndex+2);
			
			
			BikesharingStation nearestDepartureStation = this.stations.getNearestStationWithAvailableBike(oldActivity.getCoord());
			BikesharingStation nearestArrivalStation = this.stations.getNearestStation(nextActivity.getCoord());

			Leg walkLegToStation = createWalkLeg(oldActivity.getCoord(), oldActivity.getLinkId(),
					qsim.getNetsimNetwork().getNetsimLink(nearestDepartureStation.getLinkId()).getLink().getCoord(),
					nearestDepartureStation.getLinkId(), now, TransportMode.access_walk);

			this.events.processEvent(new ActivityEndEvent(now, p.getId(), oldActivity.getLinkId(), null, oldActivity.getType()));
			this.events.processEvent(new PersonDepartureEvent(now, p.getId(), oldActivity.getLinkId(), TransportMode.access_walk));
			this.currentState = State.LEG;
			this.currentMode = TransportMode.access_walk;
			this.currentExpectedArrivalTime = walkLegToStation.getTravelTime()+now; 
			nearestDepartureStation.rentABike();
			this.currentBikesharingState = BikesharingState.WALKTOBIKE;
			
		}
	}

	@Override
	public void endLegAndComputeNextState(double now) {
		
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
	public Id<Link> getCurrentLinkId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Id<Link> getDestinationLinkId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getMode() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Id<Person> getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Id<Link> chooseNextLinkId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void notifyMoveOverNode(Id<Link> newLinkId) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isWantingToArriveOnCurrentLink() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setVehicle(MobsimVehicle veh) {
		// TODO Auto-generated method stub

	}

	@Override
	public MobsimVehicle getVehicle() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Id<Vehicle> getPlannedVehicleId() {
		// TODO Auto-generated method stub
		return null;
	}
	
	private Leg createWalkLeg(Coord from, Id<Link> fromLink, Coord to, Id<Link> toLink, double startTime, String walkMode){
		Leg leg = new LegImpl(walkMode);
		double walkDistance = CoordUtils.calcEuclideanDistance(from,to)*1.3;
		double walkTime = walkDistance / 1.11;
		Route route = new GenericRouteImpl(fromLink, toLink);
		route.setDistance(walkDistance);
		route.setTravelTime(walkTime);
		leg.setRoute(route);
		leg.setDepartureTime(startTime);
		return leg;
	}

}
