package io.github.erp.internal.framework.model;

/*-
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
