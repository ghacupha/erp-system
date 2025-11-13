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

import { ITransactionAccount } from 'app/entities/accounting/transaction-account/transaction-account.model';
import { IAssetCategory } from 'app/entities/assets/asset-category/asset-category.model';
import { IIFRS16LeaseContract } from 'app/entities/leases/ifrs-16-lease-contract/ifrs-16-lease-contract.model';
import { IRouModelMetadata } from 'app/entities/leases/rou-model-metadata/rou-model-metadata.model';

export interface IRouDepreciationEntry {
  id?: number;
  description?: string | null;
  depreciationAmount?: number;
  outstandingAmount?: number;
  rouAssetIdentifier?: string | null;
  rouDepreciationIdentifier?: string;
  sequenceNumber?: number | null;
  invalidated?: boolean | null;
  debitAccount?: ITransactionAccount;
  creditAccount?: ITransactionAccount;
  assetCategory?: IAssetCategory;
  leaseContract?: IIFRS16LeaseContract;
  rouMetadata?: IRouModelMetadata;
}

export class RouDepreciationEntry implements IRouDepreciationEntry {
  constructor(
    public id?: number,
    public description?: string | null,
    public depreciationAmount?: number,
    public outstandingAmount?: number,
    public rouAssetIdentifier?: string | null,
    public rouDepreciationIdentifier?: string,
    public sequenceNumber?: number | null,
    public invalidated?: boolean | null,
    public debitAccount?: ITransactionAccount,
    public creditAccount?: ITransactionAccount,
    public assetCategory?: IAssetCategory,
    public leaseContract?: IIFRS16LeaseContract,
    public rouMetadata?: IRouModelMetadata
  ) {
    this.invalidated = this.invalidated ?? false;
  }
}

export function getRouDepreciationEntryIdentifier(rouDepreciationEntry: IRouDepreciationEntry): number | undefined {
  return rouDepreciationEntry.id;
}
