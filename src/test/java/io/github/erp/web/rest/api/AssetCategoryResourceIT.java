package io.github.erp.web.rest.api;

/*-
 * Erp System - Mark II No 23 (Baruch Series)
 * Copyright © 2021 - 2022 Edwin Njeru (mailnjeru@gmail.com)
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
import io.github.erp.domain.AssetCategory;
import io.github.erp.domain.DepreciationMethod;
import io.github.erp.domain.Placeholder;
import io.github.erp.repository.AssetCategoryRepository;
import io.github.erp.repository.search.AssetCategorySearchRepository;
import io.github.erp.service.AssetCategoryService;
import io.github.erp.service.dto.AssetCategoryDTO;
import io.github.erp.service.mapper.AssetCategoryMapper;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link AssetCategoryResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser(roles = {"DEV"})
public class AssetCategoryResourceIT {

    private static final String DEFAULT_ASSET_CATEGORY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_ASSET_CATEGORY_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_NOTES = "AAAAAAAAAA";
    private static final String UPDATED_NOTES = "BBBBBBBBBB";

    private static final String DEFAULT_REMARKS = "AAAAAAAAAA";
    private static final String UPDATED_REMARKS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/dev/asset-categories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/dev/_search/asset-categories";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AssetCategoryRepository assetCategoryRepository;

    @Mock
    private AssetCategoryRepository assetCategoryRepositoryMock;

    @Autowired
    private AssetCategoryMapper assetCategoryMapper;

    @Mock
    private AssetCategoryService assetCategoryServiceMock;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.AssetCategorySearchRepositoryMockConfiguration
     */
    @Autowired
    private AssetCategorySearchRepository mockAssetCategorySearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAssetCategoryMockMvc;

    private AssetCategory assetCategory;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AssetCategory createEntity(EntityManager em) {
        AssetCategory assetCategory = new AssetCategory()
            .assetCategoryName(DEFAULT_ASSET_CATEGORY_NAME)
            .description(DEFAULT_DESCRIPTION)
            .notes(DEFAULT_NOTES)
            .remarks(DEFAULT_REMARKS);
        // Add required entity
        DepreciationMethod depreciationMethod;
        if (TestUtil.findAll(em, DepreciationMethod.class).isEmpty()) {
            depreciationMethod = DepreciationMethodResourceIT.createEntity(em);
            em.persist(depreciationMethod);
            em.flush();
        } else {
            depreciationMethod = TestUtil.findAll(em, DepreciationMethod.class).get(0);
        }
        assetCategory.setDepreciationMethod(depreciationMethod);
        return assetCategory;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AssetCategory createUpdatedEntity(EntityManager em) {
        AssetCategory assetCategory = new AssetCategory()
            .assetCategoryName(UPDATED_ASSET_CATEGORY_NAME)
            .description(UPDATED_DESCRIPTION)
            .notes(UPDATED_NOTES)
            .remarks(UPDATED_REMARKS);
        // Add required entity
        DepreciationMethod depreciationMethod;
        if (TestUtil.findAll(em, DepreciationMethod.class).isEmpty()) {
            depreciationMethod = DepreciationMethodResourceIT.createUpdatedEntity(em);
            em.persist(depreciationMethod);
            em.flush();
        } else {
            depreciationMethod = TestUtil.findAll(em, DepreciationMethod.class).get(0);
        }
        assetCategory.setDepreciationMethod(depreciationMethod);
        return assetCategory;
    }

    @BeforeEach
    public void initTest() {
        assetCategory = createEntity(em);
    }

    @Test
    @Transactional
    void createAssetCategory() throws Exception {
        int databaseSizeBeforeCreate = assetCategoryRepository.findAll().size();
        // Create the AssetCategory
        AssetCategoryDTO assetCategoryDTO = assetCategoryMapper.toDto(assetCategory);
        restAssetCategoryMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(assetCategoryDTO))
            )
            .andExpect(status().isCreated());

        // Validate the AssetCategory in the database
        List<AssetCategory> assetCategoryList = assetCategoryRepository.findAll();
        assertThat(assetCategoryList).hasSize(databaseSizeBeforeCreate + 1);
        AssetCategory testAssetCategory = assetCategoryList.get(assetCategoryList.size() - 1);
        assertThat(testAssetCategory.getAssetCategoryName()).isEqualTo(DEFAULT_ASSET_CATEGORY_NAME);
        assertThat(testAssetCategory.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testAssetCategory.getNotes()).isEqualTo(DEFAULT_NOTES);
        assertThat(testAssetCategory.getRemarks()).isEqualTo(DEFAULT_REMARKS);

        // Validate the AssetCategory in Elasticsearch
        verify(mockAssetCategorySearchRepository, times(1)).save(testAssetCategory);
    }

    @Test
    @Transactional
    void createAssetCategoryWithExistingId() throws Exception {
        // Create the AssetCategory with an existing ID
        assetCategory.setId(1L);
        AssetCategoryDTO assetCategoryDTO = assetCategoryMapper.toDto(assetCategory);

        int databaseSizeBeforeCreate = assetCategoryRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAssetCategoryMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(assetCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AssetCategory in the database
        List<AssetCategory> assetCategoryList = assetCategoryRepository.findAll();
        assertThat(assetCategoryList).hasSize(databaseSizeBeforeCreate);

        // Validate the AssetCategory in Elasticsearch
        verify(mockAssetCategorySearchRepository, times(0)).save(assetCategory);
    }

    @Test
    @Transactional
    void checkAssetCategoryNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = assetCategoryRepository.findAll().size();
        // set the field null
        assetCategory.setAssetCategoryName(null);

        // Create the AssetCategory, which fails.
        AssetCategoryDTO assetCategoryDTO = assetCategoryMapper.toDto(assetCategory);

        restAssetCategoryMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(assetCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        List<AssetCategory> assetCategoryList = assetCategoryRepository.findAll();
        assertThat(assetCategoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAssetCategories() throws Exception {
        // Initialize the database
        assetCategoryRepository.saveAndFlush(assetCategory);

        // Get all the assetCategoryList
        restAssetCategoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(assetCategory.getId().intValue())))
            .andExpect(jsonPath("$.[*].assetCategoryName").value(hasItem(DEFAULT_ASSET_CATEGORY_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES)))
            .andExpect(jsonPath("$.[*].remarks").value(hasItem(DEFAULT_REMARKS.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllAssetCategoriesWithEagerRelationshipsIsEnabled() throws Exception {
        when(assetCategoryServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restAssetCategoryMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(assetCategoryServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllAssetCategoriesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(assetCategoryServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restAssetCategoryMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(assetCategoryServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getAssetCategory() throws Exception {
        // Initialize the database
        assetCategoryRepository.saveAndFlush(assetCategory);

        // Get the assetCategory
        restAssetCategoryMockMvc
            .perform(get(ENTITY_API_URL_ID, assetCategory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(assetCategory.getId().intValue()))
            .andExpect(jsonPath("$.assetCategoryName").value(DEFAULT_ASSET_CATEGORY_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.notes").value(DEFAULT_NOTES))
            .andExpect(jsonPath("$.remarks").value(DEFAULT_REMARKS.toString()));
    }

    @Test
    @Transactional
    void getAssetCategoriesByIdFiltering() throws Exception {
        // Initialize the database
        assetCategoryRepository.saveAndFlush(assetCategory);

        Long id = assetCategory.getId();

        defaultAssetCategoryShouldBeFound("id.equals=" + id);
        defaultAssetCategoryShouldNotBeFound("id.notEquals=" + id);

        defaultAssetCategoryShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultAssetCategoryShouldNotBeFound("id.greaterThan=" + id);

        defaultAssetCategoryShouldBeFound("id.lessThanOrEqual=" + id);
        defaultAssetCategoryShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAssetCategoriesByAssetCategoryNameIsEqualToSomething() throws Exception {
        // Initialize the database
        assetCategoryRepository.saveAndFlush(assetCategory);

        // Get all the assetCategoryList where assetCategoryName equals to DEFAULT_ASSET_CATEGORY_NAME
        defaultAssetCategoryShouldBeFound("assetCategoryName.equals=" + DEFAULT_ASSET_CATEGORY_NAME);

        // Get all the assetCategoryList where assetCategoryName equals to UPDATED_ASSET_CATEGORY_NAME
        defaultAssetCategoryShouldNotBeFound("assetCategoryName.equals=" + UPDATED_ASSET_CATEGORY_NAME);
    }

    @Test
    @Transactional
    void getAllAssetCategoriesByAssetCategoryNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        assetCategoryRepository.saveAndFlush(assetCategory);

        // Get all the assetCategoryList where assetCategoryName not equals to DEFAULT_ASSET_CATEGORY_NAME
        defaultAssetCategoryShouldNotBeFound("assetCategoryName.notEquals=" + DEFAULT_ASSET_CATEGORY_NAME);

        // Get all the assetCategoryList where assetCategoryName not equals to UPDATED_ASSET_CATEGORY_NAME
        defaultAssetCategoryShouldBeFound("assetCategoryName.notEquals=" + UPDATED_ASSET_CATEGORY_NAME);
    }

    @Test
    @Transactional
    void getAllAssetCategoriesByAssetCategoryNameIsInShouldWork() throws Exception {
        // Initialize the database
        assetCategoryRepository.saveAndFlush(assetCategory);

        // Get all the assetCategoryList where assetCategoryName in DEFAULT_ASSET_CATEGORY_NAME or UPDATED_ASSET_CATEGORY_NAME
        defaultAssetCategoryShouldBeFound("assetCategoryName.in=" + DEFAULT_ASSET_CATEGORY_NAME + "," + UPDATED_ASSET_CATEGORY_NAME);

        // Get all the assetCategoryList where assetCategoryName equals to UPDATED_ASSET_CATEGORY_NAME
        defaultAssetCategoryShouldNotBeFound("assetCategoryName.in=" + UPDATED_ASSET_CATEGORY_NAME);
    }

    @Test
    @Transactional
    void getAllAssetCategoriesByAssetCategoryNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        assetCategoryRepository.saveAndFlush(assetCategory);

        // Get all the assetCategoryList where assetCategoryName is not null
        defaultAssetCategoryShouldBeFound("assetCategoryName.specified=true");

        // Get all the assetCategoryList where assetCategoryName is null
        defaultAssetCategoryShouldNotBeFound("assetCategoryName.specified=false");
    }

    @Test
    @Transactional
    void getAllAssetCategoriesByAssetCategoryNameContainsSomething() throws Exception {
        // Initialize the database
        assetCategoryRepository.saveAndFlush(assetCategory);

        // Get all the assetCategoryList where assetCategoryName contains DEFAULT_ASSET_CATEGORY_NAME
        defaultAssetCategoryShouldBeFound("assetCategoryName.contains=" + DEFAULT_ASSET_CATEGORY_NAME);

        // Get all the assetCategoryList where assetCategoryName contains UPDATED_ASSET_CATEGORY_NAME
        defaultAssetCategoryShouldNotBeFound("assetCategoryName.contains=" + UPDATED_ASSET_CATEGORY_NAME);
    }

    @Test
    @Transactional
    void getAllAssetCategoriesByAssetCategoryNameNotContainsSomething() throws Exception {
        // Initialize the database
        assetCategoryRepository.saveAndFlush(assetCategory);

        // Get all the assetCategoryList where assetCategoryName does not contain DEFAULT_ASSET_CATEGORY_NAME
        defaultAssetCategoryShouldNotBeFound("assetCategoryName.doesNotContain=" + DEFAULT_ASSET_CATEGORY_NAME);

        // Get all the assetCategoryList where assetCategoryName does not contain UPDATED_ASSET_CATEGORY_NAME
        defaultAssetCategoryShouldBeFound("assetCategoryName.doesNotContain=" + UPDATED_ASSET_CATEGORY_NAME);
    }

    @Test
    @Transactional
    void getAllAssetCategoriesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        assetCategoryRepository.saveAndFlush(assetCategory);

        // Get all the assetCategoryList where description equals to DEFAULT_DESCRIPTION
        defaultAssetCategoryShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the assetCategoryList where description equals to UPDATED_DESCRIPTION
        defaultAssetCategoryShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllAssetCategoriesByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        assetCategoryRepository.saveAndFlush(assetCategory);

        // Get all the assetCategoryList where description not equals to DEFAULT_DESCRIPTION
        defaultAssetCategoryShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the assetCategoryList where description not equals to UPDATED_DESCRIPTION
        defaultAssetCategoryShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllAssetCategoriesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        assetCategoryRepository.saveAndFlush(assetCategory);

        // Get all the assetCategoryList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultAssetCategoryShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the assetCategoryList where description equals to UPDATED_DESCRIPTION
        defaultAssetCategoryShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllAssetCategoriesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        assetCategoryRepository.saveAndFlush(assetCategory);

        // Get all the assetCategoryList where description is not null
        defaultAssetCategoryShouldBeFound("description.specified=true");

        // Get all the assetCategoryList where description is null
        defaultAssetCategoryShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllAssetCategoriesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        assetCategoryRepository.saveAndFlush(assetCategory);

        // Get all the assetCategoryList where description contains DEFAULT_DESCRIPTION
        defaultAssetCategoryShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the assetCategoryList where description contains UPDATED_DESCRIPTION
        defaultAssetCategoryShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllAssetCategoriesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        assetCategoryRepository.saveAndFlush(assetCategory);

        // Get all the assetCategoryList where description does not contain DEFAULT_DESCRIPTION
        defaultAssetCategoryShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the assetCategoryList where description does not contain UPDATED_DESCRIPTION
        defaultAssetCategoryShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllAssetCategoriesByNotesIsEqualToSomething() throws Exception {
        // Initialize the database
        assetCategoryRepository.saveAndFlush(assetCategory);

        // Get all the assetCategoryList where notes equals to DEFAULT_NOTES
        defaultAssetCategoryShouldBeFound("notes.equals=" + DEFAULT_NOTES);

        // Get all the assetCategoryList where notes equals to UPDATED_NOTES
        defaultAssetCategoryShouldNotBeFound("notes.equals=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllAssetCategoriesByNotesIsNotEqualToSomething() throws Exception {
        // Initialize the database
        assetCategoryRepository.saveAndFlush(assetCategory);

        // Get all the assetCategoryList where notes not equals to DEFAULT_NOTES
        defaultAssetCategoryShouldNotBeFound("notes.notEquals=" + DEFAULT_NOTES);

        // Get all the assetCategoryList where notes not equals to UPDATED_NOTES
        defaultAssetCategoryShouldBeFound("notes.notEquals=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllAssetCategoriesByNotesIsInShouldWork() throws Exception {
        // Initialize the database
        assetCategoryRepository.saveAndFlush(assetCategory);

        // Get all the assetCategoryList where notes in DEFAULT_NOTES or UPDATED_NOTES
        defaultAssetCategoryShouldBeFound("notes.in=" + DEFAULT_NOTES + "," + UPDATED_NOTES);

        // Get all the assetCategoryList where notes equals to UPDATED_NOTES
        defaultAssetCategoryShouldNotBeFound("notes.in=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllAssetCategoriesByNotesIsNullOrNotNull() throws Exception {
        // Initialize the database
        assetCategoryRepository.saveAndFlush(assetCategory);

        // Get all the assetCategoryList where notes is not null
        defaultAssetCategoryShouldBeFound("notes.specified=true");

        // Get all the assetCategoryList where notes is null
        defaultAssetCategoryShouldNotBeFound("notes.specified=false");
    }

    @Test
    @Transactional
    void getAllAssetCategoriesByNotesContainsSomething() throws Exception {
        // Initialize the database
        assetCategoryRepository.saveAndFlush(assetCategory);

        // Get all the assetCategoryList where notes contains DEFAULT_NOTES
        defaultAssetCategoryShouldBeFound("notes.contains=" + DEFAULT_NOTES);

        // Get all the assetCategoryList where notes contains UPDATED_NOTES
        defaultAssetCategoryShouldNotBeFound("notes.contains=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllAssetCategoriesByNotesNotContainsSomething() throws Exception {
        // Initialize the database
        assetCategoryRepository.saveAndFlush(assetCategory);

        // Get all the assetCategoryList where notes does not contain DEFAULT_NOTES
        defaultAssetCategoryShouldNotBeFound("notes.doesNotContain=" + DEFAULT_NOTES);

        // Get all the assetCategoryList where notes does not contain UPDATED_NOTES
        defaultAssetCategoryShouldBeFound("notes.doesNotContain=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllAssetCategoriesByDepreciationMethodIsEqualToSomething() throws Exception {
        // Initialize the database
        assetCategoryRepository.saveAndFlush(assetCategory);
        DepreciationMethod depreciationMethod;
        if (TestUtil.findAll(em, DepreciationMethod.class).isEmpty()) {
            depreciationMethod = DepreciationMethodResourceIT.createEntity(em);
            em.persist(depreciationMethod);
            em.flush();
        } else {
            depreciationMethod = TestUtil.findAll(em, DepreciationMethod.class).get(0);
        }
        em.persist(depreciationMethod);
        em.flush();
        assetCategory.setDepreciationMethod(depreciationMethod);
        assetCategoryRepository.saveAndFlush(assetCategory);
        Long depreciationMethodId = depreciationMethod.getId();

        // Get all the assetCategoryList where depreciationMethod equals to depreciationMethodId
        defaultAssetCategoryShouldBeFound("depreciationMethodId.equals=" + depreciationMethodId);

        // Get all the assetCategoryList where depreciationMethod equals to (depreciationMethodId + 1)
        defaultAssetCategoryShouldNotBeFound("depreciationMethodId.equals=" + (depreciationMethodId + 1));
    }

    @Test
    @Transactional
    void getAllAssetCategoriesByPlaceholderIsEqualToSomething() throws Exception {
        // Initialize the database
        assetCategoryRepository.saveAndFlush(assetCategory);
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
        assetCategory.addPlaceholder(placeholder);
        assetCategoryRepository.saveAndFlush(assetCategory);
        Long placeholderId = placeholder.getId();

        // Get all the assetCategoryList where placeholder equals to placeholderId
        defaultAssetCategoryShouldBeFound("placeholderId.equals=" + placeholderId);

        // Get all the assetCategoryList where placeholder equals to (placeholderId + 1)
        defaultAssetCategoryShouldNotBeFound("placeholderId.equals=" + (placeholderId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAssetCategoryShouldBeFound(String filter) throws Exception {
        restAssetCategoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(assetCategory.getId().intValue())))
            .andExpect(jsonPath("$.[*].assetCategoryName").value(hasItem(DEFAULT_ASSET_CATEGORY_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES)))
            .andExpect(jsonPath("$.[*].remarks").value(hasItem(DEFAULT_REMARKS.toString())));

        // Check, that the count call also returns 1
        restAssetCategoryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAssetCategoryShouldNotBeFound(String filter) throws Exception {
        restAssetCategoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAssetCategoryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAssetCategory() throws Exception {
        // Get the assetCategory
        restAssetCategoryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewAssetCategory() throws Exception {
        // Initialize the database
        assetCategoryRepository.saveAndFlush(assetCategory);

        int databaseSizeBeforeUpdate = assetCategoryRepository.findAll().size();

        // Update the assetCategory
        AssetCategory updatedAssetCategory = assetCategoryRepository.findById(assetCategory.getId()).get();
        // Disconnect from session so that the updates on updatedAssetCategory are not directly saved in db
        em.detach(updatedAssetCategory);
        updatedAssetCategory
            .assetCategoryName(UPDATED_ASSET_CATEGORY_NAME)
            .description(UPDATED_DESCRIPTION)
            .notes(UPDATED_NOTES)
            .remarks(UPDATED_REMARKS);
        AssetCategoryDTO assetCategoryDTO = assetCategoryMapper.toDto(updatedAssetCategory);

        restAssetCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, assetCategoryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(assetCategoryDTO))
            )
            .andExpect(status().isOk());

        // Validate the AssetCategory in the database
        List<AssetCategory> assetCategoryList = assetCategoryRepository.findAll();
        assertThat(assetCategoryList).hasSize(databaseSizeBeforeUpdate);
        AssetCategory testAssetCategory = assetCategoryList.get(assetCategoryList.size() - 1);
        assertThat(testAssetCategory.getAssetCategoryName()).isEqualTo(UPDATED_ASSET_CATEGORY_NAME);
        assertThat(testAssetCategory.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testAssetCategory.getNotes()).isEqualTo(UPDATED_NOTES);
        assertThat(testAssetCategory.getRemarks()).isEqualTo(UPDATED_REMARKS);

        // Validate the AssetCategory in Elasticsearch
        verify(mockAssetCategorySearchRepository).save(testAssetCategory);
    }

    @Test
    @Transactional
    void putNonExistingAssetCategory() throws Exception {
        int databaseSizeBeforeUpdate = assetCategoryRepository.findAll().size();
        assetCategory.setId(count.incrementAndGet());

        // Create the AssetCategory
        AssetCategoryDTO assetCategoryDTO = assetCategoryMapper.toDto(assetCategory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAssetCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, assetCategoryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(assetCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AssetCategory in the database
        List<AssetCategory> assetCategoryList = assetCategoryRepository.findAll();
        assertThat(assetCategoryList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AssetCategory in Elasticsearch
        verify(mockAssetCategorySearchRepository, times(0)).save(assetCategory);
    }

    @Test
    @Transactional
    void putWithIdMismatchAssetCategory() throws Exception {
        int databaseSizeBeforeUpdate = assetCategoryRepository.findAll().size();
        assetCategory.setId(count.incrementAndGet());

        // Create the AssetCategory
        AssetCategoryDTO assetCategoryDTO = assetCategoryMapper.toDto(assetCategory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAssetCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(assetCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AssetCategory in the database
        List<AssetCategory> assetCategoryList = assetCategoryRepository.findAll();
        assertThat(assetCategoryList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AssetCategory in Elasticsearch
        verify(mockAssetCategorySearchRepository, times(0)).save(assetCategory);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAssetCategory() throws Exception {
        int databaseSizeBeforeUpdate = assetCategoryRepository.findAll().size();
        assetCategory.setId(count.incrementAndGet());

        // Create the AssetCategory
        AssetCategoryDTO assetCategoryDTO = assetCategoryMapper.toDto(assetCategory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAssetCategoryMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(assetCategoryDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AssetCategory in the database
        List<AssetCategory> assetCategoryList = assetCategoryRepository.findAll();
        assertThat(assetCategoryList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AssetCategory in Elasticsearch
        verify(mockAssetCategorySearchRepository, times(0)).save(assetCategory);
    }

    @Test
    @Transactional
    void partialUpdateAssetCategoryWithPatch() throws Exception {
        // Initialize the database
        assetCategoryRepository.saveAndFlush(assetCategory);

        int databaseSizeBeforeUpdate = assetCategoryRepository.findAll().size();

        // Update the assetCategory using partial update
        AssetCategory partialUpdatedAssetCategory = new AssetCategory();
        partialUpdatedAssetCategory.setId(assetCategory.getId());

        partialUpdatedAssetCategory.assetCategoryName(UPDATED_ASSET_CATEGORY_NAME).description(UPDATED_DESCRIPTION);

        restAssetCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAssetCategory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAssetCategory))
            )
            .andExpect(status().isOk());

        // Validate the AssetCategory in the database
        List<AssetCategory> assetCategoryList = assetCategoryRepository.findAll();
        assertThat(assetCategoryList).hasSize(databaseSizeBeforeUpdate);
        AssetCategory testAssetCategory = assetCategoryList.get(assetCategoryList.size() - 1);
        assertThat(testAssetCategory.getAssetCategoryName()).isEqualTo(UPDATED_ASSET_CATEGORY_NAME);
        assertThat(testAssetCategory.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testAssetCategory.getNotes()).isEqualTo(DEFAULT_NOTES);
        assertThat(testAssetCategory.getRemarks()).isEqualTo(DEFAULT_REMARKS);
    }

    @Test
    @Transactional
    void fullUpdateAssetCategoryWithPatch() throws Exception {
        // Initialize the database
        assetCategoryRepository.saveAndFlush(assetCategory);

        int databaseSizeBeforeUpdate = assetCategoryRepository.findAll().size();

        // Update the assetCategory using partial update
        AssetCategory partialUpdatedAssetCategory = new AssetCategory();
        partialUpdatedAssetCategory.setId(assetCategory.getId());

        partialUpdatedAssetCategory
            .assetCategoryName(UPDATED_ASSET_CATEGORY_NAME)
            .description(UPDATED_DESCRIPTION)
            .notes(UPDATED_NOTES)
            .remarks(UPDATED_REMARKS);

        restAssetCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAssetCategory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAssetCategory))
            )
            .andExpect(status().isOk());

        // Validate the AssetCategory in the database
        List<AssetCategory> assetCategoryList = assetCategoryRepository.findAll();
        assertThat(assetCategoryList).hasSize(databaseSizeBeforeUpdate);
        AssetCategory testAssetCategory = assetCategoryList.get(assetCategoryList.size() - 1);
        assertThat(testAssetCategory.getAssetCategoryName()).isEqualTo(UPDATED_ASSET_CATEGORY_NAME);
        assertThat(testAssetCategory.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testAssetCategory.getNotes()).isEqualTo(UPDATED_NOTES);
        assertThat(testAssetCategory.getRemarks()).isEqualTo(UPDATED_REMARKS);
    }

    @Test
    @Transactional
    void patchNonExistingAssetCategory() throws Exception {
        int databaseSizeBeforeUpdate = assetCategoryRepository.findAll().size();
        assetCategory.setId(count.incrementAndGet());

        // Create the AssetCategory
        AssetCategoryDTO assetCategoryDTO = assetCategoryMapper.toDto(assetCategory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAssetCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, assetCategoryDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(assetCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AssetCategory in the database
        List<AssetCategory> assetCategoryList = assetCategoryRepository.findAll();
        assertThat(assetCategoryList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AssetCategory in Elasticsearch
        verify(mockAssetCategorySearchRepository, times(0)).save(assetCategory);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAssetCategory() throws Exception {
        int databaseSizeBeforeUpdate = assetCategoryRepository.findAll().size();
        assetCategory.setId(count.incrementAndGet());

        // Create the AssetCategory
        AssetCategoryDTO assetCategoryDTO = assetCategoryMapper.toDto(assetCategory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAssetCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(assetCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AssetCategory in the database
        List<AssetCategory> assetCategoryList = assetCategoryRepository.findAll();
        assertThat(assetCategoryList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AssetCategory in Elasticsearch
        verify(mockAssetCategorySearchRepository, times(0)).save(assetCategory);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAssetCategory() throws Exception {
        int databaseSizeBeforeUpdate = assetCategoryRepository.findAll().size();
        assetCategory.setId(count.incrementAndGet());

        // Create the AssetCategory
        AssetCategoryDTO assetCategoryDTO = assetCategoryMapper.toDto(assetCategory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAssetCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(assetCategoryDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AssetCategory in the database
        List<AssetCategory> assetCategoryList = assetCategoryRepository.findAll();
        assertThat(assetCategoryList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AssetCategory in Elasticsearch
        verify(mockAssetCategorySearchRepository, times(0)).save(assetCategory);
    }

    @Test
    @Transactional
    void deleteAssetCategory() throws Exception {
        // Initialize the database
        assetCategoryRepository.saveAndFlush(assetCategory);

        int databaseSizeBeforeDelete = assetCategoryRepository.findAll().size();

        // Delete the assetCategory
        restAssetCategoryMockMvc
            .perform(delete(ENTITY_API_URL_ID, assetCategory.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AssetCategory> assetCategoryList = assetCategoryRepository.findAll();
        assertThat(assetCategoryList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the AssetCategory in Elasticsearch
        verify(mockAssetCategorySearchRepository, times(1)).deleteById(assetCategory.getId());
    }

    @Test
    @Transactional
    void searchAssetCategory() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        assetCategoryRepository.saveAndFlush(assetCategory);
        when(mockAssetCategorySearchRepository.search("id:" + assetCategory.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(assetCategory), PageRequest.of(0, 1), 1));

        // Search the assetCategory
        restAssetCategoryMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + assetCategory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(assetCategory.getId().intValue())))
            .andExpect(jsonPath("$.[*].assetCategoryName").value(hasItem(DEFAULT_ASSET_CATEGORY_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES)))
            .andExpect(jsonPath("$.[*].remarks").value(hasItem(DEFAULT_REMARKS.toString())));
    }
}
