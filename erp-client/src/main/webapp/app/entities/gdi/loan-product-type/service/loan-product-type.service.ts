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
import { ILoanProductType, getLoanProductTypeIdentifier } from '../loan-product-type.model';

export type EntityResponseType = HttpResponse<ILoanProductType>;
export type EntityArrayResponseType = HttpResponse<ILoanProductType[]>;

@Injectable({ providedIn: 'root' })
export class LoanProductTypeService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/loan-product-types');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/loan-product-types');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(loanProductType: ILoanProductType): Observable<EntityResponseType> {
    return this.http.post<ILoanProductType>(this.resourceUrl, loanProductType, { observe: 'response' });
  }

  update(loanProductType: ILoanProductType): Observable<EntityResponseType> {
    return this.http.put<ILoanProductType>(
      `${this.resourceUrl}/${getLoanProductTypeIdentifier(loanProductType) as number}`,
      loanProductType,
      { observe: 'response' }
    );
  }

  partialUpdate(loanProductType: ILoanProductType): Observable<EntityResponseType> {
    return this.http.patch<ILoanProductType>(
      `${this.resourceUrl}/${getLoanProductTypeIdentifier(loanProductType) as number}`,
      loanProductType,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ILoanProductType>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ILoanProductType[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ILoanProductType[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  addLoanProductTypeToCollectionIfMissing(
    loanProductTypeCollection: ILoanProductType[],
    ...loanProductTypesToCheck: (ILoanProductType | null | undefined)[]
  ): ILoanProductType[] {
    const loanProductTypes: ILoanProductType[] = loanProductTypesToCheck.filter(isPresent);
    if (loanProductTypes.length > 0) {
      const loanProductTypeCollectionIdentifiers = loanProductTypeCollection.map(
        loanProductTypeItem => getLoanProductTypeIdentifier(loanProductTypeItem)!
      );
      const loanProductTypesToAdd = loanProductTypes.filter(loanProductTypeItem => {
        const loanProductTypeIdentifier = getLoanProductTypeIdentifier(loanProductTypeItem);
        if (loanProductTypeIdentifier == null || loanProductTypeCollectionIdentifiers.includes(loanProductTypeIdentifier)) {
          return false;
        }
        loanProductTypeCollectionIdentifiers.push(loanProductTypeIdentifier);
        return true;
      });
      return [...loanProductTypesToAdd, ...loanProductTypeCollection];
    }
    return loanProductTypeCollection;
  }
}
