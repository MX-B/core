package io.gr1d.core.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Collection;

/**
 * The base Gr1d Pagination result
 *
 * @author Sérgio Marcelino
 */
@Getter @Setter
public class Gr1dPage<T> {

    /**
     * Array of items for the current page
     */
    private Collection<T> content;

    /**
     * The total number of elements available without pagination
     * for the given query
     */
    private long totalElements;

    /**
     * Provided page
     */
    private int page;

    /**
     * Provided pagination size, not the current page size
     */
    private int size;

}
