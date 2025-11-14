package io.github.erp.internal.files;

/*-
 * Erp System - Mark X No 11 (Jehoiada Series) Server ver 1.8.3
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.stream.Stream;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

/**
 * Storage service dedicated to CSV uploads handled by batch processing jobs.
 */
@Service("csvUploadFSStorageService")
public class CsvUploadFSStorageService implements FileStorageService {

    private static final Logger log = LoggerFactory.getLogger(CsvUploadFSStorageService.class);

    private final Path root;

    public CsvUploadFSStorageService(
        @Value("${erp.csv-upload.storage-path:${java.io.tmpdir}/erp/csv-uploads}") String storagePath
    ) {
        this.root = Paths.get(storagePath);
    }

    @PostConstruct
    public void ensureInitialized() {
        init();
    }

    @Override
    public void init() {
        try {
            if (!Files.exists(root)) {
                Files.createDirectories(root);
            }
        } catch (IOException e) {
            throw new IllegalStateException("Unable to initialize CSV upload storage directory", e);
        }
    }

    @Override
    public void save(MultipartFile file, String fileMd5CheckSum) {
        try {
            Files.copy(
                file.getInputStream(),
                this.root.resolve(Objects.requireNonNull(file.getOriginalFilename())),
                StandardCopyOption.REPLACE_EXISTING
            );
        } catch (IOException e) {
            throw new IllegalStateException("Failed to store file " + file.getOriginalFilename(), e);
        }
    }

    @Override
    public void save(MultipartFile file) {
        save(file, null);
    }

    @Override
    public void save(byte[] fileContent, String fileName) {
        try {
            FileUtils.save(fileContent, this.root.resolve(fileName));
        } catch (IOException e) {
            throw new IllegalStateException("Failed to store file " + fileName, e);
        }
    }

    @Override
    public Resource load(String filename) {
        try {
            Path file = root.resolve(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() && resource.isReadable()) {
                return resource;
            }
            throw new IllegalStateException("Could not read the file " + filename);
        } catch (IOException e) {
            throw new IllegalStateException("Could not read the file " + filename, e);
        }
    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(root.toFile());
    }

    @Override
    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.root, 1).filter(path -> !path.equals(this.root)).map(this.root::relativize);
        } catch (IOException e) {
            throw new IllegalStateException("Could not load stored files", e);
        }
    }

    @Override
    public String calculateMD5CheckSum(String filename) {
        return FileUtils.calculateMD5CheckSum(root, filename);
    }

    @Override
    public String calculateSha512CheckSum(String filename) {
        return FileUtils.calculateSha512CheckSum(root, filename);
    }

    @Override
    public String calculateCheckSum(String fileName, String algorithmName) {
        log.debug("Requested checksum using {} for file {}. Falling back to MD5.", algorithmName, fileName);
        return calculateMD5CheckSum(fileName);
    }

    @Override
    public boolean fileRemainsUnTampered(String fileName, String originalChecksum) {
        String currentChecksum = FileUtils.calculateSha512CheckSum(root, fileName);
        return currentChecksum.equalsIgnoreCase(originalChecksum);
    }
}

