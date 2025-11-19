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
import { Observable, of } from 'rxjs';
import { ApplicationConfigService } from '../../../../core/config/application-config.service';
import { createRequestOption } from '../../../../core/request/request-util';
import { ASC, DESC } from '../../../../config/pagination.constants';
import { ILeaseAmortizationSchedule } from '../../../erp-leases/lease-amortization-schedule/lease-amortization-schedule.model';

@Injectable({ providedIn: 'root' })
export class LeaseAmortizationScheduleSuggestionService {
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/leases/_search/lease-amortization-schedules');

  constructor(private http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  search(searchText: string): Observable<ILeaseAmortizationSchedule[]> {
    if (!searchText) {
      return of([]);
    }

    return this.http.get<ILeaseAmortizationSchedule[]>(this.resourceSearchUrl, {
      params: createRequestOption({
        query: searchText,
        page: 0,
        size: 10,
        sort: this.sort(),
      }),
    });
  }

  private sort(): string[] {
    const predicate = 'id';
    const ascending = true;
    const result = [predicate + ',' + (ascending ? ASC : DESC)];
    if (predicate !== 'id') {
      result.push('id');
    }
    return result;
  }
}
