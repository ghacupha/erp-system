import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption, SearchWithPagination } from 'app/shared/util/request-util';
import { IPaymentCalculation } from 'app/shared/model/payments/payment-calculation.model';

type EntityResponseType = HttpResponse<IPaymentCalculation>;
type EntityArrayResponseType = HttpResponse<IPaymentCalculation[]>;

@Injectable({ providedIn: 'root' })
export class PaymentCalculationService {
  public resourceUrl = SERVER_API_URL + 'services/erpservice/api/payment-calculations';
  public resourceSearchUrl = SERVER_API_URL + 'services/erpservice/api/_search/payment-calculations';

  constructor(protected http: HttpClient) {}

  create(paymentCalculation: IPaymentCalculation): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(paymentCalculation);
    return this.http
      .post<IPaymentCalculation>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(paymentCalculation: IPaymentCalculation): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(paymentCalculation);
    return this.http
      .put<IPaymentCalculation>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IPaymentCalculation>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IPaymentCalculation[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IPaymentCalculation[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  protected convertDateFromClient(paymentCalculation: IPaymentCalculation): IPaymentCalculation {
    const copy: IPaymentCalculation = Object.assign({}, paymentCalculation, {
      paymentDate:
        paymentCalculation.paymentDate && paymentCalculation.paymentDate.isValid()
          ? paymentCalculation.paymentDate.format(DATE_FORMAT)
          : undefined,
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.paymentDate = res.body.paymentDate ? moment(res.body.paymentDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((paymentCalculation: IPaymentCalculation) => {
        paymentCalculation.paymentDate = paymentCalculation.paymentDate ? moment(paymentCalculation.paymentDate) : undefined;
      });
    }
    return res;
  }
}
