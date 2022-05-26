package io.github.erp.internal.files;

import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class PDFMultipartFile implements MultipartFile {

    private final byte[] fileContent;

    private final String fileName;

    private final String contentType;

    private File file;

    private final String destPath;

    private FileOutputStream fileOutputStream;

    public PDFMultipartFile(byte[] fileData, String destPath, String fileName) {
        this.fileContent = fileData;
        this.fileName = fileName;
        this.destPath = destPath;
        file = new File(destPath + this.fileName);
        contentType = "application/pdf";
    }

    @Override
    public void transferTo(File dest) throws IOException, IllegalStateException {
        fileOutputStream = new FileOutputStream(dest);
        fileOutputStream.write(fileContent);
    }

    public void clearOutStreams() throws IOException {
        if (null != fileOutputStream) {
            fileOutputStream.flush();
            fileOutputStream.close();
            file.deleteOnExit();
        }
    }

    @Override
    public @NotNull String getName() {
        return file.getName();
    }

    @SneakyThrows
    @Override
    public String getOriginalFilename() {
        return file.getCanonicalPath();
    }

    @Override
    public String getContentType() {
        return contentType;
    }

    @Override
    public boolean isEmpty() {
        return fileContent == null ||fileContent.length == 0;
    }

    @Override
    public long getSize() {
        return fileContent.length;
    }

    @Override
    public byte @NotNull [] getBytes() throws IOException {
        return fileContent;
    }

    @Override
    public @NotNull InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream(fileContent);
    }
}
