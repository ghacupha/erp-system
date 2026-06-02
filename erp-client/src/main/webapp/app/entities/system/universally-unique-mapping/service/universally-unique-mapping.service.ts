import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IUniversallyUniqueMapping, getUniversallyUniqueMappingIdentifier } from '../universally-unique-mapping.model';

export type EntityResponseType = HttpResponse<IUniversallyUniqueMapping>;
export type EntityArrayResponseType = HttpResponse<IUniversallyUniqueMapping[]>;

@Injectable({ providedIn: 'root' })
export class UniversallyUniqueMappingService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/universally-unique-mappings');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/universally-unique-mappings');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(universallyUniqueMapping: IUniversallyUniqueMapping): Observable<EntityResponseType> {
    return this.http.post<IUniversallyUniqueMapping>(this.resourceUrl, universallyUniqueMapping, { observe: 'response' });
  }

  update(universallyUniqueMapping: IUniversallyUniqueMapping): Observable<EntityResponseType> {
    return this.http.put<IUniversallyUniqueMapping>(
      `${this.resourceUrl}/${getUniversallyUniqueMappingIdentifier(universallyUniqueMapping) as number}`,
      universallyUniqueMapping,
      { observe: 'response' }
    );
  }

  partialUpdate(universallyUniqueMapping: IUniversallyUniqueMapping): Observable<EntityResponseType> {
    return this.http.patch<IUniversallyUniqueMapping>(
      `${this.resourceUrl}/${getUniversallyUniqueMappingIdentifier(universallyUniqueMapping) as number}`,
      universallyUniqueMapping,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IUniversallyUniqueMapping>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IUniversallyUniqueMapping[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IUniversallyUniqueMapping[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  addUniversallyUniqueMappingToCollectionIfMissing(
    universallyUniqueMappingCollection: IUniversallyUniqueMapping[],
    ...universallyUniqueMappingsToCheck: (IUniversallyUniqueMapping | null | undefined)[]
  ): IUniversallyUniqueMapping[] {
    const universallyUniqueMappings: IUniversallyUniqueMapping[] = universallyUniqueMappingsToCheck.filter(isPresent);
    if (universallyUniqueMappings.length > 0) {
      const universallyUniqueMappingCollectionIdentifiers = universallyUniqueMappingCollection.map(
        universallyUniqueMappingItem => getUniversallyUniqueMappingIdentifier(universallyUniqueMappingItem)!
      );
      const universallyUniqueMappingsToAdd = universallyUniqueMappings.filter(universallyUniqueMappingItem => {
        const universallyUniqueMappingIdentifier = getUniversallyUniqueMappingIdentifier(universallyUniqueMappingItem);
        if (
          universallyUniqueMappingIdentifier == null ||
          universallyUniqueMappingCollectionIdentifiers.includes(universallyUniqueMappingIdentifier)
        ) {
          return false;
        }
        universallyUniqueMappingCollectionIdentifiers.push(universallyUniqueMappingIdentifier);
        return true;
      });
      return [...universallyUniqueMappingsToAdd, ...universallyUniqueMappingCollection];
    }
    return universallyUniqueMappingCollection;
  }
}
