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
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { ITACompilationRequest, getTACompilationRequestIdentifier } from '../ta-compilation-request.model';

export type EntityResponseType = HttpResponse<ITACompilationRequest>;
export type EntityArrayResponseType = HttpResponse<ITACompilationRequest[]>;

@Injectable({ providedIn: 'root' })
export class TACompilationRequestService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/ta-compilation-requests');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/ta-compilation-requests');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(tACompilationRequest: ITACompilationRequest): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(tACompilationRequest);
    return this.http
      .post<ITACompilationRequest>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(tACompilationRequest: ITACompilationRequest): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(tACompilationRequest);
    return this.http
      .put<ITACompilationRequest>(`${this.resourceUrl}/${getTACompilationRequestIdentifier(tACompilationRequest) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(tACompilationRequest: ITACompilationRequest): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(tACompilationRequest);
    return this.http
      .patch<ITACompilationRequest>(`${this.resourceUrl}/${getTACompilationRequestIdentifier(tACompilationRequest) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ITACompilationRequest>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ITACompilationRequest[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ITACompilationRequest[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  addTACompilationRequestToCollectionIfMissing(
    tACompilationRequestCollection: ITACompilationRequest[],
    ...tACompilationRequestsToCheck: (ITACompilationRequest | null | undefined)[]
  ): ITACompilationRequest[] {
    const tACompilationRequests: ITACompilationRequest[] = tACompilationRequestsToCheck.filter(isPresent);
    if (tACompilationRequests.length > 0) {
      const tACompilationRequestCollectionIdentifiers = tACompilationRequestCollection.map(
        tACompilationRequestItem => getTACompilationRequestIdentifier(tACompilationRequestItem)!
      );
      const tACompilationRequestsToAdd = tACompilationRequests.filter(tACompilationRequestItem => {
        const tACompilationRequestIdentifier = getTACompilationRequestIdentifier(tACompilationRequestItem);
        if (tACompilationRequestIdentifier == null || tACompilationRequestCollectionIdentifiers.includes(tACompilationRequestIdentifier)) {
          return false;
        }
        tACompilationRequestCollectionIdentifiers.push(tACompilationRequestIdentifier);
        return true;
      });
      return [...tACompilationRequestsToAdd, ...tACompilationRequestCollection];
    }
    return tACompilationRequestCollection;
  }

  protected convertDateFromClient(tACompilationRequest: ITACompilationRequest): ITACompilationRequest {
    return Object.assign({}, tACompilationRequest, {
      timeOfRequest: tACompilationRequest.timeOfRequest?.isValid() ? tACompilationRequest.timeOfRequest.toJSON() : undefined,
      compilationTime: tACompilationRequest.compilationTime?.isValid() ? tACompilationRequest.compilationTime.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.timeOfRequest = res.body.timeOfRequest ? dayjs(res.body.timeOfRequest) : undefined;
      res.body.compilationTime = res.body.compilationTime ? dayjs(res.body.compilationTime) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((tACompilationRequest: ITACompilationRequest) => {
        tACompilationRequest.timeOfRequest = tACompilationRequest.timeOfRequest ? dayjs(tACompilationRequest.timeOfRequest) : undefined;
        tACompilationRequest.compilationTime = tACompilationRequest.compilationTime
          ? dayjs(tACompilationRequest.compilationTime)
          : undefined;
      });
    }
    return res;
  }
}
