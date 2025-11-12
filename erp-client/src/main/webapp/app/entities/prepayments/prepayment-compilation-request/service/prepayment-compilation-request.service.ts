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
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IPrepaymentCompilationRequest, getPrepaymentCompilationRequestIdentifier } from '../prepayment-compilation-request.model';

export type EntityResponseType = HttpResponse<IPrepaymentCompilationRequest>;
export type EntityArrayResponseType = HttpResponse<IPrepaymentCompilationRequest[]>;

@Injectable({ providedIn: 'root' })
export class PrepaymentCompilationRequestService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/prepayment-compilation-requests');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/prepayment-compilation-requests');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(prepaymentCompilationRequest: IPrepaymentCompilationRequest): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(prepaymentCompilationRequest);
    return this.http
      .post<IPrepaymentCompilationRequest>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(prepaymentCompilationRequest: IPrepaymentCompilationRequest): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(prepaymentCompilationRequest);
    return this.http
      .put<IPrepaymentCompilationRequest>(
        `${this.resourceUrl}/${getPrepaymentCompilationRequestIdentifier(prepaymentCompilationRequest) as number}`,
        copy,
        { observe: 'response' }
      )
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(prepaymentCompilationRequest: IPrepaymentCompilationRequest): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(prepaymentCompilationRequest);
    return this.http
      .patch<IPrepaymentCompilationRequest>(
        `${this.resourceUrl}/${getPrepaymentCompilationRequestIdentifier(prepaymentCompilationRequest) as number}`,
        copy,
        { observe: 'response' }
      )
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IPrepaymentCompilationRequest>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IPrepaymentCompilationRequest[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IPrepaymentCompilationRequest[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  addPrepaymentCompilationRequestToCollectionIfMissing(
    prepaymentCompilationRequestCollection: IPrepaymentCompilationRequest[],
    ...prepaymentCompilationRequestsToCheck: (IPrepaymentCompilationRequest | null | undefined)[]
  ): IPrepaymentCompilationRequest[] {
    const prepaymentCompilationRequests: IPrepaymentCompilationRequest[] = prepaymentCompilationRequestsToCheck.filter(isPresent);
    if (prepaymentCompilationRequests.length > 0) {
      const prepaymentCompilationRequestCollectionIdentifiers = prepaymentCompilationRequestCollection.map(
        prepaymentCompilationRequestItem => getPrepaymentCompilationRequestIdentifier(prepaymentCompilationRequestItem)!
      );
      const prepaymentCompilationRequestsToAdd = prepaymentCompilationRequests.filter(prepaymentCompilationRequestItem => {
        const prepaymentCompilationRequestIdentifier = getPrepaymentCompilationRequestIdentifier(prepaymentCompilationRequestItem);
        if (
          prepaymentCompilationRequestIdentifier == null ||
          prepaymentCompilationRequestCollectionIdentifiers.includes(prepaymentCompilationRequestIdentifier)
        ) {
          return false;
        }
        prepaymentCompilationRequestCollectionIdentifiers.push(prepaymentCompilationRequestIdentifier);
        return true;
      });
      return [...prepaymentCompilationRequestsToAdd, ...prepaymentCompilationRequestCollection];
    }
    return prepaymentCompilationRequestCollection;
  }

  protected convertDateFromClient(prepaymentCompilationRequest: IPrepaymentCompilationRequest): IPrepaymentCompilationRequest {
    return Object.assign({}, prepaymentCompilationRequest, {
      timeOfRequest: prepaymentCompilationRequest.timeOfRequest?.isValid()
        ? prepaymentCompilationRequest.timeOfRequest.toJSON()
        : undefined,
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
      res.body.forEach((prepaymentCompilationRequest: IPrepaymentCompilationRequest) => {
        prepaymentCompilationRequest.timeOfRequest = prepaymentCompilationRequest.timeOfRequest
          ? dayjs(prepaymentCompilationRequest.timeOfRequest)
          : undefined;
      });
    }
    return res;
  }
}
