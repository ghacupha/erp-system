package io.github.erp.erp.resources.gdi;

/*-
 * Erp System - Mark IX No 4 (Iddo Series) Server ver 1.6.6
 * Copyright © 2021 - 2023 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
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
import io.github.erp.domain.CardClassType;
import io.github.erp.repository.CardClassTypeRepository;
import io.github.erp.repository.search.CardClassTypeSearchRepository;
import io.github.erp.service.dto.CardClassTypeDTO;
import io.github.erp.service.mapper.CardClassTypeMapper;
import io.github.erp.web.rest.CardClassTypeResource;
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
 * Integration tests for the {@link CardClassTypeResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser(roles = {"GRANULAR_REPORTS_USER"})
class CardClassTypeResourceIT {

    private static final String DEFAULT_CARD_CLASS_TYPE_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CARD_CLASS_TYPE_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_CARD_CLASS_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_CARD_CLASS_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_CARD_CLASS_DETAILS = "AAAAAAAAAA";
    private static final String UPDATED_CARD_CLASS_DETAILS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/granular-data/card-class-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/granular-data/_search/card-class-types";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CardClassTypeRepository cardClassTypeRepository;

    @Autowired
    private CardClassTypeMapper cardClassTypeMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.CardClassTypeSearchRepositoryMockConfiguration
     */
    @Autowired
    private CardClassTypeSearchRepository mockCardClassTypeSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCardClassTypeMockMvc;

    private CardClassType cardClassType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CardClassType createEntity(EntityManager em) {
        CardClassType cardClassType = new CardClassType()
            .cardClassTypeCode(DEFAULT_CARD_CLASS_TYPE_CODE)
            .cardClassType(DEFAULT_CARD_CLASS_TYPE)
            .cardClassDetails(DEFAULT_CARD_CLASS_DETAILS);
        return cardClassType;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CardClassType createUpdatedEntity(EntityManager em) {
        CardClassType cardClassType = new CardClassType()
            .cardClassTypeCode(UPDATED_CARD_CLASS_TYPE_CODE)
            .cardClassType(UPDATED_CARD_CLASS_TYPE)
            .cardClassDetails(UPDATED_CARD_CLASS_DETAILS);
        return cardClassType;
    }

    @BeforeEach
    public void initTest() {
        cardClassType = createEntity(em);
    }

    @Test
    @Transactional
    void createCardClassType() throws Exception {
        int databaseSizeBeforeCreate = cardClassTypeRepository.findAll().size();
        // Create the CardClassType
        CardClassTypeDTO cardClassTypeDTO = cardClassTypeMapper.toDto(cardClassType);
        restCardClassTypeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cardClassTypeDTO))
            )
            .andExpect(status().isCreated());

        // Validate the CardClassType in the database
        List<CardClassType> cardClassTypeList = cardClassTypeRepository.findAll();
        assertThat(cardClassTypeList).hasSize(databaseSizeBeforeCreate + 1);
        CardClassType testCardClassType = cardClassTypeList.get(cardClassTypeList.size() - 1);
        assertThat(testCardClassType.getCardClassTypeCode()).isEqualTo(DEFAULT_CARD_CLASS_TYPE_CODE);
        assertThat(testCardClassType.getCardClassType()).isEqualTo(DEFAULT_CARD_CLASS_TYPE);
        assertThat(testCardClassType.getCardClassDetails()).isEqualTo(DEFAULT_CARD_CLASS_DETAILS);

        // Validate the CardClassType in Elasticsearch
        verify(mockCardClassTypeSearchRepository, times(1)).save(testCardClassType);
    }

    @Test
    @Transactional
    void createCardClassTypeWithExistingId() throws Exception {
        // Create the CardClassType with an existing ID
        cardClassType.setId(1L);
        CardClassTypeDTO cardClassTypeDTO = cardClassTypeMapper.toDto(cardClassType);

        int databaseSizeBeforeCreate = cardClassTypeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCardClassTypeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cardClassTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CardClassType in the database
        List<CardClassType> cardClassTypeList = cardClassTypeRepository.findAll();
        assertThat(cardClassTypeList).hasSize(databaseSizeBeforeCreate);

        // Validate the CardClassType in Elasticsearch
        verify(mockCardClassTypeSearchRepository, times(0)).save(cardClassType);
    }

    @Test
    @Transactional
    void checkCardClassTypeCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = cardClassTypeRepository.findAll().size();
        // set the field null
        cardClassType.setCardClassTypeCode(null);

        // Create the CardClassType, which fails.
        CardClassTypeDTO cardClassTypeDTO = cardClassTypeMapper.toDto(cardClassType);

        restCardClassTypeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cardClassTypeDTO))
            )
            .andExpect(status().isBadRequest());

        List<CardClassType> cardClassTypeList = cardClassTypeRepository.findAll();
        assertThat(cardClassTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCardClassTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = cardClassTypeRepository.findAll().size();
        // set the field null
        cardClassType.setCardClassType(null);

        // Create the CardClassType, which fails.
        CardClassTypeDTO cardClassTypeDTO = cardClassTypeMapper.toDto(cardClassType);

        restCardClassTypeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cardClassTypeDTO))
            )
            .andExpect(status().isBadRequest());

        List<CardClassType> cardClassTypeList = cardClassTypeRepository.findAll();
        assertThat(cardClassTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCardClassTypes() throws Exception {
        // Initialize the database
        cardClassTypeRepository.saveAndFlush(cardClassType);

        // Get all the cardClassTypeList
        restCardClassTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cardClassType.getId().intValue())))
            .andExpect(jsonPath("$.[*].cardClassTypeCode").value(hasItem(DEFAULT_CARD_CLASS_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].cardClassType").value(hasItem(DEFAULT_CARD_CLASS_TYPE)))
            .andExpect(jsonPath("$.[*].cardClassDetails").value(hasItem(DEFAULT_CARD_CLASS_DETAILS.toString())));
    }

    @Test
    @Transactional
    void getCardClassType() throws Exception {
        // Initialize the database
        cardClassTypeRepository.saveAndFlush(cardClassType);

        // Get the cardClassType
        restCardClassTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, cardClassType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cardClassType.getId().intValue()))
            .andExpect(jsonPath("$.cardClassTypeCode").value(DEFAULT_CARD_CLASS_TYPE_CODE))
            .andExpect(jsonPath("$.cardClassType").value(DEFAULT_CARD_CLASS_TYPE))
            .andExpect(jsonPath("$.cardClassDetails").value(DEFAULT_CARD_CLASS_DETAILS.toString()));
    }

    @Test
    @Transactional
    void getCardClassTypesByIdFiltering() throws Exception {
        // Initialize the database
        cardClassTypeRepository.saveAndFlush(cardClassType);

        Long id = cardClassType.getId();

        defaultCardClassTypeShouldBeFound("id.equals=" + id);
        defaultCardClassTypeShouldNotBeFound("id.notEquals=" + id);

        defaultCardClassTypeShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCardClassTypeShouldNotBeFound("id.greaterThan=" + id);

        defaultCardClassTypeShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCardClassTypeShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCardClassTypesByCardClassTypeCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        cardClassTypeRepository.saveAndFlush(cardClassType);

        // Get all the cardClassTypeList where cardClassTypeCode equals to DEFAULT_CARD_CLASS_TYPE_CODE
        defaultCardClassTypeShouldBeFound("cardClassTypeCode.equals=" + DEFAULT_CARD_CLASS_TYPE_CODE);

        // Get all the cardClassTypeList where cardClassTypeCode equals to UPDATED_CARD_CLASS_TYPE_CODE
        defaultCardClassTypeShouldNotBeFound("cardClassTypeCode.equals=" + UPDATED_CARD_CLASS_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllCardClassTypesByCardClassTypeCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        cardClassTypeRepository.saveAndFlush(cardClassType);

        // Get all the cardClassTypeList where cardClassTypeCode not equals to DEFAULT_CARD_CLASS_TYPE_CODE
        defaultCardClassTypeShouldNotBeFound("cardClassTypeCode.notEquals=" + DEFAULT_CARD_CLASS_TYPE_CODE);

        // Get all the cardClassTypeList where cardClassTypeCode not equals to UPDATED_CARD_CLASS_TYPE_CODE
        defaultCardClassTypeShouldBeFound("cardClassTypeCode.notEquals=" + UPDATED_CARD_CLASS_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllCardClassTypesByCardClassTypeCodeIsInShouldWork() throws Exception {
        // Initialize the database
        cardClassTypeRepository.saveAndFlush(cardClassType);

        // Get all the cardClassTypeList where cardClassTypeCode in DEFAULT_CARD_CLASS_TYPE_CODE or UPDATED_CARD_CLASS_TYPE_CODE
        defaultCardClassTypeShouldBeFound("cardClassTypeCode.in=" + DEFAULT_CARD_CLASS_TYPE_CODE + "," + UPDATED_CARD_CLASS_TYPE_CODE);

        // Get all the cardClassTypeList where cardClassTypeCode equals to UPDATED_CARD_CLASS_TYPE_CODE
        defaultCardClassTypeShouldNotBeFound("cardClassTypeCode.in=" + UPDATED_CARD_CLASS_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllCardClassTypesByCardClassTypeCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        cardClassTypeRepository.saveAndFlush(cardClassType);

        // Get all the cardClassTypeList where cardClassTypeCode is not null
        defaultCardClassTypeShouldBeFound("cardClassTypeCode.specified=true");

        // Get all the cardClassTypeList where cardClassTypeCode is null
        defaultCardClassTypeShouldNotBeFound("cardClassTypeCode.specified=false");
    }

    @Test
    @Transactional
    void getAllCardClassTypesByCardClassTypeCodeContainsSomething() throws Exception {
        // Initialize the database
        cardClassTypeRepository.saveAndFlush(cardClassType);

        // Get all the cardClassTypeList where cardClassTypeCode contains DEFAULT_CARD_CLASS_TYPE_CODE
        defaultCardClassTypeShouldBeFound("cardClassTypeCode.contains=" + DEFAULT_CARD_CLASS_TYPE_CODE);

        // Get all the cardClassTypeList where cardClassTypeCode contains UPDATED_CARD_CLASS_TYPE_CODE
        defaultCardClassTypeShouldNotBeFound("cardClassTypeCode.contains=" + UPDATED_CARD_CLASS_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllCardClassTypesByCardClassTypeCodeNotContainsSomething() throws Exception {
        // Initialize the database
        cardClassTypeRepository.saveAndFlush(cardClassType);

        // Get all the cardClassTypeList where cardClassTypeCode does not contain DEFAULT_CARD_CLASS_TYPE_CODE
        defaultCardClassTypeShouldNotBeFound("cardClassTypeCode.doesNotContain=" + DEFAULT_CARD_CLASS_TYPE_CODE);

        // Get all the cardClassTypeList where cardClassTypeCode does not contain UPDATED_CARD_CLASS_TYPE_CODE
        defaultCardClassTypeShouldBeFound("cardClassTypeCode.doesNotContain=" + UPDATED_CARD_CLASS_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllCardClassTypesByCardClassTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        cardClassTypeRepository.saveAndFlush(cardClassType);

        // Get all the cardClassTypeList where cardClassType equals to DEFAULT_CARD_CLASS_TYPE
        defaultCardClassTypeShouldBeFound("cardClassType.equals=" + DEFAULT_CARD_CLASS_TYPE);

        // Get all the cardClassTypeList where cardClassType equals to UPDATED_CARD_CLASS_TYPE
        defaultCardClassTypeShouldNotBeFound("cardClassType.equals=" + UPDATED_CARD_CLASS_TYPE);
    }

    @Test
    @Transactional
    void getAllCardClassTypesByCardClassTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        cardClassTypeRepository.saveAndFlush(cardClassType);

        // Get all the cardClassTypeList where cardClassType not equals to DEFAULT_CARD_CLASS_TYPE
        defaultCardClassTypeShouldNotBeFound("cardClassType.notEquals=" + DEFAULT_CARD_CLASS_TYPE);

        // Get all the cardClassTypeList where cardClassType not equals to UPDATED_CARD_CLASS_TYPE
        defaultCardClassTypeShouldBeFound("cardClassType.notEquals=" + UPDATED_CARD_CLASS_TYPE);
    }

    @Test
    @Transactional
    void getAllCardClassTypesByCardClassTypeIsInShouldWork() throws Exception {
        // Initialize the database
        cardClassTypeRepository.saveAndFlush(cardClassType);

        // Get all the cardClassTypeList where cardClassType in DEFAULT_CARD_CLASS_TYPE or UPDATED_CARD_CLASS_TYPE
        defaultCardClassTypeShouldBeFound("cardClassType.in=" + DEFAULT_CARD_CLASS_TYPE + "," + UPDATED_CARD_CLASS_TYPE);

        // Get all the cardClassTypeList where cardClassType equals to UPDATED_CARD_CLASS_TYPE
        defaultCardClassTypeShouldNotBeFound("cardClassType.in=" + UPDATED_CARD_CLASS_TYPE);
    }

    @Test
    @Transactional
    void getAllCardClassTypesByCardClassTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        cardClassTypeRepository.saveAndFlush(cardClassType);

        // Get all the cardClassTypeList where cardClassType is not null
        defaultCardClassTypeShouldBeFound("cardClassType.specified=true");

        // Get all the cardClassTypeList where cardClassType is null
        defaultCardClassTypeShouldNotBeFound("cardClassType.specified=false");
    }

    @Test
    @Transactional
    void getAllCardClassTypesByCardClassTypeContainsSomething() throws Exception {
        // Initialize the database
        cardClassTypeRepository.saveAndFlush(cardClassType);

        // Get all the cardClassTypeList where cardClassType contains DEFAULT_CARD_CLASS_TYPE
        defaultCardClassTypeShouldBeFound("cardClassType.contains=" + DEFAULT_CARD_CLASS_TYPE);

        // Get all the cardClassTypeList where cardClassType contains UPDATED_CARD_CLASS_TYPE
        defaultCardClassTypeShouldNotBeFound("cardClassType.contains=" + UPDATED_CARD_CLASS_TYPE);
    }

    @Test
    @Transactional
    void getAllCardClassTypesByCardClassTypeNotContainsSomething() throws Exception {
        // Initialize the database
        cardClassTypeRepository.saveAndFlush(cardClassType);

        // Get all the cardClassTypeList where cardClassType does not contain DEFAULT_CARD_CLASS_TYPE
        defaultCardClassTypeShouldNotBeFound("cardClassType.doesNotContain=" + DEFAULT_CARD_CLASS_TYPE);

        // Get all the cardClassTypeList where cardClassType does not contain UPDATED_CARD_CLASS_TYPE
        defaultCardClassTypeShouldBeFound("cardClassType.doesNotContain=" + UPDATED_CARD_CLASS_TYPE);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCardClassTypeShouldBeFound(String filter) throws Exception {
        restCardClassTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cardClassType.getId().intValue())))
            .andExpect(jsonPath("$.[*].cardClassTypeCode").value(hasItem(DEFAULT_CARD_CLASS_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].cardClassType").value(hasItem(DEFAULT_CARD_CLASS_TYPE)))
            .andExpect(jsonPath("$.[*].cardClassDetails").value(hasItem(DEFAULT_CARD_CLASS_DETAILS.toString())));

        // Check, that the count call also returns 1
        restCardClassTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCardClassTypeShouldNotBeFound(String filter) throws Exception {
        restCardClassTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCardClassTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCardClassType() throws Exception {
        // Get the cardClassType
        restCardClassTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCardClassType() throws Exception {
        // Initialize the database
        cardClassTypeRepository.saveAndFlush(cardClassType);

        int databaseSizeBeforeUpdate = cardClassTypeRepository.findAll().size();

        // Update the cardClassType
        CardClassType updatedCardClassType = cardClassTypeRepository.findById(cardClassType.getId()).get();
        // Disconnect from session so that the updates on updatedCardClassType are not directly saved in db
        em.detach(updatedCardClassType);
        updatedCardClassType
            .cardClassTypeCode(UPDATED_CARD_CLASS_TYPE_CODE)
            .cardClassType(UPDATED_CARD_CLASS_TYPE)
            .cardClassDetails(UPDATED_CARD_CLASS_DETAILS);
        CardClassTypeDTO cardClassTypeDTO = cardClassTypeMapper.toDto(updatedCardClassType);

        restCardClassTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cardClassTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cardClassTypeDTO))
            )
            .andExpect(status().isOk());

        // Validate the CardClassType in the database
        List<CardClassType> cardClassTypeList = cardClassTypeRepository.findAll();
        assertThat(cardClassTypeList).hasSize(databaseSizeBeforeUpdate);
        CardClassType testCardClassType = cardClassTypeList.get(cardClassTypeList.size() - 1);
        assertThat(testCardClassType.getCardClassTypeCode()).isEqualTo(UPDATED_CARD_CLASS_TYPE_CODE);
        assertThat(testCardClassType.getCardClassType()).isEqualTo(UPDATED_CARD_CLASS_TYPE);
        assertThat(testCardClassType.getCardClassDetails()).isEqualTo(UPDATED_CARD_CLASS_DETAILS);

        // Validate the CardClassType in Elasticsearch
        verify(mockCardClassTypeSearchRepository).save(testCardClassType);
    }

    @Test
    @Transactional
    void putNonExistingCardClassType() throws Exception {
        int databaseSizeBeforeUpdate = cardClassTypeRepository.findAll().size();
        cardClassType.setId(count.incrementAndGet());

        // Create the CardClassType
        CardClassTypeDTO cardClassTypeDTO = cardClassTypeMapper.toDto(cardClassType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCardClassTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cardClassTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cardClassTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CardClassType in the database
        List<CardClassType> cardClassTypeList = cardClassTypeRepository.findAll();
        assertThat(cardClassTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CardClassType in Elasticsearch
        verify(mockCardClassTypeSearchRepository, times(0)).save(cardClassType);
    }

    @Test
    @Transactional
    void putWithIdMismatchCardClassType() throws Exception {
        int databaseSizeBeforeUpdate = cardClassTypeRepository.findAll().size();
        cardClassType.setId(count.incrementAndGet());

        // Create the CardClassType
        CardClassTypeDTO cardClassTypeDTO = cardClassTypeMapper.toDto(cardClassType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCardClassTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cardClassTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CardClassType in the database
        List<CardClassType> cardClassTypeList = cardClassTypeRepository.findAll();
        assertThat(cardClassTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CardClassType in Elasticsearch
        verify(mockCardClassTypeSearchRepository, times(0)).save(cardClassType);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCardClassType() throws Exception {
        int databaseSizeBeforeUpdate = cardClassTypeRepository.findAll().size();
        cardClassType.setId(count.incrementAndGet());

        // Create the CardClassType
        CardClassTypeDTO cardClassTypeDTO = cardClassTypeMapper.toDto(cardClassType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCardClassTypeMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cardClassTypeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CardClassType in the database
        List<CardClassType> cardClassTypeList = cardClassTypeRepository.findAll();
        assertThat(cardClassTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CardClassType in Elasticsearch
        verify(mockCardClassTypeSearchRepository, times(0)).save(cardClassType);
    }

    @Test
    @Transactional
    void partialUpdateCardClassTypeWithPatch() throws Exception {
        // Initialize the database
        cardClassTypeRepository.saveAndFlush(cardClassType);

        int databaseSizeBeforeUpdate = cardClassTypeRepository.findAll().size();

        // Update the cardClassType using partial update
        CardClassType partialUpdatedCardClassType = new CardClassType();
        partialUpdatedCardClassType.setId(cardClassType.getId());

        restCardClassTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCardClassType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCardClassType))
            )
            .andExpect(status().isOk());

        // Validate the CardClassType in the database
        List<CardClassType> cardClassTypeList = cardClassTypeRepository.findAll();
        assertThat(cardClassTypeList).hasSize(databaseSizeBeforeUpdate);
        CardClassType testCardClassType = cardClassTypeList.get(cardClassTypeList.size() - 1);
        assertThat(testCardClassType.getCardClassTypeCode()).isEqualTo(DEFAULT_CARD_CLASS_TYPE_CODE);
        assertThat(testCardClassType.getCardClassType()).isEqualTo(DEFAULT_CARD_CLASS_TYPE);
        assertThat(testCardClassType.getCardClassDetails()).isEqualTo(DEFAULT_CARD_CLASS_DETAILS);
    }

    @Test
    @Transactional
    void fullUpdateCardClassTypeWithPatch() throws Exception {
        // Initialize the database
        cardClassTypeRepository.saveAndFlush(cardClassType);

        int databaseSizeBeforeUpdate = cardClassTypeRepository.findAll().size();

        // Update the cardClassType using partial update
        CardClassType partialUpdatedCardClassType = new CardClassType();
        partialUpdatedCardClassType.setId(cardClassType.getId());

        partialUpdatedCardClassType
            .cardClassTypeCode(UPDATED_CARD_CLASS_TYPE_CODE)
            .cardClassType(UPDATED_CARD_CLASS_TYPE)
            .cardClassDetails(UPDATED_CARD_CLASS_DETAILS);

        restCardClassTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCardClassType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCardClassType))
            )
            .andExpect(status().isOk());

        // Validate the CardClassType in the database
        List<CardClassType> cardClassTypeList = cardClassTypeRepository.findAll();
        assertThat(cardClassTypeList).hasSize(databaseSizeBeforeUpdate);
        CardClassType testCardClassType = cardClassTypeList.get(cardClassTypeList.size() - 1);
        assertThat(testCardClassType.getCardClassTypeCode()).isEqualTo(UPDATED_CARD_CLASS_TYPE_CODE);
        assertThat(testCardClassType.getCardClassType()).isEqualTo(UPDATED_CARD_CLASS_TYPE);
        assertThat(testCardClassType.getCardClassDetails()).isEqualTo(UPDATED_CARD_CLASS_DETAILS);
    }

    @Test
    @Transactional
    void patchNonExistingCardClassType() throws Exception {
        int databaseSizeBeforeUpdate = cardClassTypeRepository.findAll().size();
        cardClassType.setId(count.incrementAndGet());

        // Create the CardClassType
        CardClassTypeDTO cardClassTypeDTO = cardClassTypeMapper.toDto(cardClassType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCardClassTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, cardClassTypeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cardClassTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CardClassType in the database
        List<CardClassType> cardClassTypeList = cardClassTypeRepository.findAll();
        assertThat(cardClassTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CardClassType in Elasticsearch
        verify(mockCardClassTypeSearchRepository, times(0)).save(cardClassType);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCardClassType() throws Exception {
        int databaseSizeBeforeUpdate = cardClassTypeRepository.findAll().size();
        cardClassType.setId(count.incrementAndGet());

        // Create the CardClassType
        CardClassTypeDTO cardClassTypeDTO = cardClassTypeMapper.toDto(cardClassType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCardClassTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cardClassTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CardClassType in the database
        List<CardClassType> cardClassTypeList = cardClassTypeRepository.findAll();
        assertThat(cardClassTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CardClassType in Elasticsearch
        verify(mockCardClassTypeSearchRepository, times(0)).save(cardClassType);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCardClassType() throws Exception {
        int databaseSizeBeforeUpdate = cardClassTypeRepository.findAll().size();
        cardClassType.setId(count.incrementAndGet());

        // Create the CardClassType
        CardClassTypeDTO cardClassTypeDTO = cardClassTypeMapper.toDto(cardClassType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCardClassTypeMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cardClassTypeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CardClassType in the database
        List<CardClassType> cardClassTypeList = cardClassTypeRepository.findAll();
        assertThat(cardClassTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CardClassType in Elasticsearch
        verify(mockCardClassTypeSearchRepository, times(0)).save(cardClassType);
    }

    @Test
    @Transactional
    void deleteCardClassType() throws Exception {
        // Initialize the database
        cardClassTypeRepository.saveAndFlush(cardClassType);

        int databaseSizeBeforeDelete = cardClassTypeRepository.findAll().size();

        // Delete the cardClassType
        restCardClassTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, cardClassType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CardClassType> cardClassTypeList = cardClassTypeRepository.findAll();
        assertThat(cardClassTypeList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the CardClassType in Elasticsearch
        verify(mockCardClassTypeSearchRepository, times(1)).deleteById(cardClassType.getId());
    }

    @Test
    @Transactional
    void searchCardClassType() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        cardClassTypeRepository.saveAndFlush(cardClassType);
        when(mockCardClassTypeSearchRepository.search("id:" + cardClassType.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(cardClassType), PageRequest.of(0, 1), 1));

        // Search the cardClassType
        restCardClassTypeMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + cardClassType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cardClassType.getId().intValue())))
            .andExpect(jsonPath("$.[*].cardClassTypeCode").value(hasItem(DEFAULT_CARD_CLASS_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].cardClassType").value(hasItem(DEFAULT_CARD_CLASS_TYPE)))
            .andExpect(jsonPath("$.[*].cardClassDetails").value(hasItem(DEFAULT_CARD_CLASS_DETAILS.toString())));
    }
}
