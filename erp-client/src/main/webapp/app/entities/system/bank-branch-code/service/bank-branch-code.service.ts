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

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IBankBranchCode, getBankBranchCodeIdentifier } from '../bank-branch-code.model';

export type EntityResponseType = HttpResponse<IBankBranchCode>;
export type EntityArrayResponseType = HttpResponse<IBankBranchCode[]>;

@Injectable({ providedIn: 'root' })
export class BankBranchCodeService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/bank-branch-codes');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/bank-branch-codes');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(bankBranchCode: IBankBranchCode): Observable<EntityResponseType> {
    return this.http.post<IBankBranchCode>(this.resourceUrl, bankBranchCode, { observe: 'response' });
  }

  update(bankBranchCode: IBankBranchCode): Observable<EntityResponseType> {
    return this.http.put<IBankBranchCode>(`${this.resourceUrl}/${getBankBranchCodeIdentifier(bankBranchCode) as number}`, bankBranchCode, {
      observe: 'response',
    });
  }

  partialUpdate(bankBranchCode: IBankBranchCode): Observable<EntityResponseType> {
    return this.http.patch<IBankBranchCode>(
      `${this.resourceUrl}/${getBankBranchCodeIdentifier(bankBranchCode) as number}`,
      bankBranchCode,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IBankBranchCode>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IBankBranchCode[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IBankBranchCode[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  addBankBranchCodeToCollectionIfMissing(
    bankBranchCodeCollection: IBankBranchCode[],
    ...bankBranchCodesToCheck: (IBankBranchCode | null | undefined)[]
  ): IBankBranchCode[] {
    const bankBranchCodes: IBankBranchCode[] = bankBranchCodesToCheck.filter(isPresent);
    if (bankBranchCodes.length > 0) {
      const bankBranchCodeCollectionIdentifiers = bankBranchCodeCollection.map(
        bankBranchCodeItem => getBankBranchCodeIdentifier(bankBranchCodeItem)!
      );
      const bankBranchCodesToAdd = bankBranchCodes.filter(bankBranchCodeItem => {
        const bankBranchCodeIdentifier = getBankBranchCodeIdentifier(bankBranchCodeItem);
        if (bankBranchCodeIdentifier == null || bankBranchCodeCollectionIdentifiers.includes(bankBranchCodeIdentifier)) {
          return false;
        }
        bankBranchCodeCollectionIdentifiers.push(bankBranchCodeIdentifier);
        return true;
      });
      return [...bankBranchCodesToAdd, ...bankBranchCodeCollection];
    }
    return bankBranchCodeCollection;
  }
}
