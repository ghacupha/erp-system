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
import { IPaymentRequisition, getPaymentRequisitionIdentifier } from '../payment-requisition.model';

export type EntityResponseType = HttpResponse<IPaymentRequisition>;
export type EntityArrayResponseType = HttpResponse<IPaymentRequisition[]>;

@Injectable({ providedIn: 'root' })
export class PaymentRequisitionService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/payment-requisitions');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/payment-requisitions');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(paymentRequisition: IPaymentRequisition): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(paymentRequisition);
    return this.http
      .post<IPaymentRequisition>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(paymentRequisition: IPaymentRequisition): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(paymentRequisition);
    return this.http
      .put<IPaymentRequisition>(`${this.resourceUrl}/${getPaymentRequisitionIdentifier(paymentRequisition) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(paymentRequisition: IPaymentRequisition): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(paymentRequisition);
    return this.http
      .patch<IPaymentRequisition>(`${this.resourceUrl}/${getPaymentRequisitionIdentifier(paymentRequisition) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IPaymentRequisition>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IPaymentRequisition[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IPaymentRequisition[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  addPaymentRequisitionToCollectionIfMissing(
    paymentRequisitionCollection: IPaymentRequisition[],
    ...paymentRequisitionsToCheck: (IPaymentRequisition | null | undefined)[]
  ): IPaymentRequisition[] {
    const paymentRequisitions: IPaymentRequisition[] = paymentRequisitionsToCheck.filter(isPresent);
    if (paymentRequisitions.length > 0) {
      const paymentRequisitionCollectionIdentifiers = paymentRequisitionCollection.map(
        paymentRequisitionItem => getPaymentRequisitionIdentifier(paymentRequisitionItem)!
      );
      const paymentRequisitionsToAdd = paymentRequisitions.filter(paymentRequisitionItem => {
        const paymentRequisitionIdentifier = getPaymentRequisitionIdentifier(paymentRequisitionItem);
        if (paymentRequisitionIdentifier == null || paymentRequisitionCollectionIdentifiers.includes(paymentRequisitionIdentifier)) {
          return false;
        }
        paymentRequisitionCollectionIdentifiers.push(paymentRequisitionIdentifier);
        return true;
      });
      return [...paymentRequisitionsToAdd, ...paymentRequisitionCollection];
    }
    return paymentRequisitionCollection;
  }

  protected convertDateFromClient(paymentRequisition: IPaymentRequisition): IPaymentRequisition {
    return Object.assign({}, paymentRequisition, {
      receptionDate: paymentRequisition.receptionDate?.isValid() ? paymentRequisition.receptionDate.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.receptionDate = res.body.receptionDate ? dayjs(res.body.receptionDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((paymentRequisition: IPaymentRequisition) => {
        paymentRequisition.receptionDate = paymentRequisition.receptionDate ? dayjs(paymentRequisition.receptionDate) : undefined;
      });
    }
    return res;
  }
}
