import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IBusinessSegmentTypes, getBusinessSegmentTypesIdentifier } from '../business-segment-types.model';

export type EntityResponseType = HttpResponse<IBusinessSegmentTypes>;
export type EntityArrayResponseType = HttpResponse<IBusinessSegmentTypes[]>;

@Injectable({ providedIn: 'root' })
export class BusinessSegmentTypesService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/business-segment-types');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/business-segment-types');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(businessSegmentTypes: IBusinessSegmentTypes): Observable<EntityResponseType> {
    return this.http.post<IBusinessSegmentTypes>(this.resourceUrl, businessSegmentTypes, { observe: 'response' });
  }

  update(businessSegmentTypes: IBusinessSegmentTypes): Observable<EntityResponseType> {
    return this.http.put<IBusinessSegmentTypes>(
      `${this.resourceUrl}/${getBusinessSegmentTypesIdentifier(businessSegmentTypes) as number}`,
      businessSegmentTypes,
      { observe: 'response' }
    );
  }

  partialUpdate(businessSegmentTypes: IBusinessSegmentTypes): Observable<EntityResponseType> {
    return this.http.patch<IBusinessSegmentTypes>(
      `${this.resourceUrl}/${getBusinessSegmentTypesIdentifier(businessSegmentTypes) as number}`,
      businessSegmentTypes,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IBusinessSegmentTypes>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IBusinessSegmentTypes[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IBusinessSegmentTypes[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  addBusinessSegmentTypesToCollectionIfMissing(
    businessSegmentTypesCollection: IBusinessSegmentTypes[],
    ...businessSegmentTypesToCheck: (IBusinessSegmentTypes | null | undefined)[]
  ): IBusinessSegmentTypes[] {
    const businessSegmentTypes: IBusinessSegmentTypes[] = businessSegmentTypesToCheck.filter(isPresent);
    if (businessSegmentTypes.length > 0) {
      const businessSegmentTypesCollectionIdentifiers = businessSegmentTypesCollection.map(
        businessSegmentTypesItem => getBusinessSegmentTypesIdentifier(businessSegmentTypesItem)!
      );
      const businessSegmentTypesToAdd = businessSegmentTypes.filter(businessSegmentTypesItem => {
        const businessSegmentTypesIdentifier = getBusinessSegmentTypesIdentifier(businessSegmentTypesItem);
        if (businessSegmentTypesIdentifier == null || businessSegmentTypesCollectionIdentifiers.includes(businessSegmentTypesIdentifier)) {
          return false;
        }
        businessSegmentTypesCollectionIdentifiers.push(businessSegmentTypesIdentifier);
        return true;
      });
      return [...businessSegmentTypesToAdd, ...businessSegmentTypesCollection];
    }
    return businessSegmentTypesCollection;
  }
}
