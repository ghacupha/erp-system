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

export interface IRouDepreciationEntryReportItem {
  id?: number;
  leaseContractNumber?: string | null;
  fiscalPeriodCode?: string | null;
  fiscalPeriodEndDate?: dayjs.Dayjs | null;
  assetCategoryName?: string | null;
  debitAccountNumber?: string | null;
  creditAccountNumber?: string | null;
  description?: string | null;
  shortTitle?: string | null;
  rouAssetIdentifier?: string | null;
  sequenceNumber?: number | null;
  depreciationAmount?: number | null;
  outstandingAmount?: number | null;
}

export class RouDepreciationEntryReportItem implements IRouDepreciationEntryReportItem {
  constructor(
    public id?: number,
    public leaseContractNumber?: string | null,
    public fiscalPeriodCode?: string | null,
    public fiscalPeriodEndDate?: dayjs.Dayjs | null,
    public assetCategoryName?: string | null,
    public debitAccountNumber?: string | null,
    public creditAccountNumber?: string | null,
    public description?: string | null,
    public shortTitle?: string | null,
    public rouAssetIdentifier?: string | null,
    public sequenceNumber?: number | null,
    public depreciationAmount?: number | null,
    public outstandingAmount?: number | null
  ) {}
}

export function getRouDepreciationEntryReportItemIdentifier(
  rouDepreciationEntryReportItem: IRouDepreciationEntryReportItem
): number | undefined {
  return rouDepreciationEntryReportItem.id;
}
