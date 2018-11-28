package io.gr1d.core.upload;

import org.springframework.stereotype.Component;

/**
 * Just for testing purposes
 */
@Component("UploadStrategy.MOCK")
public class MockUploadStrategy implements UploadStrategy {

    @Override
    public UploadedFile upload(final String fileBase64, final String fileName, final String path, final UploadScope scope) {
        return new UploadedFile("image", "http://www.test.com/image");
    }

    @Override
    public UploadedFile getUploadData(final String uploadId) {
        return new UploadedFile("image", "http://www.test.com/image");
    }

}
