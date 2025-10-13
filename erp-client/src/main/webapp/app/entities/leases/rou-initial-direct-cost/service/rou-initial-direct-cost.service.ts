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
import { IRouInitialDirectCost, getRouInitialDirectCostIdentifier } from '../rou-initial-direct-cost.model';

export type EntityResponseType = HttpResponse<IRouInitialDirectCost>;
export type EntityArrayResponseType = HttpResponse<IRouInitialDirectCost[]>;

@Injectable({ providedIn: 'root' })
export class RouInitialDirectCostService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/rou-initial-direct-costs');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/rou-initial-direct-costs');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(rouInitialDirectCost: IRouInitialDirectCost): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(rouInitialDirectCost);
    return this.http
      .post<IRouInitialDirectCost>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(rouInitialDirectCost: IRouInitialDirectCost): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(rouInitialDirectCost);
    return this.http
      .put<IRouInitialDirectCost>(`${this.resourceUrl}/${getRouInitialDirectCostIdentifier(rouInitialDirectCost) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(rouInitialDirectCost: IRouInitialDirectCost): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(rouInitialDirectCost);
    return this.http
      .patch<IRouInitialDirectCost>(`${this.resourceUrl}/${getRouInitialDirectCostIdentifier(rouInitialDirectCost) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IRouInitialDirectCost>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IRouInitialDirectCost[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IRouInitialDirectCost[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  addRouInitialDirectCostToCollectionIfMissing(
    rouInitialDirectCostCollection: IRouInitialDirectCost[],
    ...rouInitialDirectCostsToCheck: (IRouInitialDirectCost | null | undefined)[]
  ): IRouInitialDirectCost[] {
    const rouInitialDirectCosts: IRouInitialDirectCost[] = rouInitialDirectCostsToCheck.filter(isPresent);
    if (rouInitialDirectCosts.length > 0) {
      const rouInitialDirectCostCollectionIdentifiers = rouInitialDirectCostCollection.map(
        rouInitialDirectCostItem => getRouInitialDirectCostIdentifier(rouInitialDirectCostItem)!
      );
      const rouInitialDirectCostsToAdd = rouInitialDirectCosts.filter(rouInitialDirectCostItem => {
        const rouInitialDirectCostIdentifier = getRouInitialDirectCostIdentifier(rouInitialDirectCostItem);
        if (rouInitialDirectCostIdentifier == null || rouInitialDirectCostCollectionIdentifiers.includes(rouInitialDirectCostIdentifier)) {
          return false;
        }
        rouInitialDirectCostCollectionIdentifiers.push(rouInitialDirectCostIdentifier);
        return true;
      });
      return [...rouInitialDirectCostsToAdd, ...rouInitialDirectCostCollection];
    }
    return rouInitialDirectCostCollection;
  }

  protected convertDateFromClient(rouInitialDirectCost: IRouInitialDirectCost): IRouInitialDirectCost {
    return Object.assign({}, rouInitialDirectCost, {
      transactionDate: rouInitialDirectCost.transactionDate?.isValid()
        ? rouInitialDirectCost.transactionDate.format(DATE_FORMAT)
        : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.transactionDate = res.body.transactionDate ? dayjs(res.body.transactionDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((rouInitialDirectCost: IRouInitialDirectCost) => {
        rouInitialDirectCost.transactionDate = rouInitialDirectCost.transactionDate
          ? dayjs(rouInitialDirectCost.transactionDate)
          : undefined;
      });
    }
    return res;
  }
}
