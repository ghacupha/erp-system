
/*-
 *  Server - Leases and assets management platform
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
package io.github.erp.internal.model.sampleDataModel;

import io.github.erp.domain.enumeration.CurrencyLocality;
import io.github.erp.internal.framework.Mapping;
import io.github.erp.service.dto.CurrencyTableDTO;
import org.mapstruct.Mapper;

import static io.github.erp.domain.enumeration.CurrencyLocality.FOREIGN;
import static io.github.erp.domain.enumeration.CurrencyLocality.LOCAL;

/**
 * This is a sample implementation of how mapping is used to move from an entity's DTO to EVM
 */
@Mapper(componentModel = "spring", uses = {})
public interface CurrencyTableEVMMapping extends Mapping<CurrencyTableEVM, CurrencyTableDTO> {

    @org.mapstruct.Mapping(target = "locality", source = "locality")
    default CurrencyLocality locality(String localityString) {

        if (localityString == null) {
            return null;
        }
        if (localityString.equalsIgnoreCase("LOCAL")) {
            return LOCAL;
        }
        if (localityString.equalsIgnoreCase("FOREIGN")) {
            return FOREIGN;
        }
        return null;
    }

    @org.mapstruct.Mapping(target = "locality", source = "locality")
    default String locality(CurrencyLocality locality) {

        if (locality == null) {
            return null;
        }
        if (locality == LOCAL) {
            return "LOCAL";
        }
        if (locality == FOREIGN) {
            return "FOREIGN";
        }
        return null;
    }
}
