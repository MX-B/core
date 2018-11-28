package io.gr1d.core.check;

import io.gr1d.core.healthcheck.CheckService;
import io.gr1d.core.healthcheck.response.ServiceInfo;
import io.gr1d.core.healthcheck.response.enums.ServiceStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.SQLException;

@Component
public class DataSourceCheck implements CheckService {

    private final DataSource dataSource;

    @Autowired
    public DataSourceCheck(final DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public ServiceInfo check() {
        try {
            dataSource.getConnection().isValid(5000);
            final String url = dataSource.getConnection().getMetaData().getURL();
            return new ServiceInfo("MySQL", url, ServiceStatus.LIVE, null);
        } catch (SQLException e) {
            return new ServiceInfo("MySQL", "", ServiceStatus.DOWN, e.getMessage());
        }
    }

}
