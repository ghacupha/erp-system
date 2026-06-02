import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { ISecurityType, getSecurityTypeIdentifier } from '../security-type.model';

export type EntityResponseType = HttpResponse<ISecurityType>;
export type EntityArrayResponseType = HttpResponse<ISecurityType[]>;

@Injectable({ providedIn: 'root' })
export class SecurityTypeService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/security-types');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/security-types');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(securityType: ISecurityType): Observable<EntityResponseType> {
    return this.http.post<ISecurityType>(this.resourceUrl, securityType, { observe: 'response' });
  }

  update(securityType: ISecurityType): Observable<EntityResponseType> {
    return this.http.put<ISecurityType>(`${this.resourceUrl}/${getSecurityTypeIdentifier(securityType) as number}`, securityType, {
      observe: 'response',
    });
  }

  partialUpdate(securityType: ISecurityType): Observable<EntityResponseType> {
    return this.http.patch<ISecurityType>(`${this.resourceUrl}/${getSecurityTypeIdentifier(securityType) as number}`, securityType, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ISecurityType>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ISecurityType[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ISecurityType[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  addSecurityTypeToCollectionIfMissing(
    securityTypeCollection: ISecurityType[],
    ...securityTypesToCheck: (ISecurityType | null | undefined)[]
  ): ISecurityType[] {
    const securityTypes: ISecurityType[] = securityTypesToCheck.filter(isPresent);
    if (securityTypes.length > 0) {
      const securityTypeCollectionIdentifiers = securityTypeCollection.map(
        securityTypeItem => getSecurityTypeIdentifier(securityTypeItem)!
      );
      const securityTypesToAdd = securityTypes.filter(securityTypeItem => {
        const securityTypeIdentifier = getSecurityTypeIdentifier(securityTypeItem);
        if (securityTypeIdentifier == null || securityTypeCollectionIdentifiers.includes(securityTypeIdentifier)) {
          return false;
        }
        securityTypeCollectionIdentifiers.push(securityTypeIdentifier);
        return true;
      });
      return [...securityTypesToAdd, ...securityTypeCollection];
    }
    return securityTypeCollection;
  }
}
