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
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IWeeklyCashHolding, getWeeklyCashHoldingIdentifier } from '../weekly-cash-holding.model';

export type EntityResponseType = HttpResponse<IWeeklyCashHolding>;
export type EntityArrayResponseType = HttpResponse<IWeeklyCashHolding[]>;

@Injectable({ providedIn: 'root' })
export class WeeklyCashHoldingService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/weekly-cash-holdings');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/weekly-cash-holdings');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(weeklyCashHolding: IWeeklyCashHolding): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(weeklyCashHolding);
    return this.http
      .post<IWeeklyCashHolding>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(weeklyCashHolding: IWeeklyCashHolding): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(weeklyCashHolding);
    return this.http
      .put<IWeeklyCashHolding>(`${this.resourceUrl}/${getWeeklyCashHoldingIdentifier(weeklyCashHolding) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(weeklyCashHolding: IWeeklyCashHolding): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(weeklyCashHolding);
    return this.http
      .patch<IWeeklyCashHolding>(`${this.resourceUrl}/${getWeeklyCashHoldingIdentifier(weeklyCashHolding) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IWeeklyCashHolding>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IWeeklyCashHolding[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IWeeklyCashHolding[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  addWeeklyCashHoldingToCollectionIfMissing(
    weeklyCashHoldingCollection: IWeeklyCashHolding[],
    ...weeklyCashHoldingsToCheck: (IWeeklyCashHolding | null | undefined)[]
  ): IWeeklyCashHolding[] {
    const weeklyCashHoldings: IWeeklyCashHolding[] = weeklyCashHoldingsToCheck.filter(isPresent);
    if (weeklyCashHoldings.length > 0) {
      const weeklyCashHoldingCollectionIdentifiers = weeklyCashHoldingCollection.map(
        weeklyCashHoldingItem => getWeeklyCashHoldingIdentifier(weeklyCashHoldingItem)!
      );
      const weeklyCashHoldingsToAdd = weeklyCashHoldings.filter(weeklyCashHoldingItem => {
        const weeklyCashHoldingIdentifier = getWeeklyCashHoldingIdentifier(weeklyCashHoldingItem);
        if (weeklyCashHoldingIdentifier == null || weeklyCashHoldingCollectionIdentifiers.includes(weeklyCashHoldingIdentifier)) {
          return false;
        }
        weeklyCashHoldingCollectionIdentifiers.push(weeklyCashHoldingIdentifier);
        return true;
      });
      return [...weeklyCashHoldingsToAdd, ...weeklyCashHoldingCollection];
    }
    return weeklyCashHoldingCollection;
  }

  protected convertDateFromClient(weeklyCashHolding: IWeeklyCashHolding): IWeeklyCashHolding {
    return Object.assign({}, weeklyCashHolding, {
      reportingDate: weeklyCashHolding.reportingDate?.isValid() ? weeklyCashHolding.reportingDate.format(DATE_FORMAT) : undefined,
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
      res.body.forEach((weeklyCashHolding: IWeeklyCashHolding) => {
        weeklyCashHolding.reportingDate = weeklyCashHolding.reportingDate ? dayjs(weeklyCashHolding.reportingDate) : undefined;
      });
    }
    return res;
  }
}
