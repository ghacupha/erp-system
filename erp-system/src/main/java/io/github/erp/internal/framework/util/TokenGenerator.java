package io.github.erp.internal.framework.util;

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
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Base64;

import static io.github.erp.internal.framework.AppConstants.TOKEN_BYTE_LENGTH;


/**
 * Used as token generator for messaging and also for generating message tokens
 */
@Component("tokenGenerator")
public class TokenGenerator {

    private static final ObjectMapper mapper = createObjectMapper();

    public String generateHexToken() {

        return generateHexToken(TOKEN_BYTE_LENGTH);
    }

    public String generateBase64Token() {

        return generateBase64Token(TOKEN_BYTE_LENGTH);
    }

    //generateRandomHexToken(16) -> 2189df7475e96aa3982dbeab266497cd
    public String generateHexToken(int byteLength) {
        SecureRandom secureRandom = new SecureRandom();
        byte[] token = new byte[byteLength];
        secureRandom.nextBytes(token);
        return new BigInteger(1, token).toString(16); //hex encoding
    }

    //generateRandomBase64Token(16) -> EEcCCAYuUcQk7IuzdaPzrg
    public String generateBase64Token(int byteLength) {
        SecureRandom secureRandom = new SecureRandom();
        byte[] token = new byte[byteLength];
        secureRandom.nextBytes(token);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(token); //base64 encoding
    }

    public String md5Digest(final Object object) throws JsonProcessingException {
        return DigestUtils.md5DigestAsHex(mapper.writeValueAsBytes(object));
    }

    private static ObjectMapper createObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        mapper.registerModule(new JavaTimeModule());
        return mapper;
    }

}
