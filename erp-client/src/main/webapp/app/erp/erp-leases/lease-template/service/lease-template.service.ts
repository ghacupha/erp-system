///
/// Erp System - Mark X No 11 (Jehoiada Series) Client 1.7.9
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
import { ILeaseTemplate, getLeaseTemplateIdentifier } from '../lease-template.model';

export type EntityResponseType = HttpResponse<ILeaseTemplate>;
export type EntityArrayResponseType = HttpResponse<ILeaseTemplate[]>;

@Injectable({ providedIn: 'root' })
export class LeaseTemplateService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/leases/lease-templates');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/leases/_search/lease-templates');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(leaseTemplate: ILeaseTemplate): Observable<EntityResponseType> {
    return this.http.post<ILeaseTemplate>(this.resourceUrl, leaseTemplate, { observe: 'response' });
  }

  update(leaseTemplate: ILeaseTemplate): Observable<EntityResponseType> {
    return this.http.put<ILeaseTemplate>(`${this.resourceUrl}/${getLeaseTemplateIdentifier(leaseTemplate) as number}`, leaseTemplate, {
      observe: 'response',
    });
  }

  partialUpdate(leaseTemplate: ILeaseTemplate): Observable<EntityResponseType> {
    return this.http.patch<ILeaseTemplate>(`${this.resourceUrl}/${getLeaseTemplateIdentifier(leaseTemplate) as number}`, leaseTemplate, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ILeaseTemplate>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ILeaseTemplate[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ILeaseTemplate[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  addLeaseTemplateToCollectionIfMissing(
    leaseTemplateCollection: ILeaseTemplate[],
    ...leaseTemplatesToCheck: (ILeaseTemplate | null | undefined)[]
  ): ILeaseTemplate[] {
    const leaseTemplates: ILeaseTemplate[] = leaseTemplatesToCheck.filter(isPresent);
    if (leaseTemplates.length > 0) {
      const leaseTemplateCollectionIdentifiers = leaseTemplateCollection.map(
        leaseTemplateItem => getLeaseTemplateIdentifier(leaseTemplateItem)!
      );
      const leaseTemplatesToAdd = leaseTemplates.filter(leaseTemplateItem => {
        const leaseTemplateIdentifier = getLeaseTemplateIdentifier(leaseTemplateItem);
        if (leaseTemplateIdentifier == null || leaseTemplateCollectionIdentifiers.includes(leaseTemplateIdentifier)) {
          return false;
        }
        leaseTemplateCollectionIdentifiers.push(leaseTemplateIdentifier);
        return true;
      });
      return [...leaseTemplatesToAdd, ...leaseTemplateCollection];
    }
    return leaseTemplateCollection;
  }
}
