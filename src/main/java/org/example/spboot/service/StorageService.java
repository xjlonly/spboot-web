package org.example.spboot.service;

import java.io.IOException;
import java.io.InputStream;

public interface StorageService {
    // 根据URI打开InputStream:
    InputStream openInputStream(String uri) throws IOException;

    // 根据扩展名+InputStream保存并返回URI:
    String store(String extName, InputStream input) throws IOException;

}
