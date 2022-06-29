package io.github.erp.internal.files;

/*-
 * Erp System - Mark II No 17 (Baruch Series)
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
import java.util.Locale;
import java.util.Objects;

public class FileUtils {

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
}
