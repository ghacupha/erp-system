package io.github.erp.internal.report.attachment;

/*-
 * Erp System - Mark III No 6 (Caleb Series) Server ver 0.1.9-SNAPSHOT
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
import io.github.erp.internal.model.HasChecksum;

import java.util.UUID;

/**
 * Representation of the business-document-fso on retrieval
 */
public interface RetrievedDocument<FSO, A> extends HasChecksum, ContainsChecksumAlgorithm<A>, RecordsTamper<FSO> {

    void setDocumentFile(byte[] reportResource);

    String getDocumentTitle();

    UUID getDocumentSerial();

    String getFileContentType();

    A getChecksumAlgorithm();

    FSO isTampered(boolean isTempered);
}
