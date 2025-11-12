///
/// Erp System - Mark X No 11 (Jehoiada Series) Client 1.7.8
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
import { IPrepaymentAmortization, getPrepaymentAmortizationIdentifier } from '../prepayment-amortization.model';

export type EntityResponseType = HttpResponse<IPrepaymentAmortization>;
export type EntityArrayResponseType = HttpResponse<IPrepaymentAmortization[]>;

@Injectable({ providedIn: 'root' })
export class PrepaymentAmortizationService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/prepayment-amortizations');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/prepayment-amortizations');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(prepaymentAmortization: IPrepaymentAmortization): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(prepaymentAmortization);
    return this.http
      .post<IPrepaymentAmortization>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(prepaymentAmortization: IPrepaymentAmortization): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(prepaymentAmortization);
    return this.http
      .put<IPrepaymentAmortization>(`${this.resourceUrl}/${getPrepaymentAmortizationIdentifier(prepaymentAmortization) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(prepaymentAmortization: IPrepaymentAmortization): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(prepaymentAmortization);
    return this.http
      .patch<IPrepaymentAmortization>(
        `${this.resourceUrl}/${getPrepaymentAmortizationIdentifier(prepaymentAmortization) as number}`,
        copy,
        { observe: 'response' }
      )
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IPrepaymentAmortization>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IPrepaymentAmortization[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IPrepaymentAmortization[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  addPrepaymentAmortizationToCollectionIfMissing(
    prepaymentAmortizationCollection: IPrepaymentAmortization[],
    ...prepaymentAmortizationsToCheck: (IPrepaymentAmortization | null | undefined)[]
  ): IPrepaymentAmortization[] {
    const prepaymentAmortizations: IPrepaymentAmortization[] = prepaymentAmortizationsToCheck.filter(isPresent);
    if (prepaymentAmortizations.length > 0) {
      const prepaymentAmortizationCollectionIdentifiers = prepaymentAmortizationCollection.map(
        prepaymentAmortizationItem => getPrepaymentAmortizationIdentifier(prepaymentAmortizationItem)!
      );
      const prepaymentAmortizationsToAdd = prepaymentAmortizations.filter(prepaymentAmortizationItem => {
        const prepaymentAmortizationIdentifier = getPrepaymentAmortizationIdentifier(prepaymentAmortizationItem);
        if (
          prepaymentAmortizationIdentifier == null ||
          prepaymentAmortizationCollectionIdentifiers.includes(prepaymentAmortizationIdentifier)
        ) {
          return false;
        }
        prepaymentAmortizationCollectionIdentifiers.push(prepaymentAmortizationIdentifier);
        return true;
      });
      return [...prepaymentAmortizationsToAdd, ...prepaymentAmortizationCollection];
    }
    return prepaymentAmortizationCollection;
  }

  protected convertDateFromClient(prepaymentAmortization: IPrepaymentAmortization): IPrepaymentAmortization {
    return Object.assign({}, prepaymentAmortization, {
      prepaymentPeriod: prepaymentAmortization.prepaymentPeriod?.isValid()
        ? prepaymentAmortization.prepaymentPeriod.format(DATE_FORMAT)
        : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.prepaymentPeriod = res.body.prepaymentPeriod ? dayjs(res.body.prepaymentPeriod) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((prepaymentAmortization: IPrepaymentAmortization) => {
        prepaymentAmortization.prepaymentPeriod = prepaymentAmortization.prepaymentPeriod
          ? dayjs(prepaymentAmortization.prepaymentPeriod)
          : undefined;
      });
    }
    return res;
  }
}
