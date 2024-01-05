package io.github.erp.erp.resources;

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
import io.github.erp.IntegrationTest;
import io.github.erp.domain.DepreciationEntry;
import io.github.erp.repository.DepreciationEntryRepository;
import io.github.erp.repository.search.DepreciationEntrySearchRepository;
import io.github.erp.service.dto.DepreciationEntryDTO;
import io.github.erp.service.mapper.DepreciationEntryMapper;
import io.github.erp.web.rest.DepreciationEntryResource;
import io.github.erp.web.rest.TestUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

import static io.github.erp.web.rest.TestUtil.sameInstant;
import static io.github.erp.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the DepreciationEntryResource REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser(roles = {"FIXED_ASSETS_USER"})
class DepreciationEntryResourceIT {

    private static final ZonedDateTime DEFAULT_POSTED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_POSTED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final BigDecimal DEFAULT_DEPRECIATION_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_DEPRECIATION_AMOUNT = new BigDecimal(2);

    private static final Long DEFAULT_ASSET_NUMBER = 1L;
    private static final Long UPDATED_ASSET_NUMBER = 2L;

    private static final UUID DEFAULT_DEPRECIATION_PERIOD_IDENTIFIER = UUID.randomUUID();
    private static final UUID UPDATED_DEPRECIATION_PERIOD_IDENTIFIER = UUID.randomUUID();

    private static final UUID DEFAULT_DEPRECIATION_JOB_IDENTIFIER = UUID.randomUUID();
    private static final UUID UPDATED_DEPRECIATION_JOB_IDENTIFIER = UUID.randomUUID();

    private static final UUID DEFAULT_FISCAL_MONTH_IDENTIFIER = UUID.randomUUID();
    private static final UUID UPDATED_FISCAL_MONTH_IDENTIFIER = UUID.randomUUID();

    private static final UUID DEFAULT_FISCAL_QUARTER_IDENTIFIER = UUID.randomUUID();
    private static final UUID UPDATED_FISCAL_QUARTER_IDENTIFIER = UUID.randomUUID();

    private static final String ENTITY_API_URL = "/api/fixed-asset/depreciation-entries";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/fixed-asset/_search/depreciation-entries";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DepreciationEntryRepository depreciationEntryRepository;

    @Autowired
    private DepreciationEntryMapper depreciationEntryMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.DepreciationEntrySearchRepositoryMockConfiguration
     */
    @Autowired
    private DepreciationEntrySearchRepository mockDepreciationEntrySearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDepreciationEntryMockMvc;

    private DepreciationEntry depreciationEntry;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DepreciationEntry createEntity(EntityManager em) {
        DepreciationEntry depreciationEntry = new DepreciationEntry()
            .postedAt(DEFAULT_POSTED_AT)
            .depreciationAmount(DEFAULT_DEPRECIATION_AMOUNT)
            .assetNumber(DEFAULT_ASSET_NUMBER)
            .depreciationPeriodIdentifier(DEFAULT_DEPRECIATION_PERIOD_IDENTIFIER)
            .depreciationJobIdentifier(DEFAULT_DEPRECIATION_JOB_IDENTIFIER)
            .fiscalMonthIdentifier(DEFAULT_FISCAL_MONTH_IDENTIFIER)
            .fiscalQuarterIdentifier(DEFAULT_FISCAL_QUARTER_IDENTIFIER);
        return depreciationEntry;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DepreciationEntry createUpdatedEntity(EntityManager em) {
        DepreciationEntry depreciationEntry = new DepreciationEntry()
            .postedAt(UPDATED_POSTED_AT)
            .depreciationAmount(UPDATED_DEPRECIATION_AMOUNT)
            .assetNumber(UPDATED_ASSET_NUMBER)
            .depreciationPeriodIdentifier(UPDATED_DEPRECIATION_PERIOD_IDENTIFIER)
            .depreciationJobIdentifier(UPDATED_DEPRECIATION_JOB_IDENTIFIER)
            .fiscalMonthIdentifier(UPDATED_FISCAL_MONTH_IDENTIFIER)
            .fiscalQuarterIdentifier(UPDATED_FISCAL_QUARTER_IDENTIFIER);
        return depreciationEntry;
    }

    @BeforeEach
    public void initTest() {
        depreciationEntry = createEntity(em);
    }

    @Test
    @Transactional
    void createDepreciationEntry() throws Exception {
        int databaseSizeBeforeCreate = depreciationEntryRepository.findAll().size();
        // Create the DepreciationEntry
        DepreciationEntryDTO depreciationEntryDTO = depreciationEntryMapper.toDto(depreciationEntry);
        restDepreciationEntryMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(depreciationEntryDTO))
            )
            .andExpect(status().isCreated());

