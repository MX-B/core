package io.gr1d.core.datasource.model;

import io.gr1d.core.model.Gr1dPage;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * This is the base PageResult to be used in controllers
 *
 * @author sergio.marcelino
 */
@Getter
public class PageResult<T> extends Gr1dPage<T> {

    private PageResult(Page page, List<T> content) {
        setTotalElements(page.getTotalElements());
        setSize(page.getSize());
        setPage(page.getNumber());
        setContent(content);
    }

    public static <T> PageResult<T> ofPage(final Page<T> page) {
        return new PageResult<>(page, page.getContent());
    }

    public static <T> PageResult<T> ofPage(final Page<?> page, final List<T> content) {
        return new PageResult<>(page, content);
    }

}
