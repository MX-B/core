package io.gr1d.core.datasource;

import io.gr1d.core.datasource.validation.Unique;
import io.gr1d.core.validation.CPFCNPJ;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class MockRequest {

    @Unique(entity = MockEntity.class, property = "uniqueProperty")
    private String uniqueProperty;

    public MockEntity toEntity() {
        final MockEntity entity = new MockEntity();
        entity.setUniqueProperty(uniqueProperty);
        return entity;
    }

}
