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
import { ISecurityClassificationType, getSecurityClassificationTypeIdentifier } from '../security-classification-type.model';

export type EntityResponseType = HttpResponse<ISecurityClassificationType>;
export type EntityArrayResponseType = HttpResponse<ISecurityClassificationType[]>;

@Injectable({ providedIn: 'root' })
export class SecurityClassificationTypeService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/security-classification-types');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/security-classification-types');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(securityClassificationType: ISecurityClassificationType): Observable<EntityResponseType> {
    return this.http.post<ISecurityClassificationType>(this.resourceUrl, securityClassificationType, { observe: 'response' });
  }

  update(securityClassificationType: ISecurityClassificationType): Observable<EntityResponseType> {
    return this.http.put<ISecurityClassificationType>(
      `${this.resourceUrl}/${getSecurityClassificationTypeIdentifier(securityClassificationType) as number}`,
      securityClassificationType,
      { observe: 'response' }
    );
  }

  partialUpdate(securityClassificationType: ISecurityClassificationType): Observable<EntityResponseType> {
    return this.http.patch<ISecurityClassificationType>(
      `${this.resourceUrl}/${getSecurityClassificationTypeIdentifier(securityClassificationType) as number}`,
      securityClassificationType,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ISecurityClassificationType>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ISecurityClassificationType[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ISecurityClassificationType[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  addSecurityClassificationTypeToCollectionIfMissing(
    securityClassificationTypeCollection: ISecurityClassificationType[],
    ...securityClassificationTypesToCheck: (ISecurityClassificationType | null | undefined)[]
  ): ISecurityClassificationType[] {
    const securityClassificationTypes: ISecurityClassificationType[] = securityClassificationTypesToCheck.filter(isPresent);
    if (securityClassificationTypes.length > 0) {
      const securityClassificationTypeCollectionIdentifiers = securityClassificationTypeCollection.map(
        securityClassificationTypeItem => getSecurityClassificationTypeIdentifier(securityClassificationTypeItem)!
      );
      const securityClassificationTypesToAdd = securityClassificationTypes.filter(securityClassificationTypeItem => {
        const securityClassificationTypeIdentifier = getSecurityClassificationTypeIdentifier(securityClassificationTypeItem);
        if (
          securityClassificationTypeIdentifier == null ||
          securityClassificationTypeCollectionIdentifiers.includes(securityClassificationTypeIdentifier)
        ) {
          return false;
        }
        securityClassificationTypeCollectionIdentifiers.push(securityClassificationTypeIdentifier);
        return true;
      });
      return [...securityClassificationTypesToAdd, ...securityClassificationTypeCollection];
    }
    return securityClassificationTypeCollection;
  }
}
