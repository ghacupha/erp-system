package io.github.erp.internal.files;

import com.google.common.hash.Hashing;
import lombok.SneakyThrows;
import org.bouncycastle.crypto.digests.SHA512Digest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
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
}
