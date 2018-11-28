package io.gr1d.core.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.Collection;
import java.util.List;

/**
 * This is the base PageResult to be used in controllers
 *
 * @author sergio.marcelino
 */
@Getter
public class PageResult<T> {

    @ApiModelProperty(notes = "The page list content")
    private final Collection<T> content;

    @ApiModelProperty("The total elements found for the given query")
    private final long totalElements;

    @ApiModelProperty("The current page")
    private final int page;

    @ApiModelProperty("The size of the page, total_elements must never exceed this size")
    private final int size;

    private PageResult(Page page, List<T> content) {
        totalElements = page.getTotalElements();
        size = page.getSize();
        this.page = page.getNumber();
        this.content = content;
    }

    public static <T> PageResult<T> ofPage(final Page<T> page) {
        return new PageResult<>(page, page.getContent());
    }

    public static <T> PageResult<T> ofPage(final Page<?> page, final List<T> content) {
        return new PageResult<>(page, content);
    }

}
