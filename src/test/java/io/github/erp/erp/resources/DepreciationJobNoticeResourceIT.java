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
import io.github.erp.domain.DepreciationJobNotice;
import io.github.erp.domain.enumeration.DepreciationNoticeStatusType;
import io.github.erp.repository.DepreciationJobNoticeRepository;
import io.github.erp.repository.search.DepreciationJobNoticeSearchRepository;
import io.github.erp.service.DepreciationJobNoticeService;
import io.github.erp.service.dto.DepreciationJobNoticeDTO;
import io.github.erp.service.mapper.DepreciationJobNoticeMapper;
import io.github.erp.web.rest.DepreciationJobNoticeResource;
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
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

import static io.github.erp.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the DepreciationJobNoticeResourceProd REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser(roles = {"FIXED_ASSETS_USER"})
class DepreciationJobNoticeResourceIT {

    private static final String DEFAULT_EVENT_NARRATIVE = "AAAAAAAAAA";
    private static final String UPDATED_EVENT_NARRATIVE = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_EVENT_TIME_STAMP = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_EVENT_TIME_STAMP = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final DepreciationNoticeStatusType DEFAULT_DEPRECIATION_NOTICE_STATUS = DepreciationNoticeStatusType.INFO;
    private static final DepreciationNoticeStatusType UPDATED_DEPRECIATION_NOTICE_STATUS = DepreciationNoticeStatusType.WARNING;

    private static final String DEFAULT_SOURCE_MODULE = "AAAAAAAAAA";
    private static final String UPDATED_SOURCE_MODULE = "BBBBBBBBBB";

    private static final String DEFAULT_SOURCE_ENTITY = "AAAAAAAAAA";
    private static final String UPDATED_SOURCE_ENTITY = "BBBBBBBBBB";

    private static final String DEFAULT_ERROR_CODE = "AAAAAAAAAA";
    private static final String UPDATED_ERROR_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_ERROR_MESSAGE = "AAAAAAAAAA";
    private static final String UPDATED_ERROR_MESSAGE = "BBBBBBBBBB";

    private static final String DEFAULT_USER_ACTION = "AAAAAAAAAA";
    private static final String UPDATED_USER_ACTION = "BBBBBBBBBB";

    private static final String DEFAULT_TECHNICAL_DETAILS = "AAAAAAAAAA";
    private static final String UPDATED_TECHNICAL_DETAILS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/fixed-asset/depreciation-job-notices";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/fixed-asset/_search/depreciation-job-notices";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DepreciationJobNoticeRepository depreciationJobNoticeRepository;

    @Mock
    private DepreciationJobNoticeRepository depreciationJobNoticeRepositoryMock;

    @Autowired
    private DepreciationJobNoticeMapper depreciationJobNoticeMapper;

    @Mock
    private DepreciationJobNoticeService depreciationJobNoticeServiceMock;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.DepreciationJobNoticeSearchRepositoryMockConfiguration
     */
    @Autowired
    private DepreciationJobNoticeSearchRepository mockDepreciationJobNoticeSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDepreciationJobNoticeMockMvc;

    private DepreciationJobNotice depreciationJobNotice;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DepreciationJobNotice createEntity(EntityManager em) {
        DepreciationJobNotice depreciationJobNotice = new DepreciationJobNotice()
            .eventNarrative(DEFAULT_EVENT_NARRATIVE)
            .eventTimeStamp(DEFAULT_EVENT_TIME_STAMP)
            .depreciationNoticeStatus(DEFAULT_DEPRECIATION_NOTICE_STATUS)
            .sourceModule(DEFAULT_SOURCE_MODULE)
            .sourceEntity(DEFAULT_SOURCE_ENTITY)
            .errorCode(DEFAULT_ERROR_CODE)
            .errorMessage(DEFAULT_ERROR_MESSAGE)
            .userAction(DEFAULT_USER_ACTION)
            .technicalDetails(DEFAULT_TECHNICAL_DETAILS);
        return depreciationJobNotice;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DepreciationJobNotice createUpdatedEntity(EntityManager em) {
        DepreciationJobNotice depreciationJobNotice = new DepreciationJobNotice()
            .eventNarrative(UPDATED_EVENT_NARRATIVE)
            .eventTimeStamp(UPDATED_EVENT_TIME_STAMP)
            .depreciationNoticeStatus(UPDATED_DEPRECIATION_NOTICE_STATUS)
            .sourceModule(UPDATED_SOURCE_MODULE)
            .sourceEntity(UPDATED_SOURCE_ENTITY)
            .errorCode(UPDATED_ERROR_CODE)
            .errorMessage(UPDATED_ERROR_MESSAGE)
            .userAction(UPDATED_USER_ACTION)
            .technicalDetails(UPDATED_TECHNICAL_DETAILS);
        return depreciationJobNotice;
    }

    @BeforeEach
    public void initTest() {
        depreciationJobNotice = createEntity(em);
    }

    @Test
    @Transactional
    void createDepreciationJobNotice() throws Exception {
        int databaseSizeBeforeCreate = depreciationJobNoticeRepository.findAll().size();
        // Create the DepreciationJobNotice
        DepreciationJobNoticeDTO depreciationJobNoticeDTO = depreciationJobNoticeMapper.toDto(depreciationJobNotice);
        restDepreciationJobNoticeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(depreciationJobNoticeDTO))
            )
            .andExpect(status().isCreated());

