package io.github.erp.internal.report.attachment;

/*-
 * Erp System - Mark X No 1 (Jehoiada Series) Server ver 1.7.0
 * Copyright © 2021 - 2023 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
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
