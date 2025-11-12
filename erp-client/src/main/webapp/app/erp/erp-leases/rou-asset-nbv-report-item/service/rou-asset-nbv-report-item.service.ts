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
import { IRouAssetNBVReportItem, getRouAssetNBVReportItemIdentifier } from '../rou-asset-nbv-report-item.model';

export type EntityResponseType = HttpResponse<IRouAssetNBVReportItem>;
export type EntityArrayResponseType = HttpResponse<IRouAssetNBVReportItem[]>;

@Injectable({ providedIn: 'root' })
export class RouAssetNBVReportItemService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/leases/rou-asset-nbv-report-items');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/leases/_search/rou-asset-nbv-report-items');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IRouAssetNBVReportItem>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(leasePeriodId: number, req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IRouAssetNBVReportItem[]>(`${this.resourceUrl}/reports/${leasePeriodId}`, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IRouAssetNBVReportItem[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  addRouAssetNBVReportItemToCollectionIfMissing(
    rouAssetNBVReportItemCollection: IRouAssetNBVReportItem[],
    ...rouAssetNBVReportItemsToCheck: (IRouAssetNBVReportItem | null | undefined)[]
  ): IRouAssetNBVReportItem[] {
    const rouAssetNBVReportItems: IRouAssetNBVReportItem[] = rouAssetNBVReportItemsToCheck.filter(isPresent);
    if (rouAssetNBVReportItems.length > 0) {
      const rouAssetNBVReportItemCollectionIdentifiers = rouAssetNBVReportItemCollection.map(
        rouAssetNBVReportItemItem => getRouAssetNBVReportItemIdentifier(rouAssetNBVReportItemItem)!
      );
      const rouAssetNBVReportItemsToAdd = rouAssetNBVReportItems.filter(rouAssetNBVReportItemItem => {
        const rouAssetNBVReportItemIdentifier = getRouAssetNBVReportItemIdentifier(rouAssetNBVReportItemItem);
        if (
          rouAssetNBVReportItemIdentifier == null ||
          rouAssetNBVReportItemCollectionIdentifiers.includes(rouAssetNBVReportItemIdentifier)
        ) {
          return false;
        }
        rouAssetNBVReportItemCollectionIdentifiers.push(rouAssetNBVReportItemIdentifier);
        return true;
      });
      return [...rouAssetNBVReportItemsToAdd, ...rouAssetNBVReportItemCollection];
    }
    return rouAssetNBVReportItemCollection;
  }

  protected convertDateFromClient(rouAssetNBVReportItem: IRouAssetNBVReportItem): IRouAssetNBVReportItem {
    return Object.assign({}, rouAssetNBVReportItem, {
      commencementDate: rouAssetNBVReportItem.commencementDate?.isValid()
        ? rouAssetNBVReportItem.commencementDate.format(DATE_FORMAT)
        : undefined,
      expirationDate: rouAssetNBVReportItem.expirationDate?.isValid()
        ? rouAssetNBVReportItem.expirationDate.format(DATE_FORMAT)
        : undefined,
      fiscalPeriodEndDate: rouAssetNBVReportItem.fiscalPeriodEndDate?.isValid()
        ? rouAssetNBVReportItem.fiscalPeriodEndDate.format(DATE_FORMAT)
        : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.commencementDate = res.body.commencementDate ? dayjs(res.body.commencementDate) : undefined;
      res.body.expirationDate = res.body.expirationDate ? dayjs(res.body.expirationDate) : undefined;
      res.body.fiscalPeriodEndDate = res.body.fiscalPeriodEndDate ? dayjs(res.body.fiscalPeriodEndDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((rouAssetNBVReportItem: IRouAssetNBVReportItem) => {
        rouAssetNBVReportItem.commencementDate = rouAssetNBVReportItem.commencementDate
          ? dayjs(rouAssetNBVReportItem.commencementDate)
          : undefined;
        rouAssetNBVReportItem.expirationDate = rouAssetNBVReportItem.expirationDate
          ? dayjs(rouAssetNBVReportItem.expirationDate)
          : undefined;
        rouAssetNBVReportItem.fiscalPeriodEndDate = rouAssetNBVReportItem.fiscalPeriodEndDate
          ? dayjs(rouAssetNBVReportItem.fiscalPeriodEndDate)
          : undefined;
      });
    }
    return res;
  }
}
