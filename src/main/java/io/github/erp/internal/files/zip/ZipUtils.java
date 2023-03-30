package io.github.erp.internal.files.zip;

/*-
 * Erp System - Mark III No 12 (Caleb Series) Server ver 1.0.5-SNAPSHOT
 * Copyright © 2021 - 2023 Edwin Njeru (mailnjeru@gmail.com)
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
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.model.enums.CompressionLevel;
import net.lingala.zip4j.model.enums.EncryptionMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class ZipUtils {

    public final static Logger log = LoggerFactory.getLogger(ZipUtils.class);

    /**
     * The target-file-password is represented as char[] rather than string because string is
     * immutable and likely to remain in memory and accessible to anyone with a memory-dump
     * permissions. So for the password it's probable the outer API uses string, in which case
     * we expect someone will do something like targetFilePassword#toCharArray()
     *
     * @param targetFileName The file to be created after the sequence is completed
     * @param targetFilePassword The password of the zip file
     * @param fileOrigin This is the file to be zipped
     * @throws ZipException Anything could go wrong, cross thy fingers
     */
    public static void zipFile(String targetFileName, char[] targetFilePassword, File fileOrigin) throws ZipException {

        long start = System.currentTimeMillis();

        log.info("System is about to zip the file : {} on the file system which will be named: {}", fileOrigin, targetFileName);

        ZipParameters zipParameters = new ZipParameters();
        zipParameters.setEncryptFiles(true);
        zipParameters.setCompressionLevel(CompressionLevel.HIGHER);
        zipParameters.setEncryptionMethod(EncryptionMethod.AES);

        // TODO ZipFile zipFile = new ZipFile(targetFileName, targetFilePassword.toCharArray());
        ZipFile zipFile = new ZipFile(targetFileName, targetFilePassword);
        zipFile.addFile(fileOrigin, zipParameters);

        log.info("System has created a zipped file named: {} in {} milliseconds, standby for removal of the original file", targetFileName, System.currentTimeMillis() - start);
    }
}
