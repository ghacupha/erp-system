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
import { IPrepaymentAccount } from 'app/entities/prepayments/prepayment-account/prepayment-account.model';
import { IAmortizationRecurrence } from 'app/entities/prepayments/amortization-recurrence/amortization-recurrence.model';
import { IPlaceholder } from 'app/entities/system/placeholder/placeholder.model';
import { IPrepaymentMapping } from 'app/entities/prepayments/prepayment-mapping/prepayment-mapping.model';
import { IUniversallyUniqueMapping } from 'app/entities/system/universally-unique-mapping/universally-unique-mapping.model';

export interface IAmortizationSequence {
  id?: number;
  prepaymentAccountGuid?: string;
  recurrenceGuid?: string;
  sequenceNumber?: number;
  particulars?: string | null;
  currentAmortizationDate?: dayjs.Dayjs;
  previousAmortizationDate?: dayjs.Dayjs | null;
  nextAmortizationDate?: dayjs.Dayjs | null;
  isCommencementSequence?: boolean;
  isTerminalSequence?: boolean;
  amortizationAmount?: number;
  sequenceGuid?: string;
  prepaymentAccount?: IPrepaymentAccount;
  amortizationRecurrence?: IAmortizationRecurrence;
  placeholders?: IPlaceholder[] | null;
  prepaymentMappings?: IPrepaymentMapping[] | null;
  applicationParameters?: IUniversallyUniqueMapping[] | null;
}

export class AmortizationSequence implements IAmortizationSequence {
  constructor(
    public id?: number,
    public prepaymentAccountGuid?: string,
    public recurrenceGuid?: string,
    public sequenceNumber?: number,
    public particulars?: string | null,
    public currentAmortizationDate?: dayjs.Dayjs,
    public previousAmortizationDate?: dayjs.Dayjs | null,
    public nextAmortizationDate?: dayjs.Dayjs | null,
    public isCommencementSequence?: boolean,
    public isTerminalSequence?: boolean,
    public amortizationAmount?: number,
    public sequenceGuid?: string,
    public prepaymentAccount?: IPrepaymentAccount,
    public amortizationRecurrence?: IAmortizationRecurrence,
    public placeholders?: IPlaceholder[] | null,
    public prepaymentMappings?: IPrepaymentMapping[] | null,
    public applicationParameters?: IUniversallyUniqueMapping[] | null
  ) {
    this.isCommencementSequence = this.isCommencementSequence ?? false;
    this.isTerminalSequence = this.isTerminalSequence ?? false;
  }
}

export function getAmortizationSequenceIdentifier(amortizationSequence: IAmortizationSequence): number | undefined {
  return amortizationSequence.id;
}
