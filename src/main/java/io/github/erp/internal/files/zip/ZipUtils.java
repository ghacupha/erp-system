package io.github.erp.internal.files.zip;

import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.model.enums.CompressionLevel;
import net.lingala.zip4j.model.enums.EncryptionMethod;

import java.io.File;

public class ZipUtils {

    public static void zipFile(String targetFileName, String targetFilePassword, File toZip) throws ZipException {

        ZipParameters zipParameters = new ZipParameters();
        zipParameters.setEncryptFiles(true);
        zipParameters.setCompressionLevel(CompressionLevel.HIGHER);
        zipParameters.setEncryptionMethod(EncryptionMethod.AES);

        // ZipFile zipFile = new ZipFile("compressed.zip", "password".toCharArray());
        ZipFile zipFile = new ZipFile(targetFileName, targetFilePassword.toCharArray());
        // zipFile.addFile(new File("aFile.txt"), zipParameters);
        zipFile.addFile(toZip, zipParameters);
    }
}
