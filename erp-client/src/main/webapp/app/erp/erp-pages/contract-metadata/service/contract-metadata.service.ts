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
import { IContractMetadata, getContractMetadataIdentifier } from '../contract-metadata.model';

export type EntityResponseType = HttpResponse<IContractMetadata>;
export type EntityArrayResponseType = HttpResponse<IContractMetadata[]>;

@Injectable({ providedIn: 'root' })
export class ContractMetadataService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/contract-metadata');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/contract-metadata');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(contractMetadata: IContractMetadata): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(contractMetadata);
    return this.http
      .post<IContractMetadata>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(contractMetadata: IContractMetadata): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(contractMetadata);
    return this.http
      .put<IContractMetadata>(`${this.resourceUrl}/${getContractMetadataIdentifier(contractMetadata) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(contractMetadata: IContractMetadata): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(contractMetadata);
    return this.http
      .patch<IContractMetadata>(`${this.resourceUrl}/${getContractMetadataIdentifier(contractMetadata) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IContractMetadata>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IContractMetadata[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IContractMetadata[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  addContractMetadataToCollectionIfMissing(
    contractMetadataCollection: IContractMetadata[],
    ...contractMetadataToCheck: (IContractMetadata | null | undefined)[]
  ): IContractMetadata[] {
    const contractMetadata: IContractMetadata[] = contractMetadataToCheck.filter(isPresent);
    if (contractMetadata.length > 0) {
      const contractMetadataCollectionIdentifiers = contractMetadataCollection.map(
        contractMetadataItem => getContractMetadataIdentifier(contractMetadataItem)!
      );
      const contractMetadataToAdd = contractMetadata.filter(contractMetadataItem => {
        const contractMetadataIdentifier = getContractMetadataIdentifier(contractMetadataItem);
        if (contractMetadataIdentifier == null || contractMetadataCollectionIdentifiers.includes(contractMetadataIdentifier)) {
          return false;
        }
        contractMetadataCollectionIdentifiers.push(contractMetadataIdentifier);
        return true;
      });
      return [...contractMetadataToAdd, ...contractMetadataCollection];
    }
    return contractMetadataCollection;
  }

  protected convertDateFromClient(contractMetadata: IContractMetadata): IContractMetadata {
    return Object.assign({}, contractMetadata, {
      startDate: contractMetadata.startDate?.isValid() ? contractMetadata.startDate.format(DATE_FORMAT) : undefined,
      terminationDate: contractMetadata.terminationDate?.isValid() ? contractMetadata.terminationDate.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.startDate = res.body.startDate ? dayjs(res.body.startDate) : undefined;
      res.body.terminationDate = res.body.terminationDate ? dayjs(res.body.terminationDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((contractMetadata: IContractMetadata) => {
        contractMetadata.startDate = contractMetadata.startDate ? dayjs(contractMetadata.startDate) : undefined;
        contractMetadata.terminationDate = contractMetadata.terminationDate ? dayjs(contractMetadata.terminationDate) : undefined;
      });
    }
    return res;
  }
}
