package io.github.erp.internal.files;

/*-
 * Erp System - Mark III No 5 (Caleb Series) Server ver 0.1.7-SNAPSHOT
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
import org.springframework.core.io.Resource;
import java.nio.file.Path;
import java.util.stream.Stream;

import org.springframework.web.multipart.MultipartFile;

/**
 * This interface presents the basic working requirements to use a file from the
 * file system. The system assumes that the file location is configured by the implementation
 * at startup
 */
public interface FileStorageService {

    void init();

    /**
     * Takes a multipart file from arguments and saves the file to the "working folder"
     */
    void save(MultipartFile file, String fileMd5CheckSum);

    void save(MultipartFile file);

    /**
     * Loads an item from the working folder whose name matches the argument
     *
     * @param filename
     * @return
     */
    Resource load(String filename);

    /**
     * Deletes everything in the working folder
     */
    void deleteAll();

    /**
     * Loads all existing items in the working folder
     *
     * @return
     */
    Stream<Path> loadAll();

    /**
     * Calculates the MD5 sum of the filename provided from the working folder
     *
     * @param filename
     * @return
     */
    String calculateMD5CheckSum(String filename);
}
