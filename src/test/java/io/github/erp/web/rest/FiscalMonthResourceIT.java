package io.github.erp.web.rest;

/*-
 * Erp System - Mark X No 4 (Jehoiada Series) Server ver 1.7.4
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
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import io.github.erp.IntegrationTest;
import io.github.erp.domain.FiscalMonth;
import io.github.erp.domain.FiscalYear;
import io.github.erp.repository.FiscalMonthRepository;
import io.github.erp.repository.search.FiscalMonthSearchRepository;
import io.github.erp.service.FiscalMonthService;
import io.github.erp.service.dto.FiscalMonthDTO;
import io.github.erp.service.mapper.FiscalMonthMapper;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
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
 * Integration tests for the {@link FiscalMonthResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class FiscalMonthResourceIT {

    private static final Integer DEFAULT_MONTH_NUMBER = 1;
    private static final Integer UPDATED_MONTH_NUMBER = 2;

    private static final LocalDate DEFAULT_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_FISCAL_MONTH_CODE = "AAAAAAAAAA";
    private static final String UPDATED_FISCAL_MONTH_CODE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/fiscal-months";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/fiscal-months";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FiscalMonthRepository fiscalMonthRepository;

    @Mock
    private FiscalMonthRepository fiscalMonthRepositoryMock;

    @Autowired
    private FiscalMonthMapper fiscalMonthMapper;

    @Mock
    private FiscalMonthService fiscalMonthServiceMock;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.FiscalMonthSearchRepositoryMockConfiguration
     */
    @Autowired
    private FiscalMonthSearchRepository mockFiscalMonthSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFiscalMonthMockMvc;

    private FiscalMonth fiscalMonth;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FiscalMonth createEntity(EntityManager em) {
        FiscalMonth fiscalMonth = new FiscalMonth()
            .monthNumber(DEFAULT_MONTH_NUMBER)
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE)
            .fiscalMonthCode(DEFAULT_FISCAL_MONTH_CODE);
        // Add required entity
        FiscalYear fiscalYear;
        if (TestUtil.findAll(em, FiscalYear.class).isEmpty()) {
            fiscalYear = FiscalYearResourceIT.createEntity(em);
            em.persist(fiscalYear);
            em.flush();
        } else {
            fiscalYear = TestUtil.findAll(em, FiscalYear.class).get(0);
        }
        fiscalMonth.setFiscalYear(fiscalYear);
        return fiscalMonth;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FiscalMonth createUpdatedEntity(EntityManager em) {
        FiscalMonth fiscalMonth = new FiscalMonth()
            .monthNumber(UPDATED_MONTH_NUMBER)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .fiscalMonthCode(UPDATED_FISCAL_MONTH_CODE);
        // Add required entity
        FiscalYear fiscalYear;
        if (TestUtil.findAll(em, FiscalYear.class).isEmpty()) {
            fiscalYear = FiscalYearResourceIT.createUpdatedEntity(em);
            em.persist(fiscalYear);
            em.flush();
        } else {
            fiscalYear = TestUtil.findAll(em, FiscalYear.class).get(0);
        }
        fiscalMonth.setFiscalYear(fiscalYear);
        return fiscalMonth;
    }

    @BeforeEach
    public void initTest() {
        fiscalMonth = createEntity(em);
    }

    @Test
    @Transactional
    void createFiscalMonth() throws Exception {
        int databaseSizeBeforeCreate = fiscalMonthRepository.findAll().size();
        // Create the FiscalMonth
        FiscalMonthDTO fiscalMonthDTO = fiscalMonthMapper.toDto(fiscalMonth);
        restFiscalMonthMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fiscalMonthDTO))
            )
            .andExpect(status().isCreated());

        // Validate the FiscalMonth in the database
        List<FiscalMonth> fiscalMonthList = fiscalMonthRepository.findAll();
        assertThat(fiscalMonthList).hasSize(databaseSizeBeforeCreate + 1);
        FiscalMonth testFiscalMonth = fiscalMonthList.get(fiscalMonthList.size() - 1);
        assertThat(testFiscalMonth.getMonthNumber()).isEqualTo(DEFAULT_MONTH_NUMBER);
        assertThat(testFiscalMonth.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testFiscalMonth.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testFiscalMonth.getFiscalMonthCode()).isEqualTo(DEFAULT_FISCAL_MONTH_CODE);

        // Validate the FiscalMonth in Elasticsearch
        verify(mockFiscalMonthSearchRepository, times(1)).save(testFiscalMonth);
    }

    @Test
    @Transactional
    void createFiscalMonthWithExistingId() throws Exception {
        // Create the FiscalMonth with an existing ID
        fiscalMonth.setId(1L);
        FiscalMonthDTO fiscalMonthDTO = fiscalMonthMapper.toDto(fiscalMonth);

        int databaseSizeBeforeCreate = fiscalMonthRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFiscalMonthMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fiscalMonthDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FiscalMonth in the database
        List<FiscalMonth> fiscalMonthList = fiscalMonthRepository.findAll();
        assertThat(fiscalMonthList).hasSize(databaseSizeBeforeCreate);

        // Validate the FiscalMonth in Elasticsearch
        verify(mockFiscalMonthSearchRepository, times(0)).save(fiscalMonth);
    }

    @Test
    @Transactional
    void checkMonthNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = fiscalMonthRepository.findAll().size();
        // set the field null
        fiscalMonth.setMonthNumber(null);

        // Create the FiscalMonth, which fails.
        FiscalMonthDTO fiscalMonthDTO = fiscalMonthMapper.toDto(fiscalMonth);

        restFiscalMonthMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fiscalMonthDTO))
            )
            .andExpect(status().isBadRequest());

        List<FiscalMonth> fiscalMonthList = fiscalMonthRepository.findAll();
        assertThat(fiscalMonthList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStartDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = fiscalMonthRepository.findAll().size();
        // set the field null
        fiscalMonth.setStartDate(null);

        // Create the FiscalMonth, which fails.
        FiscalMonthDTO fiscalMonthDTO = fiscalMonthMapper.toDto(fiscalMonth);

        restFiscalMonthMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fiscalMonthDTO))
            )
            .andExpect(status().isBadRequest());

        List<FiscalMonth> fiscalMonthList = fiscalMonthRepository.findAll();
        assertThat(fiscalMonthList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEndDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = fiscalMonthRepository.findAll().size();
        // set the field null
        fiscalMonth.setEndDate(null);

        // Create the FiscalMonth, which fails.
        FiscalMonthDTO fiscalMonthDTO = fiscalMonthMapper.toDto(fiscalMonth);

        restFiscalMonthMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fiscalMonthDTO))
            )
            .andExpect(status().isBadRequest());

        List<FiscalMonth> fiscalMonthList = fiscalMonthRepository.findAll();
        assertThat(fiscalMonthList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFiscalMonthCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = fiscalMonthRepository.findAll().size();
        // set the field null
        fiscalMonth.setFiscalMonthCode(null);

        // Create the FiscalMonth, which fails.
        FiscalMonthDTO fiscalMonthDTO = fiscalMonthMapper.toDto(fiscalMonth);

        restFiscalMonthMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fiscalMonthDTO))
            )
            .andExpect(status().isBadRequest());

        List<FiscalMonth> fiscalMonthList = fiscalMonthRepository.findAll();
        assertThat(fiscalMonthList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllFiscalMonths() throws Exception {
        // Initialize the database
        fiscalMonthRepository.saveAndFlush(fiscalMonth);

        // Get all the fiscalMonthList
        restFiscalMonthMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fiscalMonth.getId().intValue())))
            .andExpect(jsonPath("$.[*].monthNumber").value(hasItem(DEFAULT_MONTH_NUMBER)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].fiscalMonthCode").value(hasItem(DEFAULT_FISCAL_MONTH_CODE)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllFiscalMonthsWithEagerRelationshipsIsEnabled() throws Exception {
        when(fiscalMonthServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restFiscalMonthMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(fiscalMonthServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllFiscalMonthsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(fiscalMonthServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restFiscalMonthMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(fiscalMonthServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getFiscalMonth() throws Exception {
        // Initialize the database
        fiscalMonthRepository.saveAndFlush(fiscalMonth);

        // Get the fiscalMonth
        restFiscalMonthMockMvc
            .perform(get(ENTITY_API_URL_ID, fiscalMonth.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(fiscalMonth.getId().intValue()))
            .andExpect(jsonPath("$.monthNumber").value(DEFAULT_MONTH_NUMBER))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.fiscalMonthCode").value(DEFAULT_FISCAL_MONTH_CODE));
    }

    @Test
    @Transactional
    void getNonExistingFiscalMonth() throws Exception {
        // Get the fiscalMonth
        restFiscalMonthMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewFiscalMonth() throws Exception {
        // Initialize the database
        fiscalMonthRepository.saveAndFlush(fiscalMonth);

        int databaseSizeBeforeUpdate = fiscalMonthRepository.findAll().size();

        // Update the fiscalMonth
        FiscalMonth updatedFiscalMonth = fiscalMonthRepository.findById(fiscalMonth.getId()).get();
        // Disconnect from session so that the updates on updatedFiscalMonth are not directly saved in db
        em.detach(updatedFiscalMonth);
        updatedFiscalMonth
            .monthNumber(UPDATED_MONTH_NUMBER)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .fiscalMonthCode(UPDATED_FISCAL_MONTH_CODE);
        FiscalMonthDTO fiscalMonthDTO = fiscalMonthMapper.toDto(updatedFiscalMonth);

        restFiscalMonthMockMvc
            .perform(
                put(ENTITY_API_URL_ID, fiscalMonthDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fiscalMonthDTO))
            )
            .andExpect(status().isOk());

        // Validate the FiscalMonth in the database
        List<FiscalMonth> fiscalMonthList = fiscalMonthRepository.findAll();
        assertThat(fiscalMonthList).hasSize(databaseSizeBeforeUpdate);
        FiscalMonth testFiscalMonth = fiscalMonthList.get(fiscalMonthList.size() - 1);
        assertThat(testFiscalMonth.getMonthNumber()).isEqualTo(UPDATED_MONTH_NUMBER);
        assertThat(testFiscalMonth.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testFiscalMonth.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testFiscalMonth.getFiscalMonthCode()).isEqualTo(UPDATED_FISCAL_MONTH_CODE);

        // Validate the FiscalMonth in Elasticsearch
        verify(mockFiscalMonthSearchRepository).save(testFiscalMonth);
    }

    @Test
    @Transactional
    void putNonExistingFiscalMonth() throws Exception {
        int databaseSizeBeforeUpdate = fiscalMonthRepository.findAll().size();
        fiscalMonth.setId(count.incrementAndGet());

        // Create the FiscalMonth
        FiscalMonthDTO fiscalMonthDTO = fiscalMonthMapper.toDto(fiscalMonth);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFiscalMonthMockMvc
            .perform(
                put(ENTITY_API_URL_ID, fiscalMonthDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fiscalMonthDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FiscalMonth in the database
        List<FiscalMonth> fiscalMonthList = fiscalMonthRepository.findAll();
        assertThat(fiscalMonthList).hasSize(databaseSizeBeforeUpdate);

        // Validate the FiscalMonth in Elasticsearch
        verify(mockFiscalMonthSearchRepository, times(0)).save(fiscalMonth);
    }

    @Test
    @Transactional
    void putWithIdMismatchFiscalMonth() throws Exception {
        int databaseSizeBeforeUpdate = fiscalMonthRepository.findAll().size();
        fiscalMonth.setId(count.incrementAndGet());

        // Create the FiscalMonth
        FiscalMonthDTO fiscalMonthDTO = fiscalMonthMapper.toDto(fiscalMonth);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFiscalMonthMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fiscalMonthDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FiscalMonth in the database
        List<FiscalMonth> fiscalMonthList = fiscalMonthRepository.findAll();
        assertThat(fiscalMonthList).hasSize(databaseSizeBeforeUpdate);

        // Validate the FiscalMonth in Elasticsearch
        verify(mockFiscalMonthSearchRepository, times(0)).save(fiscalMonth);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFiscalMonth() throws Exception {
        int databaseSizeBeforeUpdate = fiscalMonthRepository.findAll().size();
        fiscalMonth.setId(count.incrementAndGet());

        // Create the FiscalMonth
        FiscalMonthDTO fiscalMonthDTO = fiscalMonthMapper.toDto(fiscalMonth);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFiscalMonthMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fiscalMonthDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the FiscalMonth in the database
        List<FiscalMonth> fiscalMonthList = fiscalMonthRepository.findAll();
        assertThat(fiscalMonthList).hasSize(databaseSizeBeforeUpdate);

        // Validate the FiscalMonth in Elasticsearch
        verify(mockFiscalMonthSearchRepository, times(0)).save(fiscalMonth);
    }

    @Test
    @Transactional
    void partialUpdateFiscalMonthWithPatch() throws Exception {
        // Initialize the database
        fiscalMonthRepository.saveAndFlush(fiscalMonth);

        int databaseSizeBeforeUpdate = fiscalMonthRepository.findAll().size();

        // Update the fiscalMonth using partial update
        FiscalMonth partialUpdatedFiscalMonth = new FiscalMonth();
        partialUpdatedFiscalMonth.setId(fiscalMonth.getId());

        partialUpdatedFiscalMonth.endDate(UPDATED_END_DATE);

        restFiscalMonthMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFiscalMonth.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFiscalMonth))
            )
            .andExpect(status().isOk());

        // Validate the FiscalMonth in the database
        List<FiscalMonth> fiscalMonthList = fiscalMonthRepository.findAll();
        assertThat(fiscalMonthList).hasSize(databaseSizeBeforeUpdate);
        FiscalMonth testFiscalMonth = fiscalMonthList.get(fiscalMonthList.size() - 1);
        assertThat(testFiscalMonth.getMonthNumber()).isEqualTo(DEFAULT_MONTH_NUMBER);
        assertThat(testFiscalMonth.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testFiscalMonth.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testFiscalMonth.getFiscalMonthCode()).isEqualTo(DEFAULT_FISCAL_MONTH_CODE);
    }

    @Test
    @Transactional
    void fullUpdateFiscalMonthWithPatch() throws Exception {
        // Initialize the database
        fiscalMonthRepository.saveAndFlush(fiscalMonth);

        int databaseSizeBeforeUpdate = fiscalMonthRepository.findAll().size();

        // Update the fiscalMonth using partial update
        FiscalMonth partialUpdatedFiscalMonth = new FiscalMonth();
        partialUpdatedFiscalMonth.setId(fiscalMonth.getId());

        partialUpdatedFiscalMonth
            .monthNumber(UPDATED_MONTH_NUMBER)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .fiscalMonthCode(UPDATED_FISCAL_MONTH_CODE);

        restFiscalMonthMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFiscalMonth.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFiscalMonth))
            )
            .andExpect(status().isOk());

        // Validate the FiscalMonth in the database
        List<FiscalMonth> fiscalMonthList = fiscalMonthRepository.findAll();
        assertThat(fiscalMonthList).hasSize(databaseSizeBeforeUpdate);
        FiscalMonth testFiscalMonth = fiscalMonthList.get(fiscalMonthList.size() - 1);
        assertThat(testFiscalMonth.getMonthNumber()).isEqualTo(UPDATED_MONTH_NUMBER);
        assertThat(testFiscalMonth.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testFiscalMonth.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testFiscalMonth.getFiscalMonthCode()).isEqualTo(UPDATED_FISCAL_MONTH_CODE);
    }

    @Test
    @Transactional
    void patchNonExistingFiscalMonth() throws Exception {
        int databaseSizeBeforeUpdate = fiscalMonthRepository.findAll().size();
        fiscalMonth.setId(count.incrementAndGet());

        // Create the FiscalMonth
        FiscalMonthDTO fiscalMonthDTO = fiscalMonthMapper.toDto(fiscalMonth);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFiscalMonthMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, fiscalMonthDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(fiscalMonthDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FiscalMonth in the database
        List<FiscalMonth> fiscalMonthList = fiscalMonthRepository.findAll();
        assertThat(fiscalMonthList).hasSize(databaseSizeBeforeUpdate);

        // Validate the FiscalMonth in Elasticsearch
        verify(mockFiscalMonthSearchRepository, times(0)).save(fiscalMonth);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFiscalMonth() throws Exception {
        int databaseSizeBeforeUpdate = fiscalMonthRepository.findAll().size();
        fiscalMonth.setId(count.incrementAndGet());

        // Create the FiscalMonth
        FiscalMonthDTO fiscalMonthDTO = fiscalMonthMapper.toDto(fiscalMonth);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFiscalMonthMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(fiscalMonthDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FiscalMonth in the database
        List<FiscalMonth> fiscalMonthList = fiscalMonthRepository.findAll();
        assertThat(fiscalMonthList).hasSize(databaseSizeBeforeUpdate);

        // Validate the FiscalMonth in Elasticsearch
        verify(mockFiscalMonthSearchRepository, times(0)).save(fiscalMonth);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFiscalMonth() throws Exception {
        int databaseSizeBeforeUpdate = fiscalMonthRepository.findAll().size();
        fiscalMonth.setId(count.incrementAndGet());

        // Create the FiscalMonth
        FiscalMonthDTO fiscalMonthDTO = fiscalMonthMapper.toDto(fiscalMonth);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFiscalMonthMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(fiscalMonthDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FiscalMonth in the database
        List<FiscalMonth> fiscalMonthList = fiscalMonthRepository.findAll();
        assertThat(fiscalMonthList).hasSize(databaseSizeBeforeUpdate);

        // Validate the FiscalMonth in Elasticsearch
        verify(mockFiscalMonthSearchRepository, times(0)).save(fiscalMonth);
    }

    @Test
    @Transactional
    void deleteFiscalMonth() throws Exception {
        // Initialize the database
        fiscalMonthRepository.saveAndFlush(fiscalMonth);

        int databaseSizeBeforeDelete = fiscalMonthRepository.findAll().size();

        // Delete the fiscalMonth
        restFiscalMonthMockMvc
            .perform(delete(ENTITY_API_URL_ID, fiscalMonth.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<FiscalMonth> fiscalMonthList = fiscalMonthRepository.findAll();
        assertThat(fiscalMonthList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the FiscalMonth in Elasticsearch
        verify(mockFiscalMonthSearchRepository, times(1)).deleteById(fiscalMonth.getId());
    }

    @Test
    @Transactional
    void searchFiscalMonth() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        fiscalMonthRepository.saveAndFlush(fiscalMonth);
        when(mockFiscalMonthSearchRepository.search("id:" + fiscalMonth.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(fiscalMonth), PageRequest.of(0, 1), 1));

        // Search the fiscalMonth
        restFiscalMonthMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + fiscalMonth.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fiscalMonth.getId().intValue())))
            .andExpect(jsonPath("$.[*].monthNumber").value(hasItem(DEFAULT_MONTH_NUMBER)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].fiscalMonthCode").value(hasItem(DEFAULT_FISCAL_MONTH_CODE)));
    }
}
