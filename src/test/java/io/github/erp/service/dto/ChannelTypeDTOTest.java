package io.github.erp.service.dto;

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
