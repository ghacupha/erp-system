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
import { ISettlement, getSettlementIdentifier } from '../settlement.model';

export type EntityResponseType = HttpResponse<ISettlement>;
export type EntityArrayResponseType = HttpResponse<ISettlement[]>;

@Injectable({ providedIn: 'root' })
export class SettlementService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/payments/settlements');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/payments/_search/settlements');
  protected resourceSearchIndexUrl = this.applicationConfigService.getEndpointFor('api/payments/settlements/elasticsearch/re-index');
  protected resourceSystemIndexUrl = this.applicationConfigService.getEndpointFor('api/index/run-index');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(settlement: ISettlement): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(settlement);
    return this.http
      .post<ISettlement>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(settlement: ISettlement): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(settlement);
    return this.http
      .put<ISettlement>(`${this.resourceUrl}/${getSettlementIdentifier(settlement) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(settlement: ISettlement): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(settlement);
    return this.http
      .patch<ISettlement>(`${this.resourceUrl}/${getSettlementIdentifier(settlement) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ISettlement>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ISettlement[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ISettlement[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  indexSystem(req: any): Observable<any> {
    const options = createRequestOption(req);
    return this.http.get(this.resourceSystemIndexUrl, { params: options, observe: 'response' });
  }

  indexAll(req: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ISettlement[]>(this.resourceSearchIndexUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  addSettlementToCollectionIfMissing(
    settlementCollection: ISettlement[],
    ...settlementsToCheck: (ISettlement | null | undefined)[]
  ): ISettlement[] {
    const settlements: ISettlement[] = settlementsToCheck.filter(isPresent);
    if (settlements.length > 0) {
      const settlementCollectionIdentifiers = settlementCollection.map(settlementItem => getSettlementIdentifier(settlementItem)!);
      const settlementsToAdd = settlements.filter(settlementItem => {
        const settlementIdentifier = getSettlementIdentifier(settlementItem);
        if (settlementIdentifier == null || settlementCollectionIdentifiers.includes(settlementIdentifier)) {
          return false;
        }
        settlementCollectionIdentifiers.push(settlementIdentifier);
        return true;
      });
      return [...settlementsToAdd, ...settlementCollection];
    }
    return settlementCollection;
  }

  protected convertDateFromClient(settlement: ISettlement): ISettlement {
    return Object.assign({}, settlement, {
      paymentDate: settlement.paymentDate?.isValid() ? settlement.paymentDate.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.paymentDate = res.body.paymentDate ? dayjs(res.body.paymentDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((settlement: ISettlement) => {
        settlement.paymentDate = settlement.paymentDate ? dayjs(settlement.paymentDate) : undefined;
      });
    }
    return res;
  }
}
