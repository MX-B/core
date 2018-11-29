package io.gr1d.core.upload;

import io.gr1d.core.strategy.StrategyResolver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service to manage uploads
 *
 * @author Raúl Sola
 * @author Sérgio Marcelino
 */
@Slf4j
@Service
public class UploadService {

    private final StrategyResolver resolver;
    private final String uploadStrategy;

    @Autowired
    public UploadService(final StrategyResolver resolver,
                         @Value("${gr1d.upload.strategy}") final String uploadStrategy) {
        this.resolver = resolver;
        this.uploadStrategy = uploadStrategy;
    }

    /**
     * Uploads a new File
     * @param file byte[] file
     * @param fileName file name to store
     * @param path path to folder
     * @return generated
     */
    public UploadedFile upload(byte[] file, final String fileName, final String path, final UploadScope scope) {
        return resolveStrategy().map(strategy -> strategy.upload(file, fileName, path, scope))
                .orElse(null);
    }

    /**
     * Uploads a new File
     * @param fileBase64 base64 data without prefix
     * @param fileName file name to store
     * @param path path to folder
     * @return generated
     */
    public UploadedFile upload(final String fileBase64, final String fileName, final String path, final UploadScope scope) {
        return resolveStrategy().map(strategy -> strategy.upload(fileBase64, fileName, path, scope))
                .orElse(null);
    }

    public UploadedFile getUploadData(final String uploadId) {
        return resolveStrategy().map(strategy -> strategy.getUploadData(uploadId))
                .orElse(null);
    }

    private Optional<UploadStrategy> resolveStrategy() {
        final UploadStrategy strategy = resolver.resolve(UploadStrategy.class, uploadStrategy);
        if (strategy == null) {
            log.error("Unable to find an UploadStrategy for the configured plan {}", uploadStrategy);
            return Optional.empty();
        }
        return Optional.of(strategy);
    }

}
