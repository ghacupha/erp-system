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
import { ReportStatusTypes } from '../../erp-common/enumerations/report-status-types.model';
import { IPlaceholder } from '../../erp-pages/placeholder/placeholder.model';
import { IReportContentType } from '../report-content-type/report-content-type.model';
import { IReportTemplate } from '../report-template/report-template.model';
import { IUniversallyUniqueMapping } from '../../erp-pages/universally-unique-mapping/universally-unique-mapping.model';

export interface IReportRequisition {
  id?: number;
  reportName?: string;
  reportRequestTime?: dayjs.Dayjs;
  reportPassword?: string;
  reportStatus?: ReportStatusTypes | null;
  reportId?: string;
  reportFileAttachmentContentType?: string | null;
  reportFileAttachment?: string | null;
  reportFileCheckSum?: string | null;
  reportNotesContentType?: string | null;
  reportNotes?: string | null;
  placeholders?: IPlaceholder[] | null;
  parameters?: IUniversallyUniqueMapping[] | null;
  reportTemplate?: IReportTemplate;
  reportContentType?: IReportContentType;
}

export class ReportRequisition implements IReportRequisition {
  constructor(
    public id?: number,
    public reportName?: string,
    public reportRequestTime?: dayjs.Dayjs,
    public reportPassword?: string,
    public reportStatus?: ReportStatusTypes | null,
    public reportId?: string,
    public reportFileAttachmentContentType?: string | null,
    public reportFileAttachment?: string | null,
    public reportFileCheckSum?: string | null,
    public reportNotesContentType?: string | null,
    public reportNotes?: string | null,
    public placeholders?: IPlaceholder[] | null,
    public parameters?: IUniversallyUniqueMapping[] | null,
    public reportTemplate?: IReportTemplate,
    public reportContentType?: IReportContentType
  ) {}
}

export function getReportRequisitionIdentifier(reportRequisition: IReportRequisition): number | undefined {
  return reportRequisition.id;
}
