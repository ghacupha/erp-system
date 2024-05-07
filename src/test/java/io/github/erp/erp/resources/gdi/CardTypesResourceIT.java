package io.github.erp.erp.resources.gdi;

/*-
 * Erp System - Mark X No 7 (Jehoiada Series) Server ver 1.7.9
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
import io.github.erp.domain.CardTypes;
import io.github.erp.repository.CardTypesRepository;
import io.github.erp.repository.search.CardTypesSearchRepository;
import io.github.erp.service.dto.CardTypesDTO;
import io.github.erp.service.mapper.CardTypesMapper;
import io.github.erp.web.rest.CardTypesResource;
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
 * Integration tests for the {@link CardTypesResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser(roles = {"GRANULAR_REPORTS_USER"})
class CardTypesResourceIT {

    private static final String DEFAULT_CARD_TYPE_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CARD_TYPE_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_CARD_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_CARD_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_CARD_TYPE_DETAILS = "AAAAAAAAAA";
    private static final String UPDATED_CARD_TYPE_DETAILS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/granular-data/card-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/granular-data/_search/card-types";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CardTypesRepository cardTypesRepository;

    @Autowired
    private CardTypesMapper cardTypesMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.CardTypesSearchRepositoryMockConfiguration
     */
    @Autowired
    private CardTypesSearchRepository mockCardTypesSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCardTypesMockMvc;

    private CardTypes cardTypes;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CardTypes createEntity(EntityManager em) {
        CardTypes cardTypes = new CardTypes()
            .cardTypeCode(DEFAULT_CARD_TYPE_CODE)
            .cardType(DEFAULT_CARD_TYPE)
            .cardTypeDetails(DEFAULT_CARD_TYPE_DETAILS);
        return cardTypes;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CardTypes createUpdatedEntity(EntityManager em) {
        CardTypes cardTypes = new CardTypes()
            .cardTypeCode(UPDATED_CARD_TYPE_CODE)
            .cardType(UPDATED_CARD_TYPE)
            .cardTypeDetails(UPDATED_CARD_TYPE_DETAILS);
        return cardTypes;
    }

    @BeforeEach
    public void initTest() {
        cardTypes = createEntity(em);
    }

    @Test
    @Transactional
    void createCardTypes() throws Exception {
        int databaseSizeBeforeCreate = cardTypesRepository.findAll().size();
        // Create the CardTypes
        CardTypesDTO cardTypesDTO = cardTypesMapper.toDto(cardTypes);
        restCardTypesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cardTypesDTO)))
            .andExpect(status().isCreated());

        // Validate the CardTypes in the database
        List<CardTypes> cardTypesList = cardTypesRepository.findAll();
        assertThat(cardTypesList).hasSize(databaseSizeBeforeCreate + 1);
        CardTypes testCardTypes = cardTypesList.get(cardTypesList.size() - 1);
        assertThat(testCardTypes.getCardTypeCode()).isEqualTo(DEFAULT_CARD_TYPE_CODE);
        assertThat(testCardTypes.getCardType()).isEqualTo(DEFAULT_CARD_TYPE);
        assertThat(testCardTypes.getCardTypeDetails()).isEqualTo(DEFAULT_CARD_TYPE_DETAILS);

        // Validate the CardTypes in Elasticsearch
        verify(mockCardTypesSearchRepository, times(1)).save(testCardTypes);
    }

    @Test
    @Transactional
    void createCardTypesWithExistingId() throws Exception {
        // Create the CardTypes with an existing ID
        cardTypes.setId(1L);
        CardTypesDTO cardTypesDTO = cardTypesMapper.toDto(cardTypes);

        int databaseSizeBeforeCreate = cardTypesRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCardTypesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cardTypesDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CardTypes in the database
        List<CardTypes> cardTypesList = cardTypesRepository.findAll();
        assertThat(cardTypesList).hasSize(databaseSizeBeforeCreate);

        // Validate the CardTypes in Elasticsearch
        verify(mockCardTypesSearchRepository, times(0)).save(cardTypes);
    }

    @Test
    @Transactional
    void checkCardTypeCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = cardTypesRepository.findAll().size();
        // set the field null
        cardTypes.setCardTypeCode(null);

        // Create the CardTypes, which fails.
        CardTypesDTO cardTypesDTO = cardTypesMapper.toDto(cardTypes);

        restCardTypesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cardTypesDTO)))
            .andExpect(status().isBadRequest());

        List<CardTypes> cardTypesList = cardTypesRepository.findAll();
        assertThat(cardTypesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCardTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = cardTypesRepository.findAll().size();
        // set the field null
        cardTypes.setCardType(null);

        // Create the CardTypes, which fails.
        CardTypesDTO cardTypesDTO = cardTypesMapper.toDto(cardTypes);

        restCardTypesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cardTypesDTO)))
            .andExpect(status().isBadRequest());

        List<CardTypes> cardTypesList = cardTypesRepository.findAll();
        assertThat(cardTypesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCardTypes() throws Exception {
        // Initialize the database
        cardTypesRepository.saveAndFlush(cardTypes);

        // Get all the cardTypesList
        restCardTypesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cardTypes.getId().intValue())))
            .andExpect(jsonPath("$.[*].cardTypeCode").value(hasItem(DEFAULT_CARD_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].cardType").value(hasItem(DEFAULT_CARD_TYPE)))
            .andExpect(jsonPath("$.[*].cardTypeDetails").value(hasItem(DEFAULT_CARD_TYPE_DETAILS.toString())));
    }

    @Test
    @Transactional
    void getCardTypes() throws Exception {
        // Initialize the database
        cardTypesRepository.saveAndFlush(cardTypes);

        // Get the cardTypes
        restCardTypesMockMvc
            .perform(get(ENTITY_API_URL_ID, cardTypes.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cardTypes.getId().intValue()))
            .andExpect(jsonPath("$.cardTypeCode").value(DEFAULT_CARD_TYPE_CODE))
            .andExpect(jsonPath("$.cardType").value(DEFAULT_CARD_TYPE))
            .andExpect(jsonPath("$.cardTypeDetails").value(DEFAULT_CARD_TYPE_DETAILS.toString()));
    }

    @Test
    @Transactional
    void getCardTypesByIdFiltering() throws Exception {
        // Initialize the database
        cardTypesRepository.saveAndFlush(cardTypes);

        Long id = cardTypes.getId();

        defaultCardTypesShouldBeFound("id.equals=" + id);
        defaultCardTypesShouldNotBeFound("id.notEquals=" + id);

        defaultCardTypesShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCardTypesShouldNotBeFound("id.greaterThan=" + id);

        defaultCardTypesShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCardTypesShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCardTypesByCardTypeCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        cardTypesRepository.saveAndFlush(cardTypes);

        // Get all the cardTypesList where cardTypeCode equals to DEFAULT_CARD_TYPE_CODE
        defaultCardTypesShouldBeFound("cardTypeCode.equals=" + DEFAULT_CARD_TYPE_CODE);

        // Get all the cardTypesList where cardTypeCode equals to UPDATED_CARD_TYPE_CODE
        defaultCardTypesShouldNotBeFound("cardTypeCode.equals=" + UPDATED_CARD_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllCardTypesByCardTypeCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        cardTypesRepository.saveAndFlush(cardTypes);

        // Get all the cardTypesList where cardTypeCode not equals to DEFAULT_CARD_TYPE_CODE
        defaultCardTypesShouldNotBeFound("cardTypeCode.notEquals=" + DEFAULT_CARD_TYPE_CODE);

        // Get all the cardTypesList where cardTypeCode not equals to UPDATED_CARD_TYPE_CODE
        defaultCardTypesShouldBeFound("cardTypeCode.notEquals=" + UPDATED_CARD_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllCardTypesByCardTypeCodeIsInShouldWork() throws Exception {
        // Initialize the database
        cardTypesRepository.saveAndFlush(cardTypes);

        // Get all the cardTypesList where cardTypeCode in DEFAULT_CARD_TYPE_CODE or UPDATED_CARD_TYPE_CODE
        defaultCardTypesShouldBeFound("cardTypeCode.in=" + DEFAULT_CARD_TYPE_CODE + "," + UPDATED_CARD_TYPE_CODE);

        // Get all the cardTypesList where cardTypeCode equals to UPDATED_CARD_TYPE_CODE
        defaultCardTypesShouldNotBeFound("cardTypeCode.in=" + UPDATED_CARD_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllCardTypesByCardTypeCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        cardTypesRepository.saveAndFlush(cardTypes);

        // Get all the cardTypesList where cardTypeCode is not null
        defaultCardTypesShouldBeFound("cardTypeCode.specified=true");

        // Get all the cardTypesList where cardTypeCode is null
        defaultCardTypesShouldNotBeFound("cardTypeCode.specified=false");
    }

    @Test
    @Transactional
    void getAllCardTypesByCardTypeCodeContainsSomething() throws Exception {
        // Initialize the database
        cardTypesRepository.saveAndFlush(cardTypes);

        // Get all the cardTypesList where cardTypeCode contains DEFAULT_CARD_TYPE_CODE
        defaultCardTypesShouldBeFound("cardTypeCode.contains=" + DEFAULT_CARD_TYPE_CODE);

        // Get all the cardTypesList where cardTypeCode contains UPDATED_CARD_TYPE_CODE
        defaultCardTypesShouldNotBeFound("cardTypeCode.contains=" + UPDATED_CARD_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllCardTypesByCardTypeCodeNotContainsSomething() throws Exception {
        // Initialize the database
        cardTypesRepository.saveAndFlush(cardTypes);

        // Get all the cardTypesList where cardTypeCode does not contain DEFAULT_CARD_TYPE_CODE
        defaultCardTypesShouldNotBeFound("cardTypeCode.doesNotContain=" + DEFAULT_CARD_TYPE_CODE);

        // Get all the cardTypesList where cardTypeCode does not contain UPDATED_CARD_TYPE_CODE
        defaultCardTypesShouldBeFound("cardTypeCode.doesNotContain=" + UPDATED_CARD_TYPE_CODE);
    }

    @Test
    @Transactional
    void getAllCardTypesByCardTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        cardTypesRepository.saveAndFlush(cardTypes);

        // Get all the cardTypesList where cardType equals to DEFAULT_CARD_TYPE
        defaultCardTypesShouldBeFound("cardType.equals=" + DEFAULT_CARD_TYPE);

        // Get all the cardTypesList where cardType equals to UPDATED_CARD_TYPE
        defaultCardTypesShouldNotBeFound("cardType.equals=" + UPDATED_CARD_TYPE);
    }

    @Test
    @Transactional
    void getAllCardTypesByCardTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        cardTypesRepository.saveAndFlush(cardTypes);

        // Get all the cardTypesList where cardType not equals to DEFAULT_CARD_TYPE
        defaultCardTypesShouldNotBeFound("cardType.notEquals=" + DEFAULT_CARD_TYPE);

        // Get all the cardTypesList where cardType not equals to UPDATED_CARD_TYPE
        defaultCardTypesShouldBeFound("cardType.notEquals=" + UPDATED_CARD_TYPE);
    }

    @Test
    @Transactional
    void getAllCardTypesByCardTypeIsInShouldWork() throws Exception {
        // Initialize the database
        cardTypesRepository.saveAndFlush(cardTypes);

        // Get all the cardTypesList where cardType in DEFAULT_CARD_TYPE or UPDATED_CARD_TYPE
        defaultCardTypesShouldBeFound("cardType.in=" + DEFAULT_CARD_TYPE + "," + UPDATED_CARD_TYPE);

        // Get all the cardTypesList where cardType equals to UPDATED_CARD_TYPE
        defaultCardTypesShouldNotBeFound("cardType.in=" + UPDATED_CARD_TYPE);
    }

    @Test
    @Transactional
    void getAllCardTypesByCardTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        cardTypesRepository.saveAndFlush(cardTypes);

        // Get all the cardTypesList where cardType is not null
        defaultCardTypesShouldBeFound("cardType.specified=true");

        // Get all the cardTypesList where cardType is null
        defaultCardTypesShouldNotBeFound("cardType.specified=false");
    }

    @Test
    @Transactional
    void getAllCardTypesByCardTypeContainsSomething() throws Exception {
        // Initialize the database
        cardTypesRepository.saveAndFlush(cardTypes);

        // Get all the cardTypesList where cardType contains DEFAULT_CARD_TYPE
        defaultCardTypesShouldBeFound("cardType.contains=" + DEFAULT_CARD_TYPE);

        // Get all the cardTypesList where cardType contains UPDATED_CARD_TYPE
        defaultCardTypesShouldNotBeFound("cardType.contains=" + UPDATED_CARD_TYPE);
    }

    @Test
    @Transactional
    void getAllCardTypesByCardTypeNotContainsSomething() throws Exception {
        // Initialize the database
        cardTypesRepository.saveAndFlush(cardTypes);

        // Get all the cardTypesList where cardType does not contain DEFAULT_CARD_TYPE
        defaultCardTypesShouldNotBeFound("cardType.doesNotContain=" + DEFAULT_CARD_TYPE);

        // Get all the cardTypesList where cardType does not contain UPDATED_CARD_TYPE
        defaultCardTypesShouldBeFound("cardType.doesNotContain=" + UPDATED_CARD_TYPE);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCardTypesShouldBeFound(String filter) throws Exception {
        restCardTypesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cardTypes.getId().intValue())))
            .andExpect(jsonPath("$.[*].cardTypeCode").value(hasItem(DEFAULT_CARD_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].cardType").value(hasItem(DEFAULT_CARD_TYPE)))
            .andExpect(jsonPath("$.[*].cardTypeDetails").value(hasItem(DEFAULT_CARD_TYPE_DETAILS.toString())));

        // Check, that the count call also returns 1
        restCardTypesMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCardTypesShouldNotBeFound(String filter) throws Exception {
        restCardTypesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCardTypesMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCardTypes() throws Exception {
        // Get the cardTypes
        restCardTypesMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCardTypes() throws Exception {
        // Initialize the database
        cardTypesRepository.saveAndFlush(cardTypes);

        int databaseSizeBeforeUpdate = cardTypesRepository.findAll().size();

        // Update the cardTypes
        CardTypes updatedCardTypes = cardTypesRepository.findById(cardTypes.getId()).get();
        // Disconnect from session so that the updates on updatedCardTypes are not directly saved in db
        em.detach(updatedCardTypes);
        updatedCardTypes.cardTypeCode(UPDATED_CARD_TYPE_CODE).cardType(UPDATED_CARD_TYPE).cardTypeDetails(UPDATED_CARD_TYPE_DETAILS);
        CardTypesDTO cardTypesDTO = cardTypesMapper.toDto(updatedCardTypes);

        restCardTypesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cardTypesDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cardTypesDTO))
            )
            .andExpect(status().isOk());

        // Validate the CardTypes in the database
        List<CardTypes> cardTypesList = cardTypesRepository.findAll();
        assertThat(cardTypesList).hasSize(databaseSizeBeforeUpdate);
        CardTypes testCardTypes = cardTypesList.get(cardTypesList.size() - 1);
        assertThat(testCardTypes.getCardTypeCode()).isEqualTo(UPDATED_CARD_TYPE_CODE);
        assertThat(testCardTypes.getCardType()).isEqualTo(UPDATED_CARD_TYPE);
        assertThat(testCardTypes.getCardTypeDetails()).isEqualTo(UPDATED_CARD_TYPE_DETAILS);

        // Validate the CardTypes in Elasticsearch
        verify(mockCardTypesSearchRepository).save(testCardTypes);
    }

    @Test
    @Transactional
    void putNonExistingCardTypes() throws Exception {
        int databaseSizeBeforeUpdate = cardTypesRepository.findAll().size();
        cardTypes.setId(count.incrementAndGet());

        // Create the CardTypes
        CardTypesDTO cardTypesDTO = cardTypesMapper.toDto(cardTypes);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCardTypesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cardTypesDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cardTypesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CardTypes in the database
        List<CardTypes> cardTypesList = cardTypesRepository.findAll();
        assertThat(cardTypesList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CardTypes in Elasticsearch
        verify(mockCardTypesSearchRepository, times(0)).save(cardTypes);
    }

    @Test
    @Transactional
    void putWithIdMismatchCardTypes() throws Exception {
        int databaseSizeBeforeUpdate = cardTypesRepository.findAll().size();
        cardTypes.setId(count.incrementAndGet());

        // Create the CardTypes
        CardTypesDTO cardTypesDTO = cardTypesMapper.toDto(cardTypes);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCardTypesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cardTypesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CardTypes in the database
        List<CardTypes> cardTypesList = cardTypesRepository.findAll();
        assertThat(cardTypesList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CardTypes in Elasticsearch
        verify(mockCardTypesSearchRepository, times(0)).save(cardTypes);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCardTypes() throws Exception {
        int databaseSizeBeforeUpdate = cardTypesRepository.findAll().size();
        cardTypes.setId(count.incrementAndGet());

        // Create the CardTypes
        CardTypesDTO cardTypesDTO = cardTypesMapper.toDto(cardTypes);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCardTypesMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cardTypesDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CardTypes in the database
        List<CardTypes> cardTypesList = cardTypesRepository.findAll();
        assertThat(cardTypesList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CardTypes in Elasticsearch
        verify(mockCardTypesSearchRepository, times(0)).save(cardTypes);
    }

    @Test
    @Transactional
    void partialUpdateCardTypesWithPatch() throws Exception {
        // Initialize the database
        cardTypesRepository.saveAndFlush(cardTypes);

        int databaseSizeBeforeUpdate = cardTypesRepository.findAll().size();

        // Update the cardTypes using partial update
        CardTypes partialUpdatedCardTypes = new CardTypes();
        partialUpdatedCardTypes.setId(cardTypes.getId());

        partialUpdatedCardTypes.cardTypeCode(UPDATED_CARD_TYPE_CODE).cardType(UPDATED_CARD_TYPE).cardTypeDetails(UPDATED_CARD_TYPE_DETAILS);

        restCardTypesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCardTypes.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCardTypes))
            )
            .andExpect(status().isOk());

        // Validate the CardTypes in the database
        List<CardTypes> cardTypesList = cardTypesRepository.findAll();
        assertThat(cardTypesList).hasSize(databaseSizeBeforeUpdate);
        CardTypes testCardTypes = cardTypesList.get(cardTypesList.size() - 1);
        assertThat(testCardTypes.getCardTypeCode()).isEqualTo(UPDATED_CARD_TYPE_CODE);
        assertThat(testCardTypes.getCardType()).isEqualTo(UPDATED_CARD_TYPE);
        assertThat(testCardTypes.getCardTypeDetails()).isEqualTo(UPDATED_CARD_TYPE_DETAILS);
    }

    @Test
    @Transactional
    void fullUpdateCardTypesWithPatch() throws Exception {
        // Initialize the database
        cardTypesRepository.saveAndFlush(cardTypes);

        int databaseSizeBeforeUpdate = cardTypesRepository.findAll().size();

        // Update the cardTypes using partial update
        CardTypes partialUpdatedCardTypes = new CardTypes();
        partialUpdatedCardTypes.setId(cardTypes.getId());

        partialUpdatedCardTypes.cardTypeCode(UPDATED_CARD_TYPE_CODE).cardType(UPDATED_CARD_TYPE).cardTypeDetails(UPDATED_CARD_TYPE_DETAILS);

        restCardTypesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCardTypes.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCardTypes))
            )
            .andExpect(status().isOk());

        // Validate the CardTypes in the database
        List<CardTypes> cardTypesList = cardTypesRepository.findAll();
        assertThat(cardTypesList).hasSize(databaseSizeBeforeUpdate);
        CardTypes testCardTypes = cardTypesList.get(cardTypesList.size() - 1);
        assertThat(testCardTypes.getCardTypeCode()).isEqualTo(UPDATED_CARD_TYPE_CODE);
        assertThat(testCardTypes.getCardType()).isEqualTo(UPDATED_CARD_TYPE);
        assertThat(testCardTypes.getCardTypeDetails()).isEqualTo(UPDATED_CARD_TYPE_DETAILS);
    }

    @Test
    @Transactional
    void patchNonExistingCardTypes() throws Exception {
        int databaseSizeBeforeUpdate = cardTypesRepository.findAll().size();
        cardTypes.setId(count.incrementAndGet());

        // Create the CardTypes
        CardTypesDTO cardTypesDTO = cardTypesMapper.toDto(cardTypes);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCardTypesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, cardTypesDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cardTypesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CardTypes in the database
        List<CardTypes> cardTypesList = cardTypesRepository.findAll();
        assertThat(cardTypesList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CardTypes in Elasticsearch
        verify(mockCardTypesSearchRepository, times(0)).save(cardTypes);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCardTypes() throws Exception {
        int databaseSizeBeforeUpdate = cardTypesRepository.findAll().size();
        cardTypes.setId(count.incrementAndGet());

        // Create the CardTypes
        CardTypesDTO cardTypesDTO = cardTypesMapper.toDto(cardTypes);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCardTypesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cardTypesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CardTypes in the database
        List<CardTypes> cardTypesList = cardTypesRepository.findAll();
        assertThat(cardTypesList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CardTypes in Elasticsearch
        verify(mockCardTypesSearchRepository, times(0)).save(cardTypes);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCardTypes() throws Exception {
        int databaseSizeBeforeUpdate = cardTypesRepository.findAll().size();
        cardTypes.setId(count.incrementAndGet());

        // Create the CardTypes
        CardTypesDTO cardTypesDTO = cardTypesMapper.toDto(cardTypes);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCardTypesMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(cardTypesDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CardTypes in the database
        List<CardTypes> cardTypesList = cardTypesRepository.findAll();
        assertThat(cardTypesList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CardTypes in Elasticsearch
        verify(mockCardTypesSearchRepository, times(0)).save(cardTypes);
    }

    @Test
    @Transactional
    void deleteCardTypes() throws Exception {
        // Initialize the database
        cardTypesRepository.saveAndFlush(cardTypes);

        int databaseSizeBeforeDelete = cardTypesRepository.findAll().size();

        // Delete the cardTypes
        restCardTypesMockMvc
            .perform(delete(ENTITY_API_URL_ID, cardTypes.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CardTypes> cardTypesList = cardTypesRepository.findAll();
        assertThat(cardTypesList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the CardTypes in Elasticsearch
        verify(mockCardTypesSearchRepository, times(1)).deleteById(cardTypes.getId());
    }

    @Test
    @Transactional
    void searchCardTypes() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        cardTypesRepository.saveAndFlush(cardTypes);
        when(mockCardTypesSearchRepository.search("id:" + cardTypes.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(cardTypes), PageRequest.of(0, 1), 1));

        // Search the cardTypes
        restCardTypesMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + cardTypes.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cardTypes.getId().intValue())))
            .andExpect(jsonPath("$.[*].cardTypeCode").value(hasItem(DEFAULT_CARD_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].cardType").value(hasItem(DEFAULT_CARD_TYPE)))
            .andExpect(jsonPath("$.[*].cardTypeDetails").value(hasItem(DEFAULT_CARD_TYPE_DETAILS.toString())));
    }
}
