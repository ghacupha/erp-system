package io.github.erp.internal.framework.batch;

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
import io.github.erp.internal.framework.FileUploadsProperties;
import io.github.erp.internal.framework.service.DeletionUploadService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

/**
 * This is a short-term attempt at creating an abstract items-deletion-reader. The deletion
 * procedure is of a particular consequence that effects deletion of data originated from
 * a given data file,  given that the data has been marked with the same unique file-upload-token
 * of the file-upload entity's instance
 */
public class EntityItemsDeletionReader implements ItemReader<List<Long>> {

    private static final Logger log = LoggerFactory.getLogger(EntityItemsDeletionReader.class);
    private final long fileId;

    private final DeletionUploadService<? extends HasIndex> fileUploadService;
    private final FileUploadsProperties fileUploadsProperties;

    // TODO Initialize later, not in the constructor
    private ListPartition<Long> listPartition;

    /**
     * This is a job-scoped class whose existence is only guaranteed for the period that a job
     * instance is running
     *
     * @param fileId This is the id of the file-upload which is coming from the batch job instance
     * @param fileUploadService The service for accessing data about the file-upload entity's instance about to be deleted
     * @param fileUploadsProperties used here to provide information about the chunk size for data to be deleted
     */
    public EntityItemsDeletionReader(
        final @Value("#{jobParameters['fileId']}") long fileId,
        final DeletionUploadService<? extends HasIndex> fileUploadService,
        final FileUploadsProperties fileUploadsProperties
    ) {
        this.fileId = fileId;
        this.fileUploadService = fileUploadService;
        this.fileUploadsProperties = fileUploadsProperties;
    }

    @PostConstruct
    private void resetIndex() {
        final List<Long> unProcessedItems = new CopyOnWriteArrayList<>();

        // update list
        fileUploadService
            .findOne(fileId)
            .ifPresent(
                fileUpload -> {
                    String messageToken = fileUpload.getUploadToken();
                    fileUploadService
                        .findAllByUploadToken(messageToken)
                        .ifPresent(
                            entities -> {
                                // TODO pass actual entities for deletion
                                unProcessedItems.addAll(entities.stream().map(HasIndex::getId).collect(Collectors.toList()));
                            }
                        );
                }
            );

        // Going for a big chunk due to expected file size
        listPartition = new ListPartition<>(fileUploadsProperties.getListSize(), unProcessedItems);

        log.info("List items realized : {}", unProcessedItems.size());
    }

    @Override
    public List<Long> read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        List<Long> processingList = listPartition.getPartition();

        log.info("Returning list of {} items", processingList.size());

        return processingList.size() == 0 ? null : processingList;
    }
}
