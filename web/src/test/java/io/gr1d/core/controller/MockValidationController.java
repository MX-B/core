package io.gr1d.core.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.LocalDate;

@Slf4j
@RestController
public class MockValidationController extends BaseController {

    @PostMapping("/validate")
    public void validate(@RequestBody @Valid final ValidateRequest request) {
      log.info("method executed successfully {}", request);
    }

    @GetMapping("/validate")
    public void validate(@Valid final LocalDate dateStart) {
        log.info("method executed successfully {}", dateStart);
    }

}
