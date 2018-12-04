package io.gr1d.core.datasource.validation;

import io.gr1d.core.datasource.model.BaseModel;
import org.hibernate.validator.internal.engine.constraintvalidation.ConstraintValidatorContextImpl;
import org.hibernate.validator.internal.metadata.descriptor.ConstraintDescriptorImpl;
import org.hibernate.validator.internal.util.annotation.ConstraintAnnotationDescriptor;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * This is a validator to check unique logically
 *
 * @author Sérgio Marcelino
 */
public class UniqueValidator implements ConstraintValidator<Unique, String> {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public boolean isValid(final String value, final ConstraintValidatorContext context) {
        final ConstraintValidatorContextImpl impl = (ConstraintValidatorContextImpl) context;
        final ConstraintDescriptorImpl descriptor = (ConstraintDescriptorImpl) impl.getConstraintDescriptor();
        final ConstraintAnnotationDescriptor annotationDescriptor = descriptor.getAnnotationDescriptor();
        final Class<?> entity = (Class<?>) annotationDescriptor.getAttribute("entity");
        final String property = (String) annotationDescriptor.getAttribute("property");

        final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<Long> query = builder.createQuery(Long.class);
        final Root<?> from = query.from(entity);
        final Predicate predicate = builder.equal(from.get(property), value);

        if (BaseModel.class.isAssignableFrom(entity)) {
            query.where(builder.and(builder.isNull(from.get("removedAt")), predicate));
        } else {
            query.where(predicate);
        }

        query.select(builder.count(from));
        final Long count = entityManager.createQuery(query).getSingleResult();

        return count == 0;
    }

}
