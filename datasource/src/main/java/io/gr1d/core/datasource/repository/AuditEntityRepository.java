package io.gr1d.core.datasource.repository;

import io.gr1d.core.datasource.audit.AuditEntity;
import org.springframework.data.repository.CrudRepository;

public interface AuditEntityRepository extends CrudRepository<AuditEntity, Long> {
}
