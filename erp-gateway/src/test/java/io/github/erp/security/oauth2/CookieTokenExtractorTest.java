package io.github.erp.security.oauth2;

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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.OAuth2AccessToken;

/**
 * Test whether the {@link CookieTokenExtractor} can properly extract access tokens from
 * Cookies and Headers.
 */
public class CookieTokenExtractorTest {
    private CookieTokenExtractor cookieTokenExtractor;

    @BeforeEach
    public void init() {
        cookieTokenExtractor = new CookieTokenExtractor();
    }

    @Test
    public void testExtractTokenCookie() {
        MockHttpServletRequest request = OAuth2AuthenticationServiceTest.createMockHttpServletRequest();
        Authentication authentication = cookieTokenExtractor.extract(request);
        Assertions.assertEquals(OAuth2AuthenticationServiceTest.ACCESS_TOKEN_VALUE, authentication.getPrincipal().toString());
    }

    @Test
    public void testExtractTokenHeader() {
        MockHttpServletRequest request = new MockHttpServletRequest(HttpMethod.GET.name(), "http://www.test.com");
        request.addHeader("Authorization", OAuth2AccessToken.BEARER_TYPE + " " + OAuth2AuthenticationServiceTest.ACCESS_TOKEN_VALUE);
        Authentication authentication = cookieTokenExtractor.extract(request);
        Assertions.assertEquals(OAuth2AuthenticationServiceTest.ACCESS_TOKEN_VALUE, authentication.getPrincipal().toString());
    }

    @Test
    public void testExtractTokenParam() {
        MockHttpServletRequest request = new MockHttpServletRequest(HttpMethod.GET.name(), "http://www.test.com");
        request.addParameter(OAuth2AccessToken.ACCESS_TOKEN, OAuth2AuthenticationServiceTest.ACCESS_TOKEN_VALUE);
        Authentication authentication = cookieTokenExtractor.extract(request);
        Assertions.assertEquals(OAuth2AuthenticationServiceTest.ACCESS_TOKEN_VALUE, authentication.getPrincipal().toString());
    }
}