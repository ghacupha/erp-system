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

import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { map, Observable } from 'rxjs';
import * as dayjs from 'dayjs';

import { ApplicationConfigService } from 'app/core/config/application-config.service';

export interface RouDepreciationScheduleRow {
  entryId?: number;
  sequenceNumber?: number;
  leaseNumber?: string;
  periodCode?: string;
  periodStartDate?: dayjs.Dayjs;
  periodEndDate?: dayjs.Dayjs;
  initialAmount?: number;
  depreciationAmount?: number;
  outstandingAmount?: number;
}

interface RestRouDepreciationScheduleRow extends Omit<RouDepreciationScheduleRow, 'periodStartDate' | 'periodEndDate'> {
  periodStartDate?: string | null;
  periodEndDate?: string | null;
}

@Injectable({ providedIn: 'root' })
export class RouDepreciationScheduleViewService {
  private readonly resourceUrl = this.applicationConfigService.getEndpointFor('api/leases/rou-depreciation-schedule-view');

  constructor(private http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  loadSchedule(leaseContractId: number): Observable<RouDepreciationScheduleRow[]> {
    return this.http
      .get<RestRouDepreciationScheduleRow[]>(`${this.resourceUrl}/${leaseContractId}`)
      .pipe(map(rows => rows.map(row => this.convertDateFromServer(row))));
  }

  private convertDateFromServer(row: RestRouDepreciationScheduleRow): RouDepreciationScheduleRow {
    return {
      ...row,
      periodStartDate: row.periodStartDate ? dayjs(row.periodStartDate) : undefined,
      periodEndDate: row.periodEndDate ? dayjs(row.periodEndDate) : undefined,
    };
  }
}
