package io.gr1d.core.util;

import io.gr1d.core.model.Gr1dPage;

public interface PageRequester<T> {

    Gr1dPage<T> requestPage(int page);

}
