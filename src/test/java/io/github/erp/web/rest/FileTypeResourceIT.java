package io.github.erp.web.rest;

/*-
 * Erp System - Mark VI No 1 (Phoebe Series) Server ver 1.5.2
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import io.github.erp.IntegrationTest;
import io.github.erp.domain.FileType;
import io.github.erp.domain.Placeholder;
import io.github.erp.domain.enumeration.FileMediumTypes;
import io.github.erp.domain.enumeration.FileModelType;
import io.github.erp.repository.FileTypeRepository;
import io.github.erp.repository.search.FileTypeSearchRepository;
import io.github.erp.service.FileTypeService;
import io.github.erp.service.criteria.FileTypeCriteria;
import io.github.erp.service.dto.FileTypeDTO;
import io.github.erp.service.mapper.FileTypeMapper;
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
 * Integration tests for the {@link FileTypeResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class FileTypeResourceIT {

    private static final String DEFAULT_FILE_TYPE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FILE_TYPE_NAME = "BBBBBBBBBB";

    private static final FileMediumTypes DEFAULT_FILE_MEDIUM_TYPE = FileMediumTypes.EXCEL;
    private static final FileMediumTypes UPDATED_FILE_MEDIUM_TYPE = FileMediumTypes.EXCEL_XLS;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final byte[] DEFAULT_FILE_TEMPLATE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_FILE_TEMPLATE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_FILE_TEMPLATE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_FILE_TEMPLATE_CONTENT_TYPE = "image/png";

    private static final FileModelType DEFAULT_FILE_TYPE = FileModelType.CURRENCY_LIST;
    private static final FileModelType UPDATED_FILE_TYPE = FileModelType.FIXED_ASSET_ACQUISITION;

    private static final String ENTITY_API_URL = "/api/file-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/file-types";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FileTypeRepository fileTypeRepository;

    @Mock
    private FileTypeRepository fileTypeRepositoryMock;

    @Autowired
    private FileTypeMapper fileTypeMapper;

    @Mock
    private FileTypeService fileTypeServiceMock;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.FileTypeSearchRepositoryMockConfiguration
     */
    @Autowired
    private FileTypeSearchRepository mockFileTypeSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFileTypeMockMvc;

    private FileType fileType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FileType createEntity(EntityManager em) {
        FileType fileType = new FileType()
            .fileTypeName(DEFAULT_FILE_TYPE_NAME)
            .fileMediumType(DEFAULT_FILE_MEDIUM_TYPE)
            .description(DEFAULT_DESCRIPTION)
            .fileTemplate(DEFAULT_FILE_TEMPLATE)
            .fileTemplateContentType(DEFAULT_FILE_TEMPLATE_CONTENT_TYPE)
            .fileType(DEFAULT_FILE_TYPE);
        return fileType;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FileType createUpdatedEntity(EntityManager em) {
        FileType fileType = new FileType()
            .fileTypeName(UPDATED_FILE_TYPE_NAME)
            .fileMediumType(UPDATED_FILE_MEDIUM_TYPE)
            .description(UPDATED_DESCRIPTION)
            .fileTemplate(UPDATED_FILE_TEMPLATE)
            .fileTemplateContentType(UPDATED_FILE_TEMPLATE_CONTENT_TYPE)
            .fileType(UPDATED_FILE_TYPE);
        return fileType;
    }

    @BeforeEach
    public void initTest() {
        fileType = createEntity(em);
    }

    @Test
    @Transactional
    void createFileType() throws Exception {
        int databaseSizeBeforeCreate = fileTypeRepository.findAll().size();
        // Create the FileType
        FileTypeDTO fileTypeDTO = fileTypeMapper.toDto(fileType);
        restFileTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fileTypeDTO)))
            .andExpect(status().isCreated());

        // Validate the FileType in the database
        List<FileType> fileTypeList = fileTypeRepository.findAll();
        assertThat(fileTypeList).hasSize(databaseSizeBeforeCreate + 1);
        FileType testFileType = fileTypeList.get(fileTypeList.size() - 1);
        assertThat(testFileType.getFileTypeName()).isEqualTo(DEFAULT_FILE_TYPE_NAME);
        assertThat(testFileType.getFileMediumType()).isEqualTo(DEFAULT_FILE_MEDIUM_TYPE);
        assertThat(testFileType.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testFileType.getFileTemplate()).isEqualTo(DEFAULT_FILE_TEMPLATE);
        assertThat(testFileType.getFileTemplateContentType()).isEqualTo(DEFAULT_FILE_TEMPLATE_CONTENT_TYPE);
        assertThat(testFileType.getFileType()).isEqualTo(DEFAULT_FILE_TYPE);

        // Validate the FileType in Elasticsearch
        verify(mockFileTypeSearchRepository, times(1)).save(testFileType);
    }

    @Test
    @Transactional
    void createFileTypeWithExistingId() throws Exception {
        // Create the FileType with an existing ID
        fileType.setId(1L);
        FileTypeDTO fileTypeDTO = fileTypeMapper.toDto(fileType);

        int databaseSizeBeforeCreate = fileTypeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFileTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fileTypeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the FileType in the database
        List<FileType> fileTypeList = fileTypeRepository.findAll();
        assertThat(fileTypeList).hasSize(databaseSizeBeforeCreate);

        // Validate the FileType in Elasticsearch
        verify(mockFileTypeSearchRepository, times(0)).save(fileType);
    }

    @Test
    @Transactional
    void checkFileTypeNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = fileTypeRepository.findAll().size();
        // set the field null
        fileType.setFileTypeName(null);

        // Create the FileType, which fails.
        FileTypeDTO fileTypeDTO = fileTypeMapper.toDto(fileType);

        restFileTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fileTypeDTO)))
            .andExpect(status().isBadRequest());

        List<FileType> fileTypeList = fileTypeRepository.findAll();
        assertThat(fileTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFileMediumTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = fileTypeRepository.findAll().size();
        // set the field null
        fileType.setFileMediumType(null);

        // Create the FileType, which fails.
        FileTypeDTO fileTypeDTO = fileTypeMapper.toDto(fileType);

        restFileTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fileTypeDTO)))
            .andExpect(status().isBadRequest());

        List<FileType> fileTypeList = fileTypeRepository.findAll();
        assertThat(fileTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFileTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = fileTypeRepository.findAll().size();
        // set the field null
        fileType.setFileType(null);

        // Create the FileType, which fails.
        FileTypeDTO fileTypeDTO = fileTypeMapper.toDto(fileType);

        restFileTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fileTypeDTO)))
            .andExpect(status().isBadRequest());

        List<FileType> fileTypeList = fileTypeRepository.findAll();
        assertThat(fileTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllFileTypes() throws Exception {
        // Initialize the database
        fileTypeRepository.saveAndFlush(fileType);

        // Get all the fileTypeList
        restFileTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fileType.getId().intValue())))
            .andExpect(jsonPath("$.[*].fileTypeName").value(hasItem(DEFAULT_FILE_TYPE_NAME)))
            .andExpect(jsonPath("$.[*].fileMediumType").value(hasItem(DEFAULT_FILE_MEDIUM_TYPE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].fileTemplateContentType").value(hasItem(DEFAULT_FILE_TEMPLATE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].fileTemplate").value(hasItem(Base64Utils.encodeToString(DEFAULT_FILE_TEMPLATE))))
            .andExpect(jsonPath("$.[*].fileType").value(hasItem(DEFAULT_FILE_TYPE.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllFileTypesWithEagerRelationshipsIsEnabled() throws Exception {
        when(fileTypeServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restFileTypeMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(fileTypeServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllFileTypesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(fileTypeServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restFileTypeMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(fileTypeServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getFileType() throws Exception {
        // Initialize the database
        fileTypeRepository.saveAndFlush(fileType);

        // Get the fileType
        restFileTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, fileType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(fileType.getId().intValue()))
            .andExpect(jsonPath("$.fileTypeName").value(DEFAULT_FILE_TYPE_NAME))
            .andExpect(jsonPath("$.fileMediumType").value(DEFAULT_FILE_MEDIUM_TYPE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.fileTemplateContentType").value(DEFAULT_FILE_TEMPLATE_CONTENT_TYPE))
            .andExpect(jsonPath("$.fileTemplate").value(Base64Utils.encodeToString(DEFAULT_FILE_TEMPLATE)))
            .andExpect(jsonPath("$.fileType").value(DEFAULT_FILE_TYPE.toString()));
    }

    @Test
    @Transactional
    void getFileTypesByIdFiltering() throws Exception {
        // Initialize the database
        fileTypeRepository.saveAndFlush(fileType);

        Long id = fileType.getId();

        defaultFileTypeShouldBeFound("id.equals=" + id);
        defaultFileTypeShouldNotBeFound("id.notEquals=" + id);

        defaultFileTypeShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultFileTypeShouldNotBeFound("id.greaterThan=" + id);

        defaultFileTypeShouldBeFound("id.lessThanOrEqual=" + id);
        defaultFileTypeShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllFileTypesByFileTypeNameIsEqualToSomething() throws Exception {
        // Initialize the database
        fileTypeRepository.saveAndFlush(fileType);

        // Get all the fileTypeList where fileTypeName equals to DEFAULT_FILE_TYPE_NAME
        defaultFileTypeShouldBeFound("fileTypeName.equals=" + DEFAULT_FILE_TYPE_NAME);

        // Get all the fileTypeList where fileTypeName equals to UPDATED_FILE_TYPE_NAME
        defaultFileTypeShouldNotBeFound("fileTypeName.equals=" + UPDATED_FILE_TYPE_NAME);
    }

    @Test
    @Transactional
    void getAllFileTypesByFileTypeNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        fileTypeRepository.saveAndFlush(fileType);

        // Get all the fileTypeList where fileTypeName not equals to DEFAULT_FILE_TYPE_NAME
        defaultFileTypeShouldNotBeFound("fileTypeName.notEquals=" + DEFAULT_FILE_TYPE_NAME);

        // Get all the fileTypeList where fileTypeName not equals to UPDATED_FILE_TYPE_NAME
        defaultFileTypeShouldBeFound("fileTypeName.notEquals=" + UPDATED_FILE_TYPE_NAME);
    }

    @Test
    @Transactional
    void getAllFileTypesByFileTypeNameIsInShouldWork() throws Exception {
        // Initialize the database
        fileTypeRepository.saveAndFlush(fileType);

        // Get all the fileTypeList where fileTypeName in DEFAULT_FILE_TYPE_NAME or UPDATED_FILE_TYPE_NAME
        defaultFileTypeShouldBeFound("fileTypeName.in=" + DEFAULT_FILE_TYPE_NAME + "," + UPDATED_FILE_TYPE_NAME);

        // Get all the fileTypeList where fileTypeName equals to UPDATED_FILE_TYPE_NAME
        defaultFileTypeShouldNotBeFound("fileTypeName.in=" + UPDATED_FILE_TYPE_NAME);
    }

    @Test
    @Transactional
    void getAllFileTypesByFileTypeNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        fileTypeRepository.saveAndFlush(fileType);

        // Get all the fileTypeList where fileTypeName is not null
        defaultFileTypeShouldBeFound("fileTypeName.specified=true");

        // Get all the fileTypeList where fileTypeName is null
        defaultFileTypeShouldNotBeFound("fileTypeName.specified=false");
    }

    @Test
    @Transactional
    void getAllFileTypesByFileTypeNameContainsSomething() throws Exception {
        // Initialize the database
        fileTypeRepository.saveAndFlush(fileType);

        // Get all the fileTypeList where fileTypeName contains DEFAULT_FILE_TYPE_NAME
        defaultFileTypeShouldBeFound("fileTypeName.contains=" + DEFAULT_FILE_TYPE_NAME);

        // Get all the fileTypeList where fileTypeName contains UPDATED_FILE_TYPE_NAME
        defaultFileTypeShouldNotBeFound("fileTypeName.contains=" + UPDATED_FILE_TYPE_NAME);
    }

    @Test
    @Transactional
    void getAllFileTypesByFileTypeNameNotContainsSomething() throws Exception {
        // Initialize the database
        fileTypeRepository.saveAndFlush(fileType);

        // Get all the fileTypeList where fileTypeName does not contain DEFAULT_FILE_TYPE_NAME
        defaultFileTypeShouldNotBeFound("fileTypeName.doesNotContain=" + DEFAULT_FILE_TYPE_NAME);

        // Get all the fileTypeList where fileTypeName does not contain UPDATED_FILE_TYPE_NAME
        defaultFileTypeShouldBeFound("fileTypeName.doesNotContain=" + UPDATED_FILE_TYPE_NAME);
    }

    @Test
    @Transactional
    void getAllFileTypesByFileMediumTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        fileTypeRepository.saveAndFlush(fileType);

        // Get all the fileTypeList where fileMediumType equals to DEFAULT_FILE_MEDIUM_TYPE
        defaultFileTypeShouldBeFound("fileMediumType.equals=" + DEFAULT_FILE_MEDIUM_TYPE);

        // Get all the fileTypeList where fileMediumType equals to UPDATED_FILE_MEDIUM_TYPE
        defaultFileTypeShouldNotBeFound("fileMediumType.equals=" + UPDATED_FILE_MEDIUM_TYPE);
    }

    @Test
    @Transactional
    void getAllFileTypesByFileMediumTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        fileTypeRepository.saveAndFlush(fileType);

        // Get all the fileTypeList where fileMediumType not equals to DEFAULT_FILE_MEDIUM_TYPE
        defaultFileTypeShouldNotBeFound("fileMediumType.notEquals=" + DEFAULT_FILE_MEDIUM_TYPE);

        // Get all the fileTypeList where fileMediumType not equals to UPDATED_FILE_MEDIUM_TYPE
        defaultFileTypeShouldBeFound("fileMediumType.notEquals=" + UPDATED_FILE_MEDIUM_TYPE);
    }

    @Test
    @Transactional
    void getAllFileTypesByFileMediumTypeIsInShouldWork() throws Exception {
        // Initialize the database
        fileTypeRepository.saveAndFlush(fileType);

        // Get all the fileTypeList where fileMediumType in DEFAULT_FILE_MEDIUM_TYPE or UPDATED_FILE_MEDIUM_TYPE
        defaultFileTypeShouldBeFound("fileMediumType.in=" + DEFAULT_FILE_MEDIUM_TYPE + "," + UPDATED_FILE_MEDIUM_TYPE);

        // Get all the fileTypeList where fileMediumType equals to UPDATED_FILE_MEDIUM_TYPE
        defaultFileTypeShouldNotBeFound("fileMediumType.in=" + UPDATED_FILE_MEDIUM_TYPE);
    }

    @Test
    @Transactional
    void getAllFileTypesByFileMediumTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        fileTypeRepository.saveAndFlush(fileType);

        // Get all the fileTypeList where fileMediumType is not null
        defaultFileTypeShouldBeFound("fileMediumType.specified=true");

        // Get all the fileTypeList where fileMediumType is null
        defaultFileTypeShouldNotBeFound("fileMediumType.specified=false");
    }

    @Test
    @Transactional
    void getAllFileTypesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        fileTypeRepository.saveAndFlush(fileType);

        // Get all the fileTypeList where description equals to DEFAULT_DESCRIPTION
        defaultFileTypeShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the fileTypeList where description equals to UPDATED_DESCRIPTION
        defaultFileTypeShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllFileTypesByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        fileTypeRepository.saveAndFlush(fileType);

        // Get all the fileTypeList where description not equals to DEFAULT_DESCRIPTION
        defaultFileTypeShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the fileTypeList where description not equals to UPDATED_DESCRIPTION
        defaultFileTypeShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllFileTypesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        fileTypeRepository.saveAndFlush(fileType);

        // Get all the fileTypeList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultFileTypeShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the fileTypeList where description equals to UPDATED_DESCRIPTION
        defaultFileTypeShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllFileTypesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        fileTypeRepository.saveAndFlush(fileType);

        // Get all the fileTypeList where description is not null
        defaultFileTypeShouldBeFound("description.specified=true");

        // Get all the fileTypeList where description is null
        defaultFileTypeShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllFileTypesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        fileTypeRepository.saveAndFlush(fileType);

        // Get all the fileTypeList where description contains DEFAULT_DESCRIPTION
        defaultFileTypeShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the fileTypeList where description contains UPDATED_DESCRIPTION
        defaultFileTypeShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllFileTypesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        fileTypeRepository.saveAndFlush(fileType);

        // Get all the fileTypeList where description does not contain DEFAULT_DESCRIPTION
        defaultFileTypeShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the fileTypeList where description does not contain UPDATED_DESCRIPTION
        defaultFileTypeShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllFileTypesByFileTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        fileTypeRepository.saveAndFlush(fileType);

        // Get all the fileTypeList where fileType equals to DEFAULT_FILE_TYPE
        defaultFileTypeShouldBeFound("fileType.equals=" + DEFAULT_FILE_TYPE);

        // Get all the fileTypeList where fileType equals to UPDATED_FILE_TYPE
        defaultFileTypeShouldNotBeFound("fileType.equals=" + UPDATED_FILE_TYPE);
    }

    @Test
    @Transactional
    void getAllFileTypesByFileTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        fileTypeRepository.saveAndFlush(fileType);

        // Get all the fileTypeList where fileType not equals to DEFAULT_FILE_TYPE
        defaultFileTypeShouldNotBeFound("fileType.notEquals=" + DEFAULT_FILE_TYPE);

        // Get all the fileTypeList where fileType not equals to UPDATED_FILE_TYPE
        defaultFileTypeShouldBeFound("fileType.notEquals=" + UPDATED_FILE_TYPE);
    }

    @Test
    @Transactional
    void getAllFileTypesByFileTypeIsInShouldWork() throws Exception {
        // Initialize the database
        fileTypeRepository.saveAndFlush(fileType);

        // Get all the fileTypeList where fileType in DEFAULT_FILE_TYPE or UPDATED_FILE_TYPE
        defaultFileTypeShouldBeFound("fileType.in=" + DEFAULT_FILE_TYPE + "," + UPDATED_FILE_TYPE);

        // Get all the fileTypeList where fileType equals to UPDATED_FILE_TYPE
        defaultFileTypeShouldNotBeFound("fileType.in=" + UPDATED_FILE_TYPE);
    }

    @Test
    @Transactional
    void getAllFileTypesByFileTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        fileTypeRepository.saveAndFlush(fileType);

        // Get all the fileTypeList where fileType is not null
        defaultFileTypeShouldBeFound("fileType.specified=true");

        // Get all the fileTypeList where fileType is null
        defaultFileTypeShouldNotBeFound("fileType.specified=false");
    }

    @Test
    @Transactional
    void getAllFileTypesByPlaceholderIsEqualToSomething() throws Exception {
        // Initialize the database
        fileTypeRepository.saveAndFlush(fileType);
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
        fileType.addPlaceholder(placeholder);
        fileTypeRepository.saveAndFlush(fileType);
        Long placeholderId = placeholder.getId();

        // Get all the fileTypeList where placeholder equals to placeholderId
        defaultFileTypeShouldBeFound("placeholderId.equals=" + placeholderId);

        // Get all the fileTypeList where placeholder equals to (placeholderId + 1)
        defaultFileTypeShouldNotBeFound("placeholderId.equals=" + (placeholderId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultFileTypeShouldBeFound(String filter) throws Exception {
        restFileTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fileType.getId().intValue())))
            .andExpect(jsonPath("$.[*].fileTypeName").value(hasItem(DEFAULT_FILE_TYPE_NAME)))
            .andExpect(jsonPath("$.[*].fileMediumType").value(hasItem(DEFAULT_FILE_MEDIUM_TYPE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].fileTemplateContentType").value(hasItem(DEFAULT_FILE_TEMPLATE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].fileTemplate").value(hasItem(Base64Utils.encodeToString(DEFAULT_FILE_TEMPLATE))))
            .andExpect(jsonPath("$.[*].fileType").value(hasItem(DEFAULT_FILE_TYPE.toString())));

        // Check, that the count call also returns 1
        restFileTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultFileTypeShouldNotBeFound(String filter) throws Exception {
        restFileTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restFileTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingFileType() throws Exception {
        // Get the fileType
        restFileTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewFileType() throws Exception {
        // Initialize the database
        fileTypeRepository.saveAndFlush(fileType);

        int databaseSizeBeforeUpdate = fileTypeRepository.findAll().size();

        // Update the fileType
        FileType updatedFileType = fileTypeRepository.findById(fileType.getId()).get();
        // Disconnect from session so that the updates on updatedFileType are not directly saved in db
        em.detach(updatedFileType);
        updatedFileType
            .fileTypeName(UPDATED_FILE_TYPE_NAME)
            .fileMediumType(UPDATED_FILE_MEDIUM_TYPE)
            .description(UPDATED_DESCRIPTION)
            .fileTemplate(UPDATED_FILE_TEMPLATE)
            .fileTemplateContentType(UPDATED_FILE_TEMPLATE_CONTENT_TYPE)
            .fileType(UPDATED_FILE_TYPE);
        FileTypeDTO fileTypeDTO = fileTypeMapper.toDto(updatedFileType);

        restFileTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, fileTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fileTypeDTO))
            )
            .andExpect(status().isOk());

        // Validate the FileType in the database
        List<FileType> fileTypeList = fileTypeRepository.findAll();
        assertThat(fileTypeList).hasSize(databaseSizeBeforeUpdate);
        FileType testFileType = fileTypeList.get(fileTypeList.size() - 1);
        assertThat(testFileType.getFileTypeName()).isEqualTo(UPDATED_FILE_TYPE_NAME);
        assertThat(testFileType.getFileMediumType()).isEqualTo(UPDATED_FILE_MEDIUM_TYPE);
        assertThat(testFileType.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testFileType.getFileTemplate()).isEqualTo(UPDATED_FILE_TEMPLATE);
        assertThat(testFileType.getFileTemplateContentType()).isEqualTo(UPDATED_FILE_TEMPLATE_CONTENT_TYPE);
        assertThat(testFileType.getFileType()).isEqualTo(UPDATED_FILE_TYPE);

        // Validate the FileType in Elasticsearch
        verify(mockFileTypeSearchRepository).save(testFileType);
    }

    @Test
    @Transactional
    void putNonExistingFileType() throws Exception {
        int databaseSizeBeforeUpdate = fileTypeRepository.findAll().size();
        fileType.setId(count.incrementAndGet());

        // Create the FileType
        FileTypeDTO fileTypeDTO = fileTypeMapper.toDto(fileType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFileTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, fileTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fileTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FileType in the database
        List<FileType> fileTypeList = fileTypeRepository.findAll();
        assertThat(fileTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the FileType in Elasticsearch
        verify(mockFileTypeSearchRepository, times(0)).save(fileType);
    }

    @Test
    @Transactional
    void putWithIdMismatchFileType() throws Exception {
        int databaseSizeBeforeUpdate = fileTypeRepository.findAll().size();
        fileType.setId(count.incrementAndGet());

        // Create the FileType
        FileTypeDTO fileTypeDTO = fileTypeMapper.toDto(fileType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFileTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fileTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FileType in the database
        List<FileType> fileTypeList = fileTypeRepository.findAll();
        assertThat(fileTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the FileType in Elasticsearch
        verify(mockFileTypeSearchRepository, times(0)).save(fileType);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFileType() throws Exception {
        int databaseSizeBeforeUpdate = fileTypeRepository.findAll().size();
        fileType.setId(count.incrementAndGet());

        // Create the FileType
        FileTypeDTO fileTypeDTO = fileTypeMapper.toDto(fileType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFileTypeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fileTypeDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the FileType in the database
        List<FileType> fileTypeList = fileTypeRepository.findAll();
        assertThat(fileTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the FileType in Elasticsearch
        verify(mockFileTypeSearchRepository, times(0)).save(fileType);
    }

    @Test
    @Transactional
    void partialUpdateFileTypeWithPatch() throws Exception {
        // Initialize the database
        fileTypeRepository.saveAndFlush(fileType);

        int databaseSizeBeforeUpdate = fileTypeRepository.findAll().size();

        // Update the fileType using partial update
        FileType partialUpdatedFileType = new FileType();
        partialUpdatedFileType.setId(fileType.getId());

        partialUpdatedFileType
            .fileTypeName(UPDATED_FILE_TYPE_NAME)
            .fileMediumType(UPDATED_FILE_MEDIUM_TYPE)
            .description(UPDATED_DESCRIPTION)
            .fileTemplate(UPDATED_FILE_TEMPLATE)
            .fileTemplateContentType(UPDATED_FILE_TEMPLATE_CONTENT_TYPE)
            .fileType(UPDATED_FILE_TYPE);

        restFileTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFileType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFileType))
            )
            .andExpect(status().isOk());

        // Validate the FileType in the database
        List<FileType> fileTypeList = fileTypeRepository.findAll();
        assertThat(fileTypeList).hasSize(databaseSizeBeforeUpdate);
        FileType testFileType = fileTypeList.get(fileTypeList.size() - 1);
        assertThat(testFileType.getFileTypeName()).isEqualTo(UPDATED_FILE_TYPE_NAME);
        assertThat(testFileType.getFileMediumType()).isEqualTo(UPDATED_FILE_MEDIUM_TYPE);
        assertThat(testFileType.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testFileType.getFileTemplate()).isEqualTo(UPDATED_FILE_TEMPLATE);
        assertThat(testFileType.getFileTemplateContentType()).isEqualTo(UPDATED_FILE_TEMPLATE_CONTENT_TYPE);
        assertThat(testFileType.getFileType()).isEqualTo(UPDATED_FILE_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateFileTypeWithPatch() throws Exception {
        // Initialize the database
        fileTypeRepository.saveAndFlush(fileType);

        int databaseSizeBeforeUpdate = fileTypeRepository.findAll().size();

        // Update the fileType using partial update
        FileType partialUpdatedFileType = new FileType();
        partialUpdatedFileType.setId(fileType.getId());

        partialUpdatedFileType
            .fileTypeName(UPDATED_FILE_TYPE_NAME)
            .fileMediumType(UPDATED_FILE_MEDIUM_TYPE)
            .description(UPDATED_DESCRIPTION)
            .fileTemplate(UPDATED_FILE_TEMPLATE)
            .fileTemplateContentType(UPDATED_FILE_TEMPLATE_CONTENT_TYPE)
            .fileType(UPDATED_FILE_TYPE);

        restFileTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFileType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFileType))
            )
            .andExpect(status().isOk());

        // Validate the FileType in the database
        List<FileType> fileTypeList = fileTypeRepository.findAll();
        assertThat(fileTypeList).hasSize(databaseSizeBeforeUpdate);
        FileType testFileType = fileTypeList.get(fileTypeList.size() - 1);
        assertThat(testFileType.getFileTypeName()).isEqualTo(UPDATED_FILE_TYPE_NAME);
        assertThat(testFileType.getFileMediumType()).isEqualTo(UPDATED_FILE_MEDIUM_TYPE);
        assertThat(testFileType.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testFileType.getFileTemplate()).isEqualTo(UPDATED_FILE_TEMPLATE);
        assertThat(testFileType.getFileTemplateContentType()).isEqualTo(UPDATED_FILE_TEMPLATE_CONTENT_TYPE);
        assertThat(testFileType.getFileType()).isEqualTo(UPDATED_FILE_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingFileType() throws Exception {
        int databaseSizeBeforeUpdate = fileTypeRepository.findAll().size();
        fileType.setId(count.incrementAndGet());

        // Create the FileType
        FileTypeDTO fileTypeDTO = fileTypeMapper.toDto(fileType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFileTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, fileTypeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(fileTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FileType in the database
        List<FileType> fileTypeList = fileTypeRepository.findAll();
        assertThat(fileTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the FileType in Elasticsearch
        verify(mockFileTypeSearchRepository, times(0)).save(fileType);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFileType() throws Exception {
        int databaseSizeBeforeUpdate = fileTypeRepository.findAll().size();
        fileType.setId(count.incrementAndGet());

        // Create the FileType
        FileTypeDTO fileTypeDTO = fileTypeMapper.toDto(fileType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFileTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(fileTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FileType in the database
        List<FileType> fileTypeList = fileTypeRepository.findAll();
        assertThat(fileTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the FileType in Elasticsearch
        verify(mockFileTypeSearchRepository, times(0)).save(fileType);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFileType() throws Exception {
        int databaseSizeBeforeUpdate = fileTypeRepository.findAll().size();
        fileType.setId(count.incrementAndGet());

        // Create the FileType
        FileTypeDTO fileTypeDTO = fileTypeMapper.toDto(fileType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFileTypeMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(fileTypeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FileType in the database
        List<FileType> fileTypeList = fileTypeRepository.findAll();
        assertThat(fileTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the FileType in Elasticsearch
        verify(mockFileTypeSearchRepository, times(0)).save(fileType);
    }

    @Test
    @Transactional
    void deleteFileType() throws Exception {
        // Initialize the database
        fileTypeRepository.saveAndFlush(fileType);

        int databaseSizeBeforeDelete = fileTypeRepository.findAll().size();

        // Delete the fileType
        restFileTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, fileType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<FileType> fileTypeList = fileTypeRepository.findAll();
        assertThat(fileTypeList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the FileType in Elasticsearch
        verify(mockFileTypeSearchRepository, times(1)).deleteById(fileType.getId());
    }

    @Test
    @Transactional
    void searchFileType() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        fileTypeRepository.saveAndFlush(fileType);
        when(mockFileTypeSearchRepository.search("id:" + fileType.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(fileType), PageRequest.of(0, 1), 1));

        // Search the fileType
        restFileTypeMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + fileType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fileType.getId().intValue())))
            .andExpect(jsonPath("$.[*].fileTypeName").value(hasItem(DEFAULT_FILE_TYPE_NAME)))
            .andExpect(jsonPath("$.[*].fileMediumType").value(hasItem(DEFAULT_FILE_MEDIUM_TYPE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].fileTemplateContentType").value(hasItem(DEFAULT_FILE_TEMPLATE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].fileTemplate").value(hasItem(Base64Utils.encodeToString(DEFAULT_FILE_TEMPLATE))))
            .andExpect(jsonPath("$.[*].fileType").value(hasItem(DEFAULT_FILE_TYPE.toString())));
    }
}
