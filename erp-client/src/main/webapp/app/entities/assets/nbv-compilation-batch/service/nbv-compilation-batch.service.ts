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
import { INbvCompilationBatch, getNbvCompilationBatchIdentifier } from '../nbv-compilation-batch.model';

export type EntityResponseType = HttpResponse<INbvCompilationBatch>;
export type EntityArrayResponseType = HttpResponse<INbvCompilationBatch[]>;

@Injectable({ providedIn: 'root' })
export class NbvCompilationBatchService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/nbv-compilation-batches');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/nbv-compilation-batches');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(nbvCompilationBatch: INbvCompilationBatch): Observable<EntityResponseType> {
    return this.http.post<INbvCompilationBatch>(this.resourceUrl, nbvCompilationBatch, { observe: 'response' });
  }

  update(nbvCompilationBatch: INbvCompilationBatch): Observable<EntityResponseType> {
    return this.http.put<INbvCompilationBatch>(
      `${this.resourceUrl}/${getNbvCompilationBatchIdentifier(nbvCompilationBatch) as number}`,
      nbvCompilationBatch,
      { observe: 'response' }
    );
  }

  partialUpdate(nbvCompilationBatch: INbvCompilationBatch): Observable<EntityResponseType> {
    return this.http.patch<INbvCompilationBatch>(
      `${this.resourceUrl}/${getNbvCompilationBatchIdentifier(nbvCompilationBatch) as number}`,
      nbvCompilationBatch,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<INbvCompilationBatch>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<INbvCompilationBatch[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<INbvCompilationBatch[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  addNbvCompilationBatchToCollectionIfMissing(
    nbvCompilationBatchCollection: INbvCompilationBatch[],
    ...nbvCompilationBatchesToCheck: (INbvCompilationBatch | null | undefined)[]
  ): INbvCompilationBatch[] {
    const nbvCompilationBatches: INbvCompilationBatch[] = nbvCompilationBatchesToCheck.filter(isPresent);
    if (nbvCompilationBatches.length > 0) {
      const nbvCompilationBatchCollectionIdentifiers = nbvCompilationBatchCollection.map(
        nbvCompilationBatchItem => getNbvCompilationBatchIdentifier(nbvCompilationBatchItem)!
      );
      const nbvCompilationBatchesToAdd = nbvCompilationBatches.filter(nbvCompilationBatchItem => {
        const nbvCompilationBatchIdentifier = getNbvCompilationBatchIdentifier(nbvCompilationBatchItem);
        if (nbvCompilationBatchIdentifier == null || nbvCompilationBatchCollectionIdentifiers.includes(nbvCompilationBatchIdentifier)) {
          return false;
        }
        nbvCompilationBatchCollectionIdentifiers.push(nbvCompilationBatchIdentifier);
        return true;
      });
      return [...nbvCompilationBatchesToAdd, ...nbvCompilationBatchCollection];
    }
    return nbvCompilationBatchCollection;
  }
}
