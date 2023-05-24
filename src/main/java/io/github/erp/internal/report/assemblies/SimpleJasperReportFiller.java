package io.github.erp.internal.report.assemblies;

/*-
 * Erp System - Mark III No 15 (Caleb Series) Server ver 1.2.4
 * Copyright © 2021 - 2023 Edwin Njeru (mailnjeru@gmail.com)
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
import lombok.Data;
import lombok.SneakyThrows;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.Map;

@Data
@Service
public class SimpleJasperReportFiller {

    private final DataSource dataSource;

    public SimpleJasperReportFiller(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @SneakyThrows
    public JasperPrint fillReport(JasperReport compiledReport, Map<String, Object> parameters) {
        return JasperFillManager.fillReport(compiledReport, parameters, dataSource.getConnection());
    }
}
