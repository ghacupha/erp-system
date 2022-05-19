package io.github.erp.internal.report;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static io.github.erp.internal.report.QueryTools.getDataSourceFromHibernateEntityManager;

/**
 * This implementation uses the jasper-reports engine to generate a report and store it in a path
 * which path is later sent to the caller a string
 */
@Service
public class JasperReportsService implements StringMarshalledReport {

    private static Logger log = LoggerFactory.getLogger(JasperReportsService.class);

    private EntityManager em;


    public JasperReportsService(EntityManager em) {
        this.em = em;
    }

    private JasperPrint getJasperPrint(String resourceLocation) throws FileNotFoundException, JRException {
        File file = ResourceUtils.getFile(resourceLocation);

        log.debug("Commencement of report compilation using jrxml {}", file.getAbsolutePath());

        JasperReport jasperReport = JasperCompileManager
            .compileReport(file.getAbsolutePath());

        log.debug("Report file compilation complete, ");

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("Created By","ERP System Mark I version I (Artaxerxes)");

        JasperPrint jasperPrint = null;
        try {
            jasperPrint = JasperFillManager
                .fillReport(jasperReport,parameters, getDataSourceFromHibernateEntityManager(em).getConnection());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return jasperPrint;
    }

    /**
     * This method creates a specified directory if one does not exist, creating the generated
     * pdf file in the folder with the file name passed to it.
     *
     * @param fileFormat
     * @param jasperPrint
     * @param fileName
     * @return
     * @throws IOException
     * @throws JRException
     */
    private Path getUploadPath(String fileFormat, JasperPrint jasperPrint, String fileName) throws IOException, JRException {
        String uploadDir = StringUtils.cleanPath("E:\\labs\\servers\\erp-system\\generated-reports");
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

    /**
     * Returns link to the generated report
     *
     * @param uploadPath
     * @return
     */
    private String getPdfFileLink(String uploadPath){
        return uploadPath+"\\"+"dealers.pdf";
    }

    @Override
    public String createReport(String reportFormat) {
        //load the file and compile it
        String resourceLocation = "E:\\labs\\servers\\erp-system\\src\\main\\resources\\templates\\reports\\Simple_Blue.jrxml";

        JasperPrint jasperPrint = null;
        try {
            jasperPrint = getJasperPrint(resourceLocation);
        } catch (FileNotFoundException | JRException e) {
            e.printStackTrace();
        }     //create a folder to store the report
        String fileName = "\\"+"dealers.pdf";
        Path uploadPath = null;
        try {
            uploadPath = getUploadPath(reportFormat, jasperPrint, fileName);
        } catch (IOException | JRException e) {
            e.printStackTrace();
        }
        //create a private method that returns the link to the specific pdf file

        return getPdfFileLink(Objects.requireNonNull(uploadPath).toString());
    }
}
