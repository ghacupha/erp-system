package io.github.erp.internal.model.mapping;

import io.github.erp.internal.framework.Mapping;
import io.github.erp.internal.framework.model.FileUploadHasDataFile;
import io.github.erp.service.dto.FileUploadDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FileUploadHasDataFileMapping extends Mapping<FileUploadHasDataFile, FileUploadDTO> {
}
