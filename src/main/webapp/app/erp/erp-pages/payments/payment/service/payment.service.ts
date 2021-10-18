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
import { IPayment, getPaymentIdentifier } from '../payment.model';
import {NGXLogger} from "ngx-logger";

export type EntityResponseType = HttpResponse<IPayment>;
export type EntityArrayResponseType = HttpResponse<IPayment[]>;

@Injectable({ providedIn: 'root' })
export class PaymentService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/payments');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/payments');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
    protected log: NGXLogger
    ) {}

  create(payment: IPayment): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(payment);
    this.log.debug(`Request to create payment id: ${copy.paymentNumber}, dated: ${copy.paymentDate}`);
    return this.http
      .post<IPayment>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(payment: IPayment): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(payment);
    return this.http
      .put<IPayment>(`${this.resourceUrl}/${getPaymentIdentifier(payment) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(payment: IPayment): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(payment);
    return this.http
      .patch<IPayment>(`${this.resourceUrl}/${getPaymentIdentifier(payment) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IPayment>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IPayment[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IPayment[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  addPaymentToCollectionIfMissing(paymentCollection: IPayment[], ...paymentsToCheck: (IPayment | null | undefined)[]): IPayment[] {
    const payments: IPayment[] = paymentsToCheck.filter(isPresent);
    if (payments.length > 0) {
      const paymentCollectionIdentifiers = paymentCollection.map(paymentItem => getPaymentIdentifier(paymentItem)!);
      const paymentsToAdd = payments.filter(paymentItem => {
        const paymentIdentifier = getPaymentIdentifier(paymentItem);
        if (paymentIdentifier == null || paymentCollectionIdentifiers.includes(paymentIdentifier)) {
          return false;
        }
        paymentCollectionIdentifiers.push(paymentIdentifier);
        return true;
      });
      return [...paymentsToAdd, ...paymentCollection];
    }
    return paymentCollection;
  }

  protected convertDateFromClient(payment: IPayment): IPayment {
    return Object.assign({}, payment, {
      paymentDate: payment.paymentDate?.isValid() ? payment.paymentDate.format(DATE_FORMAT) : undefined,
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
      res.body.forEach((payment: IPayment) => {
        payment.paymentDate = payment.paymentDate ? dayjs(payment.paymentDate) : undefined;
      });
    }
    return res;
  }
}
