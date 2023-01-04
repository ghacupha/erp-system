package io.github.erp.internal.framework;

/*-
 * Erp System - Mark III No 8 (Caleb Series) Server ver 0.3.0-SNAPSHOT
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
import java.util.List;

/**
 * This interface is intended to be implemented transactionally
 * <p/>
 *  but with batch items persisted for every commit. We are also assuming
 * <p/>
 * that the client uses a search engine. I mean the fact that you are using this
 * <p/>
 * right?
 */
public interface BatchService<T> {

    /**
     * Save an entity.
     *
     * @param entities entity to save.
     * @return the persisted entity.
     */
    List<T> save(List<T> entities);

    /**
     * The above call only persists entities to the relations db repository
     * for efficiency sake.
     * Therefore to have it all in an index one needs to call this function
     * It is implemented as void to allow the client to optionally forego is not found necessary
     * @param entities
     * @return
     */
    void index(List<T> entities);
}
