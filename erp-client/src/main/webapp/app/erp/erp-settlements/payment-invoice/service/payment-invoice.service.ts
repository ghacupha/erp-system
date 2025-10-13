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
import { IPaymentInvoice, getPaymentInvoiceIdentifier } from '../payment-invoice.model';

export type EntityResponseType = HttpResponse<IPaymentInvoice>;
export type EntityArrayResponseType = HttpResponse<IPaymentInvoice[]>;

@Injectable({ providedIn: 'root' })
export class PaymentInvoiceService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/payments/payment-invoices');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/payments/_search/payment-invoices');
  protected resourceSearchIndexUrl = this.applicationConfigService.getEndpointFor('api/payments/payment-invoices/elasticsearch/re-index');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(paymentInvoice: IPaymentInvoice): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(paymentInvoice);
    return this.http
      .post<IPaymentInvoice>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(paymentInvoice: IPaymentInvoice): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(paymentInvoice);
    return this.http
      .put<IPaymentInvoice>(`${this.resourceUrl}/${getPaymentInvoiceIdentifier(paymentInvoice) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(paymentInvoice: IPaymentInvoice): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(paymentInvoice);
    return this.http
      .patch<IPaymentInvoice>(`${this.resourceUrl}/${getPaymentInvoiceIdentifier(paymentInvoice) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IPaymentInvoice>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IPaymentInvoice[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IPaymentInvoice[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  indexAll(req: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IPaymentInvoice[]>(this.resourceSearchIndexUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  addPaymentInvoiceToCollectionIfMissing(
    paymentInvoiceCollection: IPaymentInvoice[],
    ...paymentInvoicesToCheck: (IPaymentInvoice | null | undefined)[]
  ): IPaymentInvoice[] {
    const paymentInvoices: IPaymentInvoice[] = paymentInvoicesToCheck.filter(isPresent);
    if (paymentInvoices.length > 0) {
      const paymentInvoiceCollectionIdentifiers = paymentInvoiceCollection.map(
        paymentInvoiceItem => getPaymentInvoiceIdentifier(paymentInvoiceItem)!
      );
      const paymentInvoicesToAdd = paymentInvoices.filter(paymentInvoiceItem => {
        const paymentInvoiceIdentifier = getPaymentInvoiceIdentifier(paymentInvoiceItem);
        if (paymentInvoiceIdentifier == null || paymentInvoiceCollectionIdentifiers.includes(paymentInvoiceIdentifier)) {
          return false;
        }
        paymentInvoiceCollectionIdentifiers.push(paymentInvoiceIdentifier);
        return true;
      });
      return [...paymentInvoicesToAdd, ...paymentInvoiceCollection];
    }
    return paymentInvoiceCollection;
  }

  protected convertDateFromClient(paymentInvoice: IPaymentInvoice): IPaymentInvoice {
    return Object.assign({}, paymentInvoice, {
      invoiceDate: paymentInvoice.invoiceDate?.isValid() ? paymentInvoice.invoiceDate.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.invoiceDate = res.body.invoiceDate ? dayjs(res.body.invoiceDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((paymentInvoice: IPaymentInvoice) => {
        paymentInvoice.invoiceDate = paymentInvoice.invoiceDate ? dayjs(paymentInvoice.invoiceDate) : undefined;
      });
    }
    return res;
  }
}
