package io.github.erp.internal.report;

/*-
 * Erp System - Mark II No 28 (Baruch Series) Server ver 0.0.8-SNAPSHOT
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

/**
 * This interface is a general interface for clients that read report requests and fetch the
 * appropriate report template and dump it on the report directory path. The goal is to have
 * the right template when one is needed, so that the next step in report generation can commence
 *
 * @param <T>
 */
public interface ReportTemplatePresentation<T> {

    String presentTemplate(T dto);
}
