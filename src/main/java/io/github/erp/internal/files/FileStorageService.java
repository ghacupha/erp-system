package io.github.erp.internal.files;
import org.springframework.core.io.Resource;
import java.nio.file.Path;
import java.util.stream.Stream;

import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {

    void init();
    void save(MultipartFile file);
    Resource load(String filename);
    void deleteAll();
    Stream<Path> loadAll();
}
