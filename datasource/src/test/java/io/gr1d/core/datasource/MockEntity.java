package io.gr1d.core.datasource;

import io.gr1d.core.datasource.model.BaseModel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "mock_entity")
public class MockEntity extends BaseModel {

    @Column(name = "unique_property")
    private String uniqueProperty;

}
