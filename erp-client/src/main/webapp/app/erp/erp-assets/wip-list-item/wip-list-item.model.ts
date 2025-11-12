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

export interface IWIPListItem {
  id?: number;
  sequenceNumber?: string | null;
  particulars?: string | null;
  instalmentDate?: dayjs.Dayjs | null;
  instalmentAmount?: number | null;
  settlementCurrency?: string | null;
  outletCode?: string | null;
  settlementTransaction?: string | null;
  settlementTransactionDate?: dayjs.Dayjs | null;
  dealerName?: string | null;
  workProject?: string | null;
}

export class WIPListItem implements IWIPListItem {
  constructor(
    public id?: number,
    public sequenceNumber?: string | null,
    public particulars?: string | null,
    public instalmentDate?: dayjs.Dayjs | null,
    public instalmentAmount?: number | null,
    public settlementCurrency?: string | null,
    public outletCode?: string | null,
    public settlementTransaction?: string | null,
    public settlementTransactionDate?: dayjs.Dayjs | null,
    public dealerName?: string | null,
    public workProject?: string | null
  ) {}
}

export function getWIPListItemIdentifier(wIPListItem: IWIPListItem): number | undefined {
  return wIPListItem.id;
}
