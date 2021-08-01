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
import { IPaymentCalculation, getPaymentCalculationIdentifier } from '../payment-calculation.model';

export type EntityResponseType = HttpResponse<IPaymentCalculation>;
export type EntityArrayResponseType = HttpResponse<IPaymentCalculation[]>;

@Injectable({ providedIn: 'root' })
export class PaymentCalculationService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/payment-calculations');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/payment-calculations');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(paymentCalculation: IPaymentCalculation): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(paymentCalculation);
    return this.http
      .post<IPaymentCalculation>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(paymentCalculation: IPaymentCalculation): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(paymentCalculation);
    return this.http
      .put<IPaymentCalculation>(`${this.resourceUrl}/${getPaymentCalculationIdentifier(paymentCalculation) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(paymentCalculation: IPaymentCalculation): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(paymentCalculation);
    return this.http
      .patch<IPaymentCalculation>(`${this.resourceUrl}/${getPaymentCalculationIdentifier(paymentCalculation) as number}`, copy, {
        observe: 'response',
      })
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

  addPaymentCalculationToCollectionIfMissing(
    paymentCalculationCollection: IPaymentCalculation[],
    ...paymentCalculationsToCheck: (IPaymentCalculation | null | undefined)[]
  ): IPaymentCalculation[] {
    const paymentCalculations: IPaymentCalculation[] = paymentCalculationsToCheck.filter(isPresent);
    if (paymentCalculations.length > 0) {
      const paymentCalculationCollectionIdentifiers = paymentCalculationCollection.map(
        paymentCalculationItem => getPaymentCalculationIdentifier(paymentCalculationItem)!
      );
      const paymentCalculationsToAdd = paymentCalculations.filter(paymentCalculationItem => {
        const paymentCalculationIdentifier = getPaymentCalculationIdentifier(paymentCalculationItem);
        if (paymentCalculationIdentifier == null || paymentCalculationCollectionIdentifiers.includes(paymentCalculationIdentifier)) {
          return false;
        }
        paymentCalculationCollectionIdentifiers.push(paymentCalculationIdentifier);
        return true;
      });
      return [...paymentCalculationsToAdd, ...paymentCalculationCollection];
    }
    return paymentCalculationCollection;
  }

  protected convertDateFromClient(paymentCalculation: IPaymentCalculation): IPaymentCalculation {
    return Object.assign({}, paymentCalculation, {
      paymentDate: paymentCalculation.paymentDate?.isValid() ? paymentCalculation.paymentDate.format(DATE_FORMAT) : undefined,
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
      res.body.forEach((paymentCalculation: IPaymentCalculation) => {
        paymentCalculation.paymentDate = paymentCalculation.paymentDate ? dayjs(paymentCalculation.paymentDate) : undefined;
      });
    }
    return res;
  }
}
