package io.gr1d.core.datasource.audit;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * Class that will store audit information for other entities
 *
 * @author Vinicius Lira
 */
@Getter
@Setter
@Table(name = "audit")
@Entity
public class AuditEntity implements Serializable {

    public final static String INSERT_OPERATION = "INSERT";
    public final static String UPDATE_OPERATION = "UPDATE";
    public final static String DELETE_OPERATION = "DELETE";

    @Id
    @JsonIgnore
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column
    private Long registerId;

    @Column(length = 42)
    private String registerUuid;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column
    private String operation;

    @Lob
    @Column
    private String registerContent;

    @Column
    private String userKeycloakId;

    @Column
    private String userInfo;

    @Column
    private LocalDateTime tokenIssuedAt;

    @Column
    private String registerName;

    @Override
    public String toString() {
        return String.format("%s: { \"id\": %s, \"uuid\": \"%s\" }", getClass().getName(), getId());
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
        final AuditEntity other = (AuditEntity) obj;
        if (getId() == null) {
            return other.getId() == null;
        }
        return getId().equals(other.getId());
    }
}
