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

import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { ILeaseRepaymentPeriod, getLeaseRepaymentPeriodIdentifier } from '../lease-repayment-period.model';

export type EntityResponseType = HttpResponse<ILeaseRepaymentPeriod>;
export type EntityArrayResponseType = HttpResponse<ILeaseRepaymentPeriod[]>;

@Injectable({ providedIn: 'root' })
export class LeaseRepaymentPeriodService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/lease-repayment-periods');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/lease-repayment-periods');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(leaseRepaymentPeriod: ILeaseRepaymentPeriod): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(leaseRepaymentPeriod);
    return this.http
      .post<ILeaseRepaymentPeriod>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(leaseRepaymentPeriod: ILeaseRepaymentPeriod): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(leaseRepaymentPeriod);
    return this.http
      .put<ILeaseRepaymentPeriod>(`${this.resourceUrl}/${getLeaseRepaymentPeriodIdentifier(leaseRepaymentPeriod) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(leaseRepaymentPeriod: ILeaseRepaymentPeriod): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(leaseRepaymentPeriod);
    return this.http
      .patch<ILeaseRepaymentPeriod>(`${this.resourceUrl}/${getLeaseRepaymentPeriodIdentifier(leaseRepaymentPeriod) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ILeaseRepaymentPeriod>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ILeaseRepaymentPeriod[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ILeaseRepaymentPeriod[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  addLeaseRepaymentPeriodToCollectionIfMissing(
    leaseRepaymentPeriodCollection: ILeaseRepaymentPeriod[],
    ...leaseRepaymentPeriodsToCheck: (ILeaseRepaymentPeriod | null | undefined)[]
  ): ILeaseRepaymentPeriod[] {
    const leaseRepaymentPeriods: ILeaseRepaymentPeriod[] = leaseRepaymentPeriodsToCheck.filter(isPresent);
    if (leaseRepaymentPeriods.length > 0) {
      const leaseRepaymentPeriodCollectionIdentifiers = leaseRepaymentPeriodCollection.map(
        leaseRepaymentPeriodItem => getLeaseRepaymentPeriodIdentifier(leaseRepaymentPeriodItem)!
      );
      const leaseRepaymentPeriodsToAdd = leaseRepaymentPeriods.filter(leaseRepaymentPeriodItem => {
        const leaseRepaymentPeriodIdentifier = getLeaseRepaymentPeriodIdentifier(leaseRepaymentPeriodItem);
        if (leaseRepaymentPeriodIdentifier == null || leaseRepaymentPeriodCollectionIdentifiers.includes(leaseRepaymentPeriodIdentifier)) {
          return false;
        }
        leaseRepaymentPeriodCollectionIdentifiers.push(leaseRepaymentPeriodIdentifier);
        return true;
      });
      return [...leaseRepaymentPeriodsToAdd, ...leaseRepaymentPeriodCollection];
    }
    return leaseRepaymentPeriodCollection;
  }

  protected convertDateFromClient(leaseRepaymentPeriod: ILeaseRepaymentPeriod): ILeaseRepaymentPeriod {
    return Object.assign({}, leaseRepaymentPeriod, {
      startDate: leaseRepaymentPeriod.startDate?.isValid() ? leaseRepaymentPeriod.startDate.format(DATE_FORMAT) : undefined,
      endDate: leaseRepaymentPeriod.endDate?.isValid() ? leaseRepaymentPeriod.endDate.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.startDate = res.body.startDate ? dayjs(res.body.startDate) : undefined;
      res.body.endDate = res.body.endDate ? dayjs(res.body.endDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((leaseRepaymentPeriod: ILeaseRepaymentPeriod) => {
        leaseRepaymentPeriod.startDate = leaseRepaymentPeriod.startDate ? dayjs(leaseRepaymentPeriod.startDate) : undefined;
        leaseRepaymentPeriod.endDate = leaseRepaymentPeriod.endDate ? dayjs(leaseRepaymentPeriod.endDate) : undefined;
      });
    }
    return res;
  }
}
