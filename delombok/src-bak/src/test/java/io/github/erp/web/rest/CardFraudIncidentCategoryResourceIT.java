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
import io.github.erp.domain.CardFraudIncidentCategory;
import io.github.erp.repository.CardFraudIncidentCategoryRepository;
import io.github.erp.repository.search.CardFraudIncidentCategorySearchRepository;
import io.github.erp.service.criteria.CardFraudIncidentCategoryCriteria;
import io.github.erp.service.dto.CardFraudIncidentCategoryDTO;
import io.github.erp.service.mapper.CardFraudIncidentCategoryMapper;
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
 * Integration tests for the {@link CardFraudIncidentCategoryResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class CardFraudIncidentCategoryResourceIT {

    private static final String DEFAULT_CARD_FRAUD_CATEGORY_TYPE_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CARD_FRAUD_CATEGORY_TYPE_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_CARD_FRAUD_CATEGORY_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_CARD_FRAUD_CATEGORY_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_CARD_FRAUD_CATEGORY_TYPE_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_CARD_FRAUD_CATEGORY_TYPE_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/card-fraud-incident-categories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/card-fraud-incident-categories";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CardFraudIncidentCategoryRepository cardFraudIncidentCategoryRepository;

    @Autowired
    private CardFraudIncidentCategoryMapper cardFraudIncidentCategoryMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.CardFraudIncidentCategorySearchRepositoryMockConfiguration
     */
    @Autowired
    private CardFraudIncidentCategorySearchRepository mockCardFraudIncidentCategorySearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCardFraudIncidentCategoryMockMvc;

    private CardFraudIncidentCategory cardFraudIncidentCategory;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CardFraudIncidentCategory createEntity(EntityManager em) {
        CardFraudIncidentCategory cardFraudIncidentCategory = new CardFraudIncidentCategory()
            .cardFraudCategoryTypeCode(DEFAULT_CARD_FRAUD_CATEGORY_TYPE_CODE)
            .cardFraudCategoryType(DEFAULT_CARD_FRAUD_CATEGORY_TYPE)
            .cardFraudCategoryTypeDescription(DEFAULT_CARD_FRAUD_CATEGORY_TYPE_DESCRIPTION);
        return cardFraudIncidentCategory;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CardFraudIncidentCategory createUpdatedEntity(EntityManager em) {
        CardFraudIncidentCategory cardFraudIncidentCategory = new CardFraudIncidentCategory()
            .cardFraudCategoryTypeCode(UPDATED_CARD_FRAUD_CATEGORY_TYPE_CODE)
            .cardFraudCategoryType(UPDATED_CARD_FRAUD_CATEGORY_TYPE)
            .cardFraudCategoryTypeDescription(UPDATED_CARD_FRAUD_CATEGORY_TYPE_DESCRIPTION);
        return cardFraudIncidentCategory;
    }

    @BeforeEach
    public void initTest() {
        cardFraudIncidentCategory = createEntity(em);
    }

    @Test
    @Transactional
    void createCardFraudIncidentCategory() throws Exception {
        int databaseSizeBeforeCreate = cardFraudIncidentCategoryRepository.findAll().size();
        // Create the CardFraudIncidentCategory
        CardFraudIncidentCategoryDTO cardFraudIncidentCategoryDTO = cardFraudIncidentCategoryMapper.toDto(cardFraudIncidentCategory);
        restCardFraudIncidentCategoryMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cardFraudIncidentCategoryDTO))
            )
            .andExpect(status().isCreated());

        // Validate the CardFraudIncidentCategory in the database
        List<CardFraudIncidentCategory> cardFraudIncidentCategoryList = cardFraudIncidentCategoryRepository.findAll();
        assertThat(cardFraudIncidentCategoryList).hasSize(databaseSizeBeforeCreate + 1);
        CardFraudIncidentCategory testCardFraudIncidentCategory = cardFraudIncidentCategoryList.get(
            cardFraudIncidentCategoryList.size() - 1
        );
        assertThat(testCardFraudIncidentCategory.getCardFraudCategoryTypeCode()).isEqualTo(DEFAULT_CARD_FRAUD_CATEGORY_TYPE_CODE);
        assertThat(testCardFraudIncidentCategory.getCardFraudCategoryType()).isEqualTo(DEFAULT_CARD_FRAUD_CATEGORY_TYPE);
        assertThat(testCardFraudIncidentCategory.getCardFraudCategoryTypeDescription())
            .isEqualTo(DEFAULT_CARD_FRAUD_CATEGORY_TYPE_DESCRIPTION);

        // Validate the CardFraudIncidentCategory in Elasticsearch
        verify(mockCardFraudIncidentCategorySearchRepository, times(1)).save(testCardFraudIncidentCategory);
    }

    @Test
    @Transactional
    void createCardFraudIncidentCategoryWithExistingId() throws Exception {
        // Create the CardFraudIncidentCategory with an existing ID
        cardFraudIncidentCategory.setId(1L);
        CardFraudIncidentCategoryDTO cardFraudIncidentCategoryDTO = cardFraudIncidentCategoryMapper.toDto(cardFraudIncidentCategory);

        int databaseSizeBeforeCreate = cardFraudIncidentCategoryRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCardFraudIncidentCategoryMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cardFraudIncidentCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CardFraudIncidentCategory in the database
        List<CardFraudIncidentCategory> cardFraudIncidentCategoryList = cardFraudIncidentCategoryRepository.findAll();
        assertThat(cardFraudIncidentCategoryList).hasSize(databaseSizeBeforeCreate);

        // Validate the CardFraudIncidentCategory in Elasticsearch
        verify(mockCardFraudIncidentCategorySearchRepository, times(0)).save(cardFraudIncidentCategory);
    }

    @Test
    @Transactional
    void checkCardFraudCategoryTypeCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = cardFraudIncidentCategoryRepository.findAll().size();
        // set the field null
        cardFraudIncidentCategory.setCardFraudCategoryTypeCode(null);

        // Create the CardFraudIncidentCategory, which fails.
        CardFraudIncidentCategoryDTO cardFraudIncidentCategoryDTO = cardFraudIncidentCategoryMapper.toDto(cardFraudIncidentCategory);

        restCardFraudIncidentCategoryMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cardFraudIncidentCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        List<CardFraudIncidentCategory> cardFraudIncidentCategoryList = cardFraudIncidentCategoryRepository.findAll();
        assertThat(cardFraudIncidentCategoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCardFraudCategoryTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = cardFraudIncidentCategoryRepository.findAll().size();
        // set the field null
        cardFraudIncidentCategory.setCardFraudCategoryType(null);

        // Create the CardFraudIncidentCategory, which fails.
        CardFraudIncidentCategoryDTO cardFraudIncidentCategoryDTO = cardFraudIncidentCategoryMapper.toDto(cardFraudIncidentCategory);

        restCardFraudIncidentCategoryMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cardFraudIncidentCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        List<CardFraudIncidentCategory> cardFraudIncidentCategoryList = cardFraudIncidentCategoryRepository.findAll();
        assertThat(cardFraudIncidentCategoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCardFraudIncidentCategories() throws Exception {
        // Initialize the database
        cardFraudIncidentCategoryRepository.saveAndFlush(cardFraudIncidentCategory);

        // Get all the cardFraudIncidentCategoryList
        restCardFraudIncidentCategoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cardFraudIncidentCategory.getId().intValue())))
            .andExpect(jsonPath("$.[*].cardFraudCategoryTypeCode").value(hasItem(DEFAULT_CARD_FRAUD_CATEGORY_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].cardFraudCategoryType").value(hasItem(DEFAULT_CARD_FRAUD_CATEGORY_TYPE)))
            .andExpect(
                jsonPath("$.[*].cardFraudCategoryTypeDescription").value(hasItem(DEFAULT_CARD_FRAUD_CATEGORY_TYPE_DESCRIPTION.toString()))
            );
    }

    @Test
    @Transactional
    void getCardFraudIncidentCategory() throws Exception {
        // Initialize the database
        cardFraudIncidentCategoryRepository.saveAndFlush(cardFraudIncidentCategory);

        // Get the cardFraudIncidentCategory
        restCardFraudIncidentCategoryMockMvc
            .perform(get(ENTITY_API_URL_ID, cardFraudIncidentCategory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cardFraudIncidentCategory.getId().intValue()))
            .andExpect(jsonPath("$.cardFraudCategoryTypeCode").value(DEFAULT_CARD_FRAUD_CATEGORY_TYPE_CODE))
            .andExpect(jsonPath("$.cardFraudCategoryType").value(DEFAULT_CARD_FRAUD_CATEGORY_TYPE))
            .andExpect(jsonPath("$.cardFraudCategoryTypeDescription").value(DEFAULT_CARD_FRAUD_CATEGORY_TYPE_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    void getCardFraudIncidentCategoriesByIdFiltering() throws Exception {
        // Initialize the database
        cardFraudIncidentCategoryRepository.saveAndFlush(cardFraudIncidentCategory);

        Long id = cardFraudIncidentCategory.getId();

        defaultCardFraudIncidentCategoryShouldBeFound("id.equals=" + id);
        defaultCardFraudIncidentCategoryShouldNotBeFound("id.notEquals=" + id);

        defaultCardFraudIncidentCategoryShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCardFraudIncidentCategoryShouldNotBeFound("id.greaterThan=" + id);

        defaultCardFraudIncidentCategoryShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCardFraudIncidentCategoryShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCardFraudIncidentCategoriesByCardFraudCategoryTypeCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        cardFraudIncidentCategoryRepository.saveAndFlush(cardFraudIncidentCategory);

        // Get all the cardFraudIncidentCategoryList where cardFraudCategoryTypeCode equals to DEFAULT_CARD_FRAUD_CATEGORY_TYPE_CODE
        defaultCardFraudIncidentCategoryShouldBeFound("cardFraudCategoryTypeCode.equals=" + DEFAULT_CARD_FRAUD_CATEGORY_TYPE_CODE);

        // Get all the cardFraudIncidentCategoryList where cardFraudCategoryTypeCode equals to UPDATED_CARD_FRAUD_CATEGORY_TYPE_CODE
        defaultCardFraudIncidentCategoryShouldNotBeFound("cardFraudCategoryTypeCode.equals=" + UPDATED_CARD_FRAUD_CATEGORY_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllCardFraudIncidentCategoriesByCardFraudCategoryTypeCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        cardFraudIncidentCategoryRepository.saveAndFlush(cardFraudIncidentCategory);

        // Get all the cardFraudIncidentCategoryList where cardFraudCategoryTypeCode not equals to DEFAULT_CARD_FRAUD_CATEGORY_TYPE_CODE
        defaultCardFraudIncidentCategoryShouldNotBeFound("cardFraudCategoryTypeCode.notEquals=" + DEFAULT_CARD_FRAUD_CATEGORY_TYPE_CODE);

        // Get all the cardFraudIncidentCategoryList where cardFraudCategoryTypeCode not equals to UPDATED_CARD_FRAUD_CATEGORY_TYPE_CODE
        defaultCardFraudIncidentCategoryShouldBeFound("cardFraudCategoryTypeCode.notEquals=" + UPDATED_CARD_FRAUD_CATEGORY_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllCardFraudIncidentCategoriesByCardFraudCategoryTypeCodeIsInShouldWork() throws Exception {
        // Initialize the database
        cardFraudIncidentCategoryRepository.saveAndFlush(cardFraudIncidentCategory);

        // Get all the cardFraudIncidentCategoryList where cardFraudCategoryTypeCode in DEFAULT_CARD_FRAUD_CATEGORY_TYPE_CODE or UPDATED_CARD_FRAUD_CATEGORY_TYPE_CODE
        defaultCardFraudIncidentCategoryShouldBeFound(
            "cardFraudCategoryTypeCode.in=" + DEFAULT_CARD_FRAUD_CATEGORY_TYPE_CODE + "," + UPDATED_CARD_FRAUD_CATEGORY_TYPE_CODE
        );

        // Get all the cardFraudIncidentCategoryList where cardFraudCategoryTypeCode equals to UPDATED_CARD_FRAUD_CATEGORY_TYPE_CODE
        defaultCardFraudIncidentCategoryShouldNotBeFound("cardFraudCategoryTypeCode.in=" + UPDATED_CARD_FRAUD_CATEGORY_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllCardFraudIncidentCategoriesByCardFraudCategoryTypeCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        cardFraudIncidentCategoryRepository.saveAndFlush(cardFraudIncidentCategory);

        // Get all the cardFraudIncidentCategoryList where cardFraudCategoryTypeCode is not null
        defaultCardFraudIncidentCategoryShouldBeFound("cardFraudCategoryTypeCode.specified=true");

        // Get all the cardFraudIncidentCategoryList where cardFraudCategoryTypeCode is null
        defaultCardFraudIncidentCategoryShouldNotBeFound("cardFraudCategoryTypeCode.specified=false");
    }

    @Test
    @Transactional
    void getAllCardFraudIncidentCategoriesByCardFraudCategoryTypeCodeContainsSomething() throws Exception {
        // Initialize the database
        cardFraudIncidentCategoryRepository.saveAndFlush(cardFraudIncidentCategory);

        // Get all the cardFraudIncidentCategoryList where cardFraudCategoryTypeCode contains DEFAULT_CARD_FRAUD_CATEGORY_TYPE_CODE
        defaultCardFraudIncidentCategoryShouldBeFound("cardFraudCategoryTypeCode.contains=" + DEFAULT_CARD_FRAUD_CATEGORY_TYPE_CODE);

        // Get all the cardFraudIncidentCategoryList where cardFraudCategoryTypeCode contains UPDATED_CARD_FRAUD_CATEGORY_TYPE_CODE
        defaultCardFraudIncidentCategoryShouldNotBeFound("cardFraudCategoryTypeCode.contains=" + UPDATED_CARD_FRAUD_CATEGORY_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllCardFraudIncidentCategoriesByCardFraudCategoryTypeCodeNotContainsSomething() throws Exception {
        // Initialize the database
        cardFraudIncidentCategoryRepository.saveAndFlush(cardFraudIncidentCategory);

        // Get all the cardFraudIncidentCategoryList where cardFraudCategoryTypeCode does not contain DEFAULT_CARD_FRAUD_CATEGORY_TYPE_CODE
        defaultCardFraudIncidentCategoryShouldNotBeFound(
            "cardFraudCategoryTypeCode.doesNotContain=" + DEFAULT_CARD_FRAUD_CATEGORY_TYPE_CODE
        );

        // Get all the cardFraudIncidentCategoryList where cardFraudCategoryTypeCode does not contain UPDATED_CARD_FRAUD_CATEGORY_TYPE_CODE
        defaultCardFraudIncidentCategoryShouldBeFound("cardFraudCategoryTypeCode.doesNotContain=" + UPDATED_CARD_FRAUD_CATEGORY_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllCardFraudIncidentCategoriesByCardFraudCategoryTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        cardFraudIncidentCategoryRepository.saveAndFlush(cardFraudIncidentCategory);

        // Get all the cardFraudIncidentCategoryList where cardFraudCategoryType equals to DEFAULT_CARD_FRAUD_CATEGORY_TYPE
        defaultCardFraudIncidentCategoryShouldBeFound("cardFraudCategoryType.equals=" + DEFAULT_CARD_FRAUD_CATEGORY_TYPE);

        // Get all the cardFraudIncidentCategoryList where cardFraudCategoryType equals to UPDATED_CARD_FRAUD_CATEGORY_TYPE
        defaultCardFraudIncidentCategoryShouldNotBeFound("cardFraudCategoryType.equals=" + UPDATED_CARD_FRAUD_CATEGORY_TYPE);
    }

    @Test
    @Transactional
    void getAllCardFraudIncidentCategoriesByCardFraudCategoryTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        cardFraudIncidentCategoryRepository.saveAndFlush(cardFraudIncidentCategory);

        // Get all the cardFraudIncidentCategoryList where cardFraudCategoryType not equals to DEFAULT_CARD_FRAUD_CATEGORY_TYPE
        defaultCardFraudIncidentCategoryShouldNotBeFound("cardFraudCategoryType.notEquals=" + DEFAULT_CARD_FRAUD_CATEGORY_TYPE);

        // Get all the cardFraudIncidentCategoryList where cardFraudCategoryType not equals to UPDATED_CARD_FRAUD_CATEGORY_TYPE
        defaultCardFraudIncidentCategoryShouldBeFound("cardFraudCategoryType.notEquals=" + UPDATED_CARD_FRAUD_CATEGORY_TYPE);
    }

    @Test
    @Transactional
    void getAllCardFraudIncidentCategoriesByCardFraudCategoryTypeIsInShouldWork() throws Exception {
        // Initialize the database
        cardFraudIncidentCategoryRepository.saveAndFlush(cardFraudIncidentCategory);

        // Get all the cardFraudIncidentCategoryList where cardFraudCategoryType in DEFAULT_CARD_FRAUD_CATEGORY_TYPE or UPDATED_CARD_FRAUD_CATEGORY_TYPE
        defaultCardFraudIncidentCategoryShouldBeFound(
            "cardFraudCategoryType.in=" + DEFAULT_CARD_FRAUD_CATEGORY_TYPE + "," + UPDATED_CARD_FRAUD_CATEGORY_TYPE
        );

        // Get all the cardFraudIncidentCategoryList where cardFraudCategoryType equals to UPDATED_CARD_FRAUD_CATEGORY_TYPE
        defaultCardFraudIncidentCategoryShouldNotBeFound("cardFraudCategoryType.in=" + UPDATED_CARD_FRAUD_CATEGORY_TYPE);
    }

    @Test
    @Transactional
    void getAllCardFraudIncidentCategoriesByCardFraudCategoryTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        cardFraudIncidentCategoryRepository.saveAndFlush(cardFraudIncidentCategory);

        // Get all the cardFraudIncidentCategoryList where cardFraudCategoryType is not null
        defaultCardFraudIncidentCategoryShouldBeFound("cardFraudCategoryType.specified=true");

        // Get all the cardFraudIncidentCategoryList where cardFraudCategoryType is null
        defaultCardFraudIncidentCategoryShouldNotBeFound("cardFraudCategoryType.specified=false");
    }

    @Test
    @Transactional
    void getAllCardFraudIncidentCategoriesByCardFraudCategoryTypeContainsSomething() throws Exception {
        // Initialize the database
        cardFraudIncidentCategoryRepository.saveAndFlush(cardFraudIncidentCategory);

        // Get all the cardFraudIncidentCategoryList where cardFraudCategoryType contains DEFAULT_CARD_FRAUD_CATEGORY_TYPE
        defaultCardFraudIncidentCategoryShouldBeFound("cardFraudCategoryType.contains=" + DEFAULT_CARD_FRAUD_CATEGORY_TYPE);

        // Get all the cardFraudIncidentCategoryList where cardFraudCategoryType contains UPDATED_CARD_FRAUD_CATEGORY_TYPE
        defaultCardFraudIncidentCategoryShouldNotBeFound("cardFraudCategoryType.contains=" + UPDATED_CARD_FRAUD_CATEGORY_TYPE);
    }

    @Test
    @Transactional
    void getAllCardFraudIncidentCategoriesByCardFraudCategoryTypeNotContainsSomething() throws Exception {
        // Initialize the database
        cardFraudIncidentCategoryRepository.saveAndFlush(cardFraudIncidentCategory);

        // Get all the cardFraudIncidentCategoryList where cardFraudCategoryType does not contain DEFAULT_CARD_FRAUD_CATEGORY_TYPE
        defaultCardFraudIncidentCategoryShouldNotBeFound("cardFraudCategoryType.doesNotContain=" + DEFAULT_CARD_FRAUD_CATEGORY_TYPE);

        // Get all the cardFraudIncidentCategoryList where cardFraudCategoryType does not contain UPDATED_CARD_FRAUD_CATEGORY_TYPE
        defaultCardFraudIncidentCategoryShouldBeFound("cardFraudCategoryType.doesNotContain=" + UPDATED_CARD_FRAUD_CATEGORY_TYPE);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCardFraudIncidentCategoryShouldBeFound(String filter) throws Exception {
        restCardFraudIncidentCategoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cardFraudIncidentCategory.getId().intValue())))
            .andExpect(jsonPath("$.[*].cardFraudCategoryTypeCode").value(hasItem(DEFAULT_CARD_FRAUD_CATEGORY_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].cardFraudCategoryType").value(hasItem(DEFAULT_CARD_FRAUD_CATEGORY_TYPE)))
            .andExpect(
                jsonPath("$.[*].cardFraudCategoryTypeDescription").value(hasItem(DEFAULT_CARD_FRAUD_CATEGORY_TYPE_DESCRIPTION.toString()))
            );

        // Check, that the count call also returns 1
        restCardFraudIncidentCategoryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCardFraudIncidentCategoryShouldNotBeFound(String filter) throws Exception {
        restCardFraudIncidentCategoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCardFraudIncidentCategoryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCardFraudIncidentCategory() throws Exception {
        // Get the cardFraudIncidentCategory
        restCardFraudIncidentCategoryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCardFraudIncidentCategory() throws Exception {
        // Initialize the database
        cardFraudIncidentCategoryRepository.saveAndFlush(cardFraudIncidentCategory);

        int databaseSizeBeforeUpdate = cardFraudIncidentCategoryRepository.findAll().size();

        // Update the cardFraudIncidentCategory
        CardFraudIncidentCategory updatedCardFraudIncidentCategory = cardFraudIncidentCategoryRepository
            .findById(cardFraudIncidentCategory.getId())
            .get();
        // Disconnect from session so that the updates on updatedCardFraudIncidentCategory are not directly saved in db
        em.detach(updatedCardFraudIncidentCategory);
        updatedCardFraudIncidentCategory
            .cardFraudCategoryTypeCode(UPDATED_CARD_FRAUD_CATEGORY_TYPE_CODE)
            .cardFraudCategoryType(UPDATED_CARD_FRAUD_CATEGORY_TYPE)
            .cardFraudCategoryTypeDescription(UPDATED_CARD_FRAUD_CATEGORY_TYPE_DESCRIPTION);
        CardFraudIncidentCategoryDTO cardFraudIncidentCategoryDTO = cardFraudIncidentCategoryMapper.toDto(updatedCardFraudIncidentCategory);

        restCardFraudIncidentCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cardFraudIncidentCategoryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cardFraudIncidentCategoryDTO))
            )
            .andExpect(status().isOk());

        // Validate the CardFraudIncidentCategory in the database
        List<CardFraudIncidentCategory> cardFraudIncidentCategoryList = cardFraudIncidentCategoryRepository.findAll();
        assertThat(cardFraudIncidentCategoryList).hasSize(databaseSizeBeforeUpdate);
        CardFraudIncidentCategory testCardFraudIncidentCategory = cardFraudIncidentCategoryList.get(
            cardFraudIncidentCategoryList.size() - 1
        );
        assertThat(testCardFraudIncidentCategory.getCardFraudCategoryTypeCode()).isEqualTo(UPDATED_CARD_FRAUD_CATEGORY_TYPE_CODE);
        assertThat(testCardFraudIncidentCategory.getCardFraudCategoryType()).isEqualTo(UPDATED_CARD_FRAUD_CATEGORY_TYPE);
        assertThat(testCardFraudIncidentCategory.getCardFraudCategoryTypeDescription())
            .isEqualTo(UPDATED_CARD_FRAUD_CATEGORY_TYPE_DESCRIPTION);

        // Validate the CardFraudIncidentCategory in Elasticsearch
        verify(mockCardFraudIncidentCategorySearchRepository).save(testCardFraudIncidentCategory);
    }

    @Test
    @Transactional
    void putNonExistingCardFraudIncidentCategory() throws Exception {
        int databaseSizeBeforeUpdate = cardFraudIncidentCategoryRepository.findAll().size();
        cardFraudIncidentCategory.setId(count.incrementAndGet());

        // Create the CardFraudIncidentCategory
        CardFraudIncidentCategoryDTO cardFraudIncidentCategoryDTO = cardFraudIncidentCategoryMapper.toDto(cardFraudIncidentCategory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCardFraudIncidentCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cardFraudIncidentCategoryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cardFraudIncidentCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CardFraudIncidentCategory in the database
        List<CardFraudIncidentCategory> cardFraudIncidentCategoryList = cardFraudIncidentCategoryRepository.findAll();
        assertThat(cardFraudIncidentCategoryList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CardFraudIncidentCategory in Elasticsearch
        verify(mockCardFraudIncidentCategorySearchRepository, times(0)).save(cardFraudIncidentCategory);
    }

    @Test
    @Transactional
    void putWithIdMismatchCardFraudIncidentCategory() throws Exception {
        int databaseSizeBeforeUpdate = cardFraudIncidentCategoryRepository.findAll().size();
        cardFraudIncidentCategory.setId(count.incrementAndGet());

        // Create the CardFraudIncidentCategory
        CardFraudIncidentCategoryDTO cardFraudIncidentCategoryDTO = cardFraudIncidentCategoryMapper.toDto(cardFraudIncidentCategory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCardFraudIncidentCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cardFraudIncidentCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CardFraudIncidentCategory in the database
        List<CardFraudIncidentCategory> cardFraudIncidentCategoryList = cardFraudIncidentCategoryRepository.findAll();
        assertThat(cardFraudIncidentCategoryList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CardFraudIncidentCategory in Elasticsearch
        verify(mockCardFraudIncidentCategorySearchRepository, times(0)).save(cardFraudIncidentCategory);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCardFraudIncidentCategory() throws Exception {
        int databaseSizeBeforeUpdate = cardFraudIncidentCategoryRepository.findAll().size();
        cardFraudIncidentCategory.setId(count.incrementAndGet());

        // Create the CardFraudIncidentCategory
        CardFraudIncidentCategoryDTO cardFraudIncidentCategoryDTO = cardFraudIncidentCategoryMapper.toDto(cardFraudIncidentCategory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCardFraudIncidentCategoryMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cardFraudIncidentCategoryDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CardFraudIncidentCategory in the database
        List<CardFraudIncidentCategory> cardFraudIncidentCategoryList = cardFraudIncidentCategoryRepository.findAll();
        assertThat(cardFraudIncidentCategoryList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CardFraudIncidentCategory in Elasticsearch
        verify(mockCardFraudIncidentCategorySearchRepository, times(0)).save(cardFraudIncidentCategory);
    }

    @Test
    @Transactional
    void partialUpdateCardFraudIncidentCategoryWithPatch() throws Exception {
        // Initialize the database
        cardFraudIncidentCategoryRepository.saveAndFlush(cardFraudIncidentCategory);

        int databaseSizeBeforeUpdate = cardFraudIncidentCategoryRepository.findAll().size();

        // Update the cardFraudIncidentCategory using partial update
        CardFraudIncidentCategory partialUpdatedCardFraudIncidentCategory = new CardFraudIncidentCategory();
        partialUpdatedCardFraudIncidentCategory.setId(cardFraudIncidentCategory.getId());

        partialUpdatedCardFraudIncidentCategory.cardFraudCategoryTypeCode(UPDATED_CARD_FRAUD_CATEGORY_TYPE_CODE);

        restCardFraudIncidentCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCardFraudIncidentCategory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCardFraudIncidentCategory))
            )
            .andExpect(status().isOk());

        // Validate the CardFraudIncidentCategory in the database
        List<CardFraudIncidentCategory> cardFraudIncidentCategoryList = cardFraudIncidentCategoryRepository.findAll();
        assertThat(cardFraudIncidentCategoryList).hasSize(databaseSizeBeforeUpdate);
        CardFraudIncidentCategory testCardFraudIncidentCategory = cardFraudIncidentCategoryList.get(
            cardFraudIncidentCategoryList.size() - 1
        );
        assertThat(testCardFraudIncidentCategory.getCardFraudCategoryTypeCode()).isEqualTo(UPDATED_CARD_FRAUD_CATEGORY_TYPE_CODE);
        assertThat(testCardFraudIncidentCategory.getCardFraudCategoryType()).isEqualTo(DEFAULT_CARD_FRAUD_CATEGORY_TYPE);
        assertThat(testCardFraudIncidentCategory.getCardFraudCategoryTypeDescription())
            .isEqualTo(DEFAULT_CARD_FRAUD_CATEGORY_TYPE_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateCardFraudIncidentCategoryWithPatch() throws Exception {
        // Initialize the database
        cardFraudIncidentCategoryRepository.saveAndFlush(cardFraudIncidentCategory);

        int databaseSizeBeforeUpdate = cardFraudIncidentCategoryRepository.findAll().size();

        // Update the cardFraudIncidentCategory using partial update
        CardFraudIncidentCategory partialUpdatedCardFraudIncidentCategory = new CardFraudIncidentCategory();
        partialUpdatedCardFraudIncidentCategory.setId(cardFraudIncidentCategory.getId());

        partialUpdatedCardFraudIncidentCategory
            .cardFraudCategoryTypeCode(UPDATED_CARD_FRAUD_CATEGORY_TYPE_CODE)
            .cardFraudCategoryType(UPDATED_CARD_FRAUD_CATEGORY_TYPE)
            .cardFraudCategoryTypeDescription(UPDATED_CARD_FRAUD_CATEGORY_TYPE_DESCRIPTION);

        restCardFraudIncidentCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCardFraudIncidentCategory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCardFraudIncidentCategory))
            )
            .andExpect(status().isOk());

        // Validate the CardFraudIncidentCategory in the database
        List<CardFraudIncidentCategory> cardFraudIncidentCategoryList = cardFraudIncidentCategoryRepository.findAll();
        assertThat(cardFraudIncidentCategoryList).hasSize(databaseSizeBeforeUpdate);
        CardFraudIncidentCategory testCardFraudIncidentCategory = cardFraudIncidentCategoryList.get(
            cardFraudIncidentCategoryList.size() - 1
        );
        assertThat(testCardFraudIncidentCategory.getCardFraudCategoryTypeCode()).isEqualTo(UPDATED_CARD_FRAUD_CATEGORY_TYPE_CODE);
        assertThat(testCardFraudIncidentCategory.getCardFraudCategoryType()).isEqualTo(UPDATED_CARD_FRAUD_CATEGORY_TYPE);
        assertThat(testCardFraudIncidentCategory.getCardFraudCategoryTypeDescription())
            .isEqualTo(UPDATED_CARD_FRAUD_CATEGORY_TYPE_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingCardFraudIncidentCategory() throws Exception {
        int databaseSizeBeforeUpdate = cardFraudIncidentCategoryRepository.findAll().size();
        cardFraudIncidentCategory.setId(count.incrementAndGet());

        // Create the CardFraudIncidentCategory
        CardFraudIncidentCategoryDTO cardFraudIncidentCategoryDTO = cardFraudIncidentCategoryMapper.toDto(cardFraudIncidentCategory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCardFraudIncidentCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, cardFraudIncidentCategoryDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cardFraudIncidentCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CardFraudIncidentCategory in the database
        List<CardFraudIncidentCategory> cardFraudIncidentCategoryList = cardFraudIncidentCategoryRepository.findAll();
        assertThat(cardFraudIncidentCategoryList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CardFraudIncidentCategory in Elasticsearch
        verify(mockCardFraudIncidentCategorySearchRepository, times(0)).save(cardFraudIncidentCategory);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCardFraudIncidentCategory() throws Exception {
        int databaseSizeBeforeUpdate = cardFraudIncidentCategoryRepository.findAll().size();
        cardFraudIncidentCategory.setId(count.incrementAndGet());

        // Create the CardFraudIncidentCategory
        CardFraudIncidentCategoryDTO cardFraudIncidentCategoryDTO = cardFraudIncidentCategoryMapper.toDto(cardFraudIncidentCategory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCardFraudIncidentCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cardFraudIncidentCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CardFraudIncidentCategory in the database
        List<CardFraudIncidentCategory> cardFraudIncidentCategoryList = cardFraudIncidentCategoryRepository.findAll();
        assertThat(cardFraudIncidentCategoryList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CardFraudIncidentCategory in Elasticsearch
        verify(mockCardFraudIncidentCategorySearchRepository, times(0)).save(cardFraudIncidentCategory);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCardFraudIncidentCategory() throws Exception {
        int databaseSizeBeforeUpdate = cardFraudIncidentCategoryRepository.findAll().size();
        cardFraudIncidentCategory.setId(count.incrementAndGet());

        // Create the CardFraudIncidentCategory
        CardFraudIncidentCategoryDTO cardFraudIncidentCategoryDTO = cardFraudIncidentCategoryMapper.toDto(cardFraudIncidentCategory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCardFraudIncidentCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cardFraudIncidentCategoryDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CardFraudIncidentCategory in the database
        List<CardFraudIncidentCategory> cardFraudIncidentCategoryList = cardFraudIncidentCategoryRepository.findAll();
        assertThat(cardFraudIncidentCategoryList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CardFraudIncidentCategory in Elasticsearch
        verify(mockCardFraudIncidentCategorySearchRepository, times(0)).save(cardFraudIncidentCategory);
    }

    @Test
    @Transactional
    void deleteCardFraudIncidentCategory() throws Exception {
        // Initialize the database
        cardFraudIncidentCategoryRepository.saveAndFlush(cardFraudIncidentCategory);

        int databaseSizeBeforeDelete = cardFraudIncidentCategoryRepository.findAll().size();

        // Delete the cardFraudIncidentCategory
        restCardFraudIncidentCategoryMockMvc
            .perform(delete(ENTITY_API_URL_ID, cardFraudIncidentCategory.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CardFraudIncidentCategory> cardFraudIncidentCategoryList = cardFraudIncidentCategoryRepository.findAll();
        assertThat(cardFraudIncidentCategoryList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the CardFraudIncidentCategory in Elasticsearch
        verify(mockCardFraudIncidentCategorySearchRepository, times(1)).deleteById(cardFraudIncidentCategory.getId());
    }

    @Test
    @Transactional
    void searchCardFraudIncidentCategory() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        cardFraudIncidentCategoryRepository.saveAndFlush(cardFraudIncidentCategory);
        when(mockCardFraudIncidentCategorySearchRepository.search("id:" + cardFraudIncidentCategory.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(cardFraudIncidentCategory), PageRequest.of(0, 1), 1));

        // Search the cardFraudIncidentCategory
        restCardFraudIncidentCategoryMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + cardFraudIncidentCategory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cardFraudIncidentCategory.getId().intValue())))
            .andExpect(jsonPath("$.[*].cardFraudCategoryTypeCode").value(hasItem(DEFAULT_CARD_FRAUD_CATEGORY_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].cardFraudCategoryType").value(hasItem(DEFAULT_CARD_FRAUD_CATEGORY_TYPE)))
            .andExpect(
                jsonPath("$.[*].cardFraudCategoryTypeDescription").value(hasItem(DEFAULT_CARD_FRAUD_CATEGORY_TYPE_DESCRIPTION.toString()))
            );
    }
}
