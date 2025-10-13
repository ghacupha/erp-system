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

import { ControlTypes } from '../../erp-common/enumerations/control-types.model';
import { IPlaceholder } from '../../erp-pages/placeholder/placeholder.model';
import { IUniversallyUniqueMapping } from '../../erp-pages/universally-unique-mapping/universally-unique-mapping.model';

export interface IStringQuestionBase {
  id?: number;
  // value?: string | null;
  value?: any | null;
  key?: string;
  label?: string;
  required?: boolean | null;
  order?: number;
  controlType?: ControlTypes;
  placeholder?: string | null;
  iterable?: boolean | null;
  parameters?: IUniversallyUniqueMapping[] | null;
  placeholderItems?: IPlaceholder[] | null;
}

export class StringQuestionBase implements IStringQuestionBase {
  constructor(
    public id?: number,
    public key?: string,
    public value?: string | null,
    public label?: string,
    public required?: boolean | null,
    public order?: number,
    // public controlType: ControlTypes = ControlTypes.TEXTBOX,
    public controlType?: ControlTypes,
    public placeholder?: string | null,
    public iterable?: boolean | null,
    public parameters?: IUniversallyUniqueMapping[] | null,
    public placeholderItems?: IPlaceholder[] | null
  ) {
    this.required = this.required ?? false;
    this.iterable = this.iterable ?? false;
  }
}

export function getStringQuestionBaseIdentifier(stringQuestionBase: IStringQuestionBase): number | undefined {
  return stringQuestionBase.id;
}
