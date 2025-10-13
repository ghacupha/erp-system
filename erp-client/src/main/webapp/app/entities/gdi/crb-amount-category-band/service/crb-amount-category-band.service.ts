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
import { ICrbAmountCategoryBand, getCrbAmountCategoryBandIdentifier } from '../crb-amount-category-band.model';

export type EntityResponseType = HttpResponse<ICrbAmountCategoryBand>;
export type EntityArrayResponseType = HttpResponse<ICrbAmountCategoryBand[]>;

@Injectable({ providedIn: 'root' })
export class CrbAmountCategoryBandService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/crb-amount-category-bands');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/crb-amount-category-bands');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(crbAmountCategoryBand: ICrbAmountCategoryBand): Observable<EntityResponseType> {
    return this.http.post<ICrbAmountCategoryBand>(this.resourceUrl, crbAmountCategoryBand, { observe: 'response' });
  }

  update(crbAmountCategoryBand: ICrbAmountCategoryBand): Observable<EntityResponseType> {
    return this.http.put<ICrbAmountCategoryBand>(
      `${this.resourceUrl}/${getCrbAmountCategoryBandIdentifier(crbAmountCategoryBand) as number}`,
      crbAmountCategoryBand,
      { observe: 'response' }
    );
  }

  partialUpdate(crbAmountCategoryBand: ICrbAmountCategoryBand): Observable<EntityResponseType> {
    return this.http.patch<ICrbAmountCategoryBand>(
      `${this.resourceUrl}/${getCrbAmountCategoryBandIdentifier(crbAmountCategoryBand) as number}`,
      crbAmountCategoryBand,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICrbAmountCategoryBand>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICrbAmountCategoryBand[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICrbAmountCategoryBand[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  addCrbAmountCategoryBandToCollectionIfMissing(
    crbAmountCategoryBandCollection: ICrbAmountCategoryBand[],
    ...crbAmountCategoryBandsToCheck: (ICrbAmountCategoryBand | null | undefined)[]
  ): ICrbAmountCategoryBand[] {
    const crbAmountCategoryBands: ICrbAmountCategoryBand[] = crbAmountCategoryBandsToCheck.filter(isPresent);
    if (crbAmountCategoryBands.length > 0) {
      const crbAmountCategoryBandCollectionIdentifiers = crbAmountCategoryBandCollection.map(
        crbAmountCategoryBandItem => getCrbAmountCategoryBandIdentifier(crbAmountCategoryBandItem)!
      );
      const crbAmountCategoryBandsToAdd = crbAmountCategoryBands.filter(crbAmountCategoryBandItem => {
        const crbAmountCategoryBandIdentifier = getCrbAmountCategoryBandIdentifier(crbAmountCategoryBandItem);
        if (
          crbAmountCategoryBandIdentifier == null ||
          crbAmountCategoryBandCollectionIdentifiers.includes(crbAmountCategoryBandIdentifier)
        ) {
          return false;
        }
        crbAmountCategoryBandCollectionIdentifiers.push(crbAmountCategoryBandIdentifier);
        return true;
      });
      return [...crbAmountCategoryBandsToAdd, ...crbAmountCategoryBandCollection];
    }
    return crbAmountCategoryBandCollection;
  }
}
