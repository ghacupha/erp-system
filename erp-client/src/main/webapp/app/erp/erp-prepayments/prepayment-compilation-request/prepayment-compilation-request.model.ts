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
import { CompilationStatusTypes } from '../../erp-common/enumerations/compilation-status-types.model';
import { IPlaceholder } from '../../erp-pages/placeholder/placeholder.model';

export interface IPrepaymentCompilationRequest {
  id?: number;
  timeOfRequest?: dayjs.Dayjs | null;
  compilationStatus?: CompilationStatusTypes | null;
  itemsProcessed?: number | null;
  compilationToken?: string;
  placeholders?: IPlaceholder[] | null;
}

export class PrepaymentCompilationRequest implements IPrepaymentCompilationRequest {
  constructor(
    public id?: number,
    public timeOfRequest?: dayjs.Dayjs | null,
    public compilationStatus?: CompilationStatusTypes | null,
    public itemsProcessed?: number | null,
    public compilationToken?: string,
    public placeholders?: IPlaceholder[] | null
  ) {}
}

export function getPrepaymentCompilationRequestIdentifier(prepaymentCompilationRequest: IPrepaymentCompilationRequest): number | undefined {
  return prepaymentCompilationRequest.id;
}
