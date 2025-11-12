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
import { INatureOfCustomerComplaints, getNatureOfCustomerComplaintsIdentifier } from '../nature-of-customer-complaints.model';

export type EntityResponseType = HttpResponse<INatureOfCustomerComplaints>;
export type EntityArrayResponseType = HttpResponse<INatureOfCustomerComplaints[]>;

@Injectable({ providedIn: 'root' })
export class NatureOfCustomerComplaintsService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/nature-of-customer-complaints');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/nature-of-customer-complaints');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(natureOfCustomerComplaints: INatureOfCustomerComplaints): Observable<EntityResponseType> {
    return this.http.post<INatureOfCustomerComplaints>(this.resourceUrl, natureOfCustomerComplaints, { observe: 'response' });
  }

  update(natureOfCustomerComplaints: INatureOfCustomerComplaints): Observable<EntityResponseType> {
    return this.http.put<INatureOfCustomerComplaints>(
      `${this.resourceUrl}/${getNatureOfCustomerComplaintsIdentifier(natureOfCustomerComplaints) as number}`,
      natureOfCustomerComplaints,
      { observe: 'response' }
    );
  }

  partialUpdate(natureOfCustomerComplaints: INatureOfCustomerComplaints): Observable<EntityResponseType> {
    return this.http.patch<INatureOfCustomerComplaints>(
      `${this.resourceUrl}/${getNatureOfCustomerComplaintsIdentifier(natureOfCustomerComplaints) as number}`,
      natureOfCustomerComplaints,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<INatureOfCustomerComplaints>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<INatureOfCustomerComplaints[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<INatureOfCustomerComplaints[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  addNatureOfCustomerComplaintsToCollectionIfMissing(
    natureOfCustomerComplaintsCollection: INatureOfCustomerComplaints[],
    ...natureOfCustomerComplaintsToCheck: (INatureOfCustomerComplaints | null | undefined)[]
  ): INatureOfCustomerComplaints[] {
    const natureOfCustomerComplaints: INatureOfCustomerComplaints[] = natureOfCustomerComplaintsToCheck.filter(isPresent);
    if (natureOfCustomerComplaints.length > 0) {
      const natureOfCustomerComplaintsCollectionIdentifiers = natureOfCustomerComplaintsCollection.map(
        natureOfCustomerComplaintsItem => getNatureOfCustomerComplaintsIdentifier(natureOfCustomerComplaintsItem)!
      );
      const natureOfCustomerComplaintsToAdd = natureOfCustomerComplaints.filter(natureOfCustomerComplaintsItem => {
        const natureOfCustomerComplaintsIdentifier = getNatureOfCustomerComplaintsIdentifier(natureOfCustomerComplaintsItem);
        if (
          natureOfCustomerComplaintsIdentifier == null ||
          natureOfCustomerComplaintsCollectionIdentifiers.includes(natureOfCustomerComplaintsIdentifier)
        ) {
          return false;
        }
        natureOfCustomerComplaintsCollectionIdentifiers.push(natureOfCustomerComplaintsIdentifier);
        return true;
      });
      return [...natureOfCustomerComplaintsToAdd, ...natureOfCustomerComplaintsCollection];
    }
    return natureOfCustomerComplaintsCollection;
  }
}
