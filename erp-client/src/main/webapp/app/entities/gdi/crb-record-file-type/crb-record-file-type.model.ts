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

export interface ICrbRecordFileType {
  id?: number;
  recordFileTypeCode?: string;
  recordFileType?: string;
  recordFileTypeDetails?: string | null;
}

export class CrbRecordFileType implements ICrbRecordFileType {
  constructor(
    public id?: number,
    public recordFileTypeCode?: string,
    public recordFileType?: string,
    public recordFileTypeDetails?: string | null
  ) {}
}

export function getCrbRecordFileTypeIdentifier(crbRecordFileType: ICrbRecordFileType): number | undefined {
  return crbRecordFileType.id;
}
