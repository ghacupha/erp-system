package io.github.erp.internal.report.assemblies;

/*-
 * Erp System - Mark II No 28 (Baruch Series) Server ver 0.1.1-SNAPSHOT
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
