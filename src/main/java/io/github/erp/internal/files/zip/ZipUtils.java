package io.github.erp.internal.files.zip;

/*-
 * Erp System - Mark II No 7 (Artaxerxes Series)
 * Copyright © 2021 Edwin Njeru (mailnjeru@gmail.com)
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

    public static void zipFile(String targetFileName, char[] targetFilePassword, File toZip) throws ZipException {

        long start = System.currentTimeMillis();

        log.info("System is about to zip the file : {} on the file system which will be named: {}", targetFileName, toZip);

        ZipParameters zipParameters = new ZipParameters();
        zipParameters.setEncryptFiles(true);
        zipParameters.setCompressionLevel(CompressionLevel.HIGHER);
        zipParameters.setEncryptionMethod(EncryptionMethod.AES);

        // TODO ZipFile zipFile = new ZipFile(targetFileName, targetFilePassword.toCharArray());
        ZipFile zipFile = new ZipFile(targetFileName, targetFilePassword);
        zipFile.addFile(toZip, zipParameters);

        log.info("System has created a zipped file named: {} in {} milliseconds, standby for removal of the original file", toZip, System.currentTimeMillis() - start);
    }
}
