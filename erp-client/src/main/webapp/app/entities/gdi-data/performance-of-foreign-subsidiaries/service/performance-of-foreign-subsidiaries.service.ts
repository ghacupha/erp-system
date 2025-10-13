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
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import {
  IPerformanceOfForeignSubsidiaries,
  getPerformanceOfForeignSubsidiariesIdentifier,
} from '../performance-of-foreign-subsidiaries.model';

export type EntityResponseType = HttpResponse<IPerformanceOfForeignSubsidiaries>;
export type EntityArrayResponseType = HttpResponse<IPerformanceOfForeignSubsidiaries[]>;

@Injectable({ providedIn: 'root' })
export class PerformanceOfForeignSubsidiariesService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/performance-of-foreign-subsidiaries');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/performance-of-foreign-subsidiaries');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(performanceOfForeignSubsidiaries: IPerformanceOfForeignSubsidiaries): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(performanceOfForeignSubsidiaries);
    return this.http
      .post<IPerformanceOfForeignSubsidiaries>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(performanceOfForeignSubsidiaries: IPerformanceOfForeignSubsidiaries): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(performanceOfForeignSubsidiaries);
    return this.http
      .put<IPerformanceOfForeignSubsidiaries>(
        `${this.resourceUrl}/${getPerformanceOfForeignSubsidiariesIdentifier(performanceOfForeignSubsidiaries) as number}`,
        copy,
        { observe: 'response' }
      )
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(performanceOfForeignSubsidiaries: IPerformanceOfForeignSubsidiaries): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(performanceOfForeignSubsidiaries);
    return this.http
      .patch<IPerformanceOfForeignSubsidiaries>(
        `${this.resourceUrl}/${getPerformanceOfForeignSubsidiariesIdentifier(performanceOfForeignSubsidiaries) as number}`,
        copy,
        { observe: 'response' }
      )
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IPerformanceOfForeignSubsidiaries>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IPerformanceOfForeignSubsidiaries[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IPerformanceOfForeignSubsidiaries[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  addPerformanceOfForeignSubsidiariesToCollectionIfMissing(
    performanceOfForeignSubsidiariesCollection: IPerformanceOfForeignSubsidiaries[],
    ...performanceOfForeignSubsidiariesToCheck: (IPerformanceOfForeignSubsidiaries | null | undefined)[]
  ): IPerformanceOfForeignSubsidiaries[] {
    const performanceOfForeignSubsidiaries: IPerformanceOfForeignSubsidiaries[] = performanceOfForeignSubsidiariesToCheck.filter(isPresent);
    if (performanceOfForeignSubsidiaries.length > 0) {
      const performanceOfForeignSubsidiariesCollectionIdentifiers = performanceOfForeignSubsidiariesCollection.map(
        performanceOfForeignSubsidiariesItem => getPerformanceOfForeignSubsidiariesIdentifier(performanceOfForeignSubsidiariesItem)!
      );
      const performanceOfForeignSubsidiariesToAdd = performanceOfForeignSubsidiaries.filter(performanceOfForeignSubsidiariesItem => {
        const performanceOfForeignSubsidiariesIdentifier = getPerformanceOfForeignSubsidiariesIdentifier(
          performanceOfForeignSubsidiariesItem
        );
        if (
          performanceOfForeignSubsidiariesIdentifier == null ||
          performanceOfForeignSubsidiariesCollectionIdentifiers.includes(performanceOfForeignSubsidiariesIdentifier)
        ) {
          return false;
        }
        performanceOfForeignSubsidiariesCollectionIdentifiers.push(performanceOfForeignSubsidiariesIdentifier);
        return true;
      });
      return [...performanceOfForeignSubsidiariesToAdd, ...performanceOfForeignSubsidiariesCollection];
    }
    return performanceOfForeignSubsidiariesCollection;
  }

  protected convertDateFromClient(performanceOfForeignSubsidiaries: IPerformanceOfForeignSubsidiaries): IPerformanceOfForeignSubsidiaries {
    return Object.assign({}, performanceOfForeignSubsidiaries, {
      reportingDate: performanceOfForeignSubsidiaries.reportingDate?.isValid()
        ? performanceOfForeignSubsidiaries.reportingDate.format(DATE_FORMAT)
        : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.reportingDate = res.body.reportingDate ? dayjs(res.body.reportingDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((performanceOfForeignSubsidiaries: IPerformanceOfForeignSubsidiaries) => {
        performanceOfForeignSubsidiaries.reportingDate = performanceOfForeignSubsidiaries.reportingDate
          ? dayjs(performanceOfForeignSubsidiaries.reportingDate)
          : undefined;
      });
    }
    return res;
  }
}
