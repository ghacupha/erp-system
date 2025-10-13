package io.github.erp.erp.resources;

/*-
 * Erp System - Mark X No 10 (Jehoiada Series) Server ver 1.8.2
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
import io.github.erp.domain.*;
import io.github.erp.repository.WorkInProgressRegistrationRepository;
import io.github.erp.repository.search.WorkInProgressRegistrationSearchRepository;
import io.github.erp.service.WorkInProgressRegistrationService;
import io.github.erp.service.dto.WorkInProgressRegistrationDTO;
import io.github.erp.service.mapper.WorkInProgressRegistrationMapper;
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
 * Integration tests for the WorkInProgressRegistrationResourceProd REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser(roles = {"FIXED_ASSETS_USER"})
public class WorkInProgressRegistrationResourceIT {

    private static final String DEFAULT_SEQUENCE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_SEQUENCE_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_PARTICULARS = "AAAAAAAAAA";
    private static final String UPDATED_PARTICULARS = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_INSTALMENT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_INSTALMENT_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_INSTALMENT_DATE = LocalDate.ofEpochDay(-1L);

    private static final BigDecimal DEFAULT_INSTALMENT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_INSTALMENT_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_INSTALMENT_AMOUNT = new BigDecimal(1 - 1);

    private static final byte[] DEFAULT_COMMENTS = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_COMMENTS = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_COMMENTS_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_COMMENTS_CONTENT_TYPE = "image/png";

    private static final Double DEFAULT_LEVEL_OF_COMPLETION = 1D;
    private static final Double UPDATED_LEVEL_OF_COMPLETION = 2D;
    private static final Double SMALLER_LEVEL_OF_COMPLETION = 1D - 1D;

    private static final Boolean DEFAULT_COMPLETED = false;
    private static final Boolean UPDATED_COMPLETED = true;

    private static final String ENTITY_API_URL = "/api/work-in-progress-registrations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/work-in-progress-registrations";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private WorkInProgressRegistrationRepository workInProgressRegistrationRepository;

    @Mock
    private WorkInProgressRegistrationRepository workInProgressRegistrationRepositoryMock;

    @Autowired
    private WorkInProgressRegistrationMapper workInProgressRegistrationMapper;

    @Mock
    private WorkInProgressRegistrationService workInProgressRegistrationServiceMock;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.WorkInProgressRegistrationSearchRepositoryMockConfiguration
     */
    @Autowired
    private WorkInProgressRegistrationSearchRepository mockWorkInProgressRegistrationSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restWorkInProgressRegistrationMockMvc;

    private WorkInProgressRegistration workInProgressRegistration;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WorkInProgressRegistration createEntity(EntityManager em) {
        WorkInProgressRegistration workInProgressRegistration = new WorkInProgressRegistration()
            .sequenceNumber(DEFAULT_SEQUENCE_NUMBER)
            .particulars(DEFAULT_PARTICULARS)
            .instalmentDate(DEFAULT_INSTALMENT_DATE)
            .instalmentAmount(DEFAULT_INSTALMENT_AMOUNT)
            .comments(DEFAULT_COMMENTS)
            .commentsContentType(DEFAULT_COMMENTS_CONTENT_TYPE)
            .levelOfCompletion(DEFAULT_LEVEL_OF_COMPLETION)
            .completed(DEFAULT_COMPLETED);
        return workInProgressRegistration;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WorkInProgressRegistration createUpdatedEntity(EntityManager em) {
        WorkInProgressRegistration workInProgressRegistration = new WorkInProgressRegistration()
            .sequenceNumber(UPDATED_SEQUENCE_NUMBER)
            .particulars(UPDATED_PARTICULARS)
            .instalmentDate(UPDATED_INSTALMENT_DATE)
            .instalmentAmount(UPDATED_INSTALMENT_AMOUNT)
            .comments(UPDATED_COMMENTS)
            .commentsContentType(UPDATED_COMMENTS_CONTENT_TYPE)
            .levelOfCompletion(UPDATED_LEVEL_OF_COMPLETION)
            .completed(UPDATED_COMPLETED);
        return workInProgressRegistration;
    }

    @BeforeEach
    public void initTest() {
        workInProgressRegistration = createEntity(em);
    }

    @Test
    @Transactional
    void createWorkInProgressRegistration() throws Exception {
        int databaseSizeBeforeCreate = workInProgressRegistrationRepository.findAll().size();
        // Create the WorkInProgressRegistration
        WorkInProgressRegistrationDTO workInProgressRegistrationDTO = workInProgressRegistrationMapper.toDto(workInProgressRegistration);
        restWorkInProgressRegistrationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workInProgressRegistrationDTO))
            )
            .andExpect(status().isCreated());

        // Validate the WorkInProgressRegistration in the database
        List<WorkInProgressRegistration> workInProgressRegistrationList = workInProgressRegistrationRepository.findAll();
        assertThat(workInProgressRegistrationList).hasSize(databaseSizeBeforeCreate + 1);
        WorkInProgressRegistration testWorkInProgressRegistration = workInProgressRegistrationList.get(
            workInProgressRegistrationList.size() - 1
        );
        assertThat(testWorkInProgressRegistration.getSequenceNumber()).isEqualTo(DEFAULT_SEQUENCE_NUMBER);
        assertThat(testWorkInProgressRegistration.getParticulars()).isEqualTo(DEFAULT_PARTICULARS);
        assertThat(testWorkInProgressRegistration.getInstalmentDate()).isEqualTo(DEFAULT_INSTALMENT_DATE);
        assertThat(testWorkInProgressRegistration.getInstalmentAmount()).isEqualByComparingTo(DEFAULT_INSTALMENT_AMOUNT);
        assertThat(testWorkInProgressRegistration.getComments()).isEqualTo(DEFAULT_COMMENTS);
        assertThat(testWorkInProgressRegistration.getCommentsContentType()).isEqualTo(DEFAULT_COMMENTS_CONTENT_TYPE);
        assertThat(testWorkInProgressRegistration.getLevelOfCompletion()).isEqualTo(DEFAULT_LEVEL_OF_COMPLETION);
        assertThat(testWorkInProgressRegistration.getCompleted()).isEqualTo(DEFAULT_COMPLETED);

        // Validate the WorkInProgressRegistration in Elasticsearch
        verify(mockWorkInProgressRegistrationSearchRepository, times(1)).save(testWorkInProgressRegistration);
    }

    @Test
    @Transactional
    void createWorkInProgressRegistrationWithExistingId() throws Exception {
        // Create the WorkInProgressRegistration with an existing ID
        workInProgressRegistration.setId(1L);
        WorkInProgressRegistrationDTO workInProgressRegistrationDTO = workInProgressRegistrationMapper.toDto(workInProgressRegistration);

        int databaseSizeBeforeCreate = workInProgressRegistrationRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restWorkInProgressRegistrationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workInProgressRegistrationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkInProgressRegistration in the database
        List<WorkInProgressRegistration> workInProgressRegistrationList = workInProgressRegistrationRepository.findAll();
        assertThat(workInProgressRegistrationList).hasSize(databaseSizeBeforeCreate);

        // Validate the WorkInProgressRegistration in Elasticsearch
        verify(mockWorkInProgressRegistrationSearchRepository, times(0)).save(workInProgressRegistration);
    }

    @Test
    @Transactional
    void checkSequenceNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = workInProgressRegistrationRepository.findAll().size();
        // set the field null
        workInProgressRegistration.setSequenceNumber(null);

        // Create the WorkInProgressRegistration, which fails.
        WorkInProgressRegistrationDTO workInProgressRegistrationDTO = workInProgressRegistrationMapper.toDto(workInProgressRegistration);

        restWorkInProgressRegistrationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workInProgressRegistrationDTO))
            )
            .andExpect(status().isBadRequest());

        List<WorkInProgressRegistration> workInProgressRegistrationList = workInProgressRegistrationRepository.findAll();
        assertThat(workInProgressRegistrationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllWorkInProgressRegistrations() throws Exception {
        // Initialize the database
        workInProgressRegistrationRepository.saveAndFlush(workInProgressRegistration);

        // Get all the workInProgressRegistrationList
        restWorkInProgressRegistrationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(workInProgressRegistration.getId().intValue())))
            .andExpect(jsonPath("$.[*].sequenceNumber").value(hasItem(DEFAULT_SEQUENCE_NUMBER)))
            .andExpect(jsonPath("$.[*].particulars").value(hasItem(DEFAULT_PARTICULARS)))
            .andExpect(jsonPath("$.[*].instalmentDate").value(hasItem(DEFAULT_INSTALMENT_DATE.toString())))
            .andExpect(jsonPath("$.[*].instalmentAmount").value(hasItem(sameNumber(DEFAULT_INSTALMENT_AMOUNT))))
            .andExpect(jsonPath("$.[*].commentsContentType").value(hasItem(DEFAULT_COMMENTS_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].comments").value(hasItem(Base64Utils.encodeToString(DEFAULT_COMMENTS))))
            .andExpect(jsonPath("$.[*].levelOfCompletion").value(hasItem(DEFAULT_LEVEL_OF_COMPLETION.doubleValue())))
            .andExpect(jsonPath("$.[*].completed").value(hasItem(DEFAULT_COMPLETED.booleanValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllWorkInProgressRegistrationsWithEagerRelationshipsIsEnabled() throws Exception {
        when(workInProgressRegistrationServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restWorkInProgressRegistrationMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(workInProgressRegistrationServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllWorkInProgressRegistrationsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(workInProgressRegistrationServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restWorkInProgressRegistrationMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(workInProgressRegistrationServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getWorkInProgressRegistration() throws Exception {
        // Initialize the database
        workInProgressRegistrationRepository.saveAndFlush(workInProgressRegistration);

        // Get the workInProgressRegistration
        restWorkInProgressRegistrationMockMvc
            .perform(get(ENTITY_API_URL_ID, workInProgressRegistration.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(workInProgressRegistration.getId().intValue()))
            .andExpect(jsonPath("$.sequenceNumber").value(DEFAULT_SEQUENCE_NUMBER))
            .andExpect(jsonPath("$.particulars").value(DEFAULT_PARTICULARS))
            .andExpect(jsonPath("$.instalmentDate").value(DEFAULT_INSTALMENT_DATE.toString()))
            .andExpect(jsonPath("$.instalmentAmount").value(sameNumber(DEFAULT_INSTALMENT_AMOUNT)))
            .andExpect(jsonPath("$.commentsContentType").value(DEFAULT_COMMENTS_CONTENT_TYPE))
            .andExpect(jsonPath("$.comments").value(Base64Utils.encodeToString(DEFAULT_COMMENTS)))
            .andExpect(jsonPath("$.levelOfCompletion").value(DEFAULT_LEVEL_OF_COMPLETION.doubleValue()))
            .andExpect(jsonPath("$.completed").value(DEFAULT_COMPLETED.booleanValue()));
    }

    @Test
    @Transactional
    void getWorkInProgressRegistrationsByIdFiltering() throws Exception {
        // Initialize the database
        workInProgressRegistrationRepository.saveAndFlush(workInProgressRegistration);

        Long id = workInProgressRegistration.getId();

        defaultWorkInProgressRegistrationShouldBeFound("id.equals=" + id);
        defaultWorkInProgressRegistrationShouldNotBeFound("id.notEquals=" + id);

        defaultWorkInProgressRegistrationShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultWorkInProgressRegistrationShouldNotBeFound("id.greaterThan=" + id);

        defaultWorkInProgressRegistrationShouldBeFound("id.lessThanOrEqual=" + id);
        defaultWorkInProgressRegistrationShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllWorkInProgressRegistrationsBySequenceNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        workInProgressRegistrationRepository.saveAndFlush(workInProgressRegistration);

        // Get all the workInProgressRegistrationList where sequenceNumber equals to DEFAULT_SEQUENCE_NUMBER
        defaultWorkInProgressRegistrationShouldBeFound("sequenceNumber.equals=" + DEFAULT_SEQUENCE_NUMBER);

        // Get all the workInProgressRegistrationList where sequenceNumber equals to UPDATED_SEQUENCE_NUMBER
        defaultWorkInProgressRegistrationShouldNotBeFound("sequenceNumber.equals=" + UPDATED_SEQUENCE_NUMBER);
    }

    @Test
    @Transactional
    void getAllWorkInProgressRegistrationsBySequenceNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        workInProgressRegistrationRepository.saveAndFlush(workInProgressRegistration);

        // Get all the workInProgressRegistrationList where sequenceNumber not equals to DEFAULT_SEQUENCE_NUMBER
        defaultWorkInProgressRegistrationShouldNotBeFound("sequenceNumber.notEquals=" + DEFAULT_SEQUENCE_NUMBER);

        // Get all the workInProgressRegistrationList where sequenceNumber not equals to UPDATED_SEQUENCE_NUMBER
        defaultWorkInProgressRegistrationShouldBeFound("sequenceNumber.notEquals=" + UPDATED_SEQUENCE_NUMBER);
    }

    @Test
    @Transactional
    void getAllWorkInProgressRegistrationsBySequenceNumberIsInShouldWork() throws Exception {
        // Initialize the database
        workInProgressRegistrationRepository.saveAndFlush(workInProgressRegistration);

        // Get all the workInProgressRegistrationList where sequenceNumber in DEFAULT_SEQUENCE_NUMBER or UPDATED_SEQUENCE_NUMBER
        defaultWorkInProgressRegistrationShouldBeFound("sequenceNumber.in=" + DEFAULT_SEQUENCE_NUMBER + "," + UPDATED_SEQUENCE_NUMBER);

        // Get all the workInProgressRegistrationList where sequenceNumber equals to UPDATED_SEQUENCE_NUMBER
        defaultWorkInProgressRegistrationShouldNotBeFound("sequenceNumber.in=" + UPDATED_SEQUENCE_NUMBER);
    }

    @Test
    @Transactional
    void getAllWorkInProgressRegistrationsBySequenceNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        workInProgressRegistrationRepository.saveAndFlush(workInProgressRegistration);

        // Get all the workInProgressRegistrationList where sequenceNumber is not null
        defaultWorkInProgressRegistrationShouldBeFound("sequenceNumber.specified=true");

        // Get all the workInProgressRegistrationList where sequenceNumber is null
        defaultWorkInProgressRegistrationShouldNotBeFound("sequenceNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllWorkInProgressRegistrationsBySequenceNumberContainsSomething() throws Exception {
        // Initialize the database
        workInProgressRegistrationRepository.saveAndFlush(workInProgressRegistration);

        // Get all the workInProgressRegistrationList where sequenceNumber contains DEFAULT_SEQUENCE_NUMBER
        defaultWorkInProgressRegistrationShouldBeFound("sequenceNumber.contains=" + DEFAULT_SEQUENCE_NUMBER);

        // Get all the workInProgressRegistrationList where sequenceNumber contains UPDATED_SEQUENCE_NUMBER
        defaultWorkInProgressRegistrationShouldNotBeFound("sequenceNumber.contains=" + UPDATED_SEQUENCE_NUMBER);
    }

    @Test
    @Transactional
    void getAllWorkInProgressRegistrationsBySequenceNumberNotContainsSomething() throws Exception {
        // Initialize the database
        workInProgressRegistrationRepository.saveAndFlush(workInProgressRegistration);

        // Get all the workInProgressRegistrationList where sequenceNumber does not contain DEFAULT_SEQUENCE_NUMBER
        defaultWorkInProgressRegistrationShouldNotBeFound("sequenceNumber.doesNotContain=" + DEFAULT_SEQUENCE_NUMBER);

        // Get all the workInProgressRegistrationList where sequenceNumber does not contain UPDATED_SEQUENCE_NUMBER
        defaultWorkInProgressRegistrationShouldBeFound("sequenceNumber.doesNotContain=" + UPDATED_SEQUENCE_NUMBER);
    }

    @Test
    @Transactional
    void getAllWorkInProgressRegistrationsByParticularsIsEqualToSomething() throws Exception {
        // Initialize the database
        workInProgressRegistrationRepository.saveAndFlush(workInProgressRegistration);

        // Get all the workInProgressRegistrationList where particulars equals to DEFAULT_PARTICULARS
        defaultWorkInProgressRegistrationShouldBeFound("particulars.equals=" + DEFAULT_PARTICULARS);

        // Get all the workInProgressRegistrationList where particulars equals to UPDATED_PARTICULARS
        defaultWorkInProgressRegistrationShouldNotBeFound("particulars.equals=" + UPDATED_PARTICULARS);
    }

    @Test
    @Transactional
    void getAllWorkInProgressRegistrationsByParticularsIsNotEqualToSomething() throws Exception {
        // Initialize the database
        workInProgressRegistrationRepository.saveAndFlush(workInProgressRegistration);

        // Get all the workInProgressRegistrationList where particulars not equals to DEFAULT_PARTICULARS
        defaultWorkInProgressRegistrationShouldNotBeFound("particulars.notEquals=" + DEFAULT_PARTICULARS);

        // Get all the workInProgressRegistrationList where particulars not equals to UPDATED_PARTICULARS
        defaultWorkInProgressRegistrationShouldBeFound("particulars.notEquals=" + UPDATED_PARTICULARS);
    }

    @Test
    @Transactional
    void getAllWorkInProgressRegistrationsByParticularsIsInShouldWork() throws Exception {
        // Initialize the database
        workInProgressRegistrationRepository.saveAndFlush(workInProgressRegistration);

        // Get all the workInProgressRegistrationList where particulars in DEFAULT_PARTICULARS or UPDATED_PARTICULARS
        defaultWorkInProgressRegistrationShouldBeFound("particulars.in=" + DEFAULT_PARTICULARS + "," + UPDATED_PARTICULARS);

        // Get all the workInProgressRegistrationList where particulars equals to UPDATED_PARTICULARS
        defaultWorkInProgressRegistrationShouldNotBeFound("particulars.in=" + UPDATED_PARTICULARS);
    }

    @Test
    @Transactional
    void getAllWorkInProgressRegistrationsByParticularsIsNullOrNotNull() throws Exception {
        // Initialize the database
        workInProgressRegistrationRepository.saveAndFlush(workInProgressRegistration);

        // Get all the workInProgressRegistrationList where particulars is not null
        defaultWorkInProgressRegistrationShouldBeFound("particulars.specified=true");

        // Get all the workInProgressRegistrationList where particulars is null
        defaultWorkInProgressRegistrationShouldNotBeFound("particulars.specified=false");
    }

    @Test
    @Transactional
    void getAllWorkInProgressRegistrationsByParticularsContainsSomething() throws Exception {
        // Initialize the database
        workInProgressRegistrationRepository.saveAndFlush(workInProgressRegistration);

        // Get all the workInProgressRegistrationList where particulars contains DEFAULT_PARTICULARS
        defaultWorkInProgressRegistrationShouldBeFound("particulars.contains=" + DEFAULT_PARTICULARS);

        // Get all the workInProgressRegistrationList where particulars contains UPDATED_PARTICULARS
        defaultWorkInProgressRegistrationShouldNotBeFound("particulars.contains=" + UPDATED_PARTICULARS);
    }

    @Test
    @Transactional
    void getAllWorkInProgressRegistrationsByParticularsNotContainsSomething() throws Exception {
        // Initialize the database
        workInProgressRegistrationRepository.saveAndFlush(workInProgressRegistration);

        // Get all the workInProgressRegistrationList where particulars does not contain DEFAULT_PARTICULARS
        defaultWorkInProgressRegistrationShouldNotBeFound("particulars.doesNotContain=" + DEFAULT_PARTICULARS);

        // Get all the workInProgressRegistrationList where particulars does not contain UPDATED_PARTICULARS
        defaultWorkInProgressRegistrationShouldBeFound("particulars.doesNotContain=" + UPDATED_PARTICULARS);
    }

    @Test
    @Transactional
    void getAllWorkInProgressRegistrationsByInstalmentDateIsEqualToSomething() throws Exception {
        // Initialize the database
        workInProgressRegistrationRepository.saveAndFlush(workInProgressRegistration);

        // Get all the workInProgressRegistrationList where instalmentDate equals to DEFAULT_INSTALMENT_DATE
        defaultWorkInProgressRegistrationShouldBeFound("instalmentDate.equals=" + DEFAULT_INSTALMENT_DATE);

        // Get all the workInProgressRegistrationList where instalmentDate equals to UPDATED_INSTALMENT_DATE
        defaultWorkInProgressRegistrationShouldNotBeFound("instalmentDate.equals=" + UPDATED_INSTALMENT_DATE);
    }

    @Test
    @Transactional
    void getAllWorkInProgressRegistrationsByInstalmentDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        workInProgressRegistrationRepository.saveAndFlush(workInProgressRegistration);

        // Get all the workInProgressRegistrationList where instalmentDate not equals to DEFAULT_INSTALMENT_DATE
        defaultWorkInProgressRegistrationShouldNotBeFound("instalmentDate.notEquals=" + DEFAULT_INSTALMENT_DATE);

        // Get all the workInProgressRegistrationList where instalmentDate not equals to UPDATED_INSTALMENT_DATE
        defaultWorkInProgressRegistrationShouldBeFound("instalmentDate.notEquals=" + UPDATED_INSTALMENT_DATE);
    }

    @Test
    @Transactional
    void getAllWorkInProgressRegistrationsByInstalmentDateIsInShouldWork() throws Exception {
        // Initialize the database
        workInProgressRegistrationRepository.saveAndFlush(workInProgressRegistration);

        // Get all the workInProgressRegistrationList where instalmentDate in DEFAULT_INSTALMENT_DATE or UPDATED_INSTALMENT_DATE
        defaultWorkInProgressRegistrationShouldBeFound("instalmentDate.in=" + DEFAULT_INSTALMENT_DATE + "," + UPDATED_INSTALMENT_DATE);

        // Get all the workInProgressRegistrationList where instalmentDate equals to UPDATED_INSTALMENT_DATE
        defaultWorkInProgressRegistrationShouldNotBeFound("instalmentDate.in=" + UPDATED_INSTALMENT_DATE);
    }

    @Test
    @Transactional
    void getAllWorkInProgressRegistrationsByInstalmentDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        workInProgressRegistrationRepository.saveAndFlush(workInProgressRegistration);

        // Get all the workInProgressRegistrationList where instalmentDate is not null
        defaultWorkInProgressRegistrationShouldBeFound("instalmentDate.specified=true");

        // Get all the workInProgressRegistrationList where instalmentDate is null
        defaultWorkInProgressRegistrationShouldNotBeFound("instalmentDate.specified=false");
    }

    @Test
    @Transactional
    void getAllWorkInProgressRegistrationsByInstalmentDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        workInProgressRegistrationRepository.saveAndFlush(workInProgressRegistration);

        // Get all the workInProgressRegistrationList where instalmentDate is greater than or equal to DEFAULT_INSTALMENT_DATE
        defaultWorkInProgressRegistrationShouldBeFound("instalmentDate.greaterThanOrEqual=" + DEFAULT_INSTALMENT_DATE);

        // Get all the workInProgressRegistrationList where instalmentDate is greater than or equal to UPDATED_INSTALMENT_DATE
        defaultWorkInProgressRegistrationShouldNotBeFound("instalmentDate.greaterThanOrEqual=" + UPDATED_INSTALMENT_DATE);
    }

    @Test
    @Transactional
    void getAllWorkInProgressRegistrationsByInstalmentDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        workInProgressRegistrationRepository.saveAndFlush(workInProgressRegistration);

        // Get all the workInProgressRegistrationList where instalmentDate is less than or equal to DEFAULT_INSTALMENT_DATE
        defaultWorkInProgressRegistrationShouldBeFound("instalmentDate.lessThanOrEqual=" + DEFAULT_INSTALMENT_DATE);

        // Get all the workInProgressRegistrationList where instalmentDate is less than or equal to SMALLER_INSTALMENT_DATE
        defaultWorkInProgressRegistrationShouldNotBeFound("instalmentDate.lessThanOrEqual=" + SMALLER_INSTALMENT_DATE);
    }

    @Test
    @Transactional
    void getAllWorkInProgressRegistrationsByInstalmentDateIsLessThanSomething() throws Exception {
        // Initialize the database
        workInProgressRegistrationRepository.saveAndFlush(workInProgressRegistration);

        // Get all the workInProgressRegistrationList where instalmentDate is less than DEFAULT_INSTALMENT_DATE
        defaultWorkInProgressRegistrationShouldNotBeFound("instalmentDate.lessThan=" + DEFAULT_INSTALMENT_DATE);

        // Get all the workInProgressRegistrationList where instalmentDate is less than UPDATED_INSTALMENT_DATE
        defaultWorkInProgressRegistrationShouldBeFound("instalmentDate.lessThan=" + UPDATED_INSTALMENT_DATE);
    }

    @Test
    @Transactional
    void getAllWorkInProgressRegistrationsByInstalmentDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        workInProgressRegistrationRepository.saveAndFlush(workInProgressRegistration);

        // Get all the workInProgressRegistrationList where instalmentDate is greater than DEFAULT_INSTALMENT_DATE
        defaultWorkInProgressRegistrationShouldNotBeFound("instalmentDate.greaterThan=" + DEFAULT_INSTALMENT_DATE);

        // Get all the workInProgressRegistrationList where instalmentDate is greater than SMALLER_INSTALMENT_DATE
        defaultWorkInProgressRegistrationShouldBeFound("instalmentDate.greaterThan=" + SMALLER_INSTALMENT_DATE);
    }

    @Test
    @Transactional
    void getAllWorkInProgressRegistrationsByInstalmentAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        workInProgressRegistrationRepository.saveAndFlush(workInProgressRegistration);

        // Get all the workInProgressRegistrationList where instalmentAmount equals to DEFAULT_INSTALMENT_AMOUNT
        defaultWorkInProgressRegistrationShouldBeFound("instalmentAmount.equals=" + DEFAULT_INSTALMENT_AMOUNT);

        // Get all the workInProgressRegistrationList where instalmentAmount equals to UPDATED_INSTALMENT_AMOUNT
        defaultWorkInProgressRegistrationShouldNotBeFound("instalmentAmount.equals=" + UPDATED_INSTALMENT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllWorkInProgressRegistrationsByInstalmentAmountIsNotEqualToSomething() throws Exception {
        // Initialize the database
        workInProgressRegistrationRepository.saveAndFlush(workInProgressRegistration);

        // Get all the workInProgressRegistrationList where instalmentAmount not equals to DEFAULT_INSTALMENT_AMOUNT
        defaultWorkInProgressRegistrationShouldNotBeFound("instalmentAmount.notEquals=" + DEFAULT_INSTALMENT_AMOUNT);

        // Get all the workInProgressRegistrationList where instalmentAmount not equals to UPDATED_INSTALMENT_AMOUNT
        defaultWorkInProgressRegistrationShouldBeFound("instalmentAmount.notEquals=" + UPDATED_INSTALMENT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllWorkInProgressRegistrationsByInstalmentAmountIsInShouldWork() throws Exception {
        // Initialize the database
        workInProgressRegistrationRepository.saveAndFlush(workInProgressRegistration);

        // Get all the workInProgressRegistrationList where instalmentAmount in DEFAULT_INSTALMENT_AMOUNT or UPDATED_INSTALMENT_AMOUNT
        defaultWorkInProgressRegistrationShouldBeFound(
            "instalmentAmount.in=" + DEFAULT_INSTALMENT_AMOUNT + "," + UPDATED_INSTALMENT_AMOUNT
        );

        // Get all the workInProgressRegistrationList where instalmentAmount equals to UPDATED_INSTALMENT_AMOUNT
        defaultWorkInProgressRegistrationShouldNotBeFound("instalmentAmount.in=" + UPDATED_INSTALMENT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllWorkInProgressRegistrationsByInstalmentAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        workInProgressRegistrationRepository.saveAndFlush(workInProgressRegistration);

        // Get all the workInProgressRegistrationList where instalmentAmount is not null
        defaultWorkInProgressRegistrationShouldBeFound("instalmentAmount.specified=true");

        // Get all the workInProgressRegistrationList where instalmentAmount is null
        defaultWorkInProgressRegistrationShouldNotBeFound("instalmentAmount.specified=false");
    }

    @Test
    @Transactional
    void getAllWorkInProgressRegistrationsByInstalmentAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        workInProgressRegistrationRepository.saveAndFlush(workInProgressRegistration);

        // Get all the workInProgressRegistrationList where instalmentAmount is greater than or equal to DEFAULT_INSTALMENT_AMOUNT
        defaultWorkInProgressRegistrationShouldBeFound("instalmentAmount.greaterThanOrEqual=" + DEFAULT_INSTALMENT_AMOUNT);

        // Get all the workInProgressRegistrationList where instalmentAmount is greater than or equal to UPDATED_INSTALMENT_AMOUNT
        defaultWorkInProgressRegistrationShouldNotBeFound("instalmentAmount.greaterThanOrEqual=" + UPDATED_INSTALMENT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllWorkInProgressRegistrationsByInstalmentAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        workInProgressRegistrationRepository.saveAndFlush(workInProgressRegistration);

        // Get all the workInProgressRegistrationList where instalmentAmount is less than or equal to DEFAULT_INSTALMENT_AMOUNT
        defaultWorkInProgressRegistrationShouldBeFound("instalmentAmount.lessThanOrEqual=" + DEFAULT_INSTALMENT_AMOUNT);

        // Get all the workInProgressRegistrationList where instalmentAmount is less than or equal to SMALLER_INSTALMENT_AMOUNT
        defaultWorkInProgressRegistrationShouldNotBeFound("instalmentAmount.lessThanOrEqual=" + SMALLER_INSTALMENT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllWorkInProgressRegistrationsByInstalmentAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        workInProgressRegistrationRepository.saveAndFlush(workInProgressRegistration);

        // Get all the workInProgressRegistrationList where instalmentAmount is less than DEFAULT_INSTALMENT_AMOUNT
        defaultWorkInProgressRegistrationShouldNotBeFound("instalmentAmount.lessThan=" + DEFAULT_INSTALMENT_AMOUNT);

        // Get all the workInProgressRegistrationList where instalmentAmount is less than UPDATED_INSTALMENT_AMOUNT
        defaultWorkInProgressRegistrationShouldBeFound("instalmentAmount.lessThan=" + UPDATED_INSTALMENT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllWorkInProgressRegistrationsByInstalmentAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        workInProgressRegistrationRepository.saveAndFlush(workInProgressRegistration);

        // Get all the workInProgressRegistrationList where instalmentAmount is greater than DEFAULT_INSTALMENT_AMOUNT
        defaultWorkInProgressRegistrationShouldNotBeFound("instalmentAmount.greaterThan=" + DEFAULT_INSTALMENT_AMOUNT);

        // Get all the workInProgressRegistrationList where instalmentAmount is greater than SMALLER_INSTALMENT_AMOUNT
        defaultWorkInProgressRegistrationShouldBeFound("instalmentAmount.greaterThan=" + SMALLER_INSTALMENT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllWorkInProgressRegistrationsByLevelOfCompletionIsEqualToSomething() throws Exception {
        // Initialize the database
        workInProgressRegistrationRepository.saveAndFlush(workInProgressRegistration);

        // Get all the workInProgressRegistrationList where levelOfCompletion equals to DEFAULT_LEVEL_OF_COMPLETION
        defaultWorkInProgressRegistrationShouldBeFound("levelOfCompletion.equals=" + DEFAULT_LEVEL_OF_COMPLETION);

        // Get all the workInProgressRegistrationList where levelOfCompletion equals to UPDATED_LEVEL_OF_COMPLETION
        defaultWorkInProgressRegistrationShouldNotBeFound("levelOfCompletion.equals=" + UPDATED_LEVEL_OF_COMPLETION);
    }

    @Test
    @Transactional
    void getAllWorkInProgressRegistrationsByLevelOfCompletionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        workInProgressRegistrationRepository.saveAndFlush(workInProgressRegistration);

        // Get all the workInProgressRegistrationList where levelOfCompletion not equals to DEFAULT_LEVEL_OF_COMPLETION
        defaultWorkInProgressRegistrationShouldNotBeFound("levelOfCompletion.notEquals=" + DEFAULT_LEVEL_OF_COMPLETION);

        // Get all the workInProgressRegistrationList where levelOfCompletion not equals to UPDATED_LEVEL_OF_COMPLETION
        defaultWorkInProgressRegistrationShouldBeFound("levelOfCompletion.notEquals=" + UPDATED_LEVEL_OF_COMPLETION);
    }

    @Test
    @Transactional
    void getAllWorkInProgressRegistrationsByLevelOfCompletionIsInShouldWork() throws Exception {
        // Initialize the database
        workInProgressRegistrationRepository.saveAndFlush(workInProgressRegistration);

        // Get all the workInProgressRegistrationList where levelOfCompletion in DEFAULT_LEVEL_OF_COMPLETION or UPDATED_LEVEL_OF_COMPLETION
        defaultWorkInProgressRegistrationShouldBeFound(
            "levelOfCompletion.in=" + DEFAULT_LEVEL_OF_COMPLETION + "," + UPDATED_LEVEL_OF_COMPLETION
        );

        // Get all the workInProgressRegistrationList where levelOfCompletion equals to UPDATED_LEVEL_OF_COMPLETION
        defaultWorkInProgressRegistrationShouldNotBeFound("levelOfCompletion.in=" + UPDATED_LEVEL_OF_COMPLETION);
    }

    @Test
    @Transactional
    void getAllWorkInProgressRegistrationsByLevelOfCompletionIsNullOrNotNull() throws Exception {
        // Initialize the database
        workInProgressRegistrationRepository.saveAndFlush(workInProgressRegistration);

        // Get all the workInProgressRegistrationList where levelOfCompletion is not null
        defaultWorkInProgressRegistrationShouldBeFound("levelOfCompletion.specified=true");

        // Get all the workInProgressRegistrationList where levelOfCompletion is null
        defaultWorkInProgressRegistrationShouldNotBeFound("levelOfCompletion.specified=false");
    }

    @Test
    @Transactional
    void getAllWorkInProgressRegistrationsByLevelOfCompletionIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        workInProgressRegistrationRepository.saveAndFlush(workInProgressRegistration);

        // Get all the workInProgressRegistrationList where levelOfCompletion is greater than or equal to DEFAULT_LEVEL_OF_COMPLETION
        defaultWorkInProgressRegistrationShouldBeFound("levelOfCompletion.greaterThanOrEqual=" + DEFAULT_LEVEL_OF_COMPLETION);

        // Get all the workInProgressRegistrationList where levelOfCompletion is greater than or equal to UPDATED_LEVEL_OF_COMPLETION
        defaultWorkInProgressRegistrationShouldNotBeFound("levelOfCompletion.greaterThanOrEqual=" + UPDATED_LEVEL_OF_COMPLETION);
    }

    @Test
    @Transactional
    void getAllWorkInProgressRegistrationsByLevelOfCompletionIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        workInProgressRegistrationRepository.saveAndFlush(workInProgressRegistration);

        // Get all the workInProgressRegistrationList where levelOfCompletion is less than or equal to DEFAULT_LEVEL_OF_COMPLETION
        defaultWorkInProgressRegistrationShouldBeFound("levelOfCompletion.lessThanOrEqual=" + DEFAULT_LEVEL_OF_COMPLETION);

        // Get all the workInProgressRegistrationList where levelOfCompletion is less than or equal to SMALLER_LEVEL_OF_COMPLETION
        defaultWorkInProgressRegistrationShouldNotBeFound("levelOfCompletion.lessThanOrEqual=" + SMALLER_LEVEL_OF_COMPLETION);
    }

    @Test
    @Transactional
    void getAllWorkInProgressRegistrationsByLevelOfCompletionIsLessThanSomething() throws Exception {
        // Initialize the database
        workInProgressRegistrationRepository.saveAndFlush(workInProgressRegistration);

        // Get all the workInProgressRegistrationList where levelOfCompletion is less than DEFAULT_LEVEL_OF_COMPLETION
        defaultWorkInProgressRegistrationShouldNotBeFound("levelOfCompletion.lessThan=" + DEFAULT_LEVEL_OF_COMPLETION);

        // Get all the workInProgressRegistrationList where levelOfCompletion is less than UPDATED_LEVEL_OF_COMPLETION
        defaultWorkInProgressRegistrationShouldBeFound("levelOfCompletion.lessThan=" + UPDATED_LEVEL_OF_COMPLETION);
    }

    @Test
    @Transactional
    void getAllWorkInProgressRegistrationsByLevelOfCompletionIsGreaterThanSomething() throws Exception {
        // Initialize the database
        workInProgressRegistrationRepository.saveAndFlush(workInProgressRegistration);

        // Get all the workInProgressRegistrationList where levelOfCompletion is greater than DEFAULT_LEVEL_OF_COMPLETION
        defaultWorkInProgressRegistrationShouldNotBeFound("levelOfCompletion.greaterThan=" + DEFAULT_LEVEL_OF_COMPLETION);

        // Get all the workInProgressRegistrationList where levelOfCompletion is greater than SMALLER_LEVEL_OF_COMPLETION
        defaultWorkInProgressRegistrationShouldBeFound("levelOfCompletion.greaterThan=" + SMALLER_LEVEL_OF_COMPLETION);
    }

    @Test
    @Transactional
    void getAllWorkInProgressRegistrationsByCompletedIsEqualToSomething() throws Exception {
        // Initialize the database
        workInProgressRegistrationRepository.saveAndFlush(workInProgressRegistration);

        // Get all the workInProgressRegistrationList where completed equals to DEFAULT_COMPLETED
        defaultWorkInProgressRegistrationShouldBeFound("completed.equals=" + DEFAULT_COMPLETED);

        // Get all the workInProgressRegistrationList where completed equals to UPDATED_COMPLETED
        defaultWorkInProgressRegistrationShouldNotBeFound("completed.equals=" + UPDATED_COMPLETED);
    }

    @Test
    @Transactional
    void getAllWorkInProgressRegistrationsByCompletedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        workInProgressRegistrationRepository.saveAndFlush(workInProgressRegistration);

        // Get all the workInProgressRegistrationList where completed not equals to DEFAULT_COMPLETED
        defaultWorkInProgressRegistrationShouldNotBeFound("completed.notEquals=" + DEFAULT_COMPLETED);

        // Get all the workInProgressRegistrationList where completed not equals to UPDATED_COMPLETED
        defaultWorkInProgressRegistrationShouldBeFound("completed.notEquals=" + UPDATED_COMPLETED);
    }

    @Test
    @Transactional
    void getAllWorkInProgressRegistrationsByCompletedIsInShouldWork() throws Exception {
        // Initialize the database
        workInProgressRegistrationRepository.saveAndFlush(workInProgressRegistration);

        // Get all the workInProgressRegistrationList where completed in DEFAULT_COMPLETED or UPDATED_COMPLETED
        defaultWorkInProgressRegistrationShouldBeFound("completed.in=" + DEFAULT_COMPLETED + "," + UPDATED_COMPLETED);

        // Get all the workInProgressRegistrationList where completed equals to UPDATED_COMPLETED
        defaultWorkInProgressRegistrationShouldNotBeFound("completed.in=" + UPDATED_COMPLETED);
    }

    @Test
    @Transactional
    void getAllWorkInProgressRegistrationsByCompletedIsNullOrNotNull() throws Exception {
        // Initialize the database
        workInProgressRegistrationRepository.saveAndFlush(workInProgressRegistration);

        // Get all the workInProgressRegistrationList where completed is not null
        defaultWorkInProgressRegistrationShouldBeFound("completed.specified=true");

        // Get all the workInProgressRegistrationList where completed is null
        defaultWorkInProgressRegistrationShouldNotBeFound("completed.specified=false");
    }

    @Test
    @Transactional
    void getAllWorkInProgressRegistrationsByPlaceholderIsEqualToSomething() throws Exception {
        // Initialize the database
        workInProgressRegistrationRepository.saveAndFlush(workInProgressRegistration);
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
        workInProgressRegistration.addPlaceholder(placeholder);
        workInProgressRegistrationRepository.saveAndFlush(workInProgressRegistration);
        Long placeholderId = placeholder.getId();

        // Get all the workInProgressRegistrationList where placeholder equals to placeholderId
        defaultWorkInProgressRegistrationShouldBeFound("placeholderId.equals=" + placeholderId);

        // Get all the workInProgressRegistrationList where placeholder equals to (placeholderId + 1)
        defaultWorkInProgressRegistrationShouldNotBeFound("placeholderId.equals=" + (placeholderId + 1));
    }

    @Test
    @Transactional
    void getAllWorkInProgressRegistrationsByWorkInProgressGroupIsEqualToSomething() throws Exception {
        // Initialize the database
        workInProgressRegistrationRepository.saveAndFlush(workInProgressRegistration);
        WorkInProgressRegistration workInProgressGroup;
        if (TestUtil.findAll(em, WorkInProgressRegistration.class).isEmpty()) {
            workInProgressGroup = WorkInProgressRegistrationResourceIT.createEntity(em);
            em.persist(workInProgressGroup);
            em.flush();
        } else {
            workInProgressGroup = TestUtil.findAll(em, WorkInProgressRegistration.class).get(0);
        }
        em.persist(workInProgressGroup);
        em.flush();
        workInProgressRegistration.setWorkInProgressGroup(workInProgressGroup);
        workInProgressRegistrationRepository.saveAndFlush(workInProgressRegistration);
        Long workInProgressGroupId = workInProgressGroup.getId();

        // Get all the workInProgressRegistrationList where workInProgressGroup equals to workInProgressGroupId
        defaultWorkInProgressRegistrationShouldBeFound("workInProgressGroupId.equals=" + workInProgressGroupId);

        // Get all the workInProgressRegistrationList where workInProgressGroup equals to (workInProgressGroupId + 1)
        defaultWorkInProgressRegistrationShouldNotBeFound("workInProgressGroupId.equals=" + (workInProgressGroupId + 1));
    }

    @Test
    @Transactional
    void getAllWorkInProgressRegistrationsBySettlementCurrencyIsEqualToSomething() throws Exception {
        // Initialize the database
        workInProgressRegistrationRepository.saveAndFlush(workInProgressRegistration);
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
        workInProgressRegistration.setSettlementCurrency(settlementCurrency);
        workInProgressRegistrationRepository.saveAndFlush(workInProgressRegistration);
        Long settlementCurrencyId = settlementCurrency.getId();

        // Get all the workInProgressRegistrationList where settlementCurrency equals to settlementCurrencyId
        defaultWorkInProgressRegistrationShouldBeFound("settlementCurrencyId.equals=" + settlementCurrencyId);

        // Get all the workInProgressRegistrationList where settlementCurrency equals to (settlementCurrencyId + 1)
        defaultWorkInProgressRegistrationShouldNotBeFound("settlementCurrencyId.equals=" + (settlementCurrencyId + 1));
    }

    @Test
    @Transactional
    void getAllWorkInProgressRegistrationsByWorkProjectRegisterIsEqualToSomething() throws Exception {
        // Initialize the database
        workInProgressRegistrationRepository.saveAndFlush(workInProgressRegistration);
        WorkProjectRegister workProjectRegister;
        if (TestUtil.findAll(em, WorkProjectRegister.class).isEmpty()) {
            workProjectRegister = WorkProjectRegisterResourceIT.createEntity(em);
            em.persist(workProjectRegister);
            em.flush();
        } else {
            workProjectRegister = TestUtil.findAll(em, WorkProjectRegister.class).get(0);
        }
        em.persist(workProjectRegister);
        em.flush();
        workInProgressRegistration.setWorkProjectRegister(workProjectRegister);
        workInProgressRegistrationRepository.saveAndFlush(workInProgressRegistration);
        Long workProjectRegisterId = workProjectRegister.getId();

        // Get all the workInProgressRegistrationList where workProjectRegister equals to workProjectRegisterId
        defaultWorkInProgressRegistrationShouldBeFound("workProjectRegisterId.equals=" + workProjectRegisterId);

        // Get all the workInProgressRegistrationList where workProjectRegister equals to (workProjectRegisterId + 1)
        defaultWorkInProgressRegistrationShouldNotBeFound("workProjectRegisterId.equals=" + (workProjectRegisterId + 1));
    }

    @Test
    @Transactional
    void getAllWorkInProgressRegistrationsByBusinessDocumentIsEqualToSomething() throws Exception {
        // Initialize the database
        workInProgressRegistrationRepository.saveAndFlush(workInProgressRegistration);
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
        workInProgressRegistration.addBusinessDocument(businessDocument);
        workInProgressRegistrationRepository.saveAndFlush(workInProgressRegistration);
        Long businessDocumentId = businessDocument.getId();

        // Get all the workInProgressRegistrationList where businessDocument equals to businessDocumentId
        defaultWorkInProgressRegistrationShouldBeFound("businessDocumentId.equals=" + businessDocumentId);

        // Get all the workInProgressRegistrationList where businessDocument equals to (businessDocumentId + 1)
        defaultWorkInProgressRegistrationShouldNotBeFound("businessDocumentId.equals=" + (businessDocumentId + 1));
    }

    @Test
    @Transactional
    void getAllWorkInProgressRegistrationsByAssetAccessoryIsEqualToSomething() throws Exception {
        // Initialize the database
        workInProgressRegistrationRepository.saveAndFlush(workInProgressRegistration);
        AssetAccessory assetAccessory;
        if (TestUtil.findAll(em, AssetAccessory.class).isEmpty()) {
            assetAccessory = AssetAccessoryResourceIT.createEntity(em);
            em.persist(assetAccessory);
            em.flush();
        } else {
            assetAccessory = TestUtil.findAll(em, AssetAccessory.class).get(0);
        }
        em.persist(assetAccessory);
        em.flush();
        workInProgressRegistration.addAssetAccessory(assetAccessory);
        workInProgressRegistrationRepository.saveAndFlush(workInProgressRegistration);
        Long assetAccessoryId = assetAccessory.getId();

        // Get all the workInProgressRegistrationList where assetAccessory equals to assetAccessoryId
        defaultWorkInProgressRegistrationShouldBeFound("assetAccessoryId.equals=" + assetAccessoryId);

        // Get all the workInProgressRegistrationList where assetAccessory equals to (assetAccessoryId + 1)
        defaultWorkInProgressRegistrationShouldNotBeFound("assetAccessoryId.equals=" + (assetAccessoryId + 1));
    }

    @Test
    @Transactional
    void getAllWorkInProgressRegistrationsByAssetWarrantyIsEqualToSomething() throws Exception {
        // Initialize the database
        workInProgressRegistrationRepository.saveAndFlush(workInProgressRegistration);
        AssetWarranty assetWarranty;
        if (TestUtil.findAll(em, AssetWarranty.class).isEmpty()) {
            assetWarranty = AssetWarrantyResourceIT.createEntity(em);
            em.persist(assetWarranty);
            em.flush();
        } else {
            assetWarranty = TestUtil.findAll(em, AssetWarranty.class).get(0);
        }
        em.persist(assetWarranty);
        em.flush();
        workInProgressRegistration.addAssetWarranty(assetWarranty);
        workInProgressRegistrationRepository.saveAndFlush(workInProgressRegistration);
        Long assetWarrantyId = assetWarranty.getId();

        // Get all the workInProgressRegistrationList where assetWarranty equals to assetWarrantyId
        defaultWorkInProgressRegistrationShouldBeFound("assetWarrantyId.equals=" + assetWarrantyId);

        // Get all the workInProgressRegistrationList where assetWarranty equals to (assetWarrantyId + 1)
        defaultWorkInProgressRegistrationShouldNotBeFound("assetWarrantyId.equals=" + (assetWarrantyId + 1));
    }

    @Test
    @Transactional
    void getAllWorkInProgressRegistrationsByInvoiceIsEqualToSomething() throws Exception {
        // Initialize the database
        workInProgressRegistrationRepository.saveAndFlush(workInProgressRegistration);
        PaymentInvoice invoice;
        if (TestUtil.findAll(em, PaymentInvoice.class).isEmpty()) {
            invoice = PaymentInvoiceResourceIT.createEntity(em);
            em.persist(invoice);
            em.flush();
        } else {
            invoice = TestUtil.findAll(em, PaymentInvoice.class).get(0);
        }
        em.persist(invoice);
        em.flush();
        workInProgressRegistration.setInvoice(invoice);
        workInProgressRegistrationRepository.saveAndFlush(workInProgressRegistration);
        Long invoiceId = invoice.getId();

        // Get all the workInProgressRegistrationList where invoice equals to invoiceId
        defaultWorkInProgressRegistrationShouldBeFound("invoiceId.equals=" + invoiceId);

        // Get all the workInProgressRegistrationList where invoice equals to (invoiceId + 1)
        defaultWorkInProgressRegistrationShouldNotBeFound("invoiceId.equals=" + (invoiceId + 1));
    }

    @Test
    @Transactional
    void getAllWorkInProgressRegistrationsByOutletCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        workInProgressRegistrationRepository.saveAndFlush(workInProgressRegistration);
        ServiceOutlet outletCode;
        if (TestUtil.findAll(em, ServiceOutlet.class).isEmpty()) {
            outletCode = ServiceOutletResourceIT.createEntity(em);
            em.persist(outletCode);
            em.flush();
        } else {
            outletCode = TestUtil.findAll(em, ServiceOutlet.class).get(0);
        }
        em.persist(outletCode);
        em.flush();
        workInProgressRegistration.setOutletCode(outletCode);
        workInProgressRegistrationRepository.saveAndFlush(workInProgressRegistration);
        Long outletCodeId = outletCode.getId();

        // Get all the workInProgressRegistrationList where outletCode equals to outletCodeId
        defaultWorkInProgressRegistrationShouldBeFound("outletCodeId.equals=" + outletCodeId);

        // Get all the workInProgressRegistrationList where outletCode equals to (outletCodeId + 1)
        defaultWorkInProgressRegistrationShouldNotBeFound("outletCodeId.equals=" + (outletCodeId + 1));
    }

    @Test
    @Transactional
    void getAllWorkInProgressRegistrationsBySettlementTransactionIsEqualToSomething() throws Exception {
        // Initialize the database
        workInProgressRegistrationRepository.saveAndFlush(workInProgressRegistration);
        Settlement settlementTransaction;
        if (TestUtil.findAll(em, Settlement.class).isEmpty()) {
            settlementTransaction = SettlementResourceIT.createEntity(em);
            em.persist(settlementTransaction);
            em.flush();
        } else {
            settlementTransaction = TestUtil.findAll(em, Settlement.class).get(0);
        }
        em.persist(settlementTransaction);
        em.flush();
        workInProgressRegistration.setSettlementTransaction(settlementTransaction);
        workInProgressRegistrationRepository.saveAndFlush(workInProgressRegistration);
        Long settlementTransactionId = settlementTransaction.getId();

        // Get all the workInProgressRegistrationList where settlementTransaction equals to settlementTransactionId
        defaultWorkInProgressRegistrationShouldBeFound("settlementTransactionId.equals=" + settlementTransactionId);

        // Get all the workInProgressRegistrationList where settlementTransaction equals to (settlementTransactionId + 1)
        defaultWorkInProgressRegistrationShouldNotBeFound("settlementTransactionId.equals=" + (settlementTransactionId + 1));
    }

    @Test
    @Transactional
    void getAllWorkInProgressRegistrationsByPurchaseOrderIsEqualToSomething() throws Exception {
        // Initialize the database
        workInProgressRegistrationRepository.saveAndFlush(workInProgressRegistration);
        PurchaseOrder purchaseOrder;
        if (TestUtil.findAll(em, PurchaseOrder.class).isEmpty()) {
            purchaseOrder = PurchaseOrderResourceIT.createEntity(em);
            em.persist(purchaseOrder);
            em.flush();
        } else {
            purchaseOrder = TestUtil.findAll(em, PurchaseOrder.class).get(0);
        }
        em.persist(purchaseOrder);
        em.flush();
        workInProgressRegistration.setPurchaseOrder(purchaseOrder);
        workInProgressRegistrationRepository.saveAndFlush(workInProgressRegistration);
        Long purchaseOrderId = purchaseOrder.getId();

        // Get all the workInProgressRegistrationList where purchaseOrder equals to purchaseOrderId
        defaultWorkInProgressRegistrationShouldBeFound("purchaseOrderId.equals=" + purchaseOrderId);

        // Get all the workInProgressRegistrationList where purchaseOrder equals to (purchaseOrderId + 1)
        defaultWorkInProgressRegistrationShouldNotBeFound("purchaseOrderId.equals=" + (purchaseOrderId + 1));
    }

    @Test
    @Transactional
    void getAllWorkInProgressRegistrationsByDeliveryNoteIsEqualToSomething() throws Exception {
        // Initialize the database
        workInProgressRegistrationRepository.saveAndFlush(workInProgressRegistration);
        DeliveryNote deliveryNote;
        if (TestUtil.findAll(em, DeliveryNote.class).isEmpty()) {
            deliveryNote = DeliveryNoteResourceIT.createEntity(em);
            em.persist(deliveryNote);
            em.flush();
        } else {
            deliveryNote = TestUtil.findAll(em, DeliveryNote.class).get(0);
        }
        em.persist(deliveryNote);
        em.flush();
        workInProgressRegistration.setDeliveryNote(deliveryNote);
        workInProgressRegistrationRepository.saveAndFlush(workInProgressRegistration);
        Long deliveryNoteId = deliveryNote.getId();

        // Get all the workInProgressRegistrationList where deliveryNote equals to deliveryNoteId
        defaultWorkInProgressRegistrationShouldBeFound("deliveryNoteId.equals=" + deliveryNoteId);

        // Get all the workInProgressRegistrationList where deliveryNote equals to (deliveryNoteId + 1)
        defaultWorkInProgressRegistrationShouldNotBeFound("deliveryNoteId.equals=" + (deliveryNoteId + 1));
    }

    @Test
    @Transactional
    void getAllWorkInProgressRegistrationsByJobSheetIsEqualToSomething() throws Exception {
        // Initialize the database
        workInProgressRegistrationRepository.saveAndFlush(workInProgressRegistration);
        JobSheet jobSheet;
        if (TestUtil.findAll(em, JobSheet.class).isEmpty()) {
            jobSheet = JobSheetResourceIT.createEntity(em);
            em.persist(jobSheet);
            em.flush();
        } else {
            jobSheet = TestUtil.findAll(em, JobSheet.class).get(0);
        }
        em.persist(jobSheet);
        em.flush();
        workInProgressRegistration.setJobSheet(jobSheet);
        workInProgressRegistrationRepository.saveAndFlush(workInProgressRegistration);
        Long jobSheetId = jobSheet.getId();

        // Get all the workInProgressRegistrationList where jobSheet equals to jobSheetId
        defaultWorkInProgressRegistrationShouldBeFound("jobSheetId.equals=" + jobSheetId);

        // Get all the workInProgressRegistrationList where jobSheet equals to (jobSheetId + 1)
        defaultWorkInProgressRegistrationShouldNotBeFound("jobSheetId.equals=" + (jobSheetId + 1));
    }

    @Test
    @Transactional
    void getAllWorkInProgressRegistrationsByDealerIsEqualToSomething() throws Exception {
        // Initialize the database
        workInProgressRegistrationRepository.saveAndFlush(workInProgressRegistration);
        Dealer dealer;
        if (TestUtil.findAll(em, Dealer.class).isEmpty()) {
            dealer = DealerResourceIT.createEntity(em);
            em.persist(dealer);
            em.flush();
        } else {
            dealer = TestUtil.findAll(em, Dealer.class).get(0);
        }
        em.persist(dealer);
        em.flush();
        workInProgressRegistration.setDealer(dealer);
        workInProgressRegistrationRepository.saveAndFlush(workInProgressRegistration);
        Long dealerId = dealer.getId();

        // Get all the workInProgressRegistrationList where dealer equals to dealerId
        defaultWorkInProgressRegistrationShouldBeFound("dealerId.equals=" + dealerId);

        // Get all the workInProgressRegistrationList where dealer equals to (dealerId + 1)
        defaultWorkInProgressRegistrationShouldNotBeFound("dealerId.equals=" + (dealerId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultWorkInProgressRegistrationShouldBeFound(String filter) throws Exception {
        restWorkInProgressRegistrationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(workInProgressRegistration.getId().intValue())))
            .andExpect(jsonPath("$.[*].sequenceNumber").value(hasItem(DEFAULT_SEQUENCE_NUMBER)))
            .andExpect(jsonPath("$.[*].particulars").value(hasItem(DEFAULT_PARTICULARS)))
            .andExpect(jsonPath("$.[*].instalmentDate").value(hasItem(DEFAULT_INSTALMENT_DATE.toString())))
            .andExpect(jsonPath("$.[*].instalmentAmount").value(hasItem(sameNumber(DEFAULT_INSTALMENT_AMOUNT))))
            .andExpect(jsonPath("$.[*].commentsContentType").value(hasItem(DEFAULT_COMMENTS_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].comments").value(hasItem(Base64Utils.encodeToString(DEFAULT_COMMENTS))))
            .andExpect(jsonPath("$.[*].levelOfCompletion").value(hasItem(DEFAULT_LEVEL_OF_COMPLETION.doubleValue())))
            .andExpect(jsonPath("$.[*].completed").value(hasItem(DEFAULT_COMPLETED.booleanValue())));

        // Check, that the count call also returns 1
        restWorkInProgressRegistrationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultWorkInProgressRegistrationShouldNotBeFound(String filter) throws Exception {
        restWorkInProgressRegistrationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restWorkInProgressRegistrationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingWorkInProgressRegistration() throws Exception {
        // Get the workInProgressRegistration
        restWorkInProgressRegistrationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewWorkInProgressRegistration() throws Exception {
        // Initialize the database
        workInProgressRegistrationRepository.saveAndFlush(workInProgressRegistration);

        int databaseSizeBeforeUpdate = workInProgressRegistrationRepository.findAll().size();

        // Update the workInProgressRegistration
        WorkInProgressRegistration updatedWorkInProgressRegistration = workInProgressRegistrationRepository
            .findById(workInProgressRegistration.getId())
            .get();
        // Disconnect from session so that the updates on updatedWorkInProgressRegistration are not directly saved in db
        em.detach(updatedWorkInProgressRegistration);
        updatedWorkInProgressRegistration
            .sequenceNumber(UPDATED_SEQUENCE_NUMBER)
            .particulars(UPDATED_PARTICULARS)
            .instalmentDate(UPDATED_INSTALMENT_DATE)
            .instalmentAmount(UPDATED_INSTALMENT_AMOUNT)
            .comments(UPDATED_COMMENTS)
            .commentsContentType(UPDATED_COMMENTS_CONTENT_TYPE)
            .levelOfCompletion(UPDATED_LEVEL_OF_COMPLETION)
            .completed(UPDATED_COMPLETED);
        WorkInProgressRegistrationDTO workInProgressRegistrationDTO = workInProgressRegistrationMapper.toDto(
            updatedWorkInProgressRegistration
        );

        restWorkInProgressRegistrationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, workInProgressRegistrationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workInProgressRegistrationDTO))
            )
            .andExpect(status().isOk());

        // Validate the WorkInProgressRegistration in the database
        List<WorkInProgressRegistration> workInProgressRegistrationList = workInProgressRegistrationRepository.findAll();
        assertThat(workInProgressRegistrationList).hasSize(databaseSizeBeforeUpdate);
        WorkInProgressRegistration testWorkInProgressRegistration = workInProgressRegistrationList.get(
            workInProgressRegistrationList.size() - 1
        );
        assertThat(testWorkInProgressRegistration.getSequenceNumber()).isEqualTo(UPDATED_SEQUENCE_NUMBER);
        assertThat(testWorkInProgressRegistration.getParticulars()).isEqualTo(UPDATED_PARTICULARS);
        assertThat(testWorkInProgressRegistration.getInstalmentDate()).isEqualTo(UPDATED_INSTALMENT_DATE);
        assertThat(testWorkInProgressRegistration.getInstalmentAmount()).isEqualTo(UPDATED_INSTALMENT_AMOUNT);
        assertThat(testWorkInProgressRegistration.getComments()).isEqualTo(UPDATED_COMMENTS);
        assertThat(testWorkInProgressRegistration.getCommentsContentType()).isEqualTo(UPDATED_COMMENTS_CONTENT_TYPE);
        assertThat(testWorkInProgressRegistration.getLevelOfCompletion()).isEqualTo(UPDATED_LEVEL_OF_COMPLETION);
        assertThat(testWorkInProgressRegistration.getCompleted()).isEqualTo(UPDATED_COMPLETED);

        // Validate the WorkInProgressRegistration in Elasticsearch
        verify(mockWorkInProgressRegistrationSearchRepository).save(testWorkInProgressRegistration);
    }

    @Test
    @Transactional
    void putNonExistingWorkInProgressRegistration() throws Exception {
        int databaseSizeBeforeUpdate = workInProgressRegistrationRepository.findAll().size();
        workInProgressRegistration.setId(count.incrementAndGet());

        // Create the WorkInProgressRegistration
        WorkInProgressRegistrationDTO workInProgressRegistrationDTO = workInProgressRegistrationMapper.toDto(workInProgressRegistration);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWorkInProgressRegistrationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, workInProgressRegistrationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workInProgressRegistrationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkInProgressRegistration in the database
        List<WorkInProgressRegistration> workInProgressRegistrationList = workInProgressRegistrationRepository.findAll();
        assertThat(workInProgressRegistrationList).hasSize(databaseSizeBeforeUpdate);

        // Validate the WorkInProgressRegistration in Elasticsearch
        verify(mockWorkInProgressRegistrationSearchRepository, times(0)).save(workInProgressRegistration);
    }

    @Test
    @Transactional
    void putWithIdMismatchWorkInProgressRegistration() throws Exception {
        int databaseSizeBeforeUpdate = workInProgressRegistrationRepository.findAll().size();
        workInProgressRegistration.setId(count.incrementAndGet());

        // Create the WorkInProgressRegistration
        WorkInProgressRegistrationDTO workInProgressRegistrationDTO = workInProgressRegistrationMapper.toDto(workInProgressRegistration);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkInProgressRegistrationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workInProgressRegistrationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkInProgressRegistration in the database
        List<WorkInProgressRegistration> workInProgressRegistrationList = workInProgressRegistrationRepository.findAll();
        assertThat(workInProgressRegistrationList).hasSize(databaseSizeBeforeUpdate);

        // Validate the WorkInProgressRegistration in Elasticsearch
        verify(mockWorkInProgressRegistrationSearchRepository, times(0)).save(workInProgressRegistration);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamWorkInProgressRegistration() throws Exception {
        int databaseSizeBeforeUpdate = workInProgressRegistrationRepository.findAll().size();
        workInProgressRegistration.setId(count.incrementAndGet());

        // Create the WorkInProgressRegistration
        WorkInProgressRegistrationDTO workInProgressRegistrationDTO = workInProgressRegistrationMapper.toDto(workInProgressRegistration);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkInProgressRegistrationMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workInProgressRegistrationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the WorkInProgressRegistration in the database
        List<WorkInProgressRegistration> workInProgressRegistrationList = workInProgressRegistrationRepository.findAll();
        assertThat(workInProgressRegistrationList).hasSize(databaseSizeBeforeUpdate);

        // Validate the WorkInProgressRegistration in Elasticsearch
        verify(mockWorkInProgressRegistrationSearchRepository, times(0)).save(workInProgressRegistration);
    }

    @Test
    @Transactional
    void partialUpdateWorkInProgressRegistrationWithPatch() throws Exception {
        // Initialize the database
        workInProgressRegistrationRepository.saveAndFlush(workInProgressRegistration);

        int databaseSizeBeforeUpdate = workInProgressRegistrationRepository.findAll().size();

        // Update the workInProgressRegistration using partial update
        WorkInProgressRegistration partialUpdatedWorkInProgressRegistration = new WorkInProgressRegistration();
        partialUpdatedWorkInProgressRegistration.setId(workInProgressRegistration.getId());

        partialUpdatedWorkInProgressRegistration
            .sequenceNumber(UPDATED_SEQUENCE_NUMBER)
            .particulars(UPDATED_PARTICULARS)
            .comments(UPDATED_COMMENTS)
            .commentsContentType(UPDATED_COMMENTS_CONTENT_TYPE)
            .completed(UPDATED_COMPLETED);

        restWorkInProgressRegistrationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWorkInProgressRegistration.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWorkInProgressRegistration))
            )
            .andExpect(status().isOk());

        // Validate the WorkInProgressRegistration in the database
        List<WorkInProgressRegistration> workInProgressRegistrationList = workInProgressRegistrationRepository.findAll();
        assertThat(workInProgressRegistrationList).hasSize(databaseSizeBeforeUpdate);
        WorkInProgressRegistration testWorkInProgressRegistration = workInProgressRegistrationList.get(
            workInProgressRegistrationList.size() - 1
        );
        assertThat(testWorkInProgressRegistration.getSequenceNumber()).isEqualTo(UPDATED_SEQUENCE_NUMBER);
        assertThat(testWorkInProgressRegistration.getParticulars()).isEqualTo(UPDATED_PARTICULARS);
        assertThat(testWorkInProgressRegistration.getInstalmentDate()).isEqualTo(DEFAULT_INSTALMENT_DATE);
        assertThat(testWorkInProgressRegistration.getInstalmentAmount()).isEqualByComparingTo(DEFAULT_INSTALMENT_AMOUNT);
        assertThat(testWorkInProgressRegistration.getComments()).isEqualTo(UPDATED_COMMENTS);
        assertThat(testWorkInProgressRegistration.getCommentsContentType()).isEqualTo(UPDATED_COMMENTS_CONTENT_TYPE);
        assertThat(testWorkInProgressRegistration.getLevelOfCompletion()).isEqualTo(DEFAULT_LEVEL_OF_COMPLETION);
        assertThat(testWorkInProgressRegistration.getCompleted()).isEqualTo(UPDATED_COMPLETED);
    }

    @Test
    @Transactional
    void fullUpdateWorkInProgressRegistrationWithPatch() throws Exception {
        // Initialize the database
        workInProgressRegistrationRepository.saveAndFlush(workInProgressRegistration);

        int databaseSizeBeforeUpdate = workInProgressRegistrationRepository.findAll().size();

        // Update the workInProgressRegistration using partial update
        WorkInProgressRegistration partialUpdatedWorkInProgressRegistration = new WorkInProgressRegistration();
        partialUpdatedWorkInProgressRegistration.setId(workInProgressRegistration.getId());

        partialUpdatedWorkInProgressRegistration
            .sequenceNumber(UPDATED_SEQUENCE_NUMBER)
            .particulars(UPDATED_PARTICULARS)
            .instalmentDate(UPDATED_INSTALMENT_DATE)
            .instalmentAmount(UPDATED_INSTALMENT_AMOUNT)
            .comments(UPDATED_COMMENTS)
            .commentsContentType(UPDATED_COMMENTS_CONTENT_TYPE)
            .levelOfCompletion(UPDATED_LEVEL_OF_COMPLETION)
            .completed(UPDATED_COMPLETED);

        restWorkInProgressRegistrationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWorkInProgressRegistration.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWorkInProgressRegistration))
            )
            .andExpect(status().isOk());

        // Validate the WorkInProgressRegistration in the database
        List<WorkInProgressRegistration> workInProgressRegistrationList = workInProgressRegistrationRepository.findAll();
        assertThat(workInProgressRegistrationList).hasSize(databaseSizeBeforeUpdate);
        WorkInProgressRegistration testWorkInProgressRegistration = workInProgressRegistrationList.get(
            workInProgressRegistrationList.size() - 1
        );
        assertThat(testWorkInProgressRegistration.getSequenceNumber()).isEqualTo(UPDATED_SEQUENCE_NUMBER);
        assertThat(testWorkInProgressRegistration.getParticulars()).isEqualTo(UPDATED_PARTICULARS);
        assertThat(testWorkInProgressRegistration.getInstalmentDate()).isEqualTo(UPDATED_INSTALMENT_DATE);
        assertThat(testWorkInProgressRegistration.getInstalmentAmount()).isEqualByComparingTo(UPDATED_INSTALMENT_AMOUNT);
        assertThat(testWorkInProgressRegistration.getComments()).isEqualTo(UPDATED_COMMENTS);
        assertThat(testWorkInProgressRegistration.getCommentsContentType()).isEqualTo(UPDATED_COMMENTS_CONTENT_TYPE);
        assertThat(testWorkInProgressRegistration.getLevelOfCompletion()).isEqualTo(UPDATED_LEVEL_OF_COMPLETION);
        assertThat(testWorkInProgressRegistration.getCompleted()).isEqualTo(UPDATED_COMPLETED);
    }

    @Test
    @Transactional
    void patchNonExistingWorkInProgressRegistration() throws Exception {
        int databaseSizeBeforeUpdate = workInProgressRegistrationRepository.findAll().size();
        workInProgressRegistration.setId(count.incrementAndGet());

        // Create the WorkInProgressRegistration
        WorkInProgressRegistrationDTO workInProgressRegistrationDTO = workInProgressRegistrationMapper.toDto(workInProgressRegistration);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWorkInProgressRegistrationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, workInProgressRegistrationDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(workInProgressRegistrationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkInProgressRegistration in the database
        List<WorkInProgressRegistration> workInProgressRegistrationList = workInProgressRegistrationRepository.findAll();
        assertThat(workInProgressRegistrationList).hasSize(databaseSizeBeforeUpdate);

        // Validate the WorkInProgressRegistration in Elasticsearch
        verify(mockWorkInProgressRegistrationSearchRepository, times(0)).save(workInProgressRegistration);
    }

    @Test
    @Transactional
    void patchWithIdMismatchWorkInProgressRegistration() throws Exception {
        int databaseSizeBeforeUpdate = workInProgressRegistrationRepository.findAll().size();
        workInProgressRegistration.setId(count.incrementAndGet());

        // Create the WorkInProgressRegistration
        WorkInProgressRegistrationDTO workInProgressRegistrationDTO = workInProgressRegistrationMapper.toDto(workInProgressRegistration);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkInProgressRegistrationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(workInProgressRegistrationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkInProgressRegistration in the database
        List<WorkInProgressRegistration> workInProgressRegistrationList = workInProgressRegistrationRepository.findAll();
        assertThat(workInProgressRegistrationList).hasSize(databaseSizeBeforeUpdate);

        // Validate the WorkInProgressRegistration in Elasticsearch
        verify(mockWorkInProgressRegistrationSearchRepository, times(0)).save(workInProgressRegistration);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamWorkInProgressRegistration() throws Exception {
        int databaseSizeBeforeUpdate = workInProgressRegistrationRepository.findAll().size();
        workInProgressRegistration.setId(count.incrementAndGet());

        // Create the WorkInProgressRegistration
        WorkInProgressRegistrationDTO workInProgressRegistrationDTO = workInProgressRegistrationMapper.toDto(workInProgressRegistration);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkInProgressRegistrationMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(workInProgressRegistrationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the WorkInProgressRegistration in the database
        List<WorkInProgressRegistration> workInProgressRegistrationList = workInProgressRegistrationRepository.findAll();
        assertThat(workInProgressRegistrationList).hasSize(databaseSizeBeforeUpdate);

        // Validate the WorkInProgressRegistration in Elasticsearch
        verify(mockWorkInProgressRegistrationSearchRepository, times(0)).save(workInProgressRegistration);
    }

    @Test
    @Transactional
    void deleteWorkInProgressRegistration() throws Exception {
        // Initialize the database
        workInProgressRegistrationRepository.saveAndFlush(workInProgressRegistration);

        int databaseSizeBeforeDelete = workInProgressRegistrationRepository.findAll().size();

        // Delete the workInProgressRegistration
        restWorkInProgressRegistrationMockMvc
            .perform(delete(ENTITY_API_URL_ID, workInProgressRegistration.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<WorkInProgressRegistration> workInProgressRegistrationList = workInProgressRegistrationRepository.findAll();
        assertThat(workInProgressRegistrationList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the WorkInProgressRegistration in Elasticsearch
        verify(mockWorkInProgressRegistrationSearchRepository, times(1)).deleteById(workInProgressRegistration.getId());
    }

    @Test
    @Transactional
    void searchWorkInProgressRegistration() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        workInProgressRegistrationRepository.saveAndFlush(workInProgressRegistration);
        when(mockWorkInProgressRegistrationSearchRepository.search("id:" + workInProgressRegistration.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(workInProgressRegistration), PageRequest.of(0, 1), 1));

        // Search the workInProgressRegistration
        restWorkInProgressRegistrationMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + workInProgressRegistration.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(workInProgressRegistration.getId().intValue())))
            .andExpect(jsonPath("$.[*].sequenceNumber").value(hasItem(DEFAULT_SEQUENCE_NUMBER)))
            .andExpect(jsonPath("$.[*].particulars").value(hasItem(DEFAULT_PARTICULARS)))
            .andExpect(jsonPath("$.[*].instalmentDate").value(hasItem(DEFAULT_INSTALMENT_DATE.toString())))
            .andExpect(jsonPath("$.[*].instalmentAmount").value(hasItem(sameNumber(DEFAULT_INSTALMENT_AMOUNT))))
            .andExpect(jsonPath("$.[*].commentsContentType").value(hasItem(DEFAULT_COMMENTS_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].comments").value(hasItem(Base64Utils.encodeToString(DEFAULT_COMMENTS))))
            .andExpect(jsonPath("$.[*].levelOfCompletion").value(hasItem(DEFAULT_LEVEL_OF_COMPLETION.doubleValue())))
            .andExpect(jsonPath("$.[*].completed").value(hasItem(DEFAULT_COMPLETED.booleanValue())));
    }
}
