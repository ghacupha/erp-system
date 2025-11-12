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
import { IWeeklyCounterfeitHolding, getWeeklyCounterfeitHoldingIdentifier } from '../weekly-counterfeit-holding.model';

export type EntityResponseType = HttpResponse<IWeeklyCounterfeitHolding>;
export type EntityArrayResponseType = HttpResponse<IWeeklyCounterfeitHolding[]>;

@Injectable({ providedIn: 'root' })
export class WeeklyCounterfeitHoldingService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/weekly-counterfeit-holdings');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/weekly-counterfeit-holdings');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(weeklyCounterfeitHolding: IWeeklyCounterfeitHolding): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(weeklyCounterfeitHolding);
    return this.http
      .post<IWeeklyCounterfeitHolding>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(weeklyCounterfeitHolding: IWeeklyCounterfeitHolding): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(weeklyCounterfeitHolding);
    return this.http
      .put<IWeeklyCounterfeitHolding>(
        `${this.resourceUrl}/${getWeeklyCounterfeitHoldingIdentifier(weeklyCounterfeitHolding) as number}`,
        copy,
        { observe: 'response' }
      )
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(weeklyCounterfeitHolding: IWeeklyCounterfeitHolding): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(weeklyCounterfeitHolding);
    return this.http
      .patch<IWeeklyCounterfeitHolding>(
        `${this.resourceUrl}/${getWeeklyCounterfeitHoldingIdentifier(weeklyCounterfeitHolding) as number}`,
        copy,
        { observe: 'response' }
      )
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IWeeklyCounterfeitHolding>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IWeeklyCounterfeitHolding[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IWeeklyCounterfeitHolding[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  addWeeklyCounterfeitHoldingToCollectionIfMissing(
    weeklyCounterfeitHoldingCollection: IWeeklyCounterfeitHolding[],
    ...weeklyCounterfeitHoldingsToCheck: (IWeeklyCounterfeitHolding | null | undefined)[]
  ): IWeeklyCounterfeitHolding[] {
    const weeklyCounterfeitHoldings: IWeeklyCounterfeitHolding[] = weeklyCounterfeitHoldingsToCheck.filter(isPresent);
    if (weeklyCounterfeitHoldings.length > 0) {
      const weeklyCounterfeitHoldingCollectionIdentifiers = weeklyCounterfeitHoldingCollection.map(
        weeklyCounterfeitHoldingItem => getWeeklyCounterfeitHoldingIdentifier(weeklyCounterfeitHoldingItem)!
      );
      const weeklyCounterfeitHoldingsToAdd = weeklyCounterfeitHoldings.filter(weeklyCounterfeitHoldingItem => {
        const weeklyCounterfeitHoldingIdentifier = getWeeklyCounterfeitHoldingIdentifier(weeklyCounterfeitHoldingItem);
        if (
          weeklyCounterfeitHoldingIdentifier == null ||
          weeklyCounterfeitHoldingCollectionIdentifiers.includes(weeklyCounterfeitHoldingIdentifier)
        ) {
          return false;
        }
        weeklyCounterfeitHoldingCollectionIdentifiers.push(weeklyCounterfeitHoldingIdentifier);
        return true;
      });
      return [...weeklyCounterfeitHoldingsToAdd, ...weeklyCounterfeitHoldingCollection];
    }
    return weeklyCounterfeitHoldingCollection;
  }

  protected convertDateFromClient(weeklyCounterfeitHolding: IWeeklyCounterfeitHolding): IWeeklyCounterfeitHolding {
    return Object.assign({}, weeklyCounterfeitHolding, {
      reportingDate: weeklyCounterfeitHolding.reportingDate?.isValid()
        ? weeklyCounterfeitHolding.reportingDate.format(DATE_FORMAT)
        : undefined,
      dateConfiscated: weeklyCounterfeitHolding.dateConfiscated?.isValid()
        ? weeklyCounterfeitHolding.dateConfiscated.format(DATE_FORMAT)
        : undefined,
      dateSubmittedToCBK: weeklyCounterfeitHolding.dateSubmittedToCBK?.isValid()
        ? weeklyCounterfeitHolding.dateSubmittedToCBK.format(DATE_FORMAT)
        : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.reportingDate = res.body.reportingDate ? dayjs(res.body.reportingDate) : undefined;
      res.body.dateConfiscated = res.body.dateConfiscated ? dayjs(res.body.dateConfiscated) : undefined;
      res.body.dateSubmittedToCBK = res.body.dateSubmittedToCBK ? dayjs(res.body.dateSubmittedToCBK) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((weeklyCounterfeitHolding: IWeeklyCounterfeitHolding) => {
        weeklyCounterfeitHolding.reportingDate = weeklyCounterfeitHolding.reportingDate
          ? dayjs(weeklyCounterfeitHolding.reportingDate)
          : undefined;
        weeklyCounterfeitHolding.dateConfiscated = weeklyCounterfeitHolding.dateConfiscated
          ? dayjs(weeklyCounterfeitHolding.dateConfiscated)
          : undefined;
        weeklyCounterfeitHolding.dateSubmittedToCBK = weeklyCounterfeitHolding.dateSubmittedToCBK
          ? dayjs(weeklyCounterfeitHolding.dateSubmittedToCBK)
          : undefined;
      });
    }
    return res;
  }
}
