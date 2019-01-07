package io.gr1d.core.util;

import io.gr1d.core.model.Gr1dPage;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

public class IterablePageTest {

    @Mock
    private PageRequester<String> requester;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testIterator() {
        final IterablePage<String> iterable = new IterablePage<>(requester);
        when(requester.requestPage(anyInt())).thenReturn(
                page(0, 3, 7, 3),
                page(1, 3, 7, 3),
                page(2, 3, 7, 1));
        assertThat(iterable.hasNext()).isTrue();
        assertThat(iterable.next()).isEqualTo("Page 0 | Index 0");

        assertThat(iterable.hasNext()).isTrue();
        assertThat(iterable.next()).isEqualTo("Page 0 | Index 1");

        assertThat(iterable.hasNext()).isTrue();
        assertThat(iterable.next()).isEqualTo("Page 0 | Index 2");

        assertThat(iterable.hasNext()).isTrue();
        assertThat(iterable.next()).isEqualTo("Page 1 | Index 0");

        assertThat(iterable.hasNext()).isTrue();
        assertThat(iterable.next()).isEqualTo("Page 1 | Index 1");

        assertThat(iterable.hasNext()).isTrue();
        assertThat(iterable.next()).isEqualTo("Page 1 | Index 2");

        assertThat(iterable.hasNext()).isTrue();
        assertThat(iterable.next()).isEqualTo("Page 2 | Index 0");

        assertThat(iterable.hasNext()).isFalse();
    }

    @Test
    public void testIterable() {
        final IterablePage<String> iterable = new IterablePage<>(requester);
        when(requester.requestPage(anyInt())).thenReturn(
                page(0, 3, 7, 3),
                page(1, 3, 7, 3),
                page(2, 3, 7, 1));

        final List<String> list = new ArrayList<>();
        iterable.forEach(list::add);

        assertThat(list).hasSize(7);
        assertThat(list.get(0)).isEqualTo("Page 0 | Index 0");
        assertThat(list.get(1)).isEqualTo("Page 0 | Index 1");
        assertThat(list.get(2)).isEqualTo("Page 0 | Index 2");
        assertThat(list.get(3)).isEqualTo("Page 1 | Index 0");
        assertThat(list.get(4)).isEqualTo("Page 1 | Index 1");
        assertThat(list.get(5)).isEqualTo("Page 1 | Index 2");
        assertThat(list.get(6)).isEqualTo("Page 2 | Index 0");
    }

    private Gr1dPage<String> page(final int page, final int size, final int totalElements, final int currentPageSize) {
        final Gr1dPage<String> result = new Gr1dPage<>();
        result.setContent(new ArrayList<>(size));

        for (int i = 0; i < currentPageSize; i++) {
            result.getContent().add(String.format("Page %s | Index %s", page, i));
        }

        result.setPage(page);
        result.setSize(size);
        result.setTotalElements(totalElements);
        return result;
    }
}
