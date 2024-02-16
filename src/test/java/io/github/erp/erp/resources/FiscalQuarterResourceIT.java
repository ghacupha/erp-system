package io.github.erp.erp.resources;

/*-
 * Erp System - Mark X No 3 (Jehoiada Series) Server ver 1.7.3
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
import io.github.erp.domain.FiscalQuarter;
import io.github.erp.domain.FiscalYear;
import io.github.erp.repository.FiscalQuarterRepository;
import io.github.erp.repository.search.FiscalQuarterSearchRepository;
import io.github.erp.service.FiscalQuarterService;
import io.github.erp.service.dto.FiscalQuarterDTO;
import io.github.erp.service.mapper.FiscalQuarterMapper;
import io.github.erp.web.rest.FiscalQuarterResource;
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
 * Integration tests for the FiscalQuarterResourceProd REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class FiscalQuarterResourceIT {

    private static final Integer DEFAULT_QUARTER_NUMBER = 1;
    private static final Integer UPDATED_QUARTER_NUMBER = 2;

    private static final LocalDate DEFAULT_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_FISCAL_QUARTER_CODE = "AAAAAAAAAA";
    private static final String UPDATED_FISCAL_QUARTER_CODE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/app/fiscal-quarters";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/app/_search/fiscal-quarters";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FiscalQuarterRepository fiscalQuarterRepository;

    @Mock
    private FiscalQuarterRepository fiscalQuarterRepositoryMock;

    @Autowired
    private FiscalQuarterMapper fiscalQuarterMapper;

    @Mock
    private FiscalQuarterService fiscalQuarterServiceMock;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.FiscalQuarterSearchRepositoryMockConfiguration
     */
    @Autowired
    private FiscalQuarterSearchRepository mockFiscalQuarterSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFiscalQuarterMockMvc;

    private FiscalQuarter fiscalQuarter;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FiscalQuarter createEntity(EntityManager em) {
        FiscalQuarter fiscalQuarter = new FiscalQuarter()
            .quarterNumber(DEFAULT_QUARTER_NUMBER)
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE)
            .fiscalQuarterCode(DEFAULT_FISCAL_QUARTER_CODE);
        // Add required entity
        FiscalYear fiscalYear;
        if (TestUtil.findAll(em, FiscalYear.class).isEmpty()) {
            fiscalYear = FiscalYearResourceIT.createEntity(em);
            em.persist(fiscalYear);
            em.flush();
        } else {
            fiscalYear = TestUtil.findAll(em, FiscalYear.class).get(0);
        }
        fiscalQuarter.setFiscalYear(fiscalYear);
        return fiscalQuarter;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FiscalQuarter createUpdatedEntity(EntityManager em) {
        FiscalQuarter fiscalQuarter = new FiscalQuarter()
            .quarterNumber(UPDATED_QUARTER_NUMBER)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .fiscalQuarterCode(UPDATED_FISCAL_QUARTER_CODE);
        // Add required entity
        FiscalYear fiscalYear;
        if (TestUtil.findAll(em, FiscalYear.class).isEmpty()) {
            fiscalYear = FiscalYearResourceIT.createUpdatedEntity(em);
            em.persist(fiscalYear);
            em.flush();
        } else {
            fiscalYear = TestUtil.findAll(em, FiscalYear.class).get(0);
        }
        fiscalQuarter.setFiscalYear(fiscalYear);
        return fiscalQuarter;
    }

    @BeforeEach
    public void initTest() {
        fiscalQuarter = createEntity(em);
    }

    @Test
    @Transactional
    void createFiscalQuarter() throws Exception {
        int databaseSizeBeforeCreate = fiscalQuarterRepository.findAll().size();
        // Create the FiscalQuarter
        FiscalQuarterDTO fiscalQuarterDTO = fiscalQuarterMapper.toDto(fiscalQuarter);
        restFiscalQuarterMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fiscalQuarterDTO))
            )
            .andExpect(status().isCreated());

        // Validate the FiscalQuarter in the database
        List<FiscalQuarter> fiscalQuarterList = fiscalQuarterRepository.findAll();
        assertThat(fiscalQuarterList).hasSize(databaseSizeBeforeCreate + 1);
        FiscalQuarter testFiscalQuarter = fiscalQuarterList.get(fiscalQuarterList.size() - 1);
        assertThat(testFiscalQuarter.getQuarterNumber()).isEqualTo(DEFAULT_QUARTER_NUMBER);
        assertThat(testFiscalQuarter.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testFiscalQuarter.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testFiscalQuarter.getFiscalQuarterCode()).isEqualTo(DEFAULT_FISCAL_QUARTER_CODE);

        // Validate the FiscalQuarter in Elasticsearch
        verify(mockFiscalQuarterSearchRepository, times(1)).save(testFiscalQuarter);
    }

    @Test
    @Transactional
    void createFiscalQuarterWithExistingId() throws Exception {
        // Create the FiscalQuarter with an existing ID
        fiscalQuarter.setId(1L);
        FiscalQuarterDTO fiscalQuarterDTO = fiscalQuarterMapper.toDto(fiscalQuarter);

        int databaseSizeBeforeCreate = fiscalQuarterRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFiscalQuarterMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fiscalQuarterDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FiscalQuarter in the database
        List<FiscalQuarter> fiscalQuarterList = fiscalQuarterRepository.findAll();
        assertThat(fiscalQuarterList).hasSize(databaseSizeBeforeCreate);

        // Validate the FiscalQuarter in Elasticsearch
        verify(mockFiscalQuarterSearchRepository, times(0)).save(fiscalQuarter);
    }

    @Test
    @Transactional
    void checkQuarterNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = fiscalQuarterRepository.findAll().size();
        // set the field null
        fiscalQuarter.setQuarterNumber(null);

        // Create the FiscalQuarter, which fails.
        FiscalQuarterDTO fiscalQuarterDTO = fiscalQuarterMapper.toDto(fiscalQuarter);

        restFiscalQuarterMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fiscalQuarterDTO))
            )
            .andExpect(status().isBadRequest());

        List<FiscalQuarter> fiscalQuarterList = fiscalQuarterRepository.findAll();
        assertThat(fiscalQuarterList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStartDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = fiscalQuarterRepository.findAll().size();
        // set the field null
        fiscalQuarter.setStartDate(null);

        // Create the FiscalQuarter, which fails.
        FiscalQuarterDTO fiscalQuarterDTO = fiscalQuarterMapper.toDto(fiscalQuarter);

        restFiscalQuarterMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fiscalQuarterDTO))
            )
            .andExpect(status().isBadRequest());

        List<FiscalQuarter> fiscalQuarterList = fiscalQuarterRepository.findAll();
        assertThat(fiscalQuarterList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEndDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = fiscalQuarterRepository.findAll().size();
        // set the field null
        fiscalQuarter.setEndDate(null);

        // Create the FiscalQuarter, which fails.
        FiscalQuarterDTO fiscalQuarterDTO = fiscalQuarterMapper.toDto(fiscalQuarter);

        restFiscalQuarterMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fiscalQuarterDTO))
            )
            .andExpect(status().isBadRequest());

        List<FiscalQuarter> fiscalQuarterList = fiscalQuarterRepository.findAll();
        assertThat(fiscalQuarterList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFiscalQuarterCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = fiscalQuarterRepository.findAll().size();
        // set the field null
        fiscalQuarter.setFiscalQuarterCode(null);

        // Create the FiscalQuarter, which fails.
        FiscalQuarterDTO fiscalQuarterDTO = fiscalQuarterMapper.toDto(fiscalQuarter);

        restFiscalQuarterMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fiscalQuarterDTO))
            )
            .andExpect(status().isBadRequest());

        List<FiscalQuarter> fiscalQuarterList = fiscalQuarterRepository.findAll();
        assertThat(fiscalQuarterList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllFiscalQuarters() throws Exception {
        // Initialize the database
        fiscalQuarterRepository.saveAndFlush(fiscalQuarter);

        // Get all the fiscalQuarterList
        restFiscalQuarterMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fiscalQuarter.getId().intValue())))
            .andExpect(jsonPath("$.[*].quarterNumber").value(hasItem(DEFAULT_QUARTER_NUMBER)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].fiscalQuarterCode").value(hasItem(DEFAULT_FISCAL_QUARTER_CODE)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllFiscalQuartersWithEagerRelationshipsIsEnabled() throws Exception {
        when(fiscalQuarterServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restFiscalQuarterMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(fiscalQuarterServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllFiscalQuartersWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(fiscalQuarterServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restFiscalQuarterMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(fiscalQuarterServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getFiscalQuarter() throws Exception {
        // Initialize the database
        fiscalQuarterRepository.saveAndFlush(fiscalQuarter);

        // Get the fiscalQuarter
        restFiscalQuarterMockMvc
            .perform(get(ENTITY_API_URL_ID, fiscalQuarter.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(fiscalQuarter.getId().intValue()))
            .andExpect(jsonPath("$.quarterNumber").value(DEFAULT_QUARTER_NUMBER))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.fiscalQuarterCode").value(DEFAULT_FISCAL_QUARTER_CODE));
    }

    @Test
    @Transactional
    void getNonExistingFiscalQuarter() throws Exception {
        // Get the fiscalQuarter
        restFiscalQuarterMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewFiscalQuarter() throws Exception {
        // Initialize the database
        fiscalQuarterRepository.saveAndFlush(fiscalQuarter);

        int databaseSizeBeforeUpdate = fiscalQuarterRepository.findAll().size();

        // Update the fiscalQuarter
        FiscalQuarter updatedFiscalQuarter = fiscalQuarterRepository.findById(fiscalQuarter.getId()).get();
        // Disconnect from session so that the updates on updatedFiscalQuarter are not directly saved in db
        em.detach(updatedFiscalQuarter);
        updatedFiscalQuarter
            .quarterNumber(UPDATED_QUARTER_NUMBER)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .fiscalQuarterCode(UPDATED_FISCAL_QUARTER_CODE);
        FiscalQuarterDTO fiscalQuarterDTO = fiscalQuarterMapper.toDto(updatedFiscalQuarter);

        restFiscalQuarterMockMvc
            .perform(
                put(ENTITY_API_URL_ID, fiscalQuarterDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fiscalQuarterDTO))
            )
            .andExpect(status().isOk());

        // Validate the FiscalQuarter in the database
        List<FiscalQuarter> fiscalQuarterList = fiscalQuarterRepository.findAll();
        assertThat(fiscalQuarterList).hasSize(databaseSizeBeforeUpdate);
        FiscalQuarter testFiscalQuarter = fiscalQuarterList.get(fiscalQuarterList.size() - 1);
        assertThat(testFiscalQuarter.getQuarterNumber()).isEqualTo(UPDATED_QUARTER_NUMBER);
        assertThat(testFiscalQuarter.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testFiscalQuarter.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testFiscalQuarter.getFiscalQuarterCode()).isEqualTo(UPDATED_FISCAL_QUARTER_CODE);

        // Validate the FiscalQuarter in Elasticsearch
        verify(mockFiscalQuarterSearchRepository).save(testFiscalQuarter);
    }

    @Test
    @Transactional
    void putNonExistingFiscalQuarter() throws Exception {
        int databaseSizeBeforeUpdate = fiscalQuarterRepository.findAll().size();
        fiscalQuarter.setId(count.incrementAndGet());

        // Create the FiscalQuarter
        FiscalQuarterDTO fiscalQuarterDTO = fiscalQuarterMapper.toDto(fiscalQuarter);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFiscalQuarterMockMvc
            .perform(
                put(ENTITY_API_URL_ID, fiscalQuarterDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fiscalQuarterDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FiscalQuarter in the database
        List<FiscalQuarter> fiscalQuarterList = fiscalQuarterRepository.findAll();
        assertThat(fiscalQuarterList).hasSize(databaseSizeBeforeUpdate);

        // Validate the FiscalQuarter in Elasticsearch
        verify(mockFiscalQuarterSearchRepository, times(0)).save(fiscalQuarter);
    }

    @Test
    @Transactional
    void putWithIdMismatchFiscalQuarter() throws Exception {
        int databaseSizeBeforeUpdate = fiscalQuarterRepository.findAll().size();
        fiscalQuarter.setId(count.incrementAndGet());

        // Create the FiscalQuarter
        FiscalQuarterDTO fiscalQuarterDTO = fiscalQuarterMapper.toDto(fiscalQuarter);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFiscalQuarterMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fiscalQuarterDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FiscalQuarter in the database
        List<FiscalQuarter> fiscalQuarterList = fiscalQuarterRepository.findAll();
        assertThat(fiscalQuarterList).hasSize(databaseSizeBeforeUpdate);

        // Validate the FiscalQuarter in Elasticsearch
        verify(mockFiscalQuarterSearchRepository, times(0)).save(fiscalQuarter);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFiscalQuarter() throws Exception {
        int databaseSizeBeforeUpdate = fiscalQuarterRepository.findAll().size();
        fiscalQuarter.setId(count.incrementAndGet());

        // Create the FiscalQuarter
        FiscalQuarterDTO fiscalQuarterDTO = fiscalQuarterMapper.toDto(fiscalQuarter);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFiscalQuarterMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fiscalQuarterDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FiscalQuarter in the database
        List<FiscalQuarter> fiscalQuarterList = fiscalQuarterRepository.findAll();
        assertThat(fiscalQuarterList).hasSize(databaseSizeBeforeUpdate);

        // Validate the FiscalQuarter in Elasticsearch
        verify(mockFiscalQuarterSearchRepository, times(0)).save(fiscalQuarter);
    }

    @Test
    @Transactional
    void partialUpdateFiscalQuarterWithPatch() throws Exception {
        // Initialize the database
        fiscalQuarterRepository.saveAndFlush(fiscalQuarter);

        int databaseSizeBeforeUpdate = fiscalQuarterRepository.findAll().size();

        // Update the fiscalQuarter using partial update
        FiscalQuarter partialUpdatedFiscalQuarter = new FiscalQuarter();
        partialUpdatedFiscalQuarter.setId(fiscalQuarter.getId());

        partialUpdatedFiscalQuarter
            .quarterNumber(UPDATED_QUARTER_NUMBER)
            .startDate(UPDATED_START_DATE)
            .fiscalQuarterCode(UPDATED_FISCAL_QUARTER_CODE);

        restFiscalQuarterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFiscalQuarter.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFiscalQuarter))
            )
            .andExpect(status().isOk());

        // Validate the FiscalQuarter in the database
        List<FiscalQuarter> fiscalQuarterList = fiscalQuarterRepository.findAll();
        assertThat(fiscalQuarterList).hasSize(databaseSizeBeforeUpdate);
        FiscalQuarter testFiscalQuarter = fiscalQuarterList.get(fiscalQuarterList.size() - 1);
        assertThat(testFiscalQuarter.getQuarterNumber()).isEqualTo(UPDATED_QUARTER_NUMBER);
        assertThat(testFiscalQuarter.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testFiscalQuarter.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testFiscalQuarter.getFiscalQuarterCode()).isEqualTo(UPDATED_FISCAL_QUARTER_CODE);
    }

    @Test
    @Transactional
    void fullUpdateFiscalQuarterWithPatch() throws Exception {
        // Initialize the database
        fiscalQuarterRepository.saveAndFlush(fiscalQuarter);

        int databaseSizeBeforeUpdate = fiscalQuarterRepository.findAll().size();

        // Update the fiscalQuarter using partial update
        FiscalQuarter partialUpdatedFiscalQuarter = new FiscalQuarter();
        partialUpdatedFiscalQuarter.setId(fiscalQuarter.getId());

        partialUpdatedFiscalQuarter
            .quarterNumber(UPDATED_QUARTER_NUMBER)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .fiscalQuarterCode(UPDATED_FISCAL_QUARTER_CODE);

        restFiscalQuarterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFiscalQuarter.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFiscalQuarter))
            )
            .andExpect(status().isOk());

        // Validate the FiscalQuarter in the database
        List<FiscalQuarter> fiscalQuarterList = fiscalQuarterRepository.findAll();
        assertThat(fiscalQuarterList).hasSize(databaseSizeBeforeUpdate);
        FiscalQuarter testFiscalQuarter = fiscalQuarterList.get(fiscalQuarterList.size() - 1);
        assertThat(testFiscalQuarter.getQuarterNumber()).isEqualTo(UPDATED_QUARTER_NUMBER);
        assertThat(testFiscalQuarter.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testFiscalQuarter.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testFiscalQuarter.getFiscalQuarterCode()).isEqualTo(UPDATED_FISCAL_QUARTER_CODE);
    }

    @Test
    @Transactional
    void patchNonExistingFiscalQuarter() throws Exception {
        int databaseSizeBeforeUpdate = fiscalQuarterRepository.findAll().size();
        fiscalQuarter.setId(count.incrementAndGet());

        // Create the FiscalQuarter
        FiscalQuarterDTO fiscalQuarterDTO = fiscalQuarterMapper.toDto(fiscalQuarter);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFiscalQuarterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, fiscalQuarterDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(fiscalQuarterDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FiscalQuarter in the database
        List<FiscalQuarter> fiscalQuarterList = fiscalQuarterRepository.findAll();
        assertThat(fiscalQuarterList).hasSize(databaseSizeBeforeUpdate);

        // Validate the FiscalQuarter in Elasticsearch
        verify(mockFiscalQuarterSearchRepository, times(0)).save(fiscalQuarter);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFiscalQuarter() throws Exception {
        int databaseSizeBeforeUpdate = fiscalQuarterRepository.findAll().size();
        fiscalQuarter.setId(count.incrementAndGet());

        // Create the FiscalQuarter
        FiscalQuarterDTO fiscalQuarterDTO = fiscalQuarterMapper.toDto(fiscalQuarter);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFiscalQuarterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(fiscalQuarterDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FiscalQuarter in the database
        List<FiscalQuarter> fiscalQuarterList = fiscalQuarterRepository.findAll();
        assertThat(fiscalQuarterList).hasSize(databaseSizeBeforeUpdate);

        // Validate the FiscalQuarter in Elasticsearch
        verify(mockFiscalQuarterSearchRepository, times(0)).save(fiscalQuarter);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFiscalQuarter() throws Exception {
        int databaseSizeBeforeUpdate = fiscalQuarterRepository.findAll().size();
        fiscalQuarter.setId(count.incrementAndGet());

        // Create the FiscalQuarter
        FiscalQuarterDTO fiscalQuarterDTO = fiscalQuarterMapper.toDto(fiscalQuarter);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFiscalQuarterMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(fiscalQuarterDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FiscalQuarter in the database
        List<FiscalQuarter> fiscalQuarterList = fiscalQuarterRepository.findAll();
        assertThat(fiscalQuarterList).hasSize(databaseSizeBeforeUpdate);

        // Validate the FiscalQuarter in Elasticsearch
        verify(mockFiscalQuarterSearchRepository, times(0)).save(fiscalQuarter);
    }

    @Test
    @Transactional
    void deleteFiscalQuarter() throws Exception {
        // Initialize the database
        fiscalQuarterRepository.saveAndFlush(fiscalQuarter);

        int databaseSizeBeforeDelete = fiscalQuarterRepository.findAll().size();

        // Delete the fiscalQuarter
        restFiscalQuarterMockMvc
            .perform(delete(ENTITY_API_URL_ID, fiscalQuarter.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<FiscalQuarter> fiscalQuarterList = fiscalQuarterRepository.findAll();
        assertThat(fiscalQuarterList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the FiscalQuarter in Elasticsearch
        verify(mockFiscalQuarterSearchRepository, times(1)).deleteById(fiscalQuarter.getId());
    }

    @Test
    @Transactional
    void searchFiscalQuarter() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        fiscalQuarterRepository.saveAndFlush(fiscalQuarter);
        when(mockFiscalQuarterSearchRepository.search("id:" + fiscalQuarter.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(fiscalQuarter), PageRequest.of(0, 1), 1));

        // Search the fiscalQuarter
        restFiscalQuarterMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + fiscalQuarter.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fiscalQuarter.getId().intValue())))
            .andExpect(jsonPath("$.[*].quarterNumber").value(hasItem(DEFAULT_QUARTER_NUMBER)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].fiscalQuarterCode").value(hasItem(DEFAULT_FISCAL_QUARTER_CODE)));
    }
}
