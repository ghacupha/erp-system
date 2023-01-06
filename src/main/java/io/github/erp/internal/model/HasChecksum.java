package io.github.erp.internal.model;

/*-
 * Erp System - Mark III No 8 (Caleb Series) Server ver 0.3.0
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
 * This interface serves an object that tracks a checksum of some kind,
 * without dictating the algorithm for the calculation or the purpose. The
 * checksum is just there and is used internally used by implementation for
 * unique identification of data, files, users and verification of the same
 */
public interface HasChecksum {

    void setChecksum(String fileChecksum);

    String getFileChecksum();
}
