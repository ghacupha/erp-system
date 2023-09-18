package io.github.erp.web.rest;

/*-
 * Erp System - Mark V No 8 (Ehud Series) Server ver 1.5.1
 * Copyright © 2021 - 2023 Edwin Njeru (mailnjeru@gmail.com)
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
import io.github.erp.domain.ApplicationUser;
import io.github.erp.domain.DepreciationBatchSequence;
import io.github.erp.domain.DepreciationJob;
import io.github.erp.domain.DepreciationJobNotice;
import io.github.erp.domain.DepreciationPeriod;
import io.github.erp.domain.Placeholder;
import io.github.erp.domain.UniversallyUniqueMapping;
import io.github.erp.domain.enumeration.DepreciationNoticeStatusType;
import io.github.erp.repository.DepreciationJobNoticeRepository;
import io.github.erp.repository.search.DepreciationJobNoticeSearchRepository;
import io.github.erp.service.DepreciationJobNoticeService;
import io.github.erp.service.criteria.DepreciationJobNoticeCriteria;
import io.github.erp.service.dto.DepreciationJobNoticeDTO;
import io.github.erp.service.mapper.DepreciationJobNoticeMapper;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
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
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link DepreciationJobNoticeResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class DepreciationJobNoticeResourceIT {

    private static final String DEFAULT_EVENT_NARRATIVE = "AAAAAAAAAA";
    private static final String UPDATED_EVENT_NARRATIVE = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_EVENT_TIME_STAMP = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_EVENT_TIME_STAMP = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_EVENT_TIME_STAMP = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

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

    private static final String ENTITY_API_URL = "/api/depreciation-job-notices";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/depreciation-job-notices";

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

    @Test
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

    @Test
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

    @Test
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
    void getDepreciationJobNoticesByIdFiltering() throws Exception {
        // Initialize the database
        depreciationJobNoticeRepository.saveAndFlush(depreciationJobNotice);

        Long id = depreciationJobNotice.getId();

        defaultDepreciationJobNoticeShouldBeFound("id.equals=" + id);
        defaultDepreciationJobNoticeShouldNotBeFound("id.notEquals=" + id);

        defaultDepreciationJobNoticeShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultDepreciationJobNoticeShouldNotBeFound("id.greaterThan=" + id);

        defaultDepreciationJobNoticeShouldBeFound("id.lessThanOrEqual=" + id);
        defaultDepreciationJobNoticeShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllDepreciationJobNoticesByEventNarrativeIsEqualToSomething() throws Exception {
        // Initialize the database
        depreciationJobNoticeRepository.saveAndFlush(depreciationJobNotice);

        // Get all the depreciationJobNoticeList where eventNarrative equals to DEFAULT_EVENT_NARRATIVE
        defaultDepreciationJobNoticeShouldBeFound("eventNarrative.equals=" + DEFAULT_EVENT_NARRATIVE);

        // Get all the depreciationJobNoticeList where eventNarrative equals to UPDATED_EVENT_NARRATIVE
        defaultDepreciationJobNoticeShouldNotBeFound("eventNarrative.equals=" + UPDATED_EVENT_NARRATIVE);
    }

    @Test
    @Transactional
    void getAllDepreciationJobNoticesByEventNarrativeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        depreciationJobNoticeRepository.saveAndFlush(depreciationJobNotice);

        // Get all the depreciationJobNoticeList where eventNarrative not equals to DEFAULT_EVENT_NARRATIVE
        defaultDepreciationJobNoticeShouldNotBeFound("eventNarrative.notEquals=" + DEFAULT_EVENT_NARRATIVE);

        // Get all the depreciationJobNoticeList where eventNarrative not equals to UPDATED_EVENT_NARRATIVE
        defaultDepreciationJobNoticeShouldBeFound("eventNarrative.notEquals=" + UPDATED_EVENT_NARRATIVE);
    }

    @Test
    @Transactional
    void getAllDepreciationJobNoticesByEventNarrativeIsInShouldWork() throws Exception {
        // Initialize the database
        depreciationJobNoticeRepository.saveAndFlush(depreciationJobNotice);

        // Get all the depreciationJobNoticeList where eventNarrative in DEFAULT_EVENT_NARRATIVE or UPDATED_EVENT_NARRATIVE
        defaultDepreciationJobNoticeShouldBeFound("eventNarrative.in=" + DEFAULT_EVENT_NARRATIVE + "," + UPDATED_EVENT_NARRATIVE);

        // Get all the depreciationJobNoticeList where eventNarrative equals to UPDATED_EVENT_NARRATIVE
        defaultDepreciationJobNoticeShouldNotBeFound("eventNarrative.in=" + UPDATED_EVENT_NARRATIVE);
    }

    @Test
    @Transactional
    void getAllDepreciationJobNoticesByEventNarrativeIsNullOrNotNull() throws Exception {
        // Initialize the database
        depreciationJobNoticeRepository.saveAndFlush(depreciationJobNotice);

        // Get all the depreciationJobNoticeList where eventNarrative is not null
        defaultDepreciationJobNoticeShouldBeFound("eventNarrative.specified=true");

        // Get all the depreciationJobNoticeList where eventNarrative is null
        defaultDepreciationJobNoticeShouldNotBeFound("eventNarrative.specified=false");
    }

    @Test
    @Transactional
    void getAllDepreciationJobNoticesByEventNarrativeContainsSomething() throws Exception {
        // Initialize the database
        depreciationJobNoticeRepository.saveAndFlush(depreciationJobNotice);

        // Get all the depreciationJobNoticeList where eventNarrative contains DEFAULT_EVENT_NARRATIVE
        defaultDepreciationJobNoticeShouldBeFound("eventNarrative.contains=" + DEFAULT_EVENT_NARRATIVE);

        // Get all the depreciationJobNoticeList where eventNarrative contains UPDATED_EVENT_NARRATIVE
        defaultDepreciationJobNoticeShouldNotBeFound("eventNarrative.contains=" + UPDATED_EVENT_NARRATIVE);
    }

    @Test
    @Transactional
    void getAllDepreciationJobNoticesByEventNarrativeNotContainsSomething() throws Exception {
        // Initialize the database
        depreciationJobNoticeRepository.saveAndFlush(depreciationJobNotice);

        // Get all the depreciationJobNoticeList where eventNarrative does not contain DEFAULT_EVENT_NARRATIVE
        defaultDepreciationJobNoticeShouldNotBeFound("eventNarrative.doesNotContain=" + DEFAULT_EVENT_NARRATIVE);

        // Get all the depreciationJobNoticeList where eventNarrative does not contain UPDATED_EVENT_NARRATIVE
        defaultDepreciationJobNoticeShouldBeFound("eventNarrative.doesNotContain=" + UPDATED_EVENT_NARRATIVE);
    }

    @Test
    @Transactional
    void getAllDepreciationJobNoticesByEventTimeStampIsEqualToSomething() throws Exception {
        // Initialize the database
        depreciationJobNoticeRepository.saveAndFlush(depreciationJobNotice);

        // Get all the depreciationJobNoticeList where eventTimeStamp equals to DEFAULT_EVENT_TIME_STAMP
        defaultDepreciationJobNoticeShouldBeFound("eventTimeStamp.equals=" + DEFAULT_EVENT_TIME_STAMP);

        // Get all the depreciationJobNoticeList where eventTimeStamp equals to UPDATED_EVENT_TIME_STAMP
        defaultDepreciationJobNoticeShouldNotBeFound("eventTimeStamp.equals=" + UPDATED_EVENT_TIME_STAMP);
    }

    @Test
    @Transactional
    void getAllDepreciationJobNoticesByEventTimeStampIsNotEqualToSomething() throws Exception {
        // Initialize the database
        depreciationJobNoticeRepository.saveAndFlush(depreciationJobNotice);

        // Get all the depreciationJobNoticeList where eventTimeStamp not equals to DEFAULT_EVENT_TIME_STAMP
        defaultDepreciationJobNoticeShouldNotBeFound("eventTimeStamp.notEquals=" + DEFAULT_EVENT_TIME_STAMP);

        // Get all the depreciationJobNoticeList where eventTimeStamp not equals to UPDATED_EVENT_TIME_STAMP
        defaultDepreciationJobNoticeShouldBeFound("eventTimeStamp.notEquals=" + UPDATED_EVENT_TIME_STAMP);
    }

    @Test
    @Transactional
    void getAllDepreciationJobNoticesByEventTimeStampIsInShouldWork() throws Exception {
        // Initialize the database
        depreciationJobNoticeRepository.saveAndFlush(depreciationJobNotice);

        // Get all the depreciationJobNoticeList where eventTimeStamp in DEFAULT_EVENT_TIME_STAMP or UPDATED_EVENT_TIME_STAMP
        defaultDepreciationJobNoticeShouldBeFound("eventTimeStamp.in=" + DEFAULT_EVENT_TIME_STAMP + "," + UPDATED_EVENT_TIME_STAMP);

        // Get all the depreciationJobNoticeList where eventTimeStamp equals to UPDATED_EVENT_TIME_STAMP
        defaultDepreciationJobNoticeShouldNotBeFound("eventTimeStamp.in=" + UPDATED_EVENT_TIME_STAMP);
    }

    @Test
    @Transactional
    void getAllDepreciationJobNoticesByEventTimeStampIsNullOrNotNull() throws Exception {
        // Initialize the database
        depreciationJobNoticeRepository.saveAndFlush(depreciationJobNotice);

        // Get all the depreciationJobNoticeList where eventTimeStamp is not null
        defaultDepreciationJobNoticeShouldBeFound("eventTimeStamp.specified=true");

        // Get all the depreciationJobNoticeList where eventTimeStamp is null
        defaultDepreciationJobNoticeShouldNotBeFound("eventTimeStamp.specified=false");
    }

    @Test
    @Transactional
    void getAllDepreciationJobNoticesByEventTimeStampIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        depreciationJobNoticeRepository.saveAndFlush(depreciationJobNotice);

        // Get all the depreciationJobNoticeList where eventTimeStamp is greater than or equal to DEFAULT_EVENT_TIME_STAMP
        defaultDepreciationJobNoticeShouldBeFound("eventTimeStamp.greaterThanOrEqual=" + DEFAULT_EVENT_TIME_STAMP);

        // Get all the depreciationJobNoticeList where eventTimeStamp is greater than or equal to UPDATED_EVENT_TIME_STAMP
        defaultDepreciationJobNoticeShouldNotBeFound("eventTimeStamp.greaterThanOrEqual=" + UPDATED_EVENT_TIME_STAMP);
    }

    @Test
    @Transactional
    void getAllDepreciationJobNoticesByEventTimeStampIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        depreciationJobNoticeRepository.saveAndFlush(depreciationJobNotice);

        // Get all the depreciationJobNoticeList where eventTimeStamp is less than or equal to DEFAULT_EVENT_TIME_STAMP
        defaultDepreciationJobNoticeShouldBeFound("eventTimeStamp.lessThanOrEqual=" + DEFAULT_EVENT_TIME_STAMP);

        // Get all the depreciationJobNoticeList where eventTimeStamp is less than or equal to SMALLER_EVENT_TIME_STAMP
        defaultDepreciationJobNoticeShouldNotBeFound("eventTimeStamp.lessThanOrEqual=" + SMALLER_EVENT_TIME_STAMP);
    }

    @Test
    @Transactional
    void getAllDepreciationJobNoticesByEventTimeStampIsLessThanSomething() throws Exception {
        // Initialize the database
        depreciationJobNoticeRepository.saveAndFlush(depreciationJobNotice);

        // Get all the depreciationJobNoticeList where eventTimeStamp is less than DEFAULT_EVENT_TIME_STAMP
        defaultDepreciationJobNoticeShouldNotBeFound("eventTimeStamp.lessThan=" + DEFAULT_EVENT_TIME_STAMP);

        // Get all the depreciationJobNoticeList where eventTimeStamp is less than UPDATED_EVENT_TIME_STAMP
        defaultDepreciationJobNoticeShouldBeFound("eventTimeStamp.lessThan=" + UPDATED_EVENT_TIME_STAMP);
    }

    @Test
    @Transactional
    void getAllDepreciationJobNoticesByEventTimeStampIsGreaterThanSomething() throws Exception {
        // Initialize the database
        depreciationJobNoticeRepository.saveAndFlush(depreciationJobNotice);

        // Get all the depreciationJobNoticeList where eventTimeStamp is greater than DEFAULT_EVENT_TIME_STAMP
        defaultDepreciationJobNoticeShouldNotBeFound("eventTimeStamp.greaterThan=" + DEFAULT_EVENT_TIME_STAMP);

        // Get all the depreciationJobNoticeList where eventTimeStamp is greater than SMALLER_EVENT_TIME_STAMP
        defaultDepreciationJobNoticeShouldBeFound("eventTimeStamp.greaterThan=" + SMALLER_EVENT_TIME_STAMP);
    }

    @Test
    @Transactional
    void getAllDepreciationJobNoticesByDepreciationNoticeStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        depreciationJobNoticeRepository.saveAndFlush(depreciationJobNotice);

        // Get all the depreciationJobNoticeList where depreciationNoticeStatus equals to DEFAULT_DEPRECIATION_NOTICE_STATUS
        defaultDepreciationJobNoticeShouldBeFound("depreciationNoticeStatus.equals=" + DEFAULT_DEPRECIATION_NOTICE_STATUS);

        // Get all the depreciationJobNoticeList where depreciationNoticeStatus equals to UPDATED_DEPRECIATION_NOTICE_STATUS
        defaultDepreciationJobNoticeShouldNotBeFound("depreciationNoticeStatus.equals=" + UPDATED_DEPRECIATION_NOTICE_STATUS);
    }

    @Test
    @Transactional
    void getAllDepreciationJobNoticesByDepreciationNoticeStatusIsNotEqualToSomething() throws Exception {
        // Initialize the database
        depreciationJobNoticeRepository.saveAndFlush(depreciationJobNotice);

        // Get all the depreciationJobNoticeList where depreciationNoticeStatus not equals to DEFAULT_DEPRECIATION_NOTICE_STATUS
        defaultDepreciationJobNoticeShouldNotBeFound("depreciationNoticeStatus.notEquals=" + DEFAULT_DEPRECIATION_NOTICE_STATUS);

        // Get all the depreciationJobNoticeList where depreciationNoticeStatus not equals to UPDATED_DEPRECIATION_NOTICE_STATUS
        defaultDepreciationJobNoticeShouldBeFound("depreciationNoticeStatus.notEquals=" + UPDATED_DEPRECIATION_NOTICE_STATUS);
    }

    @Test
    @Transactional
    void getAllDepreciationJobNoticesByDepreciationNoticeStatusIsInShouldWork() throws Exception {
        // Initialize the database
        depreciationJobNoticeRepository.saveAndFlush(depreciationJobNotice);

        // Get all the depreciationJobNoticeList where depreciationNoticeStatus in DEFAULT_DEPRECIATION_NOTICE_STATUS or UPDATED_DEPRECIATION_NOTICE_STATUS
        defaultDepreciationJobNoticeShouldBeFound(
            "depreciationNoticeStatus.in=" + DEFAULT_DEPRECIATION_NOTICE_STATUS + "," + UPDATED_DEPRECIATION_NOTICE_STATUS
        );

        // Get all the depreciationJobNoticeList where depreciationNoticeStatus equals to UPDATED_DEPRECIATION_NOTICE_STATUS
        defaultDepreciationJobNoticeShouldNotBeFound("depreciationNoticeStatus.in=" + UPDATED_DEPRECIATION_NOTICE_STATUS);
    }

    @Test
    @Transactional
    void getAllDepreciationJobNoticesByDepreciationNoticeStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        depreciationJobNoticeRepository.saveAndFlush(depreciationJobNotice);

        // Get all the depreciationJobNoticeList where depreciationNoticeStatus is not null
        defaultDepreciationJobNoticeShouldBeFound("depreciationNoticeStatus.specified=true");

        // Get all the depreciationJobNoticeList where depreciationNoticeStatus is null
        defaultDepreciationJobNoticeShouldNotBeFound("depreciationNoticeStatus.specified=false");
    }

    @Test
    @Transactional
    void getAllDepreciationJobNoticesBySourceModuleIsEqualToSomething() throws Exception {
        // Initialize the database
        depreciationJobNoticeRepository.saveAndFlush(depreciationJobNotice);

        // Get all the depreciationJobNoticeList where sourceModule equals to DEFAULT_SOURCE_MODULE
        defaultDepreciationJobNoticeShouldBeFound("sourceModule.equals=" + DEFAULT_SOURCE_MODULE);

        // Get all the depreciationJobNoticeList where sourceModule equals to UPDATED_SOURCE_MODULE
        defaultDepreciationJobNoticeShouldNotBeFound("sourceModule.equals=" + UPDATED_SOURCE_MODULE);
    }

    @Test
    @Transactional
    void getAllDepreciationJobNoticesBySourceModuleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        depreciationJobNoticeRepository.saveAndFlush(depreciationJobNotice);

        // Get all the depreciationJobNoticeList where sourceModule not equals to DEFAULT_SOURCE_MODULE
        defaultDepreciationJobNoticeShouldNotBeFound("sourceModule.notEquals=" + DEFAULT_SOURCE_MODULE);

        // Get all the depreciationJobNoticeList where sourceModule not equals to UPDATED_SOURCE_MODULE
        defaultDepreciationJobNoticeShouldBeFound("sourceModule.notEquals=" + UPDATED_SOURCE_MODULE);
    }

    @Test
    @Transactional
    void getAllDepreciationJobNoticesBySourceModuleIsInShouldWork() throws Exception {
        // Initialize the database
        depreciationJobNoticeRepository.saveAndFlush(depreciationJobNotice);

        // Get all the depreciationJobNoticeList where sourceModule in DEFAULT_SOURCE_MODULE or UPDATED_SOURCE_MODULE
        defaultDepreciationJobNoticeShouldBeFound("sourceModule.in=" + DEFAULT_SOURCE_MODULE + "," + UPDATED_SOURCE_MODULE);

        // Get all the depreciationJobNoticeList where sourceModule equals to UPDATED_SOURCE_MODULE
        defaultDepreciationJobNoticeShouldNotBeFound("sourceModule.in=" + UPDATED_SOURCE_MODULE);
    }

    @Test
    @Transactional
    void getAllDepreciationJobNoticesBySourceModuleIsNullOrNotNull() throws Exception {
        // Initialize the database
        depreciationJobNoticeRepository.saveAndFlush(depreciationJobNotice);

        // Get all the depreciationJobNoticeList where sourceModule is not null
        defaultDepreciationJobNoticeShouldBeFound("sourceModule.specified=true");

        // Get all the depreciationJobNoticeList where sourceModule is null
        defaultDepreciationJobNoticeShouldNotBeFound("sourceModule.specified=false");
    }

    @Test
    @Transactional
    void getAllDepreciationJobNoticesBySourceModuleContainsSomething() throws Exception {
        // Initialize the database
        depreciationJobNoticeRepository.saveAndFlush(depreciationJobNotice);

        // Get all the depreciationJobNoticeList where sourceModule contains DEFAULT_SOURCE_MODULE
        defaultDepreciationJobNoticeShouldBeFound("sourceModule.contains=" + DEFAULT_SOURCE_MODULE);

        // Get all the depreciationJobNoticeList where sourceModule contains UPDATED_SOURCE_MODULE
        defaultDepreciationJobNoticeShouldNotBeFound("sourceModule.contains=" + UPDATED_SOURCE_MODULE);
    }

    @Test
    @Transactional
    void getAllDepreciationJobNoticesBySourceModuleNotContainsSomething() throws Exception {
        // Initialize the database
        depreciationJobNoticeRepository.saveAndFlush(depreciationJobNotice);

        // Get all the depreciationJobNoticeList where sourceModule does not contain DEFAULT_SOURCE_MODULE
        defaultDepreciationJobNoticeShouldNotBeFound("sourceModule.doesNotContain=" + DEFAULT_SOURCE_MODULE);

        // Get all the depreciationJobNoticeList where sourceModule does not contain UPDATED_SOURCE_MODULE
        defaultDepreciationJobNoticeShouldBeFound("sourceModule.doesNotContain=" + UPDATED_SOURCE_MODULE);
    }

    @Test
    @Transactional
    void getAllDepreciationJobNoticesBySourceEntityIsEqualToSomething() throws Exception {
        // Initialize the database
        depreciationJobNoticeRepository.saveAndFlush(depreciationJobNotice);

        // Get all the depreciationJobNoticeList where sourceEntity equals to DEFAULT_SOURCE_ENTITY
        defaultDepreciationJobNoticeShouldBeFound("sourceEntity.equals=" + DEFAULT_SOURCE_ENTITY);

        // Get all the depreciationJobNoticeList where sourceEntity equals to UPDATED_SOURCE_ENTITY
        defaultDepreciationJobNoticeShouldNotBeFound("sourceEntity.equals=" + UPDATED_SOURCE_ENTITY);
    }

    @Test
    @Transactional
    void getAllDepreciationJobNoticesBySourceEntityIsNotEqualToSomething() throws Exception {
        // Initialize the database
        depreciationJobNoticeRepository.saveAndFlush(depreciationJobNotice);

        // Get all the depreciationJobNoticeList where sourceEntity not equals to DEFAULT_SOURCE_ENTITY
        defaultDepreciationJobNoticeShouldNotBeFound("sourceEntity.notEquals=" + DEFAULT_SOURCE_ENTITY);

        // Get all the depreciationJobNoticeList where sourceEntity not equals to UPDATED_SOURCE_ENTITY
        defaultDepreciationJobNoticeShouldBeFound("sourceEntity.notEquals=" + UPDATED_SOURCE_ENTITY);
    }

    @Test
    @Transactional
    void getAllDepreciationJobNoticesBySourceEntityIsInShouldWork() throws Exception {
        // Initialize the database
        depreciationJobNoticeRepository.saveAndFlush(depreciationJobNotice);

        // Get all the depreciationJobNoticeList where sourceEntity in DEFAULT_SOURCE_ENTITY or UPDATED_SOURCE_ENTITY
        defaultDepreciationJobNoticeShouldBeFound("sourceEntity.in=" + DEFAULT_SOURCE_ENTITY + "," + UPDATED_SOURCE_ENTITY);

        // Get all the depreciationJobNoticeList where sourceEntity equals to UPDATED_SOURCE_ENTITY
        defaultDepreciationJobNoticeShouldNotBeFound("sourceEntity.in=" + UPDATED_SOURCE_ENTITY);
    }

    @Test
    @Transactional
    void getAllDepreciationJobNoticesBySourceEntityIsNullOrNotNull() throws Exception {
        // Initialize the database
        depreciationJobNoticeRepository.saveAndFlush(depreciationJobNotice);

        // Get all the depreciationJobNoticeList where sourceEntity is not null
        defaultDepreciationJobNoticeShouldBeFound("sourceEntity.specified=true");

        // Get all the depreciationJobNoticeList where sourceEntity is null
        defaultDepreciationJobNoticeShouldNotBeFound("sourceEntity.specified=false");
    }

    @Test
    @Transactional
    void getAllDepreciationJobNoticesBySourceEntityContainsSomething() throws Exception {
        // Initialize the database
        depreciationJobNoticeRepository.saveAndFlush(depreciationJobNotice);

        // Get all the depreciationJobNoticeList where sourceEntity contains DEFAULT_SOURCE_ENTITY
        defaultDepreciationJobNoticeShouldBeFound("sourceEntity.contains=" + DEFAULT_SOURCE_ENTITY);

        // Get all the depreciationJobNoticeList where sourceEntity contains UPDATED_SOURCE_ENTITY
        defaultDepreciationJobNoticeShouldNotBeFound("sourceEntity.contains=" + UPDATED_SOURCE_ENTITY);
    }

    @Test
    @Transactional
    void getAllDepreciationJobNoticesBySourceEntityNotContainsSomething() throws Exception {
        // Initialize the database
        depreciationJobNoticeRepository.saveAndFlush(depreciationJobNotice);

        // Get all the depreciationJobNoticeList where sourceEntity does not contain DEFAULT_SOURCE_ENTITY
        defaultDepreciationJobNoticeShouldNotBeFound("sourceEntity.doesNotContain=" + DEFAULT_SOURCE_ENTITY);

        // Get all the depreciationJobNoticeList where sourceEntity does not contain UPDATED_SOURCE_ENTITY
        defaultDepreciationJobNoticeShouldBeFound("sourceEntity.doesNotContain=" + UPDATED_SOURCE_ENTITY);
    }

    @Test
    @Transactional
    void getAllDepreciationJobNoticesByErrorCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        depreciationJobNoticeRepository.saveAndFlush(depreciationJobNotice);

        // Get all the depreciationJobNoticeList where errorCode equals to DEFAULT_ERROR_CODE
        defaultDepreciationJobNoticeShouldBeFound("errorCode.equals=" + DEFAULT_ERROR_CODE);

        // Get all the depreciationJobNoticeList where errorCode equals to UPDATED_ERROR_CODE
        defaultDepreciationJobNoticeShouldNotBeFound("errorCode.equals=" + UPDATED_ERROR_CODE);
    }

    @Test
    @Transactional
    void getAllDepreciationJobNoticesByErrorCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        depreciationJobNoticeRepository.saveAndFlush(depreciationJobNotice);

        // Get all the depreciationJobNoticeList where errorCode not equals to DEFAULT_ERROR_CODE
        defaultDepreciationJobNoticeShouldNotBeFound("errorCode.notEquals=" + DEFAULT_ERROR_CODE);

        // Get all the depreciationJobNoticeList where errorCode not equals to UPDATED_ERROR_CODE
        defaultDepreciationJobNoticeShouldBeFound("errorCode.notEquals=" + UPDATED_ERROR_CODE);
    }

    @Test
    @Transactional
    void getAllDepreciationJobNoticesByErrorCodeIsInShouldWork() throws Exception {
        // Initialize the database
        depreciationJobNoticeRepository.saveAndFlush(depreciationJobNotice);

        // Get all the depreciationJobNoticeList where errorCode in DEFAULT_ERROR_CODE or UPDATED_ERROR_CODE
        defaultDepreciationJobNoticeShouldBeFound("errorCode.in=" + DEFAULT_ERROR_CODE + "," + UPDATED_ERROR_CODE);

        // Get all the depreciationJobNoticeList where errorCode equals to UPDATED_ERROR_CODE
        defaultDepreciationJobNoticeShouldNotBeFound("errorCode.in=" + UPDATED_ERROR_CODE);
    }

    @Test
    @Transactional
    void getAllDepreciationJobNoticesByErrorCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        depreciationJobNoticeRepository.saveAndFlush(depreciationJobNotice);

        // Get all the depreciationJobNoticeList where errorCode is not null
        defaultDepreciationJobNoticeShouldBeFound("errorCode.specified=true");

        // Get all the depreciationJobNoticeList where errorCode is null
        defaultDepreciationJobNoticeShouldNotBeFound("errorCode.specified=false");
    }

    @Test
    @Transactional
    void getAllDepreciationJobNoticesByErrorCodeContainsSomething() throws Exception {
        // Initialize the database
        depreciationJobNoticeRepository.saveAndFlush(depreciationJobNotice);

        // Get all the depreciationJobNoticeList where errorCode contains DEFAULT_ERROR_CODE
        defaultDepreciationJobNoticeShouldBeFound("errorCode.contains=" + DEFAULT_ERROR_CODE);

        // Get all the depreciationJobNoticeList where errorCode contains UPDATED_ERROR_CODE
        defaultDepreciationJobNoticeShouldNotBeFound("errorCode.contains=" + UPDATED_ERROR_CODE);
    }

    @Test
    @Transactional
    void getAllDepreciationJobNoticesByErrorCodeNotContainsSomething() throws Exception {
        // Initialize the database
        depreciationJobNoticeRepository.saveAndFlush(depreciationJobNotice);

        // Get all the depreciationJobNoticeList where errorCode does not contain DEFAULT_ERROR_CODE
        defaultDepreciationJobNoticeShouldNotBeFound("errorCode.doesNotContain=" + DEFAULT_ERROR_CODE);

        // Get all the depreciationJobNoticeList where errorCode does not contain UPDATED_ERROR_CODE
        defaultDepreciationJobNoticeShouldBeFound("errorCode.doesNotContain=" + UPDATED_ERROR_CODE);
    }

    @Test
    @Transactional
    void getAllDepreciationJobNoticesByUserActionIsEqualToSomething() throws Exception {
        // Initialize the database
        depreciationJobNoticeRepository.saveAndFlush(depreciationJobNotice);

        // Get all the depreciationJobNoticeList where userAction equals to DEFAULT_USER_ACTION
        defaultDepreciationJobNoticeShouldBeFound("userAction.equals=" + DEFAULT_USER_ACTION);

        // Get all the depreciationJobNoticeList where userAction equals to UPDATED_USER_ACTION
        defaultDepreciationJobNoticeShouldNotBeFound("userAction.equals=" + UPDATED_USER_ACTION);
    }

    @Test
    @Transactional
    void getAllDepreciationJobNoticesByUserActionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        depreciationJobNoticeRepository.saveAndFlush(depreciationJobNotice);

        // Get all the depreciationJobNoticeList where userAction not equals to DEFAULT_USER_ACTION
        defaultDepreciationJobNoticeShouldNotBeFound("userAction.notEquals=" + DEFAULT_USER_ACTION);

        // Get all the depreciationJobNoticeList where userAction not equals to UPDATED_USER_ACTION
        defaultDepreciationJobNoticeShouldBeFound("userAction.notEquals=" + UPDATED_USER_ACTION);
    }

    @Test
    @Transactional
    void getAllDepreciationJobNoticesByUserActionIsInShouldWork() throws Exception {
        // Initialize the database
        depreciationJobNoticeRepository.saveAndFlush(depreciationJobNotice);

        // Get all the depreciationJobNoticeList where userAction in DEFAULT_USER_ACTION or UPDATED_USER_ACTION
        defaultDepreciationJobNoticeShouldBeFound("userAction.in=" + DEFAULT_USER_ACTION + "," + UPDATED_USER_ACTION);

        // Get all the depreciationJobNoticeList where userAction equals to UPDATED_USER_ACTION
        defaultDepreciationJobNoticeShouldNotBeFound("userAction.in=" + UPDATED_USER_ACTION);
    }

    @Test
    @Transactional
    void getAllDepreciationJobNoticesByUserActionIsNullOrNotNull() throws Exception {
        // Initialize the database
        depreciationJobNoticeRepository.saveAndFlush(depreciationJobNotice);

        // Get all the depreciationJobNoticeList where userAction is not null
        defaultDepreciationJobNoticeShouldBeFound("userAction.specified=true");

        // Get all the depreciationJobNoticeList where userAction is null
        defaultDepreciationJobNoticeShouldNotBeFound("userAction.specified=false");
    }

    @Test
    @Transactional
    void getAllDepreciationJobNoticesByUserActionContainsSomething() throws Exception {
        // Initialize the database
        depreciationJobNoticeRepository.saveAndFlush(depreciationJobNotice);

        // Get all the depreciationJobNoticeList where userAction contains DEFAULT_USER_ACTION
        defaultDepreciationJobNoticeShouldBeFound("userAction.contains=" + DEFAULT_USER_ACTION);

        // Get all the depreciationJobNoticeList where userAction contains UPDATED_USER_ACTION
        defaultDepreciationJobNoticeShouldNotBeFound("userAction.contains=" + UPDATED_USER_ACTION);
    }

    @Test
    @Transactional
    void getAllDepreciationJobNoticesByUserActionNotContainsSomething() throws Exception {
        // Initialize the database
        depreciationJobNoticeRepository.saveAndFlush(depreciationJobNotice);

        // Get all the depreciationJobNoticeList where userAction does not contain DEFAULT_USER_ACTION
        defaultDepreciationJobNoticeShouldNotBeFound("userAction.doesNotContain=" + DEFAULT_USER_ACTION);

        // Get all the depreciationJobNoticeList where userAction does not contain UPDATED_USER_ACTION
        defaultDepreciationJobNoticeShouldBeFound("userAction.doesNotContain=" + UPDATED_USER_ACTION);
    }

    @Test
    @Transactional
    void getAllDepreciationJobNoticesByDepreciationJobIsEqualToSomething() throws Exception {
        // Initialize the database
        depreciationJobNoticeRepository.saveAndFlush(depreciationJobNotice);
        DepreciationJob depreciationJob;
        if (TestUtil.findAll(em, DepreciationJob.class).isEmpty()) {
            depreciationJob = DepreciationJobResourceIT.createEntity(em);
            em.persist(depreciationJob);
            em.flush();
        } else {
            depreciationJob = TestUtil.findAll(em, DepreciationJob.class).get(0);
        }
        em.persist(depreciationJob);
        em.flush();
        depreciationJobNotice.setDepreciationJob(depreciationJob);
        depreciationJobNoticeRepository.saveAndFlush(depreciationJobNotice);
        Long depreciationJobId = depreciationJob.getId();

        // Get all the depreciationJobNoticeList where depreciationJob equals to depreciationJobId
        defaultDepreciationJobNoticeShouldBeFound("depreciationJobId.equals=" + depreciationJobId);

        // Get all the depreciationJobNoticeList where depreciationJob equals to (depreciationJobId + 1)
        defaultDepreciationJobNoticeShouldNotBeFound("depreciationJobId.equals=" + (depreciationJobId + 1));
    }

    @Test
    @Transactional
    void getAllDepreciationJobNoticesByDepreciationBatchSequenceIsEqualToSomething() throws Exception {
        // Initialize the database
        depreciationJobNoticeRepository.saveAndFlush(depreciationJobNotice);
        DepreciationBatchSequence depreciationBatchSequence;
        if (TestUtil.findAll(em, DepreciationBatchSequence.class).isEmpty()) {
            depreciationBatchSequence = DepreciationBatchSequenceResourceIT.createEntity(em);
            em.persist(depreciationBatchSequence);
            em.flush();
        } else {
            depreciationBatchSequence = TestUtil.findAll(em, DepreciationBatchSequence.class).get(0);
        }
        em.persist(depreciationBatchSequence);
        em.flush();
        depreciationJobNotice.setDepreciationBatchSequence(depreciationBatchSequence);
        depreciationJobNoticeRepository.saveAndFlush(depreciationJobNotice);
        Long depreciationBatchSequenceId = depreciationBatchSequence.getId();

        // Get all the depreciationJobNoticeList where depreciationBatchSequence equals to depreciationBatchSequenceId
        defaultDepreciationJobNoticeShouldBeFound("depreciationBatchSequenceId.equals=" + depreciationBatchSequenceId);

        // Get all the depreciationJobNoticeList where depreciationBatchSequence equals to (depreciationBatchSequenceId + 1)
        defaultDepreciationJobNoticeShouldNotBeFound("depreciationBatchSequenceId.equals=" + (depreciationBatchSequenceId + 1));
    }

    @Test
    @Transactional
    void getAllDepreciationJobNoticesByDepreciationPeriodIsEqualToSomething() throws Exception {
        // Initialize the database
        depreciationJobNoticeRepository.saveAndFlush(depreciationJobNotice);
        DepreciationPeriod depreciationPeriod;
        if (TestUtil.findAll(em, DepreciationPeriod.class).isEmpty()) {
            depreciationPeriod = DepreciationPeriodResourceIT.createEntity(em);
            em.persist(depreciationPeriod);
            em.flush();
        } else {
            depreciationPeriod = TestUtil.findAll(em, DepreciationPeriod.class).get(0);
        }
        em.persist(depreciationPeriod);
        em.flush();
        depreciationJobNotice.setDepreciationPeriod(depreciationPeriod);
        depreciationJobNoticeRepository.saveAndFlush(depreciationJobNotice);
        Long depreciationPeriodId = depreciationPeriod.getId();

        // Get all the depreciationJobNoticeList where depreciationPeriod equals to depreciationPeriodId
        defaultDepreciationJobNoticeShouldBeFound("depreciationPeriodId.equals=" + depreciationPeriodId);

        // Get all the depreciationJobNoticeList where depreciationPeriod equals to (depreciationPeriodId + 1)
        defaultDepreciationJobNoticeShouldNotBeFound("depreciationPeriodId.equals=" + (depreciationPeriodId + 1));
    }

    @Test
    @Transactional
    void getAllDepreciationJobNoticesByPlaceholderIsEqualToSomething() throws Exception {
        // Initialize the database
        depreciationJobNoticeRepository.saveAndFlush(depreciationJobNotice);
        Placeholder placeholder;
        if (TestUtil.findAll(em, Placeholder.class).isEmpty()) {
            placeholder = PlaceholderResourceIT.createEntity(em);
            em.persist(placeholder);
            em.flush();
        } else {
            placeholder = TestUtil.findAll(em, Placeholder.class).get(0);
        }
        em.persist(placeholder);
        em.flush();
        depreciationJobNotice.addPlaceholder(placeholder);
        depreciationJobNoticeRepository.saveAndFlush(depreciationJobNotice);
        Long placeholderId = placeholder.getId();

        // Get all the depreciationJobNoticeList where placeholder equals to placeholderId
        defaultDepreciationJobNoticeShouldBeFound("placeholderId.equals=" + placeholderId);

        // Get all the depreciationJobNoticeList where placeholder equals to (placeholderId + 1)
        defaultDepreciationJobNoticeShouldNotBeFound("placeholderId.equals=" + (placeholderId + 1));
    }

    @Test
    @Transactional
    void getAllDepreciationJobNoticesByUniversallyUniqueMappingIsEqualToSomething() throws Exception {
        // Initialize the database
        depreciationJobNoticeRepository.saveAndFlush(depreciationJobNotice);
        UniversallyUniqueMapping universallyUniqueMapping;
        if (TestUtil.findAll(em, UniversallyUniqueMapping.class).isEmpty()) {
            universallyUniqueMapping = UniversallyUniqueMappingResourceIT.createEntity(em);
            em.persist(universallyUniqueMapping);
            em.flush();
        } else {
            universallyUniqueMapping = TestUtil.findAll(em, UniversallyUniqueMapping.class).get(0);
        }
        em.persist(universallyUniqueMapping);
        em.flush();
        depreciationJobNotice.addUniversallyUniqueMapping(universallyUniqueMapping);
        depreciationJobNoticeRepository.saveAndFlush(depreciationJobNotice);
        Long universallyUniqueMappingId = universallyUniqueMapping.getId();

        // Get all the depreciationJobNoticeList where universallyUniqueMapping equals to universallyUniqueMappingId
        defaultDepreciationJobNoticeShouldBeFound("universallyUniqueMappingId.equals=" + universallyUniqueMappingId);

        // Get all the depreciationJobNoticeList where universallyUniqueMapping equals to (universallyUniqueMappingId + 1)
        defaultDepreciationJobNoticeShouldNotBeFound("universallyUniqueMappingId.equals=" + (universallyUniqueMappingId + 1));
    }

    @Test
    @Transactional
    void getAllDepreciationJobNoticesBySuperintendedIsEqualToSomething() throws Exception {
        // Initialize the database
        depreciationJobNoticeRepository.saveAndFlush(depreciationJobNotice);
        ApplicationUser superintended;
        if (TestUtil.findAll(em, ApplicationUser.class).isEmpty()) {
            superintended = ApplicationUserResourceIT.createEntity(em);
            em.persist(superintended);
            em.flush();
        } else {
            superintended = TestUtil.findAll(em, ApplicationUser.class).get(0);
        }
        em.persist(superintended);
        em.flush();
        depreciationJobNotice.setSuperintended(superintended);
        depreciationJobNoticeRepository.saveAndFlush(depreciationJobNotice);
        Long superintendedId = superintended.getId();

        // Get all the depreciationJobNoticeList where superintended equals to superintendedId
        defaultDepreciationJobNoticeShouldBeFound("superintendedId.equals=" + superintendedId);

        // Get all the depreciationJobNoticeList where superintended equals to (superintendedId + 1)
        defaultDepreciationJobNoticeShouldNotBeFound("superintendedId.equals=" + (superintendedId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDepreciationJobNoticeShouldBeFound(String filter) throws Exception {
        restDepreciationJobNoticeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
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

        // Check, that the count call also returns 1
        restDepreciationJobNoticeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDepreciationJobNoticeShouldNotBeFound(String filter) throws Exception {
        restDepreciationJobNoticeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDepreciationJobNoticeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
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
