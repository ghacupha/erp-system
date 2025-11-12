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
import { IRouModelMetadata, getRouModelMetadataIdentifier } from '../rou-model-metadata.model';

export type EntityResponseType = HttpResponse<IRouModelMetadata>;
export type EntityArrayResponseType = HttpResponse<IRouModelMetadata[]>;

@Injectable({ providedIn: 'root' })
export class RouModelMetadataService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/rou-model-metadata');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/rou-model-metadata');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(rouModelMetadata: IRouModelMetadata): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(rouModelMetadata);
    return this.http
      .post<IRouModelMetadata>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(rouModelMetadata: IRouModelMetadata): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(rouModelMetadata);
    return this.http
      .put<IRouModelMetadata>(`${this.resourceUrl}/${getRouModelMetadataIdentifier(rouModelMetadata) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(rouModelMetadata: IRouModelMetadata): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(rouModelMetadata);
    return this.http
      .patch<IRouModelMetadata>(`${this.resourceUrl}/${getRouModelMetadataIdentifier(rouModelMetadata) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IRouModelMetadata>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IRouModelMetadata[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IRouModelMetadata[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  addRouModelMetadataToCollectionIfMissing(
    rouModelMetadataCollection: IRouModelMetadata[],
    ...rouModelMetadataToCheck: (IRouModelMetadata | null | undefined)[]
  ): IRouModelMetadata[] {
    const rouModelMetadata: IRouModelMetadata[] = rouModelMetadataToCheck.filter(isPresent);
    if (rouModelMetadata.length > 0) {
      const rouModelMetadataCollectionIdentifiers = rouModelMetadataCollection.map(
        rouModelMetadataItem => getRouModelMetadataIdentifier(rouModelMetadataItem)!
      );
      const rouModelMetadataToAdd = rouModelMetadata.filter(rouModelMetadataItem => {
        const rouModelMetadataIdentifier = getRouModelMetadataIdentifier(rouModelMetadataItem);
        if (rouModelMetadataIdentifier == null || rouModelMetadataCollectionIdentifiers.includes(rouModelMetadataIdentifier)) {
          return false;
        }
        rouModelMetadataCollectionIdentifiers.push(rouModelMetadataIdentifier);
        return true;
      });
      return [...rouModelMetadataToAdd, ...rouModelMetadataCollection];
    }
    return rouModelMetadataCollection;
  }

  protected convertDateFromClient(rouModelMetadata: IRouModelMetadata): IRouModelMetadata {
    return Object.assign({}, rouModelMetadata, {
      commencementDate: rouModelMetadata.commencementDate?.isValid() ? rouModelMetadata.commencementDate.format(DATE_FORMAT) : undefined,
      expirationDate: rouModelMetadata.expirationDate?.isValid() ? rouModelMetadata.expirationDate.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.commencementDate = res.body.commencementDate ? dayjs(res.body.commencementDate) : undefined;
      res.body.expirationDate = res.body.expirationDate ? dayjs(res.body.expirationDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((rouModelMetadata: IRouModelMetadata) => {
        rouModelMetadata.commencementDate = rouModelMetadata.commencementDate ? dayjs(rouModelMetadata.commencementDate) : undefined;
        rouModelMetadata.expirationDate = rouModelMetadata.expirationDate ? dayjs(rouModelMetadata.expirationDate) : undefined;
      });
    }
    return res;
  }
}
