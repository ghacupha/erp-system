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
