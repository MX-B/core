package io.gr1d.core.email;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EmailAttachment {

    private final String id;
    private final String base64;
    private final String mimeType;
    private final String fileName;

}
