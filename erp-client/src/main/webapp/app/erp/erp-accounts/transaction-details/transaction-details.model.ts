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
import { ITransactionAccount } from '../transaction-account/transaction-account.model';
import { IPlaceholder } from '../../erp-pages/placeholder/placeholder.model';
import { IApplicationUser } from '../../erp-pages/application-user/application-user.model';

export interface ITransactionDetails {
  id?: number;
  entryId?: number;
  transactionDate?: dayjs.Dayjs;
  description?: string | null;
  amount?: number;
  createdAt?: dayjs.Dayjs;
  modifiedAt?: dayjs.Dayjs | null;
  transactionType?: string | null;
  debitAccount?: ITransactionAccount;
  creditAccount?: ITransactionAccount;
  placeholders?: IPlaceholder[] | null;
  postedBy?: IApplicationUser | null;
}

export class TransactionDetails implements ITransactionDetails {
  constructor(
    public id?: number,
    public entryId?: number,
    public transactionDate?: dayjs.Dayjs,
    public description?: string | null,
    public amount?: number,
    public createdAt?: dayjs.Dayjs,
    public modifiedAt?: dayjs.Dayjs | null,
    public transactionType?: string | null,
    public debitAccount?: ITransactionAccount,
    public creditAccount?: ITransactionAccount,
    public placeholders?: IPlaceholder[] | null,
    public postedBy?: IApplicationUser | null
  ) {}
}

export function getTransactionDetailsIdentifier(transactionDetails: ITransactionDetails): number | undefined {
  return transactionDetails.id;
}
