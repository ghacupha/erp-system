package io.github.erp.internal.files;

/*-
 * Erp System - Mark IX No 1 (Iddo Series) Server ver 1.6.3
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
import com.google.common.hash.Hashing;
import lombok.SneakyThrows;
import org.bouncycastle.crypto.digests.SHA512Digest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.xml.bind.DatatypeConverter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class FileUtils {

    private static final Map<String, String> fileExtensionMap;

    static {
        fileExtensionMap = new HashMap<>();
        // MS Office
        fileExtensionMap.put("doc", "application/msword");
        fileExtensionMap.put("dot", "application/msword");
        fileExtensionMap.put("docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document");
        fileExtensionMap.put("dotx", "application/vnd.openxmlformats-officedocument.wordprocessingml.template");
        fileExtensionMap.put("docm", "application/vnd.ms-word.document.macroEnabled.12");
        fileExtensionMap.put("dotm", "application/vnd.ms-word.template.macroEnabled.12");
        fileExtensionMap.put("xls", "application/vnd.ms-excel");
        fileExtensionMap.put("xlt", "application/vnd.ms-excel");
        fileExtensionMap.put("xla", "application/vnd.ms-excel");
        fileExtensionMap.put("xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        fileExtensionMap.put("xltx", "application/vnd.openxmlformats-officedocument.spreadsheetml.template");
        fileExtensionMap.put("xlsm", "application/vnd.ms-excel.sheet.macroEnabled.12");
        fileExtensionMap.put("xltm", "application/vnd.ms-excel.template.macroEnabled.12");
        fileExtensionMap.put("xlam", "application/vnd.ms-excel.addin.macroEnabled.12");
        fileExtensionMap.put("xlsb", "application/vnd.ms-excel.sheet.binary.macroEnabled.12");
        fileExtensionMap.put("ppt", "application/vnd.ms-powerpoint");
        fileExtensionMap.put("pot", "application/vnd.ms-powerpoint");
        fileExtensionMap.put("pps", "application/vnd.ms-powerpoint");
        fileExtensionMap.put("ppa", "application/vnd.ms-powerpoint");
        fileExtensionMap.put("pptx", "application/vnd.openxmlformats-officedocument.presentationml.presentation");
        fileExtensionMap.put("potx", "application/vnd.openxmlformats-officedocument.presentationml.template");
        fileExtensionMap.put("ppsx", "application/vnd.openxmlformats-officedocument.presentationml.slideshow");
        fileExtensionMap.put("ppam", "application/vnd.ms-powerpoint.addin.macroEnabled.12");
        fileExtensionMap.put("pptm", "application/vnd.ms-powerpoint.presentation.macroEnabled.12");
        fileExtensionMap.put("potm", "application/vnd.ms-powerpoint.presentation.macroEnabled.12");
        fileExtensionMap.put("ppsm", "application/vnd.ms-powerpoint.slideshow.macroEnabled.12");
        // Open Office
        fileExtensionMap.put("odt", "application/vnd.oasis.opendocument.text");
        fileExtensionMap.put("ott", "application/vnd.oasis.opendocument.text-template");
        fileExtensionMap.put("oth", "application/vnd.oasis.opendocument.text-web");
        fileExtensionMap.put("odm", "application/vnd.oasis.opendocument.text-master");
        fileExtensionMap.put("odg", "application/vnd.oasis.opendocument.graphics");
        fileExtensionMap.put("otg", "application/vnd.oasis.opendocument.graphics-template");
        fileExtensionMap.put("odp", "application/vnd.oasis.opendocument.presentation");
        fileExtensionMap.put("otp", "application/vnd.oasis.opendocument.presentation-template");
        fileExtensionMap.put("ods", "application/vnd.oasis.opendocument.spreadsheet");
        fileExtensionMap.put("ots", "application/vnd.oasis.opendocument.spreadsheet-template");
        fileExtensionMap.put("odc", "application/vnd.oasis.opendocument.chart");
        fileExtensionMap.put("odf", "application/vnd.oasis.opendocument.formula");
        fileExtensionMap.put("odb", "application/vnd.oasis.opendocument.database");
        fileExtensionMap.put("odi", "application/vnd.oasis.opendocument.image");
        fileExtensionMap.put("oxt", "application/vnd.openofficeorg.extension");
        // Other
        fileExtensionMap.put("txt", "text/plain");
        fileExtensionMap.put("rtf", "application/rtf");
        fileExtensionMap.put("pdf", "application/pdf");
    }

    private static final Logger log = LoggerFactory.getLogger(FileUtils.class);

    /**
     * We check whether an existing file has a signature similar to the one provided in the
     * argument and if not return false
     *
     * @param root
     * @param file
     * @param fileCheckSum
     * @return
     */
    @SneakyThrows
    public static boolean fileChecksOut(Path root, MultipartFile file, String fileCheckSum) {

        log.info("Checking if the file: {} is the same as the one currently in the system with checksum",
            file.getOriginalFilename());

        String localFile = "";

        if (fileCheckSum != null) {
            if (Files.exists(root.resolve(Objects.requireNonNull(file.getOriginalFilename())))) {
                Path filePath = root.resolve(Objects.requireNonNull(file.getOriginalFilename()));
                InputStream is = Files.newInputStream(filePath);
                // TODO localFileMd5 = DigestUtils.md5DigestAsHex(is.readAllBytes());
                localFile = com.google.common.io.Files.hash(filePath.toFile(), Hashing.sha512()).toString();

                return fileCheckSum.equalsIgnoreCase(localFile);
            } else {
                log.info("NULL checksum provided aborting integrity checks...");
                return false;
            }

        } else {

            throw new RuntimeException("The file validation cannot be done against a null checksum");
        }
    }

    public static String calculateMD5CheckSum(final Path root, String filename) {
        Path file = root.resolve(filename);
//            //Resource resource = new UrlResource(file.toUri());
//            if (resource.exists() || resource.isReadable()) {

        String md5Digest = "";

        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");

            File nativeFile = file.toFile();
            byte[] fileContent;
            try {
                fileContent = Files.readAllBytes(nativeFile.toPath());
            } catch (IOException e) {
                throw new RuntimeException("We were unable to read the file: " + nativeFile.getAbsolutePath());
            }

            md5.update(fileContent);
            byte[] digest = md5.digest();

            md5Digest = DatatypeConverter.printHexBinary(digest).toUpperCase(Locale.ROOT);

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Algorithm exception. Check runtime environment", e);
        }

        return md5Digest;
    }

    public static String calculateSha512CheckSum(final Path root, String filename) {
        Path file = root.resolve(filename);
//            //Resource resource = new UrlResource(file.toUri());
//            if (resource.exists() || resource.isReadable()) {

        String fileDigest = "";

        try {
            MessageDigest sha512 = MessageDigest.getInstance("SHA-512");

            File nativeFile = file.toFile();
            byte[] fileContent;
            try {
                fileContent = Files.readAllBytes(nativeFile.toPath());
            } catch (IOException e) {
                throw new RuntimeException("We were unable to read the file: " + nativeFile.getAbsolutePath());
            }

            sha512.update(fileContent);
            byte[] digest = sha512.digest();

            fileDigest = DatatypeConverter.printHexBinary(digest).toUpperCase(Locale.ROOT);

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Algorithm exception. Check runtime environment", e);
        }

        return fileDigest;
    }

    /**
     * Get file extension given the fileContentType
     * @param fileContentType
     * @return
     */
    public static String getFileExtension(String fileContentType) {

        return getKeys(fileExtensionMap, fileContentType).stream().findFirst().orElse("erp");
    }

    private static <K, V> Set<K> getKeys(Map<K, V> map, V value) {
        Set<K> keys = new HashSet<>();
        for (Map.Entry<K, V> entry : map.entrySet()) {
            if (entry.getValue().equals(value)) {
                keys.add(entry.getKey());
            }
        }
        return keys;
    }
}
