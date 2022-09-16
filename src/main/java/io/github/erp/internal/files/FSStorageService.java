package io.github.erp.internal.files;

/*-
 * Erp System - Mark II No 28 (Baruch Series) Server ver 0.1.0-SNAPSHOT
 * Copyright © 2021 - 2022 Edwin Njeru (mailnjeru@gmail.com)
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
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
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
@Service
public class FSStorageService implements FileStorageService {

    private final Path root;

    public FSStorageService(ReportsProperties reportsProperties) {
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
}
