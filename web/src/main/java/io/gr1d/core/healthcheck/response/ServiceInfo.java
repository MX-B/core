package io.gr1d.core.healthcheck.response;

import io.gr1d.core.healthcheck.response.enums.ServiceStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ServiceInfo {

    private String name;

    private String host;

    private ServiceStatus status;

    private String message;

}
