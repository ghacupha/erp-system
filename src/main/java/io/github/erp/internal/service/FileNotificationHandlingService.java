package io.github.erp.internal.service;

/*-
 * Erp System - Mark IV No 1 (Ehud Series) Server ver 1.3.0
 * Copyright © 2021 - 2023 Edwin Njeru (mailnjeru@gmail.com)
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
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
// import java.util.concurrent.CompletableFuture;
// import java.util.concurrent.Executor;

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

    public FileNotificationHandlingService(TokenGenerator tokenGenerator,
                                           TokenPersistenceService<MessageTokenDTO, MessageToken> messageTokenService,
                                           FileUploadPersistenceService<FileUploadDTO> fileUploadService,
                                           @Qualifier("fileUploadProcessorChain") FileUploadProcessorChain fileUploadProcessorChain ) {
        this.tokenGenerator = tokenGenerator;
        this.messageTokenService = messageTokenService;
        this.fileUploadService = fileUploadService;
        this.fileUploadProcessorChain = fileUploadProcessorChain;
    }

    @Override
    @Async
    public void handle(FileNotification payload) {

        // CompletableFuture<Boolean> future = new CompletableFuture<>();

        // taskExecutor.execute(() -> {
        //    uploadSequence(payload);
        //    future.complete(true);
        // });

        // return future;
        uploadSequence(payload);
    }

    private void uploadSequence(FileNotification payload) {

        Assert.notNull(payload, () -> {throw new IllegalArgumentException("Submitted null notification for asynchronous processing");});

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
        log.info("FileUploadDTO object fetched from DB with id: {}", fileUpload.getId());
        if (!PROCESSED_TOKENS.contains(payload.getMessageToken())) {
            log.info("Processing message with token {}", payload.getMessageToken().substring(0, Math.min(payload.getMessageToken().length(), 5)));
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
