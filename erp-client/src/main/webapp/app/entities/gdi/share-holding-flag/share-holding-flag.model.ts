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

import { ShareholdingFlagTypes } from 'app/entities/enumerations/shareholding-flag-types.model';

export interface IShareHoldingFlag {
  id?: number;
  shareholdingFlagTypeCode?: ShareholdingFlagTypes;
  shareholdingFlagType?: string;
  shareholdingTypeDescription?: string | null;
}

export class ShareHoldingFlag implements IShareHoldingFlag {
  constructor(
    public id?: number,
    public shareholdingFlagTypeCode?: ShareholdingFlagTypes,
    public shareholdingFlagType?: string,
    public shareholdingTypeDescription?: string | null
  ) {}
}

export function getShareHoldingFlagIdentifier(shareHoldingFlag: IShareHoldingFlag): number | undefined {
  return shareHoldingFlag.id;
}
