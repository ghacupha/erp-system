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
import { IAccountAttribute, getAccountAttributeIdentifier } from '../account-attribute.model';

export type EntityResponseType = HttpResponse<IAccountAttribute>;
export type EntityArrayResponseType = HttpResponse<IAccountAttribute[]>;

@Injectable({ providedIn: 'root' })
export class AccountAttributeService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/account-attributes');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/account-attributes');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(accountAttribute: IAccountAttribute): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(accountAttribute);
    return this.http
      .post<IAccountAttribute>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(accountAttribute: IAccountAttribute): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(accountAttribute);
    return this.http
      .put<IAccountAttribute>(`${this.resourceUrl}/${getAccountAttributeIdentifier(accountAttribute) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(accountAttribute: IAccountAttribute): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(accountAttribute);
    return this.http
      .patch<IAccountAttribute>(`${this.resourceUrl}/${getAccountAttributeIdentifier(accountAttribute) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IAccountAttribute>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IAccountAttribute[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IAccountAttribute[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  addAccountAttributeToCollectionIfMissing(
    accountAttributeCollection: IAccountAttribute[],
    ...accountAttributesToCheck: (IAccountAttribute | null | undefined)[]
  ): IAccountAttribute[] {
    const accountAttributes: IAccountAttribute[] = accountAttributesToCheck.filter(isPresent);
    if (accountAttributes.length > 0) {
      const accountAttributeCollectionIdentifiers = accountAttributeCollection.map(
        accountAttributeItem => getAccountAttributeIdentifier(accountAttributeItem)!
      );
      const accountAttributesToAdd = accountAttributes.filter(accountAttributeItem => {
        const accountAttributeIdentifier = getAccountAttributeIdentifier(accountAttributeItem);
        if (accountAttributeIdentifier == null || accountAttributeCollectionIdentifiers.includes(accountAttributeIdentifier)) {
          return false;
        }
        accountAttributeCollectionIdentifiers.push(accountAttributeIdentifier);
        return true;
      });
      return [...accountAttributesToAdd, ...accountAttributeCollection];
    }
    return accountAttributeCollection;
  }

  protected convertDateFromClient(accountAttribute: IAccountAttribute): IAccountAttribute {
    return Object.assign({}, accountAttribute, {
      reportingDate: accountAttribute.reportingDate?.isValid() ? accountAttribute.reportingDate.format(DATE_FORMAT) : undefined,
      accountOpeningDate: accountAttribute.accountOpeningDate?.isValid()
        ? accountAttribute.accountOpeningDate.format(DATE_FORMAT)
        : undefined,
      accountClosingDate: accountAttribute.accountClosingDate?.isValid()
        ? accountAttribute.accountClosingDate.format(DATE_FORMAT)
        : undefined,
      accountStatusChangeDate: accountAttribute.accountStatusChangeDate?.isValid()
        ? accountAttribute.accountStatusChangeDate.format(DATE_FORMAT)
        : undefined,
      expiryDate: accountAttribute.expiryDate?.isValid() ? accountAttribute.expiryDate.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.reportingDate = res.body.reportingDate ? dayjs(res.body.reportingDate) : undefined;
      res.body.accountOpeningDate = res.body.accountOpeningDate ? dayjs(res.body.accountOpeningDate) : undefined;
      res.body.accountClosingDate = res.body.accountClosingDate ? dayjs(res.body.accountClosingDate) : undefined;
      res.body.accountStatusChangeDate = res.body.accountStatusChangeDate ? dayjs(res.body.accountStatusChangeDate) : undefined;
      res.body.expiryDate = res.body.expiryDate ? dayjs(res.body.expiryDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((accountAttribute: IAccountAttribute) => {
        accountAttribute.reportingDate = accountAttribute.reportingDate ? dayjs(accountAttribute.reportingDate) : undefined;
        accountAttribute.accountOpeningDate = accountAttribute.accountOpeningDate ? dayjs(accountAttribute.accountOpeningDate) : undefined;
        accountAttribute.accountClosingDate = accountAttribute.accountClosingDate ? dayjs(accountAttribute.accountClosingDate) : undefined;
        accountAttribute.accountStatusChangeDate = accountAttribute.accountStatusChangeDate
          ? dayjs(accountAttribute.accountStatusChangeDate)
          : undefined;
        accountAttribute.expiryDate = accountAttribute.expiryDate ? dayjs(accountAttribute.expiryDate) : undefined;
      });
    }
    return res;
  }
}
