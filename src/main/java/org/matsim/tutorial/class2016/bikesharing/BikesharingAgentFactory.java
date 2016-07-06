package org.matsim.tutorial.class2016.bikesharing;

import org.matsim.api.core.v01.population.Person;
import org.matsim.core.mobsim.framework.MobsimAgent;
import org.matsim.core.mobsim.qsim.agents.AgentFactory;

public class BikesharingAgentFactory implements AgentFactory {

	//We need to determine: An agent going by car may become a "normal" DriverAgent, our bikesharers need to "become special" agents
	
	@Override
	public MobsimAgent createMobsimAgentFromPerson(Person p) {
		// TODO Auto-generated method stub
		return null;
	}

}
