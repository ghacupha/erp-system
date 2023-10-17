package io.github.erp.erp.resources;

/*-
 * Erp System - Mark VI No 3 (Phoebe Series) Server ver 1.5.4
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
import io.github.erp.domain.enumeration.ContractStatus;
import io.github.erp.domain.enumeration.ContractType;
import io.github.erp.repository.ContractMetadataRepository;
import io.github.erp.repository.search.ContractMetadataSearchRepository;
import io.github.erp.service.ContractMetadataService;
import io.github.erp.service.dto.ContractMetadataDTO;
import io.github.erp.service.mapper.ContractMetadataMapper;
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
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link ContractMetadataResourceProd} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ContractMetadataResourceIT {

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final ContractType DEFAULT_TYPE_OF_CONTRACT = ContractType.SUPPLIER;
    private static final ContractType UPDATED_TYPE_OF_CONTRACT = ContractType.CUSTOMER;

    private static final ContractStatus DEFAULT_CONTRACT_STATUS = ContractStatus.ACTIVE;
    private static final ContractStatus UPDATED_CONTRACT_STATUS = ContractStatus.INACTIVE;

    private static final LocalDate DEFAULT_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_START_DATE = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_TERMINATION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_TERMINATION_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_TERMINATION_DATE = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_COMMENTS_AND_ATTACHMENT = "AAAAAAAAAA";
    private static final String UPDATED_COMMENTS_AND_ATTACHMENT = "BBBBBBBBBB";

    private static final String DEFAULT_CONTRACT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_CONTRACT_TITLE = "BBBBBBBBBB";

    private static final UUID DEFAULT_CONTRACT_IDENTIFIER = UUID.randomUUID();
    private static final UUID UPDATED_CONTRACT_IDENTIFIER = UUID.randomUUID();

    private static final String DEFAULT_CONTRACT_IDENTIFIER_SHORT = "AAAAAAAAAA";
    private static final String UPDATED_CONTRACT_IDENTIFIER_SHORT = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/contract-metadata";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/contract-metadata";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ContractMetadataRepository contractMetadataRepository;

    @Mock
    private ContractMetadataRepository contractMetadataRepositoryMock;

    @Autowired
    private ContractMetadataMapper contractMetadataMapper;

    @Mock
    private ContractMetadataService contractMetadataServiceMock;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.ContractMetadataSearchRepositoryMockConfiguration
     */
    @Autowired
    private ContractMetadataSearchRepository mockContractMetadataSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restContractMetadataMockMvc;

    private ContractMetadata contractMetadata;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ContractMetadata createEntity(EntityManager em) {
        ContractMetadata contractMetadata = new ContractMetadata()
            .description(DEFAULT_DESCRIPTION)
            .typeOfContract(DEFAULT_TYPE_OF_CONTRACT)
            .contractStatus(DEFAULT_CONTRACT_STATUS)
            .startDate(DEFAULT_START_DATE)
            .terminationDate(DEFAULT_TERMINATION_DATE)
            .commentsAndAttachment(DEFAULT_COMMENTS_AND_ATTACHMENT)
            .contractTitle(DEFAULT_CONTRACT_TITLE)
            .contractIdentifier(DEFAULT_CONTRACT_IDENTIFIER)
            .contractIdentifierShort(DEFAULT_CONTRACT_IDENTIFIER_SHORT);
        return contractMetadata;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ContractMetadata createUpdatedEntity(EntityManager em) {
        ContractMetadata contractMetadata = new ContractMetadata()
            .description(UPDATED_DESCRIPTION)
            .typeOfContract(UPDATED_TYPE_OF_CONTRACT)
            .contractStatus(UPDATED_CONTRACT_STATUS)
            .startDate(UPDATED_START_DATE)
            .terminationDate(UPDATED_TERMINATION_DATE)
            .commentsAndAttachment(UPDATED_COMMENTS_AND_ATTACHMENT)
            .contractTitle(UPDATED_CONTRACT_TITLE)
            .contractIdentifier(UPDATED_CONTRACT_IDENTIFIER)
            .contractIdentifierShort(UPDATED_CONTRACT_IDENTIFIER_SHORT);
        return contractMetadata;
    }

    @BeforeEach
    public void initTest() {
        contractMetadata = createEntity(em);
    }

    @Test
    @Transactional
    void createContractMetadata() throws Exception {
        int databaseSizeBeforeCreate = contractMetadataRepository.findAll().size();
        // Create the ContractMetadata
        ContractMetadataDTO contractMetadataDTO = contractMetadataMapper.toDto(contractMetadata);
        restContractMetadataMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contractMetadataDTO))
            )
            .andExpect(status().isCreated());

        // Validate the ContractMetadata in the database
        List<ContractMetadata> contractMetadataList = contractMetadataRepository.findAll();
        assertThat(contractMetadataList).hasSize(databaseSizeBeforeCreate + 1);
        ContractMetadata testContractMetadata = contractMetadataList.get(contractMetadataList.size() - 1);
        assertThat(testContractMetadata.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testContractMetadata.getTypeOfContract()).isEqualTo(DEFAULT_TYPE_OF_CONTRACT);
        assertThat(testContractMetadata.getContractStatus()).isEqualTo(DEFAULT_CONTRACT_STATUS);
        assertThat(testContractMetadata.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testContractMetadata.getTerminationDate()).isEqualTo(DEFAULT_TERMINATION_DATE);
        assertThat(testContractMetadata.getCommentsAndAttachment()).isEqualTo(DEFAULT_COMMENTS_AND_ATTACHMENT);
        assertThat(testContractMetadata.getContractTitle()).isEqualTo(DEFAULT_CONTRACT_TITLE);
        assertThat(testContractMetadata.getContractIdentifier()).isEqualTo(DEFAULT_CONTRACT_IDENTIFIER);
        assertThat(testContractMetadata.getContractIdentifierShort()).isEqualTo(DEFAULT_CONTRACT_IDENTIFIER_SHORT);

        // Validate the ContractMetadata in Elasticsearch
        verify(mockContractMetadataSearchRepository, times(1)).save(testContractMetadata);
    }

    @Test
    @Transactional
    void createContractMetadataWithExistingId() throws Exception {
        // Create the ContractMetadata with an existing ID
        contractMetadata.setId(1L);
        ContractMetadataDTO contractMetadataDTO = contractMetadataMapper.toDto(contractMetadata);

        int databaseSizeBeforeCreate = contractMetadataRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restContractMetadataMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contractMetadataDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContractMetadata in the database
        List<ContractMetadata> contractMetadataList = contractMetadataRepository.findAll();
        assertThat(contractMetadataList).hasSize(databaseSizeBeforeCreate);

        // Validate the ContractMetadata in Elasticsearch
        verify(mockContractMetadataSearchRepository, times(0)).save(contractMetadata);
    }

    @Test
    @Transactional
    void checkTypeOfContractIsRequired() throws Exception {
        int databaseSizeBeforeTest = contractMetadataRepository.findAll().size();
        // set the field null
        contractMetadata.setTypeOfContract(null);

        // Create the ContractMetadata, which fails.
        ContractMetadataDTO contractMetadataDTO = contractMetadataMapper.toDto(contractMetadata);

        restContractMetadataMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contractMetadataDTO))
            )
            .andExpect(status().isBadRequest());

        List<ContractMetadata> contractMetadataList = contractMetadataRepository.findAll();
        assertThat(contractMetadataList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkContractStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = contractMetadataRepository.findAll().size();
        // set the field null
        contractMetadata.setContractStatus(null);

        // Create the ContractMetadata, which fails.
        ContractMetadataDTO contractMetadataDTO = contractMetadataMapper.toDto(contractMetadata);

        restContractMetadataMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contractMetadataDTO))
            )
            .andExpect(status().isBadRequest());

        List<ContractMetadata> contractMetadataList = contractMetadataRepository.findAll();
        assertThat(contractMetadataList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStartDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = contractMetadataRepository.findAll().size();
        // set the field null
        contractMetadata.setStartDate(null);

        // Create the ContractMetadata, which fails.
        ContractMetadataDTO contractMetadataDTO = contractMetadataMapper.toDto(contractMetadata);

        restContractMetadataMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contractMetadataDTO))
            )
            .andExpect(status().isBadRequest());

        List<ContractMetadata> contractMetadataList = contractMetadataRepository.findAll();
        assertThat(contractMetadataList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTerminationDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = contractMetadataRepository.findAll().size();
        // set the field null
        contractMetadata.setTerminationDate(null);

        // Create the ContractMetadata, which fails.
        ContractMetadataDTO contractMetadataDTO = contractMetadataMapper.toDto(contractMetadata);

        restContractMetadataMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contractMetadataDTO))
            )
            .andExpect(status().isBadRequest());

        List<ContractMetadata> contractMetadataList = contractMetadataRepository.findAll();
        assertThat(contractMetadataList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkContractTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = contractMetadataRepository.findAll().size();
        // set the field null
        contractMetadata.setContractTitle(null);

        // Create the ContractMetadata, which fails.
        ContractMetadataDTO contractMetadataDTO = contractMetadataMapper.toDto(contractMetadata);

        restContractMetadataMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contractMetadataDTO))
            )
            .andExpect(status().isBadRequest());

        List<ContractMetadata> contractMetadataList = contractMetadataRepository.findAll();
        assertThat(contractMetadataList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkContractIdentifierIsRequired() throws Exception {
        int databaseSizeBeforeTest = contractMetadataRepository.findAll().size();
        // set the field null
        contractMetadata.setContractIdentifier(null);

        // Create the ContractMetadata, which fails.
        ContractMetadataDTO contractMetadataDTO = contractMetadataMapper.toDto(contractMetadata);

        restContractMetadataMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contractMetadataDTO))
            )
            .andExpect(status().isBadRequest());

        List<ContractMetadata> contractMetadataList = contractMetadataRepository.findAll();
        assertThat(contractMetadataList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkContractIdentifierShortIsRequired() throws Exception {
        int databaseSizeBeforeTest = contractMetadataRepository.findAll().size();
        // set the field null
        contractMetadata.setContractIdentifierShort(null);

        // Create the ContractMetadata, which fails.
        ContractMetadataDTO contractMetadataDTO = contractMetadataMapper.toDto(contractMetadata);

        restContractMetadataMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contractMetadataDTO))
            )
            .andExpect(status().isBadRequest());

        List<ContractMetadata> contractMetadataList = contractMetadataRepository.findAll();
        assertThat(contractMetadataList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllContractMetadata() throws Exception {
        // Initialize the database
        contractMetadataRepository.saveAndFlush(contractMetadata);

        // Get all the contractMetadataList
        restContractMetadataMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contractMetadata.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].typeOfContract").value(hasItem(DEFAULT_TYPE_OF_CONTRACT.toString())))
            .andExpect(jsonPath("$.[*].contractStatus").value(hasItem(DEFAULT_CONTRACT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].terminationDate").value(hasItem(DEFAULT_TERMINATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].commentsAndAttachment").value(hasItem(DEFAULT_COMMENTS_AND_ATTACHMENT.toString())))
            .andExpect(jsonPath("$.[*].contractTitle").value(hasItem(DEFAULT_CONTRACT_TITLE)))
            .andExpect(jsonPath("$.[*].contractIdentifier").value(hasItem(DEFAULT_CONTRACT_IDENTIFIER.toString())))
            .andExpect(jsonPath("$.[*].contractIdentifierShort").value(hasItem(DEFAULT_CONTRACT_IDENTIFIER_SHORT)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllContractMetadataWithEagerRelationshipsIsEnabled() throws Exception {
        when(contractMetadataServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restContractMetadataMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(contractMetadataServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllContractMetadataWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(contractMetadataServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restContractMetadataMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(contractMetadataServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getContractMetadata() throws Exception {
        // Initialize the database
        contractMetadataRepository.saveAndFlush(contractMetadata);

        // Get the contractMetadata
        restContractMetadataMockMvc
            .perform(get(ENTITY_API_URL_ID, contractMetadata.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(contractMetadata.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.typeOfContract").value(DEFAULT_TYPE_OF_CONTRACT.toString()))
            .andExpect(jsonPath("$.contractStatus").value(DEFAULT_CONTRACT_STATUS.toString()))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.terminationDate").value(DEFAULT_TERMINATION_DATE.toString()))
            .andExpect(jsonPath("$.commentsAndAttachment").value(DEFAULT_COMMENTS_AND_ATTACHMENT.toString()))
            .andExpect(jsonPath("$.contractTitle").value(DEFAULT_CONTRACT_TITLE))
            .andExpect(jsonPath("$.contractIdentifier").value(DEFAULT_CONTRACT_IDENTIFIER.toString()))
            .andExpect(jsonPath("$.contractIdentifierShort").value(DEFAULT_CONTRACT_IDENTIFIER_SHORT));
    }

    @Test
    @Transactional
    void getContractMetadataByIdFiltering() throws Exception {
        // Initialize the database
        contractMetadataRepository.saveAndFlush(contractMetadata);

        Long id = contractMetadata.getId();

        defaultContractMetadataShouldBeFound("id.equals=" + id);
        defaultContractMetadataShouldNotBeFound("id.notEquals=" + id);

        defaultContractMetadataShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultContractMetadataShouldNotBeFound("id.greaterThan=" + id);

        defaultContractMetadataShouldBeFound("id.lessThanOrEqual=" + id);
        defaultContractMetadataShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllContractMetadataByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        contractMetadataRepository.saveAndFlush(contractMetadata);

        // Get all the contractMetadataList where description equals to DEFAULT_DESCRIPTION
        defaultContractMetadataShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the contractMetadataList where description equals to UPDATED_DESCRIPTION
        defaultContractMetadataShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllContractMetadataByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        contractMetadataRepository.saveAndFlush(contractMetadata);

        // Get all the contractMetadataList where description not equals to DEFAULT_DESCRIPTION
        defaultContractMetadataShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the contractMetadataList where description not equals to UPDATED_DESCRIPTION
        defaultContractMetadataShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllContractMetadataByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        contractMetadataRepository.saveAndFlush(contractMetadata);

        // Get all the contractMetadataList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultContractMetadataShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the contractMetadataList where description equals to UPDATED_DESCRIPTION
        defaultContractMetadataShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllContractMetadataByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        contractMetadataRepository.saveAndFlush(contractMetadata);

        // Get all the contractMetadataList where description is not null
        defaultContractMetadataShouldBeFound("description.specified=true");

        // Get all the contractMetadataList where description is null
        defaultContractMetadataShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllContractMetadataByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        contractMetadataRepository.saveAndFlush(contractMetadata);

        // Get all the contractMetadataList where description contains DEFAULT_DESCRIPTION
        defaultContractMetadataShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the contractMetadataList where description contains UPDATED_DESCRIPTION
        defaultContractMetadataShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllContractMetadataByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        contractMetadataRepository.saveAndFlush(contractMetadata);

        // Get all the contractMetadataList where description does not contain DEFAULT_DESCRIPTION
        defaultContractMetadataShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the contractMetadataList where description does not contain UPDATED_DESCRIPTION
        defaultContractMetadataShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllContractMetadataByTypeOfContractIsEqualToSomething() throws Exception {
        // Initialize the database
        contractMetadataRepository.saveAndFlush(contractMetadata);

        // Get all the contractMetadataList where typeOfContract equals to DEFAULT_TYPE_OF_CONTRACT
        defaultContractMetadataShouldBeFound("typeOfContract.equals=" + DEFAULT_TYPE_OF_CONTRACT);

        // Get all the contractMetadataList where typeOfContract equals to UPDATED_TYPE_OF_CONTRACT
        defaultContractMetadataShouldNotBeFound("typeOfContract.equals=" + UPDATED_TYPE_OF_CONTRACT);
    }

    @Test
    @Transactional
    void getAllContractMetadataByTypeOfContractIsNotEqualToSomething() throws Exception {
        // Initialize the database
        contractMetadataRepository.saveAndFlush(contractMetadata);

        // Get all the contractMetadataList where typeOfContract not equals to DEFAULT_TYPE_OF_CONTRACT
        defaultContractMetadataShouldNotBeFound("typeOfContract.notEquals=" + DEFAULT_TYPE_OF_CONTRACT);

        // Get all the contractMetadataList where typeOfContract not equals to UPDATED_TYPE_OF_CONTRACT
        defaultContractMetadataShouldBeFound("typeOfContract.notEquals=" + UPDATED_TYPE_OF_CONTRACT);
    }

    @Test
    @Transactional
    void getAllContractMetadataByTypeOfContractIsInShouldWork() throws Exception {
        // Initialize the database
        contractMetadataRepository.saveAndFlush(contractMetadata);

        // Get all the contractMetadataList where typeOfContract in DEFAULT_TYPE_OF_CONTRACT or UPDATED_TYPE_OF_CONTRACT
        defaultContractMetadataShouldBeFound("typeOfContract.in=" + DEFAULT_TYPE_OF_CONTRACT + "," + UPDATED_TYPE_OF_CONTRACT);

        // Get all the contractMetadataList where typeOfContract equals to UPDATED_TYPE_OF_CONTRACT
        defaultContractMetadataShouldNotBeFound("typeOfContract.in=" + UPDATED_TYPE_OF_CONTRACT);
    }

    @Test
    @Transactional
    void getAllContractMetadataByTypeOfContractIsNullOrNotNull() throws Exception {
        // Initialize the database
        contractMetadataRepository.saveAndFlush(contractMetadata);

        // Get all the contractMetadataList where typeOfContract is not null
        defaultContractMetadataShouldBeFound("typeOfContract.specified=true");

        // Get all the contractMetadataList where typeOfContract is null
        defaultContractMetadataShouldNotBeFound("typeOfContract.specified=false");
    }

    @Test
    @Transactional
    void getAllContractMetadataByContractStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        contractMetadataRepository.saveAndFlush(contractMetadata);

        // Get all the contractMetadataList where contractStatus equals to DEFAULT_CONTRACT_STATUS
        defaultContractMetadataShouldBeFound("contractStatus.equals=" + DEFAULT_CONTRACT_STATUS);

        // Get all the contractMetadataList where contractStatus equals to UPDATED_CONTRACT_STATUS
        defaultContractMetadataShouldNotBeFound("contractStatus.equals=" + UPDATED_CONTRACT_STATUS);
    }

    @Test
    @Transactional
    void getAllContractMetadataByContractStatusIsNotEqualToSomething() throws Exception {
        // Initialize the database
        contractMetadataRepository.saveAndFlush(contractMetadata);

        // Get all the contractMetadataList where contractStatus not equals to DEFAULT_CONTRACT_STATUS
        defaultContractMetadataShouldNotBeFound("contractStatus.notEquals=" + DEFAULT_CONTRACT_STATUS);

        // Get all the contractMetadataList where contractStatus not equals to UPDATED_CONTRACT_STATUS
        defaultContractMetadataShouldBeFound("contractStatus.notEquals=" + UPDATED_CONTRACT_STATUS);
    }

    @Test
    @Transactional
    void getAllContractMetadataByContractStatusIsInShouldWork() throws Exception {
        // Initialize the database
        contractMetadataRepository.saveAndFlush(contractMetadata);

        // Get all the contractMetadataList where contractStatus in DEFAULT_CONTRACT_STATUS or UPDATED_CONTRACT_STATUS
        defaultContractMetadataShouldBeFound("contractStatus.in=" + DEFAULT_CONTRACT_STATUS + "," + UPDATED_CONTRACT_STATUS);

        // Get all the contractMetadataList where contractStatus equals to UPDATED_CONTRACT_STATUS
        defaultContractMetadataShouldNotBeFound("contractStatus.in=" + UPDATED_CONTRACT_STATUS);
    }

    @Test
    @Transactional
    void getAllContractMetadataByContractStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        contractMetadataRepository.saveAndFlush(contractMetadata);

        // Get all the contractMetadataList where contractStatus is not null
        defaultContractMetadataShouldBeFound("contractStatus.specified=true");

        // Get all the contractMetadataList where contractStatus is null
        defaultContractMetadataShouldNotBeFound("contractStatus.specified=false");
    }

    @Test
    @Transactional
    void getAllContractMetadataByStartDateIsEqualToSomething() throws Exception {
        // Initialize the database
        contractMetadataRepository.saveAndFlush(contractMetadata);

        // Get all the contractMetadataList where startDate equals to DEFAULT_START_DATE
        defaultContractMetadataShouldBeFound("startDate.equals=" + DEFAULT_START_DATE);

        // Get all the contractMetadataList where startDate equals to UPDATED_START_DATE
        defaultContractMetadataShouldNotBeFound("startDate.equals=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllContractMetadataByStartDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        contractMetadataRepository.saveAndFlush(contractMetadata);

        // Get all the contractMetadataList where startDate not equals to DEFAULT_START_DATE
        defaultContractMetadataShouldNotBeFound("startDate.notEquals=" + DEFAULT_START_DATE);

        // Get all the contractMetadataList where startDate not equals to UPDATED_START_DATE
        defaultContractMetadataShouldBeFound("startDate.notEquals=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllContractMetadataByStartDateIsInShouldWork() throws Exception {
        // Initialize the database
        contractMetadataRepository.saveAndFlush(contractMetadata);

        // Get all the contractMetadataList where startDate in DEFAULT_START_DATE or UPDATED_START_DATE
        defaultContractMetadataShouldBeFound("startDate.in=" + DEFAULT_START_DATE + "," + UPDATED_START_DATE);

        // Get all the contractMetadataList where startDate equals to UPDATED_START_DATE
        defaultContractMetadataShouldNotBeFound("startDate.in=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllContractMetadataByStartDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        contractMetadataRepository.saveAndFlush(contractMetadata);

        // Get all the contractMetadataList where startDate is not null
        defaultContractMetadataShouldBeFound("startDate.specified=true");

        // Get all the contractMetadataList where startDate is null
        defaultContractMetadataShouldNotBeFound("startDate.specified=false");
    }

    @Test
    @Transactional
    void getAllContractMetadataByStartDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        contractMetadataRepository.saveAndFlush(contractMetadata);

        // Get all the contractMetadataList where startDate is greater than or equal to DEFAULT_START_DATE
        defaultContractMetadataShouldBeFound("startDate.greaterThanOrEqual=" + DEFAULT_START_DATE);

        // Get all the contractMetadataList where startDate is greater than or equal to UPDATED_START_DATE
        defaultContractMetadataShouldNotBeFound("startDate.greaterThanOrEqual=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllContractMetadataByStartDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        contractMetadataRepository.saveAndFlush(contractMetadata);

        // Get all the contractMetadataList where startDate is less than or equal to DEFAULT_START_DATE
        defaultContractMetadataShouldBeFound("startDate.lessThanOrEqual=" + DEFAULT_START_DATE);

        // Get all the contractMetadataList where startDate is less than or equal to SMALLER_START_DATE
        defaultContractMetadataShouldNotBeFound("startDate.lessThanOrEqual=" + SMALLER_START_DATE);
    }

    @Test
    @Transactional
    void getAllContractMetadataByStartDateIsLessThanSomething() throws Exception {
        // Initialize the database
        contractMetadataRepository.saveAndFlush(contractMetadata);

        // Get all the contractMetadataList where startDate is less than DEFAULT_START_DATE
        defaultContractMetadataShouldNotBeFound("startDate.lessThan=" + DEFAULT_START_DATE);

        // Get all the contractMetadataList where startDate is less than UPDATED_START_DATE
        defaultContractMetadataShouldBeFound("startDate.lessThan=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllContractMetadataByStartDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        contractMetadataRepository.saveAndFlush(contractMetadata);

        // Get all the contractMetadataList where startDate is greater than DEFAULT_START_DATE
        defaultContractMetadataShouldNotBeFound("startDate.greaterThan=" + DEFAULT_START_DATE);

        // Get all the contractMetadataList where startDate is greater than SMALLER_START_DATE
        defaultContractMetadataShouldBeFound("startDate.greaterThan=" + SMALLER_START_DATE);
    }

    @Test
    @Transactional
    void getAllContractMetadataByTerminationDateIsEqualToSomething() throws Exception {
        // Initialize the database
        contractMetadataRepository.saveAndFlush(contractMetadata);

        // Get all the contractMetadataList where terminationDate equals to DEFAULT_TERMINATION_DATE
        defaultContractMetadataShouldBeFound("terminationDate.equals=" + DEFAULT_TERMINATION_DATE);

        // Get all the contractMetadataList where terminationDate equals to UPDATED_TERMINATION_DATE
        defaultContractMetadataShouldNotBeFound("terminationDate.equals=" + UPDATED_TERMINATION_DATE);
    }

    @Test
    @Transactional
    void getAllContractMetadataByTerminationDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        contractMetadataRepository.saveAndFlush(contractMetadata);

        // Get all the contractMetadataList where terminationDate not equals to DEFAULT_TERMINATION_DATE
        defaultContractMetadataShouldNotBeFound("terminationDate.notEquals=" + DEFAULT_TERMINATION_DATE);

        // Get all the contractMetadataList where terminationDate not equals to UPDATED_TERMINATION_DATE
        defaultContractMetadataShouldBeFound("terminationDate.notEquals=" + UPDATED_TERMINATION_DATE);
    }

    @Test
    @Transactional
    void getAllContractMetadataByTerminationDateIsInShouldWork() throws Exception {
        // Initialize the database
        contractMetadataRepository.saveAndFlush(contractMetadata);

        // Get all the contractMetadataList where terminationDate in DEFAULT_TERMINATION_DATE or UPDATED_TERMINATION_DATE
        defaultContractMetadataShouldBeFound("terminationDate.in=" + DEFAULT_TERMINATION_DATE + "," + UPDATED_TERMINATION_DATE);

        // Get all the contractMetadataList where terminationDate equals to UPDATED_TERMINATION_DATE
        defaultContractMetadataShouldNotBeFound("terminationDate.in=" + UPDATED_TERMINATION_DATE);
    }

    @Test
    @Transactional
    void getAllContractMetadataByTerminationDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        contractMetadataRepository.saveAndFlush(contractMetadata);

        // Get all the contractMetadataList where terminationDate is not null
        defaultContractMetadataShouldBeFound("terminationDate.specified=true");

        // Get all the contractMetadataList where terminationDate is null
        defaultContractMetadataShouldNotBeFound("terminationDate.specified=false");
    }

    @Test
    @Transactional
    void getAllContractMetadataByTerminationDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        contractMetadataRepository.saveAndFlush(contractMetadata);

        // Get all the contractMetadataList where terminationDate is greater than or equal to DEFAULT_TERMINATION_DATE
        defaultContractMetadataShouldBeFound("terminationDate.greaterThanOrEqual=" + DEFAULT_TERMINATION_DATE);

        // Get all the contractMetadataList where terminationDate is greater than or equal to UPDATED_TERMINATION_DATE
        defaultContractMetadataShouldNotBeFound("terminationDate.greaterThanOrEqual=" + UPDATED_TERMINATION_DATE);
    }

    @Test
    @Transactional
    void getAllContractMetadataByTerminationDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        contractMetadataRepository.saveAndFlush(contractMetadata);

        // Get all the contractMetadataList where terminationDate is less than or equal to DEFAULT_TERMINATION_DATE
        defaultContractMetadataShouldBeFound("terminationDate.lessThanOrEqual=" + DEFAULT_TERMINATION_DATE);

        // Get all the contractMetadataList where terminationDate is less than or equal to SMALLER_TERMINATION_DATE
        defaultContractMetadataShouldNotBeFound("terminationDate.lessThanOrEqual=" + SMALLER_TERMINATION_DATE);
    }

    @Test
    @Transactional
    void getAllContractMetadataByTerminationDateIsLessThanSomething() throws Exception {
        // Initialize the database
        contractMetadataRepository.saveAndFlush(contractMetadata);

        // Get all the contractMetadataList where terminationDate is less than DEFAULT_TERMINATION_DATE
        defaultContractMetadataShouldNotBeFound("terminationDate.lessThan=" + DEFAULT_TERMINATION_DATE);

        // Get all the contractMetadataList where terminationDate is less than UPDATED_TERMINATION_DATE
        defaultContractMetadataShouldBeFound("terminationDate.lessThan=" + UPDATED_TERMINATION_DATE);
    }

    @Test
    @Transactional
    void getAllContractMetadataByTerminationDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        contractMetadataRepository.saveAndFlush(contractMetadata);

        // Get all the contractMetadataList where terminationDate is greater than DEFAULT_TERMINATION_DATE
        defaultContractMetadataShouldNotBeFound("terminationDate.greaterThan=" + DEFAULT_TERMINATION_DATE);

        // Get all the contractMetadataList where terminationDate is greater than SMALLER_TERMINATION_DATE
        defaultContractMetadataShouldBeFound("terminationDate.greaterThan=" + SMALLER_TERMINATION_DATE);
    }

    @Test
    @Transactional
    void getAllContractMetadataByContractTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        contractMetadataRepository.saveAndFlush(contractMetadata);

        // Get all the contractMetadataList where contractTitle equals to DEFAULT_CONTRACT_TITLE
        defaultContractMetadataShouldBeFound("contractTitle.equals=" + DEFAULT_CONTRACT_TITLE);

        // Get all the contractMetadataList where contractTitle equals to UPDATED_CONTRACT_TITLE
        defaultContractMetadataShouldNotBeFound("contractTitle.equals=" + UPDATED_CONTRACT_TITLE);
    }

    @Test
    @Transactional
    void getAllContractMetadataByContractTitleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        contractMetadataRepository.saveAndFlush(contractMetadata);

        // Get all the contractMetadataList where contractTitle not equals to DEFAULT_CONTRACT_TITLE
        defaultContractMetadataShouldNotBeFound("contractTitle.notEquals=" + DEFAULT_CONTRACT_TITLE);

        // Get all the contractMetadataList where contractTitle not equals to UPDATED_CONTRACT_TITLE
        defaultContractMetadataShouldBeFound("contractTitle.notEquals=" + UPDATED_CONTRACT_TITLE);
    }

    @Test
    @Transactional
    void getAllContractMetadataByContractTitleIsInShouldWork() throws Exception {
        // Initialize the database
        contractMetadataRepository.saveAndFlush(contractMetadata);

        // Get all the contractMetadataList where contractTitle in DEFAULT_CONTRACT_TITLE or UPDATED_CONTRACT_TITLE
        defaultContractMetadataShouldBeFound("contractTitle.in=" + DEFAULT_CONTRACT_TITLE + "," + UPDATED_CONTRACT_TITLE);

        // Get all the contractMetadataList where contractTitle equals to UPDATED_CONTRACT_TITLE
        defaultContractMetadataShouldNotBeFound("contractTitle.in=" + UPDATED_CONTRACT_TITLE);
    }

    @Test
    @Transactional
    void getAllContractMetadataByContractTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        contractMetadataRepository.saveAndFlush(contractMetadata);

        // Get all the contractMetadataList where contractTitle is not null
        defaultContractMetadataShouldBeFound("contractTitle.specified=true");

        // Get all the contractMetadataList where contractTitle is null
        defaultContractMetadataShouldNotBeFound("contractTitle.specified=false");
    }

    @Test
    @Transactional
    void getAllContractMetadataByContractTitleContainsSomething() throws Exception {
        // Initialize the database
        contractMetadataRepository.saveAndFlush(contractMetadata);

        // Get all the contractMetadataList where contractTitle contains DEFAULT_CONTRACT_TITLE
        defaultContractMetadataShouldBeFound("contractTitle.contains=" + DEFAULT_CONTRACT_TITLE);

        // Get all the contractMetadataList where contractTitle contains UPDATED_CONTRACT_TITLE
        defaultContractMetadataShouldNotBeFound("contractTitle.contains=" + UPDATED_CONTRACT_TITLE);
    }

    @Test
    @Transactional
    void getAllContractMetadataByContractTitleNotContainsSomething() throws Exception {
        // Initialize the database
        contractMetadataRepository.saveAndFlush(contractMetadata);

        // Get all the contractMetadataList where contractTitle does not contain DEFAULT_CONTRACT_TITLE
        defaultContractMetadataShouldNotBeFound("contractTitle.doesNotContain=" + DEFAULT_CONTRACT_TITLE);

        // Get all the contractMetadataList where contractTitle does not contain UPDATED_CONTRACT_TITLE
        defaultContractMetadataShouldBeFound("contractTitle.doesNotContain=" + UPDATED_CONTRACT_TITLE);
    }

    @Test
    @Transactional
    void getAllContractMetadataByContractIdentifierIsEqualToSomething() throws Exception {
        // Initialize the database
        contractMetadataRepository.saveAndFlush(contractMetadata);

        // Get all the contractMetadataList where contractIdentifier equals to DEFAULT_CONTRACT_IDENTIFIER
        defaultContractMetadataShouldBeFound("contractIdentifier.equals=" + DEFAULT_CONTRACT_IDENTIFIER);

        // Get all the contractMetadataList where contractIdentifier equals to UPDATED_CONTRACT_IDENTIFIER
        defaultContractMetadataShouldNotBeFound("contractIdentifier.equals=" + UPDATED_CONTRACT_IDENTIFIER);
    }

    @Test
    @Transactional
    void getAllContractMetadataByContractIdentifierIsNotEqualToSomething() throws Exception {
        // Initialize the database
        contractMetadataRepository.saveAndFlush(contractMetadata);

        // Get all the contractMetadataList where contractIdentifier not equals to DEFAULT_CONTRACT_IDENTIFIER
        defaultContractMetadataShouldNotBeFound("contractIdentifier.notEquals=" + DEFAULT_CONTRACT_IDENTIFIER);

        // Get all the contractMetadataList where contractIdentifier not equals to UPDATED_CONTRACT_IDENTIFIER
        defaultContractMetadataShouldBeFound("contractIdentifier.notEquals=" + UPDATED_CONTRACT_IDENTIFIER);
    }

    @Test
    @Transactional
    void getAllContractMetadataByContractIdentifierIsInShouldWork() throws Exception {
        // Initialize the database
        contractMetadataRepository.saveAndFlush(contractMetadata);

        // Get all the contractMetadataList where contractIdentifier in DEFAULT_CONTRACT_IDENTIFIER or UPDATED_CONTRACT_IDENTIFIER
        defaultContractMetadataShouldBeFound("contractIdentifier.in=" + DEFAULT_CONTRACT_IDENTIFIER + "," + UPDATED_CONTRACT_IDENTIFIER);

        // Get all the contractMetadataList where contractIdentifier equals to UPDATED_CONTRACT_IDENTIFIER
        defaultContractMetadataShouldNotBeFound("contractIdentifier.in=" + UPDATED_CONTRACT_IDENTIFIER);
    }

    @Test
    @Transactional
    void getAllContractMetadataByContractIdentifierIsNullOrNotNull() throws Exception {
        // Initialize the database
        contractMetadataRepository.saveAndFlush(contractMetadata);

        // Get all the contractMetadataList where contractIdentifier is not null
        defaultContractMetadataShouldBeFound("contractIdentifier.specified=true");

        // Get all the contractMetadataList where contractIdentifier is null
        defaultContractMetadataShouldNotBeFound("contractIdentifier.specified=false");
    }

    @Test
    @Transactional
    void getAllContractMetadataByContractIdentifierShortIsEqualToSomething() throws Exception {
        // Initialize the database
        contractMetadataRepository.saveAndFlush(contractMetadata);

        // Get all the contractMetadataList where contractIdentifierShort equals to DEFAULT_CONTRACT_IDENTIFIER_SHORT
        defaultContractMetadataShouldBeFound("contractIdentifierShort.equals=" + DEFAULT_CONTRACT_IDENTIFIER_SHORT);

        // Get all the contractMetadataList where contractIdentifierShort equals to UPDATED_CONTRACT_IDENTIFIER_SHORT
        defaultContractMetadataShouldNotBeFound("contractIdentifierShort.equals=" + UPDATED_CONTRACT_IDENTIFIER_SHORT);
    }

    @Test
    @Transactional
    void getAllContractMetadataByContractIdentifierShortIsNotEqualToSomething() throws Exception {
        // Initialize the database
        contractMetadataRepository.saveAndFlush(contractMetadata);

        // Get all the contractMetadataList where contractIdentifierShort not equals to DEFAULT_CONTRACT_IDENTIFIER_SHORT
        defaultContractMetadataShouldNotBeFound("contractIdentifierShort.notEquals=" + DEFAULT_CONTRACT_IDENTIFIER_SHORT);

        // Get all the contractMetadataList where contractIdentifierShort not equals to UPDATED_CONTRACT_IDENTIFIER_SHORT
        defaultContractMetadataShouldBeFound("contractIdentifierShort.notEquals=" + UPDATED_CONTRACT_IDENTIFIER_SHORT);
    }

    @Test
    @Transactional
    void getAllContractMetadataByContractIdentifierShortIsInShouldWork() throws Exception {
        // Initialize the database
        contractMetadataRepository.saveAndFlush(contractMetadata);

        // Get all the contractMetadataList where contractIdentifierShort in DEFAULT_CONTRACT_IDENTIFIER_SHORT or UPDATED_CONTRACT_IDENTIFIER_SHORT
        defaultContractMetadataShouldBeFound(
            "contractIdentifierShort.in=" + DEFAULT_CONTRACT_IDENTIFIER_SHORT + "," + UPDATED_CONTRACT_IDENTIFIER_SHORT
        );

        // Get all the contractMetadataList where contractIdentifierShort equals to UPDATED_CONTRACT_IDENTIFIER_SHORT
        defaultContractMetadataShouldNotBeFound("contractIdentifierShort.in=" + UPDATED_CONTRACT_IDENTIFIER_SHORT);
    }

    @Test
    @Transactional
    void getAllContractMetadataByContractIdentifierShortIsNullOrNotNull() throws Exception {
        // Initialize the database
        contractMetadataRepository.saveAndFlush(contractMetadata);

        // Get all the contractMetadataList where contractIdentifierShort is not null
        defaultContractMetadataShouldBeFound("contractIdentifierShort.specified=true");

        // Get all the contractMetadataList where contractIdentifierShort is null
        defaultContractMetadataShouldNotBeFound("contractIdentifierShort.specified=false");
    }

    @Test
    @Transactional
    void getAllContractMetadataByContractIdentifierShortContainsSomething() throws Exception {
        // Initialize the database
        contractMetadataRepository.saveAndFlush(contractMetadata);

        // Get all the contractMetadataList where contractIdentifierShort contains DEFAULT_CONTRACT_IDENTIFIER_SHORT
        defaultContractMetadataShouldBeFound("contractIdentifierShort.contains=" + DEFAULT_CONTRACT_IDENTIFIER_SHORT);

        // Get all the contractMetadataList where contractIdentifierShort contains UPDATED_CONTRACT_IDENTIFIER_SHORT
        defaultContractMetadataShouldNotBeFound("contractIdentifierShort.contains=" + UPDATED_CONTRACT_IDENTIFIER_SHORT);
    }

    @Test
    @Transactional
    void getAllContractMetadataByContractIdentifierShortNotContainsSomething() throws Exception {
        // Initialize the database
        contractMetadataRepository.saveAndFlush(contractMetadata);

        // Get all the contractMetadataList where contractIdentifierShort does not contain DEFAULT_CONTRACT_IDENTIFIER_SHORT
        defaultContractMetadataShouldNotBeFound("contractIdentifierShort.doesNotContain=" + DEFAULT_CONTRACT_IDENTIFIER_SHORT);

        // Get all the contractMetadataList where contractIdentifierShort does not contain UPDATED_CONTRACT_IDENTIFIER_SHORT
        defaultContractMetadataShouldBeFound("contractIdentifierShort.doesNotContain=" + UPDATED_CONTRACT_IDENTIFIER_SHORT);
    }

    @Test
    @Transactional
    void getAllContractMetadataByRelatedContractsIsEqualToSomething() throws Exception {
        // Initialize the database
        contractMetadataRepository.saveAndFlush(contractMetadata);
        ContractMetadata relatedContracts;
        if (TestUtil.findAll(em, ContractMetadata.class).isEmpty()) {
            relatedContracts = ContractMetadataResourceIT.createEntity(em);
            em.persist(relatedContracts);
            em.flush();
        } else {
            relatedContracts = TestUtil.findAll(em, ContractMetadata.class).get(0);
        }
        em.persist(relatedContracts);
        em.flush();
        contractMetadata.addRelatedContracts(relatedContracts);
        contractMetadataRepository.saveAndFlush(contractMetadata);
        Long relatedContractsId = relatedContracts.getId();

        // Get all the contractMetadataList where relatedContracts equals to relatedContractsId
        // todo defaultContractMetadataShouldBeFound("relatedContractsId.equals=" + relatedContractsId);

        // Get all the contractMetadataList where relatedContracts equals to (relatedContractsId + 1)
        defaultContractMetadataShouldNotBeFound("relatedContractsId.equals=" + (relatedContractsId + 1));
    }

    @Test
    @Transactional
    void getAllContractMetadataByDepartmentIsEqualToSomething() throws Exception {
        // Initialize the database
        contractMetadataRepository.saveAndFlush(contractMetadata);
        Dealer department;
        if (TestUtil.findAll(em, Dealer.class).isEmpty()) {
            department = DealerResourceIT.createEntity(em);
            em.persist(department);
            em.flush();
        } else {
            department = TestUtil.findAll(em, Dealer.class).get(0);
        }
        em.persist(department);
        em.flush();
        contractMetadata.setDepartment(department);
        contractMetadataRepository.saveAndFlush(contractMetadata);
        Long departmentId = department.getId();

        // Get all the contractMetadataList where department equals to departmentId
        defaultContractMetadataShouldBeFound("departmentId.equals=" + departmentId);

        // Get all the contractMetadataList where department equals to (departmentId + 1)
        defaultContractMetadataShouldNotBeFound("departmentId.equals=" + (departmentId + 1));
    }

    @Test
    @Transactional
    void getAllContractMetadataByContractPartnerIsEqualToSomething() throws Exception {
        // Initialize the database
        contractMetadataRepository.saveAndFlush(contractMetadata);
        Dealer contractPartner;
        if (TestUtil.findAll(em, Dealer.class).isEmpty()) {
            contractPartner = DealerResourceIT.createEntity(em);
            em.persist(contractPartner);
            em.flush();
        } else {
            contractPartner = TestUtil.findAll(em, Dealer.class).get(0);
        }
        em.persist(contractPartner);
        em.flush();
        contractMetadata.setContractPartner(contractPartner);
        contractMetadataRepository.saveAndFlush(contractMetadata);
        Long contractPartnerId = contractPartner.getId();

        // Get all the contractMetadataList where contractPartner equals to contractPartnerId
        defaultContractMetadataShouldBeFound("contractPartnerId.equals=" + contractPartnerId);

        // Get all the contractMetadataList where contractPartner equals to (contractPartnerId + 1)
        defaultContractMetadataShouldNotBeFound("contractPartnerId.equals=" + (contractPartnerId + 1));
    }

    @Test
    @Transactional
    void getAllContractMetadataByResponsiblePersonIsEqualToSomething() throws Exception {
        // Initialize the database
        contractMetadataRepository.saveAndFlush(contractMetadata);
        ApplicationUser responsiblePerson;
        if (TestUtil.findAll(em, ApplicationUser.class).isEmpty()) {
            responsiblePerson = ApplicationUserResourceIT.createEntity(em);
            em.persist(responsiblePerson);
            em.flush();
        } else {
            responsiblePerson = TestUtil.findAll(em, ApplicationUser.class).get(0);
        }
        em.persist(responsiblePerson);
        em.flush();
        contractMetadata.setResponsiblePerson(responsiblePerson);
        contractMetadataRepository.saveAndFlush(contractMetadata);
        Long responsiblePersonId = responsiblePerson.getId();

        // Get all the contractMetadataList where responsiblePerson equals to responsiblePersonId
        defaultContractMetadataShouldBeFound("responsiblePersonId.equals=" + responsiblePersonId);

        // Get all the contractMetadataList where responsiblePerson equals to (responsiblePersonId + 1)
        defaultContractMetadataShouldNotBeFound("responsiblePersonId.equals=" + (responsiblePersonId + 1));
    }

    @Test
    @Transactional
    void getAllContractMetadataBySignatoryIsEqualToSomething() throws Exception {
        // Initialize the database
        contractMetadataRepository.saveAndFlush(contractMetadata);
        ApplicationUser signatory;
        if (TestUtil.findAll(em, ApplicationUser.class).isEmpty()) {
            signatory = ApplicationUserResourceIT.createEntity(em);
            em.persist(signatory);
            em.flush();
        } else {
            signatory = TestUtil.findAll(em, ApplicationUser.class).get(0);
        }
        em.persist(signatory);
        em.flush();
        contractMetadata.addSignatory(signatory);
        contractMetadataRepository.saveAndFlush(contractMetadata);
        Long signatoryId = signatory.getId();

        // Get all the contractMetadataList where signatory equals to signatoryId
        defaultContractMetadataShouldBeFound("signatoryId.equals=" + signatoryId);

        // Get all the contractMetadataList where signatory equals to (signatoryId + 1)
        defaultContractMetadataShouldNotBeFound("signatoryId.equals=" + (signatoryId + 1));
    }

    @Test
    @Transactional
    void getAllContractMetadataBySecurityClearanceIsEqualToSomething() throws Exception {
        // Initialize the database
        contractMetadataRepository.saveAndFlush(contractMetadata);
        SecurityClearance securityClearance;
        if (TestUtil.findAll(em, SecurityClearance.class).isEmpty()) {
            securityClearance = SecurityClearanceResourceIT.createEntity(em);
            em.persist(securityClearance);
            em.flush();
        } else {
            securityClearance = TestUtil.findAll(em, SecurityClearance.class).get(0);
        }
        em.persist(securityClearance);
        em.flush();
        contractMetadata.setSecurityClearance(securityClearance);
        contractMetadataRepository.saveAndFlush(contractMetadata);
        Long securityClearanceId = securityClearance.getId();

        // Get all the contractMetadataList where securityClearance equals to securityClearanceId
        defaultContractMetadataShouldBeFound("securityClearanceId.equals=" + securityClearanceId);

        // Get all the contractMetadataList where securityClearance equals to (securityClearanceId + 1)
        defaultContractMetadataShouldNotBeFound("securityClearanceId.equals=" + (securityClearanceId + 1));
    }

    @Test
    @Transactional
    void getAllContractMetadataByPlaceholderIsEqualToSomething() throws Exception {
        // Initialize the database
        contractMetadataRepository.saveAndFlush(contractMetadata);
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
        contractMetadata.addPlaceholder(placeholder);
        contractMetadataRepository.saveAndFlush(contractMetadata);
        Long placeholderId = placeholder.getId();

        // Get all the contractMetadataList where placeholder equals to placeholderId
        defaultContractMetadataShouldBeFound("placeholderId.equals=" + placeholderId);

        // Get all the contractMetadataList where placeholder equals to (placeholderId + 1)
        defaultContractMetadataShouldNotBeFound("placeholderId.equals=" + (placeholderId + 1));
    }

    @Test
    @Transactional
    void getAllContractMetadataByContractDocumentFileIsEqualToSomething() throws Exception {
        // Initialize the database
        contractMetadataRepository.saveAndFlush(contractMetadata);
        BusinessDocument contractDocumentFile;
        if (TestUtil.findAll(em, BusinessDocument.class).isEmpty()) {
            contractDocumentFile = BusinessDocumentResourceIT.createEntity(em);
            em.persist(contractDocumentFile);
            em.flush();
        } else {
            contractDocumentFile = TestUtil.findAll(em, BusinessDocument.class).get(0);
        }
        em.persist(contractDocumentFile);
        em.flush();
        contractMetadata.addContractDocumentFile(contractDocumentFile);
        contractMetadataRepository.saveAndFlush(contractMetadata);
        Long contractDocumentFileId = contractDocumentFile.getId();

        // Get all the contractMetadataList where contractDocumentFile equals to contractDocumentFileId
        defaultContractMetadataShouldBeFound("contractDocumentFileId.equals=" + contractDocumentFileId);

        // Get all the contractMetadataList where contractDocumentFile equals to (contractDocumentFileId + 1)
        defaultContractMetadataShouldNotBeFound("contractDocumentFileId.equals=" + (contractDocumentFileId + 1));
    }

    @Test
    @Transactional
    void getAllContractMetadataByContractMappingsIsEqualToSomething() throws Exception {
        // Initialize the database
        contractMetadataRepository.saveAndFlush(contractMetadata);
        UniversallyUniqueMapping contractMappings;
        if (TestUtil.findAll(em, UniversallyUniqueMapping.class).isEmpty()) {
            contractMappings = UniversallyUniqueMappingResourceIT.createEntity(em);
            em.persist(contractMappings);
            em.flush();
        } else {
            contractMappings = TestUtil.findAll(em, UniversallyUniqueMapping.class).get(0);
        }
        em.persist(contractMappings);
        em.flush();
        contractMetadata.addContractMappings(contractMappings);
        contractMetadataRepository.saveAndFlush(contractMetadata);
        Long contractMappingsId = contractMappings.getId();

        // Get all the contractMetadataList where contractMappings equals to contractMappingsId
        defaultContractMetadataShouldBeFound("contractMappingsId.equals=" + contractMappingsId);

        // Get all the contractMetadataList where contractMappings equals to (contractMappingsId + 1)
        defaultContractMetadataShouldNotBeFound("contractMappingsId.equals=" + (contractMappingsId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultContractMetadataShouldBeFound(String filter) throws Exception {
        restContractMetadataMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contractMetadata.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].typeOfContract").value(hasItem(DEFAULT_TYPE_OF_CONTRACT.toString())))
            .andExpect(jsonPath("$.[*].contractStatus").value(hasItem(DEFAULT_CONTRACT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].terminationDate").value(hasItem(DEFAULT_TERMINATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].commentsAndAttachment").value(hasItem(DEFAULT_COMMENTS_AND_ATTACHMENT.toString())))
            .andExpect(jsonPath("$.[*].contractTitle").value(hasItem(DEFAULT_CONTRACT_TITLE)))
            .andExpect(jsonPath("$.[*].contractIdentifier").value(hasItem(DEFAULT_CONTRACT_IDENTIFIER.toString())))
            .andExpect(jsonPath("$.[*].contractIdentifierShort").value(hasItem(DEFAULT_CONTRACT_IDENTIFIER_SHORT)));

        // Check, that the count call also returns 1
        restContractMetadataMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultContractMetadataShouldNotBeFound(String filter) throws Exception {
        restContractMetadataMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restContractMetadataMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingContractMetadata() throws Exception {
        // Get the contractMetadata
        restContractMetadataMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewContractMetadata() throws Exception {
        // Initialize the database
        contractMetadataRepository.saveAndFlush(contractMetadata);

        int databaseSizeBeforeUpdate = contractMetadataRepository.findAll().size();

        // Update the contractMetadata
        ContractMetadata updatedContractMetadata = contractMetadataRepository.findById(contractMetadata.getId()).get();
        // Disconnect from session so that the updates on updatedContractMetadata are not directly saved in db
        em.detach(updatedContractMetadata);
        updatedContractMetadata
            .description(UPDATED_DESCRIPTION)
            .typeOfContract(UPDATED_TYPE_OF_CONTRACT)
            .contractStatus(UPDATED_CONTRACT_STATUS)
            .startDate(UPDATED_START_DATE)
            .terminationDate(UPDATED_TERMINATION_DATE)
            .commentsAndAttachment(UPDATED_COMMENTS_AND_ATTACHMENT)
            .contractTitle(UPDATED_CONTRACT_TITLE)
            .contractIdentifier(UPDATED_CONTRACT_IDENTIFIER)
            .contractIdentifierShort(UPDATED_CONTRACT_IDENTIFIER_SHORT);
        ContractMetadataDTO contractMetadataDTO = contractMetadataMapper.toDto(updatedContractMetadata);

        restContractMetadataMockMvc
            .perform(
                put(ENTITY_API_URL_ID, contractMetadataDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contractMetadataDTO))
            )
            .andExpect(status().isOk());

        // Validate the ContractMetadata in the database
        List<ContractMetadata> contractMetadataList = contractMetadataRepository.findAll();
        assertThat(contractMetadataList).hasSize(databaseSizeBeforeUpdate);
        ContractMetadata testContractMetadata = contractMetadataList.get(contractMetadataList.size() - 1);
        assertThat(testContractMetadata.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testContractMetadata.getTypeOfContract()).isEqualTo(UPDATED_TYPE_OF_CONTRACT);
        assertThat(testContractMetadata.getContractStatus()).isEqualTo(UPDATED_CONTRACT_STATUS);
        assertThat(testContractMetadata.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testContractMetadata.getTerminationDate()).isEqualTo(UPDATED_TERMINATION_DATE);
        assertThat(testContractMetadata.getCommentsAndAttachment()).isEqualTo(UPDATED_COMMENTS_AND_ATTACHMENT);
        assertThat(testContractMetadata.getContractTitle()).isEqualTo(UPDATED_CONTRACT_TITLE);
        assertThat(testContractMetadata.getContractIdentifier()).isEqualTo(UPDATED_CONTRACT_IDENTIFIER);
        assertThat(testContractMetadata.getContractIdentifierShort()).isEqualTo(UPDATED_CONTRACT_IDENTIFIER_SHORT);

        // Validate the ContractMetadata in Elasticsearch
        verify(mockContractMetadataSearchRepository).save(testContractMetadata);
    }

    @Test
    @Transactional
    void putNonExistingContractMetadata() throws Exception {
        int databaseSizeBeforeUpdate = contractMetadataRepository.findAll().size();
        contractMetadata.setId(count.incrementAndGet());

        // Create the ContractMetadata
        ContractMetadataDTO contractMetadataDTO = contractMetadataMapper.toDto(contractMetadata);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContractMetadataMockMvc
            .perform(
                put(ENTITY_API_URL_ID, contractMetadataDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contractMetadataDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContractMetadata in the database
        List<ContractMetadata> contractMetadataList = contractMetadataRepository.findAll();
        assertThat(contractMetadataList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ContractMetadata in Elasticsearch
        verify(mockContractMetadataSearchRepository, times(0)).save(contractMetadata);
    }

    @Test
    @Transactional
    void putWithIdMismatchContractMetadata() throws Exception {
        int databaseSizeBeforeUpdate = contractMetadataRepository.findAll().size();
        contractMetadata.setId(count.incrementAndGet());

        // Create the ContractMetadata
        ContractMetadataDTO contractMetadataDTO = contractMetadataMapper.toDto(contractMetadata);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContractMetadataMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contractMetadataDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContractMetadata in the database
        List<ContractMetadata> contractMetadataList = contractMetadataRepository.findAll();
        assertThat(contractMetadataList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ContractMetadata in Elasticsearch
        verify(mockContractMetadataSearchRepository, times(0)).save(contractMetadata);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamContractMetadata() throws Exception {
        int databaseSizeBeforeUpdate = contractMetadataRepository.findAll().size();
        contractMetadata.setId(count.incrementAndGet());

        // Create the ContractMetadata
        ContractMetadataDTO contractMetadataDTO = contractMetadataMapper.toDto(contractMetadata);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContractMetadataMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contractMetadataDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ContractMetadata in the database
        List<ContractMetadata> contractMetadataList = contractMetadataRepository.findAll();
        assertThat(contractMetadataList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ContractMetadata in Elasticsearch
        verify(mockContractMetadataSearchRepository, times(0)).save(contractMetadata);
    }

    @Test
    @Transactional
    void partialUpdateContractMetadataWithPatch() throws Exception {
        // Initialize the database
        contractMetadataRepository.saveAndFlush(contractMetadata);

        int databaseSizeBeforeUpdate = contractMetadataRepository.findAll().size();

        // Update the contractMetadata using partial update
        ContractMetadata partialUpdatedContractMetadata = new ContractMetadata();
        partialUpdatedContractMetadata.setId(contractMetadata.getId());

        partialUpdatedContractMetadata.startDate(UPDATED_START_DATE).terminationDate(UPDATED_TERMINATION_DATE);

        restContractMetadataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedContractMetadata.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedContractMetadata))
            )
            .andExpect(status().isOk());

        // Validate the ContractMetadata in the database
        List<ContractMetadata> contractMetadataList = contractMetadataRepository.findAll();
        assertThat(contractMetadataList).hasSize(databaseSizeBeforeUpdate);
        ContractMetadata testContractMetadata = contractMetadataList.get(contractMetadataList.size() - 1);
        assertThat(testContractMetadata.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testContractMetadata.getTypeOfContract()).isEqualTo(DEFAULT_TYPE_OF_CONTRACT);
        assertThat(testContractMetadata.getContractStatus()).isEqualTo(DEFAULT_CONTRACT_STATUS);
        assertThat(testContractMetadata.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testContractMetadata.getTerminationDate()).isEqualTo(UPDATED_TERMINATION_DATE);
        assertThat(testContractMetadata.getCommentsAndAttachment()).isEqualTo(DEFAULT_COMMENTS_AND_ATTACHMENT);
        assertThat(testContractMetadata.getContractTitle()).isEqualTo(DEFAULT_CONTRACT_TITLE);
        assertThat(testContractMetadata.getContractIdentifier()).isEqualTo(DEFAULT_CONTRACT_IDENTIFIER);
        assertThat(testContractMetadata.getContractIdentifierShort()).isEqualTo(DEFAULT_CONTRACT_IDENTIFIER_SHORT);
    }

    @Test
    @Transactional
    void fullUpdateContractMetadataWithPatch() throws Exception {
        // Initialize the database
        contractMetadataRepository.saveAndFlush(contractMetadata);

        int databaseSizeBeforeUpdate = contractMetadataRepository.findAll().size();

        // Update the contractMetadata using partial update
        ContractMetadata partialUpdatedContractMetadata = new ContractMetadata();
        partialUpdatedContractMetadata.setId(contractMetadata.getId());

        partialUpdatedContractMetadata
            .description(UPDATED_DESCRIPTION)
            .typeOfContract(UPDATED_TYPE_OF_CONTRACT)
            .contractStatus(UPDATED_CONTRACT_STATUS)
            .startDate(UPDATED_START_DATE)
            .terminationDate(UPDATED_TERMINATION_DATE)
            .commentsAndAttachment(UPDATED_COMMENTS_AND_ATTACHMENT)
            .contractTitle(UPDATED_CONTRACT_TITLE)
            .contractIdentifier(UPDATED_CONTRACT_IDENTIFIER)
            .contractIdentifierShort(UPDATED_CONTRACT_IDENTIFIER_SHORT);

        restContractMetadataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedContractMetadata.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedContractMetadata))
            )
            .andExpect(status().isOk());

        // Validate the ContractMetadata in the database
        List<ContractMetadata> contractMetadataList = contractMetadataRepository.findAll();
        assertThat(contractMetadataList).hasSize(databaseSizeBeforeUpdate);
        ContractMetadata testContractMetadata = contractMetadataList.get(contractMetadataList.size() - 1);
        assertThat(testContractMetadata.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testContractMetadata.getTypeOfContract()).isEqualTo(UPDATED_TYPE_OF_CONTRACT);
        assertThat(testContractMetadata.getContractStatus()).isEqualTo(UPDATED_CONTRACT_STATUS);
        assertThat(testContractMetadata.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testContractMetadata.getTerminationDate()).isEqualTo(UPDATED_TERMINATION_DATE);
        assertThat(testContractMetadata.getCommentsAndAttachment()).isEqualTo(UPDATED_COMMENTS_AND_ATTACHMENT);
        assertThat(testContractMetadata.getContractTitle()).isEqualTo(UPDATED_CONTRACT_TITLE);
        assertThat(testContractMetadata.getContractIdentifier()).isEqualTo(UPDATED_CONTRACT_IDENTIFIER);
        assertThat(testContractMetadata.getContractIdentifierShort()).isEqualTo(UPDATED_CONTRACT_IDENTIFIER_SHORT);
    }

    @Test
    @Transactional
    void patchNonExistingContractMetadata() throws Exception {
        int databaseSizeBeforeUpdate = contractMetadataRepository.findAll().size();
        contractMetadata.setId(count.incrementAndGet());

        // Create the ContractMetadata
        ContractMetadataDTO contractMetadataDTO = contractMetadataMapper.toDto(contractMetadata);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContractMetadataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, contractMetadataDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(contractMetadataDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContractMetadata in the database
        List<ContractMetadata> contractMetadataList = contractMetadataRepository.findAll();
        assertThat(contractMetadataList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ContractMetadata in Elasticsearch
        verify(mockContractMetadataSearchRepository, times(0)).save(contractMetadata);
    }

    @Test
    @Transactional
    void patchWithIdMismatchContractMetadata() throws Exception {
        int databaseSizeBeforeUpdate = contractMetadataRepository.findAll().size();
        contractMetadata.setId(count.incrementAndGet());

        // Create the ContractMetadata
        ContractMetadataDTO contractMetadataDTO = contractMetadataMapper.toDto(contractMetadata);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContractMetadataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(contractMetadataDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContractMetadata in the database
        List<ContractMetadata> contractMetadataList = contractMetadataRepository.findAll();
        assertThat(contractMetadataList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ContractMetadata in Elasticsearch
        verify(mockContractMetadataSearchRepository, times(0)).save(contractMetadata);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamContractMetadata() throws Exception {
        int databaseSizeBeforeUpdate = contractMetadataRepository.findAll().size();
        contractMetadata.setId(count.incrementAndGet());

        // Create the ContractMetadata
        ContractMetadataDTO contractMetadataDTO = contractMetadataMapper.toDto(contractMetadata);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContractMetadataMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(contractMetadataDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ContractMetadata in the database
        List<ContractMetadata> contractMetadataList = contractMetadataRepository.findAll();
        assertThat(contractMetadataList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ContractMetadata in Elasticsearch
        verify(mockContractMetadataSearchRepository, times(0)).save(contractMetadata);
    }

    @Test
    @Transactional
    void deleteContractMetadata() throws Exception {
        // Initialize the database
        contractMetadataRepository.saveAndFlush(contractMetadata);

        int databaseSizeBeforeDelete = contractMetadataRepository.findAll().size();

        // Delete the contractMetadata
        restContractMetadataMockMvc
            .perform(delete(ENTITY_API_URL_ID, contractMetadata.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ContractMetadata> contractMetadataList = contractMetadataRepository.findAll();
        assertThat(contractMetadataList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the ContractMetadata in Elasticsearch
        verify(mockContractMetadataSearchRepository, times(1)).deleteById(contractMetadata.getId());
    }

    @Test
    @Transactional
    void searchContractMetadata() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        contractMetadataRepository.saveAndFlush(contractMetadata);
        when(mockContractMetadataSearchRepository.search("id:" + contractMetadata.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(contractMetadata), PageRequest.of(0, 1), 1));

        // Search the contractMetadata
        restContractMetadataMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + contractMetadata.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contractMetadata.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].typeOfContract").value(hasItem(DEFAULT_TYPE_OF_CONTRACT.toString())))
            .andExpect(jsonPath("$.[*].contractStatus").value(hasItem(DEFAULT_CONTRACT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].terminationDate").value(hasItem(DEFAULT_TERMINATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].commentsAndAttachment").value(hasItem(DEFAULT_COMMENTS_AND_ATTACHMENT.toString())))
            .andExpect(jsonPath("$.[*].contractTitle").value(hasItem(DEFAULT_CONTRACT_TITLE)))
            .andExpect(jsonPath("$.[*].contractIdentifier").value(hasItem(DEFAULT_CONTRACT_IDENTIFIER.toString())))
            .andExpect(jsonPath("$.[*].contractIdentifierShort").value(hasItem(DEFAULT_CONTRACT_IDENTIFIER_SHORT)));
    }
}
