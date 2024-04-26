package io.github.erp.internal.report.dynamic;

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
import net.sf.dynamicreports.report.builder.ReportTemplateBuilder;
import net.sf.dynamicreports.report.builder.component.ComponentBuilder;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.constant.HorizontalAlignment;
import net.sf.dynamicreports.report.constant.HorizontalTextAlignment;

import static net.sf.dynamicreports.report.builder.DynamicReports.*;

public class Templates {

    public static final StyleBuilder boldStyle = stl.style().bold();
    public static final StyleBuilder boldCenteredStyle = stl.style(boldStyle).setHorizontalAlignment(HorizontalAlignment.CENTER);

    public static final ReportTemplateBuilder reportTemplate = template()
            .setColumnStyle(boldCenteredStyle)
            .setColumnTitleStyle(boldCenteredStyle)
            .setGroupStyle(boldCenteredStyle)
            .setGroupTitleStyle(boldCenteredStyle)
            .highlightDetailEvenRows();

    public static final ComponentBuilder<?, ?> footerComponent = cmp.pageXofY()
            .setStyle(boldCenteredStyle);
}
