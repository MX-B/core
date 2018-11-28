package io.gr1d.core.upload;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Data from a previously uploaded file
 * @author Sérgio Marcelino
 */
@Getter
@AllArgsConstructor
public class UploadedFile {

    private final String fileId;
    private final String url;

}
