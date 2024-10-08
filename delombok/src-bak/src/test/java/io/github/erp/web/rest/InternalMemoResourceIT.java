package io.github.erp.web.rest;

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
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import io.github.erp.IntegrationTest;
import io.github.erp.domain.BusinessDocument;
import io.github.erp.domain.Dealer;
import io.github.erp.domain.InternalMemo;
import io.github.erp.domain.MemoAction;
import io.github.erp.domain.Placeholder;
import io.github.erp.repository.InternalMemoRepository;
import io.github.erp.repository.search.InternalMemoSearchRepository;
import io.github.erp.service.InternalMemoService;
import io.github.erp.service.criteria.InternalMemoCriteria;
import io.github.erp.service.dto.InternalMemoDTO;
import io.github.erp.service.mapper.InternalMemoMapper;
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
 * Integration tests for the {@link InternalMemoResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class InternalMemoResourceIT {

    private static final String DEFAULT_CATALOGUE_NUMBER = "AAAAAAAA";
    private static final String UPDATED_CATALOGUE_NUMBER = "BBBBBBBB";

    private static final String DEFAULT_REFERENCE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_REFERENCE_NUMBER = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_MEMO_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_MEMO_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_MEMO_DATE = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_PURPOSE_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_PURPOSE_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/internal-memos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/internal-memos";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private InternalMemoRepository internalMemoRepository;

    @Mock
    private InternalMemoRepository internalMemoRepositoryMock;

    @Autowired
    private InternalMemoMapper internalMemoMapper;

    @Mock
    private InternalMemoService internalMemoServiceMock;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.InternalMemoSearchRepositoryMockConfiguration
     */
    @Autowired
    private InternalMemoSearchRepository mockInternalMemoSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restInternalMemoMockMvc;

    private InternalMemo internalMemo;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InternalMemo createEntity(EntityManager em) {
        InternalMemo internalMemo = new InternalMemo()
            .catalogueNumber(DEFAULT_CATALOGUE_NUMBER)
            .referenceNumber(DEFAULT_REFERENCE_NUMBER)
            .memoDate(DEFAULT_MEMO_DATE)
            .purposeDescription(DEFAULT_PURPOSE_DESCRIPTION);
        // Add required entity
        BusinessDocument businessDocument;
        if (TestUtil.findAll(em, BusinessDocument.class).isEmpty()) {
            businessDocument = BusinessDocumentResourceIT.createEntity(em);
            em.persist(businessDocument);
            em.flush();
        } else {
            businessDocument = TestUtil.findAll(em, BusinessDocument.class).get(0);
        }
        internalMemo.getMemoDocuments().add(businessDocument);
        return internalMemo;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InternalMemo createUpdatedEntity(EntityManager em) {
        InternalMemo internalMemo = new InternalMemo()
            .catalogueNumber(UPDATED_CATALOGUE_NUMBER)
            .referenceNumber(UPDATED_REFERENCE_NUMBER)
            .memoDate(UPDATED_MEMO_DATE)
            .purposeDescription(UPDATED_PURPOSE_DESCRIPTION);
        // Add required entity
        BusinessDocument businessDocument;
        if (TestUtil.findAll(em, BusinessDocument.class).isEmpty()) {
            businessDocument = BusinessDocumentResourceIT.createUpdatedEntity(em);
            em.persist(businessDocument);
            em.flush();
        } else {
            businessDocument = TestUtil.findAll(em, BusinessDocument.class).get(0);
        }
        internalMemo.getMemoDocuments().add(businessDocument);
        return internalMemo;
    }

    @BeforeEach
    public void initTest() {
        internalMemo = createEntity(em);
    }

    @Test
    @Transactional
    void createInternalMemo() throws Exception {
        int databaseSizeBeforeCreate = internalMemoRepository.findAll().size();
        // Create the InternalMemo
        InternalMemoDTO internalMemoDTO = internalMemoMapper.toDto(internalMemo);
        restInternalMemoMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(internalMemoDTO))
            )
            .andExpect(status().isCreated());

        // Validate the InternalMemo in the database
        List<InternalMemo> internalMemoList = internalMemoRepository.findAll();
        assertThat(internalMemoList).hasSize(databaseSizeBeforeCreate + 1);
        InternalMemo testInternalMemo = internalMemoList.get(internalMemoList.size() - 1);
        assertThat(testInternalMemo.getCatalogueNumber()).isEqualTo(DEFAULT_CATALOGUE_NUMBER);
        assertThat(testInternalMemo.getReferenceNumber()).isEqualTo(DEFAULT_REFERENCE_NUMBER);
        assertThat(testInternalMemo.getMemoDate()).isEqualTo(DEFAULT_MEMO_DATE);
        assertThat(testInternalMemo.getPurposeDescription()).isEqualTo(DEFAULT_PURPOSE_DESCRIPTION);

        // Validate the InternalMemo in Elasticsearch
        verify(mockInternalMemoSearchRepository, times(1)).save(testInternalMemo);
    }

    @Test
    @Transactional
    void createInternalMemoWithExistingId() throws Exception {
        // Create the InternalMemo with an existing ID
        internalMemo.setId(1L);
        InternalMemoDTO internalMemoDTO = internalMemoMapper.toDto(internalMemo);

        int databaseSizeBeforeCreate = internalMemoRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restInternalMemoMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(internalMemoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InternalMemo in the database
        List<InternalMemo> internalMemoList = internalMemoRepository.findAll();
        assertThat(internalMemoList).hasSize(databaseSizeBeforeCreate);

        // Validate the InternalMemo in Elasticsearch
        verify(mockInternalMemoSearchRepository, times(0)).save(internalMemo);
    }

    @Test
    @Transactional
    void checkCatalogueNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = internalMemoRepository.findAll().size();
        // set the field null
        internalMemo.setCatalogueNumber(null);

        // Create the InternalMemo, which fails.
        InternalMemoDTO internalMemoDTO = internalMemoMapper.toDto(internalMemo);

        restInternalMemoMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(internalMemoDTO))
            )
            .andExpect(status().isBadRequest());

        List<InternalMemo> internalMemoList = internalMemoRepository.findAll();
        assertThat(internalMemoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkReferenceNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = internalMemoRepository.findAll().size();
        // set the field null
        internalMemo.setReferenceNumber(null);

        // Create the InternalMemo, which fails.
        InternalMemoDTO internalMemoDTO = internalMemoMapper.toDto(internalMemo);

        restInternalMemoMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(internalMemoDTO))
            )
            .andExpect(status().isBadRequest());

        List<InternalMemo> internalMemoList = internalMemoRepository.findAll();
        assertThat(internalMemoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMemoDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = internalMemoRepository.findAll().size();
        // set the field null
        internalMemo.setMemoDate(null);

        // Create the InternalMemo, which fails.
        InternalMemoDTO internalMemoDTO = internalMemoMapper.toDto(internalMemo);

        restInternalMemoMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(internalMemoDTO))
            )
            .andExpect(status().isBadRequest());

        List<InternalMemo> internalMemoList = internalMemoRepository.findAll();
        assertThat(internalMemoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllInternalMemos() throws Exception {
        // Initialize the database
        internalMemoRepository.saveAndFlush(internalMemo);

        // Get all the internalMemoList
        restInternalMemoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(internalMemo.getId().intValue())))
            .andExpect(jsonPath("$.[*].catalogueNumber").value(hasItem(DEFAULT_CATALOGUE_NUMBER)))
            .andExpect(jsonPath("$.[*].referenceNumber").value(hasItem(DEFAULT_REFERENCE_NUMBER)))
            .andExpect(jsonPath("$.[*].memoDate").value(hasItem(DEFAULT_MEMO_DATE.toString())))
            .andExpect(jsonPath("$.[*].purposeDescription").value(hasItem(DEFAULT_PURPOSE_DESCRIPTION)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllInternalMemosWithEagerRelationshipsIsEnabled() throws Exception {
        when(internalMemoServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restInternalMemoMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(internalMemoServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllInternalMemosWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(internalMemoServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restInternalMemoMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(internalMemoServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getInternalMemo() throws Exception {
        // Initialize the database
        internalMemoRepository.saveAndFlush(internalMemo);

        // Get the internalMemo
        restInternalMemoMockMvc
            .perform(get(ENTITY_API_URL_ID, internalMemo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(internalMemo.getId().intValue()))
            .andExpect(jsonPath("$.catalogueNumber").value(DEFAULT_CATALOGUE_NUMBER))
            .andExpect(jsonPath("$.referenceNumber").value(DEFAULT_REFERENCE_NUMBER))
            .andExpect(jsonPath("$.memoDate").value(DEFAULT_MEMO_DATE.toString()))
            .andExpect(jsonPath("$.purposeDescription").value(DEFAULT_PURPOSE_DESCRIPTION));
    }

    @Test
    @Transactional
    void getInternalMemosByIdFiltering() throws Exception {
        // Initialize the database
        internalMemoRepository.saveAndFlush(internalMemo);

        Long id = internalMemo.getId();

        defaultInternalMemoShouldBeFound("id.equals=" + id);
        defaultInternalMemoShouldNotBeFound("id.notEquals=" + id);

        defaultInternalMemoShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultInternalMemoShouldNotBeFound("id.greaterThan=" + id);

        defaultInternalMemoShouldBeFound("id.lessThanOrEqual=" + id);
        defaultInternalMemoShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllInternalMemosByCatalogueNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        internalMemoRepository.saveAndFlush(internalMemo);

        // Get all the internalMemoList where catalogueNumber equals to DEFAULT_CATALOGUE_NUMBER
        defaultInternalMemoShouldBeFound("catalogueNumber.equals=" + DEFAULT_CATALOGUE_NUMBER);

        // Get all the internalMemoList where catalogueNumber equals to UPDATED_CATALOGUE_NUMBER
        defaultInternalMemoShouldNotBeFound("catalogueNumber.equals=" + UPDATED_CATALOGUE_NUMBER);
    }

    @Test
    @Transactional
    void getAllInternalMemosByCatalogueNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        internalMemoRepository.saveAndFlush(internalMemo);

        // Get all the internalMemoList where catalogueNumber not equals to DEFAULT_CATALOGUE_NUMBER
        defaultInternalMemoShouldNotBeFound("catalogueNumber.notEquals=" + DEFAULT_CATALOGUE_NUMBER);

        // Get all the internalMemoList where catalogueNumber not equals to UPDATED_CATALOGUE_NUMBER
        defaultInternalMemoShouldBeFound("catalogueNumber.notEquals=" + UPDATED_CATALOGUE_NUMBER);
    }

    @Test
    @Transactional
    void getAllInternalMemosByCatalogueNumberIsInShouldWork() throws Exception {
        // Initialize the database
        internalMemoRepository.saveAndFlush(internalMemo);

        // Get all the internalMemoList where catalogueNumber in DEFAULT_CATALOGUE_NUMBER or UPDATED_CATALOGUE_NUMBER
        defaultInternalMemoShouldBeFound("catalogueNumber.in=" + DEFAULT_CATALOGUE_NUMBER + "," + UPDATED_CATALOGUE_NUMBER);

        // Get all the internalMemoList where catalogueNumber equals to UPDATED_CATALOGUE_NUMBER
        defaultInternalMemoShouldNotBeFound("catalogueNumber.in=" + UPDATED_CATALOGUE_NUMBER);
    }

    @Test
    @Transactional
    void getAllInternalMemosByCatalogueNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        internalMemoRepository.saveAndFlush(internalMemo);

        // Get all the internalMemoList where catalogueNumber is not null
        defaultInternalMemoShouldBeFound("catalogueNumber.specified=true");

        // Get all the internalMemoList where catalogueNumber is null
        defaultInternalMemoShouldNotBeFound("catalogueNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllInternalMemosByCatalogueNumberContainsSomething() throws Exception {
        // Initialize the database
        internalMemoRepository.saveAndFlush(internalMemo);

        // Get all the internalMemoList where catalogueNumber contains DEFAULT_CATALOGUE_NUMBER
        defaultInternalMemoShouldBeFound("catalogueNumber.contains=" + DEFAULT_CATALOGUE_NUMBER);

        // Get all the internalMemoList where catalogueNumber contains UPDATED_CATALOGUE_NUMBER
        defaultInternalMemoShouldNotBeFound("catalogueNumber.contains=" + UPDATED_CATALOGUE_NUMBER);
    }

    @Test
    @Transactional
    void getAllInternalMemosByCatalogueNumberNotContainsSomething() throws Exception {
        // Initialize the database
        internalMemoRepository.saveAndFlush(internalMemo);

        // Get all the internalMemoList where catalogueNumber does not contain DEFAULT_CATALOGUE_NUMBER
        defaultInternalMemoShouldNotBeFound("catalogueNumber.doesNotContain=" + DEFAULT_CATALOGUE_NUMBER);

        // Get all the internalMemoList where catalogueNumber does not contain UPDATED_CATALOGUE_NUMBER
        defaultInternalMemoShouldBeFound("catalogueNumber.doesNotContain=" + UPDATED_CATALOGUE_NUMBER);
    }

    @Test
    @Transactional
    void getAllInternalMemosByReferenceNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        internalMemoRepository.saveAndFlush(internalMemo);

        // Get all the internalMemoList where referenceNumber equals to DEFAULT_REFERENCE_NUMBER
        defaultInternalMemoShouldBeFound("referenceNumber.equals=" + DEFAULT_REFERENCE_NUMBER);

        // Get all the internalMemoList where referenceNumber equals to UPDATED_REFERENCE_NUMBER
        defaultInternalMemoShouldNotBeFound("referenceNumber.equals=" + UPDATED_REFERENCE_NUMBER);
    }

    @Test
    @Transactional
    void getAllInternalMemosByReferenceNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        internalMemoRepository.saveAndFlush(internalMemo);

        // Get all the internalMemoList where referenceNumber not equals to DEFAULT_REFERENCE_NUMBER
        defaultInternalMemoShouldNotBeFound("referenceNumber.notEquals=" + DEFAULT_REFERENCE_NUMBER);

        // Get all the internalMemoList where referenceNumber not equals to UPDATED_REFERENCE_NUMBER
        defaultInternalMemoShouldBeFound("referenceNumber.notEquals=" + UPDATED_REFERENCE_NUMBER);
    }

    @Test
    @Transactional
    void getAllInternalMemosByReferenceNumberIsInShouldWork() throws Exception {
        // Initialize the database
        internalMemoRepository.saveAndFlush(internalMemo);

        // Get all the internalMemoList where referenceNumber in DEFAULT_REFERENCE_NUMBER or UPDATED_REFERENCE_NUMBER
        defaultInternalMemoShouldBeFound("referenceNumber.in=" + DEFAULT_REFERENCE_NUMBER + "," + UPDATED_REFERENCE_NUMBER);

        // Get all the internalMemoList where referenceNumber equals to UPDATED_REFERENCE_NUMBER
        defaultInternalMemoShouldNotBeFound("referenceNumber.in=" + UPDATED_REFERENCE_NUMBER);
    }

    @Test
    @Transactional
    void getAllInternalMemosByReferenceNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        internalMemoRepository.saveAndFlush(internalMemo);

        // Get all the internalMemoList where referenceNumber is not null
        defaultInternalMemoShouldBeFound("referenceNumber.specified=true");

        // Get all the internalMemoList where referenceNumber is null
        defaultInternalMemoShouldNotBeFound("referenceNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllInternalMemosByReferenceNumberContainsSomething() throws Exception {
        // Initialize the database
        internalMemoRepository.saveAndFlush(internalMemo);

        // Get all the internalMemoList where referenceNumber contains DEFAULT_REFERENCE_NUMBER
        defaultInternalMemoShouldBeFound("referenceNumber.contains=" + DEFAULT_REFERENCE_NUMBER);

        // Get all the internalMemoList where referenceNumber contains UPDATED_REFERENCE_NUMBER
        defaultInternalMemoShouldNotBeFound("referenceNumber.contains=" + UPDATED_REFERENCE_NUMBER);
    }

    @Test
    @Transactional
    void getAllInternalMemosByReferenceNumberNotContainsSomething() throws Exception {
        // Initialize the database
        internalMemoRepository.saveAndFlush(internalMemo);

        // Get all the internalMemoList where referenceNumber does not contain DEFAULT_REFERENCE_NUMBER
        defaultInternalMemoShouldNotBeFound("referenceNumber.doesNotContain=" + DEFAULT_REFERENCE_NUMBER);

        // Get all the internalMemoList where referenceNumber does not contain UPDATED_REFERENCE_NUMBER
        defaultInternalMemoShouldBeFound("referenceNumber.doesNotContain=" + UPDATED_REFERENCE_NUMBER);
    }

    @Test
    @Transactional
    void getAllInternalMemosByMemoDateIsEqualToSomething() throws Exception {
        // Initialize the database
        internalMemoRepository.saveAndFlush(internalMemo);

        // Get all the internalMemoList where memoDate equals to DEFAULT_MEMO_DATE
        defaultInternalMemoShouldBeFound("memoDate.equals=" + DEFAULT_MEMO_DATE);

        // Get all the internalMemoList where memoDate equals to UPDATED_MEMO_DATE
        defaultInternalMemoShouldNotBeFound("memoDate.equals=" + UPDATED_MEMO_DATE);
    }

    @Test
    @Transactional
    void getAllInternalMemosByMemoDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        internalMemoRepository.saveAndFlush(internalMemo);

        // Get all the internalMemoList where memoDate not equals to DEFAULT_MEMO_DATE
        defaultInternalMemoShouldNotBeFound("memoDate.notEquals=" + DEFAULT_MEMO_DATE);

        // Get all the internalMemoList where memoDate not equals to UPDATED_MEMO_DATE
        defaultInternalMemoShouldBeFound("memoDate.notEquals=" + UPDATED_MEMO_DATE);
    }

    @Test
    @Transactional
    void getAllInternalMemosByMemoDateIsInShouldWork() throws Exception {
        // Initialize the database
        internalMemoRepository.saveAndFlush(internalMemo);

        // Get all the internalMemoList where memoDate in DEFAULT_MEMO_DATE or UPDATED_MEMO_DATE
        defaultInternalMemoShouldBeFound("memoDate.in=" + DEFAULT_MEMO_DATE + "," + UPDATED_MEMO_DATE);

        // Get all the internalMemoList where memoDate equals to UPDATED_MEMO_DATE
        defaultInternalMemoShouldNotBeFound("memoDate.in=" + UPDATED_MEMO_DATE);
    }

    @Test
    @Transactional
    void getAllInternalMemosByMemoDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        internalMemoRepository.saveAndFlush(internalMemo);

        // Get all the internalMemoList where memoDate is not null
        defaultInternalMemoShouldBeFound("memoDate.specified=true");

        // Get all the internalMemoList where memoDate is null
        defaultInternalMemoShouldNotBeFound("memoDate.specified=false");
    }

    @Test
    @Transactional
    void getAllInternalMemosByMemoDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        internalMemoRepository.saveAndFlush(internalMemo);

        // Get all the internalMemoList where memoDate is greater than or equal to DEFAULT_MEMO_DATE
        defaultInternalMemoShouldBeFound("memoDate.greaterThanOrEqual=" + DEFAULT_MEMO_DATE);

        // Get all the internalMemoList where memoDate is greater than or equal to UPDATED_MEMO_DATE
        defaultInternalMemoShouldNotBeFound("memoDate.greaterThanOrEqual=" + UPDATED_MEMO_DATE);
    }

    @Test
    @Transactional
    void getAllInternalMemosByMemoDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        internalMemoRepository.saveAndFlush(internalMemo);

        // Get all the internalMemoList where memoDate is less than or equal to DEFAULT_MEMO_DATE
        defaultInternalMemoShouldBeFound("memoDate.lessThanOrEqual=" + DEFAULT_MEMO_DATE);

        // Get all the internalMemoList where memoDate is less than or equal to SMALLER_MEMO_DATE
        defaultInternalMemoShouldNotBeFound("memoDate.lessThanOrEqual=" + SMALLER_MEMO_DATE);
    }

    @Test
    @Transactional
    void getAllInternalMemosByMemoDateIsLessThanSomething() throws Exception {
        // Initialize the database
        internalMemoRepository.saveAndFlush(internalMemo);

        // Get all the internalMemoList where memoDate is less than DEFAULT_MEMO_DATE
        defaultInternalMemoShouldNotBeFound("memoDate.lessThan=" + DEFAULT_MEMO_DATE);

        // Get all the internalMemoList where memoDate is less than UPDATED_MEMO_DATE
        defaultInternalMemoShouldBeFound("memoDate.lessThan=" + UPDATED_MEMO_DATE);
    }

    @Test
    @Transactional
    void getAllInternalMemosByMemoDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        internalMemoRepository.saveAndFlush(internalMemo);

        // Get all the internalMemoList where memoDate is greater than DEFAULT_MEMO_DATE
        defaultInternalMemoShouldNotBeFound("memoDate.greaterThan=" + DEFAULT_MEMO_DATE);

        // Get all the internalMemoList where memoDate is greater than SMALLER_MEMO_DATE
        defaultInternalMemoShouldBeFound("memoDate.greaterThan=" + SMALLER_MEMO_DATE);
    }

    @Test
    @Transactional
    void getAllInternalMemosByPurposeDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        internalMemoRepository.saveAndFlush(internalMemo);

        // Get all the internalMemoList where purposeDescription equals to DEFAULT_PURPOSE_DESCRIPTION
        defaultInternalMemoShouldBeFound("purposeDescription.equals=" + DEFAULT_PURPOSE_DESCRIPTION);

        // Get all the internalMemoList where purposeDescription equals to UPDATED_PURPOSE_DESCRIPTION
        defaultInternalMemoShouldNotBeFound("purposeDescription.equals=" + UPDATED_PURPOSE_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllInternalMemosByPurposeDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        internalMemoRepository.saveAndFlush(internalMemo);

        // Get all the internalMemoList where purposeDescription not equals to DEFAULT_PURPOSE_DESCRIPTION
        defaultInternalMemoShouldNotBeFound("purposeDescription.notEquals=" + DEFAULT_PURPOSE_DESCRIPTION);

        // Get all the internalMemoList where purposeDescription not equals to UPDATED_PURPOSE_DESCRIPTION
        defaultInternalMemoShouldBeFound("purposeDescription.notEquals=" + UPDATED_PURPOSE_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllInternalMemosByPurposeDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        internalMemoRepository.saveAndFlush(internalMemo);

        // Get all the internalMemoList where purposeDescription in DEFAULT_PURPOSE_DESCRIPTION or UPDATED_PURPOSE_DESCRIPTION
        defaultInternalMemoShouldBeFound("purposeDescription.in=" + DEFAULT_PURPOSE_DESCRIPTION + "," + UPDATED_PURPOSE_DESCRIPTION);

        // Get all the internalMemoList where purposeDescription equals to UPDATED_PURPOSE_DESCRIPTION
        defaultInternalMemoShouldNotBeFound("purposeDescription.in=" + UPDATED_PURPOSE_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllInternalMemosByPurposeDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        internalMemoRepository.saveAndFlush(internalMemo);

        // Get all the internalMemoList where purposeDescription is not null
        defaultInternalMemoShouldBeFound("purposeDescription.specified=true");

        // Get all the internalMemoList where purposeDescription is null
        defaultInternalMemoShouldNotBeFound("purposeDescription.specified=false");
    }

    @Test
    @Transactional
    void getAllInternalMemosByPurposeDescriptionContainsSomething() throws Exception {
        // Initialize the database
        internalMemoRepository.saveAndFlush(internalMemo);

        // Get all the internalMemoList where purposeDescription contains DEFAULT_PURPOSE_DESCRIPTION
        defaultInternalMemoShouldBeFound("purposeDescription.contains=" + DEFAULT_PURPOSE_DESCRIPTION);

        // Get all the internalMemoList where purposeDescription contains UPDATED_PURPOSE_DESCRIPTION
        defaultInternalMemoShouldNotBeFound("purposeDescription.contains=" + UPDATED_PURPOSE_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllInternalMemosByPurposeDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        internalMemoRepository.saveAndFlush(internalMemo);

        // Get all the internalMemoList where purposeDescription does not contain DEFAULT_PURPOSE_DESCRIPTION
        defaultInternalMemoShouldNotBeFound("purposeDescription.doesNotContain=" + DEFAULT_PURPOSE_DESCRIPTION);

        // Get all the internalMemoList where purposeDescription does not contain UPDATED_PURPOSE_DESCRIPTION
        defaultInternalMemoShouldBeFound("purposeDescription.doesNotContain=" + UPDATED_PURPOSE_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllInternalMemosByActionRequiredIsEqualToSomething() throws Exception {
        // Initialize the database
        internalMemoRepository.saveAndFlush(internalMemo);
        MemoAction actionRequired;
        if (TestUtil.findAll(em, MemoAction.class).isEmpty()) {
            actionRequired = MemoActionResourceIT.createEntity(em);
            em.persist(actionRequired);
            em.flush();
        } else {
            actionRequired = TestUtil.findAll(em, MemoAction.class).get(0);
        }
        em.persist(actionRequired);
        em.flush();
        internalMemo.setActionRequired(actionRequired);
        internalMemoRepository.saveAndFlush(internalMemo);
        Long actionRequiredId = actionRequired.getId();

        // Get all the internalMemoList where actionRequired equals to actionRequiredId
        defaultInternalMemoShouldBeFound("actionRequiredId.equals=" + actionRequiredId);

        // Get all the internalMemoList where actionRequired equals to (actionRequiredId + 1)
        defaultInternalMemoShouldNotBeFound("actionRequiredId.equals=" + (actionRequiredId + 1));
    }

    @Test
    @Transactional
    void getAllInternalMemosByAddressedToIsEqualToSomething() throws Exception {
        // Initialize the database
        internalMemoRepository.saveAndFlush(internalMemo);
        Dealer addressedTo;
        if (TestUtil.findAll(em, Dealer.class).isEmpty()) {
            addressedTo = DealerResourceIT.createEntity(em);
            em.persist(addressedTo);
            em.flush();
        } else {
            addressedTo = TestUtil.findAll(em, Dealer.class).get(0);
        }
        em.persist(addressedTo);
        em.flush();
        internalMemo.setAddressedTo(addressedTo);
        internalMemoRepository.saveAndFlush(internalMemo);
        Long addressedToId = addressedTo.getId();

        // Get all the internalMemoList where addressedTo equals to addressedToId
        defaultInternalMemoShouldBeFound("addressedToId.equals=" + addressedToId);

        // Get all the internalMemoList where addressedTo equals to (addressedToId + 1)
        defaultInternalMemoShouldNotBeFound("addressedToId.equals=" + (addressedToId + 1));
    }

    @Test
    @Transactional
    void getAllInternalMemosByFromIsEqualToSomething() throws Exception {
        // Initialize the database
        internalMemoRepository.saveAndFlush(internalMemo);
        Dealer from;
        if (TestUtil.findAll(em, Dealer.class).isEmpty()) {
            from = DealerResourceIT.createEntity(em);
            em.persist(from);
            em.flush();
        } else {
            from = TestUtil.findAll(em, Dealer.class).get(0);
        }
        em.persist(from);
        em.flush();
        internalMemo.setFrom(from);
        internalMemoRepository.saveAndFlush(internalMemo);
        Long fromId = from.getId();

        // Get all the internalMemoList where from equals to fromId
        defaultInternalMemoShouldBeFound("fromId.equals=" + fromId);

        // Get all the internalMemoList where from equals to (fromId + 1)
        defaultInternalMemoShouldNotBeFound("fromId.equals=" + (fromId + 1));
    }

    @Test
    @Transactional
    void getAllInternalMemosByPreparedByIsEqualToSomething() throws Exception {
        // Initialize the database
        internalMemoRepository.saveAndFlush(internalMemo);
        Dealer preparedBy;
        if (TestUtil.findAll(em, Dealer.class).isEmpty()) {
            preparedBy = DealerResourceIT.createEntity(em);
            em.persist(preparedBy);
            em.flush();
        } else {
            preparedBy = TestUtil.findAll(em, Dealer.class).get(0);
        }
        em.persist(preparedBy);
        em.flush();
        internalMemo.addPreparedBy(preparedBy);
        internalMemoRepository.saveAndFlush(internalMemo);
        Long preparedById = preparedBy.getId();

        // Get all the internalMemoList where preparedBy equals to preparedById
        defaultInternalMemoShouldBeFound("preparedById.equals=" + preparedById);

        // Get all the internalMemoList where preparedBy equals to (preparedById + 1)
        defaultInternalMemoShouldNotBeFound("preparedById.equals=" + (preparedById + 1));
    }

    @Test
    @Transactional
    void getAllInternalMemosByReviewedByIsEqualToSomething() throws Exception {
        // Initialize the database
        internalMemoRepository.saveAndFlush(internalMemo);
        Dealer reviewedBy;
        if (TestUtil.findAll(em, Dealer.class).isEmpty()) {
            reviewedBy = DealerResourceIT.createEntity(em);
            em.persist(reviewedBy);
            em.flush();
        } else {
            reviewedBy = TestUtil.findAll(em, Dealer.class).get(0);
        }
        em.persist(reviewedBy);
        em.flush();
        internalMemo.addReviewedBy(reviewedBy);
        internalMemoRepository.saveAndFlush(internalMemo);
        Long reviewedById = reviewedBy.getId();

        // Get all the internalMemoList where reviewedBy equals to reviewedById
        defaultInternalMemoShouldBeFound("reviewedById.equals=" + reviewedById);

        // Get all the internalMemoList where reviewedBy equals to (reviewedById + 1)
        defaultInternalMemoShouldNotBeFound("reviewedById.equals=" + (reviewedById + 1));
    }

    @Test
    @Transactional
    void getAllInternalMemosByApprovedByIsEqualToSomething() throws Exception {
        // Initialize the database
        internalMemoRepository.saveAndFlush(internalMemo);
        Dealer approvedBy;
        if (TestUtil.findAll(em, Dealer.class).isEmpty()) {
            approvedBy = DealerResourceIT.createEntity(em);
            em.persist(approvedBy);
            em.flush();
        } else {
            approvedBy = TestUtil.findAll(em, Dealer.class).get(0);
        }
        em.persist(approvedBy);
        em.flush();
        internalMemo.addApprovedBy(approvedBy);
        internalMemoRepository.saveAndFlush(internalMemo);
        Long approvedById = approvedBy.getId();

        // Get all the internalMemoList where approvedBy equals to approvedById
        defaultInternalMemoShouldBeFound("approvedById.equals=" + approvedById);

        // Get all the internalMemoList where approvedBy equals to (approvedById + 1)
        defaultInternalMemoShouldNotBeFound("approvedById.equals=" + (approvedById + 1));
    }

    @Test
    @Transactional
    void getAllInternalMemosByMemoDocumentIsEqualToSomething() throws Exception {
        // Initialize the database
        internalMemoRepository.saveAndFlush(internalMemo);
        BusinessDocument memoDocument;
        if (TestUtil.findAll(em, BusinessDocument.class).isEmpty()) {
            memoDocument = BusinessDocumentResourceIT.createEntity(em);
            em.persist(memoDocument);
            em.flush();
        } else {
            memoDocument = TestUtil.findAll(em, BusinessDocument.class).get(0);
        }
        em.persist(memoDocument);
        em.flush();
        internalMemo.addMemoDocument(memoDocument);
        internalMemoRepository.saveAndFlush(internalMemo);
        Long memoDocumentId = memoDocument.getId();

        // Get all the internalMemoList where memoDocument equals to memoDocumentId
        defaultInternalMemoShouldBeFound("memoDocumentId.equals=" + memoDocumentId);

        // Get all the internalMemoList where memoDocument equals to (memoDocumentId + 1)
        defaultInternalMemoShouldNotBeFound("memoDocumentId.equals=" + (memoDocumentId + 1));
    }

    @Test
    @Transactional
    void getAllInternalMemosByPlaceholderIsEqualToSomething() throws Exception {
        // Initialize the database
        internalMemoRepository.saveAndFlush(internalMemo);
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
        internalMemo.addPlaceholder(placeholder);
        internalMemoRepository.saveAndFlush(internalMemo);
        Long placeholderId = placeholder.getId();

        // Get all the internalMemoList where placeholder equals to placeholderId
        defaultInternalMemoShouldBeFound("placeholderId.equals=" + placeholderId);

        // Get all the internalMemoList where placeholder equals to (placeholderId + 1)
        defaultInternalMemoShouldNotBeFound("placeholderId.equals=" + (placeholderId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultInternalMemoShouldBeFound(String filter) throws Exception {
        restInternalMemoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(internalMemo.getId().intValue())))
            .andExpect(jsonPath("$.[*].catalogueNumber").value(hasItem(DEFAULT_CATALOGUE_NUMBER)))
            .andExpect(jsonPath("$.[*].referenceNumber").value(hasItem(DEFAULT_REFERENCE_NUMBER)))
            .andExpect(jsonPath("$.[*].memoDate").value(hasItem(DEFAULT_MEMO_DATE.toString())))
            .andExpect(jsonPath("$.[*].purposeDescription").value(hasItem(DEFAULT_PURPOSE_DESCRIPTION)));

        // Check, that the count call also returns 1
        restInternalMemoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultInternalMemoShouldNotBeFound(String filter) throws Exception {
        restInternalMemoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restInternalMemoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingInternalMemo() throws Exception {
        // Get the internalMemo
        restInternalMemoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewInternalMemo() throws Exception {
        // Initialize the database
        internalMemoRepository.saveAndFlush(internalMemo);

        int databaseSizeBeforeUpdate = internalMemoRepository.findAll().size();

        // Update the internalMemo
        InternalMemo updatedInternalMemo = internalMemoRepository.findById(internalMemo.getId()).get();
        // Disconnect from session so that the updates on updatedInternalMemo are not directly saved in db
        em.detach(updatedInternalMemo);
        updatedInternalMemo
            .catalogueNumber(UPDATED_CATALOGUE_NUMBER)
            .referenceNumber(UPDATED_REFERENCE_NUMBER)
            .memoDate(UPDATED_MEMO_DATE)
            .purposeDescription(UPDATED_PURPOSE_DESCRIPTION);
        InternalMemoDTO internalMemoDTO = internalMemoMapper.toDto(updatedInternalMemo);

        restInternalMemoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, internalMemoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(internalMemoDTO))
            )
            .andExpect(status().isOk());

        // Validate the InternalMemo in the database
        List<InternalMemo> internalMemoList = internalMemoRepository.findAll();
        assertThat(internalMemoList).hasSize(databaseSizeBeforeUpdate);
        InternalMemo testInternalMemo = internalMemoList.get(internalMemoList.size() - 1);
        assertThat(testInternalMemo.getCatalogueNumber()).isEqualTo(UPDATED_CATALOGUE_NUMBER);
        assertThat(testInternalMemo.getReferenceNumber()).isEqualTo(UPDATED_REFERENCE_NUMBER);
        assertThat(testInternalMemo.getMemoDate()).isEqualTo(UPDATED_MEMO_DATE);
        assertThat(testInternalMemo.getPurposeDescription()).isEqualTo(UPDATED_PURPOSE_DESCRIPTION);

        // Validate the InternalMemo in Elasticsearch
        verify(mockInternalMemoSearchRepository).save(testInternalMemo);
    }

    @Test
    @Transactional
    void putNonExistingInternalMemo() throws Exception {
        int databaseSizeBeforeUpdate = internalMemoRepository.findAll().size();
        internalMemo.setId(count.incrementAndGet());

        // Create the InternalMemo
        InternalMemoDTO internalMemoDTO = internalMemoMapper.toDto(internalMemo);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInternalMemoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, internalMemoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(internalMemoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InternalMemo in the database
        List<InternalMemo> internalMemoList = internalMemoRepository.findAll();
        assertThat(internalMemoList).hasSize(databaseSizeBeforeUpdate);

        // Validate the InternalMemo in Elasticsearch
        verify(mockInternalMemoSearchRepository, times(0)).save(internalMemo);
    }

    @Test
    @Transactional
    void putWithIdMismatchInternalMemo() throws Exception {
        int databaseSizeBeforeUpdate = internalMemoRepository.findAll().size();
        internalMemo.setId(count.incrementAndGet());

        // Create the InternalMemo
        InternalMemoDTO internalMemoDTO = internalMemoMapper.toDto(internalMemo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInternalMemoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(internalMemoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InternalMemo in the database
        List<InternalMemo> internalMemoList = internalMemoRepository.findAll();
        assertThat(internalMemoList).hasSize(databaseSizeBeforeUpdate);

        // Validate the InternalMemo in Elasticsearch
        verify(mockInternalMemoSearchRepository, times(0)).save(internalMemo);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamInternalMemo() throws Exception {
        int databaseSizeBeforeUpdate = internalMemoRepository.findAll().size();
        internalMemo.setId(count.incrementAndGet());

        // Create the InternalMemo
        InternalMemoDTO internalMemoDTO = internalMemoMapper.toDto(internalMemo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInternalMemoMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(internalMemoDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the InternalMemo in the database
        List<InternalMemo> internalMemoList = internalMemoRepository.findAll();
        assertThat(internalMemoList).hasSize(databaseSizeBeforeUpdate);

        // Validate the InternalMemo in Elasticsearch
        verify(mockInternalMemoSearchRepository, times(0)).save(internalMemo);
    }

    @Test
    @Transactional
    void partialUpdateInternalMemoWithPatch() throws Exception {
        // Initialize the database
        internalMemoRepository.saveAndFlush(internalMemo);

        int databaseSizeBeforeUpdate = internalMemoRepository.findAll().size();

        // Update the internalMemo using partial update
        InternalMemo partialUpdatedInternalMemo = new InternalMemo();
        partialUpdatedInternalMemo.setId(internalMemo.getId());

        partialUpdatedInternalMemo
            .catalogueNumber(UPDATED_CATALOGUE_NUMBER)
            .memoDate(UPDATED_MEMO_DATE)
            .purposeDescription(UPDATED_PURPOSE_DESCRIPTION);

        restInternalMemoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInternalMemo.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedInternalMemo))
            )
            .andExpect(status().isOk());

        // Validate the InternalMemo in the database
        List<InternalMemo> internalMemoList = internalMemoRepository.findAll();
        assertThat(internalMemoList).hasSize(databaseSizeBeforeUpdate);
        InternalMemo testInternalMemo = internalMemoList.get(internalMemoList.size() - 1);
        assertThat(testInternalMemo.getCatalogueNumber()).isEqualTo(UPDATED_CATALOGUE_NUMBER);
        assertThat(testInternalMemo.getReferenceNumber()).isEqualTo(DEFAULT_REFERENCE_NUMBER);
        assertThat(testInternalMemo.getMemoDate()).isEqualTo(UPDATED_MEMO_DATE);
        assertThat(testInternalMemo.getPurposeDescription()).isEqualTo(UPDATED_PURPOSE_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateInternalMemoWithPatch() throws Exception {
        // Initialize the database
        internalMemoRepository.saveAndFlush(internalMemo);

        int databaseSizeBeforeUpdate = internalMemoRepository.findAll().size();

        // Update the internalMemo using partial update
        InternalMemo partialUpdatedInternalMemo = new InternalMemo();
        partialUpdatedInternalMemo.setId(internalMemo.getId());

        partialUpdatedInternalMemo
            .catalogueNumber(UPDATED_CATALOGUE_NUMBER)
            .referenceNumber(UPDATED_REFERENCE_NUMBER)
            .memoDate(UPDATED_MEMO_DATE)
            .purposeDescription(UPDATED_PURPOSE_DESCRIPTION);

        restInternalMemoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInternalMemo.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedInternalMemo))
            )
            .andExpect(status().isOk());

        // Validate the InternalMemo in the database
        List<InternalMemo> internalMemoList = internalMemoRepository.findAll();
        assertThat(internalMemoList).hasSize(databaseSizeBeforeUpdate);
        InternalMemo testInternalMemo = internalMemoList.get(internalMemoList.size() - 1);
        assertThat(testInternalMemo.getCatalogueNumber()).isEqualTo(UPDATED_CATALOGUE_NUMBER);
        assertThat(testInternalMemo.getReferenceNumber()).isEqualTo(UPDATED_REFERENCE_NUMBER);
        assertThat(testInternalMemo.getMemoDate()).isEqualTo(UPDATED_MEMO_DATE);
        assertThat(testInternalMemo.getPurposeDescription()).isEqualTo(UPDATED_PURPOSE_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingInternalMemo() throws Exception {
        int databaseSizeBeforeUpdate = internalMemoRepository.findAll().size();
        internalMemo.setId(count.incrementAndGet());

        // Create the InternalMemo
        InternalMemoDTO internalMemoDTO = internalMemoMapper.toDto(internalMemo);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInternalMemoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, internalMemoDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(internalMemoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InternalMemo in the database
        List<InternalMemo> internalMemoList = internalMemoRepository.findAll();
        assertThat(internalMemoList).hasSize(databaseSizeBeforeUpdate);

        // Validate the InternalMemo in Elasticsearch
        verify(mockInternalMemoSearchRepository, times(0)).save(internalMemo);
    }

    @Test
    @Transactional
    void patchWithIdMismatchInternalMemo() throws Exception {
        int databaseSizeBeforeUpdate = internalMemoRepository.findAll().size();
        internalMemo.setId(count.incrementAndGet());

        // Create the InternalMemo
        InternalMemoDTO internalMemoDTO = internalMemoMapper.toDto(internalMemo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInternalMemoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(internalMemoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InternalMemo in the database
        List<InternalMemo> internalMemoList = internalMemoRepository.findAll();
        assertThat(internalMemoList).hasSize(databaseSizeBeforeUpdate);

        // Validate the InternalMemo in Elasticsearch
        verify(mockInternalMemoSearchRepository, times(0)).save(internalMemo);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamInternalMemo() throws Exception {
        int databaseSizeBeforeUpdate = internalMemoRepository.findAll().size();
        internalMemo.setId(count.incrementAndGet());

        // Create the InternalMemo
        InternalMemoDTO internalMemoDTO = internalMemoMapper.toDto(internalMemo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInternalMemoMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(internalMemoDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the InternalMemo in the database
        List<InternalMemo> internalMemoList = internalMemoRepository.findAll();
        assertThat(internalMemoList).hasSize(databaseSizeBeforeUpdate);

        // Validate the InternalMemo in Elasticsearch
        verify(mockInternalMemoSearchRepository, times(0)).save(internalMemo);
    }

    @Test
    @Transactional
    void deleteInternalMemo() throws Exception {
        // Initialize the database
        internalMemoRepository.saveAndFlush(internalMemo);

        int databaseSizeBeforeDelete = internalMemoRepository.findAll().size();

        // Delete the internalMemo
        restInternalMemoMockMvc
            .perform(delete(ENTITY_API_URL_ID, internalMemo.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<InternalMemo> internalMemoList = internalMemoRepository.findAll();
        assertThat(internalMemoList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the InternalMemo in Elasticsearch
        verify(mockInternalMemoSearchRepository, times(1)).deleteById(internalMemo.getId());
    }

    @Test
    @Transactional
    void searchInternalMemo() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        internalMemoRepository.saveAndFlush(internalMemo);
        when(mockInternalMemoSearchRepository.search("id:" + internalMemo.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(internalMemo), PageRequest.of(0, 1), 1));

        // Search the internalMemo
        restInternalMemoMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + internalMemo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(internalMemo.getId().intValue())))
            .andExpect(jsonPath("$.[*].catalogueNumber").value(hasItem(DEFAULT_CATALOGUE_NUMBER)))
            .andExpect(jsonPath("$.[*].referenceNumber").value(hasItem(DEFAULT_REFERENCE_NUMBER)))
            .andExpect(jsonPath("$.[*].memoDate").value(hasItem(DEFAULT_MEMO_DATE.toString())))
            .andExpect(jsonPath("$.[*].purposeDescription").value(hasItem(DEFAULT_PURPOSE_DESCRIPTION)));
    }
}
