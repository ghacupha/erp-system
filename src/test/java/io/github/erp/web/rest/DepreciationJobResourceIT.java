package io.github.erp.web.rest;

/*-
 * Erp System - Mark IX No 5 (Iddo Series) Server ver 1.6.7
 * Copyright © 2021 - 2023 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
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

import static io.github.erp.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import io.github.erp.IntegrationTest;
import io.github.erp.domain.DepreciationJob;
import io.github.erp.domain.enumeration.DepreciationJobStatusType;
import io.github.erp.repository.DepreciationJobRepository;
import io.github.erp.repository.search.DepreciationJobSearchRepository;
import io.github.erp.service.dto.DepreciationJobDTO;
import io.github.erp.service.mapper.DepreciationJobMapper;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link DepreciationJobResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class DepreciationJobResourceIT {

    private static final ZonedDateTime DEFAULT_TIME_OF_COMMENCEMENT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_TIME_OF_COMMENCEMENT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final DepreciationJobStatusType DEFAULT_DEPRECIATION_JOB_STATUS = DepreciationJobStatusType.COMPLETE;
    private static final DepreciationJobStatusType UPDATED_DEPRECIATION_JOB_STATUS = DepreciationJobStatusType.RUNNING;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/depreciation-jobs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/depreciation-jobs";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DepreciationJobRepository depreciationJobRepository;

    @Autowired
    private DepreciationJobMapper depreciationJobMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.DepreciationJobSearchRepositoryMockConfiguration
     */
    @Autowired
    private DepreciationJobSearchRepository mockDepreciationJobSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDepreciationJobMockMvc;

    private DepreciationJob depreciationJob;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DepreciationJob createEntity(EntityManager em) {
        DepreciationJob depreciationJob = new DepreciationJob()
            .timeOfCommencement(DEFAULT_TIME_OF_COMMENCEMENT)
            .depreciationJobStatus(DEFAULT_DEPRECIATION_JOB_STATUS)
            .description(DEFAULT_DESCRIPTION);
        return depreciationJob;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DepreciationJob createUpdatedEntity(EntityManager em) {
        DepreciationJob depreciationJob = new DepreciationJob()
            .timeOfCommencement(UPDATED_TIME_OF_COMMENCEMENT)
            .depreciationJobStatus(UPDATED_DEPRECIATION_JOB_STATUS)
            .description(UPDATED_DESCRIPTION);
        return depreciationJob;
    }

    @BeforeEach
    public void initTest() {
        depreciationJob = createEntity(em);
    }

    @Test
    @Transactional
    void createDepreciationJob() throws Exception {
        int databaseSizeBeforeCreate = depreciationJobRepository.findAll().size();
        // Create the DepreciationJob
        DepreciationJobDTO depreciationJobDTO = depreciationJobMapper.toDto(depreciationJob);
        restDepreciationJobMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(depreciationJobDTO))
            )
            .andExpect(status().isCreated());

        // Validate the DepreciationJob in the database
        List<DepreciationJob> depreciationJobList = depreciationJobRepository.findAll();
        assertThat(depreciationJobList).hasSize(databaseSizeBeforeCreate + 1);
        DepreciationJob testDepreciationJob = depreciationJobList.get(depreciationJobList.size() - 1);
        assertThat(testDepreciationJob.getTimeOfCommencement()).isEqualTo(DEFAULT_TIME_OF_COMMENCEMENT);
        assertThat(testDepreciationJob.getDepreciationJobStatus()).isEqualTo(DEFAULT_DEPRECIATION_JOB_STATUS);
        assertThat(testDepreciationJob.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);

        // Validate the DepreciationJob in Elasticsearch
        verify(mockDepreciationJobSearchRepository, times(1)).save(testDepreciationJob);
    }

    @Test
    @Transactional
    void createDepreciationJobWithExistingId() throws Exception {
        // Create the DepreciationJob with an existing ID
        depreciationJob.setId(1L);
        DepreciationJobDTO depreciationJobDTO = depreciationJobMapper.toDto(depreciationJob);

        int databaseSizeBeforeCreate = depreciationJobRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDepreciationJobMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(depreciationJobDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DepreciationJob in the database
        List<DepreciationJob> depreciationJobList = depreciationJobRepository.findAll();
        assertThat(depreciationJobList).hasSize(databaseSizeBeforeCreate);

        // Validate the DepreciationJob in Elasticsearch
        verify(mockDepreciationJobSearchRepository, times(0)).save(depreciationJob);
    }

    @Test
    @Transactional
    void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = depreciationJobRepository.findAll().size();
        // set the field null
        depreciationJob.setDescription(null);

        // Create the DepreciationJob, which fails.
        DepreciationJobDTO depreciationJobDTO = depreciationJobMapper.toDto(depreciationJob);

        restDepreciationJobMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(depreciationJobDTO))
            )
            .andExpect(status().isBadRequest());

        List<DepreciationJob> depreciationJobList = depreciationJobRepository.findAll();
        assertThat(depreciationJobList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDepreciationJobs() throws Exception {
        // Initialize the database
        depreciationJobRepository.saveAndFlush(depreciationJob);

        // Get all the depreciationJobList
        restDepreciationJobMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(depreciationJob.getId().intValue())))
            .andExpect(jsonPath("$.[*].timeOfCommencement").value(hasItem(sameInstant(DEFAULT_TIME_OF_COMMENCEMENT))))
            .andExpect(jsonPath("$.[*].depreciationJobStatus").value(hasItem(DEFAULT_DEPRECIATION_JOB_STATUS.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getDepreciationJob() throws Exception {
        // Initialize the database
        depreciationJobRepository.saveAndFlush(depreciationJob);

        // Get the depreciationJob
        restDepreciationJobMockMvc
            .perform(get(ENTITY_API_URL_ID, depreciationJob.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(depreciationJob.getId().intValue()))
            .andExpect(jsonPath("$.timeOfCommencement").value(sameInstant(DEFAULT_TIME_OF_COMMENCEMENT)))
            .andExpect(jsonPath("$.depreciationJobStatus").value(DEFAULT_DEPRECIATION_JOB_STATUS.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getNonExistingDepreciationJob() throws Exception {
        // Get the depreciationJob
        restDepreciationJobMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewDepreciationJob() throws Exception {
        // Initialize the database
        depreciationJobRepository.saveAndFlush(depreciationJob);

        int databaseSizeBeforeUpdate = depreciationJobRepository.findAll().size();

        // Update the depreciationJob
        DepreciationJob updatedDepreciationJob = depreciationJobRepository.findById(depreciationJob.getId()).get();
        // Disconnect from session so that the updates on updatedDepreciationJob are not directly saved in db
        em.detach(updatedDepreciationJob);
        updatedDepreciationJob
            .timeOfCommencement(UPDATED_TIME_OF_COMMENCEMENT)
            .depreciationJobStatus(UPDATED_DEPRECIATION_JOB_STATUS)
            .description(UPDATED_DESCRIPTION);
        DepreciationJobDTO depreciationJobDTO = depreciationJobMapper.toDto(updatedDepreciationJob);

        restDepreciationJobMockMvc
            .perform(
                put(ENTITY_API_URL_ID, depreciationJobDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(depreciationJobDTO))
            )
            .andExpect(status().isOk());

        // Validate the DepreciationJob in the database
        List<DepreciationJob> depreciationJobList = depreciationJobRepository.findAll();
        assertThat(depreciationJobList).hasSize(databaseSizeBeforeUpdate);
        DepreciationJob testDepreciationJob = depreciationJobList.get(depreciationJobList.size() - 1);
        assertThat(testDepreciationJob.getTimeOfCommencement()).isEqualTo(UPDATED_TIME_OF_COMMENCEMENT);
        assertThat(testDepreciationJob.getDepreciationJobStatus()).isEqualTo(UPDATED_DEPRECIATION_JOB_STATUS);
        assertThat(testDepreciationJob.getDescription()).isEqualTo(UPDATED_DESCRIPTION);

        // Validate the DepreciationJob in Elasticsearch
        verify(mockDepreciationJobSearchRepository).save(testDepreciationJob);
    }

    @Test
    @Transactional
    void putNonExistingDepreciationJob() throws Exception {
        int databaseSizeBeforeUpdate = depreciationJobRepository.findAll().size();
        depreciationJob.setId(count.incrementAndGet());

        // Create the DepreciationJob
        DepreciationJobDTO depreciationJobDTO = depreciationJobMapper.toDto(depreciationJob);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDepreciationJobMockMvc
            .perform(
                put(ENTITY_API_URL_ID, depreciationJobDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(depreciationJobDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DepreciationJob in the database
        List<DepreciationJob> depreciationJobList = depreciationJobRepository.findAll();
        assertThat(depreciationJobList).hasSize(databaseSizeBeforeUpdate);

        // Validate the DepreciationJob in Elasticsearch
        verify(mockDepreciationJobSearchRepository, times(0)).save(depreciationJob);
    }

    @Test
    @Transactional
    void putWithIdMismatchDepreciationJob() throws Exception {
        int databaseSizeBeforeUpdate = depreciationJobRepository.findAll().size();
        depreciationJob.setId(count.incrementAndGet());

        // Create the DepreciationJob
        DepreciationJobDTO depreciationJobDTO = depreciationJobMapper.toDto(depreciationJob);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDepreciationJobMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(depreciationJobDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DepreciationJob in the database
        List<DepreciationJob> depreciationJobList = depreciationJobRepository.findAll();
        assertThat(depreciationJobList).hasSize(databaseSizeBeforeUpdate);

        // Validate the DepreciationJob in Elasticsearch
        verify(mockDepreciationJobSearchRepository, times(0)).save(depreciationJob);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDepreciationJob() throws Exception {
        int databaseSizeBeforeUpdate = depreciationJobRepository.findAll().size();
        depreciationJob.setId(count.incrementAndGet());

        // Create the DepreciationJob
        DepreciationJobDTO depreciationJobDTO = depreciationJobMapper.toDto(depreciationJob);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDepreciationJobMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(depreciationJobDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DepreciationJob in the database
        List<DepreciationJob> depreciationJobList = depreciationJobRepository.findAll();
        assertThat(depreciationJobList).hasSize(databaseSizeBeforeUpdate);

        // Validate the DepreciationJob in Elasticsearch
        verify(mockDepreciationJobSearchRepository, times(0)).save(depreciationJob);
    }

    @Test
    @Transactional
    void partialUpdateDepreciationJobWithPatch() throws Exception {
        // Initialize the database
        depreciationJobRepository.saveAndFlush(depreciationJob);

        int databaseSizeBeforeUpdate = depreciationJobRepository.findAll().size();

        // Update the depreciationJob using partial update
        DepreciationJob partialUpdatedDepreciationJob = new DepreciationJob();
        partialUpdatedDepreciationJob.setId(depreciationJob.getId());

        restDepreciationJobMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDepreciationJob.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDepreciationJob))
            )
            .andExpect(status().isOk());

        // Validate the DepreciationJob in the database
        List<DepreciationJob> depreciationJobList = depreciationJobRepository.findAll();
        assertThat(depreciationJobList).hasSize(databaseSizeBeforeUpdate);
        DepreciationJob testDepreciationJob = depreciationJobList.get(depreciationJobList.size() - 1);
        assertThat(testDepreciationJob.getTimeOfCommencement()).isEqualTo(DEFAULT_TIME_OF_COMMENCEMENT);
        assertThat(testDepreciationJob.getDepreciationJobStatus()).isEqualTo(DEFAULT_DEPRECIATION_JOB_STATUS);
        assertThat(testDepreciationJob.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateDepreciationJobWithPatch() throws Exception {
        // Initialize the database
        depreciationJobRepository.saveAndFlush(depreciationJob);

        int databaseSizeBeforeUpdate = depreciationJobRepository.findAll().size();

        // Update the depreciationJob using partial update
        DepreciationJob partialUpdatedDepreciationJob = new DepreciationJob();
        partialUpdatedDepreciationJob.setId(depreciationJob.getId());

        partialUpdatedDepreciationJob
            .timeOfCommencement(UPDATED_TIME_OF_COMMENCEMENT)
            .depreciationJobStatus(UPDATED_DEPRECIATION_JOB_STATUS)
            .description(UPDATED_DESCRIPTION);

        restDepreciationJobMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDepreciationJob.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDepreciationJob))
            )
            .andExpect(status().isOk());

        // Validate the DepreciationJob in the database
        List<DepreciationJob> depreciationJobList = depreciationJobRepository.findAll();
        assertThat(depreciationJobList).hasSize(databaseSizeBeforeUpdate);
        DepreciationJob testDepreciationJob = depreciationJobList.get(depreciationJobList.size() - 1);
        assertThat(testDepreciationJob.getTimeOfCommencement()).isEqualTo(UPDATED_TIME_OF_COMMENCEMENT);
        assertThat(testDepreciationJob.getDepreciationJobStatus()).isEqualTo(UPDATED_DEPRECIATION_JOB_STATUS);
        assertThat(testDepreciationJob.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingDepreciationJob() throws Exception {
        int databaseSizeBeforeUpdate = depreciationJobRepository.findAll().size();
        depreciationJob.setId(count.incrementAndGet());

        // Create the DepreciationJob
        DepreciationJobDTO depreciationJobDTO = depreciationJobMapper.toDto(depreciationJob);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDepreciationJobMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, depreciationJobDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(depreciationJobDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DepreciationJob in the database
        List<DepreciationJob> depreciationJobList = depreciationJobRepository.findAll();
        assertThat(depreciationJobList).hasSize(databaseSizeBeforeUpdate);

        // Validate the DepreciationJob in Elasticsearch
        verify(mockDepreciationJobSearchRepository, times(0)).save(depreciationJob);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDepreciationJob() throws Exception {
        int databaseSizeBeforeUpdate = depreciationJobRepository.findAll().size();
        depreciationJob.setId(count.incrementAndGet());

        // Create the DepreciationJob
        DepreciationJobDTO depreciationJobDTO = depreciationJobMapper.toDto(depreciationJob);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDepreciationJobMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(depreciationJobDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DepreciationJob in the database
        List<DepreciationJob> depreciationJobList = depreciationJobRepository.findAll();
        assertThat(depreciationJobList).hasSize(databaseSizeBeforeUpdate);

        // Validate the DepreciationJob in Elasticsearch
        verify(mockDepreciationJobSearchRepository, times(0)).save(depreciationJob);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDepreciationJob() throws Exception {
        int databaseSizeBeforeUpdate = depreciationJobRepository.findAll().size();
        depreciationJob.setId(count.incrementAndGet());

        // Create the DepreciationJob
        DepreciationJobDTO depreciationJobDTO = depreciationJobMapper.toDto(depreciationJob);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDepreciationJobMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(depreciationJobDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DepreciationJob in the database
        List<DepreciationJob> depreciationJobList = depreciationJobRepository.findAll();
        assertThat(depreciationJobList).hasSize(databaseSizeBeforeUpdate);

        // Validate the DepreciationJob in Elasticsearch
        verify(mockDepreciationJobSearchRepository, times(0)).save(depreciationJob);
    }

    @Test
    @Transactional
    void deleteDepreciationJob() throws Exception {
        // Initialize the database
        depreciationJobRepository.saveAndFlush(depreciationJob);

        int databaseSizeBeforeDelete = depreciationJobRepository.findAll().size();

        // Delete the depreciationJob
        restDepreciationJobMockMvc
            .perform(delete(ENTITY_API_URL_ID, depreciationJob.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<DepreciationJob> depreciationJobList = depreciationJobRepository.findAll();
        assertThat(depreciationJobList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the DepreciationJob in Elasticsearch
        verify(mockDepreciationJobSearchRepository, times(1)).deleteById(depreciationJob.getId());
    }

    @Test
    @Transactional
    void searchDepreciationJob() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        depreciationJobRepository.saveAndFlush(depreciationJob);
        when(mockDepreciationJobSearchRepository.search("id:" + depreciationJob.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(depreciationJob), PageRequest.of(0, 1), 1));

        // Search the depreciationJob
        restDepreciationJobMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + depreciationJob.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(depreciationJob.getId().intValue())))
            .andExpect(jsonPath("$.[*].timeOfCommencement").value(hasItem(sameInstant(DEFAULT_TIME_OF_COMMENCEMENT))))
            .andExpect(jsonPath("$.[*].depreciationJobStatus").value(hasItem(DEFAULT_DEPRECIATION_JOB_STATUS.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }
}
