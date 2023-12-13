package io.github.erp.erp.resources;

/*-
 * Erp System - Mark IX No 2 (Iddo Series) Server ver 1.6.4
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
