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
import { HttpClient, HttpParams } from '@angular/common/http';
import { map } from 'rxjs/operators';
import { Observable } from 'rxjs';
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

type RestRouDepreciationScheduleRow =
  | (Omit<RouDepreciationScheduleRow, 'periodStartDate' | 'periodEndDate'> & {
      periodStartDate?: string | null;
      periodEndDate?: string | null;
    })
  | {
      entry_id?: number;
      sequence_number?: number;
      lease_number?: string;
      period_code?: string;
      period_start_date?: string | null;
      period_end_date?: string | null;
      initial_amount?: number;
      depreciation_amount?: number;
      outstanding_amount?: number;
    };

type RestCamelRouDepreciationScheduleRow = Omit<RouDepreciationScheduleRow, 'periodStartDate' | 'periodEndDate'> & {
  periodStartDate?: string | null;
  periodEndDate?: string | null;
};

type RestSnakeRouDepreciationScheduleRow = {
  entry_id?: number;
  sequence_number?: number;
  lease_number?: string;
  period_code?: string;
  period_start_date?: string | null;
  period_end_date?: string | null;
  initial_amount?: number;
  depreciation_amount?: number;
  outstanding_amount?: number;
};

@Injectable({ providedIn: 'root' })
export class RouDepreciationScheduleViewService {
  private readonly resourceUrl = this.applicationConfigService.getEndpointFor('api/leases/rou-depreciation-schedule-view');

  constructor(private http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  loadSchedule(leaseContractId: number, asAtDate?: dayjs.Dayjs): Observable<RouDepreciationScheduleRow[]> {
    let params = new HttpParams();

    if (asAtDate) {
      params = params.set('asAtDate', asAtDate.format('YYYY-MM-DD'));
    }

    return this.http
      .get<RestRouDepreciationScheduleRow[]>(`${this.resourceUrl}/${leaseContractId}`, { params })
      .pipe(map(rows => rows.map(row => this.convertFromServer(row))));
  }

  private convertFromServer(row: RestRouDepreciationScheduleRow): RouDepreciationScheduleRow {
    if (this.isSnakeCaseRow(row)) {
      return {
        entryId: row.entry_id,
        sequenceNumber: row.sequence_number,
        leaseNumber: row.lease_number,
        periodCode: row.period_code,
        periodStartDate: this.convertDate(row.period_start_date),
        periodEndDate: this.convertDate(row.period_end_date),
        initialAmount: row.initial_amount,
        depreciationAmount: row.depreciation_amount,
        outstandingAmount: row.outstanding_amount,
      };
    }

    const camelCaseRow = row as RestCamelRouDepreciationScheduleRow;

    return {
      ...camelCaseRow,
      periodStartDate: this.convertDate(camelCaseRow.periodStartDate),
      periodEndDate: this.convertDate(camelCaseRow.periodEndDate),
    };
  }

  private isSnakeCaseRow(row: RestRouDepreciationScheduleRow): row is RestSnakeRouDepreciationScheduleRow {
    return 'entry_id' in row || 'sequence_number' in row;
  }

  private convertDate(value?: string | null): dayjs.Dayjs | undefined {
    return value ? dayjs(value) : undefined;
  }
}
