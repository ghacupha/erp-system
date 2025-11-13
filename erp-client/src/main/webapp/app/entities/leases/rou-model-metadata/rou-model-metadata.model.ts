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
import { IIFRS16LeaseContract } from 'app/entities/leases/ifrs-16-lease-contract/ifrs-16-lease-contract.model';
import { ITransactionAccount } from 'app/entities/accounting/transaction-account/transaction-account.model';
import { IAssetCategory } from 'app/entities/assets/asset-category/asset-category.model';
import { IBusinessDocument } from 'app/entities/documentation/business-document/business-document.model';

export interface IRouModelMetadata {
  id?: number;
  modelTitle?: string;
  modelVersion?: number;
  description?: string | null;
  leaseTermPeriods?: number;
  leaseAmount?: number;
  rouModelReference?: string;
  commencementDate?: dayjs.Dayjs | null;
  expirationDate?: dayjs.Dayjs | null;
  hasBeenFullyAmortised?: boolean | null;
  hasBeenDecommissioned?: boolean | null;
  ifrs16LeaseContract?: IIFRS16LeaseContract;
  assetAccount?: ITransactionAccount;
  depreciationAccount?: ITransactionAccount;
  accruedDepreciationAccount?: ITransactionAccount;
  assetCategory?: IAssetCategory | null;
  documentAttachments?: IBusinessDocument[] | null;
}

export class RouModelMetadata implements IRouModelMetadata {
  constructor(
    public id?: number,
    public modelTitle?: string,
    public modelVersion?: number,
    public description?: string | null,
    public leaseTermPeriods?: number,
    public leaseAmount?: number,
    public rouModelReference?: string,
    public commencementDate?: dayjs.Dayjs | null,
    public expirationDate?: dayjs.Dayjs | null,
    public hasBeenFullyAmortised?: boolean | null,
    public hasBeenDecommissioned?: boolean | null,
    public ifrs16LeaseContract?: IIFRS16LeaseContract,
    public assetAccount?: ITransactionAccount,
    public depreciationAccount?: ITransactionAccount,
    public accruedDepreciationAccount?: ITransactionAccount,
    public assetCategory?: IAssetCategory | null,
    public documentAttachments?: IBusinessDocument[] | null
  ) {
    this.hasBeenFullyAmortised = this.hasBeenFullyAmortised ?? false;
    this.hasBeenDecommissioned = this.hasBeenDecommissioned ?? false;
  }
}

export function getRouModelMetadataIdentifier(rouModelMetadata: IRouModelMetadata): number | undefined {
  return rouModelMetadata.id;
}
