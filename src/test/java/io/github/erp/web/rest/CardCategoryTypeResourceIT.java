package io.github.erp.web.rest;

/*-
 * Erp System - Mark X No 3 (Jehoiada Series) Server ver 1.7.3
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
import io.github.erp.domain.CardCategoryType;
import io.github.erp.domain.enumeration.CardCategoryFlag;
import io.github.erp.repository.CardCategoryTypeRepository;
import io.github.erp.repository.search.CardCategoryTypeSearchRepository;
import io.github.erp.service.criteria.CardCategoryTypeCriteria;
import io.github.erp.service.dto.CardCategoryTypeDTO;
import io.github.erp.service.mapper.CardCategoryTypeMapper;
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
 * Integration tests for the {@link CardCategoryTypeResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class CardCategoryTypeResourceIT {

    private static final CardCategoryFlag DEFAULT_CARD_CATEGORY_FLAG = CardCategoryFlag.L;
    private static final CardCategoryFlag UPDATED_CARD_CATEGORY_FLAG = CardCategoryFlag.I;

    private static final String DEFAULT_CARD_CATEGORY_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_CARD_CATEGORY_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_CARD_CATEGORY_DETAILS = "AAAAAAAAAA";
    private static final String UPDATED_CARD_CATEGORY_DETAILS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/card-category-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/card-category-types";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CardCategoryTypeRepository cardCategoryTypeRepository;

    @Autowired
    private CardCategoryTypeMapper cardCategoryTypeMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.CardCategoryTypeSearchRepositoryMockConfiguration
     */
    @Autowired
    private CardCategoryTypeSearchRepository mockCardCategoryTypeSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCardCategoryTypeMockMvc;

    private CardCategoryType cardCategoryType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CardCategoryType createEntity(EntityManager em) {
        CardCategoryType cardCategoryType = new CardCategoryType()
            .cardCategoryFlag(DEFAULT_CARD_CATEGORY_FLAG)
            .cardCategoryDescription(DEFAULT_CARD_CATEGORY_DESCRIPTION)
            .cardCategoryDetails(DEFAULT_CARD_CATEGORY_DETAILS);
        return cardCategoryType;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CardCategoryType createUpdatedEntity(EntityManager em) {
        CardCategoryType cardCategoryType = new CardCategoryType()
            .cardCategoryFlag(UPDATED_CARD_CATEGORY_FLAG)
            .cardCategoryDescription(UPDATED_CARD_CATEGORY_DESCRIPTION)
            .cardCategoryDetails(UPDATED_CARD_CATEGORY_DETAILS);
        return cardCategoryType;
    }

    @BeforeEach
    public void initTest() {
        cardCategoryType = createEntity(em);
    }

    @Test
    @Transactional
    void createCardCategoryType() throws Exception {
        int databaseSizeBeforeCreate = cardCategoryTypeRepository.findAll().size();
        // Create the CardCategoryType
        CardCategoryTypeDTO cardCategoryTypeDTO = cardCategoryTypeMapper.toDto(cardCategoryType);
        restCardCategoryTypeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cardCategoryTypeDTO))
            )
            .andExpect(status().isCreated());

        // Validate the CardCategoryType in the database
        List<CardCategoryType> cardCategoryTypeList = cardCategoryTypeRepository.findAll();
        assertThat(cardCategoryTypeList).hasSize(databaseSizeBeforeCreate + 1);
        CardCategoryType testCardCategoryType = cardCategoryTypeList.get(cardCategoryTypeList.size() - 1);
        assertThat(testCardCategoryType.getCardCategoryFlag()).isEqualTo(DEFAULT_CARD_CATEGORY_FLAG);
        assertThat(testCardCategoryType.getCardCategoryDescription()).isEqualTo(DEFAULT_CARD_CATEGORY_DESCRIPTION);
        assertThat(testCardCategoryType.getCardCategoryDetails()).isEqualTo(DEFAULT_CARD_CATEGORY_DETAILS);

        // Validate the CardCategoryType in Elasticsearch
        verify(mockCardCategoryTypeSearchRepository, times(1)).save(testCardCategoryType);
    }

    @Test
    @Transactional
    void createCardCategoryTypeWithExistingId() throws Exception {
        // Create the CardCategoryType with an existing ID
        cardCategoryType.setId(1L);
        CardCategoryTypeDTO cardCategoryTypeDTO = cardCategoryTypeMapper.toDto(cardCategoryType);

        int databaseSizeBeforeCreate = cardCategoryTypeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCardCategoryTypeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cardCategoryTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CardCategoryType in the database
        List<CardCategoryType> cardCategoryTypeList = cardCategoryTypeRepository.findAll();
        assertThat(cardCategoryTypeList).hasSize(databaseSizeBeforeCreate);

        // Validate the CardCategoryType in Elasticsearch
        verify(mockCardCategoryTypeSearchRepository, times(0)).save(cardCategoryType);
    }

    @Test
    @Transactional
    void checkCardCategoryFlagIsRequired() throws Exception {
        int databaseSizeBeforeTest = cardCategoryTypeRepository.findAll().size();
        // set the field null
        cardCategoryType.setCardCategoryFlag(null);

        // Create the CardCategoryType, which fails.
        CardCategoryTypeDTO cardCategoryTypeDTO = cardCategoryTypeMapper.toDto(cardCategoryType);

        restCardCategoryTypeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cardCategoryTypeDTO))
            )
            .andExpect(status().isBadRequest());

        List<CardCategoryType> cardCategoryTypeList = cardCategoryTypeRepository.findAll();
        assertThat(cardCategoryTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCardCategoryDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = cardCategoryTypeRepository.findAll().size();
        // set the field null
        cardCategoryType.setCardCategoryDescription(null);

        // Create the CardCategoryType, which fails.
        CardCategoryTypeDTO cardCategoryTypeDTO = cardCategoryTypeMapper.toDto(cardCategoryType);

        restCardCategoryTypeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cardCategoryTypeDTO))
            )
            .andExpect(status().isBadRequest());

        List<CardCategoryType> cardCategoryTypeList = cardCategoryTypeRepository.findAll();
        assertThat(cardCategoryTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCardCategoryTypes() throws Exception {
        // Initialize the database
        cardCategoryTypeRepository.saveAndFlush(cardCategoryType);

        // Get all the cardCategoryTypeList
        restCardCategoryTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cardCategoryType.getId().intValue())))
            .andExpect(jsonPath("$.[*].cardCategoryFlag").value(hasItem(DEFAULT_CARD_CATEGORY_FLAG.toString())))
            .andExpect(jsonPath("$.[*].cardCategoryDescription").value(hasItem(DEFAULT_CARD_CATEGORY_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].cardCategoryDetails").value(hasItem(DEFAULT_CARD_CATEGORY_DETAILS.toString())));
    }

    @Test
    @Transactional
    void getCardCategoryType() throws Exception {
        // Initialize the database
        cardCategoryTypeRepository.saveAndFlush(cardCategoryType);

        // Get the cardCategoryType
        restCardCategoryTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, cardCategoryType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cardCategoryType.getId().intValue()))
            .andExpect(jsonPath("$.cardCategoryFlag").value(DEFAULT_CARD_CATEGORY_FLAG.toString()))
            .andExpect(jsonPath("$.cardCategoryDescription").value(DEFAULT_CARD_CATEGORY_DESCRIPTION))
            .andExpect(jsonPath("$.cardCategoryDetails").value(DEFAULT_CARD_CATEGORY_DETAILS.toString()));
    }

    @Test
    @Transactional
    void getCardCategoryTypesByIdFiltering() throws Exception {
        // Initialize the database
        cardCategoryTypeRepository.saveAndFlush(cardCategoryType);

        Long id = cardCategoryType.getId();

        defaultCardCategoryTypeShouldBeFound("id.equals=" + id);
        defaultCardCategoryTypeShouldNotBeFound("id.notEquals=" + id);

        defaultCardCategoryTypeShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCardCategoryTypeShouldNotBeFound("id.greaterThan=" + id);

        defaultCardCategoryTypeShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCardCategoryTypeShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCardCategoryTypesByCardCategoryFlagIsEqualToSomething() throws Exception {
        // Initialize the database
        cardCategoryTypeRepository.saveAndFlush(cardCategoryType);

        // Get all the cardCategoryTypeList where cardCategoryFlag equals to DEFAULT_CARD_CATEGORY_FLAG
        defaultCardCategoryTypeShouldBeFound("cardCategoryFlag.equals=" + DEFAULT_CARD_CATEGORY_FLAG);

        // Get all the cardCategoryTypeList where cardCategoryFlag equals to UPDATED_CARD_CATEGORY_FLAG
        defaultCardCategoryTypeShouldNotBeFound("cardCategoryFlag.equals=" + UPDATED_CARD_CATEGORY_FLAG);
    }

    @Test
    @Transactional
    void getAllCardCategoryTypesByCardCategoryFlagIsNotEqualToSomething() throws Exception {
        // Initialize the database
        cardCategoryTypeRepository.saveAndFlush(cardCategoryType);

        // Get all the cardCategoryTypeList where cardCategoryFlag not equals to DEFAULT_CARD_CATEGORY_FLAG
        defaultCardCategoryTypeShouldNotBeFound("cardCategoryFlag.notEquals=" + DEFAULT_CARD_CATEGORY_FLAG);

        // Get all the cardCategoryTypeList where cardCategoryFlag not equals to UPDATED_CARD_CATEGORY_FLAG
        defaultCardCategoryTypeShouldBeFound("cardCategoryFlag.notEquals=" + UPDATED_CARD_CATEGORY_FLAG);
    }

    @Test
    @Transactional
    void getAllCardCategoryTypesByCardCategoryFlagIsInShouldWork() throws Exception {
        // Initialize the database
        cardCategoryTypeRepository.saveAndFlush(cardCategoryType);

        // Get all the cardCategoryTypeList where cardCategoryFlag in DEFAULT_CARD_CATEGORY_FLAG or UPDATED_CARD_CATEGORY_FLAG
        defaultCardCategoryTypeShouldBeFound("cardCategoryFlag.in=" + DEFAULT_CARD_CATEGORY_FLAG + "," + UPDATED_CARD_CATEGORY_FLAG);

        // Get all the cardCategoryTypeList where cardCategoryFlag equals to UPDATED_CARD_CATEGORY_FLAG
        defaultCardCategoryTypeShouldNotBeFound("cardCategoryFlag.in=" + UPDATED_CARD_CATEGORY_FLAG);
    }

    @Test
    @Transactional
    void getAllCardCategoryTypesByCardCategoryFlagIsNullOrNotNull() throws Exception {
        // Initialize the database
        cardCategoryTypeRepository.saveAndFlush(cardCategoryType);

        // Get all the cardCategoryTypeList where cardCategoryFlag is not null
        defaultCardCategoryTypeShouldBeFound("cardCategoryFlag.specified=true");

        // Get all the cardCategoryTypeList where cardCategoryFlag is null
        defaultCardCategoryTypeShouldNotBeFound("cardCategoryFlag.specified=false");
    }

    @Test
    @Transactional
    void getAllCardCategoryTypesByCardCategoryDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        cardCategoryTypeRepository.saveAndFlush(cardCategoryType);

        // Get all the cardCategoryTypeList where cardCategoryDescription equals to DEFAULT_CARD_CATEGORY_DESCRIPTION
        defaultCardCategoryTypeShouldBeFound("cardCategoryDescription.equals=" + DEFAULT_CARD_CATEGORY_DESCRIPTION);

        // Get all the cardCategoryTypeList where cardCategoryDescription equals to UPDATED_CARD_CATEGORY_DESCRIPTION
        defaultCardCategoryTypeShouldNotBeFound("cardCategoryDescription.equals=" + UPDATED_CARD_CATEGORY_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllCardCategoryTypesByCardCategoryDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        cardCategoryTypeRepository.saveAndFlush(cardCategoryType);

        // Get all the cardCategoryTypeList where cardCategoryDescription not equals to DEFAULT_CARD_CATEGORY_DESCRIPTION
        defaultCardCategoryTypeShouldNotBeFound("cardCategoryDescription.notEquals=" + DEFAULT_CARD_CATEGORY_DESCRIPTION);

        // Get all the cardCategoryTypeList where cardCategoryDescription not equals to UPDATED_CARD_CATEGORY_DESCRIPTION
        defaultCardCategoryTypeShouldBeFound("cardCategoryDescription.notEquals=" + UPDATED_CARD_CATEGORY_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllCardCategoryTypesByCardCategoryDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        cardCategoryTypeRepository.saveAndFlush(cardCategoryType);

        // Get all the cardCategoryTypeList where cardCategoryDescription in DEFAULT_CARD_CATEGORY_DESCRIPTION or UPDATED_CARD_CATEGORY_DESCRIPTION
        defaultCardCategoryTypeShouldBeFound(
            "cardCategoryDescription.in=" + DEFAULT_CARD_CATEGORY_DESCRIPTION + "," + UPDATED_CARD_CATEGORY_DESCRIPTION
        );

        // Get all the cardCategoryTypeList where cardCategoryDescription equals to UPDATED_CARD_CATEGORY_DESCRIPTION
        defaultCardCategoryTypeShouldNotBeFound("cardCategoryDescription.in=" + UPDATED_CARD_CATEGORY_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllCardCategoryTypesByCardCategoryDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        cardCategoryTypeRepository.saveAndFlush(cardCategoryType);

        // Get all the cardCategoryTypeList where cardCategoryDescription is not null
        defaultCardCategoryTypeShouldBeFound("cardCategoryDescription.specified=true");

        // Get all the cardCategoryTypeList where cardCategoryDescription is null
        defaultCardCategoryTypeShouldNotBeFound("cardCategoryDescription.specified=false");
    }

    @Test
    @Transactional
    void getAllCardCategoryTypesByCardCategoryDescriptionContainsSomething() throws Exception {
        // Initialize the database
        cardCategoryTypeRepository.saveAndFlush(cardCategoryType);

        // Get all the cardCategoryTypeList where cardCategoryDescription contains DEFAULT_CARD_CATEGORY_DESCRIPTION
        defaultCardCategoryTypeShouldBeFound("cardCategoryDescription.contains=" + DEFAULT_CARD_CATEGORY_DESCRIPTION);

        // Get all the cardCategoryTypeList where cardCategoryDescription contains UPDATED_CARD_CATEGORY_DESCRIPTION
        defaultCardCategoryTypeShouldNotBeFound("cardCategoryDescription.contains=" + UPDATED_CARD_CATEGORY_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllCardCategoryTypesByCardCategoryDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        cardCategoryTypeRepository.saveAndFlush(cardCategoryType);

        // Get all the cardCategoryTypeList where cardCategoryDescription does not contain DEFAULT_CARD_CATEGORY_DESCRIPTION
        defaultCardCategoryTypeShouldNotBeFound("cardCategoryDescription.doesNotContain=" + DEFAULT_CARD_CATEGORY_DESCRIPTION);

        // Get all the cardCategoryTypeList where cardCategoryDescription does not contain UPDATED_CARD_CATEGORY_DESCRIPTION
        defaultCardCategoryTypeShouldBeFound("cardCategoryDescription.doesNotContain=" + UPDATED_CARD_CATEGORY_DESCRIPTION);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCardCategoryTypeShouldBeFound(String filter) throws Exception {
        restCardCategoryTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cardCategoryType.getId().intValue())))
            .andExpect(jsonPath("$.[*].cardCategoryFlag").value(hasItem(DEFAULT_CARD_CATEGORY_FLAG.toString())))
            .andExpect(jsonPath("$.[*].cardCategoryDescription").value(hasItem(DEFAULT_CARD_CATEGORY_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].cardCategoryDetails").value(hasItem(DEFAULT_CARD_CATEGORY_DETAILS.toString())));

        // Check, that the count call also returns 1
        restCardCategoryTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCardCategoryTypeShouldNotBeFound(String filter) throws Exception {
        restCardCategoryTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCardCategoryTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCardCategoryType() throws Exception {
        // Get the cardCategoryType
        restCardCategoryTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCardCategoryType() throws Exception {
        // Initialize the database
        cardCategoryTypeRepository.saveAndFlush(cardCategoryType);

        int databaseSizeBeforeUpdate = cardCategoryTypeRepository.findAll().size();

        // Update the cardCategoryType
        CardCategoryType updatedCardCategoryType = cardCategoryTypeRepository.findById(cardCategoryType.getId()).get();
        // Disconnect from session so that the updates on updatedCardCategoryType are not directly saved in db
        em.detach(updatedCardCategoryType);
        updatedCardCategoryType
            .cardCategoryFlag(UPDATED_CARD_CATEGORY_FLAG)
            .cardCategoryDescription(UPDATED_CARD_CATEGORY_DESCRIPTION)
            .cardCategoryDetails(UPDATED_CARD_CATEGORY_DETAILS);
        CardCategoryTypeDTO cardCategoryTypeDTO = cardCategoryTypeMapper.toDto(updatedCardCategoryType);

        restCardCategoryTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cardCategoryTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cardCategoryTypeDTO))
            )
            .andExpect(status().isOk());

        // Validate the CardCategoryType in the database
        List<CardCategoryType> cardCategoryTypeList = cardCategoryTypeRepository.findAll();
        assertThat(cardCategoryTypeList).hasSize(databaseSizeBeforeUpdate);
        CardCategoryType testCardCategoryType = cardCategoryTypeList.get(cardCategoryTypeList.size() - 1);
        assertThat(testCardCategoryType.getCardCategoryFlag()).isEqualTo(UPDATED_CARD_CATEGORY_FLAG);
        assertThat(testCardCategoryType.getCardCategoryDescription()).isEqualTo(UPDATED_CARD_CATEGORY_DESCRIPTION);
        assertThat(testCardCategoryType.getCardCategoryDetails()).isEqualTo(UPDATED_CARD_CATEGORY_DETAILS);

        // Validate the CardCategoryType in Elasticsearch
        verify(mockCardCategoryTypeSearchRepository).save(testCardCategoryType);
    }

    @Test
    @Transactional
    void putNonExistingCardCategoryType() throws Exception {
        int databaseSizeBeforeUpdate = cardCategoryTypeRepository.findAll().size();
        cardCategoryType.setId(count.incrementAndGet());

        // Create the CardCategoryType
        CardCategoryTypeDTO cardCategoryTypeDTO = cardCategoryTypeMapper.toDto(cardCategoryType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCardCategoryTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cardCategoryTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cardCategoryTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CardCategoryType in the database
        List<CardCategoryType> cardCategoryTypeList = cardCategoryTypeRepository.findAll();
        assertThat(cardCategoryTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CardCategoryType in Elasticsearch
        verify(mockCardCategoryTypeSearchRepository, times(0)).save(cardCategoryType);
    }

    @Test
    @Transactional
    void putWithIdMismatchCardCategoryType() throws Exception {
        int databaseSizeBeforeUpdate = cardCategoryTypeRepository.findAll().size();
        cardCategoryType.setId(count.incrementAndGet());

        // Create the CardCategoryType
        CardCategoryTypeDTO cardCategoryTypeDTO = cardCategoryTypeMapper.toDto(cardCategoryType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCardCategoryTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cardCategoryTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CardCategoryType in the database
        List<CardCategoryType> cardCategoryTypeList = cardCategoryTypeRepository.findAll();
        assertThat(cardCategoryTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CardCategoryType in Elasticsearch
        verify(mockCardCategoryTypeSearchRepository, times(0)).save(cardCategoryType);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCardCategoryType() throws Exception {
        int databaseSizeBeforeUpdate = cardCategoryTypeRepository.findAll().size();
        cardCategoryType.setId(count.incrementAndGet());

        // Create the CardCategoryType
        CardCategoryTypeDTO cardCategoryTypeDTO = cardCategoryTypeMapper.toDto(cardCategoryType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCardCategoryTypeMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cardCategoryTypeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CardCategoryType in the database
        List<CardCategoryType> cardCategoryTypeList = cardCategoryTypeRepository.findAll();
        assertThat(cardCategoryTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CardCategoryType in Elasticsearch
        verify(mockCardCategoryTypeSearchRepository, times(0)).save(cardCategoryType);
    }

    @Test
    @Transactional
    void partialUpdateCardCategoryTypeWithPatch() throws Exception {
        // Initialize the database
        cardCategoryTypeRepository.saveAndFlush(cardCategoryType);

        int databaseSizeBeforeUpdate = cardCategoryTypeRepository.findAll().size();

        // Update the cardCategoryType using partial update
        CardCategoryType partialUpdatedCardCategoryType = new CardCategoryType();
        partialUpdatedCardCategoryType.setId(cardCategoryType.getId());

        partialUpdatedCardCategoryType.cardCategoryDetails(UPDATED_CARD_CATEGORY_DETAILS);

        restCardCategoryTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCardCategoryType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCardCategoryType))
            )
            .andExpect(status().isOk());

        // Validate the CardCategoryType in the database
        List<CardCategoryType> cardCategoryTypeList = cardCategoryTypeRepository.findAll();
        assertThat(cardCategoryTypeList).hasSize(databaseSizeBeforeUpdate);
        CardCategoryType testCardCategoryType = cardCategoryTypeList.get(cardCategoryTypeList.size() - 1);
        assertThat(testCardCategoryType.getCardCategoryFlag()).isEqualTo(DEFAULT_CARD_CATEGORY_FLAG);
        assertThat(testCardCategoryType.getCardCategoryDescription()).isEqualTo(DEFAULT_CARD_CATEGORY_DESCRIPTION);
        assertThat(testCardCategoryType.getCardCategoryDetails()).isEqualTo(UPDATED_CARD_CATEGORY_DETAILS);
    }

    @Test
    @Transactional
    void fullUpdateCardCategoryTypeWithPatch() throws Exception {
        // Initialize the database
        cardCategoryTypeRepository.saveAndFlush(cardCategoryType);

        int databaseSizeBeforeUpdate = cardCategoryTypeRepository.findAll().size();

        // Update the cardCategoryType using partial update
        CardCategoryType partialUpdatedCardCategoryType = new CardCategoryType();
        partialUpdatedCardCategoryType.setId(cardCategoryType.getId());

        partialUpdatedCardCategoryType
            .cardCategoryFlag(UPDATED_CARD_CATEGORY_FLAG)
            .cardCategoryDescription(UPDATED_CARD_CATEGORY_DESCRIPTION)
            .cardCategoryDetails(UPDATED_CARD_CATEGORY_DETAILS);

        restCardCategoryTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCardCategoryType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCardCategoryType))
            )
            .andExpect(status().isOk());

        // Validate the CardCategoryType in the database
        List<CardCategoryType> cardCategoryTypeList = cardCategoryTypeRepository.findAll();
        assertThat(cardCategoryTypeList).hasSize(databaseSizeBeforeUpdate);
        CardCategoryType testCardCategoryType = cardCategoryTypeList.get(cardCategoryTypeList.size() - 1);
        assertThat(testCardCategoryType.getCardCategoryFlag()).isEqualTo(UPDATED_CARD_CATEGORY_FLAG);
        assertThat(testCardCategoryType.getCardCategoryDescription()).isEqualTo(UPDATED_CARD_CATEGORY_DESCRIPTION);
        assertThat(testCardCategoryType.getCardCategoryDetails()).isEqualTo(UPDATED_CARD_CATEGORY_DETAILS);
    }

    @Test
    @Transactional
    void patchNonExistingCardCategoryType() throws Exception {
        int databaseSizeBeforeUpdate = cardCategoryTypeRepository.findAll().size();
        cardCategoryType.setId(count.incrementAndGet());

        // Create the CardCategoryType
        CardCategoryTypeDTO cardCategoryTypeDTO = cardCategoryTypeMapper.toDto(cardCategoryType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCardCategoryTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, cardCategoryTypeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cardCategoryTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CardCategoryType in the database
        List<CardCategoryType> cardCategoryTypeList = cardCategoryTypeRepository.findAll();
        assertThat(cardCategoryTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CardCategoryType in Elasticsearch
        verify(mockCardCategoryTypeSearchRepository, times(0)).save(cardCategoryType);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCardCategoryType() throws Exception {
        int databaseSizeBeforeUpdate = cardCategoryTypeRepository.findAll().size();
        cardCategoryType.setId(count.incrementAndGet());

        // Create the CardCategoryType
        CardCategoryTypeDTO cardCategoryTypeDTO = cardCategoryTypeMapper.toDto(cardCategoryType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCardCategoryTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cardCategoryTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CardCategoryType in the database
        List<CardCategoryType> cardCategoryTypeList = cardCategoryTypeRepository.findAll();
        assertThat(cardCategoryTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CardCategoryType in Elasticsearch
        verify(mockCardCategoryTypeSearchRepository, times(0)).save(cardCategoryType);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCardCategoryType() throws Exception {
        int databaseSizeBeforeUpdate = cardCategoryTypeRepository.findAll().size();
        cardCategoryType.setId(count.incrementAndGet());

        // Create the CardCategoryType
        CardCategoryTypeDTO cardCategoryTypeDTO = cardCategoryTypeMapper.toDto(cardCategoryType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCardCategoryTypeMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cardCategoryTypeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CardCategoryType in the database
        List<CardCategoryType> cardCategoryTypeList = cardCategoryTypeRepository.findAll();
        assertThat(cardCategoryTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CardCategoryType in Elasticsearch
        verify(mockCardCategoryTypeSearchRepository, times(0)).save(cardCategoryType);
    }

    @Test
    @Transactional
    void deleteCardCategoryType() throws Exception {
        // Initialize the database
        cardCategoryTypeRepository.saveAndFlush(cardCategoryType);

        int databaseSizeBeforeDelete = cardCategoryTypeRepository.findAll().size();

        // Delete the cardCategoryType
        restCardCategoryTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, cardCategoryType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CardCategoryType> cardCategoryTypeList = cardCategoryTypeRepository.findAll();
        assertThat(cardCategoryTypeList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the CardCategoryType in Elasticsearch
        verify(mockCardCategoryTypeSearchRepository, times(1)).deleteById(cardCategoryType.getId());
    }

    @Test
    @Transactional
    void searchCardCategoryType() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        cardCategoryTypeRepository.saveAndFlush(cardCategoryType);
        when(mockCardCategoryTypeSearchRepository.search("id:" + cardCategoryType.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(cardCategoryType), PageRequest.of(0, 1), 1));

        // Search the cardCategoryType
        restCardCategoryTypeMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + cardCategoryType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cardCategoryType.getId().intValue())))
            .andExpect(jsonPath("$.[*].cardCategoryFlag").value(hasItem(DEFAULT_CARD_CATEGORY_FLAG.toString())))
            .andExpect(jsonPath("$.[*].cardCategoryDescription").value(hasItem(DEFAULT_CARD_CATEGORY_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].cardCategoryDetails").value(hasItem(DEFAULT_CARD_CATEGORY_DETAILS.toString())));
    }
}
