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
import { IPurchaseOrder, getPurchaseOrderIdentifier } from '../purchase-order.model';

export type EntityResponseType = HttpResponse<IPurchaseOrder>;
export type EntityArrayResponseType = HttpResponse<IPurchaseOrder[]>;

@Injectable({ providedIn: 'root' })
export class PurchaseOrderService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/payments/purchase-orders');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/payments/_search/purchase-orders');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(purchaseOrder: IPurchaseOrder): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(purchaseOrder);
    return this.http
      .post<IPurchaseOrder>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(purchaseOrder: IPurchaseOrder): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(purchaseOrder);
    return this.http
      .put<IPurchaseOrder>(`${this.resourceUrl}/${getPurchaseOrderIdentifier(purchaseOrder) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(purchaseOrder: IPurchaseOrder): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(purchaseOrder);
    return this.http
      .patch<IPurchaseOrder>(`${this.resourceUrl}/${getPurchaseOrderIdentifier(purchaseOrder) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IPurchaseOrder>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IPurchaseOrder[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IPurchaseOrder[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  addPurchaseOrderToCollectionIfMissing(
    purchaseOrderCollection: IPurchaseOrder[],
    ...purchaseOrdersToCheck: (IPurchaseOrder | null | undefined)[]
  ): IPurchaseOrder[] {
    const purchaseOrders: IPurchaseOrder[] = purchaseOrdersToCheck.filter(isPresent);
    if (purchaseOrders.length > 0) {
      const purchaseOrderCollectionIdentifiers = purchaseOrderCollection.map(
        purchaseOrderItem => getPurchaseOrderIdentifier(purchaseOrderItem)!
      );
      const purchaseOrdersToAdd = purchaseOrders.filter(purchaseOrderItem => {
        const purchaseOrderIdentifier = getPurchaseOrderIdentifier(purchaseOrderItem);
        if (purchaseOrderIdentifier == null || purchaseOrderCollectionIdentifiers.includes(purchaseOrderIdentifier)) {
          return false;
        }
        purchaseOrderCollectionIdentifiers.push(purchaseOrderIdentifier);
        return true;
      });
      return [...purchaseOrdersToAdd, ...purchaseOrderCollection];
    }
    return purchaseOrderCollection;
  }

  protected convertDateFromClient(purchaseOrder: IPurchaseOrder): IPurchaseOrder {
    return Object.assign({}, purchaseOrder, {
      purchaseOrderDate: purchaseOrder.purchaseOrderDate?.isValid() ? purchaseOrder.purchaseOrderDate.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.purchaseOrderDate = res.body.purchaseOrderDate ? dayjs(res.body.purchaseOrderDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((purchaseOrder: IPurchaseOrder) => {
        purchaseOrder.purchaseOrderDate = purchaseOrder.purchaseOrderDate ? dayjs(purchaseOrder.purchaseOrderDate) : undefined;
      });
    }
    return res;
  }
}
