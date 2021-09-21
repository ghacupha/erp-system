package io.github.erp.service.mapper;

import io.github.erp.domain.*;
import io.github.erp.service.dto.MessageTokenDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link MessageToken} and its DTO {@link MessageTokenDTO}.
 */
@Mapper(componentModel = "spring", uses = { PlaceholderMapper.class })
public interface MessageTokenMapper extends EntityMapper<MessageTokenDTO, MessageToken> {
    @Mapping(target = "placeholders", source = "placeholders", qualifiedByName = "descriptionSet")
    MessageTokenDTO toDto(MessageToken s);

    @Mapping(target = "removePlaceholder", ignore = true)
    MessageToken toEntity(MessageTokenDTO messageTokenDTO);
}
