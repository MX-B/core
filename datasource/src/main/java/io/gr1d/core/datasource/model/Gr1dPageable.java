package io.gr1d.core.datasource.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.google.common.base.CaseFormat.LOWER_CAMEL;
import static com.google.common.base.CaseFormat.LOWER_UNDERSCORE;

/**
 * This is the base Pageable class
 * to use inside Controllers to handle pagination
 * and to maintain a pattern for all endpoints
 *
 * @author Sérgio Marcelino
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Gr1dPageable {

    @ApiModelProperty(notes = "The sort property to request, starting with `-` indicates DESC", example = "-name")
    private List<String> sort;

    @ApiModelProperty(notes = "The page to request, default is 0", example = "0")
    private int page = 0;

    @ApiModelProperty(notes = "The size of the page to request, default is 20, cannot be larger than 200 and less than 1", example = "20")
    private int page_size = 20;

    @ApiModelProperty(hidden = true)
    public Pageable toPageable() {
        return PageRequest.of(getFormattedPage(), getFormattedPageSize(), getFormattedSort());
    }

    @ApiModelProperty(hidden = true)
    private int getFormattedPageSize() {
        return Math.max(1, Math.min(page_size, 200));
    }

    @ApiModelProperty(hidden = true)
    private int getFormattedPage() {
        return Math.max(page, 0);
    }

    @ApiModelProperty(hidden = true)
    private Sort getFormattedSort() {
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
                ? Sort.Order.desc(convert(property.substring(1)))
                : Sort.Order.asc(convert(property));
    }

    private String convert(final String property) {
        return LOWER_UNDERSCORE.to(LOWER_CAMEL, property);
    }

}
