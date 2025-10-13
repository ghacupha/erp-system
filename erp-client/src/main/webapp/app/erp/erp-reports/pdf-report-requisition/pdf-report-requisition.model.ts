///
/// Erp System - Mark X No 10 (Jehoiada Series) Client 1.7.8
/// Copyright © 2021 - 2024 Edwin Njeru (mailnjeru@gmail.com)
///
/// This program is free software: you can redistribute it and/or modify
/// it under the terms of the GNU General Public License as published by
/// the Free Software Foundation, either version 3 of the License, or
/// (at your option) any later version.
///
/// This program is distributed in the hope that it will be useful,
/// but WITHOUT ANY WARRANTY; without even the implied warranty of
/// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
/// GNU General Public License for more details.
///
/// You should have received a copy of the GNU General Public License
/// along with this program. If not, see <http://www.gnu.org/licenses/>.
///

import * as dayjs from 'dayjs';
import { IReportTemplate } from '../report-template/report-template.model';
import { IPlaceholder } from '../../erp-pages/placeholder/placeholder.model';
import { ReportStatusTypes } from '../../erp-common/enumerations/report-status-types.model';
import { IUniversallyUniqueMapping } from '../../erp-pages/universally-unique-mapping/universally-unique-mapping.model';

export interface IPdfReportRequisition {
  id?: number;
  reportName?: string;
  reportDate?: dayjs.Dayjs | null;
  userPassword?: string | null;
  ownerPassword?: string;
  reportStatus?: ReportStatusTypes | null;
  reportId?: string;
  reportTemplate?: IReportTemplate;
  placeholders?: IPlaceholder[] | null;
  reportAttachment?: string | null;
  reportFileChecksum?: string | null;
  parameters?: IUniversallyUniqueMapping[] | null;
}

export class PdfReportRequisition implements IPdfReportRequisition {
  constructor(
    public id?: number,
    public reportName?: string,
    public reportDate?: dayjs.Dayjs | null,
    public userPassword?: string | null,
    public ownerPassword?: string,
    public reportStatus?: ReportStatusTypes | null,
    public reportId?: string,
    public reportTemplate?: IReportTemplate,
    public placeholders?: IPlaceholder[] | null,
    public reportAttachment?: string | null,
    public reportFileChecksum?: string | null,
    public parameters?: IUniversallyUniqueMapping[] | null,
  ) {}
}

export function getPdfReportRequisitionIdentifier(pdfReportRequisition: IPdfReportRequisition): number | undefined {
  return pdfReportRequisition.id;
}
