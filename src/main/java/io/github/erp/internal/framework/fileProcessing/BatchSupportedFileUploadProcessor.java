
/*-
 * Erp System - Mark X No 3 (Jehoiada Series) Server ver 1.7.3
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
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
/*-
 *  Server - Leases and assets management platform
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
package io.github.erp.internal.framework.fileProcessing;

import io.github.erp.domain.enumeration.FileModelType;
import io.github.erp.internal.model.FileNotification;
import io.github.erp.service.dto.FileUploadDTO;
import org.slf4j.Logger;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;

/**
 * This class is create to enable code reuse where the only parameter that changes the file-model-type
 */
public class BatchSupportedFileUploadProcessor implements FileUploadProcessor<FileUploadDTO>  {
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(BatchSupportedFileUploadProcessor.class);

    private final JobLauncher jobLauncher;
    public final Job batchJob;
    private final FileModelType fileModelType;

    public BatchSupportedFileUploadProcessor(final JobLauncher jobLauncher, final Job batchJob, final FileModelType fileModelType) {
        this.jobLauncher = jobLauncher;
        this.batchJob = batchJob;
        this.fileModelType = fileModelType;
    }

    @Override
    public FileUploadDTO processFileUpload(final FileUploadDTO fileUpload, final FileNotification fileNotification) {

        if (fileNotification.getfileModelType() == fileModelType) {
            log.debug("File-upload type confirmed commencing process...");

            JobParametersBuilder jobParametersBuilder = new JobParametersBuilder();
            jobParametersBuilder.addLong("fileId", fileUpload.getId());
            jobParametersBuilder.addLong("startUpTime", fileNotification.getTimestamp());
            jobParametersBuilder.addString("messageToken", fileNotification.getMessageToken());

            try {
                jobLauncher.run(batchJob, jobParametersBuilder.toJobParameters());
            } catch (JobExecutionAlreadyRunningException | JobRestartException | JobParametersInvalidException | JobInstanceAlreadyCompleteException e) {
                e.printStackTrace();
            }
        } else {

            log.debug("File upload inconsistent with the data model supported by this processor");
        }

        return fileUpload;
    }
}
