package io.github.erp.service.mapper;

/*-
 * Erp System - Mark X No 10 (Jehoiada Series) Server ver 1.8.2
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

import static org.assertj.core.api.Assertions.assertThat;

import io.github.erp.domain.LeaseLiabilityCompilation;
import io.github.erp.domain.LeaseLiabilityScheduleItem;
import io.github.erp.service.dto.LeaseLiabilityCompilationDTO;
import io.github.erp.service.dto.LeaseLiabilityScheduleItemDTO;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LeaseLiabilityScheduleItemMapperTest {

    private LeaseLiabilityScheduleItemMapper leaseLiabilityScheduleItemMapper;

    @BeforeEach
    public void setUp() {
        LeaseLiabilityScheduleItemMapperImpl mapper = new LeaseLiabilityScheduleItemMapperImpl();
        mapper.setLeaseLiabilityCompilationMapper(new LeaseLiabilityCompilationMapperStub());
        leaseLiabilityScheduleItemMapper = mapper;
    }

    @Test
    void shouldMapActiveFlagAndCompilationReference() {
        LeaseLiabilityCompilation compilation = new LeaseLiabilityCompilation();
        compilation.setId(5L);

        LeaseLiabilityScheduleItem entity = new LeaseLiabilityScheduleItem().id(1L).active(true);
        entity.setLeaseLiabilityCompilation(compilation);

        LeaseLiabilityScheduleItemDTO dto = leaseLiabilityScheduleItemMapper.toDto(entity);

        assertThat(dto.getActive()).isTrue();
        assertThat(dto.getLeaseLiabilityCompilation()).isNotNull();
        assertThat(dto.getLeaseLiabilityCompilation().getId()).isEqualTo(5L);
    }

    private static class LeaseLiabilityCompilationMapperStub implements LeaseLiabilityCompilationMapper {

        @Override
        public LeaseLiabilityCompilation toEntity(LeaseLiabilityCompilationDTO dto) {
            if (dto == null) {
                return null;
            }
            LeaseLiabilityCompilation entity = new LeaseLiabilityCompilation();
            entity.setId(dto.getId());
            return entity;
        }

        @Override
        public LeaseLiabilityCompilationDTO toDto(LeaseLiabilityCompilation entity) {
            if (entity == null) {
                return null;
            }
            LeaseLiabilityCompilationDTO dto = new LeaseLiabilityCompilationDTO();
            dto.setId(entity.getId());
            return dto;
        }

        @Override
        public List<LeaseLiabilityCompilation> toEntity(List<LeaseLiabilityCompilationDTO> dtoList) {
            if (dtoList == null) {
                return null;
            }
            List<LeaseLiabilityCompilation> entities = new ArrayList<>(dtoList.size());
            for (LeaseLiabilityCompilationDTO dto : dtoList) {
                entities.add(toEntity(dto));
            }
            return entities;
        }

        @Override
        public List<LeaseLiabilityCompilationDTO> toDto(List<LeaseLiabilityCompilation> entityList) {
            if (entityList == null) {
                return null;
            }
            List<LeaseLiabilityCompilationDTO> dtos = new ArrayList<>(entityList.size());
            for (LeaseLiabilityCompilation entity : entityList) {
                dtos.add(toDto(entity));
            }
            return dtos;
        }

        @Override
        public void partialUpdate(LeaseLiabilityCompilation entity, LeaseLiabilityCompilationDTO dto) {
            if (entity == null || dto == null) {
                return;
            }
            if (dto.getId() != null) {
                entity.setId(dto.getId());
            }
        }

        @Override
        public LeaseLiabilityCompilationDTO toDtoId(LeaseLiabilityCompilation leaseLiabilityCompilation) {
            return toDto(leaseLiabilityCompilation);
        }
    }
}
