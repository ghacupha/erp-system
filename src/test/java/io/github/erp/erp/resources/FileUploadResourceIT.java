package io.github.erp.erp.resources;

/*-
 * Erp System - Mark II No 9 (Artaxerxes Series)
 * Copyright © 2021 Edwin Njeru (mailnjeru@gmail.com)
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
import io.github.erp.domain.FileUpload;
import io.github.erp.domain.Placeholder;
import io.github.erp.repository.FileUploadRepository;
import io.github.erp.repository.search.FileUploadSearchRepository;
import io.github.erp.service.FileUploadService;
import io.github.erp.service.dto.FileUploadDTO;
import io.github.erp.service.mapper.FileUploadMapper;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;

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

/**
 * Integration tests for the {@link FileUploadResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser(roles = {"DBA"})
class FileUploadResourceIT {

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_FILE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FILE_NAME = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_PERIOD_FROM = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_PERIOD_FROM = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_PERIOD_FROM = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_PERIOD_TO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_PERIOD_TO = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_PERIOD_TO = LocalDate.ofEpochDay(-1L);

    private static final Long DEFAULT_FILE_TYPE_ID = 1L;
    private static final Long UPDATED_FILE_TYPE_ID = 2L;
    private static final Long SMALLER_FILE_TYPE_ID = 1L - 1L;

    private static final byte[] DEFAULT_DATA_FILE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_DATA_FILE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_DATA_FILE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_DATA_FILE_CONTENT_TYPE = "image/png";

    private static final Boolean DEFAULT_UPLOAD_SUCCESSFUL = false;
    private static final Boolean UPDATED_UPLOAD_SUCCESSFUL = true;

    private static final Boolean DEFAULT_UPLOAD_PROCESSED = false;
    private static final Boolean UPDATED_UPLOAD_PROCESSED = true;

    private static final String DEFAULT_UPLOAD_TOKEN = "AAAAAAAAAA";
    private static final String UPDATED_UPLOAD_TOKEN = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/files/file-uploads";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/files/_search/file-uploads";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FileUploadRepository fileUploadRepository;

    @Mock
    private FileUploadRepository fileUploadRepositoryMock;

    @Autowired
    private FileUploadMapper fileUploadMapper;

    @Mock
    private FileUploadService fileUploadServiceMock;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.FileUploadSearchRepositoryMockConfiguration
     */
    @Autowired
    private FileUploadSearchRepository mockFileUploadSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFileUploadMockMvc;

    private FileUpload fileUpload;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FileUpload createEntity(EntityManager em) {
        FileUpload fileUpload = new FileUpload()
            .description(DEFAULT_DESCRIPTION)
            .fileName(DEFAULT_FILE_NAME)
            .periodFrom(DEFAULT_PERIOD_FROM)
            .periodTo(DEFAULT_PERIOD_TO)
            .fileTypeId(DEFAULT_FILE_TYPE_ID)
            .dataFile(DEFAULT_DATA_FILE)
            .dataFileContentType(DEFAULT_DATA_FILE_CONTENT_TYPE)
            .uploadSuccessful(DEFAULT_UPLOAD_SUCCESSFUL)
            .uploadProcessed(DEFAULT_UPLOAD_PROCESSED)
            .uploadToken(DEFAULT_UPLOAD_TOKEN);
        return fileUpload;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FileUpload createUpdatedEntity(EntityManager em) {
        FileUpload fileUpload = new FileUpload()
            .description(UPDATED_DESCRIPTION)
            .fileName(UPDATED_FILE_NAME)
            .periodFrom(UPDATED_PERIOD_FROM)
            .periodTo(UPDATED_PERIOD_TO)
            .fileTypeId(UPDATED_FILE_TYPE_ID)
            .dataFile(UPDATED_DATA_FILE)
            .dataFileContentType(UPDATED_DATA_FILE_CONTENT_TYPE)
            .uploadSuccessful(UPDATED_UPLOAD_SUCCESSFUL)
            .uploadProcessed(UPDATED_UPLOAD_PROCESSED)
            .uploadToken(UPDATED_UPLOAD_TOKEN);
        return fileUpload;
    }

    @BeforeEach
    public void initTest() {
        fileUpload = createEntity(em);
    }

    @Test
    @Transactional
    void createFileUpload() throws Exception {
        int databaseSizeBeforeCreate = fileUploadRepository.findAll().size();
        // Create the FileUpload
        FileUploadDTO fileUploadDTO = fileUploadMapper.toDto(fileUpload);
        restFileUploadMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fileUploadDTO)))
            .andExpect(status().isCreated());

        // Validate the FileUpload in the database
        List<FileUpload> fileUploadList = fileUploadRepository.findAll();
        assertThat(fileUploadList).hasSize(databaseSizeBeforeCreate + 1);
        FileUpload testFileUpload = fileUploadList.get(fileUploadList.size() - 1);
        assertThat(testFileUpload.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testFileUpload.getFileName()).isEqualTo(DEFAULT_FILE_NAME);
        assertThat(testFileUpload.getPeriodFrom()).isEqualTo(DEFAULT_PERIOD_FROM);
        assertThat(testFileUpload.getPeriodTo()).isEqualTo(DEFAULT_PERIOD_TO);
        assertThat(testFileUpload.getFileTypeId()).isEqualTo(DEFAULT_FILE_TYPE_ID);
        assertThat(testFileUpload.getDataFile()).isEqualTo(DEFAULT_DATA_FILE);
        assertThat(testFileUpload.getDataFileContentType()).isEqualTo(DEFAULT_DATA_FILE_CONTENT_TYPE);
        assertThat(testFileUpload.getUploadSuccessful()).isEqualTo(DEFAULT_UPLOAD_SUCCESSFUL);
        assertThat(testFileUpload.getUploadProcessed()).isEqualTo(DEFAULT_UPLOAD_PROCESSED);
        assertThat(testFileUpload.getUploadToken()).isEqualTo(DEFAULT_UPLOAD_TOKEN);

        // Validate the FileUpload in Elasticsearch
        verify(mockFileUploadSearchRepository, times(1)).save(testFileUpload);
    }

    @Test
    @Transactional
    void createFileUploadWithExistingId() throws Exception {
        // Create the FileUpload with an existing ID
        fileUpload.setId(1L);
        FileUploadDTO fileUploadDTO = fileUploadMapper.toDto(fileUpload);

        int databaseSizeBeforeCreate = fileUploadRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFileUploadMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fileUploadDTO)))
            .andExpect(status().isBadRequest());

        // Validate the FileUpload in the database
        List<FileUpload> fileUploadList = fileUploadRepository.findAll();
        assertThat(fileUploadList).hasSize(databaseSizeBeforeCreate);

        // Validate the FileUpload in Elasticsearch
        verify(mockFileUploadSearchRepository, times(0)).save(fileUpload);
    }

    @Test
    @Transactional
    void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = fileUploadRepository.findAll().size();
        // set the field null
        fileUpload.setDescription(null);

        // Create the FileUpload, which fails.
        FileUploadDTO fileUploadDTO = fileUploadMapper.toDto(fileUpload);

        restFileUploadMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fileUploadDTO)))
            .andExpect(status().isBadRequest());

        List<FileUpload> fileUploadList = fileUploadRepository.findAll();
        assertThat(fileUploadList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFileNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = fileUploadRepository.findAll().size();
        // set the field null
        fileUpload.setFileName(null);

        // Create the FileUpload, which fails.
        FileUploadDTO fileUploadDTO = fileUploadMapper.toDto(fileUpload);

        restFileUploadMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fileUploadDTO)))
            .andExpect(status().isBadRequest());

        List<FileUpload> fileUploadList = fileUploadRepository.findAll();
        assertThat(fileUploadList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFileTypeIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = fileUploadRepository.findAll().size();
        // set the field null
        fileUpload.setFileTypeId(null);

        // Create the FileUpload, which fails.
        FileUploadDTO fileUploadDTO = fileUploadMapper.toDto(fileUpload);

        restFileUploadMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fileUploadDTO)))
            .andExpect(status().isBadRequest());

        List<FileUpload> fileUploadList = fileUploadRepository.findAll();
        assertThat(fileUploadList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllFileUploads() throws Exception {
        // Initialize the database
        fileUploadRepository.saveAndFlush(fileUpload);

        // Get all the fileUploadList
        restFileUploadMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fileUpload.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].fileName").value(hasItem(DEFAULT_FILE_NAME)))
            .andExpect(jsonPath("$.[*].periodFrom").value(hasItem(DEFAULT_PERIOD_FROM.toString())))
            .andExpect(jsonPath("$.[*].periodTo").value(hasItem(DEFAULT_PERIOD_TO.toString())))
            .andExpect(jsonPath("$.[*].fileTypeId").value(hasItem(DEFAULT_FILE_TYPE_ID.intValue())))
            .andExpect(jsonPath("$.[*].dataFileContentType").value(hasItem(DEFAULT_DATA_FILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].dataFile").value(hasItem(Base64Utils.encodeToString(DEFAULT_DATA_FILE))))
            .andExpect(jsonPath("$.[*].uploadSuccessful").value(hasItem(DEFAULT_UPLOAD_SUCCESSFUL.booleanValue())))
            .andExpect(jsonPath("$.[*].uploadProcessed").value(hasItem(DEFAULT_UPLOAD_PROCESSED.booleanValue())))
            .andExpect(jsonPath("$.[*].uploadToken").value(hasItem(DEFAULT_UPLOAD_TOKEN)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllFileUploadsWithEagerRelationshipsIsEnabled() throws Exception {
        when(fileUploadServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restFileUploadMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(fileUploadServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllFileUploadsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(fileUploadServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restFileUploadMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(fileUploadServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getFileUpload() throws Exception {
        // Initialize the database
        fileUploadRepository.saveAndFlush(fileUpload);

        // Get the fileUpload
        restFileUploadMockMvc
            .perform(get(ENTITY_API_URL_ID, fileUpload.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(fileUpload.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.fileName").value(DEFAULT_FILE_NAME))
            .andExpect(jsonPath("$.periodFrom").value(DEFAULT_PERIOD_FROM.toString()))
            .andExpect(jsonPath("$.periodTo").value(DEFAULT_PERIOD_TO.toString()))
            .andExpect(jsonPath("$.fileTypeId").value(DEFAULT_FILE_TYPE_ID.intValue()))
            .andExpect(jsonPath("$.dataFileContentType").value(DEFAULT_DATA_FILE_CONTENT_TYPE))
            .andExpect(jsonPath("$.dataFile").value(Base64Utils.encodeToString(DEFAULT_DATA_FILE)))
            .andExpect(jsonPath("$.uploadSuccessful").value(DEFAULT_UPLOAD_SUCCESSFUL.booleanValue()))
            .andExpect(jsonPath("$.uploadProcessed").value(DEFAULT_UPLOAD_PROCESSED.booleanValue()))
            .andExpect(jsonPath("$.uploadToken").value(DEFAULT_UPLOAD_TOKEN));
    }

    @Test
    @Transactional
    void getFileUploadsByIdFiltering() throws Exception {
        // Initialize the database
        fileUploadRepository.saveAndFlush(fileUpload);

        Long id = fileUpload.getId();

        defaultFileUploadShouldBeFound("id.equals=" + id);
        defaultFileUploadShouldNotBeFound("id.notEquals=" + id);

        defaultFileUploadShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultFileUploadShouldNotBeFound("id.greaterThan=" + id);

        defaultFileUploadShouldBeFound("id.lessThanOrEqual=" + id);
        defaultFileUploadShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllFileUploadsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        fileUploadRepository.saveAndFlush(fileUpload);

        // Get all the fileUploadList where description equals to DEFAULT_DESCRIPTION
        defaultFileUploadShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the fileUploadList where description equals to UPDATED_DESCRIPTION
        defaultFileUploadShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllFileUploadsByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        fileUploadRepository.saveAndFlush(fileUpload);

        // Get all the fileUploadList where description not equals to DEFAULT_DESCRIPTION
        defaultFileUploadShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the fileUploadList where description not equals to UPDATED_DESCRIPTION
        defaultFileUploadShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllFileUploadsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        fileUploadRepository.saveAndFlush(fileUpload);

        // Get all the fileUploadList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultFileUploadShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the fileUploadList where description equals to UPDATED_DESCRIPTION
        defaultFileUploadShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllFileUploadsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        fileUploadRepository.saveAndFlush(fileUpload);

        // Get all the fileUploadList where description is not null
        defaultFileUploadShouldBeFound("description.specified=true");

        // Get all the fileUploadList where description is null
        defaultFileUploadShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllFileUploadsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        fileUploadRepository.saveAndFlush(fileUpload);

        // Get all the fileUploadList where description contains DEFAULT_DESCRIPTION
        defaultFileUploadShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the fileUploadList where description contains UPDATED_DESCRIPTION
        defaultFileUploadShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllFileUploadsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        fileUploadRepository.saveAndFlush(fileUpload);

        // Get all the fileUploadList where description does not contain DEFAULT_DESCRIPTION
        defaultFileUploadShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the fileUploadList where description does not contain UPDATED_DESCRIPTION
        defaultFileUploadShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllFileUploadsByFileNameIsEqualToSomething() throws Exception {
        // Initialize the database
        fileUploadRepository.saveAndFlush(fileUpload);

        // Get all the fileUploadList where fileName equals to DEFAULT_FILE_NAME
        defaultFileUploadShouldBeFound("fileName.equals=" + DEFAULT_FILE_NAME);

        // Get all the fileUploadList where fileName equals to UPDATED_FILE_NAME
        defaultFileUploadShouldNotBeFound("fileName.equals=" + UPDATED_FILE_NAME);
    }

    @Test
    @Transactional
    void getAllFileUploadsByFileNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        fileUploadRepository.saveAndFlush(fileUpload);

        // Get all the fileUploadList where fileName not equals to DEFAULT_FILE_NAME
        defaultFileUploadShouldNotBeFound("fileName.notEquals=" + DEFAULT_FILE_NAME);

        // Get all the fileUploadList where fileName not equals to UPDATED_FILE_NAME
        defaultFileUploadShouldBeFound("fileName.notEquals=" + UPDATED_FILE_NAME);
    }

    @Test
    @Transactional
    void getAllFileUploadsByFileNameIsInShouldWork() throws Exception {
        // Initialize the database
        fileUploadRepository.saveAndFlush(fileUpload);

        // Get all the fileUploadList where fileName in DEFAULT_FILE_NAME or UPDATED_FILE_NAME
        defaultFileUploadShouldBeFound("fileName.in=" + DEFAULT_FILE_NAME + "," + UPDATED_FILE_NAME);

        // Get all the fileUploadList where fileName equals to UPDATED_FILE_NAME
        defaultFileUploadShouldNotBeFound("fileName.in=" + UPDATED_FILE_NAME);
    }

    @Test
    @Transactional
    void getAllFileUploadsByFileNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        fileUploadRepository.saveAndFlush(fileUpload);

        // Get all the fileUploadList where fileName is not null
        defaultFileUploadShouldBeFound("fileName.specified=true");

        // Get all the fileUploadList where fileName is null
        defaultFileUploadShouldNotBeFound("fileName.specified=false");
    }

    @Test
    @Transactional
    void getAllFileUploadsByFileNameContainsSomething() throws Exception {
        // Initialize the database
        fileUploadRepository.saveAndFlush(fileUpload);

        // Get all the fileUploadList where fileName contains DEFAULT_FILE_NAME
        defaultFileUploadShouldBeFound("fileName.contains=" + DEFAULT_FILE_NAME);

        // Get all the fileUploadList where fileName contains UPDATED_FILE_NAME
        defaultFileUploadShouldNotBeFound("fileName.contains=" + UPDATED_FILE_NAME);
    }

    @Test
    @Transactional
    void getAllFileUploadsByFileNameNotContainsSomething() throws Exception {
        // Initialize the database
        fileUploadRepository.saveAndFlush(fileUpload);

        // Get all the fileUploadList where fileName does not contain DEFAULT_FILE_NAME
        defaultFileUploadShouldNotBeFound("fileName.doesNotContain=" + DEFAULT_FILE_NAME);

        // Get all the fileUploadList where fileName does not contain UPDATED_FILE_NAME
        defaultFileUploadShouldBeFound("fileName.doesNotContain=" + UPDATED_FILE_NAME);
    }

    @Test
    @Transactional
    void getAllFileUploadsByPeriodFromIsEqualToSomething() throws Exception {
        // Initialize the database
        fileUploadRepository.saveAndFlush(fileUpload);

        // Get all the fileUploadList where periodFrom equals to DEFAULT_PERIOD_FROM
        defaultFileUploadShouldBeFound("periodFrom.equals=" + DEFAULT_PERIOD_FROM);

        // Get all the fileUploadList where periodFrom equals to UPDATED_PERIOD_FROM
        defaultFileUploadShouldNotBeFound("periodFrom.equals=" + UPDATED_PERIOD_FROM);
    }

    @Test
    @Transactional
    void getAllFileUploadsByPeriodFromIsNotEqualToSomething() throws Exception {
        // Initialize the database
        fileUploadRepository.saveAndFlush(fileUpload);

        // Get all the fileUploadList where periodFrom not equals to DEFAULT_PERIOD_FROM
        defaultFileUploadShouldNotBeFound("periodFrom.notEquals=" + DEFAULT_PERIOD_FROM);

        // Get all the fileUploadList where periodFrom not equals to UPDATED_PERIOD_FROM
        defaultFileUploadShouldBeFound("periodFrom.notEquals=" + UPDATED_PERIOD_FROM);
    }

    @Test
    @Transactional
    void getAllFileUploadsByPeriodFromIsInShouldWork() throws Exception {
        // Initialize the database
        fileUploadRepository.saveAndFlush(fileUpload);

        // Get all the fileUploadList where periodFrom in DEFAULT_PERIOD_FROM or UPDATED_PERIOD_FROM
        defaultFileUploadShouldBeFound("periodFrom.in=" + DEFAULT_PERIOD_FROM + "," + UPDATED_PERIOD_FROM);

        // Get all the fileUploadList where periodFrom equals to UPDATED_PERIOD_FROM
        defaultFileUploadShouldNotBeFound("periodFrom.in=" + UPDATED_PERIOD_FROM);
    }

    @Test
    @Transactional
    void getAllFileUploadsByPeriodFromIsNullOrNotNull() throws Exception {
        // Initialize the database
        fileUploadRepository.saveAndFlush(fileUpload);

        // Get all the fileUploadList where periodFrom is not null
        defaultFileUploadShouldBeFound("periodFrom.specified=true");

        // Get all the fileUploadList where periodFrom is null
        defaultFileUploadShouldNotBeFound("periodFrom.specified=false");
    }

    @Test
    @Transactional
    void getAllFileUploadsByPeriodFromIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        fileUploadRepository.saveAndFlush(fileUpload);

        // Get all the fileUploadList where periodFrom is greater than or equal to DEFAULT_PERIOD_FROM
        defaultFileUploadShouldBeFound("periodFrom.greaterThanOrEqual=" + DEFAULT_PERIOD_FROM);

        // Get all the fileUploadList where periodFrom is greater than or equal to UPDATED_PERIOD_FROM
        defaultFileUploadShouldNotBeFound("periodFrom.greaterThanOrEqual=" + UPDATED_PERIOD_FROM);
    }

    @Test
    @Transactional
    void getAllFileUploadsByPeriodFromIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        fileUploadRepository.saveAndFlush(fileUpload);

        // Get all the fileUploadList where periodFrom is less than or equal to DEFAULT_PERIOD_FROM
        defaultFileUploadShouldBeFound("periodFrom.lessThanOrEqual=" + DEFAULT_PERIOD_FROM);

        // Get all the fileUploadList where periodFrom is less than or equal to SMALLER_PERIOD_FROM
        defaultFileUploadShouldNotBeFound("periodFrom.lessThanOrEqual=" + SMALLER_PERIOD_FROM);
    }

    @Test
    @Transactional
    void getAllFileUploadsByPeriodFromIsLessThanSomething() throws Exception {
        // Initialize the database
        fileUploadRepository.saveAndFlush(fileUpload);

        // Get all the fileUploadList where periodFrom is less than DEFAULT_PERIOD_FROM
        defaultFileUploadShouldNotBeFound("periodFrom.lessThan=" + DEFAULT_PERIOD_FROM);

        // Get all the fileUploadList where periodFrom is less than UPDATED_PERIOD_FROM
        defaultFileUploadShouldBeFound("periodFrom.lessThan=" + UPDATED_PERIOD_FROM);
    }

    @Test
    @Transactional
    void getAllFileUploadsByPeriodFromIsGreaterThanSomething() throws Exception {
        // Initialize the database
        fileUploadRepository.saveAndFlush(fileUpload);

        // Get all the fileUploadList where periodFrom is greater than DEFAULT_PERIOD_FROM
        defaultFileUploadShouldNotBeFound("periodFrom.greaterThan=" + DEFAULT_PERIOD_FROM);

        // Get all the fileUploadList where periodFrom is greater than SMALLER_PERIOD_FROM
        defaultFileUploadShouldBeFound("periodFrom.greaterThan=" + SMALLER_PERIOD_FROM);
    }

    @Test
    @Transactional
    void getAllFileUploadsByPeriodToIsEqualToSomething() throws Exception {
        // Initialize the database
        fileUploadRepository.saveAndFlush(fileUpload);

        // Get all the fileUploadList where periodTo equals to DEFAULT_PERIOD_TO
        defaultFileUploadShouldBeFound("periodTo.equals=" + DEFAULT_PERIOD_TO);

        // Get all the fileUploadList where periodTo equals to UPDATED_PERIOD_TO
        defaultFileUploadShouldNotBeFound("periodTo.equals=" + UPDATED_PERIOD_TO);
    }

    @Test
    @Transactional
    void getAllFileUploadsByPeriodToIsNotEqualToSomething() throws Exception {
        // Initialize the database
        fileUploadRepository.saveAndFlush(fileUpload);

        // Get all the fileUploadList where periodTo not equals to DEFAULT_PERIOD_TO
        defaultFileUploadShouldNotBeFound("periodTo.notEquals=" + DEFAULT_PERIOD_TO);

        // Get all the fileUploadList where periodTo not equals to UPDATED_PERIOD_TO
        defaultFileUploadShouldBeFound("periodTo.notEquals=" + UPDATED_PERIOD_TO);
    }

    @Test
    @Transactional
    void getAllFileUploadsByPeriodToIsInShouldWork() throws Exception {
        // Initialize the database
        fileUploadRepository.saveAndFlush(fileUpload);

        // Get all the fileUploadList where periodTo in DEFAULT_PERIOD_TO or UPDATED_PERIOD_TO
        defaultFileUploadShouldBeFound("periodTo.in=" + DEFAULT_PERIOD_TO + "," + UPDATED_PERIOD_TO);

        // Get all the fileUploadList where periodTo equals to UPDATED_PERIOD_TO
        defaultFileUploadShouldNotBeFound("periodTo.in=" + UPDATED_PERIOD_TO);
    }

    @Test
    @Transactional
    void getAllFileUploadsByPeriodToIsNullOrNotNull() throws Exception {
        // Initialize the database
        fileUploadRepository.saveAndFlush(fileUpload);

        // Get all the fileUploadList where periodTo is not null
        defaultFileUploadShouldBeFound("periodTo.specified=true");

        // Get all the fileUploadList where periodTo is null
        defaultFileUploadShouldNotBeFound("periodTo.specified=false");
    }

    @Test
    @Transactional
    void getAllFileUploadsByPeriodToIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        fileUploadRepository.saveAndFlush(fileUpload);

        // Get all the fileUploadList where periodTo is greater than or equal to DEFAULT_PERIOD_TO
        defaultFileUploadShouldBeFound("periodTo.greaterThanOrEqual=" + DEFAULT_PERIOD_TO);

        // Get all the fileUploadList where periodTo is greater than or equal to UPDATED_PERIOD_TO
        defaultFileUploadShouldNotBeFound("periodTo.greaterThanOrEqual=" + UPDATED_PERIOD_TO);
    }

    @Test
    @Transactional
    void getAllFileUploadsByPeriodToIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        fileUploadRepository.saveAndFlush(fileUpload);

        // Get all the fileUploadList where periodTo is less than or equal to DEFAULT_PERIOD_TO
        defaultFileUploadShouldBeFound("periodTo.lessThanOrEqual=" + DEFAULT_PERIOD_TO);

        // Get all the fileUploadList where periodTo is less than or equal to SMALLER_PERIOD_TO
        defaultFileUploadShouldNotBeFound("periodTo.lessThanOrEqual=" + SMALLER_PERIOD_TO);
    }

    @Test
    @Transactional
    void getAllFileUploadsByPeriodToIsLessThanSomething() throws Exception {
        // Initialize the database
        fileUploadRepository.saveAndFlush(fileUpload);

        // Get all the fileUploadList where periodTo is less than DEFAULT_PERIOD_TO
        defaultFileUploadShouldNotBeFound("periodTo.lessThan=" + DEFAULT_PERIOD_TO);

        // Get all the fileUploadList where periodTo is less than UPDATED_PERIOD_TO
        defaultFileUploadShouldBeFound("periodTo.lessThan=" + UPDATED_PERIOD_TO);
    }

    @Test
    @Transactional
    void getAllFileUploadsByPeriodToIsGreaterThanSomething() throws Exception {
        // Initialize the database
        fileUploadRepository.saveAndFlush(fileUpload);

        // Get all the fileUploadList where periodTo is greater than DEFAULT_PERIOD_TO
        defaultFileUploadShouldNotBeFound("periodTo.greaterThan=" + DEFAULT_PERIOD_TO);

        // Get all the fileUploadList where periodTo is greater than SMALLER_PERIOD_TO
        defaultFileUploadShouldBeFound("periodTo.greaterThan=" + SMALLER_PERIOD_TO);
    }

    @Test
    @Transactional
    void getAllFileUploadsByFileTypeIdIsEqualToSomething() throws Exception {
        // Initialize the database
        fileUploadRepository.saveAndFlush(fileUpload);

        // Get all the fileUploadList where fileTypeId equals to DEFAULT_FILE_TYPE_ID
        defaultFileUploadShouldBeFound("fileTypeId.equals=" + DEFAULT_FILE_TYPE_ID);

        // Get all the fileUploadList where fileTypeId equals to UPDATED_FILE_TYPE_ID
        defaultFileUploadShouldNotBeFound("fileTypeId.equals=" + UPDATED_FILE_TYPE_ID);
    }

    @Test
    @Transactional
    void getAllFileUploadsByFileTypeIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        fileUploadRepository.saveAndFlush(fileUpload);

        // Get all the fileUploadList where fileTypeId not equals to DEFAULT_FILE_TYPE_ID
        defaultFileUploadShouldNotBeFound("fileTypeId.notEquals=" + DEFAULT_FILE_TYPE_ID);

        // Get all the fileUploadList where fileTypeId not equals to UPDATED_FILE_TYPE_ID
        defaultFileUploadShouldBeFound("fileTypeId.notEquals=" + UPDATED_FILE_TYPE_ID);
    }

    @Test
    @Transactional
    void getAllFileUploadsByFileTypeIdIsInShouldWork() throws Exception {
        // Initialize the database
        fileUploadRepository.saveAndFlush(fileUpload);

        // Get all the fileUploadList where fileTypeId in DEFAULT_FILE_TYPE_ID or UPDATED_FILE_TYPE_ID
        defaultFileUploadShouldBeFound("fileTypeId.in=" + DEFAULT_FILE_TYPE_ID + "," + UPDATED_FILE_TYPE_ID);

        // Get all the fileUploadList where fileTypeId equals to UPDATED_FILE_TYPE_ID
        defaultFileUploadShouldNotBeFound("fileTypeId.in=" + UPDATED_FILE_TYPE_ID);
    }

    @Test
    @Transactional
    void getAllFileUploadsByFileTypeIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        fileUploadRepository.saveAndFlush(fileUpload);

        // Get all the fileUploadList where fileTypeId is not null
        defaultFileUploadShouldBeFound("fileTypeId.specified=true");

        // Get all the fileUploadList where fileTypeId is null
        defaultFileUploadShouldNotBeFound("fileTypeId.specified=false");
    }

    @Test
    @Transactional
    void getAllFileUploadsByFileTypeIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        fileUploadRepository.saveAndFlush(fileUpload);

        // Get all the fileUploadList where fileTypeId is greater than or equal to DEFAULT_FILE_TYPE_ID
        defaultFileUploadShouldBeFound("fileTypeId.greaterThanOrEqual=" + DEFAULT_FILE_TYPE_ID);

        // Get all the fileUploadList where fileTypeId is greater than or equal to UPDATED_FILE_TYPE_ID
        defaultFileUploadShouldNotBeFound("fileTypeId.greaterThanOrEqual=" + UPDATED_FILE_TYPE_ID);
    }

    @Test
    @Transactional
    void getAllFileUploadsByFileTypeIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        fileUploadRepository.saveAndFlush(fileUpload);

        // Get all the fileUploadList where fileTypeId is less than or equal to DEFAULT_FILE_TYPE_ID
        defaultFileUploadShouldBeFound("fileTypeId.lessThanOrEqual=" + DEFAULT_FILE_TYPE_ID);

        // Get all the fileUploadList where fileTypeId is less than or equal to SMALLER_FILE_TYPE_ID
        defaultFileUploadShouldNotBeFound("fileTypeId.lessThanOrEqual=" + SMALLER_FILE_TYPE_ID);
    }

    @Test
    @Transactional
    void getAllFileUploadsByFileTypeIdIsLessThanSomething() throws Exception {
        // Initialize the database
        fileUploadRepository.saveAndFlush(fileUpload);

        // Get all the fileUploadList where fileTypeId is less than DEFAULT_FILE_TYPE_ID
        defaultFileUploadShouldNotBeFound("fileTypeId.lessThan=" + DEFAULT_FILE_TYPE_ID);

        // Get all the fileUploadList where fileTypeId is less than UPDATED_FILE_TYPE_ID
        defaultFileUploadShouldBeFound("fileTypeId.lessThan=" + UPDATED_FILE_TYPE_ID);
    }

    @Test
    @Transactional
    void getAllFileUploadsByFileTypeIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        fileUploadRepository.saveAndFlush(fileUpload);

        // Get all the fileUploadList where fileTypeId is greater than DEFAULT_FILE_TYPE_ID
        defaultFileUploadShouldNotBeFound("fileTypeId.greaterThan=" + DEFAULT_FILE_TYPE_ID);

        // Get all the fileUploadList where fileTypeId is greater than SMALLER_FILE_TYPE_ID
        defaultFileUploadShouldBeFound("fileTypeId.greaterThan=" + SMALLER_FILE_TYPE_ID);
    }

    @Test
    @Transactional
    void getAllFileUploadsByUploadSuccessfulIsEqualToSomething() throws Exception {
        // Initialize the database
        fileUploadRepository.saveAndFlush(fileUpload);

        // Get all the fileUploadList where uploadSuccessful equals to DEFAULT_UPLOAD_SUCCESSFUL
        defaultFileUploadShouldBeFound("uploadSuccessful.equals=" + DEFAULT_UPLOAD_SUCCESSFUL);

        // Get all the fileUploadList where uploadSuccessful equals to UPDATED_UPLOAD_SUCCESSFUL
        defaultFileUploadShouldNotBeFound("uploadSuccessful.equals=" + UPDATED_UPLOAD_SUCCESSFUL);
    }

    @Test
    @Transactional
    void getAllFileUploadsByUploadSuccessfulIsNotEqualToSomething() throws Exception {
        // Initialize the database
        fileUploadRepository.saveAndFlush(fileUpload);

        // Get all the fileUploadList where uploadSuccessful not equals to DEFAULT_UPLOAD_SUCCESSFUL
        defaultFileUploadShouldNotBeFound("uploadSuccessful.notEquals=" + DEFAULT_UPLOAD_SUCCESSFUL);

        // Get all the fileUploadList where uploadSuccessful not equals to UPDATED_UPLOAD_SUCCESSFUL
        defaultFileUploadShouldBeFound("uploadSuccessful.notEquals=" + UPDATED_UPLOAD_SUCCESSFUL);
    }

    @Test
    @Transactional
    void getAllFileUploadsByUploadSuccessfulIsInShouldWork() throws Exception {
        // Initialize the database
        fileUploadRepository.saveAndFlush(fileUpload);

        // Get all the fileUploadList where uploadSuccessful in DEFAULT_UPLOAD_SUCCESSFUL or UPDATED_UPLOAD_SUCCESSFUL
        defaultFileUploadShouldBeFound("uploadSuccessful.in=" + DEFAULT_UPLOAD_SUCCESSFUL + "," + UPDATED_UPLOAD_SUCCESSFUL);

        // Get all the fileUploadList where uploadSuccessful equals to UPDATED_UPLOAD_SUCCESSFUL
        defaultFileUploadShouldNotBeFound("uploadSuccessful.in=" + UPDATED_UPLOAD_SUCCESSFUL);
    }

    @Test
    @Transactional
    void getAllFileUploadsByUploadSuccessfulIsNullOrNotNull() throws Exception {
        // Initialize the database
        fileUploadRepository.saveAndFlush(fileUpload);

        // Get all the fileUploadList where uploadSuccessful is not null
        defaultFileUploadShouldBeFound("uploadSuccessful.specified=true");

        // Get all the fileUploadList where uploadSuccessful is null
        defaultFileUploadShouldNotBeFound("uploadSuccessful.specified=false");
    }

    @Test
    @Transactional
    void getAllFileUploadsByUploadProcessedIsEqualToSomething() throws Exception {
        // Initialize the database
        fileUploadRepository.saveAndFlush(fileUpload);

        // Get all the fileUploadList where uploadProcessed equals to DEFAULT_UPLOAD_PROCESSED
        defaultFileUploadShouldBeFound("uploadProcessed.equals=" + DEFAULT_UPLOAD_PROCESSED);

        // Get all the fileUploadList where uploadProcessed equals to UPDATED_UPLOAD_PROCESSED
        defaultFileUploadShouldNotBeFound("uploadProcessed.equals=" + UPDATED_UPLOAD_PROCESSED);
    }

    @Test
    @Transactional
    void getAllFileUploadsByUploadProcessedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        fileUploadRepository.saveAndFlush(fileUpload);

        // Get all the fileUploadList where uploadProcessed not equals to DEFAULT_UPLOAD_PROCESSED
        defaultFileUploadShouldNotBeFound("uploadProcessed.notEquals=" + DEFAULT_UPLOAD_PROCESSED);

        // Get all the fileUploadList where uploadProcessed not equals to UPDATED_UPLOAD_PROCESSED
        defaultFileUploadShouldBeFound("uploadProcessed.notEquals=" + UPDATED_UPLOAD_PROCESSED);
    }

    @Test
    @Transactional
    void getAllFileUploadsByUploadProcessedIsInShouldWork() throws Exception {
        // Initialize the database
        fileUploadRepository.saveAndFlush(fileUpload);

        // Get all the fileUploadList where uploadProcessed in DEFAULT_UPLOAD_PROCESSED or UPDATED_UPLOAD_PROCESSED
        defaultFileUploadShouldBeFound("uploadProcessed.in=" + DEFAULT_UPLOAD_PROCESSED + "," + UPDATED_UPLOAD_PROCESSED);

        // Get all the fileUploadList where uploadProcessed equals to UPDATED_UPLOAD_PROCESSED
        defaultFileUploadShouldNotBeFound("uploadProcessed.in=" + UPDATED_UPLOAD_PROCESSED);
    }

    @Test
    @Transactional
    void getAllFileUploadsByUploadProcessedIsNullOrNotNull() throws Exception {
        // Initialize the database
        fileUploadRepository.saveAndFlush(fileUpload);

        // Get all the fileUploadList where uploadProcessed is not null
        defaultFileUploadShouldBeFound("uploadProcessed.specified=true");

        // Get all the fileUploadList where uploadProcessed is null
        defaultFileUploadShouldNotBeFound("uploadProcessed.specified=false");
    }

    @Test
    @Transactional
    void getAllFileUploadsByUploadTokenIsEqualToSomething() throws Exception {
        // Initialize the database
        fileUploadRepository.saveAndFlush(fileUpload);

        // Get all the fileUploadList where uploadToken equals to DEFAULT_UPLOAD_TOKEN
        defaultFileUploadShouldBeFound("uploadToken.equals=" + DEFAULT_UPLOAD_TOKEN);

        // Get all the fileUploadList where uploadToken equals to UPDATED_UPLOAD_TOKEN
        defaultFileUploadShouldNotBeFound("uploadToken.equals=" + UPDATED_UPLOAD_TOKEN);
    }

    @Test
    @Transactional
    void getAllFileUploadsByUploadTokenIsNotEqualToSomething() throws Exception {
        // Initialize the database
        fileUploadRepository.saveAndFlush(fileUpload);

        // Get all the fileUploadList where uploadToken not equals to DEFAULT_UPLOAD_TOKEN
        defaultFileUploadShouldNotBeFound("uploadToken.notEquals=" + DEFAULT_UPLOAD_TOKEN);

        // Get all the fileUploadList where uploadToken not equals to UPDATED_UPLOAD_TOKEN
        defaultFileUploadShouldBeFound("uploadToken.notEquals=" + UPDATED_UPLOAD_TOKEN);
    }

    @Test
    @Transactional
    void getAllFileUploadsByUploadTokenIsInShouldWork() throws Exception {
        // Initialize the database
        fileUploadRepository.saveAndFlush(fileUpload);

        // Get all the fileUploadList where uploadToken in DEFAULT_UPLOAD_TOKEN or UPDATED_UPLOAD_TOKEN
        defaultFileUploadShouldBeFound("uploadToken.in=" + DEFAULT_UPLOAD_TOKEN + "," + UPDATED_UPLOAD_TOKEN);

        // Get all the fileUploadList where uploadToken equals to UPDATED_UPLOAD_TOKEN
        defaultFileUploadShouldNotBeFound("uploadToken.in=" + UPDATED_UPLOAD_TOKEN);
    }

    @Test
    @Transactional
    void getAllFileUploadsByUploadTokenIsNullOrNotNull() throws Exception {
        // Initialize the database
        fileUploadRepository.saveAndFlush(fileUpload);

        // Get all the fileUploadList where uploadToken is not null
        defaultFileUploadShouldBeFound("uploadToken.specified=true");

        // Get all the fileUploadList where uploadToken is null
        defaultFileUploadShouldNotBeFound("uploadToken.specified=false");
    }

    @Test
    @Transactional
    void getAllFileUploadsByUploadTokenContainsSomething() throws Exception {
        // Initialize the database
        fileUploadRepository.saveAndFlush(fileUpload);

        // Get all the fileUploadList where uploadToken contains DEFAULT_UPLOAD_TOKEN
        defaultFileUploadShouldBeFound("uploadToken.contains=" + DEFAULT_UPLOAD_TOKEN);

        // Get all the fileUploadList where uploadToken contains UPDATED_UPLOAD_TOKEN
        defaultFileUploadShouldNotBeFound("uploadToken.contains=" + UPDATED_UPLOAD_TOKEN);
    }

    @Test
    @Transactional
    void getAllFileUploadsByUploadTokenNotContainsSomething() throws Exception {
        // Initialize the database
        fileUploadRepository.saveAndFlush(fileUpload);

        // Get all the fileUploadList where uploadToken does not contain DEFAULT_UPLOAD_TOKEN
        defaultFileUploadShouldNotBeFound("uploadToken.doesNotContain=" + DEFAULT_UPLOAD_TOKEN);

        // Get all the fileUploadList where uploadToken does not contain UPDATED_UPLOAD_TOKEN
        defaultFileUploadShouldBeFound("uploadToken.doesNotContain=" + UPDATED_UPLOAD_TOKEN);
    }

    @Test
    @Transactional
    void getAllFileUploadsByPlaceholderIsEqualToSomething() throws Exception {
        // Initialize the database
        fileUploadRepository.saveAndFlush(fileUpload);
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
        fileUpload.addPlaceholder(placeholder);
        fileUploadRepository.saveAndFlush(fileUpload);
        Long placeholderId = placeholder.getId();

        // Get all the fileUploadList where placeholder equals to placeholderId
        defaultFileUploadShouldBeFound("placeholderId.equals=" + placeholderId);

        // Get all the fileUploadList where placeholder equals to (placeholderId + 1)
        defaultFileUploadShouldNotBeFound("placeholderId.equals=" + (placeholderId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultFileUploadShouldBeFound(String filter) throws Exception {
        restFileUploadMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fileUpload.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].fileName").value(hasItem(DEFAULT_FILE_NAME)))
            .andExpect(jsonPath("$.[*].periodFrom").value(hasItem(DEFAULT_PERIOD_FROM.toString())))
            .andExpect(jsonPath("$.[*].periodTo").value(hasItem(DEFAULT_PERIOD_TO.toString())))
            .andExpect(jsonPath("$.[*].fileTypeId").value(hasItem(DEFAULT_FILE_TYPE_ID.intValue())))
            .andExpect(jsonPath("$.[*].dataFileContentType").value(hasItem(DEFAULT_DATA_FILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].dataFile").value(hasItem(Base64Utils.encodeToString(DEFAULT_DATA_FILE))))
            .andExpect(jsonPath("$.[*].uploadSuccessful").value(hasItem(DEFAULT_UPLOAD_SUCCESSFUL.booleanValue())))
            .andExpect(jsonPath("$.[*].uploadProcessed").value(hasItem(DEFAULT_UPLOAD_PROCESSED.booleanValue())))
            .andExpect(jsonPath("$.[*].uploadToken").value(hasItem(DEFAULT_UPLOAD_TOKEN)));

        // Check, that the count call also returns 1
        restFileUploadMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultFileUploadShouldNotBeFound(String filter) throws Exception {
        restFileUploadMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restFileUploadMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingFileUpload() throws Exception {
        // Get the fileUpload
        restFileUploadMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewFileUpload() throws Exception {
        // Initialize the database
        fileUploadRepository.saveAndFlush(fileUpload);

        int databaseSizeBeforeUpdate = fileUploadRepository.findAll().size();

        // Update the fileUpload
        FileUpload updatedFileUpload = fileUploadRepository.findById(fileUpload.getId()).get();
        // Disconnect from session so that the updates on updatedFileUpload are not directly saved in db
        em.detach(updatedFileUpload);
        updatedFileUpload
            .description(UPDATED_DESCRIPTION)
            .fileName(UPDATED_FILE_NAME)
            .periodFrom(UPDATED_PERIOD_FROM)
            .periodTo(UPDATED_PERIOD_TO)
            .fileTypeId(UPDATED_FILE_TYPE_ID)
            .dataFile(UPDATED_DATA_FILE)
            .dataFileContentType(UPDATED_DATA_FILE_CONTENT_TYPE)
            .uploadSuccessful(UPDATED_UPLOAD_SUCCESSFUL)
            .uploadProcessed(UPDATED_UPLOAD_PROCESSED)
            .uploadToken(UPDATED_UPLOAD_TOKEN);
        FileUploadDTO fileUploadDTO = fileUploadMapper.toDto(updatedFileUpload);

        restFileUploadMockMvc
            .perform(
                put(ENTITY_API_URL_ID, fileUploadDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fileUploadDTO))
            )
            .andExpect(status().isOk());

        // Validate the FileUpload in the database
        List<FileUpload> fileUploadList = fileUploadRepository.findAll();
        assertThat(fileUploadList).hasSize(databaseSizeBeforeUpdate);
        FileUpload testFileUpload = fileUploadList.get(fileUploadList.size() - 1);
        assertThat(testFileUpload.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testFileUpload.getFileName()).isEqualTo(UPDATED_FILE_NAME);
        assertThat(testFileUpload.getPeriodFrom()).isEqualTo(UPDATED_PERIOD_FROM);
        assertThat(testFileUpload.getPeriodTo()).isEqualTo(UPDATED_PERIOD_TO);
        assertThat(testFileUpload.getFileTypeId()).isEqualTo(UPDATED_FILE_TYPE_ID);
        assertThat(testFileUpload.getDataFile()).isEqualTo(UPDATED_DATA_FILE);
        assertThat(testFileUpload.getDataFileContentType()).isEqualTo(UPDATED_DATA_FILE_CONTENT_TYPE);
        assertThat(testFileUpload.getUploadSuccessful()).isEqualTo(UPDATED_UPLOAD_SUCCESSFUL);
        assertThat(testFileUpload.getUploadProcessed()).isEqualTo(UPDATED_UPLOAD_PROCESSED);
        assertThat(testFileUpload.getUploadToken()).isEqualTo(UPDATED_UPLOAD_TOKEN);

        // Validate the FileUpload in Elasticsearch
        verify(mockFileUploadSearchRepository).save(testFileUpload);
    }

    @Test
    @Transactional
    void putNonExistingFileUpload() throws Exception {
        int databaseSizeBeforeUpdate = fileUploadRepository.findAll().size();
        fileUpload.setId(count.incrementAndGet());

        // Create the FileUpload
        FileUploadDTO fileUploadDTO = fileUploadMapper.toDto(fileUpload);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFileUploadMockMvc
            .perform(
                put(ENTITY_API_URL_ID, fileUploadDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fileUploadDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FileUpload in the database
        List<FileUpload> fileUploadList = fileUploadRepository.findAll();
        assertThat(fileUploadList).hasSize(databaseSizeBeforeUpdate);

        // Validate the FileUpload in Elasticsearch
        verify(mockFileUploadSearchRepository, times(0)).save(fileUpload);
    }

    @Test
    @Transactional
    void putWithIdMismatchFileUpload() throws Exception {
        int databaseSizeBeforeUpdate = fileUploadRepository.findAll().size();
        fileUpload.setId(count.incrementAndGet());

        // Create the FileUpload
        FileUploadDTO fileUploadDTO = fileUploadMapper.toDto(fileUpload);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFileUploadMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fileUploadDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FileUpload in the database
        List<FileUpload> fileUploadList = fileUploadRepository.findAll();
        assertThat(fileUploadList).hasSize(databaseSizeBeforeUpdate);

        // Validate the FileUpload in Elasticsearch
        verify(mockFileUploadSearchRepository, times(0)).save(fileUpload);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFileUpload() throws Exception {
        int databaseSizeBeforeUpdate = fileUploadRepository.findAll().size();
        fileUpload.setId(count.incrementAndGet());

        // Create the FileUpload
        FileUploadDTO fileUploadDTO = fileUploadMapper.toDto(fileUpload);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFileUploadMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fileUploadDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the FileUpload in the database
        List<FileUpload> fileUploadList = fileUploadRepository.findAll();
        assertThat(fileUploadList).hasSize(databaseSizeBeforeUpdate);

        // Validate the FileUpload in Elasticsearch
        verify(mockFileUploadSearchRepository, times(0)).save(fileUpload);
    }

    @Test
    @Transactional
    void partialUpdateFileUploadWithPatch() throws Exception {
        // Initialize the database
        fileUploadRepository.saveAndFlush(fileUpload);

        int databaseSizeBeforeUpdate = fileUploadRepository.findAll().size();

        // Update the fileUpload using partial update
        FileUpload partialUpdatedFileUpload = new FileUpload();
        partialUpdatedFileUpload.setId(fileUpload.getId());

        partialUpdatedFileUpload
            .fileName(UPDATED_FILE_NAME)
            .periodFrom(UPDATED_PERIOD_FROM)
            .fileTypeId(UPDATED_FILE_TYPE_ID)
            .dataFile(UPDATED_DATA_FILE)
            .dataFileContentType(UPDATED_DATA_FILE_CONTENT_TYPE)
            .uploadSuccessful(UPDATED_UPLOAD_SUCCESSFUL)
            .uploadProcessed(UPDATED_UPLOAD_PROCESSED)
            .uploadToken(UPDATED_UPLOAD_TOKEN);

        restFileUploadMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFileUpload.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFileUpload))
            )
            .andExpect(status().isOk());

        // Validate the FileUpload in the database
        List<FileUpload> fileUploadList = fileUploadRepository.findAll();
        assertThat(fileUploadList).hasSize(databaseSizeBeforeUpdate);
        FileUpload testFileUpload = fileUploadList.get(fileUploadList.size() - 1);
        assertThat(testFileUpload.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testFileUpload.getFileName()).isEqualTo(UPDATED_FILE_NAME);
        assertThat(testFileUpload.getPeriodFrom()).isEqualTo(UPDATED_PERIOD_FROM);
        assertThat(testFileUpload.getPeriodTo()).isEqualTo(DEFAULT_PERIOD_TO);
        assertThat(testFileUpload.getFileTypeId()).isEqualTo(UPDATED_FILE_TYPE_ID);
        assertThat(testFileUpload.getDataFile()).isEqualTo(UPDATED_DATA_FILE);
        assertThat(testFileUpload.getDataFileContentType()).isEqualTo(UPDATED_DATA_FILE_CONTENT_TYPE);
        assertThat(testFileUpload.getUploadSuccessful()).isEqualTo(UPDATED_UPLOAD_SUCCESSFUL);
        assertThat(testFileUpload.getUploadProcessed()).isEqualTo(UPDATED_UPLOAD_PROCESSED);
        assertThat(testFileUpload.getUploadToken()).isEqualTo(UPDATED_UPLOAD_TOKEN);
    }

    @Test
    @Transactional
    void fullUpdateFileUploadWithPatch() throws Exception {
        // Initialize the database
        fileUploadRepository.saveAndFlush(fileUpload);

        int databaseSizeBeforeUpdate = fileUploadRepository.findAll().size();

        // Update the fileUpload using partial update
        FileUpload partialUpdatedFileUpload = new FileUpload();
        partialUpdatedFileUpload.setId(fileUpload.getId());

        partialUpdatedFileUpload
            .description(UPDATED_DESCRIPTION)
            .fileName(UPDATED_FILE_NAME)
            .periodFrom(UPDATED_PERIOD_FROM)
            .periodTo(UPDATED_PERIOD_TO)
            .fileTypeId(UPDATED_FILE_TYPE_ID)
            .dataFile(UPDATED_DATA_FILE)
            .dataFileContentType(UPDATED_DATA_FILE_CONTENT_TYPE)
            .uploadSuccessful(UPDATED_UPLOAD_SUCCESSFUL)
            .uploadProcessed(UPDATED_UPLOAD_PROCESSED)
            .uploadToken(UPDATED_UPLOAD_TOKEN);

        restFileUploadMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFileUpload.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFileUpload))
            )
            .andExpect(status().isOk());

        // Validate the FileUpload in the database
        List<FileUpload> fileUploadList = fileUploadRepository.findAll();
        assertThat(fileUploadList).hasSize(databaseSizeBeforeUpdate);
        FileUpload testFileUpload = fileUploadList.get(fileUploadList.size() - 1);
        assertThat(testFileUpload.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testFileUpload.getFileName()).isEqualTo(UPDATED_FILE_NAME);
        assertThat(testFileUpload.getPeriodFrom()).isEqualTo(UPDATED_PERIOD_FROM);
        assertThat(testFileUpload.getPeriodTo()).isEqualTo(UPDATED_PERIOD_TO);
        assertThat(testFileUpload.getFileTypeId()).isEqualTo(UPDATED_FILE_TYPE_ID);
        assertThat(testFileUpload.getDataFile()).isEqualTo(UPDATED_DATA_FILE);
        assertThat(testFileUpload.getDataFileContentType()).isEqualTo(UPDATED_DATA_FILE_CONTENT_TYPE);
        assertThat(testFileUpload.getUploadSuccessful()).isEqualTo(UPDATED_UPLOAD_SUCCESSFUL);
        assertThat(testFileUpload.getUploadProcessed()).isEqualTo(UPDATED_UPLOAD_PROCESSED);
        assertThat(testFileUpload.getUploadToken()).isEqualTo(UPDATED_UPLOAD_TOKEN);
    }

    @Test
    @Transactional
    void patchNonExistingFileUpload() throws Exception {
        int databaseSizeBeforeUpdate = fileUploadRepository.findAll().size();
        fileUpload.setId(count.incrementAndGet());

        // Create the FileUpload
        FileUploadDTO fileUploadDTO = fileUploadMapper.toDto(fileUpload);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFileUploadMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, fileUploadDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(fileUploadDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FileUpload in the database
        List<FileUpload> fileUploadList = fileUploadRepository.findAll();
        assertThat(fileUploadList).hasSize(databaseSizeBeforeUpdate);

        // Validate the FileUpload in Elasticsearch
        verify(mockFileUploadSearchRepository, times(0)).save(fileUpload);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFileUpload() throws Exception {
        int databaseSizeBeforeUpdate = fileUploadRepository.findAll().size();
        fileUpload.setId(count.incrementAndGet());

        // Create the FileUpload
        FileUploadDTO fileUploadDTO = fileUploadMapper.toDto(fileUpload);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFileUploadMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(fileUploadDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FileUpload in the database
        List<FileUpload> fileUploadList = fileUploadRepository.findAll();
        assertThat(fileUploadList).hasSize(databaseSizeBeforeUpdate);

        // Validate the FileUpload in Elasticsearch
        verify(mockFileUploadSearchRepository, times(0)).save(fileUpload);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFileUpload() throws Exception {
        int databaseSizeBeforeUpdate = fileUploadRepository.findAll().size();
        fileUpload.setId(count.incrementAndGet());

        // Create the FileUpload
        FileUploadDTO fileUploadDTO = fileUploadMapper.toDto(fileUpload);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFileUploadMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(fileUploadDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FileUpload in the database
        List<FileUpload> fileUploadList = fileUploadRepository.findAll();
        assertThat(fileUploadList).hasSize(databaseSizeBeforeUpdate);

        // Validate the FileUpload in Elasticsearch
        verify(mockFileUploadSearchRepository, times(0)).save(fileUpload);
    }

    @Test
    @Transactional
    void deleteFileUpload() throws Exception {
        // Initialize the database
        fileUploadRepository.saveAndFlush(fileUpload);

        int databaseSizeBeforeDelete = fileUploadRepository.findAll().size();

        // Delete the fileUpload
        restFileUploadMockMvc
            .perform(delete(ENTITY_API_URL_ID, fileUpload.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<FileUpload> fileUploadList = fileUploadRepository.findAll();
        assertThat(fileUploadList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the FileUpload in Elasticsearch
        verify(mockFileUploadSearchRepository, times(1)).deleteById(fileUpload.getId());
    }

    @Test
    @Transactional
    void searchFileUpload() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        fileUploadRepository.saveAndFlush(fileUpload);
        when(mockFileUploadSearchRepository.search("id:" + fileUpload.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(fileUpload), PageRequest.of(0, 1), 1));

        // Search the fileUpload
        restFileUploadMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + fileUpload.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fileUpload.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].fileName").value(hasItem(DEFAULT_FILE_NAME)))
            .andExpect(jsonPath("$.[*].periodFrom").value(hasItem(DEFAULT_PERIOD_FROM.toString())))
            .andExpect(jsonPath("$.[*].periodTo").value(hasItem(DEFAULT_PERIOD_TO.toString())))
            .andExpect(jsonPath("$.[*].fileTypeId").value(hasItem(DEFAULT_FILE_TYPE_ID.intValue())))
            .andExpect(jsonPath("$.[*].dataFileContentType").value(hasItem(DEFAULT_DATA_FILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].dataFile").value(hasItem(Base64Utils.encodeToString(DEFAULT_DATA_FILE))))
            .andExpect(jsonPath("$.[*].uploadSuccessful").value(hasItem(DEFAULT_UPLOAD_SUCCESSFUL.booleanValue())))
            .andExpect(jsonPath("$.[*].uploadProcessed").value(hasItem(DEFAULT_UPLOAD_PROCESSED.booleanValue())))
            .andExpect(jsonPath("$.[*].uploadToken").value(hasItem(DEFAULT_UPLOAD_TOKEN)));
    }
}
