package io.github.erp.internal.report.dynamic;

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
