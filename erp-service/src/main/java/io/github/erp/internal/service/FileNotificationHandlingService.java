package io.github.erp.internal.service;

/*-
 * Leassets Server - Leases and assets management platform
 * Copyright © 2021 Edwin Njeru (mailnjeru@gmail.com)
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

import com.fasterxml.jackson.core.JsonProcessingException;
import io.github.leassets.domain.LeassetsMessageToken;
import io.github.leassets.internal.framework.fileProcessing.FileUploadProcessorChain;
import io.github.leassets.internal.framework.service.FileUploadPersistenceService;
import io.github.leassets.internal.framework.service.HandlingService;
import io.github.leassets.internal.framework.service.TokenPersistenceService;
import io.github.leassets.internal.framework.util.TokenGenerator;
import io.github.leassets.internal.model.FileNotification;
import io.github.leassets.service.dto.LeassetsFileUploadDTO;
import io.github.leassets.service.dto.LeassetsMessageTokenDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

import static io.github.leassets.internal.framework.AppConstants.PROCESSED_TOKENS;

/**
 * This is a service that handles file-notification asynchronously.
 *
 */
@Service("fileNotificationHandlingService")
public class FileNotificationHandlingService implements HandlingService<FileNotification> {

    public static Logger log = LoggerFactory.getLogger(FileNotificationHandlingService.class);

    private final TokenGenerator tokenGenerator;
    private final TokenPersistenceService<LeassetsMessageTokenDTO, LeassetsMessageToken> messageTokenService;
    private final FileUploadPersistenceService<LeassetsFileUploadDTO> fileUploadService;
    private final FileUploadProcessorChain fileUploadProcessorChain;

    public FileNotificationHandlingService(TokenGenerator tokenGenerator, TokenPersistenceService<LeassetsMessageTokenDTO, LeassetsMessageToken> messageTokenService, FileUploadPersistenceService<LeassetsFileUploadDTO> fileUploadService, FileUploadProcessorChain fileUploadProcessorChain) {
        this.tokenGenerator = tokenGenerator;
        this.messageTokenService = messageTokenService;
        this.fileUploadService = fileUploadService;
        this.fileUploadProcessorChain = fileUploadProcessorChain;
    }

    @Override
    @Async
    public void handle(FileNotification payload) {

        log.info("File notification received for: {}", payload.getFilename());

        // Generate token before getting timestamps
        String token  = getToken(payload);

        long timestamp = System.currentTimeMillis();
        payload.setTimestamp(timestamp);

        // @formatter:off
        LeassetsMessageToken messageToken = new LeassetsMessageToken()
            .tokenValue(token)
            .description(payload.getDescription())
            .timeSent(timestamp);
        // @formatter:on

        if (messageToken != null) {
            payload.setMessageToken(messageToken.getTokenValue());
        }

        LeassetsFileUploadDTO fileUpload =
            fileUploadService.findOne(Long.parseLong(payload.getFileId())).orElseThrow(() -> new IllegalArgumentException("Id # : " + payload.getFileId() + " does not exist"));

        saveFileUploadsData(payload, token, fileUpload);

        LeassetsMessageTokenDTO dto = messageTokenService.save(messageToken);
        dto.setContentFullyEnqueued(true);

    }

    private void saveFileUploadsData(FileNotification payload, String token, LeassetsFileUploadDTO fileUpload) {
        log.debug("FileUploadDTO object fetched from DB with id: {}", fileUpload.getId());
        if (!PROCESSED_TOKENS.contains(payload.getMessageToken())) {
            log.debug("Processing message with token {}", payload.getMessageToken());
            List<LeassetsFileUploadDTO> processedFiles = fileUploadProcessorChain.apply(fileUpload, payload);
            fileUpload.setUploadProcessed(true);
            fileUpload.setUploadSuccessful(true);
            fileUpload.setUploadToken(token);
            // Explicitly persist new status
            fileUploadService.save(fileUpload);
            PROCESSED_TOKENS.add(payload.getMessageToken());
        } else {
            log.info("Skipped upload of processed files {}", payload.getFilename());
        }
    }

    private String getToken(FileNotification payload) {
        String token = "";
        try {
            token = tokenGenerator.md5Digest(payload);
        } catch (JsonProcessingException e) {
            log.error("The service has failed to create a message-token and has been aborted : ", e);
        }
        return token;
    }
}
