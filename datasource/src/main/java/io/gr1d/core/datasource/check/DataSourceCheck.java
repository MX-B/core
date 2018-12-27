package io.gr1d.core.datasource.check;

import io.gr1d.core.healthcheck.CheckService;
import io.gr1d.core.healthcheck.response.ServiceInfo;
import io.gr1d.core.healthcheck.response.enums.ServiceStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Component
public class DataSourceCheck implements CheckService {

    @PersistenceContext
    private EntityManager entityManager;

    private final String datasourceUrl;

    public DataSourceCheck(@Value("${spring.datasource.url}") final String datasourceUrl) {
        this.datasourceUrl = datasourceUrl;
    }

    @Override
    public ServiceInfo check() {
        try {
            entityManager.createNativeQuery("SELECT 'a'").getFirstResult();
            return new ServiceInfo("MySQL", datasourceUrl, ServiceStatus.LIVE, null);
        } catch (Exception e) {
            return new ServiceInfo("MySQL", datasourceUrl, ServiceStatus.DOWN, e.getMessage());
        }
    }

}
