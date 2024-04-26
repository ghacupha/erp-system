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
/**
 * This interface checks if the checksum on the file and the checksum calculated for the same
 * file at present are the same
 */
public interface MatchesChecksum<Algo> {

    /**
     * This method calculates internally the checksum of a file comparing the checksum in store
     * and returns true if they match, and false otherwise
     *
     * @param documentChecksum Existing checksum of the file from records (DB)
     * @param fileName Name of the file whose checksum we seek
     * @param checksumAlgorithm Algorithm used to calculate the checksum
     * @return true is the checksum is matching
     */
    boolean checksumIsMatching(String documentChecksum, String fileName, Algo checksumAlgorithm);
}
