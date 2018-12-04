package io.gr1d.core.datasource;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MockRepository extends CrudRepository<MockEntity, Long> {
}
