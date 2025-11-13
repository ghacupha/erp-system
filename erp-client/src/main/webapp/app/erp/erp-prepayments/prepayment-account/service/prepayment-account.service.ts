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

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IPrepaymentAccount, getPrepaymentAccountIdentifier } from '../prepayment-account.model';
import { map } from 'rxjs/operators';
import { DATE_FORMAT } from '../../../../config/input.constants';
import * as dayjs from 'dayjs';

export type EntityResponseType = HttpResponse<IPrepaymentAccount>;
export type EntityArrayResponseType = HttpResponse<IPrepaymentAccount[]>;

@Injectable({ providedIn: 'root' })
export class PrepaymentAccountService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/prepayments/prepayment-accounts');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/prepayments/_search/prepayment-accounts');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(prepaymentAccount: IPrepaymentAccount): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(prepaymentAccount);
    return this.http
      .post<IPrepaymentAccount>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(prepaymentAccount: IPrepaymentAccount): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(prepaymentAccount);
    return this.http
      .put<IPrepaymentAccount>(`${this.resourceUrl}/${getPrepaymentAccountIdentifier(prepaymentAccount) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(prepaymentAccount: IPrepaymentAccount): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(prepaymentAccount);
    return this.http
      .patch<IPrepaymentAccount>(`${this.resourceUrl}/${getPrepaymentAccountIdentifier(prepaymentAccount) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IPrepaymentAccount>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  getNextCatalogueNumber(): Observable<HttpResponse<number>> {
    return this.http
      .get<number>(`${this.resourceUrl}/next/catalogue-number`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IPrepaymentAccount[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IPrepaymentAccount[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  addPrepaymentAccountToCollectionIfMissing(
    prepaymentAccountCollection: IPrepaymentAccount[],
    ...prepaymentAccountsToCheck: (IPrepaymentAccount | null | undefined)[]
  ): IPrepaymentAccount[] {
    const prepaymentAccounts: IPrepaymentAccount[] = prepaymentAccountsToCheck.filter(isPresent);
    if (prepaymentAccounts.length > 0) {
      const prepaymentAccountCollectionIdentifiers = prepaymentAccountCollection.map(
        prepaymentAccountItem => getPrepaymentAccountIdentifier(prepaymentAccountItem)!
      );
      const prepaymentAccountsToAdd = prepaymentAccounts.filter(prepaymentAccountItem => {
        const prepaymentAccountIdentifier = getPrepaymentAccountIdentifier(prepaymentAccountItem);
        if (prepaymentAccountIdentifier == null || prepaymentAccountCollectionIdentifiers.includes(prepaymentAccountIdentifier)) {
          return false;
        }
        prepaymentAccountCollectionIdentifiers.push(prepaymentAccountIdentifier);
        return true;
      });
      return [...prepaymentAccountsToAdd, ...prepaymentAccountCollection];
    }
    return prepaymentAccountCollection;
  }

  protected convertDateFromClient(prepaymentAccount: IPrepaymentAccount): IPrepaymentAccount {
    return Object.assign({}, prepaymentAccount, {
      recognitionDate: prepaymentAccount.recognitionDate?.isValid() ? prepaymentAccount.recognitionDate.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.recognitionDate = res.body.recognitionDate ? dayjs(res.body.recognitionDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((prepaymentAccount: IPrepaymentAccount) => {
        prepaymentAccount.recognitionDate = prepaymentAccount.recognitionDate ? dayjs(prepaymentAccount.recognitionDate) : undefined;
      });
    }
    return res;
  }
}
