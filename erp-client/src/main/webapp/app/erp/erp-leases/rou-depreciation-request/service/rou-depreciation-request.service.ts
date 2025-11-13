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
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IRouDepreciationRequest, getRouDepreciationRequestIdentifier } from '../rou-depreciation-request.model';

export type EntityResponseType = HttpResponse<IRouDepreciationRequest>;
export type EntityArrayResponseType = HttpResponse<IRouDepreciationRequest[]>;

@Injectable({ providedIn: 'root' })
export class RouDepreciationRequestService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/leases/rou-depreciation-requests');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/leases/_search/rou-depreciation-requests');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(rouDepreciationRequest: IRouDepreciationRequest): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(rouDepreciationRequest);
    return this.http
      .post<IRouDepreciationRequest>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(rouDepreciationRequest: IRouDepreciationRequest): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(rouDepreciationRequest);
    return this.http
      .put<IRouDepreciationRequest>(`${this.resourceUrl}/${getRouDepreciationRequestIdentifier(rouDepreciationRequest) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  invalidate(rouDepreciationRequest: IRouDepreciationRequest): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(rouDepreciationRequest);
    return this.http
      .put<IRouDepreciationRequest>(`${this.resourceUrl}/invalidate/${getRouDepreciationRequestIdentifier(rouDepreciationRequest) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  revalidate(rouDepreciationRequest: IRouDepreciationRequest): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(rouDepreciationRequest);
    return this.http
      .put<IRouDepreciationRequest>(`${this.resourceUrl}/revalidate/${getRouDepreciationRequestIdentifier(rouDepreciationRequest) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(rouDepreciationRequest: IRouDepreciationRequest): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(rouDepreciationRequest);
    return this.http
      .patch<IRouDepreciationRequest>(
        `${this.resourceUrl}/${getRouDepreciationRequestIdentifier(rouDepreciationRequest) as number}`,
        copy,
        { observe: 'response' }
      )
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IRouDepreciationRequest>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IRouDepreciationRequest[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IRouDepreciationRequest[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  addRouDepreciationRequestToCollectionIfMissing(
    rouDepreciationRequestCollection: IRouDepreciationRequest[],
    ...rouDepreciationRequestsToCheck: (IRouDepreciationRequest | null | undefined)[]
  ): IRouDepreciationRequest[] {
    const rouDepreciationRequests: IRouDepreciationRequest[] = rouDepreciationRequestsToCheck.filter(isPresent);
    if (rouDepreciationRequests.length > 0) {
      const rouDepreciationRequestCollectionIdentifiers = rouDepreciationRequestCollection.map(
        rouDepreciationRequestItem => getRouDepreciationRequestIdentifier(rouDepreciationRequestItem)!
      );
      const rouDepreciationRequestsToAdd = rouDepreciationRequests.filter(rouDepreciationRequestItem => {
        const rouDepreciationRequestIdentifier = getRouDepreciationRequestIdentifier(rouDepreciationRequestItem);
        if (
          rouDepreciationRequestIdentifier == null ||
          rouDepreciationRequestCollectionIdentifiers.includes(rouDepreciationRequestIdentifier)
        ) {
          return false;
        }
        rouDepreciationRequestCollectionIdentifiers.push(rouDepreciationRequestIdentifier);
        return true;
      });
      return [...rouDepreciationRequestsToAdd, ...rouDepreciationRequestCollection];
    }
    return rouDepreciationRequestCollection;
  }

  protected convertDateFromClient(rouDepreciationRequest: IRouDepreciationRequest): IRouDepreciationRequest {
    return Object.assign({}, rouDepreciationRequest, {
      timeOfRequest: rouDepreciationRequest.timeOfRequest?.isValid() ? rouDepreciationRequest.timeOfRequest.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.timeOfRequest = res.body.timeOfRequest ? dayjs(res.body.timeOfRequest) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((rouDepreciationRequest: IRouDepreciationRequest) => {
        rouDepreciationRequest.timeOfRequest = rouDepreciationRequest.timeOfRequest
          ? dayjs(rouDepreciationRequest.timeOfRequest)
          : undefined;
      });
    }
    return res;
  }
}
