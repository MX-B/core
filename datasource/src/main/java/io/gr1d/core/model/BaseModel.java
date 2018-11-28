package io.gr1d.core.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * Base class for the model classes containing essential properties for every
 * models
 *
 * @author Rafael M. Lins
 * @author Sérgio Marcelino
 */
@Getter
@Setter
@MappedSuperclass
public class BaseModel implements Serializable {

    private static final long serialVersionUID = -2414208181554129302L;

    @Id
    @JsonIgnore
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 42)
    private String uuid;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime updatedAt;

    @Column
    private LocalDateTime removedAt;

    protected String uuidBase() {
        return getClass().getSimpleName().substring(0, 3).toUpperCase();
    }

    protected void createUuid(final boolean force) {
        if (force || uuid == null) {
            uuid = uuidBase() + "-" + UUID.randomUUID().toString();
        }
    }

    @PrePersist
    public void beforeSave() {
        createUuid(false);
        createdAt = LocalDateTime.now();
    }

    @PreUpdate
    public void beforeUpdate() {
        updatedAt = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return String.format("%s: { \"id\": %s, \"uuid\": \"%s\" }", getClass().getName(), getId(), getUuid());
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (getId() == null ? 0 : getId().hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {    // NOSONAR
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!getClass().isAssignableFrom(obj.getClass())) {    // NOSONAR
            return false;
        }
        final BaseModel other = (BaseModel) obj;
        if (getId() == null) {
            return other.getId() == null;
        }
        return getId().equals(other.getId());
    }
}
