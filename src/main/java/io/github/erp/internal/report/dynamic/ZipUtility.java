package io.github.erp.internal.report.dynamic;

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

    public static byte[] zipByteStream(byte[] inputBytes, char[] password) {
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(inputBytes);
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
             ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream, password)) {

            ZipParameters zipParameters = new ZipParameters();
            zipParameters.setEncryptFiles(true);
            zipParameters.setEncryptionMethod(EncryptionMethod.AES);

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
