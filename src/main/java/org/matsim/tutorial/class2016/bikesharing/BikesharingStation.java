package org.matsim.tutorial.class2016.bikesharing;

import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.network.Link;

public class BikesharingStation {
	
	Id<BikesharingStation> id;
	Id<Link> linkId;
	int capacity;
	int availablebikes;
	
	
	public BikesharingStation(Id<BikesharingStation> id, Id<Link> linkId, int capacity) {
		super();
		this.id = id;
		this.linkId = linkId;
		this.capacity = capacity;
		this.availablebikes = capacity;
	}
	
	public Id<Link> getLinkId() {
		return linkId;
	}
	
	boolean hasBikeAvailable(){

		return (availablebikes>0);
		
	}
	
	void rentABike(){
		if (!hasBikeAvailable()){
			throw new RuntimeException("pls check if sth is available beforehand");
		}
		this.availablebikes--;
	}
	
	void returnABike(){
		this.availablebikes++;
	}
	
	
}
