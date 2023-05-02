
/*-
 * Erp System - Mark III No 13 (Caleb Series) Server ver 1.1.3-SNAPSHOT
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
package io.github.erp.erp.index.engine_v1;

/**
 * This is a registered index management service
 */
public interface ControllerIndexingService {

    /**
     * Method called back when the index sequence is being invoked
     */
    void index();

    /**
     * This method is called to register a service which is to respond to the callback
     */
    void register();

}
