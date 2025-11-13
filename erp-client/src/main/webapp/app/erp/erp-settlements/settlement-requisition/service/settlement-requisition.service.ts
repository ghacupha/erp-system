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
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { ISettlementRequisition, getSettlementRequisitionIdentifier } from '../settlement-requisition.model';
import { DATE_FORMAT } from '../../../../config/input.constants';

export type EntityResponseType = HttpResponse<ISettlementRequisition>;
export type EntityArrayResponseType = HttpResponse<ISettlementRequisition[]>;

@Injectable({ providedIn: 'root' })
export class SettlementRequisitionService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/requisition/settlement-requisitions');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/requisition/_search/settlement-requisitions');
  protected resourceSearchIndexUrl = this.applicationConfigService.getEndpointFor('api/requisition/elasticsearch/re-index');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(settlementRequisition: ISettlementRequisition): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(settlementRequisition);
    return this.http
      .post<ISettlementRequisition>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(settlementRequisition: ISettlementRequisition): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(settlementRequisition);
    return this.http
      .put<ISettlementRequisition>(`${this.resourceUrl}/${getSettlementRequisitionIdentifier(settlementRequisition) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(settlementRequisition: ISettlementRequisition): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(settlementRequisition);
    return this.http
      .patch<ISettlementRequisition>(`${this.resourceUrl}/${getSettlementRequisitionIdentifier(settlementRequisition) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ISettlementRequisition>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ISettlementRequisition[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ISettlementRequisition[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  indexAll(req: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ISettlementRequisition[]>(this.resourceSearchIndexUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  addSettlementRequisitionToCollectionIfMissing(
    settlementRequisitionCollection: ISettlementRequisition[],
    ...settlementRequisitionsToCheck: (ISettlementRequisition | null | undefined)[]
  ): ISettlementRequisition[] {
    const settlementRequisitions: ISettlementRequisition[] = settlementRequisitionsToCheck.filter(isPresent);
    if (settlementRequisitions.length > 0) {
      const settlementRequisitionCollectionIdentifiers = settlementRequisitionCollection.map(
        settlementRequisitionItem => getSettlementRequisitionIdentifier(settlementRequisitionItem)!
      );
      const settlementRequisitionsToAdd = settlementRequisitions.filter(settlementRequisitionItem => {
        const settlementRequisitionIdentifier = getSettlementRequisitionIdentifier(settlementRequisitionItem);
        if (
          settlementRequisitionIdentifier == null ||
          settlementRequisitionCollectionIdentifiers.includes(settlementRequisitionIdentifier)
        ) {
          return false;
        }
        settlementRequisitionCollectionIdentifiers.push(settlementRequisitionIdentifier);
        return true;
      });
      return [...settlementRequisitionsToAdd, ...settlementRequisitionCollection];
    }
    return settlementRequisitionCollection;
  }

  protected convertDateFromClient(settlementRequisition: ISettlementRequisition): ISettlementRequisition {
    return Object.assign({}, settlementRequisition, {
      timeOfRequisition: settlementRequisition.timeOfRequisition?.isValid() ? settlementRequisition.timeOfRequisition.toJSON() : undefined,
      transactionDate: settlementRequisition.transactionDate?.isValid()
        ? settlementRequisition.transactionDate.format(DATE_FORMAT)
        : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.timeOfRequisition = res.body.timeOfRequisition ? dayjs(res.body.timeOfRequisition) : undefined;
      res.body.transactionDate = res.body.transactionDate ? dayjs(res.body.transactionDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((settlementRequisition: ISettlementRequisition) => {
        settlementRequisition.timeOfRequisition = settlementRequisition.timeOfRequisition
          ? dayjs(settlementRequisition.timeOfRequisition)
          : undefined;
        settlementRequisition.transactionDate = settlementRequisition.transactionDate
          ? dayjs(settlementRequisition.transactionDate)
          : undefined;
      });
    }
    return res;
  }
}
