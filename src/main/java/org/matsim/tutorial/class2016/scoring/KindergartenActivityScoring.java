package org.matsim.tutorial.class2016.scoring;

import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.population.Activity;
import org.matsim.api.core.v01.population.Person;
import org.matsim.core.scoring.functions.CharyparNagelActivityScoring;
import org.matsim.core.scoring.functions.CharyparNagelScoringParameters;

public class KindergartenActivityScoring extends CharyparNagelActivityScoring {

	KindergartenArrivalHandler handler;
	Id<Person> personId;
	public KindergartenActivityScoring(CharyparNagelScoringParameters params, Id<Person> personId, KindergartenArrivalHandler handler) {
		super(params);
		this.handler = handler;
		this.personId = personId;
	}
	
	
	@Override
	public void handleActivity(Activity act) {
		if (act.getType().equals("pt interaction")) return;
		this.score += super.calcActScore(act.getStartTime(), act.getEndTime(), act);
		if (act.getType().startsWith("kindergarten")){
			if (act.getLinkId().equals(handler.kindergartenLink)){
				if (handler.arrivedOnLinkByCar.contains(personId)){
					this.score -= 3000.;
					handler.arrivedOnLinkByCar.remove(personId);
					System.out.println(personId + " arrived by car");
				}
			}
		}
		// TODO Auto-generated method stub
	}


}
