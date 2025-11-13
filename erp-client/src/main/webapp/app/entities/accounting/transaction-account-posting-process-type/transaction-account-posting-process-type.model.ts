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

import { ITransactionAccountCategory } from 'app/entities/accounting/transaction-account-category/transaction-account-category.model';
import { IPlaceholder } from 'app/entities/system/placeholder/placeholder.model';

export interface ITransactionAccountPostingProcessType {
  id?: number;
  name?: string;
  debitAccountType?: ITransactionAccountCategory;
  creditAccountType?: ITransactionAccountCategory;
  placeholders?: IPlaceholder[] | null;
}

export class TransactionAccountPostingProcessType implements ITransactionAccountPostingProcessType {
  constructor(
    public id?: number,
    public name?: string,
    public debitAccountType?: ITransactionAccountCategory,
    public creditAccountType?: ITransactionAccountCategory,
    public placeholders?: IPlaceholder[] | null
  ) {}
}

export function getTransactionAccountPostingProcessTypeIdentifier(
  transactionAccountPostingProcessType: ITransactionAccountPostingProcessType
): number | undefined {
  return transactionAccountPostingProcessType.id;
}
