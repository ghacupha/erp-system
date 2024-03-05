package io.github.erp.internal.report.dynamic;

/*-
 * Erp System - Mark X No 5 (Jehoiada Series) Server ver 1.7.5
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
