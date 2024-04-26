package io.github.erp.internal.report.assemblies;

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
import lombok.SneakyThrows;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRSaver;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

@Service
public class SimpleJasperReportCompiler {

    @SneakyThrows
    public JasperReport compileReport(String reportFileName) {
        // InputStream reportStream = getClass().getResourceAsStream(reportFileName);
        File reportFile = new File(reportFileName);

        InputStream reportStream = new FileInputStream(reportFile);
        JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);

        return saveReport(jasperReport, reportFileName);
    }

    @SneakyThrows
    public JasperReport compileReport(File reportFile) {

        InputStream reportStream = new FileInputStream(reportFile);
        JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);

        return saveReport(jasperReport, reportFile.getAbsolutePath());
    }

    @SneakyThrows
    public JasperReport compileReport(InputStream reportFileStream) {

        JasperReport jasperReport = JasperCompileManager.compileReport(reportFileStream);

        return jasperReport;
    }

    private JasperReport saveReport(JasperReport jasperReport, String reportFileName) throws JRException {
        JRSaver.saveObject(jasperReport, reportFileName.replace(".jrxml", ".jasper"));

        return jasperReport;
    }
}
