package io.github.erp.erp.resources;

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
@RequestMapping("/api")
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
