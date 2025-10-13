package io.github.erp.internal.framework.service;

/*-
 * Erp System - Mark X No 10 (Jehoiada Series) Server ver 1.8.2
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
import java.util.Optional;

/**
 * This class is intended for use with the deletion job listener. This is the listener called after file-related
 * data has been removed from the repository and the file can now safely be deleted as well.
 * The parameter FUDTO is the file-upload-DTO which contains the binary file array for be removed from the library.
 * To remain generic the listener has been configured to use the has-index interface for the FUDTO parameter, that
 * way the listener can then be configured with the project specific file-upload entity
 */
public interface FileUploadDeletionService<FUDTO> {

    Optional<FUDTO> findOne(long fileId);

    void delete(Long id);
}
