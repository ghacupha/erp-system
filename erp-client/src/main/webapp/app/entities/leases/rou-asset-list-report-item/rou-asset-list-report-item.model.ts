///
/// Erp System - Mark X No 11 (Jehoiada Series) Client 1.7.8
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

export interface IRouAssetListReportItem {
  id?: number;
  modelTitle?: string | null;
  modelVersion?: number | null;
  description?: string | null;
  leaseTermPeriods?: number | null;
  rouModelReference?: string | null;
  commencementDate?: dayjs.Dayjs | null;
  expirationDate?: dayjs.Dayjs | null;
  leaseContractTitle?: string | null;
  assetAccountNumber?: string | null;
  depreciationAccountNumber?: string | null;
  accruedDepreciationAccountNumber?: string | null;
  assetCategoryName?: string | null;
  leaseAmount?: number | null;
  leaseContractSerialNumber?: string | null;
}

export class RouAssetListReportItem implements IRouAssetListReportItem {
  constructor(
    public id?: number,
    public modelTitle?: string | null,
    public modelVersion?: number | null,
    public description?: string | null,
    public leaseTermPeriods?: number | null,
    public rouModelReference?: string | null,
    public commencementDate?: dayjs.Dayjs | null,
    public expirationDate?: dayjs.Dayjs | null,
    public leaseContractTitle?: string | null,
    public assetAccountNumber?: string | null,
    public depreciationAccountNumber?: string | null,
    public accruedDepreciationAccountNumber?: string | null,
    public assetCategoryName?: string | null,
    public leaseAmount?: number | null,
    public leaseContractSerialNumber?: string | null
  ) {}
}

export function getRouAssetListReportItemIdentifier(rouAssetListReportItem: IRouAssetListReportItem): number | undefined {
  return rouAssetListReportItem.id;
}
