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
