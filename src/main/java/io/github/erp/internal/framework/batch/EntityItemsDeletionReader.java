package io.github.erp.internal.framework.batch;

/*-
 * Erp System - Mark III No 6 (Caleb Series) Server ver 0.2.0
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
import io.github.erp.internal.framework.FileUploadsProperties;
import io.github.erp.internal.framework.model.FileUploadHasDataFile;
import io.github.erp.internal.framework.service.DataFileContainer;
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
    private DataFileContainer<FileUploadHasDataFile> dataFileContainer;
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
        final FileUploadsProperties fileUploadsProperties,
        DataFileContainer<FileUploadHasDataFile> dataFileContainer
    ) {
        this.fileId = fileId;
        this.fileUploadService = fileUploadService;
        this.fileUploadsProperties = fileUploadsProperties;
        this.dataFileContainer = dataFileContainer;
    }

    @PostConstruct
    private void resetIndex() {
        final List<Long> unProcessedItems = new CopyOnWriteArrayList<>();

        // update list
        dataFileContainer
            .findOne(fileId)
            .ifPresent(
                fileUpload -> {
                    String messageToken = fileUpload.getUploadToken();
                    fileUploadService
                        .findAllByUploadToken(messageToken)
                        .ifPresent(
                            entities -> {
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
