package io.github.erp.erp.resources;

/*-
 * Erp System - Mark X No 7 (Jehoiada Series) Server ver 1.7.8
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
import io.github.erp.IntegrationTest;
import io.github.erp.domain.FiscalYear;
import io.github.erp.domain.enumeration.FiscalYearStatusType;
import io.github.erp.repository.FiscalYearRepository;
import io.github.erp.repository.search.FiscalYearSearchRepository;
import io.github.erp.service.FiscalYearService;
import io.github.erp.service.dto.FiscalYearDTO;
import io.github.erp.service.mapper.FiscalYearMapper;
import io.github.erp.web.rest.FiscalYearResource;
import io.github.erp.web.rest.TestUtil;
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

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link FiscalYearResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class FiscalYearResourceIT {

    private static final String DEFAULT_FISCAL_YEAR_CODE = "AAAAAAAAAA";
    private static final String UPDATED_FISCAL_YEAR_CODE = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final FiscalYearStatusType DEFAULT_FISCAL_YEAR_STATUS = FiscalYearStatusType.OPEN;
    private static final FiscalYearStatusType UPDATED_FISCAL_YEAR_STATUS = FiscalYearStatusType.CLOSED;

    private static final String ENTITY_API_URL = "/api/app/fiscal-years";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/app/_search/fiscal-years";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FiscalYearRepository fiscalYearRepository;

    @Mock
    private FiscalYearRepository fiscalYearRepositoryMock;

    @Autowired
    private FiscalYearMapper fiscalYearMapper;

    @Mock
    private FiscalYearService fiscalYearServiceMock;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.FiscalYearSearchRepositoryMockConfiguration
     */
    @Autowired
    private FiscalYearSearchRepository mockFiscalYearSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFiscalYearMockMvc;

    private FiscalYear fiscalYear;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FiscalYear createEntity(EntityManager em) {
        FiscalYear fiscalYear = new FiscalYear()
            .fiscalYearCode(DEFAULT_FISCAL_YEAR_CODE)
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE)
            .fiscalYearStatus(DEFAULT_FISCAL_YEAR_STATUS);
        return fiscalYear;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FiscalYear createUpdatedEntity(EntityManager em) {
        FiscalYear fiscalYear = new FiscalYear()
            .fiscalYearCode(UPDATED_FISCAL_YEAR_CODE)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .fiscalYearStatus(UPDATED_FISCAL_YEAR_STATUS);
        return fiscalYear;
    }

    @BeforeEach
    public void initTest() {
        fiscalYear = createEntity(em);
    }

    @Test
    @Transactional
    void createFiscalYear() throws Exception {
        int databaseSizeBeforeCreate = fiscalYearRepository.findAll().size();
        // Create the FiscalYear
        FiscalYearDTO fiscalYearDTO = fiscalYearMapper.toDto(fiscalYear);
        restFiscalYearMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fiscalYearDTO)))
            .andExpect(status().isCreated());

        // Validate the FiscalYear in the database
        List<FiscalYear> fiscalYearList = fiscalYearRepository.findAll();
        assertThat(fiscalYearList).hasSize(databaseSizeBeforeCreate + 1);
        FiscalYear testFiscalYear = fiscalYearList.get(fiscalYearList.size() - 1);
        assertThat(testFiscalYear.getFiscalYearCode()).isEqualTo(DEFAULT_FISCAL_YEAR_CODE);
        assertThat(testFiscalYear.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testFiscalYear.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testFiscalYear.getFiscalYearStatus()).isEqualTo(DEFAULT_FISCAL_YEAR_STATUS);

        // Validate the FiscalYear in Elasticsearch
        verify(mockFiscalYearSearchRepository, times(1)).save(testFiscalYear);
    }

    @Test
    @Transactional
    void createFiscalYearWithExistingId() throws Exception {
        // Create the FiscalYear with an existing ID
        fiscalYear.setId(1L);
        FiscalYearDTO fiscalYearDTO = fiscalYearMapper.toDto(fiscalYear);

        int databaseSizeBeforeCreate = fiscalYearRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFiscalYearMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fiscalYearDTO)))
            .andExpect(status().isBadRequest());

        // Validate the FiscalYear in the database
        List<FiscalYear> fiscalYearList = fiscalYearRepository.findAll();
        assertThat(fiscalYearList).hasSize(databaseSizeBeforeCreate);

        // Validate the FiscalYear in Elasticsearch
        verify(mockFiscalYearSearchRepository, times(0)).save(fiscalYear);
    }

    @Test
    @Transactional
    void checkFiscalYearCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = fiscalYearRepository.findAll().size();
        // set the field null
        fiscalYear.setFiscalYearCode(null);

        // Create the FiscalYear, which fails.
        FiscalYearDTO fiscalYearDTO = fiscalYearMapper.toDto(fiscalYear);

        restFiscalYearMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fiscalYearDTO)))
            .andExpect(status().isBadRequest());

        List<FiscalYear> fiscalYearList = fiscalYearRepository.findAll();
        assertThat(fiscalYearList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStartDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = fiscalYearRepository.findAll().size();
        // set the field null
        fiscalYear.setStartDate(null);

        // Create the FiscalYear, which fails.
        FiscalYearDTO fiscalYearDTO = fiscalYearMapper.toDto(fiscalYear);

        restFiscalYearMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fiscalYearDTO)))
            .andExpect(status().isBadRequest());

        List<FiscalYear> fiscalYearList = fiscalYearRepository.findAll();
        assertThat(fiscalYearList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEndDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = fiscalYearRepository.findAll().size();
        // set the field null
        fiscalYear.setEndDate(null);

        // Create the FiscalYear, which fails.
        FiscalYearDTO fiscalYearDTO = fiscalYearMapper.toDto(fiscalYear);

        restFiscalYearMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fiscalYearDTO)))
            .andExpect(status().isBadRequest());

        List<FiscalYear> fiscalYearList = fiscalYearRepository.findAll();
        assertThat(fiscalYearList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllFiscalYears() throws Exception {
        // Initialize the database
        fiscalYearRepository.saveAndFlush(fiscalYear);

        // Get all the fiscalYearList
        restFiscalYearMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fiscalYear.getId().intValue())))
            .andExpect(jsonPath("$.[*].fiscalYearCode").value(hasItem(DEFAULT_FISCAL_YEAR_CODE)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].fiscalYearStatus").value(hasItem(DEFAULT_FISCAL_YEAR_STATUS.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllFiscalYearsWithEagerRelationshipsIsEnabled() throws Exception {
        when(fiscalYearServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restFiscalYearMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(fiscalYearServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllFiscalYearsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(fiscalYearServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restFiscalYearMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(fiscalYearServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getFiscalYear() throws Exception {
        // Initialize the database
        fiscalYearRepository.saveAndFlush(fiscalYear);

        // Get the fiscalYear
        restFiscalYearMockMvc
            .perform(get(ENTITY_API_URL_ID, fiscalYear.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(fiscalYear.getId().intValue()))
            .andExpect(jsonPath("$.fiscalYearCode").value(DEFAULT_FISCAL_YEAR_CODE))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.fiscalYearStatus").value(DEFAULT_FISCAL_YEAR_STATUS.toString()));
    }

    @Test
    @Transactional
    void getNonExistingFiscalYear() throws Exception {
        // Get the fiscalYear
        restFiscalYearMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewFiscalYear() throws Exception {
        // Initialize the database
        fiscalYearRepository.saveAndFlush(fiscalYear);

        int databaseSizeBeforeUpdate = fiscalYearRepository.findAll().size();

        // Update the fiscalYear
        FiscalYear updatedFiscalYear = fiscalYearRepository.findById(fiscalYear.getId()).get();
        // Disconnect from session so that the updates on updatedFiscalYear are not directly saved in db
        em.detach(updatedFiscalYear);
        updatedFiscalYear
            .fiscalYearCode(UPDATED_FISCAL_YEAR_CODE)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .fiscalYearStatus(UPDATED_FISCAL_YEAR_STATUS);
        FiscalYearDTO fiscalYearDTO = fiscalYearMapper.toDto(updatedFiscalYear);

        restFiscalYearMockMvc
            .perform(
                put(ENTITY_API_URL_ID, fiscalYearDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fiscalYearDTO))
            )
            .andExpect(status().isOk());

        // Validate the FiscalYear in the database
        List<FiscalYear> fiscalYearList = fiscalYearRepository.findAll();
        assertThat(fiscalYearList).hasSize(databaseSizeBeforeUpdate);
        FiscalYear testFiscalYear = fiscalYearList.get(fiscalYearList.size() - 1);
        assertThat(testFiscalYear.getFiscalYearCode()).isEqualTo(UPDATED_FISCAL_YEAR_CODE);
        assertThat(testFiscalYear.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testFiscalYear.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testFiscalYear.getFiscalYearStatus()).isEqualTo(UPDATED_FISCAL_YEAR_STATUS);

        // Validate the FiscalYear in Elasticsearch
        verify(mockFiscalYearSearchRepository).save(testFiscalYear);
    }

    @Test
    @Transactional
    void putNonExistingFiscalYear() throws Exception {
        int databaseSizeBeforeUpdate = fiscalYearRepository.findAll().size();
        fiscalYear.setId(count.incrementAndGet());

        // Create the FiscalYear
        FiscalYearDTO fiscalYearDTO = fiscalYearMapper.toDto(fiscalYear);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFiscalYearMockMvc
            .perform(
                put(ENTITY_API_URL_ID, fiscalYearDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fiscalYearDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FiscalYear in the database
        List<FiscalYear> fiscalYearList = fiscalYearRepository.findAll();
        assertThat(fiscalYearList).hasSize(databaseSizeBeforeUpdate);

        // Validate the FiscalYear in Elasticsearch
        verify(mockFiscalYearSearchRepository, times(0)).save(fiscalYear);
    }

    @Test
    @Transactional
    void putWithIdMismatchFiscalYear() throws Exception {
        int databaseSizeBeforeUpdate = fiscalYearRepository.findAll().size();
        fiscalYear.setId(count.incrementAndGet());

        // Create the FiscalYear
        FiscalYearDTO fiscalYearDTO = fiscalYearMapper.toDto(fiscalYear);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFiscalYearMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fiscalYearDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FiscalYear in the database
        List<FiscalYear> fiscalYearList = fiscalYearRepository.findAll();
        assertThat(fiscalYearList).hasSize(databaseSizeBeforeUpdate);

        // Validate the FiscalYear in Elasticsearch
        verify(mockFiscalYearSearchRepository, times(0)).save(fiscalYear);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFiscalYear() throws Exception {
        int databaseSizeBeforeUpdate = fiscalYearRepository.findAll().size();
        fiscalYear.setId(count.incrementAndGet());

        // Create the FiscalYear
        FiscalYearDTO fiscalYearDTO = fiscalYearMapper.toDto(fiscalYear);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFiscalYearMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fiscalYearDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the FiscalYear in the database
        List<FiscalYear> fiscalYearList = fiscalYearRepository.findAll();
        assertThat(fiscalYearList).hasSize(databaseSizeBeforeUpdate);

        // Validate the FiscalYear in Elasticsearch
        verify(mockFiscalYearSearchRepository, times(0)).save(fiscalYear);
    }

    @Test
    @Transactional
    void partialUpdateFiscalYearWithPatch() throws Exception {
        // Initialize the database
        fiscalYearRepository.saveAndFlush(fiscalYear);

        int databaseSizeBeforeUpdate = fiscalYearRepository.findAll().size();

        // Update the fiscalYear using partial update
        FiscalYear partialUpdatedFiscalYear = new FiscalYear();
        partialUpdatedFiscalYear.setId(fiscalYear.getId());

        partialUpdatedFiscalYear.startDate(UPDATED_START_DATE);

        restFiscalYearMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFiscalYear.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFiscalYear))
            )
            .andExpect(status().isOk());

        // Validate the FiscalYear in the database
        List<FiscalYear> fiscalYearList = fiscalYearRepository.findAll();
        assertThat(fiscalYearList).hasSize(databaseSizeBeforeUpdate);
        FiscalYear testFiscalYear = fiscalYearList.get(fiscalYearList.size() - 1);
        assertThat(testFiscalYear.getFiscalYearCode()).isEqualTo(DEFAULT_FISCAL_YEAR_CODE);
        assertThat(testFiscalYear.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testFiscalYear.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testFiscalYear.getFiscalYearStatus()).isEqualTo(DEFAULT_FISCAL_YEAR_STATUS);
    }

    @Test
    @Transactional
    void fullUpdateFiscalYearWithPatch() throws Exception {
        // Initialize the database
        fiscalYearRepository.saveAndFlush(fiscalYear);

        int databaseSizeBeforeUpdate = fiscalYearRepository.findAll().size();

        // Update the fiscalYear using partial update
        FiscalYear partialUpdatedFiscalYear = new FiscalYear();
        partialUpdatedFiscalYear.setId(fiscalYear.getId());

        partialUpdatedFiscalYear
            .fiscalYearCode(UPDATED_FISCAL_YEAR_CODE)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .fiscalYearStatus(UPDATED_FISCAL_YEAR_STATUS);

        restFiscalYearMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFiscalYear.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFiscalYear))
            )
            .andExpect(status().isOk());

        // Validate the FiscalYear in the database
        List<FiscalYear> fiscalYearList = fiscalYearRepository.findAll();
        assertThat(fiscalYearList).hasSize(databaseSizeBeforeUpdate);
        FiscalYear testFiscalYear = fiscalYearList.get(fiscalYearList.size() - 1);
        assertThat(testFiscalYear.getFiscalYearCode()).isEqualTo(UPDATED_FISCAL_YEAR_CODE);
        assertThat(testFiscalYear.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testFiscalYear.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testFiscalYear.getFiscalYearStatus()).isEqualTo(UPDATED_FISCAL_YEAR_STATUS);
    }

    @Test
    @Transactional
    void patchNonExistingFiscalYear() throws Exception {
        int databaseSizeBeforeUpdate = fiscalYearRepository.findAll().size();
        fiscalYear.setId(count.incrementAndGet());

        // Create the FiscalYear
        FiscalYearDTO fiscalYearDTO = fiscalYearMapper.toDto(fiscalYear);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFiscalYearMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, fiscalYearDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(fiscalYearDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FiscalYear in the database
        List<FiscalYear> fiscalYearList = fiscalYearRepository.findAll();
        assertThat(fiscalYearList).hasSize(databaseSizeBeforeUpdate);

        // Validate the FiscalYear in Elasticsearch
        verify(mockFiscalYearSearchRepository, times(0)).save(fiscalYear);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFiscalYear() throws Exception {
        int databaseSizeBeforeUpdate = fiscalYearRepository.findAll().size();
        fiscalYear.setId(count.incrementAndGet());

        // Create the FiscalYear
        FiscalYearDTO fiscalYearDTO = fiscalYearMapper.toDto(fiscalYear);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFiscalYearMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(fiscalYearDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FiscalYear in the database
        List<FiscalYear> fiscalYearList = fiscalYearRepository.findAll();
        assertThat(fiscalYearList).hasSize(databaseSizeBeforeUpdate);

        // Validate the FiscalYear in Elasticsearch
        verify(mockFiscalYearSearchRepository, times(0)).save(fiscalYear);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFiscalYear() throws Exception {
        int databaseSizeBeforeUpdate = fiscalYearRepository.findAll().size();
        fiscalYear.setId(count.incrementAndGet());

        // Create the FiscalYear
        FiscalYearDTO fiscalYearDTO = fiscalYearMapper.toDto(fiscalYear);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFiscalYearMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(fiscalYearDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FiscalYear in the database
        List<FiscalYear> fiscalYearList = fiscalYearRepository.findAll();
        assertThat(fiscalYearList).hasSize(databaseSizeBeforeUpdate);

        // Validate the FiscalYear in Elasticsearch
        verify(mockFiscalYearSearchRepository, times(0)).save(fiscalYear);
    }

    @Test
    @Transactional
    void deleteFiscalYear() throws Exception {
        // Initialize the database
        fiscalYearRepository.saveAndFlush(fiscalYear);

        int databaseSizeBeforeDelete = fiscalYearRepository.findAll().size();

        // Delete the fiscalYear
        restFiscalYearMockMvc
            .perform(delete(ENTITY_API_URL_ID, fiscalYear.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<FiscalYear> fiscalYearList = fiscalYearRepository.findAll();
        assertThat(fiscalYearList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the FiscalYear in Elasticsearch
        verify(mockFiscalYearSearchRepository, times(1)).deleteById(fiscalYear.getId());
    }

    @Test
    @Transactional
    void searchFiscalYear() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        fiscalYearRepository.saveAndFlush(fiscalYear);
        when(mockFiscalYearSearchRepository.search("id:" + fiscalYear.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(fiscalYear), PageRequest.of(0, 1), 1));

        // Search the fiscalYear
        restFiscalYearMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + fiscalYear.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fiscalYear.getId().intValue())))
            .andExpect(jsonPath("$.[*].fiscalYearCode").value(hasItem(DEFAULT_FISCAL_YEAR_CODE)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].fiscalYearStatus").value(hasItem(DEFAULT_FISCAL_YEAR_STATUS.toString())));
    }
}
