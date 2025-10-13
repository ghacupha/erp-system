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

import { IIFRS16LeaseContract } from '../../erp-leases/ifrs-16-lease-contract/ifrs-16-lease-contract.model';
import { IPlaceholder } from '../../erp-pages/placeholder/placeholder.model';
import { ITransactionAccount } from '../transaction-account/transaction-account.model';

export interface ITAAmortizationRule {
  id?: number;
  name?: string;
  identifier?: string;
  leaseContract?: IIFRS16LeaseContract;
  debit?: ITransactionAccount;
  credit?: ITransactionAccount;
  placeholders?: IPlaceholder[] | null;
}

export class TAAmortizationRule implements ITAAmortizationRule {
  constructor(
    public id?: number,
    public name?: string,
    public identifier?: string,
    public leaseContract?: IIFRS16LeaseContract,
    public debit?: ITransactionAccount,
    public credit?: ITransactionAccount,
    public placeholders?: IPlaceholder[] | null
  ) {}
}

export function getTAAmortizationRuleIdentifier(tAAmortizationRule: ITAAmortizationRule): number | undefined {
  return tAAmortizationRule.id;
}
