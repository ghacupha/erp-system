import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
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
    return this.http.post<IPaymentRequisition>(this.resourceUrl, paymentRequisition, { observe: 'response' });
  }

  update(paymentRequisition: IPaymentRequisition): Observable<EntityResponseType> {
    return this.http.put<IPaymentRequisition>(
      `${this.resourceUrl}/${getPaymentRequisitionIdentifier(paymentRequisition) as number}`,
      paymentRequisition,
      { observe: 'response' }
    );
  }

  partialUpdate(paymentRequisition: IPaymentRequisition): Observable<EntityResponseType> {
    return this.http.patch<IPaymentRequisition>(
      `${this.resourceUrl}/${getPaymentRequisitionIdentifier(paymentRequisition) as number}`,
      paymentRequisition,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPaymentRequisition>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPaymentRequisition[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPaymentRequisition[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
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
}
