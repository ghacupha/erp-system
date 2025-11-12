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
import { IIFRS16LeaseContract, getIFRS16LeaseContractIdentifier } from '../ifrs-16-lease-contract.model';

export type EntityResponseType = HttpResponse<IIFRS16LeaseContract>;
export type EntityArrayResponseType = HttpResponse<IIFRS16LeaseContract[]>;

@Injectable({ providedIn: 'root' })
export class IFRS16LeaseContractService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/ifrs-16-lease-contracts');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/ifrs-16-lease-contracts');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(iFRS16LeaseContract: IIFRS16LeaseContract): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(iFRS16LeaseContract);
    return this.http
      .post<IIFRS16LeaseContract>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(iFRS16LeaseContract: IIFRS16LeaseContract): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(iFRS16LeaseContract);
    return this.http
      .put<IIFRS16LeaseContract>(`${this.resourceUrl}/${getIFRS16LeaseContractIdentifier(iFRS16LeaseContract) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(iFRS16LeaseContract: IIFRS16LeaseContract): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(iFRS16LeaseContract);
    return this.http
      .patch<IIFRS16LeaseContract>(`${this.resourceUrl}/${getIFRS16LeaseContractIdentifier(iFRS16LeaseContract) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IIFRS16LeaseContract>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IIFRS16LeaseContract[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IIFRS16LeaseContract[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  addIFRS16LeaseContractToCollectionIfMissing(
    iFRS16LeaseContractCollection: IIFRS16LeaseContract[],
    ...iFRS16LeaseContractsToCheck: (IIFRS16LeaseContract | null | undefined)[]
  ): IIFRS16LeaseContract[] {
    const iFRS16LeaseContracts: IIFRS16LeaseContract[] = iFRS16LeaseContractsToCheck.filter(isPresent);
    if (iFRS16LeaseContracts.length > 0) {
      const iFRS16LeaseContractCollectionIdentifiers = iFRS16LeaseContractCollection.map(
        iFRS16LeaseContractItem => getIFRS16LeaseContractIdentifier(iFRS16LeaseContractItem)!
      );
      const iFRS16LeaseContractsToAdd = iFRS16LeaseContracts.filter(iFRS16LeaseContractItem => {
        const iFRS16LeaseContractIdentifier = getIFRS16LeaseContractIdentifier(iFRS16LeaseContractItem);
        if (iFRS16LeaseContractIdentifier == null || iFRS16LeaseContractCollectionIdentifiers.includes(iFRS16LeaseContractIdentifier)) {
          return false;
        }
        iFRS16LeaseContractCollectionIdentifiers.push(iFRS16LeaseContractIdentifier);
        return true;
      });
      return [...iFRS16LeaseContractsToAdd, ...iFRS16LeaseContractCollection];
    }
    return iFRS16LeaseContractCollection;
  }

  protected convertDateFromClient(iFRS16LeaseContract: IIFRS16LeaseContract): IIFRS16LeaseContract {
    return Object.assign({}, iFRS16LeaseContract, {
      inceptionDate: iFRS16LeaseContract.inceptionDate?.isValid() ? iFRS16LeaseContract.inceptionDate.format(DATE_FORMAT) : undefined,
      commencementDate: iFRS16LeaseContract.commencementDate?.isValid()
        ? iFRS16LeaseContract.commencementDate.format(DATE_FORMAT)
        : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.inceptionDate = res.body.inceptionDate ? dayjs(res.body.inceptionDate) : undefined;
      res.body.commencementDate = res.body.commencementDate ? dayjs(res.body.commencementDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((iFRS16LeaseContract: IIFRS16LeaseContract) => {
        iFRS16LeaseContract.inceptionDate = iFRS16LeaseContract.inceptionDate ? dayjs(iFRS16LeaseContract.inceptionDate) : undefined;
        iFRS16LeaseContract.commencementDate = iFRS16LeaseContract.commencementDate
          ? dayjs(iFRS16LeaseContract.commencementDate)
          : undefined;
      });
    }
    return res;
  }
}
