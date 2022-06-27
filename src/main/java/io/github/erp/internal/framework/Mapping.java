package io.github.erp.internal.framework;

/*-
 * Erp System - Mark II No 11 (Artaxerxes Series)
 * Copyright © 2021 Edwin Njeru (mailnjeru@gmail.com)
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
import com.google.common.collect.ImmutableList;

import java.util.List;

/**
 * This is a general interface for mapping one type of entity or data transfer object
 * <p/>
 * into another commonly used with mapstruct. So here we are also assuming client code to
 * <p/>
 * mapstruct configurations at the ready
 */
public interface Mapping<V1, V2> {

    V1 toValue1(V2 vs);

    V2 toValue2(V1 vs);

    default List<V1> toValue1(List<V2> vs) {
        return vs.stream().map(this::toValue1).collect(ImmutableList.toImmutableList());
    }

    default List<V2> toValue2(List<V1> vs) {
        return vs.stream().map(this::toValue2).collect(ImmutableList.toImmutableList());
    }
}
