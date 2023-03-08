package io.github.erp.internal.model.mapping;

/*-
 * Erp System - Mark III No 12 (Caleb Series) Server ver 0.8.0
 * Copyright © 2021 - 2022 Edwin Njeru (mailnjeru@gmail.com)
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
import io.github.erp.internal.framework.Mapping;
import io.github.erp.internal.model.AttachedXlsxReportRequisitionDTO;
import io.github.erp.service.dto.XlsxReportRequisitionDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AttachedXlsxReportRequisitionDTOMapping extends Mapping<XlsxReportRequisitionDTO, AttachedXlsxReportRequisitionDTO> {
}
