package io.gr1d.core.model;

import lombok.Getter;

@Getter
public class CreatedResponse {

    private final String uuid;

    public CreatedResponse(final String uuid) {
        this.uuid = uuid;
    }

}
