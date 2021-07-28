///
/// Copyright © 2021 Edwin Njeru (mailnjeru@gmail.com)
///
/// Licensed under the Apache License, Version 2.0 (the "License");
/// you may not use this file except in compliance with the License.
/// You may obtain a copy of the License at
///
///     http://www.apache.org/licenses/LICENSE-2.0
///
/// Unless required by applicable law or agreed to in writing, software
/// distributed under the License is distributed on an "AS IS" BASIS,
/// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
/// See the License for the specific language governing permissions and
/// limitations under the License.
///

import { FileMediumTypes } from 'app/shared/model/enumerations/file-medium-types.model';
import { FileModelType } from 'app/shared/model/enumerations/file-model-type.model';

export interface IFileType {
  id?: number;
  fileTypeName?: string;
  fileMediumType?: FileMediumTypes;
  description?: string;
  fileTemplateContentType?: string;
  fileTemplate?: any;
  fileType?: FileModelType;
}

export class FileType implements IFileType {
  constructor(
    public id?: number,
    public fileTypeName?: string,
    public fileMediumType?: FileMediumTypes,
    public description?: string,
    public fileTemplateContentType?: string,
    public fileTemplate?: any,
    public fileType?: FileModelType
  ) {}
}
