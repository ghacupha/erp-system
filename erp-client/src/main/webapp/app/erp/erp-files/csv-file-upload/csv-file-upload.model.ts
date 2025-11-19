///
/// Erp System - Mark X No 11 (Jehoiada Series) Client 1.7.9
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

export interface ICsvFileUpload {
  id?: number;
  originalFileName?: string;
  storedFileName?: string;
  filePath?: string;
  fileSize?: number;
  contentType?: string;
  uploadedAt?: dayjs.Dayjs;
  processed?: boolean;
  checksum?: string;
}

export class CsvFileUpload implements ICsvFileUpload {
  constructor(
    public id?: number,
    public originalFileName?: string,
    public storedFileName?: string,
    public filePath?: string,
    public fileSize?: number,
    public contentType?: string,
    public uploadedAt?: dayjs.Dayjs,
    public processed?: boolean,
    public checksum?: string
  ) {
    this.processed = this.processed ?? false;
  }
}

export function getCsvFileUploadIdentifier(csvFileUpload: ICsvFileUpload): number | undefined {
  return csvFileUpload.id;
}
