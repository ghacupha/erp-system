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

import { ISettlementCurrency } from 'app/entities/system/settlement-currency/settlement-currency.model';
import { ITransactionAccount } from 'app/entities/accounting/transaction-account/transaction-account.model';

export interface IReportingEntity {
  id?: number;
  entityName?: string;
  reportingCurrency?: ISettlementCurrency | null;
  retainedEarningsAccount?: ITransactionAccount | null;
}

export class ReportingEntity implements IReportingEntity {
  constructor(
    public id?: number,
    public entityName?: string,
    public reportingCurrency?: ISettlementCurrency | null,
    public retainedEarningsAccount?: ITransactionAccount | null
  ) {}
}

export function getReportingEntityIdentifier(reportingEntity: IReportingEntity): number | undefined {
  return reportingEntity.id;
}
