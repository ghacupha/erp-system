package io.github.erp.internal.model;

/*-
 * Erp System - Mark III No 5 (Caleb Series) Server ver 0.1.8-SNAPSHOT
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

import io.github.erp.service.dto.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Lob;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * File System Object for BusinessDocument. The default API on the server does not have
 * a field for storing the byte-array containing the actual document file, because it is not
 * intended to be stored into the database but on the file-system.
 * So while this BEO contains such a field it does not imply persistence
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BusinessDocumentFSO implements Serializable {

    private Long id;

    @NotNull
    private String documentTitle;

    private String description;

    @NotNull
    private UUID documentSerial;

    private ZonedDateTime lastModified;

    @NotNull
    private String attachmentFilePath;

    @Lob
    private byte[] documentFile;

    @NotNull
    private String documentFileContentType;

    private Boolean fileTampered;

    @NotNull
    private String documentFileChecksum;

    private ApplicationUserDTO createdBy;

    private ApplicationUserDTO lastModifiedBy;

    private DealerDTO originatingDepartment;

    private Set<UniversallyUniqueMappingDTO> applicationMappings = new HashSet<>();

    private Set<PlaceholderDTO> placeholders = new HashSet<>();

    private AlgorithmDTO fileChecksumAlgorithm;

    private SecurityClearanceDTO securityClearance;
}
