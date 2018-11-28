package io.gr1d.core.upload;

import com.google.cloud.storage.*;
import com.google.cloud.storage.Acl.Role;
import com.google.cloud.storage.Acl.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.file.Paths;
import java.util.Base64;
import java.util.Collections;

/**
 * File uploader to Google Storage
 *
 * @author Raúl Sola
 */
@Component("UploadStrategy.STORAGE")
public class CloudStorageStrategy implements UploadStrategy {

    private static Storage storage;

    private final String bucketName;

    static {
        storage = StorageOptions.getDefaultInstance().getService();
    }

    public CloudStorageStrategy(@Value("${gr1d.upload.cloudStorage.bucketName}") final String bucketName) {
        this.bucketName = bucketName;
    }

    @Override
    public UploadedFile upload(final String fileBase64, final String fileName, final String path, final UploadScope scope) {
        final byte byteArray[] = Base64.getMimeDecoder().decode(fileBase64);
        final BlobInfo blobInfo = storage.create(createBlobInfo(fileName, path, scope), byteArray);
        return fromBlobInfo(blobInfo);
    }

    @Override
    public UploadedFile getUploadData(final String uploadId) {
        final BlobInfo blobInfo = storage.get(BlobId.of(bucketName, uploadId));
        return fromBlobInfo(blobInfo);
    }

    private UploadedFile fromBlobInfo(final BlobInfo blobInfo) {
        return new UploadedFile(blobInfo.getGeneratedId(), blobInfo.getMediaLink());
    }

    private BlobInfo createBlobInfo(final String fileName, final String path, final UploadScope scope) {
        BlobInfo.Builder builder = BlobInfo.newBuilder(bucketName, Paths.get(path, fileName).toString());
        if (scope == UploadScope.PUBLIC) {
            // Modify access list to allow all users with link to read file
            return builder.setAcl(Collections.singletonList(Acl.of(User.ofAllUsers(), Role.READER))).build();
        }
        return builder.build();
    }

}
