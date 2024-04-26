package io.github.erp.internal.files;

/*-
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import io.github.erp.internal.report.ReportsProperties;
import lombok.SneakyThrows;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.xml.bind.DatatypeConverter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * This service implements the file-storage-service and as promised by the interface
 * abstracts the file-location. The client will never know where the file is installed
 * unless by query. At the time of writing such a method does not exist but  might exist
 * in future for file integrity checks.
 */
@Service("reportsFSStorageService")
public class ReportsFSStorageService implements FileStorageService {

    private final Path root;

    public ReportsFSStorageService(ReportsProperties reportsProperties) {
        root = Paths.get(reportsProperties.getReportsDirectory());
    }

    @SneakyThrows
    @Override
    public void init() {
        if(!Files.exists(root)) {
            if (!Files.isRegularFile(root)) {
                Files.createDirectory(root);
            }
        }
    }

    @Override
    public void save(MultipartFile file) {
        this.save(file, null);
    }

    /**
     * Saves the file in the argument to the file system, replacing a similar
     * file should one be found existing
     *
     * @param file
     */
    @SneakyThrows
    @Override
    public void save(MultipartFile file, String fileMd5CheckSum) {

        Files.copy(
            file.getInputStream(),
            this.root.resolve(Objects.requireNonNull(file.getOriginalFilename())),
            StandardCopyOption.REPLACE_EXISTING
        );
    }

    /**
     * Saves the file in the argument to the file system, replacing a similar
     * file should one be found existing
     *
     */

    @SneakyThrows
    @Override
    public void save(byte[] fileContent, String fileName) {

        FileUtils.save(fileContent, this.root.resolve(fileName));
    }

    @Override
    public Resource load(String filename) {
        try {
            Path file = root.resolve(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Could not read the file!");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
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
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(root.toFile());
    }

    @Override
    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.root, 1).filter(path -> !path.equals(this.root)).map(this.root::relativize);
        } catch (IOException e) {
            throw new RuntimeException("Could not load the files!");
        }
    }

    /**
     * Calculates checksum given a fileName
     *
     * @param fileName      fileName whose checksum we'll compute
     * @param algorithmName name of algorithm used to calculate checksum
     * @return file checksum
     */
    @Override
    public String calculateCheckSum(String fileName, String algorithmName) {
        return this.calculateMD5CheckSum(fileName);
    }

    @Override
    public boolean fileRemainsUnTampered(String fileName, String originalChecksum) {

        String currentChecksum = FileUtils.calculateSha512CheckSum(root, fileName);

        return currentChecksum.equalsIgnoreCase(originalChecksum);
    }
}
