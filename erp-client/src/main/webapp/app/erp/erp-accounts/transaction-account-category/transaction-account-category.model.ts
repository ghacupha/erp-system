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

import { ITransactionAccountLedger } from '../transaction-account-ledger/transaction-account-ledger.model';
import { IPlaceholder } from '../../erp-pages/placeholder/placeholder.model';
import { transactionAccountPostingTypes } from '../../erp-common/enumerations/transaction-account-posting-types.model';

export interface ITransactionAccountCategory {
  id?: number;
  name?: string;
  transactionAccountPostingType?: transactionAccountPostingTypes;
  placeholders?: IPlaceholder[] | null;
  accountLedger?: ITransactionAccountLedger;
}

export class TransactionAccountCategory implements ITransactionAccountCategory {
  constructor(
    public id?: number,
    public name?: string,
    public transactionAccountPostingType?: transactionAccountPostingTypes,
    public placeholders?: IPlaceholder[] | null,
    public accountLedger?: ITransactionAccountLedger
  ) {}
}

export function getTransactionAccountCategoryIdentifier(transactionAccountCategory: ITransactionAccountCategory): number | undefined {
  return transactionAccountCategory.id;
}
