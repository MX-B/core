package io.gr1d.core.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;

/**
 * Base class for Enumeration-like entities
 *
 * @author Rafael M. Lins
 */
@Getter
@Setter
@MappedSuperclass
public class BaseEnum implements Serializable {
    private static final long serialVersionUID = 475487439951089032L;

    @Id
    private Long id;

    @Column(nullable = false, length = 32)
    private String name;

    @Column(nullable = false, length = 32)
    private String description;

    public BaseEnum() {
        super();
    }

    public BaseEnum(final long id, final String name) {
        setId(id);
        setName(name);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" + name + ")";
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }

        return getClass().equals(obj.getClass()) && id != null && ((BaseEnum) obj).id != null && id.equals(((BaseEnum) obj).id);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = getClass().getName().hashCode();
        result = prime * result + (id == null ? 0 : id.hashCode());
        return result;
    }
}