        // Validate the DepreciationJobNotice in the database
        List<DepreciationJobNotice> depreciationJobNoticeList = depreciationJobNoticeRepository.findAll();
        assertThat(depreciationJobNoticeList).hasSize(databaseSizeBeforeCreate + 1);
        DepreciationJobNotice testDepreciationJobNotice = depreciationJobNoticeList.get(depreciationJobNoticeList.size() - 1);
        assertThat(testDepreciationJobNotice.getEventNarrative()).isEqualTo(DEFAULT_EVENT_NARRATIVE);
        assertThat(testDepreciationJobNotice.getEventTimeStamp()).isEqualTo(DEFAULT_EVENT_TIME_STAMP);
        assertThat(testDepreciationJobNotice.getDepreciationNoticeStatus()).isEqualTo(DEFAULT_DEPRECIATION_NOTICE_STATUS);
        assertThat(testDepreciationJobNotice.getSourceModule()).isEqualTo(DEFAULT_SOURCE_MODULE);
        assertThat(testDepreciationJobNotice.getSourceEntity()).isEqualTo(DEFAULT_SOURCE_ENTITY);
        assertThat(testDepreciationJobNotice.getErrorCode()).isEqualTo(DEFAULT_ERROR_CODE);
        assertThat(testDepreciationJobNotice.getErrorMessage()).isEqualTo(DEFAULT_ERROR_MESSAGE);
        assertThat(testDepreciationJobNotice.getUserAction()).isEqualTo(DEFAULT_USER_ACTION);
        assertThat(testDepreciationJobNotice.getTechnicalDetails()).isEqualTo(DEFAULT_TECHNICAL_DETAILS);

