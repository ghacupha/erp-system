package io.github.erp.internal.report.dynamic;

/*-
 * Erp System - Mark X No 9 (Jehoiada Series) Server ver 1.8.1
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
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
