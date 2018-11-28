package io.gr1d.core.upload;

/**
 * @author Raúl Sola
 * @author Sérgio Marcelino
 */
public interface UploadStrategy {

    /**
     * Uploads a new file
     * @param fileBase64 base64 file without prefix
     * @param fileName filename
     * @param path path to store (folder, etc)
     * @param scope whether the upload will be public or not
     * @return the uploaded file data
     */
    UploadedFile upload(final String fileBase64, final String fileName, final String path, final UploadScope scope);

    /**
     * Retrieves the url for a private file
     * @param uploadId file id generated through upload
     * @return the file data
     */
    UploadedFile getUploadData(final String uploadId);

}
