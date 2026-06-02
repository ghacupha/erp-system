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
import { ILeasePayment, getLeasePaymentIdentifier } from '../lease-payment.model';

export type EntityResponseType = HttpResponse<ILeasePayment>;
export type EntityArrayResponseType = HttpResponse<ILeasePayment[]>;

@Injectable({ providedIn: 'root' })
export class LeasePaymentService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/lease-payments');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/lease-payments');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(leasePayment: ILeasePayment): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(leasePayment);
    return this.http
      .post<ILeasePayment>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(leasePayment: ILeasePayment): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(leasePayment);
    return this.http
      .put<ILeasePayment>(`${this.resourceUrl}/${getLeasePaymentIdentifier(leasePayment) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(leasePayment: ILeasePayment): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(leasePayment);
    return this.http
      .patch<ILeasePayment>(`${this.resourceUrl}/${getLeasePaymentIdentifier(leasePayment) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ILeasePayment>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ILeasePayment[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ILeasePayment[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  addLeasePaymentToCollectionIfMissing(
    leasePaymentCollection: ILeasePayment[],
    ...leasePaymentsToCheck: (ILeasePayment | null | undefined)[]
  ): ILeasePayment[] {
    const leasePayments: ILeasePayment[] = leasePaymentsToCheck.filter(isPresent);
    if (leasePayments.length > 0) {
      const leasePaymentCollectionIdentifiers = leasePaymentCollection.map(
        leasePaymentItem => getLeasePaymentIdentifier(leasePaymentItem)!
      );
      const leasePaymentsToAdd = leasePayments.filter(leasePaymentItem => {
        const leasePaymentIdentifier = getLeasePaymentIdentifier(leasePaymentItem);
        if (leasePaymentIdentifier == null || leasePaymentCollectionIdentifiers.includes(leasePaymentIdentifier)) {
          return false;
        }
        leasePaymentCollectionIdentifiers.push(leasePaymentIdentifier);
        return true;
      });
      return [...leasePaymentsToAdd, ...leasePaymentCollection];
    }
    return leasePaymentCollection;
  }

  protected convertDateFromClient(leasePayment: ILeasePayment): ILeasePayment {
    return Object.assign({}, leasePayment, {
      paymentDate: leasePayment.paymentDate?.isValid() ? leasePayment.paymentDate.format(DATE_FORMAT) : undefined,
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
      res.body.forEach((leasePayment: ILeasePayment) => {
        leasePayment.paymentDate = leasePayment.paymentDate ? dayjs(leasePayment.paymentDate) : undefined;
      });
    }
    return res;
  }
}
