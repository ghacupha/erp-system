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
import { ILeaseContract, getLeaseContractIdentifier } from '../lease-contract.model';

export type EntityResponseType = HttpResponse<ILeaseContract>;
export type EntityArrayResponseType = HttpResponse<ILeaseContract[]>;

@Injectable({ providedIn: 'root' })
export class LeaseContractService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/lease-contracts');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/lease-contracts');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(leaseContract: ILeaseContract): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(leaseContract);
    return this.http
      .post<ILeaseContract>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(leaseContract: ILeaseContract): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(leaseContract);
    return this.http
      .put<ILeaseContract>(`${this.resourceUrl}/${getLeaseContractIdentifier(leaseContract) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(leaseContract: ILeaseContract): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(leaseContract);
    return this.http
      .patch<ILeaseContract>(`${this.resourceUrl}/${getLeaseContractIdentifier(leaseContract) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ILeaseContract>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ILeaseContract[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ILeaseContract[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  addLeaseContractToCollectionIfMissing(
    leaseContractCollection: ILeaseContract[],
    ...leaseContractsToCheck: (ILeaseContract | null | undefined)[]
  ): ILeaseContract[] {
    const leaseContracts: ILeaseContract[] = leaseContractsToCheck.filter(isPresent);
    if (leaseContracts.length > 0) {
      const leaseContractCollectionIdentifiers = leaseContractCollection.map(
        leaseContractItem => getLeaseContractIdentifier(leaseContractItem)!
      );
      const leaseContractsToAdd = leaseContracts.filter(leaseContractItem => {
        const leaseContractIdentifier = getLeaseContractIdentifier(leaseContractItem);
        if (leaseContractIdentifier == null || leaseContractCollectionIdentifiers.includes(leaseContractIdentifier)) {
          return false;
        }
        leaseContractCollectionIdentifiers.push(leaseContractIdentifier);
        return true;
      });
      return [...leaseContractsToAdd, ...leaseContractCollection];
    }
    return leaseContractCollection;
  }

  protected convertDateFromClient(leaseContract: ILeaseContract): ILeaseContract {
    return Object.assign({}, leaseContract, {
      commencementDate: leaseContract.commencementDate?.isValid() ? leaseContract.commencementDate.format(DATE_FORMAT) : undefined,
      terminalDate: leaseContract.terminalDate?.isValid() ? leaseContract.terminalDate.format(DATE_FORMAT) : undefined,
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
      res.body.forEach((leaseContract: ILeaseContract) => {
        leaseContract.commencementDate = leaseContract.commencementDate ? dayjs(leaseContract.commencementDate) : undefined;
        leaseContract.terminalDate = leaseContract.terminalDate ? dayjs(leaseContract.terminalDate) : undefined;
      });
    }
    return res;
  }
}
