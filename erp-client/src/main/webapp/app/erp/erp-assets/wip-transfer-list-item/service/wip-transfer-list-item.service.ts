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
import { IWIPTransferListItem, getWIPTransferListItemIdentifier } from '../wip-transfer-list-item.model';

export type EntityResponseType = HttpResponse<IWIPTransferListItem>;
export type EntityArrayResponseType = HttpResponse<IWIPTransferListItem[]>;

@Injectable({ providedIn: 'root' })
export class WIPTransferListItemService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/fixed-asset/wip-transfer-list-items');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/fixed-asset/_search/wip-transfer-list-items');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IWIPTransferListItem>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IWIPTransferListItem[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IWIPTransferListItem[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  addWIPTransferListItemToCollectionIfMissing(
    wIPTransferListItemCollection: IWIPTransferListItem[],
    ...wIPTransferListItemsToCheck: (IWIPTransferListItem | null | undefined)[]
  ): IWIPTransferListItem[] {
    const wIPTransferListItems: IWIPTransferListItem[] = wIPTransferListItemsToCheck.filter(isPresent);
    if (wIPTransferListItems.length > 0) {
      const wIPTransferListItemCollectionIdentifiers = wIPTransferListItemCollection.map(
        wIPTransferListItemItem => getWIPTransferListItemIdentifier(wIPTransferListItemItem)!
      );
      const wIPTransferListItemsToAdd = wIPTransferListItems.filter(wIPTransferListItemItem => {
        const wIPTransferListItemIdentifier = getWIPTransferListItemIdentifier(wIPTransferListItemItem);
        if (wIPTransferListItemIdentifier == null || wIPTransferListItemCollectionIdentifiers.includes(wIPTransferListItemIdentifier)) {
          return false;
        }
        wIPTransferListItemCollectionIdentifiers.push(wIPTransferListItemIdentifier);
        return true;
      });
      return [...wIPTransferListItemsToAdd, ...wIPTransferListItemCollection];
    }
    return wIPTransferListItemCollection;
  }

  protected convertDateFromClient(wIPTransferListItem: IWIPTransferListItem): IWIPTransferListItem {
    return Object.assign({}, wIPTransferListItem, {
      transferSettlementDate: wIPTransferListItem.transferSettlementDate?.isValid()
        ? wIPTransferListItem.transferSettlementDate.format(DATE_FORMAT)
        : undefined,
      wipTransferDate: wIPTransferListItem.wipTransferDate?.isValid() ? wIPTransferListItem.wipTransferDate.format(DATE_FORMAT) : undefined,
      originalSettlementDate: wIPTransferListItem.originalSettlementDate?.isValid()
        ? wIPTransferListItem.originalSettlementDate.format(DATE_FORMAT)
        : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.transferSettlementDate = res.body.transferSettlementDate ? dayjs(res.body.transferSettlementDate) : undefined;
      res.body.wipTransferDate = res.body.wipTransferDate ? dayjs(res.body.wipTransferDate) : undefined;
      res.body.originalSettlementDate = res.body.originalSettlementDate ? dayjs(res.body.originalSettlementDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((wIPTransferListItem: IWIPTransferListItem) => {
        wIPTransferListItem.transferSettlementDate = wIPTransferListItem.transferSettlementDate
          ? dayjs(wIPTransferListItem.transferSettlementDate)
          : undefined;
        wIPTransferListItem.wipTransferDate = wIPTransferListItem.wipTransferDate ? dayjs(wIPTransferListItem.wipTransferDate) : undefined;
        wIPTransferListItem.originalSettlementDate = wIPTransferListItem.originalSettlementDate
          ? dayjs(wIPTransferListItem.originalSettlementDate)
          : undefined;
      });
    }
    return res;
  }
}
