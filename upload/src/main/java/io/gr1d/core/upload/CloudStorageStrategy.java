package io.gr1d.core.upload;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.*;
import com.google.cloud.storage.Acl.Role;
import com.google.cloud.storage.Acl.User;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

import static java.util.Optional.ofNullable;

/**
 * File uploader to Google Storage
 *
 * @author Raúl Sola
 * @author Sérgio Marcelino
 */
@Slf4j
@Component("UploadStrategy.STORAGE")
@ConditionalOnProperty(value = "gr1d.upload.strategy", havingValue = "STORAGE")
public class CloudStorageStrategy implements UploadStrategy {

    private Storage storage;

    private final String bucketName;
    private final long signUrlDuration;

    public CloudStorageStrategy(@Value("${gr1d.upload.cloudStorage.bucketName:}") final String bucketName,
                                @Value("${gr1d.upload.cloudStorage.signUrlDuration:120}") final long signUrlDuration,
                                final io.gr1d.core.upload.GoogleCredentials credentials) throws IOException {
        this.bucketName = bucketName;
        this.signUrlDuration = signUrlDuration;

        try (final ByteArrayInputStream stream = new ByteArrayInputStream(new Gson().toJson(credentials).getBytes())) {
            final GoogleCredentials cred = GoogleCredentials.fromStream(stream);
            this.storage = StorageOptions.newBuilder().setCredentials(cred).build().getService();
        }
    }

    @Override
    public UploadedFile upload(final String fileBase64, final String fileName, final String path, final UploadScope scope) {
        final byte byteArray[] = Base64.getMimeDecoder().decode(fileBase64);

        return this.upload(byteArray, fileName, path, scope);
    }

    @Override
    public UploadedFile upload(byte[] file, String fileName, String path, UploadScope scope) {
        final BlobInfo blobInfo = storage.create(createBlobInfo(fileName, path, scope), file);
        return fromBlobInfo(blobInfo, scope);
    }

    @Override
    public UploadedFile getUploadData(final String uploadId) {
        return ofNullable(storage.get(BlobId.of(bucketName, uploadId)))
                .map(blob -> fromBlobInfo(blob, UploadScope.PRIVATE))
                .orElseGet(() -> {
                    log.error("Upload not found for ID: {}", uploadId);
                    return null;
                });
    }

    private UploadedFile fromBlobInfo(final BlobInfo blobInfo, final UploadScope scope) {
        final String fileId = blobInfo.getBlobId().getName();
        final String url = scope == UploadScope.PUBLIC
                ? blobInfo.getMediaLink()
                : storage.signUrl(blobInfo, signUrlDuration, TimeUnit.MINUTES).toString();
        return new UploadedFile(fileId, url);
    }

    private BlobInfo createBlobInfo(final String fileName, final String path, final UploadScope scope) {
        final BlobInfo.Builder builder = BlobInfo.newBuilder(bucketName, Paths.get(path, fileName).toString());
        if (scope == UploadScope.PUBLIC) {
            // Modify access list to allow all users with link to read file
            return builder.setAcl(Collections.singletonList(Acl.of(User.ofAllUsers(), Role.READER))).build();
        }
        return builder.build();
    }

}
