package org.matsim.tutorial.class2016.scoring;

import org.matsim.api.core.v01.network.Network;
import org.matsim.api.core.v01.population.Leg;
import org.matsim.core.scoring.functions.CharyparNagelLegScoring;
import org.matsim.core.scoring.functions.CharyparNagelScoringParameters;
import org.matsim.pt.transitSchedule.api.TransitSchedule;

public class KindergartenLegScoring extends CharyparNagelLegScoring {

	public KindergartenLegScoring(CharyparNagelScoringParameters params, Network network) {
		super(params, network);
		// TODO Auto-generated constructor stub
	}

	public KindergartenLegScoring(CharyparNagelScoringParameters params, Network network,
			TransitSchedule transitSchedule) {
		super(params, network, transitSchedule);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void finish() {

	}

	@Override
	public double getScore() {
		return this.score;
	}

	protected double calcLegScore(final double departureTime, final double arrivalTime, final Leg leg) {
		double legScore = super.calcLegScore(departureTime, arrivalTime, leg);
		return legScore;
	}

	@Override
	public void handleLeg(Leg leg) {
		double legScore = calcLegScore(leg.getDepartureTime(), leg.getDepartureTime() + leg.getTravelTime(), leg);
		this.score += legScore;

	}

}
