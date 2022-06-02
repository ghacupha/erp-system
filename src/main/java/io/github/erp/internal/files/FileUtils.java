package io.github.erp.internal.files;

import lombok.SneakyThrows;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

public class FileUtils {

    /**
     * We check whether an existing file has a signature similar to the one provided in the
     * argument and if not return false
     *
     * @param root
     * @param file
     * @param fileMd5CheckSum
     * @return
     */
    @SneakyThrows
    public static boolean fileChecksOut(Path root, MultipartFile file, String fileMd5CheckSum) {
        String localFileMd5 = "";

        if (fileMd5CheckSum != null) {
            if (Files.exists(root.resolve(Objects.requireNonNull(file.getOriginalFilename())))) {
                Path filePath = root.resolve(Objects.requireNonNull(file.getOriginalFilename()));
                InputStream is = Files.newInputStream(filePath);
                localFileMd5 = DigestUtils.md5DigestAsHex(is);

                return fileMd5CheckSum.equalsIgnoreCase(localFileMd5);
            } else {
                return false;
            }

        } else {

            throw new RuntimeException("The file validation cannot be done against a null checksum");
        }
    }
}
