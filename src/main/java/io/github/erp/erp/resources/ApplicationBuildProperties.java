package io.github.erp.erp.resources;

/*-
 * Erp System - Mark III No 10 (Caleb Series) Server ver 0.6.0
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.info.BuildProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/v2/api")
public class ApplicationBuildProperties {

    @Value("${git.commit.message.short}")
    private String commitMessage;

    @Value("${git.branch}")
    private String branch;

    @Value("${git.commit.id}")
    private String commitId;

    private final BuildProperties buildProperties;

    public ApplicationBuildProperties(BuildProperties buildProperties) {
        this.buildProperties = buildProperties;
    }

    private static final Logger log = LoggerFactory.getLogger(ApplicationBuildProperties.class);

    /**
     * {@code GET  /server-version} : get the server version.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of agencyNotices in body.
     */
    @GetMapping("/server-version")
    public ResponseEntity<String> getServerVersion() {
        log.debug("Request for server's version");
        return ResponseEntity.ok().body(buildProperties.getVersion());
    }

    /**
     * {@code GET  /server-build-time} : get the server version.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of agencyNotices in body.
     */
    @GetMapping("/server-build-time")
    public ResponseEntity<Instant> getServerBuildTime() {
        log.debug("Request for server's version");
        return ResponseEntity.ok(buildProperties.getTime());
    }

    @RequestMapping("/commitId")
    public ResponseEntity<Map<String, String>> getCommitId() {
        Map<String, String> result = new HashMap<>();
        result.put("Commit message",commitMessage);
        result.put("Commit branch", branch);
        result.put("Commit id", commitId);
        return ResponseEntity.ok(result);
    }
}
