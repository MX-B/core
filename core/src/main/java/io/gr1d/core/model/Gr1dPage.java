package io.gr1d.core.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
    private List<T> content;

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

    public void setContent(final Collection<T> collection) {
        content = new ArrayList<>(collection);
    }

}
