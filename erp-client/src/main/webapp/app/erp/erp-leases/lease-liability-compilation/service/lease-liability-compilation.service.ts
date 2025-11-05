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
import { ILeaseLiabilityCompilation, getLeaseLiabilityCompilationIdentifier } from '../lease-liability-compilation.model';

export type EntityResponseType = HttpResponse<ILeaseLiabilityCompilation>;
export type EntityArrayResponseType = HttpResponse<ILeaseLiabilityCompilation[]>;

@Injectable({ providedIn: 'root' })
export class LeaseLiabilityCompilationService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/leases/lease-liability-compilations');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/leases/_search/lease-liability-compilations');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(leaseLiabilityCompilation: ILeaseLiabilityCompilation): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(leaseLiabilityCompilation);
    return this.http
      .post<ILeaseLiabilityCompilation>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(leaseLiabilityCompilation: ILeaseLiabilityCompilation): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(leaseLiabilityCompilation);
    return this.http
      .put<ILeaseLiabilityCompilation>(
        `${this.resourceUrl}/${getLeaseLiabilityCompilationIdentifier(leaseLiabilityCompilation) as number}`,
        copy,
        { observe: 'response' }
      )
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(leaseLiabilityCompilation: ILeaseLiabilityCompilation): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(leaseLiabilityCompilation);
    return this.http
      .patch<ILeaseLiabilityCompilation>(
        `${this.resourceUrl}/${getLeaseLiabilityCompilationIdentifier(leaseLiabilityCompilation) as number}`,
        copy,
        { observe: 'response' }
      )
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ILeaseLiabilityCompilation>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ILeaseLiabilityCompilation[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  activate(id: number): Observable<EntityResponseType> {
    return this.http
      .post<ILeaseLiabilityCompilation>(`${this.resourceUrl}/${id}/activate`, {}, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  deactivate(id: number): Observable<EntityResponseType> {
    return this.http
      .post<ILeaseLiabilityCompilation>(`${this.resourceUrl}/${id}/deactivate`, {}, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ILeaseLiabilityCompilation[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  addLeaseLiabilityCompilationToCollectionIfMissing(
    leaseLiabilityCompilationCollection: ILeaseLiabilityCompilation[],
    ...leaseLiabilityCompilationsToCheck: (ILeaseLiabilityCompilation | null | undefined)[]
  ): ILeaseLiabilityCompilation[] {
    const leaseLiabilityCompilations: ILeaseLiabilityCompilation[] = leaseLiabilityCompilationsToCheck.filter(isPresent);
    if (leaseLiabilityCompilations.length > 0) {
      const leaseLiabilityCompilationCollectionIdentifiers = leaseLiabilityCompilationCollection.map(
        leaseLiabilityCompilationItem => getLeaseLiabilityCompilationIdentifier(leaseLiabilityCompilationItem)!
      );
      const leaseLiabilityCompilationsToAdd = leaseLiabilityCompilations.filter(leaseLiabilityCompilationItem => {
        const leaseLiabilityCompilationIdentifier = getLeaseLiabilityCompilationIdentifier(leaseLiabilityCompilationItem);
        if (
          leaseLiabilityCompilationIdentifier == null ||
          leaseLiabilityCompilationCollectionIdentifiers.includes(leaseLiabilityCompilationIdentifier)
        ) {
          return false;
        }
        leaseLiabilityCompilationCollectionIdentifiers.push(leaseLiabilityCompilationIdentifier);
        return true;
      });
      return [...leaseLiabilityCompilationsToAdd, ...leaseLiabilityCompilationCollection];
    }
    return leaseLiabilityCompilationCollection;
  }

  protected convertDateFromClient(leaseLiabilityCompilation: ILeaseLiabilityCompilation): ILeaseLiabilityCompilation {
    return Object.assign({}, leaseLiabilityCompilation, {
      timeOfRequest: leaseLiabilityCompilation.timeOfRequest?.isValid() ? leaseLiabilityCompilation.timeOfRequest.toJSON() : undefined,
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
      res.body.forEach((leaseLiabilityCompilation: ILeaseLiabilityCompilation) => {
        leaseLiabilityCompilation.timeOfRequest = leaseLiabilityCompilation.timeOfRequest
          ? dayjs(leaseLiabilityCompilation.timeOfRequest)
          : undefined;
      });
    }
    return res;
  }
}
