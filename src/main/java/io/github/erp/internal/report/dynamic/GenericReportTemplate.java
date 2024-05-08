package io.github.erp.internal.report.dynamic;

/*-
 * Erp System - Mark X No 8 (Jehoiada Series) Server ver 1.8.0
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
import net.sf.dynamicreports.jasper.builder.export.Exporters;
import net.sf.dynamicreports.jasper.constant.PdfPermission;
import net.sf.dynamicreports.report.builder.DynamicReports;
import net.sf.dynamicreports.report.builder.column.ColumnBuilder;
import net.sf.dynamicreports.report.builder.column.Columns;
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.datasource.DRDataSource;
import net.sf.dynamicreports.report.exception.DRException;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static net.sf.dynamicreports.report.builder.DynamicReports.report;
import static net.sf.dynamicreports.report.builder.DynamicReports.stl;

/**
 * Takes a class type and dynamically generates columns based on class properties
 * @param <T>
 */
public class GenericReportTemplate<T> {

    private final Class<T> type;
    private final List<TextColumnBuilder<?>> columns;
    private final DRDataSource dataSource;

    public GenericReportTemplate(Class<T> type) {
        this.type = type;
        this.columns = generateColumns();
        this.dataSource = new DRDataSource(getFieldNames());
    }

    public GenericReportTemplate<T> configureReport() {
        DynamicReports.report()
            .columns(columns.toArray(new TextColumnBuilder[columns.size()]))
            .title(DynamicReports.cmp.text(type.getSimpleName() + " Report"))
            .setTemplate(Templates.reportTemplate)
            .setSubtotalStyle(stl.style().setPadding(2).setBottomBorder(stl.pen1Point()))
            .pageFooter(Templates.footerComponent)
            .setPageMargin(DynamicReports.margin().setLeft(20).setRight(20))
            .build();

        return this;
    }

    public GenericReportTemplate<T>  setDataSource(List<T> entityList) {
        for (T entity : entityList) {
            for (Field field : type.getDeclaredFields()) {
                try {
                    field.setAccessible(true);
                    Object value = field.get(entity);
                    dataSource.add(value != null ? value.toString() : "");
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }

        return this;
    }

    public void toXlsx(OutputStream outputStream, String reportTitle) throws DRException {
        DynamicReports.report()
            .columns(columns.toArray(new TextColumnBuilder[columns.size()]))
            .title(DynamicReports.cmp.text(reportTitle))
            .setTemplate(Templates.reportTemplate)
            .setSubtotalStyle(stl.style().setPadding(2).setBottomBorder(stl.pen1Point()))
            .pageFooter(Templates.footerComponent)
            .setPageMargin(DynamicReports.margin().setLeft(20).setRight(20))
            .setDataSource(dataSource)
            .toXlsx(Exporters.xlsxExporter(outputStream));
    }


    public void toPdf(OutputStream outputStream, String pdfPassword, String systemReportPassword) throws DRException {
        DynamicReports.report()
            .columns((ColumnBuilder<?, ?>) columns)
            .title(DynamicReports.cmp.text(type.getSimpleName() + " Report"))
            .setTemplate(Templates.reportTemplate)
            .setSubtotalStyle(stl.style().setPadding(2).setBottomBorder(stl.pen1Point()))
            .pageFooter(Templates.footerComponent)
            .setPageMargin(DynamicReports.margin().setLeft(20).setRight(20))
            .setDataSource(dataSource)
            .toPdf(Exporters.pdfExporter(outputStream)
                .setEncrypted(true)
                .setOwnerPassword(systemReportPassword)
                .setUserPassword(pdfPassword)
                .addPermission(PdfPermission.SCREEN_READERS)
            );
    }

    public void toPdf(OutputStream outputStream, String systemReportPassword) throws DRException {
        DynamicReports.report()
            .columns(columns.toArray(new TextColumnBuilder[0]))
            .title(DynamicReports.cmp.text(type.getSimpleName() + " Report"))
            .setTemplate(Templates.reportTemplate)
            .setSubtotalStyle(stl.style().setPadding(2).setBottomBorder(stl.pen1Point()))
            .pageFooter(Templates.footerComponent)
            .setPageMargin(DynamicReports.margin().setLeft(20).setRight(20))
            .setDataSource(dataSource)
            .toPdf(Exporters.pdfExporter(outputStream)
                .setEncrypted(true)
                .setOwnerPassword(systemReportPassword)
                .addPermission(PdfPermission.SCREEN_READERS)
            );
    }

    private List<TextColumnBuilder<?>> generateColumns() {
        List<TextColumnBuilder<?>> columns = new ArrayList<>();

        for (Field field : type.getDeclaredFields()) {
            TextColumnBuilder<?> column = Columns.column(field.getName(), field.getName(), DynamicReports.type.stringType());
            columns.add(column);
        }

        return columns;
    }

    private String[] getFieldNames() {
        return Arrays.stream(type.getDeclaredFields())
            .map(Field::getName)
            .toArray(String[]::new);
    }
}

