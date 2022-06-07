package io.github.erp.domain;

/*-
 * Erp System - Mark II No 7 (Artaxerxes Series)
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

import static org.assertj.core.api.Assertions.assertThat;

import io.github.erp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PdfReportRequisitionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PdfReportRequisition.class);
        PdfReportRequisition pdfReportRequisition1 = new PdfReportRequisition();
        pdfReportRequisition1.setId(1L);
        PdfReportRequisition pdfReportRequisition2 = new PdfReportRequisition();
        pdfReportRequisition2.setId(pdfReportRequisition1.getId());
        assertThat(pdfReportRequisition1).isEqualTo(pdfReportRequisition2);
        pdfReportRequisition2.setId(2L);
        assertThat(pdfReportRequisition1).isNotEqualTo(pdfReportRequisition2);
        pdfReportRequisition1.setId(null);
        assertThat(pdfReportRequisition1).isNotEqualTo(pdfReportRequisition2);
    }
}
