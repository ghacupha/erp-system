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
import { IRouAccountBalanceReportItem, getRouAccountBalanceReportItemIdentifier } from '../rou-account-balance-report-item.model';

export type EntityResponseType = HttpResponse<IRouAccountBalanceReportItem>;
export type EntityArrayResponseType = HttpResponse<IRouAccountBalanceReportItem[]>;

@Injectable({ providedIn: 'root' })
export class RouAccountBalanceReportItemService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/leases/rou-account-balance-report-items');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/leases/_search/rou-account-balance-report-items');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IRouAccountBalanceReportItem>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(leasePeriodId:number, req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IRouAccountBalanceReportItem[]>(`${this.resourceUrl}/reports/${leasePeriodId}`, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IRouAccountBalanceReportItem[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  addRouAccountBalanceReportItemToCollectionIfMissing(
    rouAccountBalanceReportItemCollection: IRouAccountBalanceReportItem[],
    ...rouAccountBalanceReportItemsToCheck: (IRouAccountBalanceReportItem | null | undefined)[]
  ): IRouAccountBalanceReportItem[] {
    const rouAccountBalanceReportItems: IRouAccountBalanceReportItem[] = rouAccountBalanceReportItemsToCheck.filter(isPresent);
    if (rouAccountBalanceReportItems.length > 0) {
      const rouAccountBalanceReportItemCollectionIdentifiers = rouAccountBalanceReportItemCollection.map(
        rouAccountBalanceReportItemItem => getRouAccountBalanceReportItemIdentifier(rouAccountBalanceReportItemItem)!
      );
      const rouAccountBalanceReportItemsToAdd = rouAccountBalanceReportItems.filter(rouAccountBalanceReportItemItem => {
        const rouAccountBalanceReportItemIdentifier = getRouAccountBalanceReportItemIdentifier(rouAccountBalanceReportItemItem);
        if (
          rouAccountBalanceReportItemIdentifier == null ||
          rouAccountBalanceReportItemCollectionIdentifiers.includes(rouAccountBalanceReportItemIdentifier)
        ) {
          return false;
        }
        rouAccountBalanceReportItemCollectionIdentifiers.push(rouAccountBalanceReportItemIdentifier);
        return true;
      });
      return [...rouAccountBalanceReportItemsToAdd, ...rouAccountBalanceReportItemCollection];
    }
    return rouAccountBalanceReportItemCollection;
  }

  protected convertDateFromClient(rouAccountBalanceReportItem: IRouAccountBalanceReportItem): IRouAccountBalanceReportItem {
    return Object.assign({}, rouAccountBalanceReportItem, {
      fiscalPeriodEndDate: rouAccountBalanceReportItem.fiscalPeriodEndDate?.isValid()
        ? rouAccountBalanceReportItem.fiscalPeriodEndDate.format(DATE_FORMAT)
        : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.fiscalPeriodEndDate = res.body.fiscalPeriodEndDate ? dayjs(res.body.fiscalPeriodEndDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((rouAccountBalanceReportItem: IRouAccountBalanceReportItem) => {
        rouAccountBalanceReportItem.fiscalPeriodEndDate = rouAccountBalanceReportItem.fiscalPeriodEndDate
          ? dayjs(rouAccountBalanceReportItem.fiscalPeriodEndDate)
          : undefined;
      });
    }
    return res;
  }
}
