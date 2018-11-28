package io.gr1d.core.healthcheck.response;

import io.gr1d.core.healthcheck.response.enums.ServiceStatus;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;
import java.util.LinkedList;

@Getter @Setter
public class HealthCheckResponse {

	private String version;
	
	private String name;
	
	private ServiceStatus status;
	
	private Collection<ServiceInfo> services;

	public HealthCheckResponse(String name, String version, ServiceStatus status) {
		this.name = name;
		this.version = version;
		this.status = status;
		this.services = new LinkedList<>();
	}
}
