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
import { IDepreciationMethod } from '../../erp-assets/depreciation-method/depreciation-method.model';
import { IPrepaymentMapping } from '../prepayment-mapping/prepayment-mapping.model';
import { IUniversallyUniqueMapping } from '../../erp-pages/universally-unique-mapping/universally-unique-mapping.model';
import { IPrepaymentAccount } from '../prepayment-account/prepayment-account.model';
import { recurrenceFrequency } from '../../erp-common/enumerations/recurrence-frequency.model';

export interface IAmortizationRecurrence {
  id?: number;
  firstAmortizationDate?: dayjs.Dayjs;
  amortizationFrequency?: recurrenceFrequency;
  numberOfRecurrences?: number;
  notesContentType?: string | null;
  notes?: string | null;
  particulars?: string | null;
  isActive?: boolean | null;
  isOverWritten?: boolean | null;
  timeOfInstallation?: dayjs.Dayjs;
  recurrenceGuid?: string;
  prepaymentAccountGuid?: string;
  placeholders?: IPlaceholder[] | null;
  parameters?: IPrepaymentMapping[] | null;
  applicationParameters?: IUniversallyUniqueMapping[] | null;
  depreciationMethod?: IDepreciationMethod;
  prepaymentAccount?: IPrepaymentAccount;
}

export class AmortizationRecurrence implements IAmortizationRecurrence {
  constructor(
    public id?: number,
    public firstAmortizationDate?: dayjs.Dayjs,
    public amortizationFrequency?: recurrenceFrequency,
    public numberOfRecurrences?: number,
    public notesContentType?: string | null,
    public notes?: string | null,
    public particulars?: string | null,
    public isActive?: boolean | null,
    public isOverWritten?: boolean | null,
    public timeOfInstallation?: dayjs.Dayjs,
    public recurrenceGuid?: string,
    public prepaymentAccountGuid?: string,
    public placeholders?: IPlaceholder[] | null,
    public parameters?: IPrepaymentMapping[] | null,
    public applicationParameters?: IUniversallyUniqueMapping[] | null,
    public depreciationMethod?: IDepreciationMethod,
    public prepaymentAccount?: IPrepaymentAccount
  ) {
    this.isActive = this.isActive ?? false;
    this.isOverWritten = this.isOverWritten ?? false;
  }
}

export function getAmortizationRecurrenceIdentifier(amortizationRecurrence: IAmortizationRecurrence): number | undefined {
  return amortizationRecurrence.id;
}
