package io.github.erp.service.mapper;

import io.github.erp.domain.*;
import io.github.erp.service.dto.FileTypeDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link FileType} and its DTO {@link FileTypeDTO}.
 */
@Mapper(componentModel = "spring", uses = { PlaceholderMapper.class })
public interface FileTypeMapper extends EntityMapper<FileTypeDTO, FileType> {
    @Mapping(target = "placeholders", source = "placeholders", qualifiedByName = "idSet")
    FileTypeDTO toDto(FileType s);

    @Mapping(target = "removePlaceholder", ignore = true)
    FileType toEntity(FileTypeDTO fileTypeDTO);
}
