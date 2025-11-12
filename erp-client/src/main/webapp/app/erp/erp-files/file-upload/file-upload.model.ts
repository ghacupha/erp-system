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

import * as dayjs from 'dayjs';
import { IPlaceholder } from '../../erp-pages/placeholder/placeholder.model';

export interface IFileUpload {
  id?: number;
  description?: string;
  fileName?: string;
  periodFrom?: dayjs.Dayjs | null;
  periodTo?: dayjs.Dayjs | null;
  fileTypeId?: number;
  dataFileContentType?: string;
  dataFile?: string;
  uploadSuccessful?: boolean | null;
  uploadProcessed?: boolean | null;
  uploadToken?: string | null;
  placeholders?: IPlaceholder[] | null;
}

export class FileUpload implements IFileUpload {
  constructor(
    public id?: number,
    public description?: string,
    public fileName?: string,
    public periodFrom?: dayjs.Dayjs | null,
    public periodTo?: dayjs.Dayjs | null,
    public fileTypeId?: number,
    public dataFileContentType?: string,
    public dataFile?: string,
    public uploadSuccessful?: boolean | null,
    public uploadProcessed?: boolean | null,
    public uploadToken?: string | null,
    public placeholders?: IPlaceholder[] | null
  ) {
    this.uploadSuccessful = this.uploadSuccessful ?? false;
    this.uploadProcessed = this.uploadProcessed ?? false;
  }
}

export function getFileUploadIdentifier(fileUpload: IFileUpload): number | undefined {
  return fileUpload.id;
}
