package com.model;

public class RouteDTO {
	private String route_nm;
	private String region_name;
	private String st_sta_nm;
	private String ed_sta_nm;
	private String route_id;
	
	public RouteDTO(String route_nm, String region_name, String st_sta_nm, String ed_sta_nm, String route_id) {
		this.route_nm = route_nm;
		this.region_name = region_name;
		this.st_sta_nm = st_sta_nm;
		this.ed_sta_nm = ed_sta_nm;
		this.route_id = route_id;
	}

	public String getRoute_nm() {
		return route_nm;
	}

	public void setRoute_nm(String route_nm) {
		this.route_nm = route_nm;
	}

	public String getRegion_name() {
		return region_name;
	}

	public void setRegion_name(String region_name) {
		this.region_name = region_name;
	}

	public String getSt_sta_nm() {
		return st_sta_nm;
	}

	public void setSt_sta_nm(String st_sta_nm) {
		this.st_sta_nm = st_sta_nm;
	}

	public String getEd_sta_nm() {
		return ed_sta_nm;
	}

	public void setEd_sta_nm(String ed_sta_nm) {
		this.ed_sta_nm = ed_sta_nm;
	}

	public String getRoute_id() {
		return route_id;
	}

	public void setRoute_id(String route_id) {
		this.route_id = route_id;
	}
	
}
