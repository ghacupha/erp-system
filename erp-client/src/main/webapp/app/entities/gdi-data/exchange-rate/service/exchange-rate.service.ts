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
import { IExchangeRate, getExchangeRateIdentifier } from '../exchange-rate.model';

export type EntityResponseType = HttpResponse<IExchangeRate>;
export type EntityArrayResponseType = HttpResponse<IExchangeRate[]>;

@Injectable({ providedIn: 'root' })
export class ExchangeRateService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/exchange-rates');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/exchange-rates');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(exchangeRate: IExchangeRate): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(exchangeRate);
    return this.http
      .post<IExchangeRate>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(exchangeRate: IExchangeRate): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(exchangeRate);
    return this.http
      .put<IExchangeRate>(`${this.resourceUrl}/${getExchangeRateIdentifier(exchangeRate) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(exchangeRate: IExchangeRate): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(exchangeRate);
    return this.http
      .patch<IExchangeRate>(`${this.resourceUrl}/${getExchangeRateIdentifier(exchangeRate) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IExchangeRate>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IExchangeRate[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IExchangeRate[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  addExchangeRateToCollectionIfMissing(
    exchangeRateCollection: IExchangeRate[],
    ...exchangeRatesToCheck: (IExchangeRate | null | undefined)[]
  ): IExchangeRate[] {
    const exchangeRates: IExchangeRate[] = exchangeRatesToCheck.filter(isPresent);
    if (exchangeRates.length > 0) {
      const exchangeRateCollectionIdentifiers = exchangeRateCollection.map(
        exchangeRateItem => getExchangeRateIdentifier(exchangeRateItem)!
      );
      const exchangeRatesToAdd = exchangeRates.filter(exchangeRateItem => {
        const exchangeRateIdentifier = getExchangeRateIdentifier(exchangeRateItem);
        if (exchangeRateIdentifier == null || exchangeRateCollectionIdentifiers.includes(exchangeRateIdentifier)) {
          return false;
        }
        exchangeRateCollectionIdentifiers.push(exchangeRateIdentifier);
        return true;
      });
      return [...exchangeRatesToAdd, ...exchangeRateCollection];
    }
    return exchangeRateCollection;
  }

  protected convertDateFromClient(exchangeRate: IExchangeRate): IExchangeRate {
    return Object.assign({}, exchangeRate, {
      businessReportingDay: exchangeRate.businessReportingDay?.isValid()
        ? exchangeRate.businessReportingDay.format(DATE_FORMAT)
        : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.businessReportingDay = res.body.businessReportingDay ? dayjs(res.body.businessReportingDay) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((exchangeRate: IExchangeRate) => {
        exchangeRate.businessReportingDay = exchangeRate.businessReportingDay ? dayjs(exchangeRate.businessReportingDay) : undefined;
      });
    }
    return res;
  }
}
