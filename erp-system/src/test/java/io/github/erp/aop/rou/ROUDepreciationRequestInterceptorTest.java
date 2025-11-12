package io.github.erp.aop.rou;

/*-
 * Erp System - Mark X No 10 (Jehoiada Series) Server ver 1.8.2
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

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

import io.github.erp.internal.service.rou.ROUDepreciationEntryCompilationJob;
import io.github.erp.service.dto.RouDepreciationRequestDTO;
import org.aspectj.lang.JoinPoint;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class ROUDepreciationRequestInterceptorTest {

    @Mock
    private ROUDepreciationEntryCompilationJob rouDepreciationEntryCompilationJob;

    private ROUDepreciationRequestInterceptor interceptor;

    @BeforeEach
    void setUp() {
        interceptor = new ROUDepreciationRequestInterceptor(rouDepreciationEntryCompilationJob);
    }

    @Test
    void whenResponseContainsBody_shouldTriggerCompilationJob() {
        RouDepreciationRequestDTO dto = new RouDepreciationRequestDTO();
        dto.setId(42L);

        interceptor.getCreatedReportInfo(mock(JoinPoint.class), ResponseEntity.ok(dto));

        verify(rouDepreciationEntryCompilationJob).compileROUDepreciationEntries(dto);
    }

    @Test
    void whenResponseHasNoBody_shouldFailFastAndNotTriggerJob() {
        ResponseEntity<RouDepreciationRequestDTO> emptyResponse = ResponseEntity.ok().build();

        assertThatThrownBy(() -> interceptor.getCreatedReportInfo(mock(JoinPoint.class), emptyResponse))
            .isInstanceOf(NullPointerException.class);

        verifyNoInteractions(rouDepreciationEntryCompilationJob);
    }
}
