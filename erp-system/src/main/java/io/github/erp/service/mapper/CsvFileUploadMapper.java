package io.github.erp.service.mapper;

import io.github.erp.domain.CsvFileUpload;
import io.github.erp.service.dto.CsvFileUploadDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface CsvFileUploadMapper extends EntityMapper<CsvFileUploadDTO, CsvFileUpload> {
    @Named("id")
    @Mapping(target = "id", source = "id")
    CsvFileUploadDTO toDtoId(CsvFileUpload csvFileUpload);
}
