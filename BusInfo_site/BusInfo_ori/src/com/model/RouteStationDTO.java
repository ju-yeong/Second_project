package com.model;

public class RouteStationDTO {
	private String updown;
	private String station_nm;
	private String station_id;
	public RouteStationDTO(String updown, String station_nm, String station_id) {
		this.updown = updown;
		this.station_nm = station_nm;
		this.station_id = station_id;
	}
	public String getUpdown() {
		return updown;
	}
	public void setUpdown(String updown) {
		this.updown = updown;
	}
	public String getStation_nm() {
		return station_nm;
	}
	public void setStation_nm(String station_nm) {
		this.station_nm = station_nm;
	}
	public String getStation_id() {
		return station_id;
	}
	public void setStation_id(String station_id) {
		this.station_id = station_id;
	}
	
	
}
