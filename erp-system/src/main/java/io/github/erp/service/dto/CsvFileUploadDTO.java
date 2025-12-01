package io.github.erp.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

public class CsvFileUploadDTO implements Serializable {

    private Long id;
    private String originalFileName;
    private String storedFileName;
    private String filePath;
    private Long fileSize;
    private String contentType;
    private Instant uploadedAt;
    private Boolean processed;
    private String checksum;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOriginalFileName() {
        return originalFileName;
    }

    public void setOriginalFileName(String originalFileName) {
        this.originalFileName = originalFileName;
    }

    public String getStoredFileName() {
        return storedFileName;
    }

    public void setStoredFileName(String storedFileName) {
        this.storedFileName = storedFileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Instant getUploadedAt() {
        return uploadedAt;
    }

    public void setUploadedAt(Instant uploadedAt) {
        this.uploadedAt = uploadedAt;
    }

    public Boolean getProcessed() {
        return processed;
    }

    public void setProcessed(Boolean processed) {
        this.processed = processed;
    }

    public String getChecksum() {
        return checksum;
    }

    public void setChecksum(String checksum) {
        this.checksum = checksum;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CsvFileUploadDTO)) {
            return false;
        }
        CsvFileUploadDTO that = (CsvFileUploadDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    @Override
    public String toString() {
        return "CsvFileUploadDTO{" +
            "id=" + getId() +
            ", storedFileName='" + getStoredFileName() + '\'' +
            ", uploadedAt=" + getUploadedAt() +
            '}';
    }
}
