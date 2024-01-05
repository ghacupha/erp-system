package io.github.erp.web.rest;

/*-
 * Erp System - Mark X No 1 (Jehoiada Series) Server ver 1.7.0
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
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import io.github.erp.IntegrationTest;
import io.github.erp.domain.DepreciationBatchSequence;
import io.github.erp.domain.enumeration.DepreciationBatchStatusType;
import io.github.erp.repository.DepreciationBatchSequenceRepository;
import io.github.erp.repository.search.DepreciationBatchSequenceSearchRepository;
import io.github.erp.service.dto.DepreciationBatchSequenceDTO;
import io.github.erp.service.mapper.DepreciationBatchSequenceMapper;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.UUID;
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
 * Integration tests for the {@link DepreciationBatchSequenceResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class DepreciationBatchSequenceResourceIT {

    private static final Integer DEFAULT_START_INDEX = 1;
    private static final Integer UPDATED_START_INDEX = 2;

    private static final Integer DEFAULT_END_INDEX = 1;
    private static final Integer UPDATED_END_INDEX = 2;

    private static final DepreciationBatchStatusType DEFAULT_DEPRECIATION_BATCH_STATUS = DepreciationBatchStatusType.CREATED;
    private static final DepreciationBatchStatusType UPDATED_DEPRECIATION_BATCH_STATUS = DepreciationBatchStatusType.RUNNING;

    private static final UUID DEFAULT_DEPRECIATION_PERIOD_IDENTIFIER = UUID.randomUUID();
    private static final UUID UPDATED_DEPRECIATION_PERIOD_IDENTIFIER = UUID.randomUUID();

    private static final UUID DEFAULT_DEPRECIATION_JOB_IDENTIFIER = UUID.randomUUID();
    private static final UUID UPDATED_DEPRECIATION_JOB_IDENTIFIER = UUID.randomUUID();

    private static final UUID DEFAULT_FISCAL_MONTH_IDENTIFIER = UUID.randomUUID();
    private static final UUID UPDATED_FISCAL_MONTH_IDENTIFIER = UUID.randomUUID();

    private static final UUID DEFAULT_FISCAL_QUARTER_IDENTIFIER = UUID.randomUUID();
    private static final UUID UPDATED_FISCAL_QUARTER_IDENTIFIER = UUID.randomUUID();

    private static final String ENTITY_API_URL = "/api/depreciation-batch-sequences";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/depreciation-batch-sequences";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DepreciationBatchSequenceRepository depreciationBatchSequenceRepository;

    @Autowired
    private DepreciationBatchSequenceMapper depreciationBatchSequenceMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.DepreciationBatchSequenceSearchRepositoryMockConfiguration
     */
    @Autowired
    private DepreciationBatchSequenceSearchRepository mockDepreciationBatchSequenceSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDepreciationBatchSequenceMockMvc;

    private DepreciationBatchSequence depreciationBatchSequence;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DepreciationBatchSequence createEntity(EntityManager em) {
        DepreciationBatchSequence depreciationBatchSequence = new DepreciationBatchSequence()
            .startIndex(DEFAULT_START_INDEX)
            .endIndex(DEFAULT_END_INDEX)
            .depreciationBatchStatus(DEFAULT_DEPRECIATION_BATCH_STATUS)
            .depreciationPeriodIdentifier(DEFAULT_DEPRECIATION_PERIOD_IDENTIFIER)
            .depreciationJobIdentifier(DEFAULT_DEPRECIATION_JOB_IDENTIFIER)
            .fiscalMonthIdentifier(DEFAULT_FISCAL_MONTH_IDENTIFIER)
            .fiscalQuarterIdentifier(DEFAULT_FISCAL_QUARTER_IDENTIFIER);
        return depreciationBatchSequence;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DepreciationBatchSequence createUpdatedEntity(EntityManager em) {
        DepreciationBatchSequence depreciationBatchSequence = new DepreciationBatchSequence()
            .startIndex(UPDATED_START_INDEX)
            .endIndex(UPDATED_END_INDEX)
            .depreciationBatchStatus(UPDATED_DEPRECIATION_BATCH_STATUS)
            .depreciationPeriodIdentifier(UPDATED_DEPRECIATION_PERIOD_IDENTIFIER)
            .depreciationJobIdentifier(UPDATED_DEPRECIATION_JOB_IDENTIFIER)
            .fiscalMonthIdentifier(UPDATED_FISCAL_MONTH_IDENTIFIER)
            .fiscalQuarterIdentifier(UPDATED_FISCAL_QUARTER_IDENTIFIER);
        return depreciationBatchSequence;
    }

    @BeforeEach
    public void initTest() {
        depreciationBatchSequence = createEntity(em);
    }

    @Test
    @Transactional
    void createDepreciationBatchSequence() throws Exception {
        int databaseSizeBeforeCreate = depreciationBatchSequenceRepository.findAll().size();
        // Create the DepreciationBatchSequence
        DepreciationBatchSequenceDTO depreciationBatchSequenceDTO = depreciationBatchSequenceMapper.toDto(depreciationBatchSequence);
        restDepreciationBatchSequenceMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(depreciationBatchSequenceDTO))
            )
            .andExpect(status().isCreated());

        // Validate the DepreciationBatchSequence in the database
        List<DepreciationBatchSequence> depreciationBatchSequenceList = depreciationBatchSequenceRepository.findAll();
        assertThat(depreciationBatchSequenceList).hasSize(databaseSizeBeforeCreate + 1);
        DepreciationBatchSequence testDepreciationBatchSequence = depreciationBatchSequenceList.get(
            depreciationBatchSequenceList.size() - 1
        );
        assertThat(testDepreciationBatchSequence.getStartIndex()).isEqualTo(DEFAULT_START_INDEX);
        assertThat(testDepreciationBatchSequence.getEndIndex()).isEqualTo(DEFAULT_END_INDEX);
        assertThat(testDepreciationBatchSequence.getDepreciationBatchStatus()).isEqualTo(DEFAULT_DEPRECIATION_BATCH_STATUS);
        assertThat(testDepreciationBatchSequence.getDepreciationPeriodIdentifier()).isEqualTo(DEFAULT_DEPRECIATION_PERIOD_IDENTIFIER);
        assertThat(testDepreciationBatchSequence.getDepreciationJobIdentifier()).isEqualTo(DEFAULT_DEPRECIATION_JOB_IDENTIFIER);
        assertThat(testDepreciationBatchSequence.getFiscalMonthIdentifier()).isEqualTo(DEFAULT_FISCAL_MONTH_IDENTIFIER);
        assertThat(testDepreciationBatchSequence.getFiscalQuarterIdentifier()).isEqualTo(DEFAULT_FISCAL_QUARTER_IDENTIFIER);

        // Validate the DepreciationBatchSequence in Elasticsearch
        verify(mockDepreciationBatchSequenceSearchRepository, times(1)).save(testDepreciationBatchSequence);
    }

    @Test
    @Transactional
    void createDepreciationBatchSequenceWithExistingId() throws Exception {
        // Create the DepreciationBatchSequence with an existing ID
        depreciationBatchSequence.setId(1L);
        DepreciationBatchSequenceDTO depreciationBatchSequenceDTO = depreciationBatchSequenceMapper.toDto(depreciationBatchSequence);

        int databaseSizeBeforeCreate = depreciationBatchSequenceRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDepreciationBatchSequenceMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(depreciationBatchSequenceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DepreciationBatchSequence in the database
        List<DepreciationBatchSequence> depreciationBatchSequenceList = depreciationBatchSequenceRepository.findAll();
        assertThat(depreciationBatchSequenceList).hasSize(databaseSizeBeforeCreate);

        // Validate the DepreciationBatchSequence in Elasticsearch
        verify(mockDepreciationBatchSequenceSearchRepository, times(0)).save(depreciationBatchSequence);
    }

    @Test
    @Transactional
    void getAllDepreciationBatchSequences() throws Exception {
        // Initialize the database
        depreciationBatchSequenceRepository.saveAndFlush(depreciationBatchSequence);

        // Get all the depreciationBatchSequenceList
        restDepreciationBatchSequenceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(depreciationBatchSequence.getId().intValue())))
            .andExpect(jsonPath("$.[*].startIndex").value(hasItem(DEFAULT_START_INDEX)))
            .andExpect(jsonPath("$.[*].endIndex").value(hasItem(DEFAULT_END_INDEX)))
            .andExpect(jsonPath("$.[*].depreciationBatchStatus").value(hasItem(DEFAULT_DEPRECIATION_BATCH_STATUS.toString())))
            .andExpect(jsonPath("$.[*].depreciationPeriodIdentifier").value(hasItem(DEFAULT_DEPRECIATION_PERIOD_IDENTIFIER.toString())))
            .andExpect(jsonPath("$.[*].depreciationJobIdentifier").value(hasItem(DEFAULT_DEPRECIATION_JOB_IDENTIFIER.toString())))
            .andExpect(jsonPath("$.[*].fiscalMonthIdentifier").value(hasItem(DEFAULT_FISCAL_MONTH_IDENTIFIER.toString())))
            .andExpect(jsonPath("$.[*].fiscalQuarterIdentifier").value(hasItem(DEFAULT_FISCAL_QUARTER_IDENTIFIER.toString())));
    }

    @Test
    @Transactional
    void getDepreciationBatchSequence() throws Exception {
        // Initialize the database
        depreciationBatchSequenceRepository.saveAndFlush(depreciationBatchSequence);

        // Get the depreciationBatchSequence
        restDepreciationBatchSequenceMockMvc
            .perform(get(ENTITY_API_URL_ID, depreciationBatchSequence.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(depreciationBatchSequence.getId().intValue()))
            .andExpect(jsonPath("$.startIndex").value(DEFAULT_START_INDEX))
            .andExpect(jsonPath("$.endIndex").value(DEFAULT_END_INDEX))
            .andExpect(jsonPath("$.depreciationBatchStatus").value(DEFAULT_DEPRECIATION_BATCH_STATUS.toString()))
            .andExpect(jsonPath("$.depreciationPeriodIdentifier").value(DEFAULT_DEPRECIATION_PERIOD_IDENTIFIER.toString()))
            .andExpect(jsonPath("$.depreciationJobIdentifier").value(DEFAULT_DEPRECIATION_JOB_IDENTIFIER.toString()))
            .andExpect(jsonPath("$.fiscalMonthIdentifier").value(DEFAULT_FISCAL_MONTH_IDENTIFIER.toString()))
            .andExpect(jsonPath("$.fiscalQuarterIdentifier").value(DEFAULT_FISCAL_QUARTER_IDENTIFIER.toString()));
    }

    @Test
    @Transactional
    void getNonExistingDepreciationBatchSequence() throws Exception {
        // Get the depreciationBatchSequence
        restDepreciationBatchSequenceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewDepreciationBatchSequence() throws Exception {
        // Initialize the database
        depreciationBatchSequenceRepository.saveAndFlush(depreciationBatchSequence);

        int databaseSizeBeforeUpdate = depreciationBatchSequenceRepository.findAll().size();

        // Update the depreciationBatchSequence
        DepreciationBatchSequence updatedDepreciationBatchSequence = depreciationBatchSequenceRepository
            .findById(depreciationBatchSequence.getId())
            .get();
        // Disconnect from session so that the updates on updatedDepreciationBatchSequence are not directly saved in db
        em.detach(updatedDepreciationBatchSequence);
        updatedDepreciationBatchSequence
            .startIndex(UPDATED_START_INDEX)
            .endIndex(UPDATED_END_INDEX)
            .depreciationBatchStatus(UPDATED_DEPRECIATION_BATCH_STATUS)
            .depreciationPeriodIdentifier(UPDATED_DEPRECIATION_PERIOD_IDENTIFIER)
            .depreciationJobIdentifier(UPDATED_DEPRECIATION_JOB_IDENTIFIER)
            .fiscalMonthIdentifier(UPDATED_FISCAL_MONTH_IDENTIFIER)
            .fiscalQuarterIdentifier(UPDATED_FISCAL_QUARTER_IDENTIFIER);
        DepreciationBatchSequenceDTO depreciationBatchSequenceDTO = depreciationBatchSequenceMapper.toDto(updatedDepreciationBatchSequence);

        restDepreciationBatchSequenceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, depreciationBatchSequenceDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(depreciationBatchSequenceDTO))
            )
            .andExpect(status().isOk());

        // Validate the DepreciationBatchSequence in the database
        List<DepreciationBatchSequence> depreciationBatchSequenceList = depreciationBatchSequenceRepository.findAll();
        assertThat(depreciationBatchSequenceList).hasSize(databaseSizeBeforeUpdate);
        DepreciationBatchSequence testDepreciationBatchSequence = depreciationBatchSequenceList.get(
            depreciationBatchSequenceList.size() - 1
        );
        assertThat(testDepreciationBatchSequence.getStartIndex()).isEqualTo(UPDATED_START_INDEX);
        assertThat(testDepreciationBatchSequence.getEndIndex()).isEqualTo(UPDATED_END_INDEX);
        assertThat(testDepreciationBatchSequence.getDepreciationBatchStatus()).isEqualTo(UPDATED_DEPRECIATION_BATCH_STATUS);
        assertThat(testDepreciationBatchSequence.getDepreciationPeriodIdentifier()).isEqualTo(UPDATED_DEPRECIATION_PERIOD_IDENTIFIER);
        assertThat(testDepreciationBatchSequence.getDepreciationJobIdentifier()).isEqualTo(UPDATED_DEPRECIATION_JOB_IDENTIFIER);
        assertThat(testDepreciationBatchSequence.getFiscalMonthIdentifier()).isEqualTo(UPDATED_FISCAL_MONTH_IDENTIFIER);
        assertThat(testDepreciationBatchSequence.getFiscalQuarterIdentifier()).isEqualTo(UPDATED_FISCAL_QUARTER_IDENTIFIER);

        // Validate the DepreciationBatchSequence in Elasticsearch
        verify(mockDepreciationBatchSequenceSearchRepository).save(testDepreciationBatchSequence);
    }

    @Test
    @Transactional
    void putNonExistingDepreciationBatchSequence() throws Exception {
        int databaseSizeBeforeUpdate = depreciationBatchSequenceRepository.findAll().size();
        depreciationBatchSequence.setId(count.incrementAndGet());

        // Create the DepreciationBatchSequence
        DepreciationBatchSequenceDTO depreciationBatchSequenceDTO = depreciationBatchSequenceMapper.toDto(depreciationBatchSequence);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDepreciationBatchSequenceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, depreciationBatchSequenceDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(depreciationBatchSequenceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DepreciationBatchSequence in the database
        List<DepreciationBatchSequence> depreciationBatchSequenceList = depreciationBatchSequenceRepository.findAll();
        assertThat(depreciationBatchSequenceList).hasSize(databaseSizeBeforeUpdate);

        // Validate the DepreciationBatchSequence in Elasticsearch
        verify(mockDepreciationBatchSequenceSearchRepository, times(0)).save(depreciationBatchSequence);
    }

    @Test
    @Transactional
    void putWithIdMismatchDepreciationBatchSequence() throws Exception {
        int databaseSizeBeforeUpdate = depreciationBatchSequenceRepository.findAll().size();
        depreciationBatchSequence.setId(count.incrementAndGet());

        // Create the DepreciationBatchSequence
        DepreciationBatchSequenceDTO depreciationBatchSequenceDTO = depreciationBatchSequenceMapper.toDto(depreciationBatchSequence);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDepreciationBatchSequenceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(depreciationBatchSequenceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DepreciationBatchSequence in the database
        List<DepreciationBatchSequence> depreciationBatchSequenceList = depreciationBatchSequenceRepository.findAll();
        assertThat(depreciationBatchSequenceList).hasSize(databaseSizeBeforeUpdate);

        // Validate the DepreciationBatchSequence in Elasticsearch
        verify(mockDepreciationBatchSequenceSearchRepository, times(0)).save(depreciationBatchSequence);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDepreciationBatchSequence() throws Exception {
        int databaseSizeBeforeUpdate = depreciationBatchSequenceRepository.findAll().size();
        depreciationBatchSequence.setId(count.incrementAndGet());

        // Create the DepreciationBatchSequence
        DepreciationBatchSequenceDTO depreciationBatchSequenceDTO = depreciationBatchSequenceMapper.toDto(depreciationBatchSequence);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDepreciationBatchSequenceMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(depreciationBatchSequenceDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DepreciationBatchSequence in the database
        List<DepreciationBatchSequence> depreciationBatchSequenceList = depreciationBatchSequenceRepository.findAll();
        assertThat(depreciationBatchSequenceList).hasSize(databaseSizeBeforeUpdate);

        // Validate the DepreciationBatchSequence in Elasticsearch
        verify(mockDepreciationBatchSequenceSearchRepository, times(0)).save(depreciationBatchSequence);
    }

    @Test
    @Transactional
    void partialUpdateDepreciationBatchSequenceWithPatch() throws Exception {
        // Initialize the database
        depreciationBatchSequenceRepository.saveAndFlush(depreciationBatchSequence);

        int databaseSizeBeforeUpdate = depreciationBatchSequenceRepository.findAll().size();

        // Update the depreciationBatchSequence using partial update
        DepreciationBatchSequence partialUpdatedDepreciationBatchSequence = new DepreciationBatchSequence();
        partialUpdatedDepreciationBatchSequence.setId(depreciationBatchSequence.getId());

        partialUpdatedDepreciationBatchSequence
            .depreciationPeriodIdentifier(UPDATED_DEPRECIATION_PERIOD_IDENTIFIER)
            .fiscalMonthIdentifier(UPDATED_FISCAL_MONTH_IDENTIFIER);

        restDepreciationBatchSequenceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDepreciationBatchSequence.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDepreciationBatchSequence))
            )
            .andExpect(status().isOk());

        // Validate the DepreciationBatchSequence in the database
        List<DepreciationBatchSequence> depreciationBatchSequenceList = depreciationBatchSequenceRepository.findAll();
        assertThat(depreciationBatchSequenceList).hasSize(databaseSizeBeforeUpdate);
        DepreciationBatchSequence testDepreciationBatchSequence = depreciationBatchSequenceList.get(
            depreciationBatchSequenceList.size() - 1
        );
        assertThat(testDepreciationBatchSequence.getStartIndex()).isEqualTo(DEFAULT_START_INDEX);
        assertThat(testDepreciationBatchSequence.getEndIndex()).isEqualTo(DEFAULT_END_INDEX);
        assertThat(testDepreciationBatchSequence.getDepreciationBatchStatus()).isEqualTo(DEFAULT_DEPRECIATION_BATCH_STATUS);
        assertThat(testDepreciationBatchSequence.getDepreciationPeriodIdentifier()).isEqualTo(UPDATED_DEPRECIATION_PERIOD_IDENTIFIER);
        assertThat(testDepreciationBatchSequence.getDepreciationJobIdentifier()).isEqualTo(DEFAULT_DEPRECIATION_JOB_IDENTIFIER);
        assertThat(testDepreciationBatchSequence.getFiscalMonthIdentifier()).isEqualTo(UPDATED_FISCAL_MONTH_IDENTIFIER);
        assertThat(testDepreciationBatchSequence.getFiscalQuarterIdentifier()).isEqualTo(DEFAULT_FISCAL_QUARTER_IDENTIFIER);
    }

    @Test
    @Transactional
    void fullUpdateDepreciationBatchSequenceWithPatch() throws Exception {
        // Initialize the database
        depreciationBatchSequenceRepository.saveAndFlush(depreciationBatchSequence);

        int databaseSizeBeforeUpdate = depreciationBatchSequenceRepository.findAll().size();

        // Update the depreciationBatchSequence using partial update
        DepreciationBatchSequence partialUpdatedDepreciationBatchSequence = new DepreciationBatchSequence();
        partialUpdatedDepreciationBatchSequence.setId(depreciationBatchSequence.getId());

        partialUpdatedDepreciationBatchSequence
            .startIndex(UPDATED_START_INDEX)
            .endIndex(UPDATED_END_INDEX)
            .depreciationBatchStatus(UPDATED_DEPRECIATION_BATCH_STATUS)
            .depreciationPeriodIdentifier(UPDATED_DEPRECIATION_PERIOD_IDENTIFIER)
            .depreciationJobIdentifier(UPDATED_DEPRECIATION_JOB_IDENTIFIER)
            .fiscalMonthIdentifier(UPDATED_FISCAL_MONTH_IDENTIFIER)
            .fiscalQuarterIdentifier(UPDATED_FISCAL_QUARTER_IDENTIFIER);

        restDepreciationBatchSequenceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDepreciationBatchSequence.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDepreciationBatchSequence))
            )
            .andExpect(status().isOk());

        // Validate the DepreciationBatchSequence in the database
        List<DepreciationBatchSequence> depreciationBatchSequenceList = depreciationBatchSequenceRepository.findAll();
        assertThat(depreciationBatchSequenceList).hasSize(databaseSizeBeforeUpdate);
        DepreciationBatchSequence testDepreciationBatchSequence = depreciationBatchSequenceList.get(
            depreciationBatchSequenceList.size() - 1
        );
        assertThat(testDepreciationBatchSequence.getStartIndex()).isEqualTo(UPDATED_START_INDEX);
        assertThat(testDepreciationBatchSequence.getEndIndex()).isEqualTo(UPDATED_END_INDEX);
        assertThat(testDepreciationBatchSequence.getDepreciationBatchStatus()).isEqualTo(UPDATED_DEPRECIATION_BATCH_STATUS);
        assertThat(testDepreciationBatchSequence.getDepreciationPeriodIdentifier()).isEqualTo(UPDATED_DEPRECIATION_PERIOD_IDENTIFIER);
        assertThat(testDepreciationBatchSequence.getDepreciationJobIdentifier()).isEqualTo(UPDATED_DEPRECIATION_JOB_IDENTIFIER);
        assertThat(testDepreciationBatchSequence.getFiscalMonthIdentifier()).isEqualTo(UPDATED_FISCAL_MONTH_IDENTIFIER);
        assertThat(testDepreciationBatchSequence.getFiscalQuarterIdentifier()).isEqualTo(UPDATED_FISCAL_QUARTER_IDENTIFIER);
    }

    @Test
    @Transactional
    void patchNonExistingDepreciationBatchSequence() throws Exception {
        int databaseSizeBeforeUpdate = depreciationBatchSequenceRepository.findAll().size();
        depreciationBatchSequence.setId(count.incrementAndGet());

        // Create the DepreciationBatchSequence
        DepreciationBatchSequenceDTO depreciationBatchSequenceDTO = depreciationBatchSequenceMapper.toDto(depreciationBatchSequence);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDepreciationBatchSequenceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, depreciationBatchSequenceDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(depreciationBatchSequenceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DepreciationBatchSequence in the database
        List<DepreciationBatchSequence> depreciationBatchSequenceList = depreciationBatchSequenceRepository.findAll();
        assertThat(depreciationBatchSequenceList).hasSize(databaseSizeBeforeUpdate);

        // Validate the DepreciationBatchSequence in Elasticsearch
        verify(mockDepreciationBatchSequenceSearchRepository, times(0)).save(depreciationBatchSequence);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDepreciationBatchSequence() throws Exception {
        int databaseSizeBeforeUpdate = depreciationBatchSequenceRepository.findAll().size();
        depreciationBatchSequence.setId(count.incrementAndGet());

        // Create the DepreciationBatchSequence
        DepreciationBatchSequenceDTO depreciationBatchSequenceDTO = depreciationBatchSequenceMapper.toDto(depreciationBatchSequence);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDepreciationBatchSequenceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(depreciationBatchSequenceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DepreciationBatchSequence in the database
        List<DepreciationBatchSequence> depreciationBatchSequenceList = depreciationBatchSequenceRepository.findAll();
        assertThat(depreciationBatchSequenceList).hasSize(databaseSizeBeforeUpdate);

        // Validate the DepreciationBatchSequence in Elasticsearch
        verify(mockDepreciationBatchSequenceSearchRepository, times(0)).save(depreciationBatchSequence);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDepreciationBatchSequence() throws Exception {
        int databaseSizeBeforeUpdate = depreciationBatchSequenceRepository.findAll().size();
        depreciationBatchSequence.setId(count.incrementAndGet());

        // Create the DepreciationBatchSequence
        DepreciationBatchSequenceDTO depreciationBatchSequenceDTO = depreciationBatchSequenceMapper.toDto(depreciationBatchSequence);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDepreciationBatchSequenceMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(depreciationBatchSequenceDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DepreciationBatchSequence in the database
        List<DepreciationBatchSequence> depreciationBatchSequenceList = depreciationBatchSequenceRepository.findAll();
        assertThat(depreciationBatchSequenceList).hasSize(databaseSizeBeforeUpdate);

        // Validate the DepreciationBatchSequence in Elasticsearch
        verify(mockDepreciationBatchSequenceSearchRepository, times(0)).save(depreciationBatchSequence);
    }

    @Test
    @Transactional
    void deleteDepreciationBatchSequence() throws Exception {
        // Initialize the database
        depreciationBatchSequenceRepository.saveAndFlush(depreciationBatchSequence);

        int databaseSizeBeforeDelete = depreciationBatchSequenceRepository.findAll().size();

        // Delete the depreciationBatchSequence
        restDepreciationBatchSequenceMockMvc
            .perform(delete(ENTITY_API_URL_ID, depreciationBatchSequence.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<DepreciationBatchSequence> depreciationBatchSequenceList = depreciationBatchSequenceRepository.findAll();
        assertThat(depreciationBatchSequenceList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the DepreciationBatchSequence in Elasticsearch
        verify(mockDepreciationBatchSequenceSearchRepository, times(1)).deleteById(depreciationBatchSequence.getId());
    }

    @Test
    @Transactional
    void searchDepreciationBatchSequence() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        depreciationBatchSequenceRepository.saveAndFlush(depreciationBatchSequence);
        when(mockDepreciationBatchSequenceSearchRepository.search("id:" + depreciationBatchSequence.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(depreciationBatchSequence), PageRequest.of(0, 1), 1));

        // Search the depreciationBatchSequence
        restDepreciationBatchSequenceMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + depreciationBatchSequence.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(depreciationBatchSequence.getId().intValue())))
            .andExpect(jsonPath("$.[*].startIndex").value(hasItem(DEFAULT_START_INDEX)))
            .andExpect(jsonPath("$.[*].endIndex").value(hasItem(DEFAULT_END_INDEX)))
            .andExpect(jsonPath("$.[*].depreciationBatchStatus").value(hasItem(DEFAULT_DEPRECIATION_BATCH_STATUS.toString())))
            .andExpect(jsonPath("$.[*].depreciationPeriodIdentifier").value(hasItem(DEFAULT_DEPRECIATION_PERIOD_IDENTIFIER.toString())))
            .andExpect(jsonPath("$.[*].depreciationJobIdentifier").value(hasItem(DEFAULT_DEPRECIATION_JOB_IDENTIFIER.toString())))
            .andExpect(jsonPath("$.[*].fiscalMonthIdentifier").value(hasItem(DEFAULT_FISCAL_MONTH_IDENTIFIER.toString())))
            .andExpect(jsonPath("$.[*].fiscalQuarterIdentifier").value(hasItem(DEFAULT_FISCAL_QUARTER_IDENTIFIER.toString())));
    }
}
