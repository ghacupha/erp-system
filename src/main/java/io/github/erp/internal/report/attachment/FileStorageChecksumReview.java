package io.github.erp.internal.report.attachment;

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
        return documentChecksum.equalsIgnoreCase(fileStorageService.calculateCheckSum(fileName, checksumAlgorithm.getName()));
    }
}
