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
import { ILeaseModelMetadata, getLeaseModelMetadataIdentifier } from '../lease-model-metadata.model';

export type EntityResponseType = HttpResponse<ILeaseModelMetadata>;
export type EntityArrayResponseType = HttpResponse<ILeaseModelMetadata[]>;

@Injectable({ providedIn: 'root' })
export class LeaseModelMetadataService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/leases/lease-model-metadata');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/leases/_search/lease-model-metadata');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(leaseModelMetadata: ILeaseModelMetadata): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(leaseModelMetadata);
    return this.http
      .post<ILeaseModelMetadata>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(leaseModelMetadata: ILeaseModelMetadata): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(leaseModelMetadata);
    return this.http
      .put<ILeaseModelMetadata>(`${this.resourceUrl}/${getLeaseModelMetadataIdentifier(leaseModelMetadata) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(leaseModelMetadata: ILeaseModelMetadata): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(leaseModelMetadata);
    return this.http
      .patch<ILeaseModelMetadata>(`${this.resourceUrl}/${getLeaseModelMetadataIdentifier(leaseModelMetadata) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ILeaseModelMetadata>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ILeaseModelMetadata[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ILeaseModelMetadata[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  addLeaseModelMetadataToCollectionIfMissing(
    leaseModelMetadataCollection: ILeaseModelMetadata[],
    ...leaseModelMetadataToCheck: (ILeaseModelMetadata | null | undefined)[]
  ): ILeaseModelMetadata[] {
    const leaseModelMetadata: ILeaseModelMetadata[] = leaseModelMetadataToCheck.filter(isPresent);
    if (leaseModelMetadata.length > 0) {
      const leaseModelMetadataCollectionIdentifiers = leaseModelMetadataCollection.map(
        leaseModelMetadataItem => getLeaseModelMetadataIdentifier(leaseModelMetadataItem)!
      );
      const leaseModelMetadataToAdd = leaseModelMetadata.filter(leaseModelMetadataItem => {
        const leaseModelMetadataIdentifier = getLeaseModelMetadataIdentifier(leaseModelMetadataItem);
        if (leaseModelMetadataIdentifier == null || leaseModelMetadataCollectionIdentifiers.includes(leaseModelMetadataIdentifier)) {
          return false;
        }
        leaseModelMetadataCollectionIdentifiers.push(leaseModelMetadataIdentifier);
        return true;
      });
      return [...leaseModelMetadataToAdd, ...leaseModelMetadataCollection];
    }
    return leaseModelMetadataCollection;
  }

  protected convertDateFromClient(leaseModelMetadata: ILeaseModelMetadata): ILeaseModelMetadata {
    return Object.assign({}, leaseModelMetadata, {
      commencementDate: leaseModelMetadata.commencementDate?.isValid()
        ? leaseModelMetadata.commencementDate.format(DATE_FORMAT)
        : undefined,
      terminalDate: leaseModelMetadata.terminalDate?.isValid() ? leaseModelMetadata.terminalDate.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.commencementDate = res.body.commencementDate ? dayjs(res.body.commencementDate) : undefined;
      res.body.terminalDate = res.body.terminalDate ? dayjs(res.body.terminalDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((leaseModelMetadata: ILeaseModelMetadata) => {
        leaseModelMetadata.commencementDate = leaseModelMetadata.commencementDate ? dayjs(leaseModelMetadata.commencementDate) : undefined;
        leaseModelMetadata.terminalDate = leaseModelMetadata.terminalDate ? dayjs(leaseModelMetadata.terminalDate) : undefined;
      });
    }
    return res;
  }
}
