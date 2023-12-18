package io.github.erp.internal.files;

/*-
 * Erp System - Mark IX No 2 (Iddo Series) Server ver 1.6.4
 * Copyright © 2021 - 2023 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
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

    /**
     * Calculates the SHA512 sum of the filename provided from the working folder
     *
     * @param filename
     * @return
     */
    String calculateSha512CheckSum(String filename);

    /**
     * Calculates checksum given a fileName
     *
     * @param fileName fileName whose checksum we'll compute
     * @param algorithmName name of algorithm used to calculate checksum
     * @return file checksum
     */
    String calculateCheckSum(String fileName, String algorithmName);

    /**
     * This methods return true if the file in the file system is not tampered. It checks
     * the the current SHA512 file checkSum against the calculated one from the file system.
     *
     * @param fileName Filename from the file system
     * @param originalChecksum Original checksum calculated when saving the file
     * @return TRUE if the file is uncompromised.
     */
    boolean fileRemainsUnTampered(String fileName, String originalChecksum);
}
