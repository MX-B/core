package io.gr1d.core.healthcheck;


import io.gr1d.core.healthcheck.response.ServiceInfo;

public interface CheckService {

    ServiceInfo check();

}
