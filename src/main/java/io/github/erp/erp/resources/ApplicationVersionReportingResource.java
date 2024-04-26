package io.github.erp.erp.resources;

/*-
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
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
import io.github.erp.internal.model.ApplicationStatus;
import io.github.erp.service.dto.ApplicationUserDTO;
import lombok.Data;
import lombok.Builder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.jhipster.web.util.ResponseUtil;

import java.util.Optional;

@RestController("applicationVersionReportingResource")
@RequestMapping("/api/app")
public class ApplicationVersionReportingResource {

    private static final Logger log = LoggerFactory.getLogger(ApplicationVersionReportingResource.class);

    private final String build;
    private final String version;
    // private final String profile;
    private final String branch;

    public ApplicationVersionReportingResource(
        @Value("${git.commit.id.abbrev:}") String build,
        @Value("${git.commit.id.describe:}") String version,
        // @Value("${spring.profiles.active}") String profile,
        @Value("${git.branch:}") String branch) {
        this.build = build;
        this.version = version;
        // this.profile = profile;
        this.branch = branch;
    }

    /**
     * {@code GET  /application-users/:id} : get the "id" applicationUser.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the applicationUserDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/application-status")
    public ResponseEntity<ApplicationStatus> getApplicationStatus() {
        log.debug("REST request to get Application Status");
        Optional<ApplicationStatus> applicationUserDTO = Optional.ofNullable(
            ApplicationStatus.builder()
                .build(build)
                .version(version)
                // .profile(profile)
                .branch(branch)
                .build());
        return ResponseUtil.wrapOrNotFound(applicationUserDTO);
    }
}
