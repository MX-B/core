package io.gr1d.core.upload;

/**
 * Upload Scope
 *
 * @author Sérgio Marcelino
 */
public enum UploadScope {
    /**
     * The URL will never required authentication
     */
    PUBLIC,

    /**
     * The URL will be generated and temporary
     */
    PRIVATE
}
