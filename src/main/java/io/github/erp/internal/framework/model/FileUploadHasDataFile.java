package io.github.erp.internal.framework.model;

import io.github.erp.internal.framework.batch.HasDataFile;
import io.github.erp.service.dto.PlaceholderDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Lob;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileUploadHasDataFile implements Serializable, HasDataFile {

    private Long id;

    private String description;

    private String fileName;

    private LocalDate periodFrom;

    private LocalDate periodTo;

    private Long fileTypeId;

    private byte[] dataFile;

    private String dataFileContentType;
    private Boolean uploadSuccessful;

    private Boolean uploadProcessed;


    private String uploadToken;

    private Set<PlaceholderDTO> placeholders = new HashSet<>();
}
