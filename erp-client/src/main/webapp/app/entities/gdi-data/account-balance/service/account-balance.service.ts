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
import { IAccountBalance, getAccountBalanceIdentifier } from '../account-balance.model';

export type EntityResponseType = HttpResponse<IAccountBalance>;
export type EntityArrayResponseType = HttpResponse<IAccountBalance[]>;

@Injectable({ providedIn: 'root' })
export class AccountBalanceService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/account-balances');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/account-balances');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(accountBalance: IAccountBalance): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(accountBalance);
    return this.http
      .post<IAccountBalance>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(accountBalance: IAccountBalance): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(accountBalance);
    return this.http
      .put<IAccountBalance>(`${this.resourceUrl}/${getAccountBalanceIdentifier(accountBalance) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(accountBalance: IAccountBalance): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(accountBalance);
    return this.http
      .patch<IAccountBalance>(`${this.resourceUrl}/${getAccountBalanceIdentifier(accountBalance) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IAccountBalance>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IAccountBalance[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IAccountBalance[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  addAccountBalanceToCollectionIfMissing(
    accountBalanceCollection: IAccountBalance[],
    ...accountBalancesToCheck: (IAccountBalance | null | undefined)[]
  ): IAccountBalance[] {
    const accountBalances: IAccountBalance[] = accountBalancesToCheck.filter(isPresent);
    if (accountBalances.length > 0) {
      const accountBalanceCollectionIdentifiers = accountBalanceCollection.map(
        accountBalanceItem => getAccountBalanceIdentifier(accountBalanceItem)!
      );
      const accountBalancesToAdd = accountBalances.filter(accountBalanceItem => {
        const accountBalanceIdentifier = getAccountBalanceIdentifier(accountBalanceItem);
        if (accountBalanceIdentifier == null || accountBalanceCollectionIdentifiers.includes(accountBalanceIdentifier)) {
          return false;
        }
        accountBalanceCollectionIdentifiers.push(accountBalanceIdentifier);
        return true;
      });
      return [...accountBalancesToAdd, ...accountBalanceCollection];
    }
    return accountBalanceCollection;
  }

  protected convertDateFromClient(accountBalance: IAccountBalance): IAccountBalance {
    return Object.assign({}, accountBalance, {
      reportingDate: accountBalance.reportingDate?.isValid() ? accountBalance.reportingDate.format(DATE_FORMAT) : undefined,
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
      res.body.forEach((accountBalance: IAccountBalance) => {
        accountBalance.reportingDate = accountBalance.reportingDate ? dayjs(accountBalance.reportingDate) : undefined;
      });
    }
    return res;
  }
}
