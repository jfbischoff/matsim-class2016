package org.matsim.tutorial.class2016.scoring;

import java.util.HashSet;
import java.util.Set;

import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.TransportMode;
import org.matsim.api.core.v01.events.ActivityStartEvent;
import org.matsim.api.core.v01.events.PersonArrivalEvent;
import org.matsim.api.core.v01.events.handler.ActivityStartEventHandler;
import org.matsim.api.core.v01.events.handler.PersonArrivalEventHandler;
import org.matsim.api.core.v01.network.Link;
import org.matsim.api.core.v01.population.Activity;
import org.matsim.api.core.v01.population.Person;
import org.matsim.core.scoring.functions.CharyparNagelActivityScoring;
import org.matsim.core.scoring.functions.CharyparNagelScoringParameters;

public class KindergartenArrivalHandler  implements PersonArrivalEventHandler, ActivityStartEventHandler {

	Id<Link> kindergartenLink = Id.createLinkId(8142);
	Set<Id<Person>> arrivedOnLinkByCar = new HashSet<>();
	int kinder = 0;

	@Override
	public void reset(int iteration) {
		arrivedOnLinkByCar = new HashSet<>();
		kinder = 0;
	}

	@Override
	public void handleEvent(PersonArrivalEvent event) {
		if (event.getLinkId().equals(kindergartenLink)){
			if (event.getLegMode().equals(TransportMode.car)){
				this.arrivedOnLinkByCar.add(event.getPersonId());
			}
		}
	}

	@Override
	public void handleEvent(ActivityStartEvent event) {
		
		if (event.getLinkId().equals(kindergartenLink)){
			if (event.getActType().equals("kindergarten1"))
			kinder++;
		}
	}
	
	
}
