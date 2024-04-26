package io.github.erp.internal.files.zip;

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
