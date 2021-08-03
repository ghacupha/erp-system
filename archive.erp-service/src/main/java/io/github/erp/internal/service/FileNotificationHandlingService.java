package io.github.erp.internal.service;

/*-
 * Copyright © 2021 Edwin Njeru (mailnjeru@gmail.com)
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
import com.fasterxml.jackson.core.JsonProcessingException;
import io.github.erp.domain.MessageToken;
import io.github.erp.internal.framework.fileProcessing.FileUploadProcessorChain;
import io.github.erp.internal.framework.service.FileUploadPersistenceService;
import io.github.erp.internal.framework.service.HandlingService;
import io.github.erp.internal.framework.service.TokenPersistenceService;
import io.github.erp.internal.framework.util.TokenGenerator;
import io.github.erp.internal.model.FileNotification;
import io.github.erp.service.dto.FileUploadDTO;
import io.github.erp.service.dto.MessageTokenDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

import static io.github.erp.internal.framework.AppConstants.PROCESSED_TOKENS;

/**
 * This is a service that handles file-notification asynchronously.
 *
 */
@Service("fileNotificationHandlingService")
public class FileNotificationHandlingService implements HandlingService<FileNotification> {

    public static Logger log = LoggerFactory.getLogger(FileNotificationHandlingService.class);

    private final TokenGenerator tokenGenerator;
    private final TokenPersistenceService<MessageTokenDTO, MessageToken> messageTokenService;
    private final FileUploadPersistenceService<FileUploadDTO> fileUploadService;
    private final FileUploadProcessorChain fileUploadProcessorChain;

    public FileNotificationHandlingService(TokenGenerator tokenGenerator, TokenPersistenceService<MessageTokenDTO, MessageToken> messageTokenService, FileUploadPersistenceService<FileUploadDTO> fileUploadService, FileUploadProcessorChain fileUploadProcessorChain) {
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
        MessageToken messageToken = new MessageToken()
            .tokenValue(token)
            .description(payload.getDescription())
            .timeSent(timestamp);
        // @formatter:on

        if (messageToken != null) {
            payload.setMessageToken(messageToken.getTokenValue());
        }

        FileUploadDTO fileUpload =
            fileUploadService.findOne(Long.parseLong(payload.getFileId())).orElseThrow(() -> new IllegalArgumentException("Id # : " + payload.getFileId() + " does not exist"));

        saveFileUploadsData(payload, token, fileUpload);

        MessageTokenDTO dto = messageTokenService.save(messageToken);
        dto.setContentFullyEnqueued(true);

    }

    private void saveFileUploadsData(FileNotification payload, String token, FileUploadDTO fileUpload) {
        log.debug("FileUploadDTO object fetched from DB with id: {}", fileUpload.getId());
        if (!PROCESSED_TOKENS.contains(payload.getMessageToken())) {
            log.debug("Processing message with token {}", payload.getMessageToken());
            List<FileUploadDTO> processedFiles = fileUploadProcessorChain.apply(fileUpload, payload);
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
