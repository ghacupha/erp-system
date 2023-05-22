package io.github.erp.web.rest;

/*-
 * Erp System - Mark III No 15 (Caleb Series) Server ver 1.2.2
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

import static io.github.erp.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import io.github.erp.IntegrationTest;
import io.github.erp.domain.BusinessDocument;
import io.github.erp.domain.Dealer;
import io.github.erp.domain.Placeholder;
import io.github.erp.domain.SettlementCurrency;
import io.github.erp.domain.WorkProjectRegister;
import io.github.erp.repository.WorkProjectRegisterRepository;
import io.github.erp.repository.search.WorkProjectRegisterSearchRepository;
import io.github.erp.service.WorkProjectRegisterService;
import io.github.erp.service.criteria.WorkProjectRegisterCriteria;
import io.github.erp.service.dto.WorkProjectRegisterDTO;
import io.github.erp.service.mapper.WorkProjectRegisterMapper;
import java.math.BigDecimal;
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
 * Integration tests for the {@link WorkProjectRegisterResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class WorkProjectRegisterResourceIT {

    private static final String DEFAULT_CATALOGUE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_CATALOGUE_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final byte[] DEFAULT_DETAILS = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_DETAILS = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_DETAILS_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_DETAILS_CONTENT_TYPE = "image/png";

    private static final BigDecimal DEFAULT_TOTAL_PROJECT_COST = new BigDecimal(1);
    private static final BigDecimal UPDATED_TOTAL_PROJECT_COST = new BigDecimal(2);
    private static final BigDecimal SMALLER_TOTAL_PROJECT_COST = new BigDecimal(1 - 1);

    private static final byte[] DEFAULT_ADDITIONAL_NOTES = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_ADDITIONAL_NOTES = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_ADDITIONAL_NOTES_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_ADDITIONAL_NOTES_CONTENT_TYPE = "image/png";

    private static final String ENTITY_API_URL = "/api/work-project-registers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/work-project-registers";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private WorkProjectRegisterRepository workProjectRegisterRepository;

    @Mock
    private WorkProjectRegisterRepository workProjectRegisterRepositoryMock;

    @Autowired
    private WorkProjectRegisterMapper workProjectRegisterMapper;

    @Mock
    private WorkProjectRegisterService workProjectRegisterServiceMock;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.WorkProjectRegisterSearchRepositoryMockConfiguration
     */
    @Autowired
    private WorkProjectRegisterSearchRepository mockWorkProjectRegisterSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restWorkProjectRegisterMockMvc;

    private WorkProjectRegister workProjectRegister;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WorkProjectRegister createEntity(EntityManager em) {
        WorkProjectRegister workProjectRegister = new WorkProjectRegister()
            .catalogueNumber(DEFAULT_CATALOGUE_NUMBER)
            .description(DEFAULT_DESCRIPTION)
            .details(DEFAULT_DETAILS)
            .detailsContentType(DEFAULT_DETAILS_CONTENT_TYPE)
            .totalProjectCost(DEFAULT_TOTAL_PROJECT_COST)
            .additionalNotes(DEFAULT_ADDITIONAL_NOTES)
            .additionalNotesContentType(DEFAULT_ADDITIONAL_NOTES_CONTENT_TYPE);
        // Add required entity
        Dealer dealer;
        if (TestUtil.findAll(em, Dealer.class).isEmpty()) {
            dealer = DealerResourceIT.createEntity(em);
            em.persist(dealer);
            em.flush();
        } else {
            dealer = TestUtil.findAll(em, Dealer.class).get(0);
        }
        workProjectRegister.getDealers().add(dealer);
        return workProjectRegister;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WorkProjectRegister createUpdatedEntity(EntityManager em) {
        WorkProjectRegister workProjectRegister = new WorkProjectRegister()
            .catalogueNumber(UPDATED_CATALOGUE_NUMBER)
            .description(UPDATED_DESCRIPTION)
            .details(UPDATED_DETAILS)
            .detailsContentType(UPDATED_DETAILS_CONTENT_TYPE)
            .totalProjectCost(UPDATED_TOTAL_PROJECT_COST)
            .additionalNotes(UPDATED_ADDITIONAL_NOTES)
            .additionalNotesContentType(UPDATED_ADDITIONAL_NOTES_CONTENT_TYPE);
        // Add required entity
        Dealer dealer;
        if (TestUtil.findAll(em, Dealer.class).isEmpty()) {
            dealer = DealerResourceIT.createUpdatedEntity(em);
            em.persist(dealer);
            em.flush();
        } else {
            dealer = TestUtil.findAll(em, Dealer.class).get(0);
        }
        workProjectRegister.getDealers().add(dealer);
        return workProjectRegister;
    }

    @BeforeEach
    public void initTest() {
        workProjectRegister = createEntity(em);
    }

    @Test
    @Transactional
    void createWorkProjectRegister() throws Exception {
        int databaseSizeBeforeCreate = workProjectRegisterRepository.findAll().size();
        // Create the WorkProjectRegister
        WorkProjectRegisterDTO workProjectRegisterDTO = workProjectRegisterMapper.toDto(workProjectRegister);
        restWorkProjectRegisterMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workProjectRegisterDTO))
            )
            .andExpect(status().isCreated());

        // Validate the WorkProjectRegister in the database
        List<WorkProjectRegister> workProjectRegisterList = workProjectRegisterRepository.findAll();
        assertThat(workProjectRegisterList).hasSize(databaseSizeBeforeCreate + 1);
        WorkProjectRegister testWorkProjectRegister = workProjectRegisterList.get(workProjectRegisterList.size() - 1);
        assertThat(testWorkProjectRegister.getCatalogueNumber()).isEqualTo(DEFAULT_CATALOGUE_NUMBER);
        assertThat(testWorkProjectRegister.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testWorkProjectRegister.getDetails()).isEqualTo(DEFAULT_DETAILS);
        assertThat(testWorkProjectRegister.getDetailsContentType()).isEqualTo(DEFAULT_DETAILS_CONTENT_TYPE);
        assertThat(testWorkProjectRegister.getTotalProjectCost()).isEqualByComparingTo(DEFAULT_TOTAL_PROJECT_COST);
        assertThat(testWorkProjectRegister.getAdditionalNotes()).isEqualTo(DEFAULT_ADDITIONAL_NOTES);
        assertThat(testWorkProjectRegister.getAdditionalNotesContentType()).isEqualTo(DEFAULT_ADDITIONAL_NOTES_CONTENT_TYPE);

        // Validate the WorkProjectRegister in Elasticsearch
        verify(mockWorkProjectRegisterSearchRepository, times(1)).save(testWorkProjectRegister);
    }

    @Test
    @Transactional
    void createWorkProjectRegisterWithExistingId() throws Exception {
        // Create the WorkProjectRegister with an existing ID
        workProjectRegister.setId(1L);
        WorkProjectRegisterDTO workProjectRegisterDTO = workProjectRegisterMapper.toDto(workProjectRegister);

        int databaseSizeBeforeCreate = workProjectRegisterRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restWorkProjectRegisterMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workProjectRegisterDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkProjectRegister in the database
        List<WorkProjectRegister> workProjectRegisterList = workProjectRegisterRepository.findAll();
        assertThat(workProjectRegisterList).hasSize(databaseSizeBeforeCreate);

        // Validate the WorkProjectRegister in Elasticsearch
        verify(mockWorkProjectRegisterSearchRepository, times(0)).save(workProjectRegister);
    }

    @Test
    @Transactional
    void checkCatalogueNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = workProjectRegisterRepository.findAll().size();
        // set the field null
        workProjectRegister.setCatalogueNumber(null);

        // Create the WorkProjectRegister, which fails.
        WorkProjectRegisterDTO workProjectRegisterDTO = workProjectRegisterMapper.toDto(workProjectRegister);

        restWorkProjectRegisterMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workProjectRegisterDTO))
            )
            .andExpect(status().isBadRequest());

        List<WorkProjectRegister> workProjectRegisterList = workProjectRegisterRepository.findAll();
        assertThat(workProjectRegisterList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = workProjectRegisterRepository.findAll().size();
        // set the field null
        workProjectRegister.setDescription(null);

        // Create the WorkProjectRegister, which fails.
        WorkProjectRegisterDTO workProjectRegisterDTO = workProjectRegisterMapper.toDto(workProjectRegister);

        restWorkProjectRegisterMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workProjectRegisterDTO))
            )
            .andExpect(status().isBadRequest());

        List<WorkProjectRegister> workProjectRegisterList = workProjectRegisterRepository.findAll();
        assertThat(workProjectRegisterList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllWorkProjectRegisters() throws Exception {
        // Initialize the database
        workProjectRegisterRepository.saveAndFlush(workProjectRegister);

        // Get all the workProjectRegisterList
        restWorkProjectRegisterMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(workProjectRegister.getId().intValue())))
            .andExpect(jsonPath("$.[*].catalogueNumber").value(hasItem(DEFAULT_CATALOGUE_NUMBER)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].detailsContentType").value(hasItem(DEFAULT_DETAILS_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].details").value(hasItem(Base64Utils.encodeToString(DEFAULT_DETAILS))))
            .andExpect(jsonPath("$.[*].totalProjectCost").value(hasItem(sameNumber(DEFAULT_TOTAL_PROJECT_COST))))
            .andExpect(jsonPath("$.[*].additionalNotesContentType").value(hasItem(DEFAULT_ADDITIONAL_NOTES_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].additionalNotes").value(hasItem(Base64Utils.encodeToString(DEFAULT_ADDITIONAL_NOTES))));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllWorkProjectRegistersWithEagerRelationshipsIsEnabled() throws Exception {
        when(workProjectRegisterServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restWorkProjectRegisterMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(workProjectRegisterServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllWorkProjectRegistersWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(workProjectRegisterServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restWorkProjectRegisterMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(workProjectRegisterServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getWorkProjectRegister() throws Exception {
        // Initialize the database
        workProjectRegisterRepository.saveAndFlush(workProjectRegister);

        // Get the workProjectRegister
        restWorkProjectRegisterMockMvc
            .perform(get(ENTITY_API_URL_ID, workProjectRegister.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(workProjectRegister.getId().intValue()))
            .andExpect(jsonPath("$.catalogueNumber").value(DEFAULT_CATALOGUE_NUMBER))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.detailsContentType").value(DEFAULT_DETAILS_CONTENT_TYPE))
            .andExpect(jsonPath("$.details").value(Base64Utils.encodeToString(DEFAULT_DETAILS)))
            .andExpect(jsonPath("$.totalProjectCost").value(sameNumber(DEFAULT_TOTAL_PROJECT_COST)))
            .andExpect(jsonPath("$.additionalNotesContentType").value(DEFAULT_ADDITIONAL_NOTES_CONTENT_TYPE))
            .andExpect(jsonPath("$.additionalNotes").value(Base64Utils.encodeToString(DEFAULT_ADDITIONAL_NOTES)));
    }

    @Test
    @Transactional
    void getWorkProjectRegistersByIdFiltering() throws Exception {
        // Initialize the database
        workProjectRegisterRepository.saveAndFlush(workProjectRegister);

        Long id = workProjectRegister.getId();

        defaultWorkProjectRegisterShouldBeFound("id.equals=" + id);
        defaultWorkProjectRegisterShouldNotBeFound("id.notEquals=" + id);

        defaultWorkProjectRegisterShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultWorkProjectRegisterShouldNotBeFound("id.greaterThan=" + id);

        defaultWorkProjectRegisterShouldBeFound("id.lessThanOrEqual=" + id);
        defaultWorkProjectRegisterShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllWorkProjectRegistersByCatalogueNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        workProjectRegisterRepository.saveAndFlush(workProjectRegister);

        // Get all the workProjectRegisterList where catalogueNumber equals to DEFAULT_CATALOGUE_NUMBER
        defaultWorkProjectRegisterShouldBeFound("catalogueNumber.equals=" + DEFAULT_CATALOGUE_NUMBER);

        // Get all the workProjectRegisterList where catalogueNumber equals to UPDATED_CATALOGUE_NUMBER
        defaultWorkProjectRegisterShouldNotBeFound("catalogueNumber.equals=" + UPDATED_CATALOGUE_NUMBER);
    }

    @Test
    @Transactional
    void getAllWorkProjectRegistersByCatalogueNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        workProjectRegisterRepository.saveAndFlush(workProjectRegister);

        // Get all the workProjectRegisterList where catalogueNumber not equals to DEFAULT_CATALOGUE_NUMBER
        defaultWorkProjectRegisterShouldNotBeFound("catalogueNumber.notEquals=" + DEFAULT_CATALOGUE_NUMBER);

        // Get all the workProjectRegisterList where catalogueNumber not equals to UPDATED_CATALOGUE_NUMBER
        defaultWorkProjectRegisterShouldBeFound("catalogueNumber.notEquals=" + UPDATED_CATALOGUE_NUMBER);
    }

    @Test
    @Transactional
    void getAllWorkProjectRegistersByCatalogueNumberIsInShouldWork() throws Exception {
        // Initialize the database
        workProjectRegisterRepository.saveAndFlush(workProjectRegister);

        // Get all the workProjectRegisterList where catalogueNumber in DEFAULT_CATALOGUE_NUMBER or UPDATED_CATALOGUE_NUMBER
        defaultWorkProjectRegisterShouldBeFound("catalogueNumber.in=" + DEFAULT_CATALOGUE_NUMBER + "," + UPDATED_CATALOGUE_NUMBER);

        // Get all the workProjectRegisterList where catalogueNumber equals to UPDATED_CATALOGUE_NUMBER
        defaultWorkProjectRegisterShouldNotBeFound("catalogueNumber.in=" + UPDATED_CATALOGUE_NUMBER);
    }

    @Test
    @Transactional
    void getAllWorkProjectRegistersByCatalogueNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        workProjectRegisterRepository.saveAndFlush(workProjectRegister);

        // Get all the workProjectRegisterList where catalogueNumber is not null
        defaultWorkProjectRegisterShouldBeFound("catalogueNumber.specified=true");

        // Get all the workProjectRegisterList where catalogueNumber is null
        defaultWorkProjectRegisterShouldNotBeFound("catalogueNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllWorkProjectRegistersByCatalogueNumberContainsSomething() throws Exception {
        // Initialize the database
        workProjectRegisterRepository.saveAndFlush(workProjectRegister);

        // Get all the workProjectRegisterList where catalogueNumber contains DEFAULT_CATALOGUE_NUMBER
        defaultWorkProjectRegisterShouldBeFound("catalogueNumber.contains=" + DEFAULT_CATALOGUE_NUMBER);

        // Get all the workProjectRegisterList where catalogueNumber contains UPDATED_CATALOGUE_NUMBER
        defaultWorkProjectRegisterShouldNotBeFound("catalogueNumber.contains=" + UPDATED_CATALOGUE_NUMBER);
    }

    @Test
    @Transactional
    void getAllWorkProjectRegistersByCatalogueNumberNotContainsSomething() throws Exception {
        // Initialize the database
        workProjectRegisterRepository.saveAndFlush(workProjectRegister);

        // Get all the workProjectRegisterList where catalogueNumber does not contain DEFAULT_CATALOGUE_NUMBER
        defaultWorkProjectRegisterShouldNotBeFound("catalogueNumber.doesNotContain=" + DEFAULT_CATALOGUE_NUMBER);

        // Get all the workProjectRegisterList where catalogueNumber does not contain UPDATED_CATALOGUE_NUMBER
        defaultWorkProjectRegisterShouldBeFound("catalogueNumber.doesNotContain=" + UPDATED_CATALOGUE_NUMBER);
    }

    @Test
    @Transactional
    void getAllWorkProjectRegistersByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        workProjectRegisterRepository.saveAndFlush(workProjectRegister);

        // Get all the workProjectRegisterList where description equals to DEFAULT_DESCRIPTION
        defaultWorkProjectRegisterShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the workProjectRegisterList where description equals to UPDATED_DESCRIPTION
        defaultWorkProjectRegisterShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllWorkProjectRegistersByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        workProjectRegisterRepository.saveAndFlush(workProjectRegister);

        // Get all the workProjectRegisterList where description not equals to DEFAULT_DESCRIPTION
        defaultWorkProjectRegisterShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the workProjectRegisterList where description not equals to UPDATED_DESCRIPTION
        defaultWorkProjectRegisterShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllWorkProjectRegistersByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        workProjectRegisterRepository.saveAndFlush(workProjectRegister);

        // Get all the workProjectRegisterList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultWorkProjectRegisterShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the workProjectRegisterList where description equals to UPDATED_DESCRIPTION
        defaultWorkProjectRegisterShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllWorkProjectRegistersByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        workProjectRegisterRepository.saveAndFlush(workProjectRegister);

        // Get all the workProjectRegisterList where description is not null
        defaultWorkProjectRegisterShouldBeFound("description.specified=true");

        // Get all the workProjectRegisterList where description is null
        defaultWorkProjectRegisterShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllWorkProjectRegistersByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        workProjectRegisterRepository.saveAndFlush(workProjectRegister);

        // Get all the workProjectRegisterList where description contains DEFAULT_DESCRIPTION
        defaultWorkProjectRegisterShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the workProjectRegisterList where description contains UPDATED_DESCRIPTION
        defaultWorkProjectRegisterShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllWorkProjectRegistersByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        workProjectRegisterRepository.saveAndFlush(workProjectRegister);

        // Get all the workProjectRegisterList where description does not contain DEFAULT_DESCRIPTION
        defaultWorkProjectRegisterShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the workProjectRegisterList where description does not contain UPDATED_DESCRIPTION
        defaultWorkProjectRegisterShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllWorkProjectRegistersByTotalProjectCostIsEqualToSomething() throws Exception {
        // Initialize the database
        workProjectRegisterRepository.saveAndFlush(workProjectRegister);

        // Get all the workProjectRegisterList where totalProjectCost equals to DEFAULT_TOTAL_PROJECT_COST
        defaultWorkProjectRegisterShouldBeFound("totalProjectCost.equals=" + DEFAULT_TOTAL_PROJECT_COST);

        // Get all the workProjectRegisterList where totalProjectCost equals to UPDATED_TOTAL_PROJECT_COST
        defaultWorkProjectRegisterShouldNotBeFound("totalProjectCost.equals=" + UPDATED_TOTAL_PROJECT_COST);
    }

    @Test
    @Transactional
    void getAllWorkProjectRegistersByTotalProjectCostIsNotEqualToSomething() throws Exception {
        // Initialize the database
        workProjectRegisterRepository.saveAndFlush(workProjectRegister);

        // Get all the workProjectRegisterList where totalProjectCost not equals to DEFAULT_TOTAL_PROJECT_COST
        defaultWorkProjectRegisterShouldNotBeFound("totalProjectCost.notEquals=" + DEFAULT_TOTAL_PROJECT_COST);

        // Get all the workProjectRegisterList where totalProjectCost not equals to UPDATED_TOTAL_PROJECT_COST
        defaultWorkProjectRegisterShouldBeFound("totalProjectCost.notEquals=" + UPDATED_TOTAL_PROJECT_COST);
    }

    @Test
    @Transactional
    void getAllWorkProjectRegistersByTotalProjectCostIsInShouldWork() throws Exception {
        // Initialize the database
        workProjectRegisterRepository.saveAndFlush(workProjectRegister);

        // Get all the workProjectRegisterList where totalProjectCost in DEFAULT_TOTAL_PROJECT_COST or UPDATED_TOTAL_PROJECT_COST
        defaultWorkProjectRegisterShouldBeFound("totalProjectCost.in=" + DEFAULT_TOTAL_PROJECT_COST + "," + UPDATED_TOTAL_PROJECT_COST);

        // Get all the workProjectRegisterList where totalProjectCost equals to UPDATED_TOTAL_PROJECT_COST
        defaultWorkProjectRegisterShouldNotBeFound("totalProjectCost.in=" + UPDATED_TOTAL_PROJECT_COST);
    }

    @Test
    @Transactional
    void getAllWorkProjectRegistersByTotalProjectCostIsNullOrNotNull() throws Exception {
        // Initialize the database
        workProjectRegisterRepository.saveAndFlush(workProjectRegister);

        // Get all the workProjectRegisterList where totalProjectCost is not null
        defaultWorkProjectRegisterShouldBeFound("totalProjectCost.specified=true");

        // Get all the workProjectRegisterList where totalProjectCost is null
        defaultWorkProjectRegisterShouldNotBeFound("totalProjectCost.specified=false");
    }

    @Test
    @Transactional
    void getAllWorkProjectRegistersByTotalProjectCostIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        workProjectRegisterRepository.saveAndFlush(workProjectRegister);

        // Get all the workProjectRegisterList where totalProjectCost is greater than or equal to DEFAULT_TOTAL_PROJECT_COST
        defaultWorkProjectRegisterShouldBeFound("totalProjectCost.greaterThanOrEqual=" + DEFAULT_TOTAL_PROJECT_COST);

        // Get all the workProjectRegisterList where totalProjectCost is greater than or equal to UPDATED_TOTAL_PROJECT_COST
        defaultWorkProjectRegisterShouldNotBeFound("totalProjectCost.greaterThanOrEqual=" + UPDATED_TOTAL_PROJECT_COST);
    }

    @Test
    @Transactional
    void getAllWorkProjectRegistersByTotalProjectCostIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        workProjectRegisterRepository.saveAndFlush(workProjectRegister);

        // Get all the workProjectRegisterList where totalProjectCost is less than or equal to DEFAULT_TOTAL_PROJECT_COST
        defaultWorkProjectRegisterShouldBeFound("totalProjectCost.lessThanOrEqual=" + DEFAULT_TOTAL_PROJECT_COST);

        // Get all the workProjectRegisterList where totalProjectCost is less than or equal to SMALLER_TOTAL_PROJECT_COST
        defaultWorkProjectRegisterShouldNotBeFound("totalProjectCost.lessThanOrEqual=" + SMALLER_TOTAL_PROJECT_COST);
    }

    @Test
    @Transactional
    void getAllWorkProjectRegistersByTotalProjectCostIsLessThanSomething() throws Exception {
        // Initialize the database
        workProjectRegisterRepository.saveAndFlush(workProjectRegister);

        // Get all the workProjectRegisterList where totalProjectCost is less than DEFAULT_TOTAL_PROJECT_COST
        defaultWorkProjectRegisterShouldNotBeFound("totalProjectCost.lessThan=" + DEFAULT_TOTAL_PROJECT_COST);

        // Get all the workProjectRegisterList where totalProjectCost is less than UPDATED_TOTAL_PROJECT_COST
        defaultWorkProjectRegisterShouldBeFound("totalProjectCost.lessThan=" + UPDATED_TOTAL_PROJECT_COST);
    }

    @Test
    @Transactional
    void getAllWorkProjectRegistersByTotalProjectCostIsGreaterThanSomething() throws Exception {
        // Initialize the database
        workProjectRegisterRepository.saveAndFlush(workProjectRegister);

        // Get all the workProjectRegisterList where totalProjectCost is greater than DEFAULT_TOTAL_PROJECT_COST
        defaultWorkProjectRegisterShouldNotBeFound("totalProjectCost.greaterThan=" + DEFAULT_TOTAL_PROJECT_COST);

        // Get all the workProjectRegisterList where totalProjectCost is greater than SMALLER_TOTAL_PROJECT_COST
        defaultWorkProjectRegisterShouldBeFound("totalProjectCost.greaterThan=" + SMALLER_TOTAL_PROJECT_COST);
    }

    @Test
    @Transactional
    void getAllWorkProjectRegistersByDealersIsEqualToSomething() throws Exception {
        // Initialize the database
        workProjectRegisterRepository.saveAndFlush(workProjectRegister);
        Dealer dealers;
        if (TestUtil.findAll(em, Dealer.class).isEmpty()) {
            dealers = DealerResourceIT.createEntity(em);
            em.persist(dealers);
            em.flush();
        } else {
            dealers = TestUtil.findAll(em, Dealer.class).get(0);
        }
        em.persist(dealers);
        em.flush();
        workProjectRegister.addDealers(dealers);
        workProjectRegisterRepository.saveAndFlush(workProjectRegister);
        Long dealersId = dealers.getId();

        // Get all the workProjectRegisterList where dealers equals to dealersId
        defaultWorkProjectRegisterShouldBeFound("dealersId.equals=" + dealersId);

        // Get all the workProjectRegisterList where dealers equals to (dealersId + 1)
        defaultWorkProjectRegisterShouldNotBeFound("dealersId.equals=" + (dealersId + 1));
    }

    @Test
    @Transactional
    void getAllWorkProjectRegistersBySettlementCurrencyIsEqualToSomething() throws Exception {
        // Initialize the database
        workProjectRegisterRepository.saveAndFlush(workProjectRegister);
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
        workProjectRegister.setSettlementCurrency(settlementCurrency);
        workProjectRegisterRepository.saveAndFlush(workProjectRegister);
        Long settlementCurrencyId = settlementCurrency.getId();

        // Get all the workProjectRegisterList where settlementCurrency equals to settlementCurrencyId
        defaultWorkProjectRegisterShouldBeFound("settlementCurrencyId.equals=" + settlementCurrencyId);

        // Get all the workProjectRegisterList where settlementCurrency equals to (settlementCurrencyId + 1)
        defaultWorkProjectRegisterShouldNotBeFound("settlementCurrencyId.equals=" + (settlementCurrencyId + 1));
    }

    @Test
    @Transactional
    void getAllWorkProjectRegistersByPlaceholderIsEqualToSomething() throws Exception {
        // Initialize the database
        workProjectRegisterRepository.saveAndFlush(workProjectRegister);
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
        workProjectRegister.addPlaceholder(placeholder);
        workProjectRegisterRepository.saveAndFlush(workProjectRegister);
        Long placeholderId = placeholder.getId();

        // Get all the workProjectRegisterList where placeholder equals to placeholderId
        defaultWorkProjectRegisterShouldBeFound("placeholderId.equals=" + placeholderId);

        // Get all the workProjectRegisterList where placeholder equals to (placeholderId + 1)
        defaultWorkProjectRegisterShouldNotBeFound("placeholderId.equals=" + (placeholderId + 1));
    }

    @Test
    @Transactional
    void getAllWorkProjectRegistersByBusinessDocumentIsEqualToSomething() throws Exception {
        // Initialize the database
        workProjectRegisterRepository.saveAndFlush(workProjectRegister);
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
        workProjectRegister.addBusinessDocument(businessDocument);
        workProjectRegisterRepository.saveAndFlush(workProjectRegister);
        Long businessDocumentId = businessDocument.getId();

        // Get all the workProjectRegisterList where businessDocument equals to businessDocumentId
        defaultWorkProjectRegisterShouldBeFound("businessDocumentId.equals=" + businessDocumentId);

        // Get all the workProjectRegisterList where businessDocument equals to (businessDocumentId + 1)
        defaultWorkProjectRegisterShouldNotBeFound("businessDocumentId.equals=" + (businessDocumentId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultWorkProjectRegisterShouldBeFound(String filter) throws Exception {
        restWorkProjectRegisterMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(workProjectRegister.getId().intValue())))
            .andExpect(jsonPath("$.[*].catalogueNumber").value(hasItem(DEFAULT_CATALOGUE_NUMBER)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].detailsContentType").value(hasItem(DEFAULT_DETAILS_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].details").value(hasItem(Base64Utils.encodeToString(DEFAULT_DETAILS))))
            .andExpect(jsonPath("$.[*].totalProjectCost").value(hasItem(sameNumber(DEFAULT_TOTAL_PROJECT_COST))))
            .andExpect(jsonPath("$.[*].additionalNotesContentType").value(hasItem(DEFAULT_ADDITIONAL_NOTES_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].additionalNotes").value(hasItem(Base64Utils.encodeToString(DEFAULT_ADDITIONAL_NOTES))));

        // Check, that the count call also returns 1
        restWorkProjectRegisterMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultWorkProjectRegisterShouldNotBeFound(String filter) throws Exception {
        restWorkProjectRegisterMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restWorkProjectRegisterMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingWorkProjectRegister() throws Exception {
        // Get the workProjectRegister
        restWorkProjectRegisterMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewWorkProjectRegister() throws Exception {
        // Initialize the database
        workProjectRegisterRepository.saveAndFlush(workProjectRegister);

        int databaseSizeBeforeUpdate = workProjectRegisterRepository.findAll().size();

        // Update the workProjectRegister
        WorkProjectRegister updatedWorkProjectRegister = workProjectRegisterRepository.findById(workProjectRegister.getId()).get();
        // Disconnect from session so that the updates on updatedWorkProjectRegister are not directly saved in db
        em.detach(updatedWorkProjectRegister);
        updatedWorkProjectRegister
            .catalogueNumber(UPDATED_CATALOGUE_NUMBER)
            .description(UPDATED_DESCRIPTION)
            .details(UPDATED_DETAILS)
            .detailsContentType(UPDATED_DETAILS_CONTENT_TYPE)
            .totalProjectCost(UPDATED_TOTAL_PROJECT_COST)
            .additionalNotes(UPDATED_ADDITIONAL_NOTES)
            .additionalNotesContentType(UPDATED_ADDITIONAL_NOTES_CONTENT_TYPE);
        WorkProjectRegisterDTO workProjectRegisterDTO = workProjectRegisterMapper.toDto(updatedWorkProjectRegister);

        restWorkProjectRegisterMockMvc
            .perform(
                put(ENTITY_API_URL_ID, workProjectRegisterDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workProjectRegisterDTO))
            )
            .andExpect(status().isOk());

        // Validate the WorkProjectRegister in the database
        List<WorkProjectRegister> workProjectRegisterList = workProjectRegisterRepository.findAll();
        assertThat(workProjectRegisterList).hasSize(databaseSizeBeforeUpdate);
        WorkProjectRegister testWorkProjectRegister = workProjectRegisterList.get(workProjectRegisterList.size() - 1);
        assertThat(testWorkProjectRegister.getCatalogueNumber()).isEqualTo(UPDATED_CATALOGUE_NUMBER);
        assertThat(testWorkProjectRegister.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testWorkProjectRegister.getDetails()).isEqualTo(UPDATED_DETAILS);
        assertThat(testWorkProjectRegister.getDetailsContentType()).isEqualTo(UPDATED_DETAILS_CONTENT_TYPE);
        assertThat(testWorkProjectRegister.getTotalProjectCost()).isEqualTo(UPDATED_TOTAL_PROJECT_COST);
        assertThat(testWorkProjectRegister.getAdditionalNotes()).isEqualTo(UPDATED_ADDITIONAL_NOTES);
        assertThat(testWorkProjectRegister.getAdditionalNotesContentType()).isEqualTo(UPDATED_ADDITIONAL_NOTES_CONTENT_TYPE);

        // Validate the WorkProjectRegister in Elasticsearch
        verify(mockWorkProjectRegisterSearchRepository).save(testWorkProjectRegister);
    }

    @Test
    @Transactional
    void putNonExistingWorkProjectRegister() throws Exception {
        int databaseSizeBeforeUpdate = workProjectRegisterRepository.findAll().size();
        workProjectRegister.setId(count.incrementAndGet());

        // Create the WorkProjectRegister
        WorkProjectRegisterDTO workProjectRegisterDTO = workProjectRegisterMapper.toDto(workProjectRegister);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWorkProjectRegisterMockMvc
            .perform(
                put(ENTITY_API_URL_ID, workProjectRegisterDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workProjectRegisterDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkProjectRegister in the database
        List<WorkProjectRegister> workProjectRegisterList = workProjectRegisterRepository.findAll();
        assertThat(workProjectRegisterList).hasSize(databaseSizeBeforeUpdate);

        // Validate the WorkProjectRegister in Elasticsearch
        verify(mockWorkProjectRegisterSearchRepository, times(0)).save(workProjectRegister);
    }

    @Test
    @Transactional
    void putWithIdMismatchWorkProjectRegister() throws Exception {
        int databaseSizeBeforeUpdate = workProjectRegisterRepository.findAll().size();
        workProjectRegister.setId(count.incrementAndGet());

        // Create the WorkProjectRegister
        WorkProjectRegisterDTO workProjectRegisterDTO = workProjectRegisterMapper.toDto(workProjectRegister);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkProjectRegisterMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workProjectRegisterDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkProjectRegister in the database
        List<WorkProjectRegister> workProjectRegisterList = workProjectRegisterRepository.findAll();
        assertThat(workProjectRegisterList).hasSize(databaseSizeBeforeUpdate);

        // Validate the WorkProjectRegister in Elasticsearch
        verify(mockWorkProjectRegisterSearchRepository, times(0)).save(workProjectRegister);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamWorkProjectRegister() throws Exception {
        int databaseSizeBeforeUpdate = workProjectRegisterRepository.findAll().size();
        workProjectRegister.setId(count.incrementAndGet());

        // Create the WorkProjectRegister
        WorkProjectRegisterDTO workProjectRegisterDTO = workProjectRegisterMapper.toDto(workProjectRegister);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkProjectRegisterMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workProjectRegisterDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the WorkProjectRegister in the database
        List<WorkProjectRegister> workProjectRegisterList = workProjectRegisterRepository.findAll();
        assertThat(workProjectRegisterList).hasSize(databaseSizeBeforeUpdate);

        // Validate the WorkProjectRegister in Elasticsearch
        verify(mockWorkProjectRegisterSearchRepository, times(0)).save(workProjectRegister);
    }

    @Test
    @Transactional
    void partialUpdateWorkProjectRegisterWithPatch() throws Exception {
        // Initialize the database
        workProjectRegisterRepository.saveAndFlush(workProjectRegister);

        int databaseSizeBeforeUpdate = workProjectRegisterRepository.findAll().size();

        // Update the workProjectRegister using partial update
        WorkProjectRegister partialUpdatedWorkProjectRegister = new WorkProjectRegister();
        partialUpdatedWorkProjectRegister.setId(workProjectRegister.getId());

        partialUpdatedWorkProjectRegister
            .catalogueNumber(UPDATED_CATALOGUE_NUMBER)
            .description(UPDATED_DESCRIPTION)
            .totalProjectCost(UPDATED_TOTAL_PROJECT_COST);

        restWorkProjectRegisterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWorkProjectRegister.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWorkProjectRegister))
            )
            .andExpect(status().isOk());

        // Validate the WorkProjectRegister in the database
        List<WorkProjectRegister> workProjectRegisterList = workProjectRegisterRepository.findAll();
        assertThat(workProjectRegisterList).hasSize(databaseSizeBeforeUpdate);
        WorkProjectRegister testWorkProjectRegister = workProjectRegisterList.get(workProjectRegisterList.size() - 1);
        assertThat(testWorkProjectRegister.getCatalogueNumber()).isEqualTo(UPDATED_CATALOGUE_NUMBER);
        assertThat(testWorkProjectRegister.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testWorkProjectRegister.getDetails()).isEqualTo(DEFAULT_DETAILS);
        assertThat(testWorkProjectRegister.getDetailsContentType()).isEqualTo(DEFAULT_DETAILS_CONTENT_TYPE);
        assertThat(testWorkProjectRegister.getTotalProjectCost()).isEqualByComparingTo(UPDATED_TOTAL_PROJECT_COST);
        assertThat(testWorkProjectRegister.getAdditionalNotes()).isEqualTo(DEFAULT_ADDITIONAL_NOTES);
        assertThat(testWorkProjectRegister.getAdditionalNotesContentType()).isEqualTo(DEFAULT_ADDITIONAL_NOTES_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateWorkProjectRegisterWithPatch() throws Exception {
        // Initialize the database
        workProjectRegisterRepository.saveAndFlush(workProjectRegister);

        int databaseSizeBeforeUpdate = workProjectRegisterRepository.findAll().size();

        // Update the workProjectRegister using partial update
        WorkProjectRegister partialUpdatedWorkProjectRegister = new WorkProjectRegister();
        partialUpdatedWorkProjectRegister.setId(workProjectRegister.getId());

        partialUpdatedWorkProjectRegister
            .catalogueNumber(UPDATED_CATALOGUE_NUMBER)
            .description(UPDATED_DESCRIPTION)
            .details(UPDATED_DETAILS)
            .detailsContentType(UPDATED_DETAILS_CONTENT_TYPE)
            .totalProjectCost(UPDATED_TOTAL_PROJECT_COST)
            .additionalNotes(UPDATED_ADDITIONAL_NOTES)
            .additionalNotesContentType(UPDATED_ADDITIONAL_NOTES_CONTENT_TYPE);

        restWorkProjectRegisterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWorkProjectRegister.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWorkProjectRegister))
            )
            .andExpect(status().isOk());

        // Validate the WorkProjectRegister in the database
        List<WorkProjectRegister> workProjectRegisterList = workProjectRegisterRepository.findAll();
        assertThat(workProjectRegisterList).hasSize(databaseSizeBeforeUpdate);
        WorkProjectRegister testWorkProjectRegister = workProjectRegisterList.get(workProjectRegisterList.size() - 1);
        assertThat(testWorkProjectRegister.getCatalogueNumber()).isEqualTo(UPDATED_CATALOGUE_NUMBER);
        assertThat(testWorkProjectRegister.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testWorkProjectRegister.getDetails()).isEqualTo(UPDATED_DETAILS);
        assertThat(testWorkProjectRegister.getDetailsContentType()).isEqualTo(UPDATED_DETAILS_CONTENT_TYPE);
        assertThat(testWorkProjectRegister.getTotalProjectCost()).isEqualByComparingTo(UPDATED_TOTAL_PROJECT_COST);
        assertThat(testWorkProjectRegister.getAdditionalNotes()).isEqualTo(UPDATED_ADDITIONAL_NOTES);
        assertThat(testWorkProjectRegister.getAdditionalNotesContentType()).isEqualTo(UPDATED_ADDITIONAL_NOTES_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingWorkProjectRegister() throws Exception {
        int databaseSizeBeforeUpdate = workProjectRegisterRepository.findAll().size();
        workProjectRegister.setId(count.incrementAndGet());

        // Create the WorkProjectRegister
        WorkProjectRegisterDTO workProjectRegisterDTO = workProjectRegisterMapper.toDto(workProjectRegister);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWorkProjectRegisterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, workProjectRegisterDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(workProjectRegisterDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkProjectRegister in the database
        List<WorkProjectRegister> workProjectRegisterList = workProjectRegisterRepository.findAll();
        assertThat(workProjectRegisterList).hasSize(databaseSizeBeforeUpdate);

        // Validate the WorkProjectRegister in Elasticsearch
        verify(mockWorkProjectRegisterSearchRepository, times(0)).save(workProjectRegister);
    }

    @Test
    @Transactional
    void patchWithIdMismatchWorkProjectRegister() throws Exception {
        int databaseSizeBeforeUpdate = workProjectRegisterRepository.findAll().size();
        workProjectRegister.setId(count.incrementAndGet());

        // Create the WorkProjectRegister
        WorkProjectRegisterDTO workProjectRegisterDTO = workProjectRegisterMapper.toDto(workProjectRegister);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkProjectRegisterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(workProjectRegisterDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkProjectRegister in the database
        List<WorkProjectRegister> workProjectRegisterList = workProjectRegisterRepository.findAll();
        assertThat(workProjectRegisterList).hasSize(databaseSizeBeforeUpdate);

        // Validate the WorkProjectRegister in Elasticsearch
        verify(mockWorkProjectRegisterSearchRepository, times(0)).save(workProjectRegister);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamWorkProjectRegister() throws Exception {
        int databaseSizeBeforeUpdate = workProjectRegisterRepository.findAll().size();
        workProjectRegister.setId(count.incrementAndGet());

        // Create the WorkProjectRegister
        WorkProjectRegisterDTO workProjectRegisterDTO = workProjectRegisterMapper.toDto(workProjectRegister);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkProjectRegisterMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(workProjectRegisterDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the WorkProjectRegister in the database
        List<WorkProjectRegister> workProjectRegisterList = workProjectRegisterRepository.findAll();
        assertThat(workProjectRegisterList).hasSize(databaseSizeBeforeUpdate);

        // Validate the WorkProjectRegister in Elasticsearch
        verify(mockWorkProjectRegisterSearchRepository, times(0)).save(workProjectRegister);
    }

    @Test
    @Transactional
    void deleteWorkProjectRegister() throws Exception {
        // Initialize the database
        workProjectRegisterRepository.saveAndFlush(workProjectRegister);

        int databaseSizeBeforeDelete = workProjectRegisterRepository.findAll().size();

        // Delete the workProjectRegister
        restWorkProjectRegisterMockMvc
            .perform(delete(ENTITY_API_URL_ID, workProjectRegister.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<WorkProjectRegister> workProjectRegisterList = workProjectRegisterRepository.findAll();
        assertThat(workProjectRegisterList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the WorkProjectRegister in Elasticsearch
        verify(mockWorkProjectRegisterSearchRepository, times(1)).deleteById(workProjectRegister.getId());
    }

    @Test
    @Transactional
    void searchWorkProjectRegister() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        workProjectRegisterRepository.saveAndFlush(workProjectRegister);
        when(mockWorkProjectRegisterSearchRepository.search("id:" + workProjectRegister.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(workProjectRegister), PageRequest.of(0, 1), 1));

        // Search the workProjectRegister
        restWorkProjectRegisterMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + workProjectRegister.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(workProjectRegister.getId().intValue())))
            .andExpect(jsonPath("$.[*].catalogueNumber").value(hasItem(DEFAULT_CATALOGUE_NUMBER)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].detailsContentType").value(hasItem(DEFAULT_DETAILS_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].details").value(hasItem(Base64Utils.encodeToString(DEFAULT_DETAILS))))
            .andExpect(jsonPath("$.[*].totalProjectCost").value(hasItem(sameNumber(DEFAULT_TOTAL_PROJECT_COST))))
            .andExpect(jsonPath("$.[*].additionalNotesContentType").value(hasItem(DEFAULT_ADDITIONAL_NOTES_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].additionalNotes").value(hasItem(Base64Utils.encodeToString(DEFAULT_ADDITIONAL_NOTES))));
    }
}
