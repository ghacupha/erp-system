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

import org.springframework.security.jwt.crypto.sign.SignatureVerifier;

/**
 * Abstracts how to create a {@link SignatureVerifier} to verify JWT tokens with a public key.
 * Implementations will have to contact the OAuth2 authorization server to fetch the public key
 * and use it to build a {@link SignatureVerifier} in a server specific way.
 *
 * @see UaaSignatureVerifierClient
 */
public interface OAuth2SignatureVerifierClient {
    /**
     * Returns the {@link SignatureVerifier} used to verify JWT tokens.
     * Fetches the public key from the Authorization server to create
     * this verifier.
     *
     * @return the new verifier used to verify JWT signatures.
     * Will be null if we cannot contact the token endpoint.
     * @throws Exception if we could not create a {@link SignatureVerifier} or contact the token endpoint.
     */
    SignatureVerifier getSignatureVerifier() throws Exception;
}
