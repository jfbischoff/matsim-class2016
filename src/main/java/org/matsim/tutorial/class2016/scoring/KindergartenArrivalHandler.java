package org.matsim.tutorial.class2016.scoring;

import java.util.HashSet;
import java.util.Set;

import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.TransportMode;
import org.matsim.api.core.v01.events.PersonArrivalEvent;
import org.matsim.api.core.v01.events.handler.PersonArrivalEventHandler;
import org.matsim.api.core.v01.network.Link;
import org.matsim.api.core.v01.population.Activity;
import org.matsim.api.core.v01.population.Person;
import org.matsim.core.scoring.functions.CharyparNagelActivityScoring;
import org.matsim.core.scoring.functions.CharyparNagelScoringParameters;

public class KindergartenArrivalHandler  implements PersonArrivalEventHandler {

	Id<Link> kindergartenLink = Id.createLinkId(8142);
	Set<Id<Person>> arrivedOnLinkByCar = new HashSet<>();
	

	@Override
	public void reset(int iteration) {
		arrivedOnLinkByCar = new HashSet<>();
	}

	@Override
	public void handleEvent(PersonArrivalEvent event) {
		if (event.getLinkId().equals(kindergartenLink)){
			if (event.getLegMode().equals(TransportMode.car)){
				this.arrivedOnLinkByCar.add(event.getPersonId());
			}
		}
	}
	
	
}
