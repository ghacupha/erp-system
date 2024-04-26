package io.github.erp.internal.report.attachment;

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
import io.github.erp.internal.files.FileStorageService;
import io.github.erp.service.dto.AlgorithmDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class FileStorageChecksumReview implements MatchesChecksum<AlgorithmDTO> {

    private final FileStorageService fileStorageService;

    public FileStorageChecksumReview(@Qualifier("businessDocumentFSStorageService") FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

    /**
     * This method calculates internally the checksum of a file comparing the checksum in store
     * and returns true if they match, and false otherwise
     *
     * @param documentChecksum  Existing checksum of the file from records (DB)
     * @param checksumAlgorithm Algorithm used to calculate the checksum
     * @return true is the checksum is matching
     */
    @Override
    public boolean checksumIsMatching(String documentChecksum, String fileName, AlgorithmDTO checksumAlgorithm) {
        return !documentChecksum.equalsIgnoreCase(fileStorageService.calculateCheckSum(fileName, checksumAlgorithm.getName()));
    }
}
