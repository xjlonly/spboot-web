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
import java.io.*;
import java.util.UUID;

@Component
//@Profile("default")
@ConditionalOnProperty(value = "storage.type", havingValue = "local",matchIfMissing = true)
public class LocalStorageService implements StorageService{

    @Value("${storage.local:/var/static}")
    String localStorageRootDir;

    @Autowired
    StorageConfiguration storageConfiguration;

    final Logger logger = LoggerFactory.getLogger(getClass());
    private File localStorageRoot;

    @PostConstruct
    public void init(){
        logger.info("load configuration:root-dir:{}", storageConfiguration.getRootDir());
        logger.info("load configuration:allowTypes:{}",storageConfiguration.getAllowTypes());
        logger.info("load configuration:allowTypes:{}",storageConfiguration.getMaxSize());
        logger.info("Initializing local storage with root dir:{}", this.localStorageRootDir);
        this.localStorageRoot = new File(this.localStorageRootDir);
    }

    @Override
    public InputStream openInputStream(String uri) throws IOException {
        File file = new File(this.localStorageRoot, uri);
        return new BufferedInputStream(new FileInputStream(file));
    }

    @Override
    public String store(String extName, InputStream input) throws IOException {
        String fileName = UUID.randomUUID().toString() + "." + extName;
        File targetFile = new File(this.localStorageRoot,fileName);
        try(OutputStream output = new BufferedOutputStream(new FileOutputStream(targetFile))){
            input.transferTo(output);
        }
        return fileName;
    }
}
