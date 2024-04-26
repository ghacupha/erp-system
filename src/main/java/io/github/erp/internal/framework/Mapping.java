package io.github.erp.internal.framework;

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
