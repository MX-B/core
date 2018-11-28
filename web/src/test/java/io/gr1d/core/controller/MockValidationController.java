package io.gr1d.core.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
public class MockValidationController extends BaseController {

    @PostMapping("/validate")
    public void validate(@RequestBody @Valid final ValidateRequest request) {
      log.info("method executed successfully");
    }

}
