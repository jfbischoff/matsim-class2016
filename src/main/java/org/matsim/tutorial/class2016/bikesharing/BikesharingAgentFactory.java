package org.matsim.tutorial.class2016.bikesharing;

import javax.inject.Inject;

import org.matsim.api.core.v01.population.Leg;
import org.matsim.api.core.v01.population.Person;
import org.matsim.api.core.v01.population.Plan;
import org.matsim.api.core.v01.population.PlanElement;
import org.matsim.core.mobsim.framework.MobsimAgent;
import org.matsim.core.mobsim.qsim.agents.AgentFactory;
import org.matsim.core.mobsim.qsim.agents.PersonDriverAgentImpl;
import org.matsim.core.mobsim.qsim.interfaces.Netsim;

public class BikesharingAgentFactory implements AgentFactory {

	//We need to determine: An agent going by car may become a "normal" DriverAgent, our bikesharers need to "become special" agents
	
	private final Netsim simulation;
	private final BikeSharingStations stations;
	@Inject
	public BikesharingAgentFactory(final Netsim simulation, BikeSharingStations stations) {
		this.simulation = simulation;
		this.stations = stations;
		
	}
	
	@Override
	public MobsimAgent createMobsimAgentFromPerson(Person p) {
		Plan plan = p.getSelectedPlan();
		for (PlanElement pe : plan.getPlanElements()){
			if (pe instanceof Leg){
				Leg leg = (Leg) pe;
				if (leg.getMode().equals("bike"))
				{
					createBikesharingAgent(p);
				}
					
			}
		}
		
		// TODO Auto-generated method stub
		return 	new PersonDriverAgentImpl(p.getSelectedPlan(), this.simulation); 

	}

	private void createBikesharingAgent(Person p) {
		// TODO Auto-generated method stub
		
	}

}
