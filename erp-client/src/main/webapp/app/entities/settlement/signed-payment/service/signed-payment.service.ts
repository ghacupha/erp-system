///
/// Erp System - Mark X No 10 (Jehoiada Series) Client 1.7.8
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
import { ISignedPayment, getSignedPaymentIdentifier } from '../signed-payment.model';

export type EntityResponseType = HttpResponse<ISignedPayment>;
export type EntityArrayResponseType = HttpResponse<ISignedPayment[]>;

@Injectable({ providedIn: 'root' })
export class SignedPaymentService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/signed-payments');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/signed-payments');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(signedPayment: ISignedPayment): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(signedPayment);
    return this.http
      .post<ISignedPayment>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(signedPayment: ISignedPayment): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(signedPayment);
    return this.http
      .put<ISignedPayment>(`${this.resourceUrl}/${getSignedPaymentIdentifier(signedPayment) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(signedPayment: ISignedPayment): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(signedPayment);
    return this.http
      .patch<ISignedPayment>(`${this.resourceUrl}/${getSignedPaymentIdentifier(signedPayment) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ISignedPayment>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ISignedPayment[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ISignedPayment[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  addSignedPaymentToCollectionIfMissing(
    signedPaymentCollection: ISignedPayment[],
    ...signedPaymentsToCheck: (ISignedPayment | null | undefined)[]
  ): ISignedPayment[] {
    const signedPayments: ISignedPayment[] = signedPaymentsToCheck.filter(isPresent);
    if (signedPayments.length > 0) {
      const signedPaymentCollectionIdentifiers = signedPaymentCollection.map(
        signedPaymentItem => getSignedPaymentIdentifier(signedPaymentItem)!
      );
      const signedPaymentsToAdd = signedPayments.filter(signedPaymentItem => {
        const signedPaymentIdentifier = getSignedPaymentIdentifier(signedPaymentItem);
        if (signedPaymentIdentifier == null || signedPaymentCollectionIdentifiers.includes(signedPaymentIdentifier)) {
          return false;
        }
        signedPaymentCollectionIdentifiers.push(signedPaymentIdentifier);
        return true;
      });
      return [...signedPaymentsToAdd, ...signedPaymentCollection];
    }
    return signedPaymentCollection;
  }

  protected convertDateFromClient(signedPayment: ISignedPayment): ISignedPayment {
    return Object.assign({}, signedPayment, {
      transactionDate: signedPayment.transactionDate?.isValid() ? signedPayment.transactionDate.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.transactionDate = res.body.transactionDate ? dayjs(res.body.transactionDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((signedPayment: ISignedPayment) => {
        signedPayment.transactionDate = signedPayment.transactionDate ? dayjs(signedPayment.transactionDate) : undefined;
      });
    }
    return res;
  }
}
