package org.example.spboot.service;

import org.example.spboot.entity.StorageConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;

@Component
//@Profile("!default")
@ConditionalOnProperty(value = "storage.type", havingValue = "aws")
public class CloudStorageService implements StorageService{
    @Value("${storage.cloud.bucket:}")
    String bucket;

    @Value("${storage.cloud.access-key:}")
    String accessKey;

    @Value("${storage.cloud.access-secret:}")
    String accessSecret;



    final Logger logger = LoggerFactory.getLogger(getClass());

    @PostConstruct
    public void init() {
        // TODO:
        logger.info("Initializing cloud storage...");

    }

    @Override
    public InputStream openInputStream(String uri) throws IOException {
        // TODO:
        throw new IOException("File not found: " + uri);
    }

    @Override
    public String store(String extName, InputStream input) throws IOException {
        // TODO:
        throw new IOException("Unable to access cloud storage.");
    }
}
