package io.github.erp.web.rest;

/*-
 * Erp System - Mark VIII No 2 (Hilkiah Series) Server ver 1.6.1
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
import io.github.erp.domain.Algorithm;
import io.github.erp.domain.ApplicationUser;
import io.github.erp.domain.BusinessDocument;
import io.github.erp.domain.Dealer;
import io.github.erp.domain.Placeholder;
import io.github.erp.domain.SecurityClearance;
import io.github.erp.domain.UniversallyUniqueMapping;
import io.github.erp.repository.BusinessDocumentRepository;
import io.github.erp.repository.search.BusinessDocumentSearchRepository;
import io.github.erp.service.BusinessDocumentService;
import io.github.erp.service.criteria.BusinessDocumentCriteria;
import io.github.erp.service.dto.BusinessDocumentDTO;
import io.github.erp.service.mapper.BusinessDocumentMapper;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.UUID;
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
 * Integration tests for the {@link BusinessDocumentResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class BusinessDocumentResourceIT {

    private static final String DEFAULT_DOCUMENT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_DOCUMENT_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final UUID DEFAULT_DOCUMENT_SERIAL = UUID.randomUUID();
    private static final UUID UPDATED_DOCUMENT_SERIAL = UUID.randomUUID();

    private static final ZonedDateTime DEFAULT_LAST_MODIFIED = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_LAST_MODIFIED = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_LAST_MODIFIED = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final String DEFAULT_ATTACHMENT_FILE_PATH = "AAAAAAAAAA";
    private static final String UPDATED_ATTACHMENT_FILE_PATH = "BBBBBBBBBB";

    private static final String DEFAULT_DOCUMENT_FILE_CONTENT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_DOCUMENT_FILE_CONTENT_TYPE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_FILE_TAMPERED = false;
    private static final Boolean UPDATED_FILE_TAMPERED = true;

    private static final String DEFAULT_DOCUMENT_FILE_CHECKSUM = "AAAAAAAAAA";
    private static final String UPDATED_DOCUMENT_FILE_CHECKSUM = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/business-documents";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/business-documents";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private BusinessDocumentRepository businessDocumentRepository;

    @Mock
    private BusinessDocumentRepository businessDocumentRepositoryMock;

    @Autowired
    private BusinessDocumentMapper businessDocumentMapper;

    @Mock
    private BusinessDocumentService businessDocumentServiceMock;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.BusinessDocumentSearchRepositoryMockConfiguration
     */
    @Autowired
    private BusinessDocumentSearchRepository mockBusinessDocumentSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBusinessDocumentMockMvc;

    private BusinessDocument businessDocument;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BusinessDocument createEntity(EntityManager em) {
        BusinessDocument businessDocument = new BusinessDocument()
            .documentTitle(DEFAULT_DOCUMENT_TITLE)
            .description(DEFAULT_DESCRIPTION)
            .documentSerial(DEFAULT_DOCUMENT_SERIAL)
            .lastModified(DEFAULT_LAST_MODIFIED)
            .attachmentFilePath(DEFAULT_ATTACHMENT_FILE_PATH)
            .documentFileContentType(DEFAULT_DOCUMENT_FILE_CONTENT_TYPE)
            .fileTampered(DEFAULT_FILE_TAMPERED)
            .documentFileChecksum(DEFAULT_DOCUMENT_FILE_CHECKSUM);
        // Add required entity
        ApplicationUser applicationUser;
        if (TestUtil.findAll(em, ApplicationUser.class).isEmpty()) {
            applicationUser = ApplicationUserResourceIT.createEntity(em);
            em.persist(applicationUser);
            em.flush();
        } else {
            applicationUser = TestUtil.findAll(em, ApplicationUser.class).get(0);
        }
        businessDocument.setCreatedBy(applicationUser);
        // Add required entity
        Dealer dealer;
        if (TestUtil.findAll(em, Dealer.class).isEmpty()) {
            dealer = DealerResourceIT.createEntity(em);
            em.persist(dealer);
            em.flush();
        } else {
            dealer = TestUtil.findAll(em, Dealer.class).get(0);
        }
        businessDocument.setOriginatingDepartment(dealer);
        // Add required entity
        Algorithm algorithm;
        if (TestUtil.findAll(em, Algorithm.class).isEmpty()) {
            algorithm = AlgorithmResourceIT.createEntity(em);
            em.persist(algorithm);
            em.flush();
        } else {
            algorithm = TestUtil.findAll(em, Algorithm.class).get(0);
        }
        businessDocument.setFileChecksumAlgorithm(algorithm);
        // Add required entity
        SecurityClearance securityClearance;
        if (TestUtil.findAll(em, SecurityClearance.class).isEmpty()) {
            securityClearance = SecurityClearanceResourceIT.createEntity(em);
            em.persist(securityClearance);
            em.flush();
        } else {
            securityClearance = TestUtil.findAll(em, SecurityClearance.class).get(0);
        }
        businessDocument.setSecurityClearance(securityClearance);
        return businessDocument;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BusinessDocument createUpdatedEntity(EntityManager em) {
        BusinessDocument businessDocument = new BusinessDocument()
            .documentTitle(UPDATED_DOCUMENT_TITLE)
            .description(UPDATED_DESCRIPTION)
            .documentSerial(UPDATED_DOCUMENT_SERIAL)
            .lastModified(UPDATED_LAST_MODIFIED)
            .attachmentFilePath(UPDATED_ATTACHMENT_FILE_PATH)
            .documentFileContentType(UPDATED_DOCUMENT_FILE_CONTENT_TYPE)
            .fileTampered(UPDATED_FILE_TAMPERED)
            .documentFileChecksum(UPDATED_DOCUMENT_FILE_CHECKSUM);
        // Add required entity
        ApplicationUser applicationUser;
        if (TestUtil.findAll(em, ApplicationUser.class).isEmpty()) {
            applicationUser = ApplicationUserResourceIT.createUpdatedEntity(em);
            em.persist(applicationUser);
            em.flush();
        } else {
            applicationUser = TestUtil.findAll(em, ApplicationUser.class).get(0);
        }
        businessDocument.setCreatedBy(applicationUser);
        // Add required entity
        Dealer dealer;
        if (TestUtil.findAll(em, Dealer.class).isEmpty()) {
            dealer = DealerResourceIT.createUpdatedEntity(em);
            em.persist(dealer);
            em.flush();
        } else {
            dealer = TestUtil.findAll(em, Dealer.class).get(0);
        }
        businessDocument.setOriginatingDepartment(dealer);
        // Add required entity
        Algorithm algorithm;
        if (TestUtil.findAll(em, Algorithm.class).isEmpty()) {
            algorithm = AlgorithmResourceIT.createUpdatedEntity(em);
            em.persist(algorithm);
            em.flush();
        } else {
            algorithm = TestUtil.findAll(em, Algorithm.class).get(0);
        }
        businessDocument.setFileChecksumAlgorithm(algorithm);
        // Add required entity
        SecurityClearance securityClearance;
        if (TestUtil.findAll(em, SecurityClearance.class).isEmpty()) {
            securityClearance = SecurityClearanceResourceIT.createUpdatedEntity(em);
            em.persist(securityClearance);
            em.flush();
        } else {
            securityClearance = TestUtil.findAll(em, SecurityClearance.class).get(0);
        }
        businessDocument.setSecurityClearance(securityClearance);
        return businessDocument;
    }

    @BeforeEach
    public void initTest() {
        businessDocument = createEntity(em);
    }

    @Test
    @Transactional
    void createBusinessDocument() throws Exception {
        int databaseSizeBeforeCreate = businessDocumentRepository.findAll().size();
        // Create the BusinessDocument
        BusinessDocumentDTO businessDocumentDTO = businessDocumentMapper.toDto(businessDocument);
        restBusinessDocumentMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(businessDocumentDTO))
            )
            .andExpect(status().isCreated());

        // Validate the BusinessDocument in the database
        List<BusinessDocument> businessDocumentList = businessDocumentRepository.findAll();
        assertThat(businessDocumentList).hasSize(databaseSizeBeforeCreate + 1);
        BusinessDocument testBusinessDocument = businessDocumentList.get(businessDocumentList.size() - 1);
        assertThat(testBusinessDocument.getDocumentTitle()).isEqualTo(DEFAULT_DOCUMENT_TITLE);
        assertThat(testBusinessDocument.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testBusinessDocument.getDocumentSerial()).isEqualTo(DEFAULT_DOCUMENT_SERIAL);
        assertThat(testBusinessDocument.getLastModified()).isEqualTo(DEFAULT_LAST_MODIFIED);
        assertThat(testBusinessDocument.getAttachmentFilePath()).isEqualTo(DEFAULT_ATTACHMENT_FILE_PATH);
        assertThat(testBusinessDocument.getDocumentFileContentType()).isEqualTo(DEFAULT_DOCUMENT_FILE_CONTENT_TYPE);
        assertThat(testBusinessDocument.getFileTampered()).isEqualTo(DEFAULT_FILE_TAMPERED);
        assertThat(testBusinessDocument.getDocumentFileChecksum()).isEqualTo(DEFAULT_DOCUMENT_FILE_CHECKSUM);

        // Validate the BusinessDocument in Elasticsearch
        verify(mockBusinessDocumentSearchRepository, times(1)).save(testBusinessDocument);
    }

    @Test
    @Transactional
    void createBusinessDocumentWithExistingId() throws Exception {
        // Create the BusinessDocument with an existing ID
        businessDocument.setId(1L);
        BusinessDocumentDTO businessDocumentDTO = businessDocumentMapper.toDto(businessDocument);

        int databaseSizeBeforeCreate = businessDocumentRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBusinessDocumentMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(businessDocumentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BusinessDocument in the database
        List<BusinessDocument> businessDocumentList = businessDocumentRepository.findAll();
        assertThat(businessDocumentList).hasSize(databaseSizeBeforeCreate);

        // Validate the BusinessDocument in Elasticsearch
        verify(mockBusinessDocumentSearchRepository, times(0)).save(businessDocument);
    }

    @Test
    @Transactional
    void checkDocumentTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = businessDocumentRepository.findAll().size();
        // set the field null
        businessDocument.setDocumentTitle(null);

        // Create the BusinessDocument, which fails.
        BusinessDocumentDTO businessDocumentDTO = businessDocumentMapper.toDto(businessDocument);

        restBusinessDocumentMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(businessDocumentDTO))
            )
            .andExpect(status().isBadRequest());

        List<BusinessDocument> businessDocumentList = businessDocumentRepository.findAll();
        assertThat(businessDocumentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDocumentSerialIsRequired() throws Exception {
        int databaseSizeBeforeTest = businessDocumentRepository.findAll().size();
        // set the field null
        businessDocument.setDocumentSerial(null);

        // Create the BusinessDocument, which fails.
        BusinessDocumentDTO businessDocumentDTO = businessDocumentMapper.toDto(businessDocument);

        restBusinessDocumentMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(businessDocumentDTO))
            )
            .andExpect(status().isBadRequest());

        List<BusinessDocument> businessDocumentList = businessDocumentRepository.findAll();
        assertThat(businessDocumentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAttachmentFilePathIsRequired() throws Exception {
        int databaseSizeBeforeTest = businessDocumentRepository.findAll().size();
        // set the field null
        businessDocument.setAttachmentFilePath(null);

        // Create the BusinessDocument, which fails.
        BusinessDocumentDTO businessDocumentDTO = businessDocumentMapper.toDto(businessDocument);

        restBusinessDocumentMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(businessDocumentDTO))
            )
            .andExpect(status().isBadRequest());

        List<BusinessDocument> businessDocumentList = businessDocumentRepository.findAll();
        assertThat(businessDocumentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDocumentFileContentTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = businessDocumentRepository.findAll().size();
        // set the field null
        businessDocument.setDocumentFileContentType(null);

        // Create the BusinessDocument, which fails.
        BusinessDocumentDTO businessDocumentDTO = businessDocumentMapper.toDto(businessDocument);

        restBusinessDocumentMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(businessDocumentDTO))
            )
            .andExpect(status().isBadRequest());

        List<BusinessDocument> businessDocumentList = businessDocumentRepository.findAll();
        assertThat(businessDocumentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDocumentFileChecksumIsRequired() throws Exception {
        int databaseSizeBeforeTest = businessDocumentRepository.findAll().size();
        // set the field null
        businessDocument.setDocumentFileChecksum(null);

        // Create the BusinessDocument, which fails.
        BusinessDocumentDTO businessDocumentDTO = businessDocumentMapper.toDto(businessDocument);

        restBusinessDocumentMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(businessDocumentDTO))
            )
            .andExpect(status().isBadRequest());

        List<BusinessDocument> businessDocumentList = businessDocumentRepository.findAll();
        assertThat(businessDocumentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllBusinessDocuments() throws Exception {
        // Initialize the database
        businessDocumentRepository.saveAndFlush(businessDocument);

        // Get all the businessDocumentList
        restBusinessDocumentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(businessDocument.getId().intValue())))
            .andExpect(jsonPath("$.[*].documentTitle").value(hasItem(DEFAULT_DOCUMENT_TITLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].documentSerial").value(hasItem(DEFAULT_DOCUMENT_SERIAL.toString())))
            .andExpect(jsonPath("$.[*].lastModified").value(hasItem(sameInstant(DEFAULT_LAST_MODIFIED))))
            .andExpect(jsonPath("$.[*].attachmentFilePath").value(hasItem(DEFAULT_ATTACHMENT_FILE_PATH)))
            .andExpect(jsonPath("$.[*].documentFileContentType").value(hasItem(DEFAULT_DOCUMENT_FILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].fileTampered").value(hasItem(DEFAULT_FILE_TAMPERED.booleanValue())))
            .andExpect(jsonPath("$.[*].documentFileChecksum").value(hasItem(DEFAULT_DOCUMENT_FILE_CHECKSUM)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllBusinessDocumentsWithEagerRelationshipsIsEnabled() throws Exception {
        when(businessDocumentServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restBusinessDocumentMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(businessDocumentServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllBusinessDocumentsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(businessDocumentServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restBusinessDocumentMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(businessDocumentServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getBusinessDocument() throws Exception {
        // Initialize the database
        businessDocumentRepository.saveAndFlush(businessDocument);

        // Get the businessDocument
        restBusinessDocumentMockMvc
            .perform(get(ENTITY_API_URL_ID, businessDocument.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(businessDocument.getId().intValue()))
            .andExpect(jsonPath("$.documentTitle").value(DEFAULT_DOCUMENT_TITLE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.documentSerial").value(DEFAULT_DOCUMENT_SERIAL.toString()))
            .andExpect(jsonPath("$.lastModified").value(sameInstant(DEFAULT_LAST_MODIFIED)))
            .andExpect(jsonPath("$.attachmentFilePath").value(DEFAULT_ATTACHMENT_FILE_PATH))
            .andExpect(jsonPath("$.documentFileContentType").value(DEFAULT_DOCUMENT_FILE_CONTENT_TYPE))
            .andExpect(jsonPath("$.fileTampered").value(DEFAULT_FILE_TAMPERED.booleanValue()))
            .andExpect(jsonPath("$.documentFileChecksum").value(DEFAULT_DOCUMENT_FILE_CHECKSUM));
    }

    @Test
    @Transactional
    void getBusinessDocumentsByIdFiltering() throws Exception {
        // Initialize the database
        businessDocumentRepository.saveAndFlush(businessDocument);

        Long id = businessDocument.getId();

        defaultBusinessDocumentShouldBeFound("id.equals=" + id);
        defaultBusinessDocumentShouldNotBeFound("id.notEquals=" + id);

        defaultBusinessDocumentShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultBusinessDocumentShouldNotBeFound("id.greaterThan=" + id);

        defaultBusinessDocumentShouldBeFound("id.lessThanOrEqual=" + id);
        defaultBusinessDocumentShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllBusinessDocumentsByDocumentTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        businessDocumentRepository.saveAndFlush(businessDocument);

        // Get all the businessDocumentList where documentTitle equals to DEFAULT_DOCUMENT_TITLE
        defaultBusinessDocumentShouldBeFound("documentTitle.equals=" + DEFAULT_DOCUMENT_TITLE);

        // Get all the businessDocumentList where documentTitle equals to UPDATED_DOCUMENT_TITLE
        defaultBusinessDocumentShouldNotBeFound("documentTitle.equals=" + UPDATED_DOCUMENT_TITLE);
    }

    @Test
    @Transactional
    void getAllBusinessDocumentsByDocumentTitleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        businessDocumentRepository.saveAndFlush(businessDocument);

        // Get all the businessDocumentList where documentTitle not equals to DEFAULT_DOCUMENT_TITLE
        defaultBusinessDocumentShouldNotBeFound("documentTitle.notEquals=" + DEFAULT_DOCUMENT_TITLE);

        // Get all the businessDocumentList where documentTitle not equals to UPDATED_DOCUMENT_TITLE
        defaultBusinessDocumentShouldBeFound("documentTitle.notEquals=" + UPDATED_DOCUMENT_TITLE);
    }

    @Test
    @Transactional
    void getAllBusinessDocumentsByDocumentTitleIsInShouldWork() throws Exception {
        // Initialize the database
        businessDocumentRepository.saveAndFlush(businessDocument);

        // Get all the businessDocumentList where documentTitle in DEFAULT_DOCUMENT_TITLE or UPDATED_DOCUMENT_TITLE
        defaultBusinessDocumentShouldBeFound("documentTitle.in=" + DEFAULT_DOCUMENT_TITLE + "," + UPDATED_DOCUMENT_TITLE);

        // Get all the businessDocumentList where documentTitle equals to UPDATED_DOCUMENT_TITLE
        defaultBusinessDocumentShouldNotBeFound("documentTitle.in=" + UPDATED_DOCUMENT_TITLE);
    }

    @Test
    @Transactional
    void getAllBusinessDocumentsByDocumentTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        businessDocumentRepository.saveAndFlush(businessDocument);

        // Get all the businessDocumentList where documentTitle is not null
        defaultBusinessDocumentShouldBeFound("documentTitle.specified=true");

        // Get all the businessDocumentList where documentTitle is null
        defaultBusinessDocumentShouldNotBeFound("documentTitle.specified=false");
    }

    @Test
    @Transactional
    void getAllBusinessDocumentsByDocumentTitleContainsSomething() throws Exception {
        // Initialize the database
        businessDocumentRepository.saveAndFlush(businessDocument);

        // Get all the businessDocumentList where documentTitle contains DEFAULT_DOCUMENT_TITLE
        defaultBusinessDocumentShouldBeFound("documentTitle.contains=" + DEFAULT_DOCUMENT_TITLE);

        // Get all the businessDocumentList where documentTitle contains UPDATED_DOCUMENT_TITLE
        defaultBusinessDocumentShouldNotBeFound("documentTitle.contains=" + UPDATED_DOCUMENT_TITLE);
    }

    @Test
    @Transactional
    void getAllBusinessDocumentsByDocumentTitleNotContainsSomething() throws Exception {
        // Initialize the database
        businessDocumentRepository.saveAndFlush(businessDocument);

        // Get all the businessDocumentList where documentTitle does not contain DEFAULT_DOCUMENT_TITLE
        defaultBusinessDocumentShouldNotBeFound("documentTitle.doesNotContain=" + DEFAULT_DOCUMENT_TITLE);

        // Get all the businessDocumentList where documentTitle does not contain UPDATED_DOCUMENT_TITLE
        defaultBusinessDocumentShouldBeFound("documentTitle.doesNotContain=" + UPDATED_DOCUMENT_TITLE);
    }

    @Test
    @Transactional
    void getAllBusinessDocumentsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        businessDocumentRepository.saveAndFlush(businessDocument);

        // Get all the businessDocumentList where description equals to DEFAULT_DESCRIPTION
        defaultBusinessDocumentShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the businessDocumentList where description equals to UPDATED_DESCRIPTION
        defaultBusinessDocumentShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllBusinessDocumentsByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        businessDocumentRepository.saveAndFlush(businessDocument);

        // Get all the businessDocumentList where description not equals to DEFAULT_DESCRIPTION
        defaultBusinessDocumentShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the businessDocumentList where description not equals to UPDATED_DESCRIPTION
        defaultBusinessDocumentShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllBusinessDocumentsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        businessDocumentRepository.saveAndFlush(businessDocument);

        // Get all the businessDocumentList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultBusinessDocumentShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the businessDocumentList where description equals to UPDATED_DESCRIPTION
        defaultBusinessDocumentShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllBusinessDocumentsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        businessDocumentRepository.saveAndFlush(businessDocument);

        // Get all the businessDocumentList where description is not null
        defaultBusinessDocumentShouldBeFound("description.specified=true");

        // Get all the businessDocumentList where description is null
        defaultBusinessDocumentShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllBusinessDocumentsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        businessDocumentRepository.saveAndFlush(businessDocument);

        // Get all the businessDocumentList where description contains DEFAULT_DESCRIPTION
        defaultBusinessDocumentShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the businessDocumentList where description contains UPDATED_DESCRIPTION
        defaultBusinessDocumentShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllBusinessDocumentsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        businessDocumentRepository.saveAndFlush(businessDocument);

        // Get all the businessDocumentList where description does not contain DEFAULT_DESCRIPTION
        defaultBusinessDocumentShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the businessDocumentList where description does not contain UPDATED_DESCRIPTION
        defaultBusinessDocumentShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllBusinessDocumentsByDocumentSerialIsEqualToSomething() throws Exception {
        // Initialize the database
        businessDocumentRepository.saveAndFlush(businessDocument);

        // Get all the businessDocumentList where documentSerial equals to DEFAULT_DOCUMENT_SERIAL
        defaultBusinessDocumentShouldBeFound("documentSerial.equals=" + DEFAULT_DOCUMENT_SERIAL);

        // Get all the businessDocumentList where documentSerial equals to UPDATED_DOCUMENT_SERIAL
        defaultBusinessDocumentShouldNotBeFound("documentSerial.equals=" + UPDATED_DOCUMENT_SERIAL);
    }

    @Test
    @Transactional
    void getAllBusinessDocumentsByDocumentSerialIsNotEqualToSomething() throws Exception {
        // Initialize the database
        businessDocumentRepository.saveAndFlush(businessDocument);

        // Get all the businessDocumentList where documentSerial not equals to DEFAULT_DOCUMENT_SERIAL
        defaultBusinessDocumentShouldNotBeFound("documentSerial.notEquals=" + DEFAULT_DOCUMENT_SERIAL);

        // Get all the businessDocumentList where documentSerial not equals to UPDATED_DOCUMENT_SERIAL
        defaultBusinessDocumentShouldBeFound("documentSerial.notEquals=" + UPDATED_DOCUMENT_SERIAL);
    }

    @Test
    @Transactional
    void getAllBusinessDocumentsByDocumentSerialIsInShouldWork() throws Exception {
        // Initialize the database
        businessDocumentRepository.saveAndFlush(businessDocument);

        // Get all the businessDocumentList where documentSerial in DEFAULT_DOCUMENT_SERIAL or UPDATED_DOCUMENT_SERIAL
        defaultBusinessDocumentShouldBeFound("documentSerial.in=" + DEFAULT_DOCUMENT_SERIAL + "," + UPDATED_DOCUMENT_SERIAL);

        // Get all the businessDocumentList where documentSerial equals to UPDATED_DOCUMENT_SERIAL
        defaultBusinessDocumentShouldNotBeFound("documentSerial.in=" + UPDATED_DOCUMENT_SERIAL);
    }

    @Test
    @Transactional
    void getAllBusinessDocumentsByDocumentSerialIsNullOrNotNull() throws Exception {
        // Initialize the database
        businessDocumentRepository.saveAndFlush(businessDocument);

        // Get all the businessDocumentList where documentSerial is not null
        defaultBusinessDocumentShouldBeFound("documentSerial.specified=true");

        // Get all the businessDocumentList where documentSerial is null
        defaultBusinessDocumentShouldNotBeFound("documentSerial.specified=false");
    }

    @Test
    @Transactional
    void getAllBusinessDocumentsByLastModifiedIsEqualToSomething() throws Exception {
        // Initialize the database
        businessDocumentRepository.saveAndFlush(businessDocument);

        // Get all the businessDocumentList where lastModified equals to DEFAULT_LAST_MODIFIED
        defaultBusinessDocumentShouldBeFound("lastModified.equals=" + DEFAULT_LAST_MODIFIED);

        // Get all the businessDocumentList where lastModified equals to UPDATED_LAST_MODIFIED
        defaultBusinessDocumentShouldNotBeFound("lastModified.equals=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllBusinessDocumentsByLastModifiedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        businessDocumentRepository.saveAndFlush(businessDocument);

        // Get all the businessDocumentList where lastModified not equals to DEFAULT_LAST_MODIFIED
        defaultBusinessDocumentShouldNotBeFound("lastModified.notEquals=" + DEFAULT_LAST_MODIFIED);

        // Get all the businessDocumentList where lastModified not equals to UPDATED_LAST_MODIFIED
        defaultBusinessDocumentShouldBeFound("lastModified.notEquals=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllBusinessDocumentsByLastModifiedIsInShouldWork() throws Exception {
        // Initialize the database
        businessDocumentRepository.saveAndFlush(businessDocument);

        // Get all the businessDocumentList where lastModified in DEFAULT_LAST_MODIFIED or UPDATED_LAST_MODIFIED
        defaultBusinessDocumentShouldBeFound("lastModified.in=" + DEFAULT_LAST_MODIFIED + "," + UPDATED_LAST_MODIFIED);

        // Get all the businessDocumentList where lastModified equals to UPDATED_LAST_MODIFIED
        defaultBusinessDocumentShouldNotBeFound("lastModified.in=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllBusinessDocumentsByLastModifiedIsNullOrNotNull() throws Exception {
        // Initialize the database
        businessDocumentRepository.saveAndFlush(businessDocument);

        // Get all the businessDocumentList where lastModified is not null
        defaultBusinessDocumentShouldBeFound("lastModified.specified=true");

        // Get all the businessDocumentList where lastModified is null
        defaultBusinessDocumentShouldNotBeFound("lastModified.specified=false");
    }

    @Test
    @Transactional
    void getAllBusinessDocumentsByLastModifiedIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        businessDocumentRepository.saveAndFlush(businessDocument);

        // Get all the businessDocumentList where lastModified is greater than or equal to DEFAULT_LAST_MODIFIED
        defaultBusinessDocumentShouldBeFound("lastModified.greaterThanOrEqual=" + DEFAULT_LAST_MODIFIED);

        // Get all the businessDocumentList where lastModified is greater than or equal to UPDATED_LAST_MODIFIED
        defaultBusinessDocumentShouldNotBeFound("lastModified.greaterThanOrEqual=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllBusinessDocumentsByLastModifiedIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        businessDocumentRepository.saveAndFlush(businessDocument);

        // Get all the businessDocumentList where lastModified is less than or equal to DEFAULT_LAST_MODIFIED
        defaultBusinessDocumentShouldBeFound("lastModified.lessThanOrEqual=" + DEFAULT_LAST_MODIFIED);

        // Get all the businessDocumentList where lastModified is less than or equal to SMALLER_LAST_MODIFIED
        defaultBusinessDocumentShouldNotBeFound("lastModified.lessThanOrEqual=" + SMALLER_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllBusinessDocumentsByLastModifiedIsLessThanSomething() throws Exception {
        // Initialize the database
        businessDocumentRepository.saveAndFlush(businessDocument);

        // Get all the businessDocumentList where lastModified is less than DEFAULT_LAST_MODIFIED
        defaultBusinessDocumentShouldNotBeFound("lastModified.lessThan=" + DEFAULT_LAST_MODIFIED);

        // Get all the businessDocumentList where lastModified is less than UPDATED_LAST_MODIFIED
        defaultBusinessDocumentShouldBeFound("lastModified.lessThan=" + UPDATED_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllBusinessDocumentsByLastModifiedIsGreaterThanSomething() throws Exception {
        // Initialize the database
        businessDocumentRepository.saveAndFlush(businessDocument);

        // Get all the businessDocumentList where lastModified is greater than DEFAULT_LAST_MODIFIED
        defaultBusinessDocumentShouldNotBeFound("lastModified.greaterThan=" + DEFAULT_LAST_MODIFIED);

        // Get all the businessDocumentList where lastModified is greater than SMALLER_LAST_MODIFIED
        defaultBusinessDocumentShouldBeFound("lastModified.greaterThan=" + SMALLER_LAST_MODIFIED);
    }

    @Test
    @Transactional
    void getAllBusinessDocumentsByAttachmentFilePathIsEqualToSomething() throws Exception {
        // Initialize the database
        businessDocumentRepository.saveAndFlush(businessDocument);

        // Get all the businessDocumentList where attachmentFilePath equals to DEFAULT_ATTACHMENT_FILE_PATH
        defaultBusinessDocumentShouldBeFound("attachmentFilePath.equals=" + DEFAULT_ATTACHMENT_FILE_PATH);

        // Get all the businessDocumentList where attachmentFilePath equals to UPDATED_ATTACHMENT_FILE_PATH
        defaultBusinessDocumentShouldNotBeFound("attachmentFilePath.equals=" + UPDATED_ATTACHMENT_FILE_PATH);
    }

    @Test
    @Transactional
    void getAllBusinessDocumentsByAttachmentFilePathIsNotEqualToSomething() throws Exception {
        // Initialize the database
        businessDocumentRepository.saveAndFlush(businessDocument);

        // Get all the businessDocumentList where attachmentFilePath not equals to DEFAULT_ATTACHMENT_FILE_PATH
        defaultBusinessDocumentShouldNotBeFound("attachmentFilePath.notEquals=" + DEFAULT_ATTACHMENT_FILE_PATH);

        // Get all the businessDocumentList where attachmentFilePath not equals to UPDATED_ATTACHMENT_FILE_PATH
        defaultBusinessDocumentShouldBeFound("attachmentFilePath.notEquals=" + UPDATED_ATTACHMENT_FILE_PATH);
    }

    @Test
    @Transactional
    void getAllBusinessDocumentsByAttachmentFilePathIsInShouldWork() throws Exception {
        // Initialize the database
        businessDocumentRepository.saveAndFlush(businessDocument);

        // Get all the businessDocumentList where attachmentFilePath in DEFAULT_ATTACHMENT_FILE_PATH or UPDATED_ATTACHMENT_FILE_PATH
        defaultBusinessDocumentShouldBeFound("attachmentFilePath.in=" + DEFAULT_ATTACHMENT_FILE_PATH + "," + UPDATED_ATTACHMENT_FILE_PATH);

        // Get all the businessDocumentList where attachmentFilePath equals to UPDATED_ATTACHMENT_FILE_PATH
        defaultBusinessDocumentShouldNotBeFound("attachmentFilePath.in=" + UPDATED_ATTACHMENT_FILE_PATH);
    }

    @Test
    @Transactional
    void getAllBusinessDocumentsByAttachmentFilePathIsNullOrNotNull() throws Exception {
        // Initialize the database
        businessDocumentRepository.saveAndFlush(businessDocument);

        // Get all the businessDocumentList where attachmentFilePath is not null
        defaultBusinessDocumentShouldBeFound("attachmentFilePath.specified=true");

        // Get all the businessDocumentList where attachmentFilePath is null
        defaultBusinessDocumentShouldNotBeFound("attachmentFilePath.specified=false");
    }

    @Test
    @Transactional
    void getAllBusinessDocumentsByAttachmentFilePathContainsSomething() throws Exception {
        // Initialize the database
        businessDocumentRepository.saveAndFlush(businessDocument);

        // Get all the businessDocumentList where attachmentFilePath contains DEFAULT_ATTACHMENT_FILE_PATH
        defaultBusinessDocumentShouldBeFound("attachmentFilePath.contains=" + DEFAULT_ATTACHMENT_FILE_PATH);

        // Get all the businessDocumentList where attachmentFilePath contains UPDATED_ATTACHMENT_FILE_PATH
        defaultBusinessDocumentShouldNotBeFound("attachmentFilePath.contains=" + UPDATED_ATTACHMENT_FILE_PATH);
    }

    @Test
    @Transactional
    void getAllBusinessDocumentsByAttachmentFilePathNotContainsSomething() throws Exception {
        // Initialize the database
        businessDocumentRepository.saveAndFlush(businessDocument);

        // Get all the businessDocumentList where attachmentFilePath does not contain DEFAULT_ATTACHMENT_FILE_PATH
        defaultBusinessDocumentShouldNotBeFound("attachmentFilePath.doesNotContain=" + DEFAULT_ATTACHMENT_FILE_PATH);

        // Get all the businessDocumentList where attachmentFilePath does not contain UPDATED_ATTACHMENT_FILE_PATH
        defaultBusinessDocumentShouldBeFound("attachmentFilePath.doesNotContain=" + UPDATED_ATTACHMENT_FILE_PATH);
    }

    @Test
    @Transactional
    void getAllBusinessDocumentsByDocumentFileContentTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        businessDocumentRepository.saveAndFlush(businessDocument);

        // Get all the businessDocumentList where documentFileContentType equals to DEFAULT_DOCUMENT_FILE_CONTENT_TYPE
        defaultBusinessDocumentShouldBeFound("documentFileContentType.equals=" + DEFAULT_DOCUMENT_FILE_CONTENT_TYPE);

        // Get all the businessDocumentList where documentFileContentType equals to UPDATED_DOCUMENT_FILE_CONTENT_TYPE
        defaultBusinessDocumentShouldNotBeFound("documentFileContentType.equals=" + UPDATED_DOCUMENT_FILE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void getAllBusinessDocumentsByDocumentFileContentTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        businessDocumentRepository.saveAndFlush(businessDocument);

        // Get all the businessDocumentList where documentFileContentType not equals to DEFAULT_DOCUMENT_FILE_CONTENT_TYPE
        defaultBusinessDocumentShouldNotBeFound("documentFileContentType.notEquals=" + DEFAULT_DOCUMENT_FILE_CONTENT_TYPE);

        // Get all the businessDocumentList where documentFileContentType not equals to UPDATED_DOCUMENT_FILE_CONTENT_TYPE
        defaultBusinessDocumentShouldBeFound("documentFileContentType.notEquals=" + UPDATED_DOCUMENT_FILE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void getAllBusinessDocumentsByDocumentFileContentTypeIsInShouldWork() throws Exception {
        // Initialize the database
        businessDocumentRepository.saveAndFlush(businessDocument);

        // Get all the businessDocumentList where documentFileContentType in DEFAULT_DOCUMENT_FILE_CONTENT_TYPE or UPDATED_DOCUMENT_FILE_CONTENT_TYPE
        defaultBusinessDocumentShouldBeFound(
            "documentFileContentType.in=" + DEFAULT_DOCUMENT_FILE_CONTENT_TYPE + "," + UPDATED_DOCUMENT_FILE_CONTENT_TYPE
        );

        // Get all the businessDocumentList where documentFileContentType equals to UPDATED_DOCUMENT_FILE_CONTENT_TYPE
        defaultBusinessDocumentShouldNotBeFound("documentFileContentType.in=" + UPDATED_DOCUMENT_FILE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void getAllBusinessDocumentsByDocumentFileContentTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        businessDocumentRepository.saveAndFlush(businessDocument);

        // Get all the businessDocumentList where documentFileContentType is not null
        defaultBusinessDocumentShouldBeFound("documentFileContentType.specified=true");

        // Get all the businessDocumentList where documentFileContentType is null
        defaultBusinessDocumentShouldNotBeFound("documentFileContentType.specified=false");
    }

    @Test
    @Transactional
    void getAllBusinessDocumentsByDocumentFileContentTypeContainsSomething() throws Exception {
        // Initialize the database
        businessDocumentRepository.saveAndFlush(businessDocument);

        // Get all the businessDocumentList where documentFileContentType contains DEFAULT_DOCUMENT_FILE_CONTENT_TYPE
        defaultBusinessDocumentShouldBeFound("documentFileContentType.contains=" + DEFAULT_DOCUMENT_FILE_CONTENT_TYPE);

        // Get all the businessDocumentList where documentFileContentType contains UPDATED_DOCUMENT_FILE_CONTENT_TYPE
        defaultBusinessDocumentShouldNotBeFound("documentFileContentType.contains=" + UPDATED_DOCUMENT_FILE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void getAllBusinessDocumentsByDocumentFileContentTypeNotContainsSomething() throws Exception {
        // Initialize the database
        businessDocumentRepository.saveAndFlush(businessDocument);

        // Get all the businessDocumentList where documentFileContentType does not contain DEFAULT_DOCUMENT_FILE_CONTENT_TYPE
        defaultBusinessDocumentShouldNotBeFound("documentFileContentType.doesNotContain=" + DEFAULT_DOCUMENT_FILE_CONTENT_TYPE);

        // Get all the businessDocumentList where documentFileContentType does not contain UPDATED_DOCUMENT_FILE_CONTENT_TYPE
        defaultBusinessDocumentShouldBeFound("documentFileContentType.doesNotContain=" + UPDATED_DOCUMENT_FILE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void getAllBusinessDocumentsByFileTamperedIsEqualToSomething() throws Exception {
        // Initialize the database
        businessDocumentRepository.saveAndFlush(businessDocument);

        // Get all the businessDocumentList where fileTampered equals to DEFAULT_FILE_TAMPERED
        defaultBusinessDocumentShouldBeFound("fileTampered.equals=" + DEFAULT_FILE_TAMPERED);

        // Get all the businessDocumentList where fileTampered equals to UPDATED_FILE_TAMPERED
        defaultBusinessDocumentShouldNotBeFound("fileTampered.equals=" + UPDATED_FILE_TAMPERED);
    }

    @Test
    @Transactional
    void getAllBusinessDocumentsByFileTamperedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        businessDocumentRepository.saveAndFlush(businessDocument);

        // Get all the businessDocumentList where fileTampered not equals to DEFAULT_FILE_TAMPERED
        defaultBusinessDocumentShouldNotBeFound("fileTampered.notEquals=" + DEFAULT_FILE_TAMPERED);

        // Get all the businessDocumentList where fileTampered not equals to UPDATED_FILE_TAMPERED
        defaultBusinessDocumentShouldBeFound("fileTampered.notEquals=" + UPDATED_FILE_TAMPERED);
    }

    @Test
    @Transactional
    void getAllBusinessDocumentsByFileTamperedIsInShouldWork() throws Exception {
        // Initialize the database
        businessDocumentRepository.saveAndFlush(businessDocument);

        // Get all the businessDocumentList where fileTampered in DEFAULT_FILE_TAMPERED or UPDATED_FILE_TAMPERED
        defaultBusinessDocumentShouldBeFound("fileTampered.in=" + DEFAULT_FILE_TAMPERED + "," + UPDATED_FILE_TAMPERED);

        // Get all the businessDocumentList where fileTampered equals to UPDATED_FILE_TAMPERED
        defaultBusinessDocumentShouldNotBeFound("fileTampered.in=" + UPDATED_FILE_TAMPERED);
    }

    @Test
    @Transactional
    void getAllBusinessDocumentsByFileTamperedIsNullOrNotNull() throws Exception {
        // Initialize the database
        businessDocumentRepository.saveAndFlush(businessDocument);

        // Get all the businessDocumentList where fileTampered is not null
        defaultBusinessDocumentShouldBeFound("fileTampered.specified=true");

        // Get all the businessDocumentList where fileTampered is null
        defaultBusinessDocumentShouldNotBeFound("fileTampered.specified=false");
    }

    @Test
    @Transactional
    void getAllBusinessDocumentsByDocumentFileChecksumIsEqualToSomething() throws Exception {
        // Initialize the database
        businessDocumentRepository.saveAndFlush(businessDocument);

        // Get all the businessDocumentList where documentFileChecksum equals to DEFAULT_DOCUMENT_FILE_CHECKSUM
        defaultBusinessDocumentShouldBeFound("documentFileChecksum.equals=" + DEFAULT_DOCUMENT_FILE_CHECKSUM);

        // Get all the businessDocumentList where documentFileChecksum equals to UPDATED_DOCUMENT_FILE_CHECKSUM
        defaultBusinessDocumentShouldNotBeFound("documentFileChecksum.equals=" + UPDATED_DOCUMENT_FILE_CHECKSUM);
    }

    @Test
    @Transactional
    void getAllBusinessDocumentsByDocumentFileChecksumIsNotEqualToSomething() throws Exception {
        // Initialize the database
        businessDocumentRepository.saveAndFlush(businessDocument);

        // Get all the businessDocumentList where documentFileChecksum not equals to DEFAULT_DOCUMENT_FILE_CHECKSUM
        defaultBusinessDocumentShouldNotBeFound("documentFileChecksum.notEquals=" + DEFAULT_DOCUMENT_FILE_CHECKSUM);

        // Get all the businessDocumentList where documentFileChecksum not equals to UPDATED_DOCUMENT_FILE_CHECKSUM
        defaultBusinessDocumentShouldBeFound("documentFileChecksum.notEquals=" + UPDATED_DOCUMENT_FILE_CHECKSUM);
    }

    @Test
    @Transactional
    void getAllBusinessDocumentsByDocumentFileChecksumIsInShouldWork() throws Exception {
        // Initialize the database
        businessDocumentRepository.saveAndFlush(businessDocument);

        // Get all the businessDocumentList where documentFileChecksum in DEFAULT_DOCUMENT_FILE_CHECKSUM or UPDATED_DOCUMENT_FILE_CHECKSUM
        defaultBusinessDocumentShouldBeFound(
            "documentFileChecksum.in=" + DEFAULT_DOCUMENT_FILE_CHECKSUM + "," + UPDATED_DOCUMENT_FILE_CHECKSUM
        );

        // Get all the businessDocumentList where documentFileChecksum equals to UPDATED_DOCUMENT_FILE_CHECKSUM
        defaultBusinessDocumentShouldNotBeFound("documentFileChecksum.in=" + UPDATED_DOCUMENT_FILE_CHECKSUM);
    }

    @Test
    @Transactional
    void getAllBusinessDocumentsByDocumentFileChecksumIsNullOrNotNull() throws Exception {
        // Initialize the database
        businessDocumentRepository.saveAndFlush(businessDocument);

        // Get all the businessDocumentList where documentFileChecksum is not null
        defaultBusinessDocumentShouldBeFound("documentFileChecksum.specified=true");

        // Get all the businessDocumentList where documentFileChecksum is null
        defaultBusinessDocumentShouldNotBeFound("documentFileChecksum.specified=false");
    }

    @Test
    @Transactional
    void getAllBusinessDocumentsByDocumentFileChecksumContainsSomething() throws Exception {
        // Initialize the database
        businessDocumentRepository.saveAndFlush(businessDocument);

        // Get all the businessDocumentList where documentFileChecksum contains DEFAULT_DOCUMENT_FILE_CHECKSUM
        defaultBusinessDocumentShouldBeFound("documentFileChecksum.contains=" + DEFAULT_DOCUMENT_FILE_CHECKSUM);

        // Get all the businessDocumentList where documentFileChecksum contains UPDATED_DOCUMENT_FILE_CHECKSUM
        defaultBusinessDocumentShouldNotBeFound("documentFileChecksum.contains=" + UPDATED_DOCUMENT_FILE_CHECKSUM);
    }

    @Test
    @Transactional
    void getAllBusinessDocumentsByDocumentFileChecksumNotContainsSomething() throws Exception {
        // Initialize the database
        businessDocumentRepository.saveAndFlush(businessDocument);

        // Get all the businessDocumentList where documentFileChecksum does not contain DEFAULT_DOCUMENT_FILE_CHECKSUM
        defaultBusinessDocumentShouldNotBeFound("documentFileChecksum.doesNotContain=" + DEFAULT_DOCUMENT_FILE_CHECKSUM);

        // Get all the businessDocumentList where documentFileChecksum does not contain UPDATED_DOCUMENT_FILE_CHECKSUM
        defaultBusinessDocumentShouldBeFound("documentFileChecksum.doesNotContain=" + UPDATED_DOCUMENT_FILE_CHECKSUM);
    }

    @Test
    @Transactional
    void getAllBusinessDocumentsByCreatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        businessDocumentRepository.saveAndFlush(businessDocument);
        ApplicationUser createdBy;
        if (TestUtil.findAll(em, ApplicationUser.class).isEmpty()) {
            createdBy = ApplicationUserResourceIT.createEntity(em);
            em.persist(createdBy);
            em.flush();
        } else {
            createdBy = TestUtil.findAll(em, ApplicationUser.class).get(0);
        }
        em.persist(createdBy);
        em.flush();
        businessDocument.setCreatedBy(createdBy);
        businessDocumentRepository.saveAndFlush(businessDocument);
        Long createdById = createdBy.getId();

        // Get all the businessDocumentList where createdBy equals to createdById
        defaultBusinessDocumentShouldBeFound("createdById.equals=" + createdById);

        // Get all the businessDocumentList where createdBy equals to (createdById + 1)
        defaultBusinessDocumentShouldNotBeFound("createdById.equals=" + (createdById + 1));
    }

    @Test
    @Transactional
    void getAllBusinessDocumentsByLastModifiedByIsEqualToSomething() throws Exception {
        // Initialize the database
        businessDocumentRepository.saveAndFlush(businessDocument);
        ApplicationUser lastModifiedBy;
        if (TestUtil.findAll(em, ApplicationUser.class).isEmpty()) {
            lastModifiedBy = ApplicationUserResourceIT.createEntity(em);
            em.persist(lastModifiedBy);
            em.flush();
        } else {
            lastModifiedBy = TestUtil.findAll(em, ApplicationUser.class).get(0);
        }
        em.persist(lastModifiedBy);
        em.flush();
        businessDocument.setLastModifiedBy(lastModifiedBy);
        businessDocumentRepository.saveAndFlush(businessDocument);
        Long lastModifiedById = lastModifiedBy.getId();

        // Get all the businessDocumentList where lastModifiedBy equals to lastModifiedById
        defaultBusinessDocumentShouldBeFound("lastModifiedById.equals=" + lastModifiedById);

        // Get all the businessDocumentList where lastModifiedBy equals to (lastModifiedById + 1)
        defaultBusinessDocumentShouldNotBeFound("lastModifiedById.equals=" + (lastModifiedById + 1));
    }

    @Test
    @Transactional
    void getAllBusinessDocumentsByOriginatingDepartmentIsEqualToSomething() throws Exception {
        // Initialize the database
        businessDocumentRepository.saveAndFlush(businessDocument);
        Dealer originatingDepartment;
        if (TestUtil.findAll(em, Dealer.class).isEmpty()) {
            originatingDepartment = DealerResourceIT.createEntity(em);
            em.persist(originatingDepartment);
            em.flush();
        } else {
            originatingDepartment = TestUtil.findAll(em, Dealer.class).get(0);
        }
        em.persist(originatingDepartment);
        em.flush();
        businessDocument.setOriginatingDepartment(originatingDepartment);
        businessDocumentRepository.saveAndFlush(businessDocument);
        Long originatingDepartmentId = originatingDepartment.getId();

        // Get all the businessDocumentList where originatingDepartment equals to originatingDepartmentId
        defaultBusinessDocumentShouldBeFound("originatingDepartmentId.equals=" + originatingDepartmentId);

        // Get all the businessDocumentList where originatingDepartment equals to (originatingDepartmentId + 1)
        defaultBusinessDocumentShouldNotBeFound("originatingDepartmentId.equals=" + (originatingDepartmentId + 1));
    }

    @Test
    @Transactional
    void getAllBusinessDocumentsByApplicationMappingsIsEqualToSomething() throws Exception {
        // Initialize the database
        businessDocumentRepository.saveAndFlush(businessDocument);
        UniversallyUniqueMapping applicationMappings;
        if (TestUtil.findAll(em, UniversallyUniqueMapping.class).isEmpty()) {
            applicationMappings = UniversallyUniqueMappingResourceIT.createEntity(em);
            em.persist(applicationMappings);
            em.flush();
        } else {
            applicationMappings = TestUtil.findAll(em, UniversallyUniqueMapping.class).get(0);
        }
        em.persist(applicationMappings);
        em.flush();
        businessDocument.addApplicationMappings(applicationMappings);
        businessDocumentRepository.saveAndFlush(businessDocument);
        Long applicationMappingsId = applicationMappings.getId();

        // Get all the businessDocumentList where applicationMappings equals to applicationMappingsId
        defaultBusinessDocumentShouldBeFound("applicationMappingsId.equals=" + applicationMappingsId);

        // Get all the businessDocumentList where applicationMappings equals to (applicationMappingsId + 1)
        defaultBusinessDocumentShouldNotBeFound("applicationMappingsId.equals=" + (applicationMappingsId + 1));
    }

    @Test
    @Transactional
    void getAllBusinessDocumentsByPlaceholderIsEqualToSomething() throws Exception {
        // Initialize the database
        businessDocumentRepository.saveAndFlush(businessDocument);
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
        businessDocument.addPlaceholder(placeholder);
        businessDocumentRepository.saveAndFlush(businessDocument);
        Long placeholderId = placeholder.getId();

        // Get all the businessDocumentList where placeholder equals to placeholderId
        defaultBusinessDocumentShouldBeFound("placeholderId.equals=" + placeholderId);

        // Get all the businessDocumentList where placeholder equals to (placeholderId + 1)
        defaultBusinessDocumentShouldNotBeFound("placeholderId.equals=" + (placeholderId + 1));
    }

    @Test
    @Transactional
    void getAllBusinessDocumentsByFileChecksumAlgorithmIsEqualToSomething() throws Exception {
        // Initialize the database
        businessDocumentRepository.saveAndFlush(businessDocument);
        Algorithm fileChecksumAlgorithm;
        if (TestUtil.findAll(em, Algorithm.class).isEmpty()) {
            fileChecksumAlgorithm = AlgorithmResourceIT.createEntity(em);
            em.persist(fileChecksumAlgorithm);
            em.flush();
        } else {
            fileChecksumAlgorithm = TestUtil.findAll(em, Algorithm.class).get(0);
        }
        em.persist(fileChecksumAlgorithm);
        em.flush();
        businessDocument.setFileChecksumAlgorithm(fileChecksumAlgorithm);
        businessDocumentRepository.saveAndFlush(businessDocument);
        Long fileChecksumAlgorithmId = fileChecksumAlgorithm.getId();

        // Get all the businessDocumentList where fileChecksumAlgorithm equals to fileChecksumAlgorithmId
        defaultBusinessDocumentShouldBeFound("fileChecksumAlgorithmId.equals=" + fileChecksumAlgorithmId);

        // Get all the businessDocumentList where fileChecksumAlgorithm equals to (fileChecksumAlgorithmId + 1)
        defaultBusinessDocumentShouldNotBeFound("fileChecksumAlgorithmId.equals=" + (fileChecksumAlgorithmId + 1));
    }

    @Test
    @Transactional
    void getAllBusinessDocumentsBySecurityClearanceIsEqualToSomething() throws Exception {
        // Initialize the database
        businessDocumentRepository.saveAndFlush(businessDocument);
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
        businessDocument.setSecurityClearance(securityClearance);
        businessDocumentRepository.saveAndFlush(businessDocument);
        Long securityClearanceId = securityClearance.getId();

        // Get all the businessDocumentList where securityClearance equals to securityClearanceId
        defaultBusinessDocumentShouldBeFound("securityClearanceId.equals=" + securityClearanceId);

        // Get all the businessDocumentList where securityClearance equals to (securityClearanceId + 1)
        defaultBusinessDocumentShouldNotBeFound("securityClearanceId.equals=" + (securityClearanceId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultBusinessDocumentShouldBeFound(String filter) throws Exception {
        restBusinessDocumentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(businessDocument.getId().intValue())))
            .andExpect(jsonPath("$.[*].documentTitle").value(hasItem(DEFAULT_DOCUMENT_TITLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].documentSerial").value(hasItem(DEFAULT_DOCUMENT_SERIAL.toString())))
            .andExpect(jsonPath("$.[*].lastModified").value(hasItem(sameInstant(DEFAULT_LAST_MODIFIED))))
            .andExpect(jsonPath("$.[*].attachmentFilePath").value(hasItem(DEFAULT_ATTACHMENT_FILE_PATH)))
            .andExpect(jsonPath("$.[*].documentFileContentType").value(hasItem(DEFAULT_DOCUMENT_FILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].fileTampered").value(hasItem(DEFAULT_FILE_TAMPERED.booleanValue())))
            .andExpect(jsonPath("$.[*].documentFileChecksum").value(hasItem(DEFAULT_DOCUMENT_FILE_CHECKSUM)));

        // Check, that the count call also returns 1
        restBusinessDocumentMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultBusinessDocumentShouldNotBeFound(String filter) throws Exception {
        restBusinessDocumentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restBusinessDocumentMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingBusinessDocument() throws Exception {
        // Get the businessDocument
        restBusinessDocumentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewBusinessDocument() throws Exception {
        // Initialize the database
        businessDocumentRepository.saveAndFlush(businessDocument);

        int databaseSizeBeforeUpdate = businessDocumentRepository.findAll().size();

        // Update the businessDocument
        BusinessDocument updatedBusinessDocument = businessDocumentRepository.findById(businessDocument.getId()).get();
        // Disconnect from session so that the updates on updatedBusinessDocument are not directly saved in db
        em.detach(updatedBusinessDocument);
        updatedBusinessDocument
            .documentTitle(UPDATED_DOCUMENT_TITLE)
            .description(UPDATED_DESCRIPTION)
            .documentSerial(UPDATED_DOCUMENT_SERIAL)
            .lastModified(UPDATED_LAST_MODIFIED)
            .attachmentFilePath(UPDATED_ATTACHMENT_FILE_PATH)
            .documentFileContentType(UPDATED_DOCUMENT_FILE_CONTENT_TYPE)
            .fileTampered(UPDATED_FILE_TAMPERED)
            .documentFileChecksum(UPDATED_DOCUMENT_FILE_CHECKSUM);
        BusinessDocumentDTO businessDocumentDTO = businessDocumentMapper.toDto(updatedBusinessDocument);

        restBusinessDocumentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, businessDocumentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(businessDocumentDTO))
            )
            .andExpect(status().isOk());

        // Validate the BusinessDocument in the database
        List<BusinessDocument> businessDocumentList = businessDocumentRepository.findAll();
        assertThat(businessDocumentList).hasSize(databaseSizeBeforeUpdate);
        BusinessDocument testBusinessDocument = businessDocumentList.get(businessDocumentList.size() - 1);
        assertThat(testBusinessDocument.getDocumentTitle()).isEqualTo(UPDATED_DOCUMENT_TITLE);
        assertThat(testBusinessDocument.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testBusinessDocument.getDocumentSerial()).isEqualTo(UPDATED_DOCUMENT_SERIAL);
        assertThat(testBusinessDocument.getLastModified()).isEqualTo(UPDATED_LAST_MODIFIED);
        assertThat(testBusinessDocument.getAttachmentFilePath()).isEqualTo(UPDATED_ATTACHMENT_FILE_PATH);
        assertThat(testBusinessDocument.getDocumentFileContentType()).isEqualTo(UPDATED_DOCUMENT_FILE_CONTENT_TYPE);
        assertThat(testBusinessDocument.getFileTampered()).isEqualTo(UPDATED_FILE_TAMPERED);
        assertThat(testBusinessDocument.getDocumentFileChecksum()).isEqualTo(UPDATED_DOCUMENT_FILE_CHECKSUM);

        // Validate the BusinessDocument in Elasticsearch
        verify(mockBusinessDocumentSearchRepository).save(testBusinessDocument);
    }

    @Test
    @Transactional
    void putNonExistingBusinessDocument() throws Exception {
        int databaseSizeBeforeUpdate = businessDocumentRepository.findAll().size();
        businessDocument.setId(count.incrementAndGet());

        // Create the BusinessDocument
        BusinessDocumentDTO businessDocumentDTO = businessDocumentMapper.toDto(businessDocument);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBusinessDocumentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, businessDocumentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(businessDocumentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BusinessDocument in the database
        List<BusinessDocument> businessDocumentList = businessDocumentRepository.findAll();
        assertThat(businessDocumentList).hasSize(databaseSizeBeforeUpdate);

        // Validate the BusinessDocument in Elasticsearch
        verify(mockBusinessDocumentSearchRepository, times(0)).save(businessDocument);
    }

    @Test
    @Transactional
    void putWithIdMismatchBusinessDocument() throws Exception {
        int databaseSizeBeforeUpdate = businessDocumentRepository.findAll().size();
        businessDocument.setId(count.incrementAndGet());

        // Create the BusinessDocument
        BusinessDocumentDTO businessDocumentDTO = businessDocumentMapper.toDto(businessDocument);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBusinessDocumentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(businessDocumentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BusinessDocument in the database
        List<BusinessDocument> businessDocumentList = businessDocumentRepository.findAll();
        assertThat(businessDocumentList).hasSize(databaseSizeBeforeUpdate);

        // Validate the BusinessDocument in Elasticsearch
        verify(mockBusinessDocumentSearchRepository, times(0)).save(businessDocument);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBusinessDocument() throws Exception {
        int databaseSizeBeforeUpdate = businessDocumentRepository.findAll().size();
        businessDocument.setId(count.incrementAndGet());

        // Create the BusinessDocument
        BusinessDocumentDTO businessDocumentDTO = businessDocumentMapper.toDto(businessDocument);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBusinessDocumentMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(businessDocumentDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the BusinessDocument in the database
        List<BusinessDocument> businessDocumentList = businessDocumentRepository.findAll();
        assertThat(businessDocumentList).hasSize(databaseSizeBeforeUpdate);

        // Validate the BusinessDocument in Elasticsearch
        verify(mockBusinessDocumentSearchRepository, times(0)).save(businessDocument);
    }

    @Test
    @Transactional
    void partialUpdateBusinessDocumentWithPatch() throws Exception {
        // Initialize the database
        businessDocumentRepository.saveAndFlush(businessDocument);

        int databaseSizeBeforeUpdate = businessDocumentRepository.findAll().size();

        // Update the businessDocument using partial update
        BusinessDocument partialUpdatedBusinessDocument = new BusinessDocument();
        partialUpdatedBusinessDocument.setId(businessDocument.getId());

        partialUpdatedBusinessDocument
            .documentSerial(UPDATED_DOCUMENT_SERIAL)
            .lastModified(UPDATED_LAST_MODIFIED)
            .attachmentFilePath(UPDATED_ATTACHMENT_FILE_PATH)
            .fileTampered(UPDATED_FILE_TAMPERED);

        restBusinessDocumentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBusinessDocument.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBusinessDocument))
            )
            .andExpect(status().isOk());

        // Validate the BusinessDocument in the database
        List<BusinessDocument> businessDocumentList = businessDocumentRepository.findAll();
        assertThat(businessDocumentList).hasSize(databaseSizeBeforeUpdate);
        BusinessDocument testBusinessDocument = businessDocumentList.get(businessDocumentList.size() - 1);
        assertThat(testBusinessDocument.getDocumentTitle()).isEqualTo(DEFAULT_DOCUMENT_TITLE);
        assertThat(testBusinessDocument.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testBusinessDocument.getDocumentSerial()).isEqualTo(UPDATED_DOCUMENT_SERIAL);
        assertThat(testBusinessDocument.getLastModified()).isEqualTo(UPDATED_LAST_MODIFIED);
        assertThat(testBusinessDocument.getAttachmentFilePath()).isEqualTo(UPDATED_ATTACHMENT_FILE_PATH);
        assertThat(testBusinessDocument.getDocumentFileContentType()).isEqualTo(DEFAULT_DOCUMENT_FILE_CONTENT_TYPE);
        assertThat(testBusinessDocument.getFileTampered()).isEqualTo(UPDATED_FILE_TAMPERED);
        assertThat(testBusinessDocument.getDocumentFileChecksum()).isEqualTo(DEFAULT_DOCUMENT_FILE_CHECKSUM);
    }

    @Test
    @Transactional
    void fullUpdateBusinessDocumentWithPatch() throws Exception {
        // Initialize the database
        businessDocumentRepository.saveAndFlush(businessDocument);

        int databaseSizeBeforeUpdate = businessDocumentRepository.findAll().size();

        // Update the businessDocument using partial update
        BusinessDocument partialUpdatedBusinessDocument = new BusinessDocument();
        partialUpdatedBusinessDocument.setId(businessDocument.getId());

        partialUpdatedBusinessDocument
            .documentTitle(UPDATED_DOCUMENT_TITLE)
            .description(UPDATED_DESCRIPTION)
            .documentSerial(UPDATED_DOCUMENT_SERIAL)
            .lastModified(UPDATED_LAST_MODIFIED)
            .attachmentFilePath(UPDATED_ATTACHMENT_FILE_PATH)
            .documentFileContentType(UPDATED_DOCUMENT_FILE_CONTENT_TYPE)
            .fileTampered(UPDATED_FILE_TAMPERED)
            .documentFileChecksum(UPDATED_DOCUMENT_FILE_CHECKSUM);

        restBusinessDocumentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBusinessDocument.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBusinessDocument))
            )
            .andExpect(status().isOk());

        // Validate the BusinessDocument in the database
        List<BusinessDocument> businessDocumentList = businessDocumentRepository.findAll();
        assertThat(businessDocumentList).hasSize(databaseSizeBeforeUpdate);
        BusinessDocument testBusinessDocument = businessDocumentList.get(businessDocumentList.size() - 1);
        assertThat(testBusinessDocument.getDocumentTitle()).isEqualTo(UPDATED_DOCUMENT_TITLE);
        assertThat(testBusinessDocument.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testBusinessDocument.getDocumentSerial()).isEqualTo(UPDATED_DOCUMENT_SERIAL);
        assertThat(testBusinessDocument.getLastModified()).isEqualTo(UPDATED_LAST_MODIFIED);
        assertThat(testBusinessDocument.getAttachmentFilePath()).isEqualTo(UPDATED_ATTACHMENT_FILE_PATH);
        assertThat(testBusinessDocument.getDocumentFileContentType()).isEqualTo(UPDATED_DOCUMENT_FILE_CONTENT_TYPE);
        assertThat(testBusinessDocument.getFileTampered()).isEqualTo(UPDATED_FILE_TAMPERED);
        assertThat(testBusinessDocument.getDocumentFileChecksum()).isEqualTo(UPDATED_DOCUMENT_FILE_CHECKSUM);
    }

    @Test
    @Transactional
    void patchNonExistingBusinessDocument() throws Exception {
        int databaseSizeBeforeUpdate = businessDocumentRepository.findAll().size();
        businessDocument.setId(count.incrementAndGet());

        // Create the BusinessDocument
        BusinessDocumentDTO businessDocumentDTO = businessDocumentMapper.toDto(businessDocument);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBusinessDocumentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, businessDocumentDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(businessDocumentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BusinessDocument in the database
        List<BusinessDocument> businessDocumentList = businessDocumentRepository.findAll();
        assertThat(businessDocumentList).hasSize(databaseSizeBeforeUpdate);

        // Validate the BusinessDocument in Elasticsearch
        verify(mockBusinessDocumentSearchRepository, times(0)).save(businessDocument);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBusinessDocument() throws Exception {
        int databaseSizeBeforeUpdate = businessDocumentRepository.findAll().size();
        businessDocument.setId(count.incrementAndGet());

        // Create the BusinessDocument
        BusinessDocumentDTO businessDocumentDTO = businessDocumentMapper.toDto(businessDocument);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBusinessDocumentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(businessDocumentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BusinessDocument in the database
        List<BusinessDocument> businessDocumentList = businessDocumentRepository.findAll();
        assertThat(businessDocumentList).hasSize(databaseSizeBeforeUpdate);

        // Validate the BusinessDocument in Elasticsearch
        verify(mockBusinessDocumentSearchRepository, times(0)).save(businessDocument);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBusinessDocument() throws Exception {
        int databaseSizeBeforeUpdate = businessDocumentRepository.findAll().size();
        businessDocument.setId(count.incrementAndGet());

        // Create the BusinessDocument
        BusinessDocumentDTO businessDocumentDTO = businessDocumentMapper.toDto(businessDocument);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBusinessDocumentMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(businessDocumentDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the BusinessDocument in the database
        List<BusinessDocument> businessDocumentList = businessDocumentRepository.findAll();
        assertThat(businessDocumentList).hasSize(databaseSizeBeforeUpdate);

        // Validate the BusinessDocument in Elasticsearch
        verify(mockBusinessDocumentSearchRepository, times(0)).save(businessDocument);
    }

    @Test
    @Transactional
    void deleteBusinessDocument() throws Exception {
        // Initialize the database
        businessDocumentRepository.saveAndFlush(businessDocument);

        int databaseSizeBeforeDelete = businessDocumentRepository.findAll().size();

        // Delete the businessDocument
        restBusinessDocumentMockMvc
            .perform(delete(ENTITY_API_URL_ID, businessDocument.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<BusinessDocument> businessDocumentList = businessDocumentRepository.findAll();
        assertThat(businessDocumentList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the BusinessDocument in Elasticsearch
        verify(mockBusinessDocumentSearchRepository, times(1)).deleteById(businessDocument.getId());
    }

    @Test
    @Transactional
    void searchBusinessDocument() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        businessDocumentRepository.saveAndFlush(businessDocument);
        when(mockBusinessDocumentSearchRepository.search("id:" + businessDocument.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(businessDocument), PageRequest.of(0, 1), 1));

        // Search the businessDocument
        restBusinessDocumentMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + businessDocument.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(businessDocument.getId().intValue())))
            .andExpect(jsonPath("$.[*].documentTitle").value(hasItem(DEFAULT_DOCUMENT_TITLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].documentSerial").value(hasItem(DEFAULT_DOCUMENT_SERIAL.toString())))
            .andExpect(jsonPath("$.[*].lastModified").value(hasItem(sameInstant(DEFAULT_LAST_MODIFIED))))
            .andExpect(jsonPath("$.[*].attachmentFilePath").value(hasItem(DEFAULT_ATTACHMENT_FILE_PATH)))
            .andExpect(jsonPath("$.[*].documentFileContentType").value(hasItem(DEFAULT_DOCUMENT_FILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].fileTampered").value(hasItem(DEFAULT_FILE_TAMPERED.booleanValue())))
            .andExpect(jsonPath("$.[*].documentFileChecksum").value(hasItem(DEFAULT_DOCUMENT_FILE_CHECKSUM)));
    }
}
