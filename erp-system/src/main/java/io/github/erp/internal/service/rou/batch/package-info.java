
/*-
 * Erp System - Mark X No 11 (Jehoiada Series) Server ver 1.8.3
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
/**
 * The batch implementation of ROU depreciation starts with an initial acquisition of
 * rou-model-metadata instances and after filtering and sorting the process then processes each individually
 * by determining the depreciation horizon for each and extracting from the repository the
 * relevant lease-period instances. Then for each lease-period the system will then
 * calculate depreciation amount and persist that data as depreciation-entry
 */
package io.github.erp.internal.service.rou.batch;
