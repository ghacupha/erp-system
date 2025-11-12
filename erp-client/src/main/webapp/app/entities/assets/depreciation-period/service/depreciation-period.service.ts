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
import { IDepreciationPeriod, getDepreciationPeriodIdentifier } from '../depreciation-period.model';

export type EntityResponseType = HttpResponse<IDepreciationPeriod>;
export type EntityArrayResponseType = HttpResponse<IDepreciationPeriod[]>;

@Injectable({ providedIn: 'root' })
export class DepreciationPeriodService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/depreciation-periods');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/depreciation-periods');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(depreciationPeriod: IDepreciationPeriod): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(depreciationPeriod);
    return this.http
      .post<IDepreciationPeriod>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(depreciationPeriod: IDepreciationPeriod): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(depreciationPeriod);
    return this.http
      .put<IDepreciationPeriod>(`${this.resourceUrl}/${getDepreciationPeriodIdentifier(depreciationPeriod) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(depreciationPeriod: IDepreciationPeriod): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(depreciationPeriod);
    return this.http
      .patch<IDepreciationPeriod>(`${this.resourceUrl}/${getDepreciationPeriodIdentifier(depreciationPeriod) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IDepreciationPeriod>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IDepreciationPeriod[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IDepreciationPeriod[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  addDepreciationPeriodToCollectionIfMissing(
    depreciationPeriodCollection: IDepreciationPeriod[],
    ...depreciationPeriodsToCheck: (IDepreciationPeriod | null | undefined)[]
  ): IDepreciationPeriod[] {
    const depreciationPeriods: IDepreciationPeriod[] = depreciationPeriodsToCheck.filter(isPresent);
    if (depreciationPeriods.length > 0) {
      const depreciationPeriodCollectionIdentifiers = depreciationPeriodCollection.map(
        depreciationPeriodItem => getDepreciationPeriodIdentifier(depreciationPeriodItem)!
      );
      const depreciationPeriodsToAdd = depreciationPeriods.filter(depreciationPeriodItem => {
        const depreciationPeriodIdentifier = getDepreciationPeriodIdentifier(depreciationPeriodItem);
        if (depreciationPeriodIdentifier == null || depreciationPeriodCollectionIdentifiers.includes(depreciationPeriodIdentifier)) {
          return false;
        }
        depreciationPeriodCollectionIdentifiers.push(depreciationPeriodIdentifier);
        return true;
      });
      return [...depreciationPeriodsToAdd, ...depreciationPeriodCollection];
    }
    return depreciationPeriodCollection;
  }

  protected convertDateFromClient(depreciationPeriod: IDepreciationPeriod): IDepreciationPeriod {
    return Object.assign({}, depreciationPeriod, {
      startDate: depreciationPeriod.startDate?.isValid() ? depreciationPeriod.startDate.format(DATE_FORMAT) : undefined,
      endDate: depreciationPeriod.endDate?.isValid() ? depreciationPeriod.endDate.format(DATE_FORMAT) : undefined,
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
      res.body.forEach((depreciationPeriod: IDepreciationPeriod) => {
        depreciationPeriod.startDate = depreciationPeriod.startDate ? dayjs(depreciationPeriod.startDate) : undefined;
        depreciationPeriod.endDate = depreciationPeriod.endDate ? dayjs(depreciationPeriod.endDate) : undefined;
      });
    }
    return res;
  }
}
