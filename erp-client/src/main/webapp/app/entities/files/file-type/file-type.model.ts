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

import { IPlaceholder } from 'app/entities/system/placeholder/placeholder.model';
import { FileMediumTypes } from 'app/entities/enumerations/file-medium-types.model';
import { FileModelType } from 'app/entities/enumerations/file-model-type.model';

export interface IFileType {
  id?: number;
  fileTypeName?: string;
  fileMediumType?: FileMediumTypes;
  description?: string | null;
  fileTemplateContentType?: string | null;
  fileTemplate?: string | null;
  fileType?: FileModelType;
  placeholders?: IPlaceholder[] | null;
}

export class FileType implements IFileType {
  constructor(
    public id?: number,
    public fileTypeName?: string,
    public fileMediumType?: FileMediumTypes,
    public description?: string | null,
    public fileTemplateContentType?: string | null,
    public fileTemplate?: string | null,
    public fileType?: FileModelType,
    public placeholders?: IPlaceholder[] | null
  ) {}
}

export function getFileTypeIdentifier(fileType: IFileType): number | undefined {
  return fileType.id;
}
