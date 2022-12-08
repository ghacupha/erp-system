package io.github.erp.web.rest.api;

/*-
 * Erp System - Mark III No 4 (Caleb Series) Server ver 0.1.5-SNAPSHOT
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
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import io.github.erp.IntegrationTest;
import io.github.erp.domain.MessageToken;
import io.github.erp.domain.Placeholder;
import io.github.erp.repository.MessageTokenRepository;
import io.github.erp.repository.search.MessageTokenSearchRepository;
import io.github.erp.service.MessageTokenService;
import io.github.erp.service.dto.MessageTokenDTO;
import io.github.erp.service.mapper.MessageTokenMapper;
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

/**
 * Integration tests for the {@link MessageTokenResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser(roles = {"DEV"})
public class MessageTokenResourceIT {

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Long DEFAULT_TIME_SENT = 1L;
    private static final Long UPDATED_TIME_SENT = 2L;
    private static final Long SMALLER_TIME_SENT = 1L - 1L;

    private static final String DEFAULT_TOKEN_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_TOKEN_VALUE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_RECEIVED = false;
    private static final Boolean UPDATED_RECEIVED = true;

    private static final Boolean DEFAULT_ACTIONED = false;
    private static final Boolean UPDATED_ACTIONED = true;

    private static final Boolean DEFAULT_CONTENT_FULLY_ENQUEUED = false;
    private static final Boolean UPDATED_CONTENT_FULLY_ENQUEUED = true;

    private static final String ENTITY_API_URL = "/api/dev/message-tokens";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/dev/_search/message-tokens";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MessageTokenRepository messageTokenRepository;

    @Mock
    private MessageTokenRepository messageTokenRepositoryMock;

    @Autowired
    private MessageTokenMapper messageTokenMapper;

    @Mock
    private MessageTokenService messageTokenServiceMock;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.MessageTokenSearchRepositoryMockConfiguration
     */
    @Autowired
    private MessageTokenSearchRepository mockMessageTokenSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMessageTokenMockMvc;

    private MessageToken messageToken;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MessageToken createEntity(EntityManager em) {
        MessageToken messageToken = new MessageToken()
            .description(DEFAULT_DESCRIPTION)
            .timeSent(DEFAULT_TIME_SENT)
            .tokenValue(DEFAULT_TOKEN_VALUE)
            .received(DEFAULT_RECEIVED)
            .actioned(DEFAULT_ACTIONED)
            .contentFullyEnqueued(DEFAULT_CONTENT_FULLY_ENQUEUED);
        return messageToken;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MessageToken createUpdatedEntity(EntityManager em) {
        MessageToken messageToken = new MessageToken()
            .description(UPDATED_DESCRIPTION)
            .timeSent(UPDATED_TIME_SENT)
            .tokenValue(UPDATED_TOKEN_VALUE)
            .received(UPDATED_RECEIVED)
            .actioned(UPDATED_ACTIONED)
            .contentFullyEnqueued(UPDATED_CONTENT_FULLY_ENQUEUED);
        return messageToken;
    }

    @BeforeEach
    public void initTest() {
        messageToken = createEntity(em);
    }

    @Test
    @Transactional
    void createMessageToken() throws Exception {
        int databaseSizeBeforeCreate = messageTokenRepository.findAll().size();
        // Create the MessageToken
        MessageTokenDTO messageTokenDTO = messageTokenMapper.toDto(messageToken);
        restMessageTokenMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(messageTokenDTO))
            )
            .andExpect(status().isCreated());

        // Validate the MessageToken in the database
        List<MessageToken> messageTokenList = messageTokenRepository.findAll();
        assertThat(messageTokenList).hasSize(databaseSizeBeforeCreate + 1);
        MessageToken testMessageToken = messageTokenList.get(messageTokenList.size() - 1);
        assertThat(testMessageToken.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testMessageToken.getTimeSent()).isEqualTo(DEFAULT_TIME_SENT);
        assertThat(testMessageToken.getTokenValue()).isEqualTo(DEFAULT_TOKEN_VALUE);
        assertThat(testMessageToken.getReceived()).isEqualTo(DEFAULT_RECEIVED);
        assertThat(testMessageToken.getActioned()).isEqualTo(DEFAULT_ACTIONED);
        assertThat(testMessageToken.getContentFullyEnqueued()).isEqualTo(DEFAULT_CONTENT_FULLY_ENQUEUED);

        // Validate the MessageToken in Elasticsearch
        verify(mockMessageTokenSearchRepository, times(1)).save(testMessageToken);
    }

    @Test
    @Transactional
    void createMessageTokenWithExistingId() throws Exception {
        // Create the MessageToken with an existing ID
        messageToken.setId(1L);
        MessageTokenDTO messageTokenDTO = messageTokenMapper.toDto(messageToken);

        int databaseSizeBeforeCreate = messageTokenRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMessageTokenMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(messageTokenDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MessageToken in the database
        List<MessageToken> messageTokenList = messageTokenRepository.findAll();
        assertThat(messageTokenList).hasSize(databaseSizeBeforeCreate);

        // Validate the MessageToken in Elasticsearch
        verify(mockMessageTokenSearchRepository, times(0)).save(messageToken);
    }

    @Test
    @Transactional
    void checkTimeSentIsRequired() throws Exception {
        int databaseSizeBeforeTest = messageTokenRepository.findAll().size();
        // set the field null
        messageToken.setTimeSent(null);

        // Create the MessageToken, which fails.
        MessageTokenDTO messageTokenDTO = messageTokenMapper.toDto(messageToken);

        restMessageTokenMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(messageTokenDTO))
            )
            .andExpect(status().isBadRequest());

        List<MessageToken> messageTokenList = messageTokenRepository.findAll();
        assertThat(messageTokenList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTokenValueIsRequired() throws Exception {
        int databaseSizeBeforeTest = messageTokenRepository.findAll().size();
        // set the field null
        messageToken.setTokenValue(null);

        // Create the MessageToken, which fails.
        MessageTokenDTO messageTokenDTO = messageTokenMapper.toDto(messageToken);

        restMessageTokenMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(messageTokenDTO))
            )
            .andExpect(status().isBadRequest());

        List<MessageToken> messageTokenList = messageTokenRepository.findAll();
        assertThat(messageTokenList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMessageTokens() throws Exception {
        // Initialize the database
        messageTokenRepository.saveAndFlush(messageToken);

        // Get all the messageTokenList
        restMessageTokenMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(messageToken.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].timeSent").value(hasItem(DEFAULT_TIME_SENT.intValue())))
            .andExpect(jsonPath("$.[*].tokenValue").value(hasItem(DEFAULT_TOKEN_VALUE)))
            .andExpect(jsonPath("$.[*].received").value(hasItem(DEFAULT_RECEIVED.booleanValue())))
            .andExpect(jsonPath("$.[*].actioned").value(hasItem(DEFAULT_ACTIONED.booleanValue())))
            .andExpect(jsonPath("$.[*].contentFullyEnqueued").value(hasItem(DEFAULT_CONTENT_FULLY_ENQUEUED.booleanValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllMessageTokensWithEagerRelationshipsIsEnabled() throws Exception {
        when(messageTokenServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restMessageTokenMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(messageTokenServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllMessageTokensWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(messageTokenServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restMessageTokenMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(messageTokenServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getMessageToken() throws Exception {
        // Initialize the database
        messageTokenRepository.saveAndFlush(messageToken);

        // Get the messageToken
        restMessageTokenMockMvc
            .perform(get(ENTITY_API_URL_ID, messageToken.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(messageToken.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.timeSent").value(DEFAULT_TIME_SENT.intValue()))
            .andExpect(jsonPath("$.tokenValue").value(DEFAULT_TOKEN_VALUE))
            .andExpect(jsonPath("$.received").value(DEFAULT_RECEIVED.booleanValue()))
            .andExpect(jsonPath("$.actioned").value(DEFAULT_ACTIONED.booleanValue()))
            .andExpect(jsonPath("$.contentFullyEnqueued").value(DEFAULT_CONTENT_FULLY_ENQUEUED.booleanValue()));
    }

    @Test
    @Transactional
    void getMessageTokensByIdFiltering() throws Exception {
        // Initialize the database
        messageTokenRepository.saveAndFlush(messageToken);

        Long id = messageToken.getId();

        defaultMessageTokenShouldBeFound("id.equals=" + id);
        defaultMessageTokenShouldNotBeFound("id.notEquals=" + id);

        defaultMessageTokenShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultMessageTokenShouldNotBeFound("id.greaterThan=" + id);

        defaultMessageTokenShouldBeFound("id.lessThanOrEqual=" + id);
        defaultMessageTokenShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllMessageTokensByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        messageTokenRepository.saveAndFlush(messageToken);

        // Get all the messageTokenList where description equals to DEFAULT_DESCRIPTION
        defaultMessageTokenShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the messageTokenList where description equals to UPDATED_DESCRIPTION
        defaultMessageTokenShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllMessageTokensByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        messageTokenRepository.saveAndFlush(messageToken);

        // Get all the messageTokenList where description not equals to DEFAULT_DESCRIPTION
        defaultMessageTokenShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the messageTokenList where description not equals to UPDATED_DESCRIPTION
        defaultMessageTokenShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllMessageTokensByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        messageTokenRepository.saveAndFlush(messageToken);

        // Get all the messageTokenList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultMessageTokenShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the messageTokenList where description equals to UPDATED_DESCRIPTION
        defaultMessageTokenShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllMessageTokensByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        messageTokenRepository.saveAndFlush(messageToken);

        // Get all the messageTokenList where description is not null
        defaultMessageTokenShouldBeFound("description.specified=true");

        // Get all the messageTokenList where description is null
        defaultMessageTokenShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllMessageTokensByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        messageTokenRepository.saveAndFlush(messageToken);

        // Get all the messageTokenList where description contains DEFAULT_DESCRIPTION
        defaultMessageTokenShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the messageTokenList where description contains UPDATED_DESCRIPTION
        defaultMessageTokenShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllMessageTokensByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        messageTokenRepository.saveAndFlush(messageToken);

        // Get all the messageTokenList where description does not contain DEFAULT_DESCRIPTION
        defaultMessageTokenShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the messageTokenList where description does not contain UPDATED_DESCRIPTION
        defaultMessageTokenShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllMessageTokensByTimeSentIsEqualToSomething() throws Exception {
        // Initialize the database
        messageTokenRepository.saveAndFlush(messageToken);

        // Get all the messageTokenList where timeSent equals to DEFAULT_TIME_SENT
        defaultMessageTokenShouldBeFound("timeSent.equals=" + DEFAULT_TIME_SENT);

        // Get all the messageTokenList where timeSent equals to UPDATED_TIME_SENT
        defaultMessageTokenShouldNotBeFound("timeSent.equals=" + UPDATED_TIME_SENT);
    }

    @Test
    @Transactional
    void getAllMessageTokensByTimeSentIsNotEqualToSomething() throws Exception {
        // Initialize the database
        messageTokenRepository.saveAndFlush(messageToken);

        // Get all the messageTokenList where timeSent not equals to DEFAULT_TIME_SENT
        defaultMessageTokenShouldNotBeFound("timeSent.notEquals=" + DEFAULT_TIME_SENT);

        // Get all the messageTokenList where timeSent not equals to UPDATED_TIME_SENT
        defaultMessageTokenShouldBeFound("timeSent.notEquals=" + UPDATED_TIME_SENT);
    }

    @Test
    @Transactional
    void getAllMessageTokensByTimeSentIsInShouldWork() throws Exception {
        // Initialize the database
        messageTokenRepository.saveAndFlush(messageToken);

        // Get all the messageTokenList where timeSent in DEFAULT_TIME_SENT or UPDATED_TIME_SENT
        defaultMessageTokenShouldBeFound("timeSent.in=" + DEFAULT_TIME_SENT + "," + UPDATED_TIME_SENT);

        // Get all the messageTokenList where timeSent equals to UPDATED_TIME_SENT
        defaultMessageTokenShouldNotBeFound("timeSent.in=" + UPDATED_TIME_SENT);
    }

    @Test
    @Transactional
    void getAllMessageTokensByTimeSentIsNullOrNotNull() throws Exception {
        // Initialize the database
        messageTokenRepository.saveAndFlush(messageToken);

        // Get all the messageTokenList where timeSent is not null
        defaultMessageTokenShouldBeFound("timeSent.specified=true");

        // Get all the messageTokenList where timeSent is null
        defaultMessageTokenShouldNotBeFound("timeSent.specified=false");
    }

    @Test
    @Transactional
    void getAllMessageTokensByTimeSentIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        messageTokenRepository.saveAndFlush(messageToken);

        // Get all the messageTokenList where timeSent is greater than or equal to DEFAULT_TIME_SENT
        defaultMessageTokenShouldBeFound("timeSent.greaterThanOrEqual=" + DEFAULT_TIME_SENT);

        // Get all the messageTokenList where timeSent is greater than or equal to UPDATED_TIME_SENT
        defaultMessageTokenShouldNotBeFound("timeSent.greaterThanOrEqual=" + UPDATED_TIME_SENT);
    }

    @Test
    @Transactional
    void getAllMessageTokensByTimeSentIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        messageTokenRepository.saveAndFlush(messageToken);

        // Get all the messageTokenList where timeSent is less than or equal to DEFAULT_TIME_SENT
        defaultMessageTokenShouldBeFound("timeSent.lessThanOrEqual=" + DEFAULT_TIME_SENT);

        // Get all the messageTokenList where timeSent is less than or equal to SMALLER_TIME_SENT
        defaultMessageTokenShouldNotBeFound("timeSent.lessThanOrEqual=" + SMALLER_TIME_SENT);
    }

    @Test
    @Transactional
    void getAllMessageTokensByTimeSentIsLessThanSomething() throws Exception {
        // Initialize the database
        messageTokenRepository.saveAndFlush(messageToken);

        // Get all the messageTokenList where timeSent is less than DEFAULT_TIME_SENT
        defaultMessageTokenShouldNotBeFound("timeSent.lessThan=" + DEFAULT_TIME_SENT);

        // Get all the messageTokenList where timeSent is less than UPDATED_TIME_SENT
        defaultMessageTokenShouldBeFound("timeSent.lessThan=" + UPDATED_TIME_SENT);
    }

    @Test
    @Transactional
    void getAllMessageTokensByTimeSentIsGreaterThanSomething() throws Exception {
        // Initialize the database
        messageTokenRepository.saveAndFlush(messageToken);

        // Get all the messageTokenList where timeSent is greater than DEFAULT_TIME_SENT
        defaultMessageTokenShouldNotBeFound("timeSent.greaterThan=" + DEFAULT_TIME_SENT);

        // Get all the messageTokenList where timeSent is greater than SMALLER_TIME_SENT
        defaultMessageTokenShouldBeFound("timeSent.greaterThan=" + SMALLER_TIME_SENT);
    }

    @Test
    @Transactional
    void getAllMessageTokensByTokenValueIsEqualToSomething() throws Exception {
        // Initialize the database
        messageTokenRepository.saveAndFlush(messageToken);

        // Get all the messageTokenList where tokenValue equals to DEFAULT_TOKEN_VALUE
        defaultMessageTokenShouldBeFound("tokenValue.equals=" + DEFAULT_TOKEN_VALUE);

        // Get all the messageTokenList where tokenValue equals to UPDATED_TOKEN_VALUE
        defaultMessageTokenShouldNotBeFound("tokenValue.equals=" + UPDATED_TOKEN_VALUE);
    }

    @Test
    @Transactional
    void getAllMessageTokensByTokenValueIsNotEqualToSomething() throws Exception {
        // Initialize the database
        messageTokenRepository.saveAndFlush(messageToken);

        // Get all the messageTokenList where tokenValue not equals to DEFAULT_TOKEN_VALUE
        defaultMessageTokenShouldNotBeFound("tokenValue.notEquals=" + DEFAULT_TOKEN_VALUE);

        // Get all the messageTokenList where tokenValue not equals to UPDATED_TOKEN_VALUE
        defaultMessageTokenShouldBeFound("tokenValue.notEquals=" + UPDATED_TOKEN_VALUE);
    }

    @Test
    @Transactional
    void getAllMessageTokensByTokenValueIsInShouldWork() throws Exception {
        // Initialize the database
        messageTokenRepository.saveAndFlush(messageToken);

        // Get all the messageTokenList where tokenValue in DEFAULT_TOKEN_VALUE or UPDATED_TOKEN_VALUE
        defaultMessageTokenShouldBeFound("tokenValue.in=" + DEFAULT_TOKEN_VALUE + "," + UPDATED_TOKEN_VALUE);

        // Get all the messageTokenList where tokenValue equals to UPDATED_TOKEN_VALUE
        defaultMessageTokenShouldNotBeFound("tokenValue.in=" + UPDATED_TOKEN_VALUE);
    }

    @Test
    @Transactional
    void getAllMessageTokensByTokenValueIsNullOrNotNull() throws Exception {
        // Initialize the database
        messageTokenRepository.saveAndFlush(messageToken);

        // Get all the messageTokenList where tokenValue is not null
        defaultMessageTokenShouldBeFound("tokenValue.specified=true");

        // Get all the messageTokenList where tokenValue is null
        defaultMessageTokenShouldNotBeFound("tokenValue.specified=false");
    }

    @Test
    @Transactional
    void getAllMessageTokensByTokenValueContainsSomething() throws Exception {
        // Initialize the database
        messageTokenRepository.saveAndFlush(messageToken);

        // Get all the messageTokenList where tokenValue contains DEFAULT_TOKEN_VALUE
        defaultMessageTokenShouldBeFound("tokenValue.contains=" + DEFAULT_TOKEN_VALUE);

        // Get all the messageTokenList where tokenValue contains UPDATED_TOKEN_VALUE
        defaultMessageTokenShouldNotBeFound("tokenValue.contains=" + UPDATED_TOKEN_VALUE);
    }

    @Test
    @Transactional
    void getAllMessageTokensByTokenValueNotContainsSomething() throws Exception {
        // Initialize the database
        messageTokenRepository.saveAndFlush(messageToken);

        // Get all the messageTokenList where tokenValue does not contain DEFAULT_TOKEN_VALUE
        defaultMessageTokenShouldNotBeFound("tokenValue.doesNotContain=" + DEFAULT_TOKEN_VALUE);

        // Get all the messageTokenList where tokenValue does not contain UPDATED_TOKEN_VALUE
        defaultMessageTokenShouldBeFound("tokenValue.doesNotContain=" + UPDATED_TOKEN_VALUE);
    }

    @Test
    @Transactional
    void getAllMessageTokensByReceivedIsEqualToSomething() throws Exception {
        // Initialize the database
        messageTokenRepository.saveAndFlush(messageToken);

        // Get all the messageTokenList where received equals to DEFAULT_RECEIVED
        defaultMessageTokenShouldBeFound("received.equals=" + DEFAULT_RECEIVED);

        // Get all the messageTokenList where received equals to UPDATED_RECEIVED
        defaultMessageTokenShouldNotBeFound("received.equals=" + UPDATED_RECEIVED);
    }

    @Test
    @Transactional
    void getAllMessageTokensByReceivedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        messageTokenRepository.saveAndFlush(messageToken);

        // Get all the messageTokenList where received not equals to DEFAULT_RECEIVED
        defaultMessageTokenShouldNotBeFound("received.notEquals=" + DEFAULT_RECEIVED);

        // Get all the messageTokenList where received not equals to UPDATED_RECEIVED
        defaultMessageTokenShouldBeFound("received.notEquals=" + UPDATED_RECEIVED);
    }

    @Test
    @Transactional
    void getAllMessageTokensByReceivedIsInShouldWork() throws Exception {
        // Initialize the database
        messageTokenRepository.saveAndFlush(messageToken);

        // Get all the messageTokenList where received in DEFAULT_RECEIVED or UPDATED_RECEIVED
        defaultMessageTokenShouldBeFound("received.in=" + DEFAULT_RECEIVED + "," + UPDATED_RECEIVED);

        // Get all the messageTokenList where received equals to UPDATED_RECEIVED
        defaultMessageTokenShouldNotBeFound("received.in=" + UPDATED_RECEIVED);
    }

    @Test
    @Transactional
    void getAllMessageTokensByReceivedIsNullOrNotNull() throws Exception {
        // Initialize the database
        messageTokenRepository.saveAndFlush(messageToken);

        // Get all the messageTokenList where received is not null
        defaultMessageTokenShouldBeFound("received.specified=true");

        // Get all the messageTokenList where received is null
        defaultMessageTokenShouldNotBeFound("received.specified=false");
    }

    @Test
    @Transactional
    void getAllMessageTokensByActionedIsEqualToSomething() throws Exception {
        // Initialize the database
        messageTokenRepository.saveAndFlush(messageToken);

        // Get all the messageTokenList where actioned equals to DEFAULT_ACTIONED
        defaultMessageTokenShouldBeFound("actioned.equals=" + DEFAULT_ACTIONED);

        // Get all the messageTokenList where actioned equals to UPDATED_ACTIONED
        defaultMessageTokenShouldNotBeFound("actioned.equals=" + UPDATED_ACTIONED);
    }

    @Test
    @Transactional
    void getAllMessageTokensByActionedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        messageTokenRepository.saveAndFlush(messageToken);

        // Get all the messageTokenList where actioned not equals to DEFAULT_ACTIONED
        defaultMessageTokenShouldNotBeFound("actioned.notEquals=" + DEFAULT_ACTIONED);

        // Get all the messageTokenList where actioned not equals to UPDATED_ACTIONED
        defaultMessageTokenShouldBeFound("actioned.notEquals=" + UPDATED_ACTIONED);
    }

    @Test
    @Transactional
    void getAllMessageTokensByActionedIsInShouldWork() throws Exception {
        // Initialize the database
        messageTokenRepository.saveAndFlush(messageToken);

        // Get all the messageTokenList where actioned in DEFAULT_ACTIONED or UPDATED_ACTIONED
        defaultMessageTokenShouldBeFound("actioned.in=" + DEFAULT_ACTIONED + "," + UPDATED_ACTIONED);

        // Get all the messageTokenList where actioned equals to UPDATED_ACTIONED
        defaultMessageTokenShouldNotBeFound("actioned.in=" + UPDATED_ACTIONED);
    }

    @Test
    @Transactional
    void getAllMessageTokensByActionedIsNullOrNotNull() throws Exception {
        // Initialize the database
        messageTokenRepository.saveAndFlush(messageToken);

        // Get all the messageTokenList where actioned is not null
        defaultMessageTokenShouldBeFound("actioned.specified=true");

        // Get all the messageTokenList where actioned is null
        defaultMessageTokenShouldNotBeFound("actioned.specified=false");
    }

    @Test
    @Transactional
    void getAllMessageTokensByContentFullyEnqueuedIsEqualToSomething() throws Exception {
        // Initialize the database
        messageTokenRepository.saveAndFlush(messageToken);

        // Get all the messageTokenList where contentFullyEnqueued equals to DEFAULT_CONTENT_FULLY_ENQUEUED
        defaultMessageTokenShouldBeFound("contentFullyEnqueued.equals=" + DEFAULT_CONTENT_FULLY_ENQUEUED);

        // Get all the messageTokenList where contentFullyEnqueued equals to UPDATED_CONTENT_FULLY_ENQUEUED
        defaultMessageTokenShouldNotBeFound("contentFullyEnqueued.equals=" + UPDATED_CONTENT_FULLY_ENQUEUED);
    }

    @Test
    @Transactional
    void getAllMessageTokensByContentFullyEnqueuedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        messageTokenRepository.saveAndFlush(messageToken);

        // Get all the messageTokenList where contentFullyEnqueued not equals to DEFAULT_CONTENT_FULLY_ENQUEUED
        defaultMessageTokenShouldNotBeFound("contentFullyEnqueued.notEquals=" + DEFAULT_CONTENT_FULLY_ENQUEUED);

        // Get all the messageTokenList where contentFullyEnqueued not equals to UPDATED_CONTENT_FULLY_ENQUEUED
        defaultMessageTokenShouldBeFound("contentFullyEnqueued.notEquals=" + UPDATED_CONTENT_FULLY_ENQUEUED);
    }

    @Test
    @Transactional
    void getAllMessageTokensByContentFullyEnqueuedIsInShouldWork() throws Exception {
        // Initialize the database
        messageTokenRepository.saveAndFlush(messageToken);

        // Get all the messageTokenList where contentFullyEnqueued in DEFAULT_CONTENT_FULLY_ENQUEUED or UPDATED_CONTENT_FULLY_ENQUEUED
        defaultMessageTokenShouldBeFound(
            "contentFullyEnqueued.in=" + DEFAULT_CONTENT_FULLY_ENQUEUED + "," + UPDATED_CONTENT_FULLY_ENQUEUED
        );

        // Get all the messageTokenList where contentFullyEnqueued equals to UPDATED_CONTENT_FULLY_ENQUEUED
        defaultMessageTokenShouldNotBeFound("contentFullyEnqueued.in=" + UPDATED_CONTENT_FULLY_ENQUEUED);
    }

    @Test
    @Transactional
    void getAllMessageTokensByContentFullyEnqueuedIsNullOrNotNull() throws Exception {
        // Initialize the database
        messageTokenRepository.saveAndFlush(messageToken);

        // Get all the messageTokenList where contentFullyEnqueued is not null
        defaultMessageTokenShouldBeFound("contentFullyEnqueued.specified=true");

        // Get all the messageTokenList where contentFullyEnqueued is null
        defaultMessageTokenShouldNotBeFound("contentFullyEnqueued.specified=false");
    }

    @Test
    @Transactional
    void getAllMessageTokensByPlaceholderIsEqualToSomething() throws Exception {
        // Initialize the database
        messageTokenRepository.saveAndFlush(messageToken);
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
        messageToken.addPlaceholder(placeholder);
        messageTokenRepository.saveAndFlush(messageToken);
        Long placeholderId = placeholder.getId();

        // Get all the messageTokenList where placeholder equals to placeholderId
        defaultMessageTokenShouldBeFound("placeholderId.equals=" + placeholderId);

        // Get all the messageTokenList where placeholder equals to (placeholderId + 1)
        defaultMessageTokenShouldNotBeFound("placeholderId.equals=" + (placeholderId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultMessageTokenShouldBeFound(String filter) throws Exception {
        restMessageTokenMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(messageToken.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].timeSent").value(hasItem(DEFAULT_TIME_SENT.intValue())))
            .andExpect(jsonPath("$.[*].tokenValue").value(hasItem(DEFAULT_TOKEN_VALUE)))
            .andExpect(jsonPath("$.[*].received").value(hasItem(DEFAULT_RECEIVED.booleanValue())))
            .andExpect(jsonPath("$.[*].actioned").value(hasItem(DEFAULT_ACTIONED.booleanValue())))
            .andExpect(jsonPath("$.[*].contentFullyEnqueued").value(hasItem(DEFAULT_CONTENT_FULLY_ENQUEUED.booleanValue())));

        // Check, that the count call also returns 1
        restMessageTokenMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultMessageTokenShouldNotBeFound(String filter) throws Exception {
        restMessageTokenMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restMessageTokenMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingMessageToken() throws Exception {
        // Get the messageToken
        restMessageTokenMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewMessageToken() throws Exception {
        // Initialize the database
        messageTokenRepository.saveAndFlush(messageToken);

        int databaseSizeBeforeUpdate = messageTokenRepository.findAll().size();

        // Update the messageToken
        MessageToken updatedMessageToken = messageTokenRepository.findById(messageToken.getId()).get();
        // Disconnect from session so that the updates on updatedMessageToken are not directly saved in db
        em.detach(updatedMessageToken);
        updatedMessageToken
            .description(UPDATED_DESCRIPTION)
            .timeSent(UPDATED_TIME_SENT)
            .tokenValue(UPDATED_TOKEN_VALUE)
            .received(UPDATED_RECEIVED)
            .actioned(UPDATED_ACTIONED)
            .contentFullyEnqueued(UPDATED_CONTENT_FULLY_ENQUEUED);
        MessageTokenDTO messageTokenDTO = messageTokenMapper.toDto(updatedMessageToken);

        restMessageTokenMockMvc
            .perform(
                put(ENTITY_API_URL_ID, messageTokenDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(messageTokenDTO))
            )
            .andExpect(status().isOk());

        // Validate the MessageToken in the database
        List<MessageToken> messageTokenList = messageTokenRepository.findAll();
        assertThat(messageTokenList).hasSize(databaseSizeBeforeUpdate);
        MessageToken testMessageToken = messageTokenList.get(messageTokenList.size() - 1);
        assertThat(testMessageToken.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testMessageToken.getTimeSent()).isEqualTo(UPDATED_TIME_SENT);
        assertThat(testMessageToken.getTokenValue()).isEqualTo(UPDATED_TOKEN_VALUE);
        assertThat(testMessageToken.getReceived()).isEqualTo(UPDATED_RECEIVED);
        assertThat(testMessageToken.getActioned()).isEqualTo(UPDATED_ACTIONED);
        assertThat(testMessageToken.getContentFullyEnqueued()).isEqualTo(UPDATED_CONTENT_FULLY_ENQUEUED);

        // Validate the MessageToken in Elasticsearch
        verify(mockMessageTokenSearchRepository).save(testMessageToken);
    }

    @Test
    @Transactional
    void putNonExistingMessageToken() throws Exception {
        int databaseSizeBeforeUpdate = messageTokenRepository.findAll().size();
        messageToken.setId(count.incrementAndGet());

        // Create the MessageToken
        MessageTokenDTO messageTokenDTO = messageTokenMapper.toDto(messageToken);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMessageTokenMockMvc
            .perform(
                put(ENTITY_API_URL_ID, messageTokenDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(messageTokenDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MessageToken in the database
        List<MessageToken> messageTokenList = messageTokenRepository.findAll();
        assertThat(messageTokenList).hasSize(databaseSizeBeforeUpdate);

        // Validate the MessageToken in Elasticsearch
        verify(mockMessageTokenSearchRepository, times(0)).save(messageToken);
    }

    @Test
    @Transactional
    void putWithIdMismatchMessageToken() throws Exception {
        int databaseSizeBeforeUpdate = messageTokenRepository.findAll().size();
        messageToken.setId(count.incrementAndGet());

        // Create the MessageToken
        MessageTokenDTO messageTokenDTO = messageTokenMapper.toDto(messageToken);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMessageTokenMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(messageTokenDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MessageToken in the database
        List<MessageToken> messageTokenList = messageTokenRepository.findAll();
        assertThat(messageTokenList).hasSize(databaseSizeBeforeUpdate);

        // Validate the MessageToken in Elasticsearch
        verify(mockMessageTokenSearchRepository, times(0)).save(messageToken);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMessageToken() throws Exception {
        int databaseSizeBeforeUpdate = messageTokenRepository.findAll().size();
        messageToken.setId(count.incrementAndGet());

        // Create the MessageToken
        MessageTokenDTO messageTokenDTO = messageTokenMapper.toDto(messageToken);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMessageTokenMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(messageTokenDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the MessageToken in the database
        List<MessageToken> messageTokenList = messageTokenRepository.findAll();
        assertThat(messageTokenList).hasSize(databaseSizeBeforeUpdate);

        // Validate the MessageToken in Elasticsearch
        verify(mockMessageTokenSearchRepository, times(0)).save(messageToken);
    }

    @Test
    @Transactional
    void partialUpdateMessageTokenWithPatch() throws Exception {
        // Initialize the database
        messageTokenRepository.saveAndFlush(messageToken);

        int databaseSizeBeforeUpdate = messageTokenRepository.findAll().size();

        // Update the messageToken using partial update
        MessageToken partialUpdatedMessageToken = new MessageToken();
        partialUpdatedMessageToken.setId(messageToken.getId());

        partialUpdatedMessageToken.description(UPDATED_DESCRIPTION);

        restMessageTokenMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMessageToken.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMessageToken))
            )
            .andExpect(status().isOk());

        // Validate the MessageToken in the database
        List<MessageToken> messageTokenList = messageTokenRepository.findAll();
        assertThat(messageTokenList).hasSize(databaseSizeBeforeUpdate);
        MessageToken testMessageToken = messageTokenList.get(messageTokenList.size() - 1);
        assertThat(testMessageToken.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testMessageToken.getTimeSent()).isEqualTo(DEFAULT_TIME_SENT);
        assertThat(testMessageToken.getTokenValue()).isEqualTo(DEFAULT_TOKEN_VALUE);
        assertThat(testMessageToken.getReceived()).isEqualTo(DEFAULT_RECEIVED);
        assertThat(testMessageToken.getActioned()).isEqualTo(DEFAULT_ACTIONED);
        assertThat(testMessageToken.getContentFullyEnqueued()).isEqualTo(DEFAULT_CONTENT_FULLY_ENQUEUED);
    }

    @Test
    @Transactional
    void fullUpdateMessageTokenWithPatch() throws Exception {
        // Initialize the database
        messageTokenRepository.saveAndFlush(messageToken);

        int databaseSizeBeforeUpdate = messageTokenRepository.findAll().size();

        // Update the messageToken using partial update
        MessageToken partialUpdatedMessageToken = new MessageToken();
        partialUpdatedMessageToken.setId(messageToken.getId());

        partialUpdatedMessageToken
            .description(UPDATED_DESCRIPTION)
            .timeSent(UPDATED_TIME_SENT)
            .tokenValue(UPDATED_TOKEN_VALUE)
            .received(UPDATED_RECEIVED)
            .actioned(UPDATED_ACTIONED)
            .contentFullyEnqueued(UPDATED_CONTENT_FULLY_ENQUEUED);

        restMessageTokenMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMessageToken.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMessageToken))
            )
            .andExpect(status().isOk());

        // Validate the MessageToken in the database
        List<MessageToken> messageTokenList = messageTokenRepository.findAll();
        assertThat(messageTokenList).hasSize(databaseSizeBeforeUpdate);
        MessageToken testMessageToken = messageTokenList.get(messageTokenList.size() - 1);
        assertThat(testMessageToken.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testMessageToken.getTimeSent()).isEqualTo(UPDATED_TIME_SENT);
        assertThat(testMessageToken.getTokenValue()).isEqualTo(UPDATED_TOKEN_VALUE);
        assertThat(testMessageToken.getReceived()).isEqualTo(UPDATED_RECEIVED);
        assertThat(testMessageToken.getActioned()).isEqualTo(UPDATED_ACTIONED);
        assertThat(testMessageToken.getContentFullyEnqueued()).isEqualTo(UPDATED_CONTENT_FULLY_ENQUEUED);
    }

    @Test
    @Transactional
    void patchNonExistingMessageToken() throws Exception {
        int databaseSizeBeforeUpdate = messageTokenRepository.findAll().size();
        messageToken.setId(count.incrementAndGet());

        // Create the MessageToken
        MessageTokenDTO messageTokenDTO = messageTokenMapper.toDto(messageToken);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMessageTokenMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, messageTokenDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(messageTokenDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MessageToken in the database
        List<MessageToken> messageTokenList = messageTokenRepository.findAll();
        assertThat(messageTokenList).hasSize(databaseSizeBeforeUpdate);

        // Validate the MessageToken in Elasticsearch
        verify(mockMessageTokenSearchRepository, times(0)).save(messageToken);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMessageToken() throws Exception {
        int databaseSizeBeforeUpdate = messageTokenRepository.findAll().size();
        messageToken.setId(count.incrementAndGet());

        // Create the MessageToken
        MessageTokenDTO messageTokenDTO = messageTokenMapper.toDto(messageToken);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMessageTokenMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(messageTokenDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MessageToken in the database
        List<MessageToken> messageTokenList = messageTokenRepository.findAll();
        assertThat(messageTokenList).hasSize(databaseSizeBeforeUpdate);

        // Validate the MessageToken in Elasticsearch
        verify(mockMessageTokenSearchRepository, times(0)).save(messageToken);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMessageToken() throws Exception {
        int databaseSizeBeforeUpdate = messageTokenRepository.findAll().size();
        messageToken.setId(count.incrementAndGet());

        // Create the MessageToken
        MessageTokenDTO messageTokenDTO = messageTokenMapper.toDto(messageToken);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMessageTokenMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(messageTokenDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the MessageToken in the database
        List<MessageToken> messageTokenList = messageTokenRepository.findAll();
        assertThat(messageTokenList).hasSize(databaseSizeBeforeUpdate);

        // Validate the MessageToken in Elasticsearch
        verify(mockMessageTokenSearchRepository, times(0)).save(messageToken);
    }

    @Test
    @Transactional
    void deleteMessageToken() throws Exception {
        // Initialize the database
        messageTokenRepository.saveAndFlush(messageToken);

        int databaseSizeBeforeDelete = messageTokenRepository.findAll().size();

        // Delete the messageToken
        restMessageTokenMockMvc
            .perform(delete(ENTITY_API_URL_ID, messageToken.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<MessageToken> messageTokenList = messageTokenRepository.findAll();
        assertThat(messageTokenList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the MessageToken in Elasticsearch
        verify(mockMessageTokenSearchRepository, times(1)).deleteById(messageToken.getId());
    }

    @Test
    @Transactional
    void searchMessageToken() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        messageTokenRepository.saveAndFlush(messageToken);
        when(mockMessageTokenSearchRepository.search("id:" + messageToken.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(messageToken), PageRequest.of(0, 1), 1));

        // Search the messageToken
        restMessageTokenMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + messageToken.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(messageToken.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].timeSent").value(hasItem(DEFAULT_TIME_SENT.intValue())))
            .andExpect(jsonPath("$.[*].tokenValue").value(hasItem(DEFAULT_TOKEN_VALUE)))
            .andExpect(jsonPath("$.[*].received").value(hasItem(DEFAULT_RECEIVED.booleanValue())))
            .andExpect(jsonPath("$.[*].actioned").value(hasItem(DEFAULT_ACTIONED.booleanValue())))
            .andExpect(jsonPath("$.[*].contentFullyEnqueued").value(hasItem(DEFAULT_CONTENT_FULLY_ENQUEUED.booleanValue())));
    }
}
