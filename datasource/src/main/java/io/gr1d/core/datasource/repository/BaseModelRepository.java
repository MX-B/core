package io.gr1d.core.datasource.repository;

import io.gr1d.core.datasource.model.BaseModel;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Optional;

@NoRepositoryBean
public interface BaseModelRepository<T extends BaseModel> extends CrudRepository<T, Long>, JpaSpecificationExecutor<T> {

    Optional<T> findByUuid(String uuid);
    Optional<T> findByUuidAndRemovedAtIsNull(String uuid);

}