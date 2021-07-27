package io.github.erp.service.mapper;


import io.github.erp.domain.*;
import io.github.erp.service.dto.FileUploadDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link FileUpload} and its DTO {@link FileUploadDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface FileUploadMapper extends EntityMapper<FileUploadDTO, FileUpload> {



    default FileUpload fromId(Long id) {
        if (id == null) {
            return null;
        }
        FileUpload fileUpload = new FileUpload();
        fileUpload.setId(id);
        return fileUpload;
    }
}
