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

import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { ILeaseAmortizationSchedule, getLeaseAmortizationScheduleIdentifier } from '../lease-amortization-schedule.model';

export type EntityResponseType = HttpResponse<ILeaseAmortizationSchedule>;
export type EntityArrayResponseType = HttpResponse<ILeaseAmortizationSchedule[]>;

@Injectable({ providedIn: 'root' })
export class LeaseAmortizationScheduleService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/leases/lease-amortization-schedules');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/leases/_search/lease-amortization-schedules');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(leaseAmortizationSchedule: ILeaseAmortizationSchedule): Observable<EntityResponseType> {
    return this.http.post<ILeaseAmortizationSchedule>(this.resourceUrl, leaseAmortizationSchedule, { observe: 'response' });
  }

  update(leaseAmortizationSchedule: ILeaseAmortizationSchedule): Observable<EntityResponseType> {
    return this.http.put<ILeaseAmortizationSchedule>(
      `${this.resourceUrl}/${getLeaseAmortizationScheduleIdentifier(leaseAmortizationSchedule) as number}`,
      leaseAmortizationSchedule,
      { observe: 'response' }
    );
  }

  partialUpdate(leaseAmortizationSchedule: ILeaseAmortizationSchedule): Observable<EntityResponseType> {
    return this.http.patch<ILeaseAmortizationSchedule>(
      `${this.resourceUrl}/${getLeaseAmortizationScheduleIdentifier(leaseAmortizationSchedule) as number}`,
      leaseAmortizationSchedule,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ILeaseAmortizationSchedule>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ILeaseAmortizationSchedule[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ILeaseAmortizationSchedule[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  addLeaseAmortizationScheduleToCollectionIfMissing(
    leaseAmortizationScheduleCollection: ILeaseAmortizationSchedule[],
    ...leaseAmortizationSchedulesToCheck: (ILeaseAmortizationSchedule | null | undefined)[]
  ): ILeaseAmortizationSchedule[] {
    const leaseAmortizationSchedules: ILeaseAmortizationSchedule[] = leaseAmortizationSchedulesToCheck.filter(isPresent);
    if (leaseAmortizationSchedules.length > 0) {
      const leaseAmortizationScheduleCollectionIdentifiers = leaseAmortizationScheduleCollection.map(
        leaseAmortizationScheduleItem => getLeaseAmortizationScheduleIdentifier(leaseAmortizationScheduleItem)!
      );
      const leaseAmortizationSchedulesToAdd = leaseAmortizationSchedules.filter(leaseAmortizationScheduleItem => {
        const leaseAmortizationScheduleIdentifier = getLeaseAmortizationScheduleIdentifier(leaseAmortizationScheduleItem);
        if (
          leaseAmortizationScheduleIdentifier == null ||
          leaseAmortizationScheduleCollectionIdentifiers.includes(leaseAmortizationScheduleIdentifier)
        ) {
          return false;
        }
        leaseAmortizationScheduleCollectionIdentifiers.push(leaseAmortizationScheduleIdentifier);
        return true;
      });
      return [...leaseAmortizationSchedulesToAdd, ...leaseAmortizationScheduleCollection];
    }
    return leaseAmortizationScheduleCollection;
  }
}
