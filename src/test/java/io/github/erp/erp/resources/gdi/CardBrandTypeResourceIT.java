package io.github.erp.erp.resources.gdi;

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
import io.github.erp.IntegrationTest;
import io.github.erp.domain.CardBrandType;
import io.github.erp.repository.CardBrandTypeRepository;
import io.github.erp.repository.search.CardBrandTypeSearchRepository;
import io.github.erp.service.dto.CardBrandTypeDTO;
import io.github.erp.service.mapper.CardBrandTypeMapper;
import io.github.erp.web.rest.CardBrandTypeResource;
import io.github.erp.web.rest.TestUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
 * Integration tests for the {@link CardBrandTypeResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser(roles = {"GRANULAR_REPORTS_USER"})
class CardBrandTypeResourceIT {

    private static final String DEFAULT_CARD_BRAND_TYPE_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CARD_BRAND_TYPE_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_CARD_BRAND_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_CARD_BRAND_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_CARD_BRAND_TYPE_DETAILS = "AAAAAAAAAA";
    private static final String UPDATED_CARD_BRAND_TYPE_DETAILS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/granular-data/card-brand-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/granular-data/_search/card-brand-types";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CardBrandTypeRepository cardBrandTypeRepository;

    @Autowired
    private CardBrandTypeMapper cardBrandTypeMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.CardBrandTypeSearchRepositoryMockConfiguration
     */
    @Autowired
    private CardBrandTypeSearchRepository mockCardBrandTypeSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCardBrandTypeMockMvc;

    private CardBrandType cardBrandType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CardBrandType createEntity(EntityManager em) {
        CardBrandType cardBrandType = new CardBrandType()
            .cardBrandTypeCode(DEFAULT_CARD_BRAND_TYPE_CODE)
            .cardBrandType(DEFAULT_CARD_BRAND_TYPE)
            .cardBrandTypeDetails(DEFAULT_CARD_BRAND_TYPE_DETAILS);
        return cardBrandType;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CardBrandType createUpdatedEntity(EntityManager em) {
        CardBrandType cardBrandType = new CardBrandType()
            .cardBrandTypeCode(UPDATED_CARD_BRAND_TYPE_CODE)
            .cardBrandType(UPDATED_CARD_BRAND_TYPE)
            .cardBrandTypeDetails(UPDATED_CARD_BRAND_TYPE_DETAILS);
        return cardBrandType;
    }

    @BeforeEach
    public void initTest() {
        cardBrandType = createEntity(em);
    }

    @Test
    @Transactional
    void createCardBrandType() throws Exception {
        int databaseSizeBeforeCreate = cardBrandTypeRepository.findAll().size();
        // Create the CardBrandType
        CardBrandTypeDTO cardBrandTypeDTO = cardBrandTypeMapper.toDto(cardBrandType);
        restCardBrandTypeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cardBrandTypeDTO))
            )
            .andExpect(status().isCreated());

        // Validate the CardBrandType in the database
        List<CardBrandType> cardBrandTypeList = cardBrandTypeRepository.findAll();
        assertThat(cardBrandTypeList).hasSize(databaseSizeBeforeCreate + 1);
        CardBrandType testCardBrandType = cardBrandTypeList.get(cardBrandTypeList.size() - 1);
        assertThat(testCardBrandType.getCardBrandTypeCode()).isEqualTo(DEFAULT_CARD_BRAND_TYPE_CODE);
        assertThat(testCardBrandType.getCardBrandType()).isEqualTo(DEFAULT_CARD_BRAND_TYPE);
        assertThat(testCardBrandType.getCardBrandTypeDetails()).isEqualTo(DEFAULT_CARD_BRAND_TYPE_DETAILS);

        // Validate the CardBrandType in Elasticsearch
        verify(mockCardBrandTypeSearchRepository, times(1)).save(testCardBrandType);
    }

    @Test
    @Transactional
    void createCardBrandTypeWithExistingId() throws Exception {
        // Create the CardBrandType with an existing ID
        cardBrandType.setId(1L);
        CardBrandTypeDTO cardBrandTypeDTO = cardBrandTypeMapper.toDto(cardBrandType);

        int databaseSizeBeforeCreate = cardBrandTypeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCardBrandTypeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cardBrandTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CardBrandType in the database
        List<CardBrandType> cardBrandTypeList = cardBrandTypeRepository.findAll();
        assertThat(cardBrandTypeList).hasSize(databaseSizeBeforeCreate);

        // Validate the CardBrandType in Elasticsearch
        verify(mockCardBrandTypeSearchRepository, times(0)).save(cardBrandType);
    }

    @Test
    @Transactional
    void checkCardBrandTypeCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = cardBrandTypeRepository.findAll().size();
        // set the field null
        cardBrandType.setCardBrandTypeCode(null);

        // Create the CardBrandType, which fails.
        CardBrandTypeDTO cardBrandTypeDTO = cardBrandTypeMapper.toDto(cardBrandType);

        restCardBrandTypeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cardBrandTypeDTO))
            )
            .andExpect(status().isBadRequest());

        List<CardBrandType> cardBrandTypeList = cardBrandTypeRepository.findAll();
        assertThat(cardBrandTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCardBrandTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = cardBrandTypeRepository.findAll().size();
        // set the field null
        cardBrandType.setCardBrandType(null);

        // Create the CardBrandType, which fails.
        CardBrandTypeDTO cardBrandTypeDTO = cardBrandTypeMapper.toDto(cardBrandType);

        restCardBrandTypeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cardBrandTypeDTO))
            )
            .andExpect(status().isBadRequest());

        List<CardBrandType> cardBrandTypeList = cardBrandTypeRepository.findAll();
        assertThat(cardBrandTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCardBrandTypes() throws Exception {
        // Initialize the database
        cardBrandTypeRepository.saveAndFlush(cardBrandType);

        // Get all the cardBrandTypeList
        restCardBrandTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cardBrandType.getId().intValue())))
            .andExpect(jsonPath("$.[*].cardBrandTypeCode").value(hasItem(DEFAULT_CARD_BRAND_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].cardBrandType").value(hasItem(DEFAULT_CARD_BRAND_TYPE)))
            .andExpect(jsonPath("$.[*].cardBrandTypeDetails").value(hasItem(DEFAULT_CARD_BRAND_TYPE_DETAILS.toString())));
    }

    @Test
    @Transactional
    void getCardBrandType() throws Exception {
        // Initialize the database
        cardBrandTypeRepository.saveAndFlush(cardBrandType);

        // Get the cardBrandType
        restCardBrandTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, cardBrandType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cardBrandType.getId().intValue()))
            .andExpect(jsonPath("$.cardBrandTypeCode").value(DEFAULT_CARD_BRAND_TYPE_CODE))
            .andExpect(jsonPath("$.cardBrandType").value(DEFAULT_CARD_BRAND_TYPE))
            .andExpect(jsonPath("$.cardBrandTypeDetails").value(DEFAULT_CARD_BRAND_TYPE_DETAILS.toString()));
    }

    @Test
    @Transactional
    void getCardBrandTypesByIdFiltering() throws Exception {
        // Initialize the database
        cardBrandTypeRepository.saveAndFlush(cardBrandType);

        Long id = cardBrandType.getId();

        defaultCardBrandTypeShouldBeFound("id.equals=" + id);
        defaultCardBrandTypeShouldNotBeFound("id.notEquals=" + id);

        defaultCardBrandTypeShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCardBrandTypeShouldNotBeFound("id.greaterThan=" + id);

        defaultCardBrandTypeShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCardBrandTypeShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCardBrandTypesByCardBrandTypeCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        cardBrandTypeRepository.saveAndFlush(cardBrandType);

        // Get all the cardBrandTypeList where cardBrandTypeCode equals to DEFAULT_CARD_BRAND_TYPE_CODE
        defaultCardBrandTypeShouldBeFound("cardBrandTypeCode.equals=" + DEFAULT_CARD_BRAND_TYPE_CODE);

        // Get all the cardBrandTypeList where cardBrandTypeCode equals to UPDATED_CARD_BRAND_TYPE_CODE
        defaultCardBrandTypeShouldNotBeFound("cardBrandTypeCode.equals=" + UPDATED_CARD_BRAND_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllCardBrandTypesByCardBrandTypeCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        cardBrandTypeRepository.saveAndFlush(cardBrandType);

        // Get all the cardBrandTypeList where cardBrandTypeCode not equals to DEFAULT_CARD_BRAND_TYPE_CODE
        defaultCardBrandTypeShouldNotBeFound("cardBrandTypeCode.notEquals=" + DEFAULT_CARD_BRAND_TYPE_CODE);

        // Get all the cardBrandTypeList where cardBrandTypeCode not equals to UPDATED_CARD_BRAND_TYPE_CODE
        defaultCardBrandTypeShouldBeFound("cardBrandTypeCode.notEquals=" + UPDATED_CARD_BRAND_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllCardBrandTypesByCardBrandTypeCodeIsInShouldWork() throws Exception {
        // Initialize the database
        cardBrandTypeRepository.saveAndFlush(cardBrandType);

        // Get all the cardBrandTypeList where cardBrandTypeCode in DEFAULT_CARD_BRAND_TYPE_CODE or UPDATED_CARD_BRAND_TYPE_CODE
        defaultCardBrandTypeShouldBeFound("cardBrandTypeCode.in=" + DEFAULT_CARD_BRAND_TYPE_CODE + "," + UPDATED_CARD_BRAND_TYPE_CODE);

        // Get all the cardBrandTypeList where cardBrandTypeCode equals to UPDATED_CARD_BRAND_TYPE_CODE
        defaultCardBrandTypeShouldNotBeFound("cardBrandTypeCode.in=" + UPDATED_CARD_BRAND_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllCardBrandTypesByCardBrandTypeCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        cardBrandTypeRepository.saveAndFlush(cardBrandType);

        // Get all the cardBrandTypeList where cardBrandTypeCode is not null
        defaultCardBrandTypeShouldBeFound("cardBrandTypeCode.specified=true");

        // Get all the cardBrandTypeList where cardBrandTypeCode is null
        defaultCardBrandTypeShouldNotBeFound("cardBrandTypeCode.specified=false");
    }

    @Test
    @Transactional
    void getAllCardBrandTypesByCardBrandTypeCodeContainsSomething() throws Exception {
        // Initialize the database
        cardBrandTypeRepository.saveAndFlush(cardBrandType);

        // Get all the cardBrandTypeList where cardBrandTypeCode contains DEFAULT_CARD_BRAND_TYPE_CODE
        defaultCardBrandTypeShouldBeFound("cardBrandTypeCode.contains=" + DEFAULT_CARD_BRAND_TYPE_CODE);

        // Get all the cardBrandTypeList where cardBrandTypeCode contains UPDATED_CARD_BRAND_TYPE_CODE
        defaultCardBrandTypeShouldNotBeFound("cardBrandTypeCode.contains=" + UPDATED_CARD_BRAND_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllCardBrandTypesByCardBrandTypeCodeNotContainsSomething() throws Exception {
        // Initialize the database
        cardBrandTypeRepository.saveAndFlush(cardBrandType);

        // Get all the cardBrandTypeList where cardBrandTypeCode does not contain DEFAULT_CARD_BRAND_TYPE_CODE
        defaultCardBrandTypeShouldNotBeFound("cardBrandTypeCode.doesNotContain=" + DEFAULT_CARD_BRAND_TYPE_CODE);

        // Get all the cardBrandTypeList where cardBrandTypeCode does not contain UPDATED_CARD_BRAND_TYPE_CODE
        defaultCardBrandTypeShouldBeFound("cardBrandTypeCode.doesNotContain=" + UPDATED_CARD_BRAND_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllCardBrandTypesByCardBrandTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        cardBrandTypeRepository.saveAndFlush(cardBrandType);

        // Get all the cardBrandTypeList where cardBrandType equals to DEFAULT_CARD_BRAND_TYPE
        defaultCardBrandTypeShouldBeFound("cardBrandType.equals=" + DEFAULT_CARD_BRAND_TYPE);

        // Get all the cardBrandTypeList where cardBrandType equals to UPDATED_CARD_BRAND_TYPE
        defaultCardBrandTypeShouldNotBeFound("cardBrandType.equals=" + UPDATED_CARD_BRAND_TYPE);
    }

    @Test
    @Transactional
    void getAllCardBrandTypesByCardBrandTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        cardBrandTypeRepository.saveAndFlush(cardBrandType);

        // Get all the cardBrandTypeList where cardBrandType not equals to DEFAULT_CARD_BRAND_TYPE
        defaultCardBrandTypeShouldNotBeFound("cardBrandType.notEquals=" + DEFAULT_CARD_BRAND_TYPE);

        // Get all the cardBrandTypeList where cardBrandType not equals to UPDATED_CARD_BRAND_TYPE
        defaultCardBrandTypeShouldBeFound("cardBrandType.notEquals=" + UPDATED_CARD_BRAND_TYPE);
    }

    @Test
    @Transactional
    void getAllCardBrandTypesByCardBrandTypeIsInShouldWork() throws Exception {
        // Initialize the database
        cardBrandTypeRepository.saveAndFlush(cardBrandType);

        // Get all the cardBrandTypeList where cardBrandType in DEFAULT_CARD_BRAND_TYPE or UPDATED_CARD_BRAND_TYPE
        defaultCardBrandTypeShouldBeFound("cardBrandType.in=" + DEFAULT_CARD_BRAND_TYPE + "," + UPDATED_CARD_BRAND_TYPE);

        // Get all the cardBrandTypeList where cardBrandType equals to UPDATED_CARD_BRAND_TYPE
        defaultCardBrandTypeShouldNotBeFound("cardBrandType.in=" + UPDATED_CARD_BRAND_TYPE);
    }

    @Test
    @Transactional
    void getAllCardBrandTypesByCardBrandTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        cardBrandTypeRepository.saveAndFlush(cardBrandType);

        // Get all the cardBrandTypeList where cardBrandType is not null
        defaultCardBrandTypeShouldBeFound("cardBrandType.specified=true");

        // Get all the cardBrandTypeList where cardBrandType is null
        defaultCardBrandTypeShouldNotBeFound("cardBrandType.specified=false");
    }

    @Test
    @Transactional
    void getAllCardBrandTypesByCardBrandTypeContainsSomething() throws Exception {
        // Initialize the database
        cardBrandTypeRepository.saveAndFlush(cardBrandType);

        // Get all the cardBrandTypeList where cardBrandType contains DEFAULT_CARD_BRAND_TYPE
        defaultCardBrandTypeShouldBeFound("cardBrandType.contains=" + DEFAULT_CARD_BRAND_TYPE);

        // Get all the cardBrandTypeList where cardBrandType contains UPDATED_CARD_BRAND_TYPE
        defaultCardBrandTypeShouldNotBeFound("cardBrandType.contains=" + UPDATED_CARD_BRAND_TYPE);
    }

    @Test
    @Transactional
    void getAllCardBrandTypesByCardBrandTypeNotContainsSomething() throws Exception {
        // Initialize the database
        cardBrandTypeRepository.saveAndFlush(cardBrandType);

        // Get all the cardBrandTypeList where cardBrandType does not contain DEFAULT_CARD_BRAND_TYPE
        defaultCardBrandTypeShouldNotBeFound("cardBrandType.doesNotContain=" + DEFAULT_CARD_BRAND_TYPE);

        // Get all the cardBrandTypeList where cardBrandType does not contain UPDATED_CARD_BRAND_TYPE
        defaultCardBrandTypeShouldBeFound("cardBrandType.doesNotContain=" + UPDATED_CARD_BRAND_TYPE);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCardBrandTypeShouldBeFound(String filter) throws Exception {
        restCardBrandTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cardBrandType.getId().intValue())))
            .andExpect(jsonPath("$.[*].cardBrandTypeCode").value(hasItem(DEFAULT_CARD_BRAND_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].cardBrandType").value(hasItem(DEFAULT_CARD_BRAND_TYPE)))
            .andExpect(jsonPath("$.[*].cardBrandTypeDetails").value(hasItem(DEFAULT_CARD_BRAND_TYPE_DETAILS.toString())));

        // Check, that the count call also returns 1
        restCardBrandTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCardBrandTypeShouldNotBeFound(String filter) throws Exception {
        restCardBrandTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCardBrandTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCardBrandType() throws Exception {
        // Get the cardBrandType
        restCardBrandTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCardBrandType() throws Exception {
        // Initialize the database
        cardBrandTypeRepository.saveAndFlush(cardBrandType);

        int databaseSizeBeforeUpdate = cardBrandTypeRepository.findAll().size();

        // Update the cardBrandType
        CardBrandType updatedCardBrandType = cardBrandTypeRepository.findById(cardBrandType.getId()).get();
        // Disconnect from session so that the updates on updatedCardBrandType are not directly saved in db
        em.detach(updatedCardBrandType);
        updatedCardBrandType
            .cardBrandTypeCode(UPDATED_CARD_BRAND_TYPE_CODE)
            .cardBrandType(UPDATED_CARD_BRAND_TYPE)
            .cardBrandTypeDetails(UPDATED_CARD_BRAND_TYPE_DETAILS);
        CardBrandTypeDTO cardBrandTypeDTO = cardBrandTypeMapper.toDto(updatedCardBrandType);

        restCardBrandTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cardBrandTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cardBrandTypeDTO))
            )
            .andExpect(status().isOk());

        // Validate the CardBrandType in the database
        List<CardBrandType> cardBrandTypeList = cardBrandTypeRepository.findAll();
        assertThat(cardBrandTypeList).hasSize(databaseSizeBeforeUpdate);
        CardBrandType testCardBrandType = cardBrandTypeList.get(cardBrandTypeList.size() - 1);
        assertThat(testCardBrandType.getCardBrandTypeCode()).isEqualTo(UPDATED_CARD_BRAND_TYPE_CODE);
        assertThat(testCardBrandType.getCardBrandType()).isEqualTo(UPDATED_CARD_BRAND_TYPE);
        assertThat(testCardBrandType.getCardBrandTypeDetails()).isEqualTo(UPDATED_CARD_BRAND_TYPE_DETAILS);

        // Validate the CardBrandType in Elasticsearch
        verify(mockCardBrandTypeSearchRepository).save(testCardBrandType);
    }

    @Test
    @Transactional
    void putNonExistingCardBrandType() throws Exception {
        int databaseSizeBeforeUpdate = cardBrandTypeRepository.findAll().size();
        cardBrandType.setId(count.incrementAndGet());

        // Create the CardBrandType
        CardBrandTypeDTO cardBrandTypeDTO = cardBrandTypeMapper.toDto(cardBrandType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCardBrandTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cardBrandTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cardBrandTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CardBrandType in the database
        List<CardBrandType> cardBrandTypeList = cardBrandTypeRepository.findAll();
        assertThat(cardBrandTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CardBrandType in Elasticsearch
        verify(mockCardBrandTypeSearchRepository, times(0)).save(cardBrandType);
    }

    @Test
    @Transactional
    void putWithIdMismatchCardBrandType() throws Exception {
        int databaseSizeBeforeUpdate = cardBrandTypeRepository.findAll().size();
        cardBrandType.setId(count.incrementAndGet());

        // Create the CardBrandType
        CardBrandTypeDTO cardBrandTypeDTO = cardBrandTypeMapper.toDto(cardBrandType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCardBrandTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cardBrandTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CardBrandType in the database
        List<CardBrandType> cardBrandTypeList = cardBrandTypeRepository.findAll();
        assertThat(cardBrandTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CardBrandType in Elasticsearch
        verify(mockCardBrandTypeSearchRepository, times(0)).save(cardBrandType);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCardBrandType() throws Exception {
        int databaseSizeBeforeUpdate = cardBrandTypeRepository.findAll().size();
        cardBrandType.setId(count.incrementAndGet());

        // Create the CardBrandType
        CardBrandTypeDTO cardBrandTypeDTO = cardBrandTypeMapper.toDto(cardBrandType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCardBrandTypeMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cardBrandTypeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CardBrandType in the database
        List<CardBrandType> cardBrandTypeList = cardBrandTypeRepository.findAll();
        assertThat(cardBrandTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CardBrandType in Elasticsearch
        verify(mockCardBrandTypeSearchRepository, times(0)).save(cardBrandType);
    }

    @Test
    @Transactional
    void partialUpdateCardBrandTypeWithPatch() throws Exception {
        // Initialize the database
        cardBrandTypeRepository.saveAndFlush(cardBrandType);

        int databaseSizeBeforeUpdate = cardBrandTypeRepository.findAll().size();

        // Update the cardBrandType using partial update
        CardBrandType partialUpdatedCardBrandType = new CardBrandType();
        partialUpdatedCardBrandType.setId(cardBrandType.getId());

        partialUpdatedCardBrandType.cardBrandType(UPDATED_CARD_BRAND_TYPE).cardBrandTypeDetails(UPDATED_CARD_BRAND_TYPE_DETAILS);

        restCardBrandTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCardBrandType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCardBrandType))
            )
            .andExpect(status().isOk());

        // Validate the CardBrandType in the database
        List<CardBrandType> cardBrandTypeList = cardBrandTypeRepository.findAll();
        assertThat(cardBrandTypeList).hasSize(databaseSizeBeforeUpdate);
        CardBrandType testCardBrandType = cardBrandTypeList.get(cardBrandTypeList.size() - 1);
        assertThat(testCardBrandType.getCardBrandTypeCode()).isEqualTo(DEFAULT_CARD_BRAND_TYPE_CODE);
        assertThat(testCardBrandType.getCardBrandType()).isEqualTo(UPDATED_CARD_BRAND_TYPE);
        assertThat(testCardBrandType.getCardBrandTypeDetails()).isEqualTo(UPDATED_CARD_BRAND_TYPE_DETAILS);
    }

    @Test
    @Transactional
    void fullUpdateCardBrandTypeWithPatch() throws Exception {
        // Initialize the database
        cardBrandTypeRepository.saveAndFlush(cardBrandType);

        int databaseSizeBeforeUpdate = cardBrandTypeRepository.findAll().size();

        // Update the cardBrandType using partial update
        CardBrandType partialUpdatedCardBrandType = new CardBrandType();
        partialUpdatedCardBrandType.setId(cardBrandType.getId());

        partialUpdatedCardBrandType
            .cardBrandTypeCode(UPDATED_CARD_BRAND_TYPE_CODE)
            .cardBrandType(UPDATED_CARD_BRAND_TYPE)
            .cardBrandTypeDetails(UPDATED_CARD_BRAND_TYPE_DETAILS);

        restCardBrandTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCardBrandType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCardBrandType))
            )
            .andExpect(status().isOk());

        // Validate the CardBrandType in the database
        List<CardBrandType> cardBrandTypeList = cardBrandTypeRepository.findAll();
        assertThat(cardBrandTypeList).hasSize(databaseSizeBeforeUpdate);
        CardBrandType testCardBrandType = cardBrandTypeList.get(cardBrandTypeList.size() - 1);
        assertThat(testCardBrandType.getCardBrandTypeCode()).isEqualTo(UPDATED_CARD_BRAND_TYPE_CODE);
        assertThat(testCardBrandType.getCardBrandType()).isEqualTo(UPDATED_CARD_BRAND_TYPE);
        assertThat(testCardBrandType.getCardBrandTypeDetails()).isEqualTo(UPDATED_CARD_BRAND_TYPE_DETAILS);
    }

    @Test
    @Transactional
    void patchNonExistingCardBrandType() throws Exception {
        int databaseSizeBeforeUpdate = cardBrandTypeRepository.findAll().size();
        cardBrandType.setId(count.incrementAndGet());

        // Create the CardBrandType
        CardBrandTypeDTO cardBrandTypeDTO = cardBrandTypeMapper.toDto(cardBrandType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCardBrandTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, cardBrandTypeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cardBrandTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CardBrandType in the database
        List<CardBrandType> cardBrandTypeList = cardBrandTypeRepository.findAll();
        assertThat(cardBrandTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CardBrandType in Elasticsearch
        verify(mockCardBrandTypeSearchRepository, times(0)).save(cardBrandType);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCardBrandType() throws Exception {
        int databaseSizeBeforeUpdate = cardBrandTypeRepository.findAll().size();
        cardBrandType.setId(count.incrementAndGet());

        // Create the CardBrandType
        CardBrandTypeDTO cardBrandTypeDTO = cardBrandTypeMapper.toDto(cardBrandType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCardBrandTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cardBrandTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CardBrandType in the database
        List<CardBrandType> cardBrandTypeList = cardBrandTypeRepository.findAll();
        assertThat(cardBrandTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CardBrandType in Elasticsearch
        verify(mockCardBrandTypeSearchRepository, times(0)).save(cardBrandType);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCardBrandType() throws Exception {
        int databaseSizeBeforeUpdate = cardBrandTypeRepository.findAll().size();
        cardBrandType.setId(count.incrementAndGet());

        // Create the CardBrandType
        CardBrandTypeDTO cardBrandTypeDTO = cardBrandTypeMapper.toDto(cardBrandType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCardBrandTypeMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cardBrandTypeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CardBrandType in the database
        List<CardBrandType> cardBrandTypeList = cardBrandTypeRepository.findAll();
        assertThat(cardBrandTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CardBrandType in Elasticsearch
        verify(mockCardBrandTypeSearchRepository, times(0)).save(cardBrandType);
    }

    @Test
    @Transactional
    void deleteCardBrandType() throws Exception {
        // Initialize the database
        cardBrandTypeRepository.saveAndFlush(cardBrandType);

        int databaseSizeBeforeDelete = cardBrandTypeRepository.findAll().size();

        // Delete the cardBrandType
        restCardBrandTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, cardBrandType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CardBrandType> cardBrandTypeList = cardBrandTypeRepository.findAll();
        assertThat(cardBrandTypeList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the CardBrandType in Elasticsearch
        verify(mockCardBrandTypeSearchRepository, times(1)).deleteById(cardBrandType.getId());
    }

    @Test
    @Transactional
    void searchCardBrandType() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        cardBrandTypeRepository.saveAndFlush(cardBrandType);
        when(mockCardBrandTypeSearchRepository.search("id:" + cardBrandType.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(cardBrandType), PageRequest.of(0, 1), 1));

        // Search the cardBrandType
        restCardBrandTypeMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + cardBrandType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cardBrandType.getId().intValue())))
            .andExpect(jsonPath("$.[*].cardBrandTypeCode").value(hasItem(DEFAULT_CARD_BRAND_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].cardBrandType").value(hasItem(DEFAULT_CARD_BRAND_TYPE)))
            .andExpect(jsonPath("$.[*].cardBrandTypeDetails").value(hasItem(DEFAULT_CARD_BRAND_TYPE_DETAILS.toString())));
    }
}
