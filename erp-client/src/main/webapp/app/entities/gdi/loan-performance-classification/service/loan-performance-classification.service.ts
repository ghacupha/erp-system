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
import { ILoanPerformanceClassification, getLoanPerformanceClassificationIdentifier } from '../loan-performance-classification.model';

export type EntityResponseType = HttpResponse<ILoanPerformanceClassification>;
export type EntityArrayResponseType = HttpResponse<ILoanPerformanceClassification[]>;

@Injectable({ providedIn: 'root' })
export class LoanPerformanceClassificationService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/loan-performance-classifications');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/loan-performance-classifications');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(loanPerformanceClassification: ILoanPerformanceClassification): Observable<EntityResponseType> {
    return this.http.post<ILoanPerformanceClassification>(this.resourceUrl, loanPerformanceClassification, { observe: 'response' });
  }

  update(loanPerformanceClassification: ILoanPerformanceClassification): Observable<EntityResponseType> {
    return this.http.put<ILoanPerformanceClassification>(
      `${this.resourceUrl}/${getLoanPerformanceClassificationIdentifier(loanPerformanceClassification) as number}`,
      loanPerformanceClassification,
      { observe: 'response' }
    );
  }

  partialUpdate(loanPerformanceClassification: ILoanPerformanceClassification): Observable<EntityResponseType> {
    return this.http.patch<ILoanPerformanceClassification>(
      `${this.resourceUrl}/${getLoanPerformanceClassificationIdentifier(loanPerformanceClassification) as number}`,
      loanPerformanceClassification,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ILoanPerformanceClassification>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ILoanPerformanceClassification[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ILoanPerformanceClassification[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  addLoanPerformanceClassificationToCollectionIfMissing(
    loanPerformanceClassificationCollection: ILoanPerformanceClassification[],
    ...loanPerformanceClassificationsToCheck: (ILoanPerformanceClassification | null | undefined)[]
  ): ILoanPerformanceClassification[] {
    const loanPerformanceClassifications: ILoanPerformanceClassification[] = loanPerformanceClassificationsToCheck.filter(isPresent);
    if (loanPerformanceClassifications.length > 0) {
      const loanPerformanceClassificationCollectionIdentifiers = loanPerformanceClassificationCollection.map(
        loanPerformanceClassificationItem => getLoanPerformanceClassificationIdentifier(loanPerformanceClassificationItem)!
      );
      const loanPerformanceClassificationsToAdd = loanPerformanceClassifications.filter(loanPerformanceClassificationItem => {
        const loanPerformanceClassificationIdentifier = getLoanPerformanceClassificationIdentifier(loanPerformanceClassificationItem);
        if (
          loanPerformanceClassificationIdentifier == null ||
          loanPerformanceClassificationCollectionIdentifiers.includes(loanPerformanceClassificationIdentifier)
        ) {
          return false;
        }
        loanPerformanceClassificationCollectionIdentifiers.push(loanPerformanceClassificationIdentifier);
        return true;
      });
      return [...loanPerformanceClassificationsToAdd, ...loanPerformanceClassificationCollection];
    }
    return loanPerformanceClassificationCollection;
  }
}
