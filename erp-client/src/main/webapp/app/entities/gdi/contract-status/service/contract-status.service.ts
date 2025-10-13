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

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IContractStatus, getContractStatusIdentifier } from '../contract-status.model';

export type EntityResponseType = HttpResponse<IContractStatus>;
export type EntityArrayResponseType = HttpResponse<IContractStatus[]>;

@Injectable({ providedIn: 'root' })
export class ContractStatusService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/contract-statuses');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/contract-statuses');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(contractStatus: IContractStatus): Observable<EntityResponseType> {
    return this.http.post<IContractStatus>(this.resourceUrl, contractStatus, { observe: 'response' });
  }

  update(contractStatus: IContractStatus): Observable<EntityResponseType> {
    return this.http.put<IContractStatus>(`${this.resourceUrl}/${getContractStatusIdentifier(contractStatus) as number}`, contractStatus, {
      observe: 'response',
    });
  }

  partialUpdate(contractStatus: IContractStatus): Observable<EntityResponseType> {
    return this.http.patch<IContractStatus>(
      `${this.resourceUrl}/${getContractStatusIdentifier(contractStatus) as number}`,
      contractStatus,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IContractStatus>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IContractStatus[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IContractStatus[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  addContractStatusToCollectionIfMissing(
    contractStatusCollection: IContractStatus[],
    ...contractStatusesToCheck: (IContractStatus | null | undefined)[]
  ): IContractStatus[] {
    const contractStatuses: IContractStatus[] = contractStatusesToCheck.filter(isPresent);
    if (contractStatuses.length > 0) {
      const contractStatusCollectionIdentifiers = contractStatusCollection.map(
        contractStatusItem => getContractStatusIdentifier(contractStatusItem)!
      );
      const contractStatusesToAdd = contractStatuses.filter(contractStatusItem => {
        const contractStatusIdentifier = getContractStatusIdentifier(contractStatusItem);
        if (contractStatusIdentifier == null || contractStatusCollectionIdentifiers.includes(contractStatusIdentifier)) {
          return false;
        }
        contractStatusCollectionIdentifiers.push(contractStatusIdentifier);
        return true;
      });
      return [...contractStatusesToAdd, ...contractStatusCollection];
    }
    return contractStatusCollection;
  }
}
