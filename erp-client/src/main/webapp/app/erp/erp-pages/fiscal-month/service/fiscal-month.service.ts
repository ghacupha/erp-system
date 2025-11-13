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
import { HttpClient, HttpParams, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IFiscalMonth, getFiscalMonthIdentifier } from '../fiscal-month.model';

export type EntityResponseType = HttpResponse<IFiscalMonth>;
export type EntityArrayResponseType = HttpResponse<IFiscalMonth[]>;

@Injectable({ providedIn: 'root' })
export class FiscalMonthService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/app/fiscal-months');
  protected resourceLapsedPeriodsUrl = this.applicationConfigService.getEndpointFor('api/app/fiscal-months/lapsed-period');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/app/_search/fiscal-months');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(fiscalMonth: IFiscalMonth): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(fiscalMonth);
    return this.http
      .post<IFiscalMonth>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(fiscalMonth: IFiscalMonth): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(fiscalMonth);
    return this.http
      .put<IFiscalMonth>(`${this.resourceUrl}/${getFiscalMonthIdentifier(fiscalMonth) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(fiscalMonth: IFiscalMonth): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(fiscalMonth);
    return this.http
      .patch<IFiscalMonth>(`${this.resourceUrl}/${getFiscalMonthIdentifier(fiscalMonth) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IFiscalMonth>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IFiscalMonth[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  /**
   * You have a specified fiscal-month, and this method provides the fiscal-month instance after
   * the specified number of fiscal-periods have lapsed
   * @param currentFiscalMonthId id of the current fiscal month
   * @param fiscalPeriods number of periods lapsed
   */
  findFiscalMonthAfterGivenPeriods(currentFiscalMonthId: number, fiscalPeriods: number): Observable<EntityResponseType> {

    const req = {
      currentFiscalMonthId,
      fiscalPeriods
    }
    const options: HttpParams = createRequestOption(req);

    return this.http
      .get<IFiscalMonth>(this.resourceLapsedPeriodsUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IFiscalMonth[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  addFiscalMonthToCollectionIfMissing(
    fiscalMonthCollection: IFiscalMonth[],
    ...fiscalMonthsToCheck: (IFiscalMonth | null | undefined)[]
  ): IFiscalMonth[] {
    const fiscalMonths: IFiscalMonth[] = fiscalMonthsToCheck.filter(isPresent);
    if (fiscalMonths.length > 0) {
      const fiscalMonthCollectionIdentifiers = fiscalMonthCollection.map(fiscalMonthItem => getFiscalMonthIdentifier(fiscalMonthItem)!);
      const fiscalMonthsToAdd = fiscalMonths.filter(fiscalMonthItem => {
        const fiscalMonthIdentifier = getFiscalMonthIdentifier(fiscalMonthItem);
        if (fiscalMonthIdentifier == null || fiscalMonthCollectionIdentifiers.includes(fiscalMonthIdentifier)) {
          return false;
        }
        fiscalMonthCollectionIdentifiers.push(fiscalMonthIdentifier);
        return true;
      });
      return [...fiscalMonthsToAdd, ...fiscalMonthCollection];
    }
    return fiscalMonthCollection;
  }

  protected convertDateFromClient(fiscalMonth: IFiscalMonth): IFiscalMonth {
    return Object.assign({}, fiscalMonth, {
      startDate: fiscalMonth.startDate?.isValid() ? fiscalMonth.startDate.format(DATE_FORMAT) : undefined,
      endDate: fiscalMonth.endDate?.isValid() ? fiscalMonth.endDate.format(DATE_FORMAT) : undefined,
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
      res.body.forEach((fiscalMonth: IFiscalMonth) => {
        fiscalMonth.startDate = fiscalMonth.startDate ? dayjs(fiscalMonth.startDate) : undefined;
        fiscalMonth.endDate = fiscalMonth.endDate ? dayjs(fiscalMonth.endDate) : undefined;
      });
    }
    return res;
  }
}