        // Validate the DepreciationEntry in the database
        List<DepreciationEntry> depreciationEntryList = depreciationEntryRepository.findAll();
        assertThat(depreciationEntryList).hasSize(databaseSizeBeforeCreate + 1);
        DepreciationEntry testDepreciationEntry = depreciationEntryList.get(depreciationEntryList.size() - 1);
        assertThat(testDepreciationEntry.getPostedAt()).isEqualTo(DEFAULT_POSTED_AT);
        assertThat(testDepreciationEntry.getDepreciationAmount()).isEqualByComparingTo(DEFAULT_DEPRECIATION_AMOUNT);
        assertThat(testDepreciationEntry.getAssetNumber()).isEqualTo(DEFAULT_ASSET_NUMBER);
        assertThat(testDepreciationEntry.getDepreciationPeriodIdentifier()).isEqualTo(DEFAULT_DEPRECIATION_PERIOD_IDENTIFIER);
        assertThat(testDepreciationEntry.getDepreciationJobIdentifier()).isEqualTo(DEFAULT_DEPRECIATION_JOB_IDENTIFIER);
        assertThat(testDepreciationEntry.getFiscalMonthIdentifier()).isEqualTo(DEFAULT_FISCAL_MONTH_IDENTIFIER);
        assertThat(testDepreciationEntry.getFiscalQuarterIdentifier()).isEqualTo(DEFAULT_FISCAL_QUARTER_IDENTIFIER);

