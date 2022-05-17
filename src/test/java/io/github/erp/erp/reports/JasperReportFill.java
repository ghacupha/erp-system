package io.github.erp.erp.reports;

import io.github.erp.IntegrationTest;
import io.github.erp.domain.Dealer;
import io.github.erp.erp.resources.DealerResourceIT;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.sql.DataSource;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static io.github.erp.internal.framework.excel.ExcelTestUtil.readFile;

@IntegrationTest
public class JasperReportFill {

    private static final String TAG = "JasperReportFill";
    private static final Logger log = LoggerFactory.getLogger(TAG);

    @Autowired
    private DataSource dataSource;

    @Autowired
    private EntityManager em;

    @Test
    @Transactional
    void createSampleReport() throws Exception {

        Dealer dealer = DealerResourceIT.createEntity(em);
        Dealer dealer2 = DealerResourceIT.createUpdatedEntity(em);
        em.persist(dealer);
        em.persist(dealer2);
        em.flush();

        File file =readFile("Simple_Blue.jrxml");

        JasperReport jasperReport = JasperCompileManager
            .compileReport(file.getAbsolutePath());

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("createdBy","David");

        // TODO Confirm if we should use the datasource of the EM for this part in order to ensure data is filled
        JasperPrint jasperPrint = JasperFillManager.fillReport(
            jasperReport, parameters, dataSource.getConnection());

        getUploadPath("pdf", jasperPrint, "/test-report.pdf");
    }

    private static Path getUploadPath(String fileFormat, JasperPrint jasperPrint, String fileName) throws IOException, JRException {
        String uploadDir = StringUtils.cleanPath("./generated-reports/");
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)){
            Files.createDirectories(uploadPath);
        }
        //generate the report and save it in the just created folder
        if (fileFormat.equalsIgnoreCase("pdf")){
            JasperExportManager.exportReportToPdfFile(
                jasperPrint, uploadPath+fileName
            );
        }

        return uploadPath;
    }

    public static File readFile(String filename) {

        log.info("\nReading file : {}...", filename);

        // @formatter:off
        return new File(
            Objects.requireNonNull(
                ClassLoader.getSystemClassLoader()
                    .getResource("templates/reports/" + filename))
                .getFile());
        // @formatter:on
    }


}
