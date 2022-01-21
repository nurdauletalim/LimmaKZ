package kz.reself.limma.filestorage.configuration;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.errors.*;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;

@Slf4j
@Configuration
public class MinioConfig {

    @Bean
    public MinioClient minioClient() throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException{

        MinioClient minioClient;
        minioClient = MinioClient.builder()
                .endpoint(minioProperties().getEndpoint())
                .credentials(minioProperties().getMinioRootUser(), minioProperties().getMinioRootPassword())
                .build();
        minioClient.setTimeout(
                minioProperties().getConnectTimeout().toMillis(),
                minioProperties().getWriteTimeout().toMillis(),
                minioProperties().getReadTimeout().toMillis()
        );

        if (minioProperties().isCheckBucket()) {
            try {
                log.debug("Checking if bucket {} exists", minioProperties().getBucket());
                BucketExistsArgs existsArgs = BucketExistsArgs.builder()
                        .bucket(minioProperties().getBucket())
                        .build();
                boolean b = minioClient.bucketExists(existsArgs);
                if (!b) {
                    if (minioProperties().isCreateBucket()) {
                        MakeBucketArgs makeBucketArgs = MakeBucketArgs.builder()
                                .bucket(minioProperties().getBucket())
                                .build();
                        minioClient.makeBucket(makeBucketArgs);
                    } else {
                        throw new IllegalStateException("Bucket does not exist: " + minioProperties().getBucket());
                    }
                }
            } catch (Exception e) {
                log.error("Error while checking bucket", e);
                throw e;
            }
        }

        return minioClient;
    }

    @Bean
    @ConfigurationProperties("minio")
    public MinioProperties minioProperties() {
        return new MinioProperties();
    }

    @Setter
    @Getter
    public static class MinioProperties {
        private String endpoint;
        private String minioRootUser;
        private String minioRootPassword;
        private String bucket;

        /**
         * Define the connect timeout for the Minio Client.
         */
        private Duration connectTimeout = Duration.ofSeconds(10);

        /**
         * Define the write timeout for the Minio Client.
         */
        private Duration writeTimeout = Duration.ofSeconds(60);

        /**
         * Define the read timeout for the Minio Client.
         */
        private Duration readTimeout = Duration.ofSeconds(10);

        private boolean checkBucket;
        private boolean createBucket;
    }
}
