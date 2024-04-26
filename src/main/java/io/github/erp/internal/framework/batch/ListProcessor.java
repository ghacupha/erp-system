package io.github.erp.internal.framework.batch;

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
import org.springframework.batch.item.ItemProcessor;

import java.util.List;

/**
 * This is a processor mostly applicable in batch processes that takes a list of one form of data
 * and responds with another
 *
 * @param <E> Initial form of data
 * @param <D> Output form of data
 */
public interface ListProcessor<E, D> extends ItemProcessor<List<E>, List<D>> {
}
