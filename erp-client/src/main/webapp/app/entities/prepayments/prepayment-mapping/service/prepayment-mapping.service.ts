import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IPrepaymentMapping, getPrepaymentMappingIdentifier } from '../prepayment-mapping.model';

export type EntityResponseType = HttpResponse<IPrepaymentMapping>;
export type EntityArrayResponseType = HttpResponse<IPrepaymentMapping[]>;

@Injectable({ providedIn: 'root' })
export class PrepaymentMappingService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/prepayment-mappings');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/prepayment-mappings');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(prepaymentMapping: IPrepaymentMapping): Observable<EntityResponseType> {
    return this.http.post<IPrepaymentMapping>(this.resourceUrl, prepaymentMapping, { observe: 'response' });
  }

  update(prepaymentMapping: IPrepaymentMapping): Observable<EntityResponseType> {
    return this.http.put<IPrepaymentMapping>(
      `${this.resourceUrl}/${getPrepaymentMappingIdentifier(prepaymentMapping) as number}`,
      prepaymentMapping,
      { observe: 'response' }
    );
  }

  partialUpdate(prepaymentMapping: IPrepaymentMapping): Observable<EntityResponseType> {
    return this.http.patch<IPrepaymentMapping>(
      `${this.resourceUrl}/${getPrepaymentMappingIdentifier(prepaymentMapping) as number}`,
      prepaymentMapping,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPrepaymentMapping>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPrepaymentMapping[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPrepaymentMapping[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  addPrepaymentMappingToCollectionIfMissing(
    prepaymentMappingCollection: IPrepaymentMapping[],
    ...prepaymentMappingsToCheck: (IPrepaymentMapping | null | undefined)[]
  ): IPrepaymentMapping[] {
    const prepaymentMappings: IPrepaymentMapping[] = prepaymentMappingsToCheck.filter(isPresent);
    if (prepaymentMappings.length > 0) {
      const prepaymentMappingCollectionIdentifiers = prepaymentMappingCollection.map(
        prepaymentMappingItem => getPrepaymentMappingIdentifier(prepaymentMappingItem)!
      );
      const prepaymentMappingsToAdd = prepaymentMappings.filter(prepaymentMappingItem => {
        const prepaymentMappingIdentifier = getPrepaymentMappingIdentifier(prepaymentMappingItem);
        if (prepaymentMappingIdentifier == null || prepaymentMappingCollectionIdentifiers.includes(prepaymentMappingIdentifier)) {
          return false;
        }
        prepaymentMappingCollectionIdentifiers.push(prepaymentMappingIdentifier);
        return true;
      });
      return [...prepaymentMappingsToAdd, ...prepaymentMappingCollection];
    }
    return prepaymentMappingCollection;
  }
}
