import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption, SearchWithPagination } from 'app/shared/util/request-util';
import { IPaymentCategory } from 'app/shared/model/payments/payment-category.model';

type EntityResponseType = HttpResponse<IPaymentCategory>;
type EntityArrayResponseType = HttpResponse<IPaymentCategory[]>;

@Injectable({ providedIn: 'root' })
export class PaymentCategoryService {
  public resourceUrl = SERVER_API_URL + 'services/erpservice/api/payment-categories';
  public resourceSearchUrl = SERVER_API_URL + 'services/erpservice/api/_search/payment-categories';

  constructor(protected http: HttpClient) {}

  create(paymentCategory: IPaymentCategory): Observable<EntityResponseType> {
    return this.http.post<IPaymentCategory>(this.resourceUrl, paymentCategory, { observe: 'response' });
  }

  update(paymentCategory: IPaymentCategory): Observable<EntityResponseType> {
    return this.http.put<IPaymentCategory>(this.resourceUrl, paymentCategory, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPaymentCategory>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPaymentCategory[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPaymentCategory[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }
}
