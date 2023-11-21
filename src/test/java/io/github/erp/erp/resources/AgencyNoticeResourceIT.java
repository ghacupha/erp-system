package io.github.erp.erp.resources;

/*-
 * Erp System - Mark VIII No 1 (Hilkiah Series) Server ver 1.6.0
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
import io.github.erp.IntegrationTest;
import io.github.erp.domain.*;
import io.github.erp.domain.enumeration.AgencyStatusType;
import io.github.erp.repository.AgencyNoticeRepository;
import io.github.erp.repository.search.AgencyNoticeSearchRepository;
import io.github.erp.service.AgencyNoticeService;
import io.github.erp.service.dto.AgencyNoticeDTO;
import io.github.erp.service.mapper.AgencyNoticeMapper;
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
import org.springframework.util.Base64Utils;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

import static io.github.erp.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link AgencyNoticeResourceProd} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser(roles = {"TAX_MODULE_USER"})
public class AgencyNoticeResourceIT {

    private static final String DEFAULT_REFERENCE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_REFERENCE_NUMBER = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_REFERENCE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_REFERENCE_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_REFERENCE_DATE = LocalDate.ofEpochDay(-1L);

    private static final BigDecimal DEFAULT_ASSESSMENT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_ASSESSMENT_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_ASSESSMENT_AMOUNT = new BigDecimal(1 - 1);

    private static final AgencyStatusType DEFAULT_AGENCY_STATUS = AgencyStatusType.CLEARED;
    private static final AgencyStatusType UPDATED_AGENCY_STATUS = AgencyStatusType.NOT_CLEARED;

    private static final byte[] DEFAULT_ASSESSMENT_NOTICE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_ASSESSMENT_NOTICE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_ASSESSMENT_NOTICE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_ASSESSMENT_NOTICE_CONTENT_TYPE = "image/png";

    private static final String ENTITY_API_URL = "/api/taxes/agency-notices";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/taxes/_search/agency-notices";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AgencyNoticeRepository agencyNoticeRepository;

    @Mock
    private AgencyNoticeRepository agencyNoticeRepositoryMock;

    @Autowired
    private AgencyNoticeMapper agencyNoticeMapper;

    @Mock
    private AgencyNoticeService agencyNoticeServiceMock;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.AgencyNoticeSearchRepositoryMockConfiguration
     */
    @Autowired
    private AgencyNoticeSearchRepository mockAgencyNoticeSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAgencyNoticeMockMvc;

    private AgencyNotice agencyNotice;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AgencyNotice createEntity(EntityManager em) {
        AgencyNotice agencyNotice = new AgencyNotice()
            .referenceNumber(DEFAULT_REFERENCE_NUMBER)
            .referenceDate(DEFAULT_REFERENCE_DATE)
            .assessmentAmount(DEFAULT_ASSESSMENT_AMOUNT)
            .agencyStatus(DEFAULT_AGENCY_STATUS)
            .assessmentNotice(DEFAULT_ASSESSMENT_NOTICE)
            .assessmentNoticeContentType(DEFAULT_ASSESSMENT_NOTICE_CONTENT_TYPE);
        return agencyNotice;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AgencyNotice createUpdatedEntity(EntityManager em) {
        AgencyNotice agencyNotice = new AgencyNotice()
            .referenceNumber(UPDATED_REFERENCE_NUMBER)
            .referenceDate(UPDATED_REFERENCE_DATE)
            .assessmentAmount(UPDATED_ASSESSMENT_AMOUNT)
            .agencyStatus(UPDATED_AGENCY_STATUS)
            .assessmentNotice(UPDATED_ASSESSMENT_NOTICE)
            .assessmentNoticeContentType(UPDATED_ASSESSMENT_NOTICE_CONTENT_TYPE);
        return agencyNotice;
    }

    @BeforeEach
    public void initTest() {
        agencyNotice = createEntity(em);
    }

    @Test
    @Transactional
    void createAgencyNotice() throws Exception {
        int databaseSizeBeforeCreate = agencyNoticeRepository.findAll().size();
        // Create the AgencyNotice
        AgencyNoticeDTO agencyNoticeDTO = agencyNoticeMapper.toDto(agencyNotice);
        restAgencyNoticeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(agencyNoticeDTO))
            )
            .andExpect(status().isCreated());

        // Validate the AgencyNotice in the database
        List<AgencyNotice> agencyNoticeList = agencyNoticeRepository.findAll();
        assertThat(agencyNoticeList).hasSize(databaseSizeBeforeCreate + 1);
        AgencyNotice testAgencyNotice = agencyNoticeList.get(agencyNoticeList.size() - 1);
        assertThat(testAgencyNotice.getReferenceNumber()).isEqualTo(DEFAULT_REFERENCE_NUMBER);
        assertThat(testAgencyNotice.getReferenceDate()).isEqualTo(DEFAULT_REFERENCE_DATE);
        assertThat(testAgencyNotice.getAssessmentAmount()).isEqualByComparingTo(DEFAULT_ASSESSMENT_AMOUNT);
        assertThat(testAgencyNotice.getAgencyStatus()).isEqualTo(DEFAULT_AGENCY_STATUS);
        assertThat(testAgencyNotice.getAssessmentNotice()).isEqualTo(DEFAULT_ASSESSMENT_NOTICE);
        assertThat(testAgencyNotice.getAssessmentNoticeContentType()).isEqualTo(DEFAULT_ASSESSMENT_NOTICE_CONTENT_TYPE);

        // Validate the AgencyNotice in Elasticsearch
        verify(mockAgencyNoticeSearchRepository, times(1)).save(testAgencyNotice);
    }

    @Test
    @Transactional
    void createAgencyNoticeWithExistingId() throws Exception {
        // Create the AgencyNotice with an existing ID
        agencyNotice.setId(1L);
        AgencyNoticeDTO agencyNoticeDTO = agencyNoticeMapper.toDto(agencyNotice);

        int databaseSizeBeforeCreate = agencyNoticeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAgencyNoticeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(agencyNoticeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AgencyNotice in the database
        List<AgencyNotice> agencyNoticeList = agencyNoticeRepository.findAll();
        assertThat(agencyNoticeList).hasSize(databaseSizeBeforeCreate);

        // Validate the AgencyNotice in Elasticsearch
        verify(mockAgencyNoticeSearchRepository, times(0)).save(agencyNotice);
    }

    @Test
    @Transactional
    void checkReferenceNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = agencyNoticeRepository.findAll().size();
        // set the field null
        agencyNotice.setReferenceNumber(null);

        // Create the AgencyNotice, which fails.
        AgencyNoticeDTO agencyNoticeDTO = agencyNoticeMapper.toDto(agencyNotice);

        restAgencyNoticeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(agencyNoticeDTO))
            )
            .andExpect(status().isBadRequest());

        List<AgencyNotice> agencyNoticeList = agencyNoticeRepository.findAll();
        assertThat(agencyNoticeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAssessmentAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = agencyNoticeRepository.findAll().size();
        // set the field null
        agencyNotice.setAssessmentAmount(null);

        // Create the AgencyNotice, which fails.
        AgencyNoticeDTO agencyNoticeDTO = agencyNoticeMapper.toDto(agencyNotice);

        restAgencyNoticeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(agencyNoticeDTO))
            )
            .andExpect(status().isBadRequest());

        List<AgencyNotice> agencyNoticeList = agencyNoticeRepository.findAll();
        assertThat(agencyNoticeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAgencyStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = agencyNoticeRepository.findAll().size();
        // set the field null
        agencyNotice.setAgencyStatus(null);

        // Create the AgencyNotice, which fails.
        AgencyNoticeDTO agencyNoticeDTO = agencyNoticeMapper.toDto(agencyNotice);

        restAgencyNoticeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(agencyNoticeDTO))
            )
            .andExpect(status().isBadRequest());

        List<AgencyNotice> agencyNoticeList = agencyNoticeRepository.findAll();
        assertThat(agencyNoticeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAgencyNotices() throws Exception {
        // Initialize the database
        agencyNoticeRepository.saveAndFlush(agencyNotice);

        // Get all the agencyNoticeList
        restAgencyNoticeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(agencyNotice.getId().intValue())))
            .andExpect(jsonPath("$.[*].referenceNumber").value(hasItem(DEFAULT_REFERENCE_NUMBER)))
            .andExpect(jsonPath("$.[*].referenceDate").value(hasItem(DEFAULT_REFERENCE_DATE.toString())))
            .andExpect(jsonPath("$.[*].assessmentAmount").value(hasItem(sameNumber(DEFAULT_ASSESSMENT_AMOUNT))))
            .andExpect(jsonPath("$.[*].agencyStatus").value(hasItem(DEFAULT_AGENCY_STATUS.toString())))
            .andExpect(jsonPath("$.[*].assessmentNoticeContentType").value(hasItem(DEFAULT_ASSESSMENT_NOTICE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].assessmentNotice").value(hasItem(Base64Utils.encodeToString(DEFAULT_ASSESSMENT_NOTICE))));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllAgencyNoticesWithEagerRelationshipsIsEnabled() throws Exception {
        when(agencyNoticeServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restAgencyNoticeMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(agencyNoticeServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllAgencyNoticesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(agencyNoticeServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restAgencyNoticeMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(agencyNoticeServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getAgencyNotice() throws Exception {
        // Initialize the database
        agencyNoticeRepository.saveAndFlush(agencyNotice);

        // Get the agencyNotice
        restAgencyNoticeMockMvc
            .perform(get(ENTITY_API_URL_ID, agencyNotice.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(agencyNotice.getId().intValue()))
            .andExpect(jsonPath("$.referenceNumber").value(DEFAULT_REFERENCE_NUMBER))
            .andExpect(jsonPath("$.referenceDate").value(DEFAULT_REFERENCE_DATE.toString()))
            .andExpect(jsonPath("$.assessmentAmount").value(sameNumber(DEFAULT_ASSESSMENT_AMOUNT)))
            .andExpect(jsonPath("$.agencyStatus").value(DEFAULT_AGENCY_STATUS.toString()))
            .andExpect(jsonPath("$.assessmentNoticeContentType").value(DEFAULT_ASSESSMENT_NOTICE_CONTENT_TYPE))
            .andExpect(jsonPath("$.assessmentNotice").value(Base64Utils.encodeToString(DEFAULT_ASSESSMENT_NOTICE)));
    }

    @Test
    @Transactional
    void getAgencyNoticesByIdFiltering() throws Exception {
        // Initialize the database
        agencyNoticeRepository.saveAndFlush(agencyNotice);

        Long id = agencyNotice.getId();

        defaultAgencyNoticeShouldBeFound("id.equals=" + id);
        defaultAgencyNoticeShouldNotBeFound("id.notEquals=" + id);

        defaultAgencyNoticeShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultAgencyNoticeShouldNotBeFound("id.greaterThan=" + id);

        defaultAgencyNoticeShouldBeFound("id.lessThanOrEqual=" + id);
        defaultAgencyNoticeShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAgencyNoticesByReferenceNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        agencyNoticeRepository.saveAndFlush(agencyNotice);

        // Get all the agencyNoticeList where referenceNumber equals to DEFAULT_REFERENCE_NUMBER
        defaultAgencyNoticeShouldBeFound("referenceNumber.equals=" + DEFAULT_REFERENCE_NUMBER);

        // Get all the agencyNoticeList where referenceNumber equals to UPDATED_REFERENCE_NUMBER
        defaultAgencyNoticeShouldNotBeFound("referenceNumber.equals=" + UPDATED_REFERENCE_NUMBER);
    }

    @Test
    @Transactional
    void getAllAgencyNoticesByReferenceNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        agencyNoticeRepository.saveAndFlush(agencyNotice);

        // Get all the agencyNoticeList where referenceNumber not equals to DEFAULT_REFERENCE_NUMBER
        defaultAgencyNoticeShouldNotBeFound("referenceNumber.notEquals=" + DEFAULT_REFERENCE_NUMBER);

        // Get all the agencyNoticeList where referenceNumber not equals to UPDATED_REFERENCE_NUMBER
        defaultAgencyNoticeShouldBeFound("referenceNumber.notEquals=" + UPDATED_REFERENCE_NUMBER);
    }

    @Test
    @Transactional
    void getAllAgencyNoticesByReferenceNumberIsInShouldWork() throws Exception {
        // Initialize the database
        agencyNoticeRepository.saveAndFlush(agencyNotice);

        // Get all the agencyNoticeList where referenceNumber in DEFAULT_REFERENCE_NUMBER or UPDATED_REFERENCE_NUMBER
        defaultAgencyNoticeShouldBeFound("referenceNumber.in=" + DEFAULT_REFERENCE_NUMBER + "," + UPDATED_REFERENCE_NUMBER);

        // Get all the agencyNoticeList where referenceNumber equals to UPDATED_REFERENCE_NUMBER
        defaultAgencyNoticeShouldNotBeFound("referenceNumber.in=" + UPDATED_REFERENCE_NUMBER);
    }

    @Test
    @Transactional
    void getAllAgencyNoticesByReferenceNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        agencyNoticeRepository.saveAndFlush(agencyNotice);

        // Get all the agencyNoticeList where referenceNumber is not null
        defaultAgencyNoticeShouldBeFound("referenceNumber.specified=true");

        // Get all the agencyNoticeList where referenceNumber is null
        defaultAgencyNoticeShouldNotBeFound("referenceNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllAgencyNoticesByReferenceNumberContainsSomething() throws Exception {
        // Initialize the database
        agencyNoticeRepository.saveAndFlush(agencyNotice);

        // Get all the agencyNoticeList where referenceNumber contains DEFAULT_REFERENCE_NUMBER
        defaultAgencyNoticeShouldBeFound("referenceNumber.contains=" + DEFAULT_REFERENCE_NUMBER);

        // Get all the agencyNoticeList where referenceNumber contains UPDATED_REFERENCE_NUMBER
        defaultAgencyNoticeShouldNotBeFound("referenceNumber.contains=" + UPDATED_REFERENCE_NUMBER);
    }

    @Test
    @Transactional
    void getAllAgencyNoticesByReferenceNumberNotContainsSomething() throws Exception {
        // Initialize the database
        agencyNoticeRepository.saveAndFlush(agencyNotice);

        // Get all the agencyNoticeList where referenceNumber does not contain DEFAULT_REFERENCE_NUMBER
        defaultAgencyNoticeShouldNotBeFound("referenceNumber.doesNotContain=" + DEFAULT_REFERENCE_NUMBER);

        // Get all the agencyNoticeList where referenceNumber does not contain UPDATED_REFERENCE_NUMBER
        defaultAgencyNoticeShouldBeFound("referenceNumber.doesNotContain=" + UPDATED_REFERENCE_NUMBER);
    }

    @Test
    @Transactional
    void getAllAgencyNoticesByReferenceDateIsEqualToSomething() throws Exception {
        // Initialize the database
        agencyNoticeRepository.saveAndFlush(agencyNotice);

        // Get all the agencyNoticeList where referenceDate equals to DEFAULT_REFERENCE_DATE
        defaultAgencyNoticeShouldBeFound("referenceDate.equals=" + DEFAULT_REFERENCE_DATE);

        // Get all the agencyNoticeList where referenceDate equals to UPDATED_REFERENCE_DATE
        defaultAgencyNoticeShouldNotBeFound("referenceDate.equals=" + UPDATED_REFERENCE_DATE);
    }

    @Test
    @Transactional
    void getAllAgencyNoticesByReferenceDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        agencyNoticeRepository.saveAndFlush(agencyNotice);

        // Get all the agencyNoticeList where referenceDate not equals to DEFAULT_REFERENCE_DATE
        defaultAgencyNoticeShouldNotBeFound("referenceDate.notEquals=" + DEFAULT_REFERENCE_DATE);

        // Get all the agencyNoticeList where referenceDate not equals to UPDATED_REFERENCE_DATE
        defaultAgencyNoticeShouldBeFound("referenceDate.notEquals=" + UPDATED_REFERENCE_DATE);
    }

    @Test
    @Transactional
    void getAllAgencyNoticesByReferenceDateIsInShouldWork() throws Exception {
        // Initialize the database
        agencyNoticeRepository.saveAndFlush(agencyNotice);

        // Get all the agencyNoticeList where referenceDate in DEFAULT_REFERENCE_DATE or UPDATED_REFERENCE_DATE
        defaultAgencyNoticeShouldBeFound("referenceDate.in=" + DEFAULT_REFERENCE_DATE + "," + UPDATED_REFERENCE_DATE);

        // Get all the agencyNoticeList where referenceDate equals to UPDATED_REFERENCE_DATE
        defaultAgencyNoticeShouldNotBeFound("referenceDate.in=" + UPDATED_REFERENCE_DATE);
    }

    @Test
    @Transactional
    void getAllAgencyNoticesByReferenceDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        agencyNoticeRepository.saveAndFlush(agencyNotice);

        // Get all the agencyNoticeList where referenceDate is not null
        defaultAgencyNoticeShouldBeFound("referenceDate.specified=true");

        // Get all the agencyNoticeList where referenceDate is null
        defaultAgencyNoticeShouldNotBeFound("referenceDate.specified=false");
    }

    @Test
    @Transactional
    void getAllAgencyNoticesByReferenceDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        agencyNoticeRepository.saveAndFlush(agencyNotice);

        // Get all the agencyNoticeList where referenceDate is greater than or equal to DEFAULT_REFERENCE_DATE
        defaultAgencyNoticeShouldBeFound("referenceDate.greaterThanOrEqual=" + DEFAULT_REFERENCE_DATE);

        // Get all the agencyNoticeList where referenceDate is greater than or equal to UPDATED_REFERENCE_DATE
        defaultAgencyNoticeShouldNotBeFound("referenceDate.greaterThanOrEqual=" + UPDATED_REFERENCE_DATE);
    }

    @Test
    @Transactional
    void getAllAgencyNoticesByReferenceDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        agencyNoticeRepository.saveAndFlush(agencyNotice);

        // Get all the agencyNoticeList where referenceDate is less than or equal to DEFAULT_REFERENCE_DATE
        defaultAgencyNoticeShouldBeFound("referenceDate.lessThanOrEqual=" + DEFAULT_REFERENCE_DATE);

        // Get all the agencyNoticeList where referenceDate is less than or equal to SMALLER_REFERENCE_DATE
        defaultAgencyNoticeShouldNotBeFound("referenceDate.lessThanOrEqual=" + SMALLER_REFERENCE_DATE);
    }

    @Test
    @Transactional
    void getAllAgencyNoticesByReferenceDateIsLessThanSomething() throws Exception {
        // Initialize the database
        agencyNoticeRepository.saveAndFlush(agencyNotice);

        // Get all the agencyNoticeList where referenceDate is less than DEFAULT_REFERENCE_DATE
        defaultAgencyNoticeShouldNotBeFound("referenceDate.lessThan=" + DEFAULT_REFERENCE_DATE);

        // Get all the agencyNoticeList where referenceDate is less than UPDATED_REFERENCE_DATE
        defaultAgencyNoticeShouldBeFound("referenceDate.lessThan=" + UPDATED_REFERENCE_DATE);
    }

    @Test
    @Transactional
    void getAllAgencyNoticesByReferenceDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        agencyNoticeRepository.saveAndFlush(agencyNotice);

        // Get all the agencyNoticeList where referenceDate is greater than DEFAULT_REFERENCE_DATE
        defaultAgencyNoticeShouldNotBeFound("referenceDate.greaterThan=" + DEFAULT_REFERENCE_DATE);

        // Get all the agencyNoticeList where referenceDate is greater than SMALLER_REFERENCE_DATE
        defaultAgencyNoticeShouldBeFound("referenceDate.greaterThan=" + SMALLER_REFERENCE_DATE);
    }

    @Test
    @Transactional
    void getAllAgencyNoticesByAssessmentAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        agencyNoticeRepository.saveAndFlush(agencyNotice);

        // Get all the agencyNoticeList where assessmentAmount equals to DEFAULT_ASSESSMENT_AMOUNT
        defaultAgencyNoticeShouldBeFound("assessmentAmount.equals=" + DEFAULT_ASSESSMENT_AMOUNT);

        // Get all the agencyNoticeList where assessmentAmount equals to UPDATED_ASSESSMENT_AMOUNT
        defaultAgencyNoticeShouldNotBeFound("assessmentAmount.equals=" + UPDATED_ASSESSMENT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllAgencyNoticesByAssessmentAmountIsNotEqualToSomething() throws Exception {
        // Initialize the database
        agencyNoticeRepository.saveAndFlush(agencyNotice);

        // Get all the agencyNoticeList where assessmentAmount not equals to DEFAULT_ASSESSMENT_AMOUNT
        defaultAgencyNoticeShouldNotBeFound("assessmentAmount.notEquals=" + DEFAULT_ASSESSMENT_AMOUNT);

        // Get all the agencyNoticeList where assessmentAmount not equals to UPDATED_ASSESSMENT_AMOUNT
        defaultAgencyNoticeShouldBeFound("assessmentAmount.notEquals=" + UPDATED_ASSESSMENT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllAgencyNoticesByAssessmentAmountIsInShouldWork() throws Exception {
        // Initialize the database
        agencyNoticeRepository.saveAndFlush(agencyNotice);

        // Get all the agencyNoticeList where assessmentAmount in DEFAULT_ASSESSMENT_AMOUNT or UPDATED_ASSESSMENT_AMOUNT
        defaultAgencyNoticeShouldBeFound("assessmentAmount.in=" + DEFAULT_ASSESSMENT_AMOUNT + "," + UPDATED_ASSESSMENT_AMOUNT);

        // Get all the agencyNoticeList where assessmentAmount equals to UPDATED_ASSESSMENT_AMOUNT
        defaultAgencyNoticeShouldNotBeFound("assessmentAmount.in=" + UPDATED_ASSESSMENT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllAgencyNoticesByAssessmentAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        agencyNoticeRepository.saveAndFlush(agencyNotice);

        // Get all the agencyNoticeList where assessmentAmount is not null
        defaultAgencyNoticeShouldBeFound("assessmentAmount.specified=true");

        // Get all the agencyNoticeList where assessmentAmount is null
        defaultAgencyNoticeShouldNotBeFound("assessmentAmount.specified=false");
    }

    @Test
    @Transactional
    void getAllAgencyNoticesByAssessmentAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        agencyNoticeRepository.saveAndFlush(agencyNotice);

        // Get all the agencyNoticeList where assessmentAmount is greater than or equal to DEFAULT_ASSESSMENT_AMOUNT
        defaultAgencyNoticeShouldBeFound("assessmentAmount.greaterThanOrEqual=" + DEFAULT_ASSESSMENT_AMOUNT);

        // Get all the agencyNoticeList where assessmentAmount is greater than or equal to UPDATED_ASSESSMENT_AMOUNT
        defaultAgencyNoticeShouldNotBeFound("assessmentAmount.greaterThanOrEqual=" + UPDATED_ASSESSMENT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllAgencyNoticesByAssessmentAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        agencyNoticeRepository.saveAndFlush(agencyNotice);

        // Get all the agencyNoticeList where assessmentAmount is less than or equal to DEFAULT_ASSESSMENT_AMOUNT
        defaultAgencyNoticeShouldBeFound("assessmentAmount.lessThanOrEqual=" + DEFAULT_ASSESSMENT_AMOUNT);

        // Get all the agencyNoticeList where assessmentAmount is less than or equal to SMALLER_ASSESSMENT_AMOUNT
        defaultAgencyNoticeShouldNotBeFound("assessmentAmount.lessThanOrEqual=" + SMALLER_ASSESSMENT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllAgencyNoticesByAssessmentAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        agencyNoticeRepository.saveAndFlush(agencyNotice);

        // Get all the agencyNoticeList where assessmentAmount is less than DEFAULT_ASSESSMENT_AMOUNT
        defaultAgencyNoticeShouldNotBeFound("assessmentAmount.lessThan=" + DEFAULT_ASSESSMENT_AMOUNT);

        // Get all the agencyNoticeList where assessmentAmount is less than UPDATED_ASSESSMENT_AMOUNT
        defaultAgencyNoticeShouldBeFound("assessmentAmount.lessThan=" + UPDATED_ASSESSMENT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllAgencyNoticesByAssessmentAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        agencyNoticeRepository.saveAndFlush(agencyNotice);

        // Get all the agencyNoticeList where assessmentAmount is greater than DEFAULT_ASSESSMENT_AMOUNT
        defaultAgencyNoticeShouldNotBeFound("assessmentAmount.greaterThan=" + DEFAULT_ASSESSMENT_AMOUNT);

        // Get all the agencyNoticeList where assessmentAmount is greater than SMALLER_ASSESSMENT_AMOUNT
        defaultAgencyNoticeShouldBeFound("assessmentAmount.greaterThan=" + SMALLER_ASSESSMENT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllAgencyNoticesByAgencyStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        agencyNoticeRepository.saveAndFlush(agencyNotice);

        // Get all the agencyNoticeList where agencyStatus equals to DEFAULT_AGENCY_STATUS
        defaultAgencyNoticeShouldBeFound("agencyStatus.equals=" + DEFAULT_AGENCY_STATUS);

        // Get all the agencyNoticeList where agencyStatus equals to UPDATED_AGENCY_STATUS
        defaultAgencyNoticeShouldNotBeFound("agencyStatus.equals=" + UPDATED_AGENCY_STATUS);
    }

    @Test
    @Transactional
    void getAllAgencyNoticesByAgencyStatusIsNotEqualToSomething() throws Exception {
        // Initialize the database
        agencyNoticeRepository.saveAndFlush(agencyNotice);

        // Get all the agencyNoticeList where agencyStatus not equals to DEFAULT_AGENCY_STATUS
        defaultAgencyNoticeShouldNotBeFound("agencyStatus.notEquals=" + DEFAULT_AGENCY_STATUS);

        // Get all the agencyNoticeList where agencyStatus not equals to UPDATED_AGENCY_STATUS
        defaultAgencyNoticeShouldBeFound("agencyStatus.notEquals=" + UPDATED_AGENCY_STATUS);
    }

    @Test
    @Transactional
    void getAllAgencyNoticesByAgencyStatusIsInShouldWork() throws Exception {
        // Initialize the database
        agencyNoticeRepository.saveAndFlush(agencyNotice);

        // Get all the agencyNoticeList where agencyStatus in DEFAULT_AGENCY_STATUS or UPDATED_AGENCY_STATUS
        defaultAgencyNoticeShouldBeFound("agencyStatus.in=" + DEFAULT_AGENCY_STATUS + "," + UPDATED_AGENCY_STATUS);

        // Get all the agencyNoticeList where agencyStatus equals to UPDATED_AGENCY_STATUS
        defaultAgencyNoticeShouldNotBeFound("agencyStatus.in=" + UPDATED_AGENCY_STATUS);
    }

    @Test
    @Transactional
    void getAllAgencyNoticesByAgencyStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        agencyNoticeRepository.saveAndFlush(agencyNotice);

        // Get all the agencyNoticeList where agencyStatus is not null
        defaultAgencyNoticeShouldBeFound("agencyStatus.specified=true");

        // Get all the agencyNoticeList where agencyStatus is null
        defaultAgencyNoticeShouldNotBeFound("agencyStatus.specified=false");
    }

    @Test
    @Transactional
    void getAllAgencyNoticesByCorrespondentsIsEqualToSomething() throws Exception {
        // Initialize the database
        agencyNoticeRepository.saveAndFlush(agencyNotice);
        Dealer correspondents;
        if (TestUtil.findAll(em, Dealer.class).isEmpty()) {
            correspondents = DealerResourceIT.createEntity(em);
            em.persist(correspondents);
            em.flush();
        } else {
            correspondents = TestUtil.findAll(em, Dealer.class).get(0);
        }
        em.persist(correspondents);
        em.flush();
        agencyNotice.addCorrespondents(correspondents);
        agencyNoticeRepository.saveAndFlush(agencyNotice);
        Long correspondentsId = correspondents.getId();

        // Get all the agencyNoticeList where correspondents equals to correspondentsId
        defaultAgencyNoticeShouldBeFound("correspondentsId.equals=" + correspondentsId);

        // Get all the agencyNoticeList where correspondents equals to (correspondentsId + 1)
        defaultAgencyNoticeShouldNotBeFound("correspondentsId.equals=" + (correspondentsId + 1));
    }

    @Test
    @Transactional
    void getAllAgencyNoticesBySettlementCurrencyIsEqualToSomething() throws Exception {
        // Initialize the database
        agencyNoticeRepository.saveAndFlush(agencyNotice);
        SettlementCurrency settlementCurrency;
        if (TestUtil.findAll(em, SettlementCurrency.class).isEmpty()) {
            settlementCurrency = SettlementCurrencyResourceIT.createEntity(em);
            em.persist(settlementCurrency);
            em.flush();
        } else {
            settlementCurrency = TestUtil.findAll(em, SettlementCurrency.class).get(0);
        }
        em.persist(settlementCurrency);
        em.flush();
        agencyNotice.setSettlementCurrency(settlementCurrency);
        agencyNoticeRepository.saveAndFlush(agencyNotice);
        Long settlementCurrencyId = settlementCurrency.getId();

        // Get all the agencyNoticeList where settlementCurrency equals to settlementCurrencyId
        defaultAgencyNoticeShouldBeFound("settlementCurrencyId.equals=" + settlementCurrencyId);

        // Get all the agencyNoticeList where settlementCurrency equals to (settlementCurrencyId + 1)
        defaultAgencyNoticeShouldNotBeFound("settlementCurrencyId.equals=" + (settlementCurrencyId + 1));
    }

    @Test
    @Transactional
    void getAllAgencyNoticesByAssessorIsEqualToSomething() throws Exception {
        // Initialize the database
        agencyNoticeRepository.saveAndFlush(agencyNotice);
        Dealer assessor;
        if (TestUtil.findAll(em, Dealer.class).isEmpty()) {
            assessor = DealerResourceIT.createEntity(em);
            em.persist(assessor);
            em.flush();
        } else {
            assessor = TestUtil.findAll(em, Dealer.class).get(0);
        }
        em.persist(assessor);
        em.flush();
        agencyNotice.setAssessor(assessor);
        agencyNoticeRepository.saveAndFlush(agencyNotice);
        Long assessorId = assessor.getId();

        // Get all the agencyNoticeList where assessor equals to assessorId
        defaultAgencyNoticeShouldBeFound("assessorId.equals=" + assessorId);

        // Get all the agencyNoticeList where assessor equals to (assessorId + 1)
        defaultAgencyNoticeShouldNotBeFound("assessorId.equals=" + (assessorId + 1));
    }

    @Test
    @Transactional
    void getAllAgencyNoticesByPlaceholderIsEqualToSomething() throws Exception {
        // Initialize the database
        agencyNoticeRepository.saveAndFlush(agencyNotice);
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
        agencyNotice.addPlaceholder(placeholder);
        agencyNoticeRepository.saveAndFlush(agencyNotice);
        Long placeholderId = placeholder.getId();

        // Get all the agencyNoticeList where placeholder equals to placeholderId
        defaultAgencyNoticeShouldBeFound("placeholderId.equals=" + placeholderId);

        // Get all the agencyNoticeList where placeholder equals to (placeholderId + 1)
        defaultAgencyNoticeShouldNotBeFound("placeholderId.equals=" + (placeholderId + 1));
    }

    @Test
    @Transactional
    void getAllAgencyNoticesByBusinessDocumentIsEqualToSomething() throws Exception {
        // Initialize the database
        agencyNoticeRepository.saveAndFlush(agencyNotice);
        BusinessDocument businessDocument;
        if (TestUtil.findAll(em, BusinessDocument.class).isEmpty()) {
            businessDocument = BusinessDocumentResourceIT.createEntity(em);
            em.persist(businessDocument);
            em.flush();
        } else {
            businessDocument = TestUtil.findAll(em, BusinessDocument.class).get(0);
        }
        em.persist(businessDocument);
        em.flush();
        agencyNotice.addBusinessDocument(businessDocument);
        agencyNoticeRepository.saveAndFlush(agencyNotice);
        Long businessDocumentId = businessDocument.getId();

        // Get all the agencyNoticeList where businessDocument equals to businessDocumentId
        defaultAgencyNoticeShouldBeFound("businessDocumentId.equals=" + businessDocumentId);

        // Get all the agencyNoticeList where businessDocument equals to (businessDocumentId + 1)
        defaultAgencyNoticeShouldNotBeFound("businessDocumentId.equals=" + (businessDocumentId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAgencyNoticeShouldBeFound(String filter) throws Exception {
        restAgencyNoticeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(agencyNotice.getId().intValue())))
            .andExpect(jsonPath("$.[*].referenceNumber").value(hasItem(DEFAULT_REFERENCE_NUMBER)))
            .andExpect(jsonPath("$.[*].referenceDate").value(hasItem(DEFAULT_REFERENCE_DATE.toString())))
            .andExpect(jsonPath("$.[*].assessmentAmount").value(hasItem(sameNumber(DEFAULT_ASSESSMENT_AMOUNT))))
            .andExpect(jsonPath("$.[*].agencyStatus").value(hasItem(DEFAULT_AGENCY_STATUS.toString())))
            .andExpect(jsonPath("$.[*].assessmentNoticeContentType").value(hasItem(DEFAULT_ASSESSMENT_NOTICE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].assessmentNotice").value(hasItem(Base64Utils.encodeToString(DEFAULT_ASSESSMENT_NOTICE))));

        // Check, that the count call also returns 1
        restAgencyNoticeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAgencyNoticeShouldNotBeFound(String filter) throws Exception {
        restAgencyNoticeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAgencyNoticeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAgencyNotice() throws Exception {
        // Get the agencyNotice
        restAgencyNoticeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewAgencyNotice() throws Exception {
        // Initialize the database
        agencyNoticeRepository.saveAndFlush(agencyNotice);

        int databaseSizeBeforeUpdate = agencyNoticeRepository.findAll().size();

        // Update the agencyNotice
        AgencyNotice updatedAgencyNotice = agencyNoticeRepository.findById(agencyNotice.getId()).get();
        // Disconnect from session so that the updates on updatedAgencyNotice are not directly saved in db
        em.detach(updatedAgencyNotice);
        updatedAgencyNotice
            .referenceNumber(UPDATED_REFERENCE_NUMBER)
            .referenceDate(UPDATED_REFERENCE_DATE)
            .assessmentAmount(UPDATED_ASSESSMENT_AMOUNT)
            .agencyStatus(UPDATED_AGENCY_STATUS)
            .assessmentNotice(UPDATED_ASSESSMENT_NOTICE)
            .assessmentNoticeContentType(UPDATED_ASSESSMENT_NOTICE_CONTENT_TYPE);
        AgencyNoticeDTO agencyNoticeDTO = agencyNoticeMapper.toDto(updatedAgencyNotice);

        restAgencyNoticeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, agencyNoticeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(agencyNoticeDTO))
            )
            .andExpect(status().isOk());

        // Validate the AgencyNotice in the database
        List<AgencyNotice> agencyNoticeList = agencyNoticeRepository.findAll();
        assertThat(agencyNoticeList).hasSize(databaseSizeBeforeUpdate);
        AgencyNotice testAgencyNotice = agencyNoticeList.get(agencyNoticeList.size() - 1);
        assertThat(testAgencyNotice.getReferenceNumber()).isEqualTo(UPDATED_REFERENCE_NUMBER);
        assertThat(testAgencyNotice.getReferenceDate()).isEqualTo(UPDATED_REFERENCE_DATE);
        assertThat(testAgencyNotice.getAssessmentAmount()).isEqualTo(UPDATED_ASSESSMENT_AMOUNT);
        assertThat(testAgencyNotice.getAgencyStatus()).isEqualTo(UPDATED_AGENCY_STATUS);
        assertThat(testAgencyNotice.getAssessmentNotice()).isEqualTo(UPDATED_ASSESSMENT_NOTICE);
        assertThat(testAgencyNotice.getAssessmentNoticeContentType()).isEqualTo(UPDATED_ASSESSMENT_NOTICE_CONTENT_TYPE);

        // Validate the AgencyNotice in Elasticsearch
        verify(mockAgencyNoticeSearchRepository).save(testAgencyNotice);
    }

    @Test
    @Transactional
    void putNonExistingAgencyNotice() throws Exception {
        int databaseSizeBeforeUpdate = agencyNoticeRepository.findAll().size();
        agencyNotice.setId(count.incrementAndGet());

        // Create the AgencyNotice
        AgencyNoticeDTO agencyNoticeDTO = agencyNoticeMapper.toDto(agencyNotice);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAgencyNoticeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, agencyNoticeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(agencyNoticeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AgencyNotice in the database
        List<AgencyNotice> agencyNoticeList = agencyNoticeRepository.findAll();
        assertThat(agencyNoticeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AgencyNotice in Elasticsearch
        verify(mockAgencyNoticeSearchRepository, times(0)).save(agencyNotice);
    }

    @Test
    @Transactional
    void putWithIdMismatchAgencyNotice() throws Exception {
        int databaseSizeBeforeUpdate = agencyNoticeRepository.findAll().size();
        agencyNotice.setId(count.incrementAndGet());

        // Create the AgencyNotice
        AgencyNoticeDTO agencyNoticeDTO = agencyNoticeMapper.toDto(agencyNotice);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAgencyNoticeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(agencyNoticeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AgencyNotice in the database
        List<AgencyNotice> agencyNoticeList = agencyNoticeRepository.findAll();
        assertThat(agencyNoticeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AgencyNotice in Elasticsearch
        verify(mockAgencyNoticeSearchRepository, times(0)).save(agencyNotice);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAgencyNotice() throws Exception {
        int databaseSizeBeforeUpdate = agencyNoticeRepository.findAll().size();
        agencyNotice.setId(count.incrementAndGet());

        // Create the AgencyNotice
        AgencyNoticeDTO agencyNoticeDTO = agencyNoticeMapper.toDto(agencyNotice);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAgencyNoticeMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(agencyNoticeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AgencyNotice in the database
        List<AgencyNotice> agencyNoticeList = agencyNoticeRepository.findAll();
        assertThat(agencyNoticeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AgencyNotice in Elasticsearch
        verify(mockAgencyNoticeSearchRepository, times(0)).save(agencyNotice);
    }

    @Test
    @Transactional
    void partialUpdateAgencyNoticeWithPatch() throws Exception {
        // Initialize the database
        agencyNoticeRepository.saveAndFlush(agencyNotice);

        int databaseSizeBeforeUpdate = agencyNoticeRepository.findAll().size();

        // Update the agencyNotice using partial update
        AgencyNotice partialUpdatedAgencyNotice = new AgencyNotice();
        partialUpdatedAgencyNotice.setId(agencyNotice.getId());

        partialUpdatedAgencyNotice
            .assessmentAmount(UPDATED_ASSESSMENT_AMOUNT)
            .agencyStatus(UPDATED_AGENCY_STATUS)
            .assessmentNotice(UPDATED_ASSESSMENT_NOTICE)
            .assessmentNoticeContentType(UPDATED_ASSESSMENT_NOTICE_CONTENT_TYPE);

        restAgencyNoticeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAgencyNotice.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAgencyNotice))
            )
            .andExpect(status().isOk());

        // Validate the AgencyNotice in the database
        List<AgencyNotice> agencyNoticeList = agencyNoticeRepository.findAll();
        assertThat(agencyNoticeList).hasSize(databaseSizeBeforeUpdate);
        AgencyNotice testAgencyNotice = agencyNoticeList.get(agencyNoticeList.size() - 1);
        assertThat(testAgencyNotice.getReferenceNumber()).isEqualTo(DEFAULT_REFERENCE_NUMBER);
        assertThat(testAgencyNotice.getReferenceDate()).isEqualTo(DEFAULT_REFERENCE_DATE);
        assertThat(testAgencyNotice.getAssessmentAmount()).isEqualByComparingTo(UPDATED_ASSESSMENT_AMOUNT);
        assertThat(testAgencyNotice.getAgencyStatus()).isEqualTo(UPDATED_AGENCY_STATUS);
        assertThat(testAgencyNotice.getAssessmentNotice()).isEqualTo(UPDATED_ASSESSMENT_NOTICE);
        assertThat(testAgencyNotice.getAssessmentNoticeContentType()).isEqualTo(UPDATED_ASSESSMENT_NOTICE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateAgencyNoticeWithPatch() throws Exception {
        // Initialize the database
        agencyNoticeRepository.saveAndFlush(agencyNotice);

        int databaseSizeBeforeUpdate = agencyNoticeRepository.findAll().size();

        // Update the agencyNotice using partial update
        AgencyNotice partialUpdatedAgencyNotice = new AgencyNotice();
        partialUpdatedAgencyNotice.setId(agencyNotice.getId());

        partialUpdatedAgencyNotice
            .referenceNumber(UPDATED_REFERENCE_NUMBER)
            .referenceDate(UPDATED_REFERENCE_DATE)
            .assessmentAmount(UPDATED_ASSESSMENT_AMOUNT)
            .agencyStatus(UPDATED_AGENCY_STATUS)
            .assessmentNotice(UPDATED_ASSESSMENT_NOTICE)
            .assessmentNoticeContentType(UPDATED_ASSESSMENT_NOTICE_CONTENT_TYPE);

        restAgencyNoticeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAgencyNotice.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAgencyNotice))
            )
            .andExpect(status().isOk());

        // Validate the AgencyNotice in the database
        List<AgencyNotice> agencyNoticeList = agencyNoticeRepository.findAll();
        assertThat(agencyNoticeList).hasSize(databaseSizeBeforeUpdate);
        AgencyNotice testAgencyNotice = agencyNoticeList.get(agencyNoticeList.size() - 1);
        assertThat(testAgencyNotice.getReferenceNumber()).isEqualTo(UPDATED_REFERENCE_NUMBER);
        assertThat(testAgencyNotice.getReferenceDate()).isEqualTo(UPDATED_REFERENCE_DATE);
        assertThat(testAgencyNotice.getAssessmentAmount()).isEqualByComparingTo(UPDATED_ASSESSMENT_AMOUNT);
        assertThat(testAgencyNotice.getAgencyStatus()).isEqualTo(UPDATED_AGENCY_STATUS);
        assertThat(testAgencyNotice.getAssessmentNotice()).isEqualTo(UPDATED_ASSESSMENT_NOTICE);
        assertThat(testAgencyNotice.getAssessmentNoticeContentType()).isEqualTo(UPDATED_ASSESSMENT_NOTICE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingAgencyNotice() throws Exception {
        int databaseSizeBeforeUpdate = agencyNoticeRepository.findAll().size();
        agencyNotice.setId(count.incrementAndGet());

        // Create the AgencyNotice
        AgencyNoticeDTO agencyNoticeDTO = agencyNoticeMapper.toDto(agencyNotice);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAgencyNoticeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, agencyNoticeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(agencyNoticeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AgencyNotice in the database
        List<AgencyNotice> agencyNoticeList = agencyNoticeRepository.findAll();
        assertThat(agencyNoticeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AgencyNotice in Elasticsearch
        verify(mockAgencyNoticeSearchRepository, times(0)).save(agencyNotice);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAgencyNotice() throws Exception {
        int databaseSizeBeforeUpdate = agencyNoticeRepository.findAll().size();
        agencyNotice.setId(count.incrementAndGet());

        // Create the AgencyNotice
        AgencyNoticeDTO agencyNoticeDTO = agencyNoticeMapper.toDto(agencyNotice);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAgencyNoticeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(agencyNoticeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AgencyNotice in the database
        List<AgencyNotice> agencyNoticeList = agencyNoticeRepository.findAll();
        assertThat(agencyNoticeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AgencyNotice in Elasticsearch
        verify(mockAgencyNoticeSearchRepository, times(0)).save(agencyNotice);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAgencyNotice() throws Exception {
        int databaseSizeBeforeUpdate = agencyNoticeRepository.findAll().size();
        agencyNotice.setId(count.incrementAndGet());

        // Create the AgencyNotice
        AgencyNoticeDTO agencyNoticeDTO = agencyNoticeMapper.toDto(agencyNotice);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAgencyNoticeMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(agencyNoticeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AgencyNotice in the database
        List<AgencyNotice> agencyNoticeList = agencyNoticeRepository.findAll();
        assertThat(agencyNoticeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AgencyNotice in Elasticsearch
        verify(mockAgencyNoticeSearchRepository, times(0)).save(agencyNotice);
    }

    @Test
    @Transactional
    void deleteAgencyNotice() throws Exception {
        // Initialize the database
        agencyNoticeRepository.saveAndFlush(agencyNotice);

        int databaseSizeBeforeDelete = agencyNoticeRepository.findAll().size();

        // Delete the agencyNotice
        restAgencyNoticeMockMvc
            .perform(delete(ENTITY_API_URL_ID, agencyNotice.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AgencyNotice> agencyNoticeList = agencyNoticeRepository.findAll();
        assertThat(agencyNoticeList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the AgencyNotice in Elasticsearch
        verify(mockAgencyNoticeSearchRepository, times(1)).deleteById(agencyNotice.getId());
    }

    @Test
    @Transactional
    void searchAgencyNotice() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        agencyNoticeRepository.saveAndFlush(agencyNotice);
        when(mockAgencyNoticeSearchRepository.search("id:" + agencyNotice.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(agencyNotice), PageRequest.of(0, 1), 1));

        // Search the agencyNotice
        restAgencyNoticeMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + agencyNotice.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(agencyNotice.getId().intValue())))
            .andExpect(jsonPath("$.[*].referenceNumber").value(hasItem(DEFAULT_REFERENCE_NUMBER)))
            .andExpect(jsonPath("$.[*].referenceDate").value(hasItem(DEFAULT_REFERENCE_DATE.toString())))
            .andExpect(jsonPath("$.[*].assessmentAmount").value(hasItem(sameNumber(DEFAULT_ASSESSMENT_AMOUNT))))
            .andExpect(jsonPath("$.[*].agencyStatus").value(hasItem(DEFAULT_AGENCY_STATUS.toString())))
            .andExpect(jsonPath("$.[*].assessmentNoticeContentType").value(hasItem(DEFAULT_ASSESSMENT_NOTICE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].assessmentNotice").value(hasItem(Base64Utils.encodeToString(DEFAULT_ASSESSMENT_NOTICE))));
    }
}
