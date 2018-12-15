package io.gr1d.core.healthcheck;

import io.gr1d.core.healthcheck.response.HealthCheckResponse;
import io.gr1d.core.healthcheck.response.enums.ServiceStatus;
import io.gr1d.core.response.Gr1dError;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class HealthCheckController {

	private static final String ROUTE_BASIC = "/hc";
	private static final String ROUTE_COMPLETE = ROUTE_BASIC + "/complete";
	
	private final HealthcheckService service;

	@Autowired
	public HealthCheckController(final HealthcheckService service) {
		this.service = service;
	}

	@RequestMapping(path = ROUTE_BASIC, method = RequestMethod.GET)
	@ApiOperation(value = "Health Check Service", notes = "Check all system's services and responds if ok or not", tags = "Healthcheck")
    @ApiResponses({
        @ApiResponse(code = 200, message = "All Services working fine"),
        @ApiResponse(code = 503, message = "Any Service is Down")
    })
	public ResponseEntity<ServiceStatus> simpleHealthCheck() {
		log.trace("function=simpleHealthCheck status=init");
		final HealthCheckResponse res = service.check();
		final HttpStatus status = getStatus(res);
		log.trace("function=simpleHealthCheck status=done");
		return ResponseEntity.status(status).body(res.getStatus());
	}

	@RequestMapping(path = ROUTE_COMPLETE, method = RequestMethod.GET)
	@ApiOperation(value = "Health Check Service", notes = "Check all system's services and responds a detailed info", tags = "Healthcheck")
    @ApiResponses({
        @ApiResponse(code = 200, message = "All Services working fine", response = HealthCheckResponse.class),
        @ApiResponse(code = 401, message = "Access unauthorized", response = Gr1dError[].class),
        @ApiResponse(code = 403, message = "Access forbidden", response = Gr1dError[].class),
        @ApiResponse(code = 503, message = "Any Service is Down", response = HealthCheckResponse.class)
    })
	public ResponseEntity<HealthCheckResponse> completeHealthCheck() {
		log.trace("function=completeHealthCheck status=init");
		HealthCheckResponse res = service.check();
		HttpStatus status = getStatus(res);
		log.trace("function=completeHealthCheck status=done");
		return ResponseEntity.status(status).body(res);
	}
	
	private HttpStatus getStatus(HealthCheckResponse res) {
		return ServiceStatus.LIVE.equals(res.getStatus()) ? HttpStatus.OK : HttpStatus.SERVICE_UNAVAILABLE;
	}
}
