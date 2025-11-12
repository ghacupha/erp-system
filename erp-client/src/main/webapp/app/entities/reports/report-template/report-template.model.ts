///
/// Erp System - Mark X No 11 (Jehoiada Series) Client 1.7.8
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

import { IPlaceholder } from 'app/entities/system/placeholder/placeholder.model';

export interface IReportTemplate {
  id?: number;
  catalogueNumber?: string;
  description?: string | null;
  notesContentType?: string | null;
  notes?: string | null;
  reportFileContentType?: string | null;
  reportFile?: string | null;
  compileReportFileContentType?: string | null;
  compileReportFile?: string | null;
  placeholders?: IPlaceholder[] | null;
}

export class ReportTemplate implements IReportTemplate {
  constructor(
    public id?: number,
    public catalogueNumber?: string,
    public description?: string | null,
    public notesContentType?: string | null,
    public notes?: string | null,
    public reportFileContentType?: string | null,
    public reportFile?: string | null,
    public compileReportFileContentType?: string | null,
    public compileReportFile?: string | null,
    public placeholders?: IPlaceholder[] | null
  ) {}
}

export function getReportTemplateIdentifier(reportTemplate: IReportTemplate): number | undefined {
  return reportTemplate.id;
}
