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

import { ITransactionAccountLedger } from 'app/entities/accounting/transaction-account-ledger/transaction-account-ledger.model';
import { ITransactionAccountCategory } from 'app/entities/accounting/transaction-account-category/transaction-account-category.model';
import { IPlaceholder } from 'app/entities/system/placeholder/placeholder.model';
import { IServiceOutlet } from 'app/entities/system/service-outlet/service-outlet.model';
import { ISettlementCurrency } from 'app/entities/system/settlement-currency/settlement-currency.model';
import { IReportingEntity } from 'app/entities/admin/reporting-entity/reporting-entity.model';
import { AccountTypes } from 'app/entities/enumerations/account-types.model';
import { AccountSubTypes } from 'app/entities/enumerations/account-sub-types.model';

export interface ITransactionAccount {
  id?: number;
  accountNumber?: string;
  accountName?: string;
  notesContentType?: string | null;
  notes?: string | null;
  accountType?: AccountTypes;
  accountSubType?: AccountSubTypes;
  dummyAccount?: boolean | null;
  accountLedger?: ITransactionAccountLedger;
  accountCategory?: ITransactionAccountCategory;
  placeholders?: IPlaceholder[] | null;
  serviceOutlet?: IServiceOutlet;
  settlementCurrency?: ISettlementCurrency;
  institution?: IReportingEntity;
}

export class TransactionAccount implements ITransactionAccount {
  constructor(
    public id?: number,
    public accountNumber?: string,
    public accountName?: string,
    public notesContentType?: string | null,
    public notes?: string | null,
    public accountType?: AccountTypes,
    public accountSubType?: AccountSubTypes,
    public dummyAccount?: boolean | null,
    public accountLedger?: ITransactionAccountLedger,
    public accountCategory?: ITransactionAccountCategory,
    public placeholders?: IPlaceholder[] | null,
    public serviceOutlet?: IServiceOutlet,
    public settlementCurrency?: ISettlementCurrency,
    public institution?: IReportingEntity
  ) {
    this.dummyAccount = this.dummyAccount ?? false;
  }
}

export function getTransactionAccountIdentifier(transactionAccount: ITransactionAccount): number | undefined {
  return transactionAccount.id;
}
