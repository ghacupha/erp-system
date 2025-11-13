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
import { ICreditCardFacility, getCreditCardFacilityIdentifier } from '../credit-card-facility.model';

export type EntityResponseType = HttpResponse<ICreditCardFacility>;
export type EntityArrayResponseType = HttpResponse<ICreditCardFacility[]>;

@Injectable({ providedIn: 'root' })
export class CreditCardFacilityService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/credit-card-facilities');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/credit-card-facilities');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(creditCardFacility: ICreditCardFacility): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(creditCardFacility);
    return this.http
      .post<ICreditCardFacility>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(creditCardFacility: ICreditCardFacility): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(creditCardFacility);
    return this.http
      .put<ICreditCardFacility>(`${this.resourceUrl}/${getCreditCardFacilityIdentifier(creditCardFacility) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(creditCardFacility: ICreditCardFacility): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(creditCardFacility);
    return this.http
      .patch<ICreditCardFacility>(`${this.resourceUrl}/${getCreditCardFacilityIdentifier(creditCardFacility) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ICreditCardFacility>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ICreditCardFacility[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ICreditCardFacility[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  addCreditCardFacilityToCollectionIfMissing(
    creditCardFacilityCollection: ICreditCardFacility[],
    ...creditCardFacilitiesToCheck: (ICreditCardFacility | null | undefined)[]
  ): ICreditCardFacility[] {
    const creditCardFacilities: ICreditCardFacility[] = creditCardFacilitiesToCheck.filter(isPresent);
    if (creditCardFacilities.length > 0) {
      const creditCardFacilityCollectionIdentifiers = creditCardFacilityCollection.map(
        creditCardFacilityItem => getCreditCardFacilityIdentifier(creditCardFacilityItem)!
      );
      const creditCardFacilitiesToAdd = creditCardFacilities.filter(creditCardFacilityItem => {
        const creditCardFacilityIdentifier = getCreditCardFacilityIdentifier(creditCardFacilityItem);
        if (creditCardFacilityIdentifier == null || creditCardFacilityCollectionIdentifiers.includes(creditCardFacilityIdentifier)) {
          return false;
        }
        creditCardFacilityCollectionIdentifiers.push(creditCardFacilityIdentifier);
        return true;
      });
      return [...creditCardFacilitiesToAdd, ...creditCardFacilityCollection];
    }
    return creditCardFacilityCollection;
  }

  protected convertDateFromClient(creditCardFacility: ICreditCardFacility): ICreditCardFacility {
    return Object.assign({}, creditCardFacility, {
      reportingDate: creditCardFacility.reportingDate?.isValid() ? creditCardFacility.reportingDate.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.reportingDate = res.body.reportingDate ? dayjs(res.body.reportingDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((creditCardFacility: ICreditCardFacility) => {
        creditCardFacility.reportingDate = creditCardFacility.reportingDate ? dayjs(creditCardFacility.reportingDate) : undefined;
      });
    }
    return res;
  }
}
