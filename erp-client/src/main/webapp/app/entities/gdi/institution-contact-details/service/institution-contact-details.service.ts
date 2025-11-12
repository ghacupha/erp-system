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
import { IInstitutionContactDetails, getInstitutionContactDetailsIdentifier } from '../institution-contact-details.model';

export type EntityResponseType = HttpResponse<IInstitutionContactDetails>;
export type EntityArrayResponseType = HttpResponse<IInstitutionContactDetails[]>;

@Injectable({ providedIn: 'root' })
export class InstitutionContactDetailsService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/institution-contact-details');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/institution-contact-details');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(institutionContactDetails: IInstitutionContactDetails): Observable<EntityResponseType> {
    return this.http.post<IInstitutionContactDetails>(this.resourceUrl, institutionContactDetails, { observe: 'response' });
  }

  update(institutionContactDetails: IInstitutionContactDetails): Observable<EntityResponseType> {
    return this.http.put<IInstitutionContactDetails>(
      `${this.resourceUrl}/${getInstitutionContactDetailsIdentifier(institutionContactDetails) as number}`,
      institutionContactDetails,
      { observe: 'response' }
    );
  }

  partialUpdate(institutionContactDetails: IInstitutionContactDetails): Observable<EntityResponseType> {
    return this.http.patch<IInstitutionContactDetails>(
      `${this.resourceUrl}/${getInstitutionContactDetailsIdentifier(institutionContactDetails) as number}`,
      institutionContactDetails,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IInstitutionContactDetails>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IInstitutionContactDetails[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IInstitutionContactDetails[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  addInstitutionContactDetailsToCollectionIfMissing(
    institutionContactDetailsCollection: IInstitutionContactDetails[],
    ...institutionContactDetailsToCheck: (IInstitutionContactDetails | null | undefined)[]
  ): IInstitutionContactDetails[] {
    const institutionContactDetails: IInstitutionContactDetails[] = institutionContactDetailsToCheck.filter(isPresent);
    if (institutionContactDetails.length > 0) {
      const institutionContactDetailsCollectionIdentifiers = institutionContactDetailsCollection.map(
        institutionContactDetailsItem => getInstitutionContactDetailsIdentifier(institutionContactDetailsItem)!
      );
      const institutionContactDetailsToAdd = institutionContactDetails.filter(institutionContactDetailsItem => {
        const institutionContactDetailsIdentifier = getInstitutionContactDetailsIdentifier(institutionContactDetailsItem);
        if (
          institutionContactDetailsIdentifier == null ||
          institutionContactDetailsCollectionIdentifiers.includes(institutionContactDetailsIdentifier)
        ) {
          return false;
        }
        institutionContactDetailsCollectionIdentifiers.push(institutionContactDetailsIdentifier);
        return true;
      });
      return [...institutionContactDetailsToAdd, ...institutionContactDetailsCollection];
    }
    return institutionContactDetailsCollection;
  }
}
