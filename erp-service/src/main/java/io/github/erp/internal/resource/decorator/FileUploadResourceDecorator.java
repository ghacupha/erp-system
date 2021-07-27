package io.github.erp.internal.resource.decorator;

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

import io.github.leassets.service.dto.LeassetsFileUploadCriteria;
import io.github.leassets.service.dto.LeassetsFileUploadDTO;
import io.github.leassets.web.rest.LeassetsFileUploadResource;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URISyntaxException;
import java.util.List;

/**
 * This decorator implements the {@code IFileUploadResource} using the original {@code FileUploadResource}
 * <p/>
 * which has the effect that the original code will have not indication whatsoever that it has been
 * <p/>
 * tempered with to implement some interface
 * <p/>
 * This class can now be extented by a client to extend the original code without repeatative calls
 * <p/>
 * to the original code itself. Check child classes for illustration
 */
@Component("fileUploadResourceDecorator")
public class FileUploadResourceDecorator implements IFileUploadResource {

    private final LeassetsFileUploadResource fileUploadResource;

    public FileUploadResourceDecorator(final LeassetsFileUploadResource fileUploadResource) {
        this.fileUploadResource = fileUploadResource;
    }

    /**
     * {@code POST  /file-uploads} : Create a new fileUpload.
     *
     * @param fileUploadDTO the fileUploadDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new fileUploadDTO, or with status {@code 400 (Bad Request)} if the fileUpload has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/file-uploads")
    public ResponseEntity<LeassetsFileUploadDTO> createFileUpload(@Valid @RequestBody LeassetsFileUploadDTO fileUploadDTO) throws URISyntaxException {

        return fileUploadResource.createLeassetsFileUpload(fileUploadDTO);
    }

    /**
     * {@code PUT  /file-uploads} : Updates an existing fileUpload.
     *
     * @param fileUploadDTO the fileUploadDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fileUploadDTO, or with status {@code 400 (Bad Request)} if the fileUploadDTO is not valid, or with
     * status {@code 500 (Internal Server Error)} if the fileUploadDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/file-uploads")
    public ResponseEntity<LeassetsFileUploadDTO> updateFileUpload(@Valid @RequestBody LeassetsFileUploadDTO fileUploadDTO) throws URISyntaxException {

        return fileUploadResource.updateLeassetsFileUpload(fileUploadDTO);
    }

    /**
     * {@code GET  /file-uploads} : get all the fileUploads.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of fileUploads in body.
     */
    @GetMapping("/file-uploads")
    public ResponseEntity<List<LeassetsFileUploadDTO>> getAllFileUploads(LeassetsFileUploadCriteria criteria, Pageable pageable) {

        return fileUploadResource.getAllLeassetsFileUploads(criteria, pageable);
    }

    /**
     * {@code GET  /file-uploads/count} : count all the fileUploads.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/file-uploads/count")
    public ResponseEntity<Long> countFileUploads(LeassetsFileUploadCriteria criteria) {

        return fileUploadResource.countLeassetsFileUploads(criteria);
    }

    /**
     * {@code GET  /file-uploads/:id} : get the "id" fileUpload.
     *
     * @param id the id of the fileUploadDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the fileUploadDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/file-uploads/{id}")
    public ResponseEntity<LeassetsFileUploadDTO> getFileUpload(@PathVariable Long id) {

        return fileUploadResource.getLeassetsFileUpload(id);
    }

    /**
     * {@code DELETE  /file-uploads/:id} : delete the "id" fileUpload.
     *
     * @param id the id of the fileUploadDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/file-uploads/{id}")
    public ResponseEntity<Void> deleteFileUpload(@PathVariable Long id) {

        return fileUploadResource.deleteLeassetsFileUpload(id);
    }
}
