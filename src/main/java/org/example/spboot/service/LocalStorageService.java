package org.example.spboot.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

@Component
@Profile("default")
public class LocalStorageService implements StorageService{

    @Value("${storage.local:/var/static}")
    String localStorageRootDir;

    final Logger logger = LoggerFactory.getLogger(getClass());
    private File localStorageRoot;

    @PostConstruct
    public void init(){
        logger.info("Initializing local storage with root dir:{}", this.localStorageRootDir);
        this.localStorageRoot = new File(this.localStorageRootDir);
    }

    @Override
    public InputStream openInputStream(String uri) throws IOException {
        return null;
    }

    @Override
    public String store(String extName, InputStream input) throws IOException {
        return null;
    }
}
