package io.github.erp.web.rest;

/*-
 * Copyright © 2021 Edwin Njeru (mailnjeru@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import io.github.erp.ErpServiceApp;
import io.github.erp.config.SecurityBeanOverrideConfiguration;
import io.github.erp.domain.MessageToken;
import io.github.erp.repository.MessageTokenRepository;
import io.github.erp.repository.search.MessageTokenSearchRepository;
import io.github.erp.service.MessageTokenService;
import io.github.erp.service.dto.MessageTokenDTO;
import io.github.erp.service.mapper.MessageTokenMapper;
import io.github.erp.service.dto.MessageTokenCriteria;
import io.github.erp.service.MessageTokenQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link MessageTokenResource} REST controller.
 */
@SpringBootTest(classes = { SecurityBeanOverrideConfiguration.class, ErpServiceApp.class })
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
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

    @Autowired
    private MessageTokenRepository messageTokenRepository;

    @Autowired
    private MessageTokenMapper messageTokenMapper;

    @Autowired
    private MessageTokenService messageTokenService;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.MessageTokenSearchRepositoryMockConfiguration
     */
    @Autowired
    private MessageTokenSearchRepository mockMessageTokenSearchRepository;

    @Autowired
    private MessageTokenQueryService messageTokenQueryService;

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
    public void createMessageToken() throws Exception {
        int databaseSizeBeforeCreate = messageTokenRepository.findAll().size();
        // Create the MessageToken
        MessageTokenDTO messageTokenDTO = messageTokenMapper.toDto(messageToken);
        restMessageTokenMockMvc.perform(post("/api/message-tokens").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(messageTokenDTO)))
            .andExpect(status().isCreated());

        // Validate the MessageToken in the database
        List<MessageToken> messageTokenList = messageTokenRepository.findAll();
        assertThat(messageTokenList).hasSize(databaseSizeBeforeCreate + 1);
        MessageToken testMessageToken = messageTokenList.get(messageTokenList.size() - 1);
        assertThat(testMessageToken.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testMessageToken.getTimeSent()).isEqualTo(DEFAULT_TIME_SENT);
        assertThat(testMessageToken.getTokenValue()).isEqualTo(DEFAULT_TOKEN_VALUE);
        assertThat(testMessageToken.isReceived()).isEqualTo(DEFAULT_RECEIVED);
        assertThat(testMessageToken.isActioned()).isEqualTo(DEFAULT_ACTIONED);
        assertThat(testMessageToken.isContentFullyEnqueued()).isEqualTo(DEFAULT_CONTENT_FULLY_ENQUEUED);

        // Validate the MessageToken in Elasticsearch
        verify(mockMessageTokenSearchRepository, times(1)).save(testMessageToken);
    }

    @Test
    @Transactional
    public void createMessageTokenWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = messageTokenRepository.findAll().size();

        // Create the MessageToken with an existing ID
        messageToken.setId(1L);
        MessageTokenDTO messageTokenDTO = messageTokenMapper.toDto(messageToken);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMessageTokenMockMvc.perform(post("/api/message-tokens").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(messageTokenDTO)))
            .andExpect(status().isBadRequest());

        // Validate the MessageToken in the database
        List<MessageToken> messageTokenList = messageTokenRepository.findAll();
        assertThat(messageTokenList).hasSize(databaseSizeBeforeCreate);

        // Validate the MessageToken in Elasticsearch
        verify(mockMessageTokenSearchRepository, times(0)).save(messageToken);
    }


    @Test
    @Transactional
    public void checkTimeSentIsRequired() throws Exception {
        int databaseSizeBeforeTest = messageTokenRepository.findAll().size();
        // set the field null
        messageToken.setTimeSent(null);

        // Create the MessageToken, which fails.
        MessageTokenDTO messageTokenDTO = messageTokenMapper.toDto(messageToken);


        restMessageTokenMockMvc.perform(post("/api/message-tokens").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(messageTokenDTO)))
            .andExpect(status().isBadRequest());

        List<MessageToken> messageTokenList = messageTokenRepository.findAll();
        assertThat(messageTokenList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTokenValueIsRequired() throws Exception {
        int databaseSizeBeforeTest = messageTokenRepository.findAll().size();
        // set the field null
        messageToken.setTokenValue(null);

        // Create the MessageToken, which fails.
        MessageTokenDTO messageTokenDTO = messageTokenMapper.toDto(messageToken);


        restMessageTokenMockMvc.perform(post("/api/message-tokens").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(messageTokenDTO)))
            .andExpect(status().isBadRequest());

        List<MessageToken> messageTokenList = messageTokenRepository.findAll();
        assertThat(messageTokenList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllMessageTokens() throws Exception {
        // Initialize the database
        messageTokenRepository.saveAndFlush(messageToken);

        // Get all the messageTokenList
        restMessageTokenMockMvc.perform(get("/api/message-tokens?sort=id,desc"))
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
    
    @Test
    @Transactional
    public void getMessageToken() throws Exception {
        // Initialize the database
        messageTokenRepository.saveAndFlush(messageToken);

        // Get the messageToken
        restMessageTokenMockMvc.perform(get("/api/message-tokens/{id}", messageToken.getId()))
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
    public void getMessageTokensByIdFiltering() throws Exception {
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
    public void getAllMessageTokensByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        messageTokenRepository.saveAndFlush(messageToken);

        // Get all the messageTokenList where description equals to DEFAULT_DESCRIPTION
        defaultMessageTokenShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the messageTokenList where description equals to UPDATED_DESCRIPTION
        defaultMessageTokenShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllMessageTokensByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        messageTokenRepository.saveAndFlush(messageToken);

        // Get all the messageTokenList where description not equals to DEFAULT_DESCRIPTION
        defaultMessageTokenShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the messageTokenList where description not equals to UPDATED_DESCRIPTION
        defaultMessageTokenShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllMessageTokensByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        messageTokenRepository.saveAndFlush(messageToken);

        // Get all the messageTokenList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultMessageTokenShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the messageTokenList where description equals to UPDATED_DESCRIPTION
        defaultMessageTokenShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllMessageTokensByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        messageTokenRepository.saveAndFlush(messageToken);

        // Get all the messageTokenList where description is not null
        defaultMessageTokenShouldBeFound("description.specified=true");

        // Get all the messageTokenList where description is null
        defaultMessageTokenShouldNotBeFound("description.specified=false");
    }
                @Test
    @Transactional
    public void getAllMessageTokensByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        messageTokenRepository.saveAndFlush(messageToken);

        // Get all the messageTokenList where description contains DEFAULT_DESCRIPTION
        defaultMessageTokenShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the messageTokenList where description contains UPDATED_DESCRIPTION
        defaultMessageTokenShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllMessageTokensByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        messageTokenRepository.saveAndFlush(messageToken);

        // Get all the messageTokenList where description does not contain DEFAULT_DESCRIPTION
        defaultMessageTokenShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the messageTokenList where description does not contain UPDATED_DESCRIPTION
        defaultMessageTokenShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }


    @Test
    @Transactional
    public void getAllMessageTokensByTimeSentIsEqualToSomething() throws Exception {
        // Initialize the database
        messageTokenRepository.saveAndFlush(messageToken);

        // Get all the messageTokenList where timeSent equals to DEFAULT_TIME_SENT
        defaultMessageTokenShouldBeFound("timeSent.equals=" + DEFAULT_TIME_SENT);

        // Get all the messageTokenList where timeSent equals to UPDATED_TIME_SENT
        defaultMessageTokenShouldNotBeFound("timeSent.equals=" + UPDATED_TIME_SENT);
    }

    @Test
    @Transactional
    public void getAllMessageTokensByTimeSentIsNotEqualToSomething() throws Exception {
        // Initialize the database
        messageTokenRepository.saveAndFlush(messageToken);

        // Get all the messageTokenList where timeSent not equals to DEFAULT_TIME_SENT
        defaultMessageTokenShouldNotBeFound("timeSent.notEquals=" + DEFAULT_TIME_SENT);

        // Get all the messageTokenList where timeSent not equals to UPDATED_TIME_SENT
        defaultMessageTokenShouldBeFound("timeSent.notEquals=" + UPDATED_TIME_SENT);
    }

    @Test
    @Transactional
    public void getAllMessageTokensByTimeSentIsInShouldWork() throws Exception {
        // Initialize the database
        messageTokenRepository.saveAndFlush(messageToken);

        // Get all the messageTokenList where timeSent in DEFAULT_TIME_SENT or UPDATED_TIME_SENT
        defaultMessageTokenShouldBeFound("timeSent.in=" + DEFAULT_TIME_SENT + "," + UPDATED_TIME_SENT);

        // Get all the messageTokenList where timeSent equals to UPDATED_TIME_SENT
        defaultMessageTokenShouldNotBeFound("timeSent.in=" + UPDATED_TIME_SENT);
    }

    @Test
    @Transactional
    public void getAllMessageTokensByTimeSentIsNullOrNotNull() throws Exception {
        // Initialize the database
        messageTokenRepository.saveAndFlush(messageToken);

        // Get all the messageTokenList where timeSent is not null
        defaultMessageTokenShouldBeFound("timeSent.specified=true");

        // Get all the messageTokenList where timeSent is null
        defaultMessageTokenShouldNotBeFound("timeSent.specified=false");
    }

    @Test
    @Transactional
    public void getAllMessageTokensByTimeSentIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        messageTokenRepository.saveAndFlush(messageToken);

        // Get all the messageTokenList where timeSent is greater than or equal to DEFAULT_TIME_SENT
        defaultMessageTokenShouldBeFound("timeSent.greaterThanOrEqual=" + DEFAULT_TIME_SENT);

        // Get all the messageTokenList where timeSent is greater than or equal to UPDATED_TIME_SENT
        defaultMessageTokenShouldNotBeFound("timeSent.greaterThanOrEqual=" + UPDATED_TIME_SENT);
    }

    @Test
    @Transactional
    public void getAllMessageTokensByTimeSentIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        messageTokenRepository.saveAndFlush(messageToken);

        // Get all the messageTokenList where timeSent is less than or equal to DEFAULT_TIME_SENT
        defaultMessageTokenShouldBeFound("timeSent.lessThanOrEqual=" + DEFAULT_TIME_SENT);

        // Get all the messageTokenList where timeSent is less than or equal to SMALLER_TIME_SENT
        defaultMessageTokenShouldNotBeFound("timeSent.lessThanOrEqual=" + SMALLER_TIME_SENT);
    }

    @Test
    @Transactional
    public void getAllMessageTokensByTimeSentIsLessThanSomething() throws Exception {
        // Initialize the database
        messageTokenRepository.saveAndFlush(messageToken);

        // Get all the messageTokenList where timeSent is less than DEFAULT_TIME_SENT
        defaultMessageTokenShouldNotBeFound("timeSent.lessThan=" + DEFAULT_TIME_SENT);

        // Get all the messageTokenList where timeSent is less than UPDATED_TIME_SENT
        defaultMessageTokenShouldBeFound("timeSent.lessThan=" + UPDATED_TIME_SENT);
    }

    @Test
    @Transactional
    public void getAllMessageTokensByTimeSentIsGreaterThanSomething() throws Exception {
        // Initialize the database
        messageTokenRepository.saveAndFlush(messageToken);

        // Get all the messageTokenList where timeSent is greater than DEFAULT_TIME_SENT
        defaultMessageTokenShouldNotBeFound("timeSent.greaterThan=" + DEFAULT_TIME_SENT);

        // Get all the messageTokenList where timeSent is greater than SMALLER_TIME_SENT
        defaultMessageTokenShouldBeFound("timeSent.greaterThan=" + SMALLER_TIME_SENT);
    }


    @Test
    @Transactional
    public void getAllMessageTokensByTokenValueIsEqualToSomething() throws Exception {
        // Initialize the database
        messageTokenRepository.saveAndFlush(messageToken);

        // Get all the messageTokenList where tokenValue equals to DEFAULT_TOKEN_VALUE
        defaultMessageTokenShouldBeFound("tokenValue.equals=" + DEFAULT_TOKEN_VALUE);

        // Get all the messageTokenList where tokenValue equals to UPDATED_TOKEN_VALUE
        defaultMessageTokenShouldNotBeFound("tokenValue.equals=" + UPDATED_TOKEN_VALUE);
    }

    @Test
    @Transactional
    public void getAllMessageTokensByTokenValueIsNotEqualToSomething() throws Exception {
        // Initialize the database
        messageTokenRepository.saveAndFlush(messageToken);

        // Get all the messageTokenList where tokenValue not equals to DEFAULT_TOKEN_VALUE
        defaultMessageTokenShouldNotBeFound("tokenValue.notEquals=" + DEFAULT_TOKEN_VALUE);

        // Get all the messageTokenList where tokenValue not equals to UPDATED_TOKEN_VALUE
        defaultMessageTokenShouldBeFound("tokenValue.notEquals=" + UPDATED_TOKEN_VALUE);
    }

    @Test
    @Transactional
    public void getAllMessageTokensByTokenValueIsInShouldWork() throws Exception {
        // Initialize the database
        messageTokenRepository.saveAndFlush(messageToken);

        // Get all the messageTokenList where tokenValue in DEFAULT_TOKEN_VALUE or UPDATED_TOKEN_VALUE
        defaultMessageTokenShouldBeFound("tokenValue.in=" + DEFAULT_TOKEN_VALUE + "," + UPDATED_TOKEN_VALUE);

        // Get all the messageTokenList where tokenValue equals to UPDATED_TOKEN_VALUE
        defaultMessageTokenShouldNotBeFound("tokenValue.in=" + UPDATED_TOKEN_VALUE);
    }

    @Test
    @Transactional
    public void getAllMessageTokensByTokenValueIsNullOrNotNull() throws Exception {
        // Initialize the database
        messageTokenRepository.saveAndFlush(messageToken);

        // Get all the messageTokenList where tokenValue is not null
        defaultMessageTokenShouldBeFound("tokenValue.specified=true");

        // Get all the messageTokenList where tokenValue is null
        defaultMessageTokenShouldNotBeFound("tokenValue.specified=false");
    }
                @Test
    @Transactional
    public void getAllMessageTokensByTokenValueContainsSomething() throws Exception {
        // Initialize the database
        messageTokenRepository.saveAndFlush(messageToken);

        // Get all the messageTokenList where tokenValue contains DEFAULT_TOKEN_VALUE
        defaultMessageTokenShouldBeFound("tokenValue.contains=" + DEFAULT_TOKEN_VALUE);

        // Get all the messageTokenList where tokenValue contains UPDATED_TOKEN_VALUE
        defaultMessageTokenShouldNotBeFound("tokenValue.contains=" + UPDATED_TOKEN_VALUE);
    }

    @Test
    @Transactional
    public void getAllMessageTokensByTokenValueNotContainsSomething() throws Exception {
        // Initialize the database
        messageTokenRepository.saveAndFlush(messageToken);

        // Get all the messageTokenList where tokenValue does not contain DEFAULT_TOKEN_VALUE
        defaultMessageTokenShouldNotBeFound("tokenValue.doesNotContain=" + DEFAULT_TOKEN_VALUE);

        // Get all the messageTokenList where tokenValue does not contain UPDATED_TOKEN_VALUE
        defaultMessageTokenShouldBeFound("tokenValue.doesNotContain=" + UPDATED_TOKEN_VALUE);
    }


    @Test
    @Transactional
    public void getAllMessageTokensByReceivedIsEqualToSomething() throws Exception {
        // Initialize the database
        messageTokenRepository.saveAndFlush(messageToken);

        // Get all the messageTokenList where received equals to DEFAULT_RECEIVED
        defaultMessageTokenShouldBeFound("received.equals=" + DEFAULT_RECEIVED);

        // Get all the messageTokenList where received equals to UPDATED_RECEIVED
        defaultMessageTokenShouldNotBeFound("received.equals=" + UPDATED_RECEIVED);
    }

    @Test
    @Transactional
    public void getAllMessageTokensByReceivedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        messageTokenRepository.saveAndFlush(messageToken);

        // Get all the messageTokenList where received not equals to DEFAULT_RECEIVED
        defaultMessageTokenShouldNotBeFound("received.notEquals=" + DEFAULT_RECEIVED);

        // Get all the messageTokenList where received not equals to UPDATED_RECEIVED
        defaultMessageTokenShouldBeFound("received.notEquals=" + UPDATED_RECEIVED);
    }

    @Test
    @Transactional
    public void getAllMessageTokensByReceivedIsInShouldWork() throws Exception {
        // Initialize the database
        messageTokenRepository.saveAndFlush(messageToken);

        // Get all the messageTokenList where received in DEFAULT_RECEIVED or UPDATED_RECEIVED
        defaultMessageTokenShouldBeFound("received.in=" + DEFAULT_RECEIVED + "," + UPDATED_RECEIVED);

        // Get all the messageTokenList where received equals to UPDATED_RECEIVED
        defaultMessageTokenShouldNotBeFound("received.in=" + UPDATED_RECEIVED);
    }

    @Test
    @Transactional
    public void getAllMessageTokensByReceivedIsNullOrNotNull() throws Exception {
        // Initialize the database
        messageTokenRepository.saveAndFlush(messageToken);

        // Get all the messageTokenList where received is not null
        defaultMessageTokenShouldBeFound("received.specified=true");

        // Get all the messageTokenList where received is null
        defaultMessageTokenShouldNotBeFound("received.specified=false");
    }

    @Test
    @Transactional
    public void getAllMessageTokensByActionedIsEqualToSomething() throws Exception {
        // Initialize the database
        messageTokenRepository.saveAndFlush(messageToken);

        // Get all the messageTokenList where actioned equals to DEFAULT_ACTIONED
        defaultMessageTokenShouldBeFound("actioned.equals=" + DEFAULT_ACTIONED);

        // Get all the messageTokenList where actioned equals to UPDATED_ACTIONED
        defaultMessageTokenShouldNotBeFound("actioned.equals=" + UPDATED_ACTIONED);
    }

    @Test
    @Transactional
    public void getAllMessageTokensByActionedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        messageTokenRepository.saveAndFlush(messageToken);

        // Get all the messageTokenList where actioned not equals to DEFAULT_ACTIONED
        defaultMessageTokenShouldNotBeFound("actioned.notEquals=" + DEFAULT_ACTIONED);

        // Get all the messageTokenList where actioned not equals to UPDATED_ACTIONED
        defaultMessageTokenShouldBeFound("actioned.notEquals=" + UPDATED_ACTIONED);
    }

    @Test
    @Transactional
    public void getAllMessageTokensByActionedIsInShouldWork() throws Exception {
        // Initialize the database
        messageTokenRepository.saveAndFlush(messageToken);

        // Get all the messageTokenList where actioned in DEFAULT_ACTIONED or UPDATED_ACTIONED
        defaultMessageTokenShouldBeFound("actioned.in=" + DEFAULT_ACTIONED + "," + UPDATED_ACTIONED);

        // Get all the messageTokenList where actioned equals to UPDATED_ACTIONED
        defaultMessageTokenShouldNotBeFound("actioned.in=" + UPDATED_ACTIONED);
    }

    @Test
    @Transactional
    public void getAllMessageTokensByActionedIsNullOrNotNull() throws Exception {
        // Initialize the database
        messageTokenRepository.saveAndFlush(messageToken);

        // Get all the messageTokenList where actioned is not null
        defaultMessageTokenShouldBeFound("actioned.specified=true");

        // Get all the messageTokenList where actioned is null
        defaultMessageTokenShouldNotBeFound("actioned.specified=false");
    }

    @Test
    @Transactional
    public void getAllMessageTokensByContentFullyEnqueuedIsEqualToSomething() throws Exception {
        // Initialize the database
        messageTokenRepository.saveAndFlush(messageToken);

        // Get all the messageTokenList where contentFullyEnqueued equals to DEFAULT_CONTENT_FULLY_ENQUEUED
        defaultMessageTokenShouldBeFound("contentFullyEnqueued.equals=" + DEFAULT_CONTENT_FULLY_ENQUEUED);

        // Get all the messageTokenList where contentFullyEnqueued equals to UPDATED_CONTENT_FULLY_ENQUEUED
        defaultMessageTokenShouldNotBeFound("contentFullyEnqueued.equals=" + UPDATED_CONTENT_FULLY_ENQUEUED);
    }

    @Test
    @Transactional
    public void getAllMessageTokensByContentFullyEnqueuedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        messageTokenRepository.saveAndFlush(messageToken);

        // Get all the messageTokenList where contentFullyEnqueued not equals to DEFAULT_CONTENT_FULLY_ENQUEUED
        defaultMessageTokenShouldNotBeFound("contentFullyEnqueued.notEquals=" + DEFAULT_CONTENT_FULLY_ENQUEUED);

        // Get all the messageTokenList where contentFullyEnqueued not equals to UPDATED_CONTENT_FULLY_ENQUEUED
        defaultMessageTokenShouldBeFound("contentFullyEnqueued.notEquals=" + UPDATED_CONTENT_FULLY_ENQUEUED);
    }

    @Test
    @Transactional
    public void getAllMessageTokensByContentFullyEnqueuedIsInShouldWork() throws Exception {
        // Initialize the database
        messageTokenRepository.saveAndFlush(messageToken);

        // Get all the messageTokenList where contentFullyEnqueued in DEFAULT_CONTENT_FULLY_ENQUEUED or UPDATED_CONTENT_FULLY_ENQUEUED
        defaultMessageTokenShouldBeFound("contentFullyEnqueued.in=" + DEFAULT_CONTENT_FULLY_ENQUEUED + "," + UPDATED_CONTENT_FULLY_ENQUEUED);

        // Get all the messageTokenList where contentFullyEnqueued equals to UPDATED_CONTENT_FULLY_ENQUEUED
        defaultMessageTokenShouldNotBeFound("contentFullyEnqueued.in=" + UPDATED_CONTENT_FULLY_ENQUEUED);
    }

    @Test
    @Transactional
    public void getAllMessageTokensByContentFullyEnqueuedIsNullOrNotNull() throws Exception {
        // Initialize the database
        messageTokenRepository.saveAndFlush(messageToken);

        // Get all the messageTokenList where contentFullyEnqueued is not null
        defaultMessageTokenShouldBeFound("contentFullyEnqueued.specified=true");

        // Get all the messageTokenList where contentFullyEnqueued is null
        defaultMessageTokenShouldNotBeFound("contentFullyEnqueued.specified=false");
    }
    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultMessageTokenShouldBeFound(String filter) throws Exception {
        restMessageTokenMockMvc.perform(get("/api/message-tokens?sort=id,desc&" + filter))
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
        restMessageTokenMockMvc.perform(get("/api/message-tokens/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultMessageTokenShouldNotBeFound(String filter) throws Exception {
        restMessageTokenMockMvc.perform(get("/api/message-tokens?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restMessageTokenMockMvc.perform(get("/api/message-tokens/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingMessageToken() throws Exception {
        // Get the messageToken
        restMessageTokenMockMvc.perform(get("/api/message-tokens/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMessageToken() throws Exception {
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

        restMessageTokenMockMvc.perform(put("/api/message-tokens").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(messageTokenDTO)))
            .andExpect(status().isOk());

        // Validate the MessageToken in the database
        List<MessageToken> messageTokenList = messageTokenRepository.findAll();
        assertThat(messageTokenList).hasSize(databaseSizeBeforeUpdate);
        MessageToken testMessageToken = messageTokenList.get(messageTokenList.size() - 1);
        assertThat(testMessageToken.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testMessageToken.getTimeSent()).isEqualTo(UPDATED_TIME_SENT);
        assertThat(testMessageToken.getTokenValue()).isEqualTo(UPDATED_TOKEN_VALUE);
        assertThat(testMessageToken.isReceived()).isEqualTo(UPDATED_RECEIVED);
        assertThat(testMessageToken.isActioned()).isEqualTo(UPDATED_ACTIONED);
        assertThat(testMessageToken.isContentFullyEnqueued()).isEqualTo(UPDATED_CONTENT_FULLY_ENQUEUED);

        // Validate the MessageToken in Elasticsearch
        verify(mockMessageTokenSearchRepository, times(1)).save(testMessageToken);
    }

    @Test
    @Transactional
    public void updateNonExistingMessageToken() throws Exception {
        int databaseSizeBeforeUpdate = messageTokenRepository.findAll().size();

        // Create the MessageToken
        MessageTokenDTO messageTokenDTO = messageTokenMapper.toDto(messageToken);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMessageTokenMockMvc.perform(put("/api/message-tokens").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(messageTokenDTO)))
            .andExpect(status().isBadRequest());

        // Validate the MessageToken in the database
        List<MessageToken> messageTokenList = messageTokenRepository.findAll();
        assertThat(messageTokenList).hasSize(databaseSizeBeforeUpdate);

        // Validate the MessageToken in Elasticsearch
        verify(mockMessageTokenSearchRepository, times(0)).save(messageToken);
    }

    @Test
    @Transactional
    public void deleteMessageToken() throws Exception {
        // Initialize the database
        messageTokenRepository.saveAndFlush(messageToken);

        int databaseSizeBeforeDelete = messageTokenRepository.findAll().size();

        // Delete the messageToken
        restMessageTokenMockMvc.perform(delete("/api/message-tokens/{id}", messageToken.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<MessageToken> messageTokenList = messageTokenRepository.findAll();
        assertThat(messageTokenList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the MessageToken in Elasticsearch
        verify(mockMessageTokenSearchRepository, times(1)).deleteById(messageToken.getId());
    }

    @Test
    @Transactional
    public void searchMessageToken() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        messageTokenRepository.saveAndFlush(messageToken);
        when(mockMessageTokenSearchRepository.search(queryStringQuery("id:" + messageToken.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(messageToken), PageRequest.of(0, 1), 1));

        // Search the messageToken
        restMessageTokenMockMvc.perform(get("/api/_search/message-tokens?query=id:" + messageToken.getId()))
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
