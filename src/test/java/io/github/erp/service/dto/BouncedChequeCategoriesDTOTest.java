package io.github.erp.service.dto;

/*-
 * Erp System - Mark IX No 3 (Iddo Series) Server ver 1.6.5
 * Copyright © 2021 - 2023 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
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

class BouncedChequeCategoriesDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(BouncedChequeCategoriesDTO.class);
        BouncedChequeCategoriesDTO bouncedChequeCategoriesDTO1 = new BouncedChequeCategoriesDTO();
        bouncedChequeCategoriesDTO1.setId(1L);
        BouncedChequeCategoriesDTO bouncedChequeCategoriesDTO2 = new BouncedChequeCategoriesDTO();
        assertThat(bouncedChequeCategoriesDTO1).isNotEqualTo(bouncedChequeCategoriesDTO2);
        bouncedChequeCategoriesDTO2.setId(bouncedChequeCategoriesDTO1.getId());
        assertThat(bouncedChequeCategoriesDTO1).isEqualTo(bouncedChequeCategoriesDTO2);
        bouncedChequeCategoriesDTO2.setId(2L);
        assertThat(bouncedChequeCategoriesDTO1).isNotEqualTo(bouncedChequeCategoriesDTO2);
        bouncedChequeCategoriesDTO1.setId(null);
        assertThat(bouncedChequeCategoriesDTO1).isNotEqualTo(bouncedChequeCategoriesDTO2);
    }
}
