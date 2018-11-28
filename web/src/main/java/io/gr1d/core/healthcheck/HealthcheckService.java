package io.gr1d.core.healthcheck;

import io.gr1d.core.healthcheck.response.HealthCheckResponse;
import io.gr1d.core.healthcheck.response.ServiceInfo;
import io.gr1d.core.healthcheck.response.enums.ServiceStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Optional.ofNullable;

@Slf4j
@Service
public class HealthcheckService {

    @Value("${app.name:}")
    private String appName;

    @Value("${app.version:}")
    private String appVersion;

    @Autowired(required = false)
    private Collection<CheckService> services;

    public HealthCheckResponse check() {
        log.trace("function=check status=init");
        final HealthCheckResponse res = new HealthCheckResponse(appName, appVersion, ServiceStatus.LIVE);
        final List<ServiceInfo> serviceInfos = ofNullable(services)
                .map(Collection::stream)
                .orElseGet(Stream::empty)
                .map(CheckService::check)
                .collect(Collectors.toList());
        serviceInfos.stream()
                .map(ServiceInfo::getStatus)
                .filter(ServiceStatus.DOWN::equals).findAny()
                .ifPresent(res::setStatus);

        res.setServices(serviceInfos);
        log.trace("function=check status=done");
        return res;
    }
}
