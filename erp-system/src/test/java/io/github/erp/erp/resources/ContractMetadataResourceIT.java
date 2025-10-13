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
import io.github.erp.domain.ContractMetadata;
import io.github.erp.domain.enumeration.ContractStatus;
import io.github.erp.domain.enumeration.ContractType;
import io.github.erp.repository.ContractMetadataRepository;
import io.github.erp.repository.search.ContractMetadataSearchRepository;
import io.github.erp.service.ContractMetadataService;
import io.github.erp.service.dto.ContractMetadataDTO;
import io.github.erp.service.mapper.ContractMetadataMapper;
import io.github.erp.web.rest.ContractMetadataResource;
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

    private static final LocalDate DEFAULT_TERMINATION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_TERMINATION_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_COMMENTS_AND_ATTACHMENT = "AAAAAAAAAA";
    private static final String UPDATED_COMMENTS_AND_ATTACHMENT = "BBBBBBBBBB";

    private static final String DEFAULT_CONTRACT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_CONTRACT_TITLE = "BBBBBBBBBB";

    private static final UUID DEFAULT_CONTRACT_IDENTIFIER = UUID.randomUUID();
    private static final UUID UPDATED_CONTRACT_IDENTIFIER = UUID.randomUUID();

    private static final String DEFAULT_CONTRACT_IDENTIFIER_SHORT = "AAAAAAAAAA";
    private static final String UPDATED_CONTRACT_IDENTIFIER_SHORT = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/app/contract-metadata";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/app/_search/contract-metadata";

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