        // Validate the DepreciationJobNotice in Elasticsearch
        verify(mockDepreciationJobNoticeSearchRepository, times(1)).save(testDepreciationJobNotice);
    }

    @Test
    @Transactional
    void createDepreciationJobNoticeWithExistingId() throws Exception {
        // Create the DepreciationJobNotice with an existing ID
        depreciationJobNotice.setId(1L);
        DepreciationJobNoticeDTO depreciationJobNoticeDTO = depreciationJobNoticeMapper.toDto(depreciationJobNotice);

        int databaseSizeBeforeCreate = depreciationJobNoticeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDepreciationJobNoticeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(depreciationJobNoticeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DepreciationJobNotice in the database
        List<DepreciationJobNotice> depreciationJobNoticeList = depreciationJobNoticeRepository.findAll();
        assertThat(depreciationJobNoticeList).hasSize(databaseSizeBeforeCreate);

        // Validate the DepreciationJobNotice in Elasticsearch
        verify(mockDepreciationJobNoticeSearchRepository, times(0)).save(depreciationJobNotice);
    }

    // @Test
    @Transactional
    void checkEventNarrativeIsRequired() throws Exception {
        int databaseSizeBeforeTest = depreciationJobNoticeRepository.findAll().size();
        // set the field null
        depreciationJobNotice.setEventNarrative(null);

        // Create the DepreciationJobNotice, which fails.
        DepreciationJobNoticeDTO depreciationJobNoticeDTO = depreciationJobNoticeMapper.toDto(depreciationJobNotice);

        restDepreciationJobNoticeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(depreciationJobNoticeDTO))
            )
            .andExpect(status().isBadRequest());

        List<DepreciationJobNotice> depreciationJobNoticeList = depreciationJobNoticeRepository.findAll();
        assertThat(depreciationJobNoticeList).hasSize(databaseSizeBeforeTest);
    }

    // @Test
    @Transactional
    void checkEventTimeStampIsRequired() throws Exception {
        int databaseSizeBeforeTest = depreciationJobNoticeRepository.findAll().size();
        // set the field null
        depreciationJobNotice.setEventTimeStamp(null);

        // Create the DepreciationJobNotice, which fails.
        DepreciationJobNoticeDTO depreciationJobNoticeDTO = depreciationJobNoticeMapper.toDto(depreciationJobNotice);

        restDepreciationJobNoticeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(depreciationJobNoticeDTO))
            )
            .andExpect(status().isBadRequest());

        List<DepreciationJobNotice> depreciationJobNoticeList = depreciationJobNoticeRepository.findAll();
        assertThat(depreciationJobNoticeList).hasSize(databaseSizeBeforeTest);
    }

    // @Test
    @Transactional
    void checkDepreciationNoticeStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = depreciationJobNoticeRepository.findAll().size();
        // set the field null
        depreciationJobNotice.setDepreciationNoticeStatus(null);

        // Create the DepreciationJobNotice, which fails.
        DepreciationJobNoticeDTO depreciationJobNoticeDTO = depreciationJobNoticeMapper.toDto(depreciationJobNotice);

        restDepreciationJobNoticeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(depreciationJobNoticeDTO))
            )
            .andExpect(status().isBadRequest());

        List<DepreciationJobNotice> depreciationJobNoticeList = depreciationJobNoticeRepository.findAll();
        assertThat(depreciationJobNoticeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDepreciationJobNotices() throws Exception {
        // Initialize the database
        depreciationJobNoticeRepository.saveAndFlush(depreciationJobNotice);

        // Get all the depreciationJobNoticeList
        restDepreciationJobNoticeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(depreciationJobNotice.getId().intValue())))
            .andExpect(jsonPath("$.[*].eventNarrative").value(hasItem(DEFAULT_EVENT_NARRATIVE)))
            .andExpect(jsonPath("$.[*].eventTimeStamp").value(hasItem(sameInstant(DEFAULT_EVENT_TIME_STAMP))))
            .andExpect(jsonPath("$.[*].depreciationNoticeStatus").value(hasItem(DEFAULT_DEPRECIATION_NOTICE_STATUS.toString())))
            .andExpect(jsonPath("$.[*].sourceModule").value(hasItem(DEFAULT_SOURCE_MODULE)))
            .andExpect(jsonPath("$.[*].sourceEntity").value(hasItem(DEFAULT_SOURCE_ENTITY)))
            .andExpect(jsonPath("$.[*].errorCode").value(hasItem(DEFAULT_ERROR_CODE)))
            .andExpect(jsonPath("$.[*].errorMessage").value(hasItem(DEFAULT_ERROR_MESSAGE.toString())))
            .andExpect(jsonPath("$.[*].userAction").value(hasItem(DEFAULT_USER_ACTION)))
            .andExpect(jsonPath("$.[*].technicalDetails").value(hasItem(DEFAULT_TECHNICAL_DETAILS.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllDepreciationJobNoticesWithEagerRelationshipsIsEnabled() throws Exception {
        when(depreciationJobNoticeServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restDepreciationJobNoticeMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(depreciationJobNoticeServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllDepreciationJobNoticesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(depreciationJobNoticeServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restDepreciationJobNoticeMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(depreciationJobNoticeServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getDepreciationJobNotice() throws Exception {
        // Initialize the database
        depreciationJobNoticeRepository.saveAndFlush(depreciationJobNotice);

        // Get the depreciationJobNotice
        restDepreciationJobNoticeMockMvc
            .perform(get(ENTITY_API_URL_ID, depreciationJobNotice.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(depreciationJobNotice.getId().intValue()))
            .andExpect(jsonPath("$.eventNarrative").value(DEFAULT_EVENT_NARRATIVE))
            .andExpect(jsonPath("$.eventTimeStamp").value(sameInstant(DEFAULT_EVENT_TIME_STAMP)))
            .andExpect(jsonPath("$.depreciationNoticeStatus").value(DEFAULT_DEPRECIATION_NOTICE_STATUS.toString()))
            .andExpect(jsonPath("$.sourceModule").value(DEFAULT_SOURCE_MODULE))
            .andExpect(jsonPath("$.sourceEntity").value(DEFAULT_SOURCE_ENTITY))
            .andExpect(jsonPath("$.errorCode").value(DEFAULT_ERROR_CODE))
            .andExpect(jsonPath("$.errorMessage").value(DEFAULT_ERROR_MESSAGE.toString()))
            .andExpect(jsonPath("$.userAction").value(DEFAULT_USER_ACTION))
            .andExpect(jsonPath("$.technicalDetails").value(DEFAULT_TECHNICAL_DETAILS.toString()));
    }

    @Test
    @Transactional
    void getNonExistingDepreciationJobNotice() throws Exception {
        // Get the depreciationJobNotice
        restDepreciationJobNoticeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewDepreciationJobNotice() throws Exception {
        // Initialize the database
        depreciationJobNoticeRepository.saveAndFlush(depreciationJobNotice);

        int databaseSizeBeforeUpdate = depreciationJobNoticeRepository.findAll().size();

        // Update the depreciationJobNotice
        DepreciationJobNotice updatedDepreciationJobNotice = depreciationJobNoticeRepository.findById(depreciationJobNotice.getId()).get();
        // Disconnect from session so that the updates on updatedDepreciationJobNotice are not directly saved in db
        em.detach(updatedDepreciationJobNotice);
        updatedDepreciationJobNotice
            .eventNarrative(UPDATED_EVENT_NARRATIVE)
            .eventTimeStamp(UPDATED_EVENT_TIME_STAMP)
            .depreciationNoticeStatus(UPDATED_DEPRECIATION_NOTICE_STATUS)
            .sourceModule(UPDATED_SOURCE_MODULE)
            .sourceEntity(UPDATED_SOURCE_ENTITY)
            .errorCode(UPDATED_ERROR_CODE)
            .errorMessage(UPDATED_ERROR_MESSAGE)
            .userAction(UPDATED_USER_ACTION)
            .technicalDetails(UPDATED_TECHNICAL_DETAILS);
        DepreciationJobNoticeDTO depreciationJobNoticeDTO = depreciationJobNoticeMapper.toDto(updatedDepreciationJobNotice);

        restDepreciationJobNoticeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, depreciationJobNoticeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(depreciationJobNoticeDTO))
            )
            .andExpect(status().isOk());

        // Validate the DepreciationJobNotice in the database
        List<DepreciationJobNotice> depreciationJobNoticeList = depreciationJobNoticeRepository.findAll();
        assertThat(depreciationJobNoticeList).hasSize(databaseSizeBeforeUpdate);
        DepreciationJobNotice testDepreciationJobNotice = depreciationJobNoticeList.get(depreciationJobNoticeList.size() - 1);
        assertThat(testDepreciationJobNotice.getEventNarrative()).isEqualTo(UPDATED_EVENT_NARRATIVE);
        assertThat(testDepreciationJobNotice.getEventTimeStamp()).isEqualTo(UPDATED_EVENT_TIME_STAMP);
        assertThat(testDepreciationJobNotice.getDepreciationNoticeStatus()).isEqualTo(UPDATED_DEPRECIATION_NOTICE_STATUS);
        assertThat(testDepreciationJobNotice.getSourceModule()).isEqualTo(UPDATED_SOURCE_MODULE);
        assertThat(testDepreciationJobNotice.getSourceEntity()).isEqualTo(UPDATED_SOURCE_ENTITY);
        assertThat(testDepreciationJobNotice.getErrorCode()).isEqualTo(UPDATED_ERROR_CODE);
        assertThat(testDepreciationJobNotice.getErrorMessage()).isEqualTo(UPDATED_ERROR_MESSAGE);
        assertThat(testDepreciationJobNotice.getUserAction()).isEqualTo(UPDATED_USER_ACTION);
        assertThat(testDepreciationJobNotice.getTechnicalDetails()).isEqualTo(UPDATED_TECHNICAL_DETAILS);

        // Validate the DepreciationJobNotice in Elasticsearch
        verify(mockDepreciationJobNoticeSearchRepository).save(testDepreciationJobNotice);
    }

    @Test
    @Transactional
    void putNonExistingDepreciationJobNotice() throws Exception {
        int databaseSizeBeforeUpdate = depreciationJobNoticeRepository.findAll().size();
        depreciationJobNotice.setId(count.incrementAndGet());

        // Create the DepreciationJobNotice
        DepreciationJobNoticeDTO depreciationJobNoticeDTO = depreciationJobNoticeMapper.toDto(depreciationJobNotice);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDepreciationJobNoticeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, depreciationJobNoticeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(depreciationJobNoticeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DepreciationJobNotice in the database
        List<DepreciationJobNotice> depreciationJobNoticeList = depreciationJobNoticeRepository.findAll();
        assertThat(depreciationJobNoticeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the DepreciationJobNotice in Elasticsearch
        verify(mockDepreciationJobNoticeSearchRepository, times(0)).save(depreciationJobNotice);
    }

    @Test
    @Transactional
    void putWithIdMismatchDepreciationJobNotice() throws Exception {
        int databaseSizeBeforeUpdate = depreciationJobNoticeRepository.findAll().size();
        depreciationJobNotice.setId(count.incrementAndGet());

        // Create the DepreciationJobNotice
        DepreciationJobNoticeDTO depreciationJobNoticeDTO = depreciationJobNoticeMapper.toDto(depreciationJobNotice);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDepreciationJobNoticeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(depreciationJobNoticeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DepreciationJobNotice in the database
        List<DepreciationJobNotice> depreciationJobNoticeList = depreciationJobNoticeRepository.findAll();
        assertThat(depreciationJobNoticeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the DepreciationJobNotice in Elasticsearch
        verify(mockDepreciationJobNoticeSearchRepository, times(0)).save(depreciationJobNotice);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDepreciationJobNotice() throws Exception {
        int databaseSizeBeforeUpdate = depreciationJobNoticeRepository.findAll().size();
        depreciationJobNotice.setId(count.incrementAndGet());

        // Create the DepreciationJobNotice
        DepreciationJobNoticeDTO depreciationJobNoticeDTO = depreciationJobNoticeMapper.toDto(depreciationJobNotice);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDepreciationJobNoticeMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(depreciationJobNoticeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DepreciationJobNotice in the database
        List<DepreciationJobNotice> depreciationJobNoticeList = depreciationJobNoticeRepository.findAll();
        assertThat(depreciationJobNoticeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the DepreciationJobNotice in Elasticsearch
        verify(mockDepreciationJobNoticeSearchRepository, times(0)).save(depreciationJobNotice);
    }

    @Test
    @Transactional
    void partialUpdateDepreciationJobNoticeWithPatch() throws Exception {
        // Initialize the database
        depreciationJobNoticeRepository.saveAndFlush(depreciationJobNotice);

        int databaseSizeBeforeUpdate = depreciationJobNoticeRepository.findAll().size();

        // Update the depreciationJobNotice using partial update
        DepreciationJobNotice partialUpdatedDepreciationJobNotice = new DepreciationJobNotice();
        partialUpdatedDepreciationJobNotice.setId(depreciationJobNotice.getId());

        partialUpdatedDepreciationJobNotice
            .errorCode(UPDATED_ERROR_CODE)
            .errorMessage(UPDATED_ERROR_MESSAGE)
            .technicalDetails(UPDATED_TECHNICAL_DETAILS);

        restDepreciationJobNoticeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDepreciationJobNotice.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDepreciationJobNotice))
            )
            .andExpect(status().isOk());

        // Validate the DepreciationJobNotice in the database
        List<DepreciationJobNotice> depreciationJobNoticeList = depreciationJobNoticeRepository.findAll();
        assertThat(depreciationJobNoticeList).hasSize(databaseSizeBeforeUpdate);
        DepreciationJobNotice testDepreciationJobNotice = depreciationJobNoticeList.get(depreciationJobNoticeList.size() - 1);
        assertThat(testDepreciationJobNotice.getEventNarrative()).isEqualTo(DEFAULT_EVENT_NARRATIVE);
        assertThat(testDepreciationJobNotice.getEventTimeStamp()).isEqualTo(DEFAULT_EVENT_TIME_STAMP);
        assertThat(testDepreciationJobNotice.getDepreciationNoticeStatus()).isEqualTo(DEFAULT_DEPRECIATION_NOTICE_STATUS);
        assertThat(testDepreciationJobNotice.getSourceModule()).isEqualTo(DEFAULT_SOURCE_MODULE);
        assertThat(testDepreciationJobNotice.getSourceEntity()).isEqualTo(DEFAULT_SOURCE_ENTITY);
        assertThat(testDepreciationJobNotice.getErrorCode()).isEqualTo(UPDATED_ERROR_CODE);
        assertThat(testDepreciationJobNotice.getErrorMessage()).isEqualTo(UPDATED_ERROR_MESSAGE);
        assertThat(testDepreciationJobNotice.getUserAction()).isEqualTo(DEFAULT_USER_ACTION);
        assertThat(testDepreciationJobNotice.getTechnicalDetails()).isEqualTo(UPDATED_TECHNICAL_DETAILS);
    }

    @Test
    @Transactional
    void fullUpdateDepreciationJobNoticeWithPatch() throws Exception {
        // Initialize the database
        depreciationJobNoticeRepository.saveAndFlush(depreciationJobNotice);

        int databaseSizeBeforeUpdate = depreciationJobNoticeRepository.findAll().size();

        // Update the depreciationJobNotice using partial update
        DepreciationJobNotice partialUpdatedDepreciationJobNotice = new DepreciationJobNotice();
        partialUpdatedDepreciationJobNotice.setId(depreciationJobNotice.getId());

        partialUpdatedDepreciationJobNotice
            .eventNarrative(UPDATED_EVENT_NARRATIVE)
            .eventTimeStamp(UPDATED_EVENT_TIME_STAMP)
            .depreciationNoticeStatus(UPDATED_DEPRECIATION_NOTICE_STATUS)
            .sourceModule(UPDATED_SOURCE_MODULE)
            .sourceEntity(UPDATED_SOURCE_ENTITY)
            .errorCode(UPDATED_ERROR_CODE)
            .errorMessage(UPDATED_ERROR_MESSAGE)
            .userAction(UPDATED_USER_ACTION)
            .technicalDetails(UPDATED_TECHNICAL_DETAILS);

        restDepreciationJobNoticeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDepreciationJobNotice.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDepreciationJobNotice))
            )
            .andExpect(status().isOk());

        // Validate the DepreciationJobNotice in the database
        List<DepreciationJobNotice> depreciationJobNoticeList = depreciationJobNoticeRepository.findAll();
        assertThat(depreciationJobNoticeList).hasSize(databaseSizeBeforeUpdate);
        DepreciationJobNotice testDepreciationJobNotice = depreciationJobNoticeList.get(depreciationJobNoticeList.size() - 1);
        assertThat(testDepreciationJobNotice.getEventNarrative()).isEqualTo(UPDATED_EVENT_NARRATIVE);
        assertThat(testDepreciationJobNotice.getEventTimeStamp()).isEqualTo(UPDATED_EVENT_TIME_STAMP);
        assertThat(testDepreciationJobNotice.getDepreciationNoticeStatus()).isEqualTo(UPDATED_DEPRECIATION_NOTICE_STATUS);
        assertThat(testDepreciationJobNotice.getSourceModule()).isEqualTo(UPDATED_SOURCE_MODULE);
        assertThat(testDepreciationJobNotice.getSourceEntity()).isEqualTo(UPDATED_SOURCE_ENTITY);
        assertThat(testDepreciationJobNotice.getErrorCode()).isEqualTo(UPDATED_ERROR_CODE);
        assertThat(testDepreciationJobNotice.getErrorMessage()).isEqualTo(UPDATED_ERROR_MESSAGE);
        assertThat(testDepreciationJobNotice.getUserAction()).isEqualTo(UPDATED_USER_ACTION);
        assertThat(testDepreciationJobNotice.getTechnicalDetails()).isEqualTo(UPDATED_TECHNICAL_DETAILS);
    }

    @Test
    @Transactional
    void patchNonExistingDepreciationJobNotice() throws Exception {
        int databaseSizeBeforeUpdate = depreciationJobNoticeRepository.findAll().size();
        depreciationJobNotice.setId(count.incrementAndGet());

        // Create the DepreciationJobNotice
        DepreciationJobNoticeDTO depreciationJobNoticeDTO = depreciationJobNoticeMapper.toDto(depreciationJobNotice);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDepreciationJobNoticeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, depreciationJobNoticeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(depreciationJobNoticeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DepreciationJobNotice in the database
        List<DepreciationJobNotice> depreciationJobNoticeList = depreciationJobNoticeRepository.findAll();
        assertThat(depreciationJobNoticeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the DepreciationJobNotice in Elasticsearch
        verify(mockDepreciationJobNoticeSearchRepository, times(0)).save(depreciationJobNotice);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDepreciationJobNotice() throws Exception {
        int databaseSizeBeforeUpdate = depreciationJobNoticeRepository.findAll().size();
        depreciationJobNotice.setId(count.incrementAndGet());

        // Create the DepreciationJobNotice
        DepreciationJobNoticeDTO depreciationJobNoticeDTO = depreciationJobNoticeMapper.toDto(depreciationJobNotice);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDepreciationJobNoticeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(depreciationJobNoticeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DepreciationJobNotice in the database
        List<DepreciationJobNotice> depreciationJobNoticeList = depreciationJobNoticeRepository.findAll();
        assertThat(depreciationJobNoticeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the DepreciationJobNotice in Elasticsearch
        verify(mockDepreciationJobNoticeSearchRepository, times(0)).save(depreciationJobNotice);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDepreciationJobNotice() throws Exception {
        int databaseSizeBeforeUpdate = depreciationJobNoticeRepository.findAll().size();
        depreciationJobNotice.setId(count.incrementAndGet());

        // Create the DepreciationJobNotice
        DepreciationJobNoticeDTO depreciationJobNoticeDTO = depreciationJobNoticeMapper.toDto(depreciationJobNotice);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDepreciationJobNoticeMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(depreciationJobNoticeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DepreciationJobNotice in the database
        List<DepreciationJobNotice> depreciationJobNoticeList = depreciationJobNoticeRepository.findAll();
        assertThat(depreciationJobNoticeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the DepreciationJobNotice in Elasticsearch
        verify(mockDepreciationJobNoticeSearchRepository, times(0)).save(depreciationJobNotice);
    }

    @Test
    @Transactional
    void deleteDepreciationJobNotice() throws Exception {
        // Initialize the database
        depreciationJobNoticeRepository.saveAndFlush(depreciationJobNotice);

        int databaseSizeBeforeDelete = depreciationJobNoticeRepository.findAll().size();

        // Delete the depreciationJobNotice
        restDepreciationJobNoticeMockMvc
            .perform(delete(ENTITY_API_URL_ID, depreciationJobNotice.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<DepreciationJobNotice> depreciationJobNoticeList = depreciationJobNoticeRepository.findAll();
        assertThat(depreciationJobNoticeList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the DepreciationJobNotice in Elasticsearch
        verify(mockDepreciationJobNoticeSearchRepository, times(1)).deleteById(depreciationJobNotice.getId());
    }

    @Test
    @Transactional
    void searchDepreciationJobNotice() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        depreciationJobNoticeRepository.saveAndFlush(depreciationJobNotice);
        when(mockDepreciationJobNoticeSearchRepository.search("id:" + depreciationJobNotice.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(depreciationJobNotice), PageRequest.of(0, 1), 1));

        // Search the depreciationJobNotice
        restDepreciationJobNoticeMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + depreciationJobNotice.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(depreciationJobNotice.getId().intValue())))
            .andExpect(jsonPath("$.[*].eventNarrative").value(hasItem(DEFAULT_EVENT_NARRATIVE)))
            .andExpect(jsonPath("$.[*].eventTimeStamp").value(hasItem(sameInstant(DEFAULT_EVENT_TIME_STAMP))))
            .andExpect(jsonPath("$.[*].depreciationNoticeStatus").value(hasItem(DEFAULT_DEPRECIATION_NOTICE_STATUS.toString())))
            .andExpect(jsonPath("$.[*].sourceModule").value(hasItem(DEFAULT_SOURCE_MODULE)))
            .andExpect(jsonPath("$.[*].sourceEntity").value(hasItem(DEFAULT_SOURCE_ENTITY)))
            .andExpect(jsonPath("$.[*].errorCode").value(hasItem(DEFAULT_ERROR_CODE)))
            .andExpect(jsonPath("$.[*].errorMessage").value(hasItem(DEFAULT_ERROR_MESSAGE.toString())))
            .andExpect(jsonPath("$.[*].userAction").value(hasItem(DEFAULT_USER_ACTION)))
            .andExpect(jsonPath("$.[*].technicalDetails").value(hasItem(DEFAULT_TECHNICAL_DETAILS.toString())));
    }
}
