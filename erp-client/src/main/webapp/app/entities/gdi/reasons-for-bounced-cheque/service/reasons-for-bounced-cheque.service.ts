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
import { IReasonsForBouncedCheque, getReasonsForBouncedChequeIdentifier } from '../reasons-for-bounced-cheque.model';

export type EntityResponseType = HttpResponse<IReasonsForBouncedCheque>;
export type EntityArrayResponseType = HttpResponse<IReasonsForBouncedCheque[]>;

@Injectable({ providedIn: 'root' })
export class ReasonsForBouncedChequeService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/reasons-for-bounced-cheques');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/reasons-for-bounced-cheques');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(reasonsForBouncedCheque: IReasonsForBouncedCheque): Observable<EntityResponseType> {
    return this.http.post<IReasonsForBouncedCheque>(this.resourceUrl, reasonsForBouncedCheque, { observe: 'response' });
  }

  update(reasonsForBouncedCheque: IReasonsForBouncedCheque): Observable<EntityResponseType> {
    return this.http.put<IReasonsForBouncedCheque>(
      `${this.resourceUrl}/${getReasonsForBouncedChequeIdentifier(reasonsForBouncedCheque) as number}`,
      reasonsForBouncedCheque,
      { observe: 'response' }
    );
  }

  partialUpdate(reasonsForBouncedCheque: IReasonsForBouncedCheque): Observable<EntityResponseType> {
    return this.http.patch<IReasonsForBouncedCheque>(
      `${this.resourceUrl}/${getReasonsForBouncedChequeIdentifier(reasonsForBouncedCheque) as number}`,
      reasonsForBouncedCheque,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IReasonsForBouncedCheque>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IReasonsForBouncedCheque[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IReasonsForBouncedCheque[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  addReasonsForBouncedChequeToCollectionIfMissing(
    reasonsForBouncedChequeCollection: IReasonsForBouncedCheque[],
    ...reasonsForBouncedChequesToCheck: (IReasonsForBouncedCheque | null | undefined)[]
  ): IReasonsForBouncedCheque[] {
    const reasonsForBouncedCheques: IReasonsForBouncedCheque[] = reasonsForBouncedChequesToCheck.filter(isPresent);
    if (reasonsForBouncedCheques.length > 0) {
      const reasonsForBouncedChequeCollectionIdentifiers = reasonsForBouncedChequeCollection.map(
        reasonsForBouncedChequeItem => getReasonsForBouncedChequeIdentifier(reasonsForBouncedChequeItem)!
      );
      const reasonsForBouncedChequesToAdd = reasonsForBouncedCheques.filter(reasonsForBouncedChequeItem => {
        const reasonsForBouncedChequeIdentifier = getReasonsForBouncedChequeIdentifier(reasonsForBouncedChequeItem);
        if (
          reasonsForBouncedChequeIdentifier == null ||
          reasonsForBouncedChequeCollectionIdentifiers.includes(reasonsForBouncedChequeIdentifier)
        ) {
          return false;
        }
        reasonsForBouncedChequeCollectionIdentifiers.push(reasonsForBouncedChequeIdentifier);
        return true;
      });
      return [...reasonsForBouncedChequesToAdd, ...reasonsForBouncedChequeCollection];
    }
    return reasonsForBouncedChequeCollection;
  }
}
