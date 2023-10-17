package io.github.erp.service.dto;

/*-
 * Erp System - Mark VI No 3 (Phoebe Series) Server ver 1.5.4
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

import static org.assertj.core.api.Assertions.assertThat;

import io.github.erp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ChannelTypeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ChannelTypeDTO.class);
        ChannelTypeDTO channelTypeDTO1 = new ChannelTypeDTO();
        channelTypeDTO1.setId(1L);
        ChannelTypeDTO channelTypeDTO2 = new ChannelTypeDTO();
        assertThat(channelTypeDTO1).isNotEqualTo(channelTypeDTO2);
        channelTypeDTO2.setId(channelTypeDTO1.getId());
        assertThat(channelTypeDTO1).isEqualTo(channelTypeDTO2);
        channelTypeDTO2.setId(2L);
        assertThat(channelTypeDTO1).isNotEqualTo(channelTypeDTO2);
        channelTypeDTO1.setId(null);
        assertThat(channelTypeDTO1).isNotEqualTo(channelTypeDTO2);
    }
}
