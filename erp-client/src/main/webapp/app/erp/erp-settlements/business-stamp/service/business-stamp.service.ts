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
import { IBusinessStamp, getBusinessStampIdentifier } from '../business-stamp.model';

export type EntityResponseType = HttpResponse<IBusinessStamp>;
export type EntityArrayResponseType = HttpResponse<IBusinessStamp[]>;

@Injectable({ providedIn: 'root' })
export class BusinessStampService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/payments/business-stamps');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/payments/_search/business-stamps');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(businessStamp: IBusinessStamp): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(businessStamp);
    return this.http
      .post<IBusinessStamp>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(businessStamp: IBusinessStamp): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(businessStamp);
    return this.http
      .put<IBusinessStamp>(`${this.resourceUrl}/${getBusinessStampIdentifier(businessStamp) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(businessStamp: IBusinessStamp): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(businessStamp);
    return this.http
      .patch<IBusinessStamp>(`${this.resourceUrl}/${getBusinessStampIdentifier(businessStamp) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IBusinessStamp>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IBusinessStamp[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IBusinessStamp[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  addBusinessStampToCollectionIfMissing(
    businessStampCollection: IBusinessStamp[],
    ...businessStampsToCheck: (IBusinessStamp | null | undefined)[]
  ): IBusinessStamp[] {
    const businessStamps: IBusinessStamp[] = businessStampsToCheck.filter(isPresent);
    if (businessStamps.length > 0) {
      const businessStampCollectionIdentifiers = businessStampCollection.map(
        businessStampItem => getBusinessStampIdentifier(businessStampItem)!
      );
      const businessStampsToAdd = businessStamps.filter(businessStampItem => {
        const businessStampIdentifier = getBusinessStampIdentifier(businessStampItem);
        if (businessStampIdentifier == null || businessStampCollectionIdentifiers.includes(businessStampIdentifier)) {
          return false;
        }
        businessStampCollectionIdentifiers.push(businessStampIdentifier);
        return true;
      });
      return [...businessStampsToAdd, ...businessStampCollection];
    }
    return businessStampCollection;
  }

  protected convertDateFromClient(businessStamp: IBusinessStamp): IBusinessStamp {
    return Object.assign({}, businessStamp, {
      stampDate: businessStamp.stampDate?.isValid() ? businessStamp.stampDate.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.stampDate = res.body.stampDate ? dayjs(res.body.stampDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((businessStamp: IBusinessStamp) => {
        businessStamp.stampDate = businessStamp.stampDate ? dayjs(businessStamp.stampDate) : undefined;
      });
    }
    return res;
  }
}
