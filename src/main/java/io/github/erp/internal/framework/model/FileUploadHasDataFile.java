package io.github.erp.internal.framework.model;

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
