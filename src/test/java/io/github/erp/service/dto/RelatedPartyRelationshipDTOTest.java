package io.github.erp.service.dto;

/*-
 * Erp System - Mark X No 5 (Jehoiada Series) Server ver 1.7.5
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
import static org.assertj.core.api.Assertions.assertThat;

import io.github.erp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RelatedPartyRelationshipDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RelatedPartyRelationshipDTO.class);
        RelatedPartyRelationshipDTO relatedPartyRelationshipDTO1 = new RelatedPartyRelationshipDTO();
        relatedPartyRelationshipDTO1.setId(1L);
        RelatedPartyRelationshipDTO relatedPartyRelationshipDTO2 = new RelatedPartyRelationshipDTO();
        assertThat(relatedPartyRelationshipDTO1).isNotEqualTo(relatedPartyRelationshipDTO2);
        relatedPartyRelationshipDTO2.setId(relatedPartyRelationshipDTO1.getId());
        assertThat(relatedPartyRelationshipDTO1).isEqualTo(relatedPartyRelationshipDTO2);
        relatedPartyRelationshipDTO2.setId(2L);
        assertThat(relatedPartyRelationshipDTO1).isNotEqualTo(relatedPartyRelationshipDTO2);
        relatedPartyRelationshipDTO1.setId(null);
        assertThat(relatedPartyRelationshipDTO1).isNotEqualTo(relatedPartyRelationshipDTO2);
    }
}
