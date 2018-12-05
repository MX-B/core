package io.gr1d.core.datasource;

import io.gr1d.core.controller.BaseController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.LocalDateTime;

@Slf4j
@RestController("/mock")
public class MockValidationController extends BaseController {

    @Autowired private MockRepository mockRepository;

    @PostMapping
    public void create(@RequestBody @Valid final MockRequest request) {
        mockRepository.save(request.toEntity());
        log.info("method executed successfully");
    }

    @DeleteMapping
    public void deleteAll() {
        mockRepository.findAll().forEach(item -> {
            item.setRemovedAt(LocalDateTime.now());
            mockRepository.save(item);
        });
    }

}
