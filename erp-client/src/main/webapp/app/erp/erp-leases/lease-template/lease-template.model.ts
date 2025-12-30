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

import { ITransactionAccount } from '../../erp-accounts/transaction-account/transaction-account.model';
import { IAssetCategory } from '../../erp-assets/asset-category/asset-category.model';
import { IServiceOutlet } from '../../erp-granular/service-outlet/service-outlet.model';
import { IDealer } from '../../erp-pages/dealers/dealer/dealer.model';

export interface ILeaseTemplate {
  id?: number;
  templateTitle?: string;
  assetAccount?: ITransactionAccount | null;
  depreciationAccount?: ITransactionAccount | null;
  accruedDepreciationAccount?: ITransactionAccount | null;
  interestPaidTransferDebitAccount?: ITransactionAccount | null;
  interestPaidTransferCreditAccount?: ITransactionAccount | null;
  interestAccruedDebitAccount?: ITransactionAccount | null;
  interestAccruedCreditAccount?: ITransactionAccount | null;
  leaseRecognitionDebitAccount?: ITransactionAccount | null;
  leaseRecognitionCreditAccount?: ITransactionAccount | null;
  leaseRepaymentDebitAccount?: ITransactionAccount | null;
  leaseRepaymentCreditAccount?: ITransactionAccount | null;
  rouRecognitionCreditAccount?: ITransactionAccount | null;
  rouRecognitionDebitAccount?: ITransactionAccount | null;
  assetCategory?: IAssetCategory | null;
  serviceOutlet?: IServiceOutlet | null;
  mainDealer?: IDealer | null;
}

export class LeaseTemplate implements ILeaseTemplate {
  constructor(
    public id?: number,
    public templateTitle?: string,
    public assetAccount?: ITransactionAccount | null,
    public depreciationAccount?: ITransactionAccount | null,
    public accruedDepreciationAccount?: ITransactionAccount | null,
    public interestPaidTransferDebitAccount?: ITransactionAccount | null,
    public interestPaidTransferCreditAccount?: ITransactionAccount | null,
    public interestAccruedDebitAccount?: ITransactionAccount | null,
    public interestAccruedCreditAccount?: ITransactionAccount | null,
    public leaseRecognitionDebitAccount?: ITransactionAccount | null,
    public leaseRecognitionCreditAccount?: ITransactionAccount | null,
    public leaseRepaymentDebitAccount?: ITransactionAccount | null,
    public leaseRepaymentCreditAccount?: ITransactionAccount | null,
    public rouRecognitionCreditAccount?: ITransactionAccount | null,
    public rouRecognitionDebitAccount?: ITransactionAccount | null,
    public assetCategory?: IAssetCategory | null,
    public serviceOutlet?: IServiceOutlet | null,
    public mainDealer?: IDealer | null
  ) {}
}

export function getLeaseTemplateIdentifier(leaseTemplate: ILeaseTemplate): number | undefined {
  return leaseTemplate.id;
}
