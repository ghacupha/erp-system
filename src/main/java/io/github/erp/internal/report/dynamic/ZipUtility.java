package io.github.erp.internal.report.dynamic;

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
import net.lingala.zip4j.io.inputstream.ZipInputStream;
import net.lingala.zip4j.io.outputstream.ZipOutputStream;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.model.enums.EncryptionMethod;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * General utility for handling zip of files.
 */
public class ZipUtility {

    public static byte[] zipByteStream(byte[] inputBytes, String fileNameInZip, char[] password) {
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(inputBytes);
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
             ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream, password)) {

            ZipParameters zipParameters = new ZipParameters();
            zipParameters.setEncryptFiles(true);
            zipParameters.setEncryptionMethod(EncryptionMethod.AES);

            // Use the specified filename for the Excel file inside the zip folder
            zipParameters.setFileNameInZip(fileNameInZip);

            zipOutputStream.putNextEntry(zipParameters);

            byte[] buffer = new byte[1024];
            int len;
            while ((len = inputStream.read(buffer)) > 0) {
                zipOutputStream.write(buffer, 0, len);
            }

            zipOutputStream.closeEntry();

            return outputStream.toByteArray();

        } catch (IOException e) {
            e.printStackTrace();
            // Handle exceptions as needed
            return new byte[0];
        }
    }

    public static byte[] unencryptedZipByteStream(byte[] inputBytes, String fileNameInZip) {
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(inputBytes);
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
             ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream)) {

            ZipParameters zipParameters = new ZipParameters();
            zipParameters.setEncryptFiles(false);

            // Use the specified filename for the Excel file inside the zip folder
            zipParameters.setFileNameInZip(fileNameInZip);

            zipOutputStream.putNextEntry(zipParameters);

            byte[] buffer = new byte[1024];
            int len;
            while ((len = inputStream.read(buffer)) > 0) {
                zipOutputStream.write(buffer, 0, len);
            }

            zipOutputStream.closeEntry();

            return outputStream.toByteArray();

        } catch (IOException e) {
            e.printStackTrace();
            // Handle exceptions as needed
            return new byte[0];
        }
    }

    public static byte[] unzipByteStream(byte[] zippedBytes, char[] password) {
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(zippedBytes);
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
             ZipInputStream zipInputStream = new ZipInputStream(inputStream, password)) {

            zipInputStream.getNextEntry();

            byte[] buffer = new byte[1024];
            int len;
            while ((len = zipInputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, len);
            }

            return outputStream.toByteArray();

        } catch (IOException e) {
            e.printStackTrace();
            // Handle exceptions as needed
            return new byte[0];
        }
    }
}