        // Validate the DepreciationEntry in Elasticsearch
        verify(mockDepreciationEntrySearchRepository, times(1)).save(testDepreciationEntry);
    }

    @Test
    @Transactional
    void createDepreciationEntryWithExistingId() throws Exception {
        // Create the DepreciationEntry with an existing ID
        depreciationEntry.setId(1L);
        DepreciationEntryDTO depreciationEntryDTO = depreciationEntryMapper.toDto(depreciationEntry);

        int databaseSizeBeforeCreate = depreciationEntryRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDepreciationEntryMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(depreciationEntryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DepreciationEntry in the database
        List<DepreciationEntry> depreciationEntryList = depreciationEntryRepository.findAll();
        assertThat(depreciationEntryList).hasSize(databaseSizeBeforeCreate);

        // Validate the DepreciationEntry in Elasticsearch
        verify(mockDepreciationEntrySearchRepository, times(0)).save(depreciationEntry);
    }

    @Test
    @Transactional
    void getAllDepreciationEntries() throws Exception {
        // Initialize the database
        depreciationEntryRepository.saveAndFlush(depreciationEntry);

        // Get all the depreciationEntryList
        restDepreciationEntryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(depreciationEntry.getId().intValue())))
            .andExpect(jsonPath("$.[*].postedAt").value(hasItem(sameInstant(DEFAULT_POSTED_AT))))
            .andExpect(jsonPath("$.[*].depreciationAmount").value(hasItem(sameNumber(DEFAULT_DEPRECIATION_AMOUNT))))
            .andExpect(jsonPath("$.[*].assetNumber").value(hasItem(DEFAULT_ASSET_NUMBER.intValue())))
            .andExpect(jsonPath("$.[*].depreciationPeriodIdentifier").value(hasItem(DEFAULT_DEPRECIATION_PERIOD_IDENTIFIER.toString())))
            .andExpect(jsonPath("$.[*].depreciationJobIdentifier").value(hasItem(DEFAULT_DEPRECIATION_JOB_IDENTIFIER.toString())))
            .andExpect(jsonPath("$.[*].fiscalMonthIdentifier").value(hasItem(DEFAULT_FISCAL_MONTH_IDENTIFIER.toString())))
            .andExpect(jsonPath("$.[*].fiscalQuarterIdentifier").value(hasItem(DEFAULT_FISCAL_QUARTER_IDENTIFIER.toString())));
    }

    @Test
    @Transactional
    void getDepreciationEntry() throws Exception {
        // Initialize the database
        depreciationEntryRepository.saveAndFlush(depreciationEntry);

        // Get the depreciationEntry
        restDepreciationEntryMockMvc
            .perform(get(ENTITY_API_URL_ID, depreciationEntry.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(depreciationEntry.getId().intValue()))
            .andExpect(jsonPath("$.postedAt").value(sameInstant(DEFAULT_POSTED_AT)))
            .andExpect(jsonPath("$.depreciationAmount").value(sameNumber(DEFAULT_DEPRECIATION_AMOUNT)))
            .andExpect(jsonPath("$.assetNumber").value(DEFAULT_ASSET_NUMBER.intValue()))
            .andExpect(jsonPath("$.depreciationPeriodIdentifier").value(DEFAULT_DEPRECIATION_PERIOD_IDENTIFIER.toString()))
            .andExpect(jsonPath("$.depreciationJobIdentifier").value(DEFAULT_DEPRECIATION_JOB_IDENTIFIER.toString()))
            .andExpect(jsonPath("$.fiscalMonthIdentifier").value(DEFAULT_FISCAL_MONTH_IDENTIFIER.toString()))
            .andExpect(jsonPath("$.fiscalQuarterIdentifier").value(DEFAULT_FISCAL_QUARTER_IDENTIFIER.toString()));
    }

    @Test
    @Transactional
    void getNonExistingDepreciationEntry() throws Exception {
        // Get the depreciationEntry
        restDepreciationEntryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewDepreciationEntry() throws Exception {
        // Initialize the database
        depreciationEntryRepository.saveAndFlush(depreciationEntry);

        int databaseSizeBeforeUpdate = depreciationEntryRepository.findAll().size();

        // Update the depreciationEntry
        DepreciationEntry updatedDepreciationEntry = depreciationEntryRepository.findById(depreciationEntry.getId()).get();
        // Disconnect from session so that the updates on updatedDepreciationEntry are not directly saved in db
        em.detach(updatedDepreciationEntry);
        updatedDepreciationEntry
            .postedAt(UPDATED_POSTED_AT)
            .depreciationAmount(UPDATED_DEPRECIATION_AMOUNT)
            .assetNumber(UPDATED_ASSET_NUMBER)
            .depreciationPeriodIdentifier(UPDATED_DEPRECIATION_PERIOD_IDENTIFIER)
            .depreciationJobIdentifier(UPDATED_DEPRECIATION_JOB_IDENTIFIER)
            .fiscalMonthIdentifier(UPDATED_FISCAL_MONTH_IDENTIFIER)
            .fiscalQuarterIdentifier(UPDATED_FISCAL_QUARTER_IDENTIFIER);
        DepreciationEntryDTO depreciationEntryDTO = depreciationEntryMapper.toDto(updatedDepreciationEntry);

        restDepreciationEntryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, depreciationEntryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(depreciationEntryDTO))
            )
            .andExpect(status().isOk());

        // Validate the DepreciationEntry in the database
        List<DepreciationEntry> depreciationEntryList = depreciationEntryRepository.findAll();
        assertThat(depreciationEntryList).hasSize(databaseSizeBeforeUpdate);
        DepreciationEntry testDepreciationEntry = depreciationEntryList.get(depreciationEntryList.size() - 1);
        assertThat(testDepreciationEntry.getPostedAt()).isEqualTo(UPDATED_POSTED_AT);
        assertThat(testDepreciationEntry.getDepreciationAmount()).isEqualTo(UPDATED_DEPRECIATION_AMOUNT);
        assertThat(testDepreciationEntry.getAssetNumber()).isEqualTo(UPDATED_ASSET_NUMBER);
        assertThat(testDepreciationEntry.getDepreciationPeriodIdentifier()).isEqualTo(UPDATED_DEPRECIATION_PERIOD_IDENTIFIER);
        assertThat(testDepreciationEntry.getDepreciationJobIdentifier()).isEqualTo(UPDATED_DEPRECIATION_JOB_IDENTIFIER);
        assertThat(testDepreciationEntry.getFiscalMonthIdentifier()).isEqualTo(UPDATED_FISCAL_MONTH_IDENTIFIER);
        assertThat(testDepreciationEntry.getFiscalQuarterIdentifier()).isEqualTo(UPDATED_FISCAL_QUARTER_IDENTIFIER);

        // Validate the DepreciationEntry in Elasticsearch
        verify(mockDepreciationEntrySearchRepository).save(testDepreciationEntry);
    }

    @Test
    @Transactional
    void putNonExistingDepreciationEntry() throws Exception {
        int databaseSizeBeforeUpdate = depreciationEntryRepository.findAll().size();
        depreciationEntry.setId(count.incrementAndGet());

        // Create the DepreciationEntry
        DepreciationEntryDTO depreciationEntryDTO = depreciationEntryMapper.toDto(depreciationEntry);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDepreciationEntryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, depreciationEntryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(depreciationEntryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DepreciationEntry in the database
        List<DepreciationEntry> depreciationEntryList = depreciationEntryRepository.findAll();
        assertThat(depreciationEntryList).hasSize(databaseSizeBeforeUpdate);

        // Validate the DepreciationEntry in Elasticsearch
        verify(mockDepreciationEntrySearchRepository, times(0)).save(depreciationEntry);
    }

    @Test
    @Transactional
    void putWithIdMismatchDepreciationEntry() throws Exception {
        int databaseSizeBeforeUpdate = depreciationEntryRepository.findAll().size();
        depreciationEntry.setId(count.incrementAndGet());

        // Create the DepreciationEntry
        DepreciationEntryDTO depreciationEntryDTO = depreciationEntryMapper.toDto(depreciationEntry);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDepreciationEntryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(depreciationEntryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DepreciationEntry in the database
        List<DepreciationEntry> depreciationEntryList = depreciationEntryRepository.findAll();
        assertThat(depreciationEntryList).hasSize(databaseSizeBeforeUpdate);

        // Validate the DepreciationEntry in Elasticsearch
        verify(mockDepreciationEntrySearchRepository, times(0)).save(depreciationEntry);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDepreciationEntry() throws Exception {
        int databaseSizeBeforeUpdate = depreciationEntryRepository.findAll().size();
        depreciationEntry.setId(count.incrementAndGet());

        // Create the DepreciationEntry
        DepreciationEntryDTO depreciationEntryDTO = depreciationEntryMapper.toDto(depreciationEntry);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDepreciationEntryMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(depreciationEntryDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DepreciationEntry in the database
        List<DepreciationEntry> depreciationEntryList = depreciationEntryRepository.findAll();
        assertThat(depreciationEntryList).hasSize(databaseSizeBeforeUpdate);

        // Validate the DepreciationEntry in Elasticsearch
        verify(mockDepreciationEntrySearchRepository, times(0)).save(depreciationEntry);
    }

    @Test
    @Transactional
    void partialUpdateDepreciationEntryWithPatch() throws Exception {
        // Initialize the database
        depreciationEntryRepository.saveAndFlush(depreciationEntry);

        int databaseSizeBeforeUpdate = depreciationEntryRepository.findAll().size();

        // Update the depreciationEntry using partial update
        DepreciationEntry partialUpdatedDepreciationEntry = new DepreciationEntry();
        partialUpdatedDepreciationEntry.setId(depreciationEntry.getId());

        partialUpdatedDepreciationEntry
            .postedAt(UPDATED_POSTED_AT)
            .depreciationPeriodIdentifier(UPDATED_DEPRECIATION_PERIOD_IDENTIFIER)
            .depreciationJobIdentifier(UPDATED_DEPRECIATION_JOB_IDENTIFIER)
            .fiscalMonthIdentifier(UPDATED_FISCAL_MONTH_IDENTIFIER);

        restDepreciationEntryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDepreciationEntry.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDepreciationEntry))
            )
            .andExpect(status().isOk());

        // Validate the DepreciationEntry in the database
        List<DepreciationEntry> depreciationEntryList = depreciationEntryRepository.findAll();
        assertThat(depreciationEntryList).hasSize(databaseSizeBeforeUpdate);
        DepreciationEntry testDepreciationEntry = depreciationEntryList.get(depreciationEntryList.size() - 1);
        assertThat(testDepreciationEntry.getPostedAt()).isEqualTo(UPDATED_POSTED_AT);
        assertThat(testDepreciationEntry.getDepreciationAmount()).isEqualByComparingTo(DEFAULT_DEPRECIATION_AMOUNT);
        assertThat(testDepreciationEntry.getAssetNumber()).isEqualTo(DEFAULT_ASSET_NUMBER);
        assertThat(testDepreciationEntry.getDepreciationPeriodIdentifier()).isEqualTo(UPDATED_DEPRECIATION_PERIOD_IDENTIFIER);
        assertThat(testDepreciationEntry.getDepreciationJobIdentifier()).isEqualTo(UPDATED_DEPRECIATION_JOB_IDENTIFIER);
        assertThat(testDepreciationEntry.getFiscalMonthIdentifier()).isEqualTo(UPDATED_FISCAL_MONTH_IDENTIFIER);
        assertThat(testDepreciationEntry.getFiscalQuarterIdentifier()).isEqualTo(DEFAULT_FISCAL_QUARTER_IDENTIFIER);
    }

    @Test
    @Transactional
    void fullUpdateDepreciationEntryWithPatch() throws Exception {
        // Initialize the database
        depreciationEntryRepository.saveAndFlush(depreciationEntry);

        int databaseSizeBeforeUpdate = depreciationEntryRepository.findAll().size();

        // Update the depreciationEntry using partial update
        DepreciationEntry partialUpdatedDepreciationEntry = new DepreciationEntry();
        partialUpdatedDepreciationEntry.setId(depreciationEntry.getId());

        partialUpdatedDepreciationEntry
            .postedAt(UPDATED_POSTED_AT)
            .depreciationAmount(UPDATED_DEPRECIATION_AMOUNT)
            .assetNumber(UPDATED_ASSET_NUMBER)
            .depreciationPeriodIdentifier(UPDATED_DEPRECIATION_PERIOD_IDENTIFIER)
            .depreciationJobIdentifier(UPDATED_DEPRECIATION_JOB_IDENTIFIER)
            .fiscalMonthIdentifier(UPDATED_FISCAL_MONTH_IDENTIFIER)
            .fiscalQuarterIdentifier(UPDATED_FISCAL_QUARTER_IDENTIFIER);

        restDepreciationEntryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDepreciationEntry.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDepreciationEntry))
            )
            .andExpect(status().isOk());

        // Validate the DepreciationEntry in the database
        List<DepreciationEntry> depreciationEntryList = depreciationEntryRepository.findAll();
        assertThat(depreciationEntryList).hasSize(databaseSizeBeforeUpdate);
        DepreciationEntry testDepreciationEntry = depreciationEntryList.get(depreciationEntryList.size() - 1);
        assertThat(testDepreciationEntry.getPostedAt()).isEqualTo(UPDATED_POSTED_AT);
        assertThat(testDepreciationEntry.getDepreciationAmount()).isEqualByComparingTo(UPDATED_DEPRECIATION_AMOUNT);
        assertThat(testDepreciationEntry.getAssetNumber()).isEqualTo(UPDATED_ASSET_NUMBER);
        assertThat(testDepreciationEntry.getDepreciationPeriodIdentifier()).isEqualTo(UPDATED_DEPRECIATION_PERIOD_IDENTIFIER);
        assertThat(testDepreciationEntry.getDepreciationJobIdentifier()).isEqualTo(UPDATED_DEPRECIATION_JOB_IDENTIFIER);
        assertThat(testDepreciationEntry.getFiscalMonthIdentifier()).isEqualTo(UPDATED_FISCAL_MONTH_IDENTIFIER);
        assertThat(testDepreciationEntry.getFiscalQuarterIdentifier()).isEqualTo(UPDATED_FISCAL_QUARTER_IDENTIFIER);
    }

    @Test
    @Transactional
    void patchNonExistingDepreciationEntry() throws Exception {
        int databaseSizeBeforeUpdate = depreciationEntryRepository.findAll().size();
        depreciationEntry.setId(count.incrementAndGet());

        // Create the DepreciationEntry
        DepreciationEntryDTO depreciationEntryDTO = depreciationEntryMapper.toDto(depreciationEntry);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDepreciationEntryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, depreciationEntryDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(depreciationEntryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DepreciationEntry in the database
        List<DepreciationEntry> depreciationEntryList = depreciationEntryRepository.findAll();
        assertThat(depreciationEntryList).hasSize(databaseSizeBeforeUpdate);

        // Validate the DepreciationEntry in Elasticsearch
        verify(mockDepreciationEntrySearchRepository, times(0)).save(depreciationEntry);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDepreciationEntry() throws Exception {
        int databaseSizeBeforeUpdate = depreciationEntryRepository.findAll().size();
        depreciationEntry.setId(count.incrementAndGet());

        // Create the DepreciationEntry
        DepreciationEntryDTO depreciationEntryDTO = depreciationEntryMapper.toDto(depreciationEntry);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDepreciationEntryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(depreciationEntryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DepreciationEntry in the database
        List<DepreciationEntry> depreciationEntryList = depreciationEntryRepository.findAll();
        assertThat(depreciationEntryList).hasSize(databaseSizeBeforeUpdate);

        // Validate the DepreciationEntry in Elasticsearch
        verify(mockDepreciationEntrySearchRepository, times(0)).save(depreciationEntry);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDepreciationEntry() throws Exception {
        int databaseSizeBeforeUpdate = depreciationEntryRepository.findAll().size();
        depreciationEntry.setId(count.incrementAndGet());

        // Create the DepreciationEntry
        DepreciationEntryDTO depreciationEntryDTO = depreciationEntryMapper.toDto(depreciationEntry);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDepreciationEntryMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(depreciationEntryDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DepreciationEntry in the database
        List<DepreciationEntry> depreciationEntryList = depreciationEntryRepository.findAll();
        assertThat(depreciationEntryList).hasSize(databaseSizeBeforeUpdate);

        // Validate the DepreciationEntry in Elasticsearch
        verify(mockDepreciationEntrySearchRepository, times(0)).save(depreciationEntry);
    }

    @Test
    @Transactional
    void deleteDepreciationEntry() throws Exception {
        // Initialize the database
        depreciationEntryRepository.saveAndFlush(depreciationEntry);

        int databaseSizeBeforeDelete = depreciationEntryRepository.findAll().size();

        // Delete the depreciationEntry
        restDepreciationEntryMockMvc
            .perform(delete(ENTITY_API_URL_ID, depreciationEntry.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<DepreciationEntry> depreciationEntryList = depreciationEntryRepository.findAll();
        assertThat(depreciationEntryList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the DepreciationEntry in Elasticsearch
        verify(mockDepreciationEntrySearchRepository, times(1)).deleteById(depreciationEntry.getId());
    }

    @Test
    @Transactional
    void searchDepreciationEntry() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        depreciationEntryRepository.saveAndFlush(depreciationEntry);
        when(mockDepreciationEntrySearchRepository.search("id:" + depreciationEntry.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(depreciationEntry), PageRequest.of(0, 1), 1));

        // Search the depreciationEntry
        restDepreciationEntryMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + depreciationEntry.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(depreciationEntry.getId().intValue())))
            .andExpect(jsonPath("$.[*].postedAt").value(hasItem(sameInstant(DEFAULT_POSTED_AT))))
            .andExpect(jsonPath("$.[*].depreciationAmount").value(hasItem(sameNumber(DEFAULT_DEPRECIATION_AMOUNT))))
            .andExpect(jsonPath("$.[*].assetNumber").value(hasItem(DEFAULT_ASSET_NUMBER.intValue())))
            .andExpect(jsonPath("$.[*].depreciationPeriodIdentifier").value(hasItem(DEFAULT_DEPRECIATION_PERIOD_IDENTIFIER.toString())))
            .andExpect(jsonPath("$.[*].depreciationJobIdentifier").value(hasItem(DEFAULT_DEPRECIATION_JOB_IDENTIFIER.toString())))
            .andExpect(jsonPath("$.[*].fiscalMonthIdentifier").value(hasItem(DEFAULT_FISCAL_MONTH_IDENTIFIER.toString())))
            .andExpect(jsonPath("$.[*].fiscalQuarterIdentifier").value(hasItem(DEFAULT_FISCAL_QUARTER_IDENTIFIER.toString())));
    }
}
