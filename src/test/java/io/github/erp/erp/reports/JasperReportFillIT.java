package io.github.erp.erp.reports;

import io.github.erp.IntegrationTest;
import io.github.erp.domain.Dealer;
import io.github.erp.erp.resources.DealerResourceIT;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@IntegrationTest
public class JasperReportFillIT {

    private static final String TAG = "JasperReportFill";
    private static final Logger log = LoggerFactory.getLogger(TAG);

    @Autowired
    private DataSource dataSource;

    //@Autowired
    //private EntityManager em;

    @Autowired
    public DataSource testDataSource;

    @Autowired
    private LocalContainerEntityManagerFactoryBean testEntityManagerFactoryBean;

    @Test
    public void createSampleReport() throws Exception {

        EntityManager entityManager = testEntityManagerFactoryBean.getObject().createEntityManager();

        Dealer dealer = DealerResourceIT.createEntity(entityManager);
        Dealer dealer2 = DealerResourceIT.createUpdatedEntity(entityManager);
        entityManager.persist(dealer);
        entityManager.persist(dealer2);
        // entityManager.flush();

        File file =readFile("Simple_Blue.jrxml");

        JasperReport jasperReport = JasperCompileManager
            .compileReport(file.getAbsolutePath());

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("createdBy","David");

        DataSource dts = testEntityManagerFactoryBean.getDataSource();

        // TODO Confirm if we should use the datasource of the EM for this part in order to ensure data is filled
        assert dts != null;
        JasperPrint jasperPrint = JasperFillManager.fillReport(
            jasperReport, parameters, dts.getConnection());

        getUploadPath(jasperPrint, "/test-report.pdf");
    }

    private static Path getUploadPath(JasperPrint jasperPrint, String fileName) throws IOException, JRException {
        String uploadDir = StringUtils.cleanPath("./generated-reports/");
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)){
            Files.createDirectories(uploadPath);
        }
        //generate the report and save it in the just created folder
        JasperExportManager.exportReportToPdfFile(jasperPrint, uploadPath+fileName);

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

    /*public DataSource getDataSourceFromHibernateEntityManager(EntityManager em) {
        EntityManagerFactoryInfo info = em.get;
        return info.getDataSource();
    }*/

    private DataSource getDataSource(EntityManagerFactory emf) {

        Map<String, Object> properties = emf.getProperties();
        return  (DataSource) properties.get("hibernate.connection.datasource");
    }

    @Bean("testDataSource")
    public DataSource testDataSource() {
        DriverManagerDataSource driver = new DriverManagerDataSource();
        driver.setDriverClassName("org.postgresql.Driver");
        driver.setUrl("jdbc:postgresql://10.60.27.22:5433/erpSystemDev");
        driver.setUsername("postgres");
        driver.setPassword("greywarren");
        // driver.setValidationQuery("SELECT 1");

        return driver;
    }

    @Bean("testEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean testEntityManagerFactory() {
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setDatabase(Database.POSTGRESQL);
        vendorAdapter.setGenerateDdl(true);

        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setJpaVendorAdapter(vendorAdapter);
        factory.setPackagesToScan(getClass().getPackage().getName());
        factory.setDataSource(Objects.requireNonNull(testEntityManagerFactoryBean.getDataSource()));

        return factory;
    }

    @Bean("testTransactionManager")
    public JpaTransactionManager testTransactionManager() {
        JpaTransactionManager txManager = new JpaTransactionManager();
        txManager.setEntityManagerFactory(testEntityManagerFactoryBean.getObject());

        return txManager;
    }


}
