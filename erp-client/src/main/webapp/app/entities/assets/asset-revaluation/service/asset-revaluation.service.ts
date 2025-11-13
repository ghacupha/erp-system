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
import { IAssetRevaluation, getAssetRevaluationIdentifier } from '../asset-revaluation.model';

export type EntityResponseType = HttpResponse<IAssetRevaluation>;
export type EntityArrayResponseType = HttpResponse<IAssetRevaluation[]>;

@Injectable({ providedIn: 'root' })
export class AssetRevaluationService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/asset-revaluations');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/asset-revaluations');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(assetRevaluation: IAssetRevaluation): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(assetRevaluation);
    return this.http
      .post<IAssetRevaluation>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(assetRevaluation: IAssetRevaluation): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(assetRevaluation);
    return this.http
      .put<IAssetRevaluation>(`${this.resourceUrl}/${getAssetRevaluationIdentifier(assetRevaluation) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(assetRevaluation: IAssetRevaluation): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(assetRevaluation);
    return this.http
      .patch<IAssetRevaluation>(`${this.resourceUrl}/${getAssetRevaluationIdentifier(assetRevaluation) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IAssetRevaluation>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IAssetRevaluation[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IAssetRevaluation[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  addAssetRevaluationToCollectionIfMissing(
    assetRevaluationCollection: IAssetRevaluation[],
    ...assetRevaluationsToCheck: (IAssetRevaluation | null | undefined)[]
  ): IAssetRevaluation[] {
    const assetRevaluations: IAssetRevaluation[] = assetRevaluationsToCheck.filter(isPresent);
    if (assetRevaluations.length > 0) {
      const assetRevaluationCollectionIdentifiers = assetRevaluationCollection.map(
        assetRevaluationItem => getAssetRevaluationIdentifier(assetRevaluationItem)!
      );
      const assetRevaluationsToAdd = assetRevaluations.filter(assetRevaluationItem => {
        const assetRevaluationIdentifier = getAssetRevaluationIdentifier(assetRevaluationItem);
        if (assetRevaluationIdentifier == null || assetRevaluationCollectionIdentifiers.includes(assetRevaluationIdentifier)) {
          return false;
        }
        assetRevaluationCollectionIdentifiers.push(assetRevaluationIdentifier);
        return true;
      });
      return [...assetRevaluationsToAdd, ...assetRevaluationCollection];
    }
    return assetRevaluationCollection;
  }

  protected convertDateFromClient(assetRevaluation: IAssetRevaluation): IAssetRevaluation {
    return Object.assign({}, assetRevaluation, {
      revaluationDate: assetRevaluation.revaluationDate?.isValid() ? assetRevaluation.revaluationDate.format(DATE_FORMAT) : undefined,
      timeOfCreation: assetRevaluation.timeOfCreation?.isValid() ? assetRevaluation.timeOfCreation.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.revaluationDate = res.body.revaluationDate ? dayjs(res.body.revaluationDate) : undefined;
      res.body.timeOfCreation = res.body.timeOfCreation ? dayjs(res.body.timeOfCreation) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((assetRevaluation: IAssetRevaluation) => {
        assetRevaluation.revaluationDate = assetRevaluation.revaluationDate ? dayjs(assetRevaluation.revaluationDate) : undefined;
        assetRevaluation.timeOfCreation = assetRevaluation.timeOfCreation ? dayjs(assetRevaluation.timeOfCreation) : undefined;
      });
    }
    return res;
  }
}
