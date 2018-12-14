package io.gr1d.core.datasource.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * This is the base Pageable class
 * to use inside Controllers to handle pagination
 * and to maintain a pattern for all endpoints
 *
 * @author Sérgio Marcelino
 */
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Gr1dPageable implements Pageable {

    @ApiModelProperty(notes = "The sort property to request, starting with `-` indicates DESC")
    public List<String> sort;

    @ApiModelProperty(notes = "The page to request, default is 0")
    public int page = 0;

    @ApiModelProperty(notes = "The size of the page to request, default is 20, cannot be larger than 200 and less than 1")
    public int pageSize = 20;

    @ApiModelProperty(hidden = true)
    public int getPageSize() {
        return Math.max(1, Math.min(pageSize, 200));
    }

    @Override
    @ApiModelProperty(hidden = true)
    public int getPageNumber() {
        return Math.max(page, 0);
    }

    @Override
    @ApiModelProperty(hidden = true)
    public long getOffset() {
        return getPageNumber() * getPageSize();
    }

    @ApiModelProperty(hidden = true)
    public Sort getSort() {
        if (sort == null) return Sort.unsorted();
        final List<Sort.Order> orders = sort.stream()
                .filter(Objects::nonNull)
                .map(this::getOrder)
                .collect(Collectors.toList());
        return Sort.by(orders);
    }

    @ApiModelProperty(hidden = true)
    private Sort.Order getOrder(final String property) {
        return property.startsWith("-")
                ? Sort.Order.desc(property.substring(1))
                : Sort.Order.asc(property);
    }

    @Override
    @ApiModelProperty(hidden = true)
    public Pageable next() {
        return new Gr1dPageable(sort, getPageNumber() + 1, getPageSize());
    }

    @Override
    @ApiModelProperty(hidden = true)
    public Pageable previousOrFirst() {
        return new Gr1dPageable(sort, Math.max(0, getPageNumber() - 1), getPageSize());
    }

    @Override
    @ApiModelProperty(hidden = true)
    public Pageable first() {
        return new Gr1dPageable(sort, 0, getPageSize());
    }

    @Override
    @ApiModelProperty(hidden = true)
    public boolean hasPrevious() {
        return getPageNumber() > 0;
    }
}
