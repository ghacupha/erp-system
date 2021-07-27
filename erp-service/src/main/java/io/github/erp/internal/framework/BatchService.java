package io.github.erp.internal.framework;

/*-
 * Copyright © 2021 Edwin Njeru (mailnjeru@gmail.com)
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
     * @param entities
     * @return
     */
    void index(List<T> entities);
}
