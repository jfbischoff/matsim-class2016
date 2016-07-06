package org.matsim.tutorial.class2016.bikesharing;

import java.util.HashSet;
import java.util.Set;

import org.matsim.api.core.v01.Coord;
import org.matsim.api.core.v01.network.Network;
import org.matsim.core.utils.geometry.CoordUtils;

public class BikeSharingStations {

	Set<BikesharingStation> allStations = new HashSet<>();
	Network network;
	
	public BikeSharingStations(Network network) {
		this.network = network;
	}
	
	void addStation(BikesharingStation station){
		this.allStations.add(station);
	}
	
	BikesharingStation getNearestStation(Coord coord){
		double closestDistance = Double.MAX_VALUE;
		BikesharingStation closestStation = null;
		for (BikesharingStation station : allStations){
			Coord stationCoord = network.getLinks().get(station.getLinkId()).getCoord();
			double distance = CoordUtils.calcEuclideanDistance(coord, stationCoord);
			if (distance<closestDistance){
				closestDistance = distance;
				closestStation = station;
			}
		}
		
		return closestStation;
	}
	
	BikesharingStation getNearestStationWithAvailableBike(Coord coord){
		double closestDistance = Double.MAX_VALUE;
		BikesharingStation closestStation = null;
		for (BikesharingStation station : allStations){
			if (station.hasBikeAvailable()){
			Coord stationCoord = network.getLinks().get(station.getLinkId()).getCoord();
			double distance = CoordUtils.calcEuclideanDistance(coord, stationCoord);
			if (distance<closestDistance){
				closestDistance = distance;
				closestStation = station;
			}
			}
		}
		
		return closestStation;
	}
	
}
