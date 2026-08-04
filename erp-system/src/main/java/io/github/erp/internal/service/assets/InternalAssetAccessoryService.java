package io.github.erp.internal.service.assets;

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
import io.github.erp.service.dto.AssetAccessoryDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Extension-pattern replacement for the generated {@code AssetAccessoryService} (see
 * InternalAssetRegistrationService for the identical precedent) - same CRUD/search surface, but
 * indexes asynchronously via Kafka instead of writing to Elasticsearch synchronously inside the
 * save transaction.
 */
public interface InternalAssetAccessoryService {
    AssetAccessoryDTO save(AssetAccessoryDTO assetAccessoryDTO);

    Optional<AssetAccessoryDTO> partialUpdate(AssetAccessoryDTO assetAccessoryDTO);

    Page<AssetAccessoryDTO> findAll(Pageable pageable);

    Page<AssetAccessoryDTO> findAllWithEagerRelationships(Pageable pageable);

    Optional<AssetAccessoryDTO> findOne(Long id);

    void delete(Long id);

    Page<AssetAccessoryDTO> search(String query, Pageable pageable);
}
