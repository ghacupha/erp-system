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
import { IProfessionalQualification, getProfessionalQualificationIdentifier } from '../professional-qualification.model';

export type EntityResponseType = HttpResponse<IProfessionalQualification>;
export type EntityArrayResponseType = HttpResponse<IProfessionalQualification[]>;

@Injectable({ providedIn: 'root' })
export class ProfessionalQualificationService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/professional-qualifications');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/professional-qualifications');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(professionalQualification: IProfessionalQualification): Observable<EntityResponseType> {
    return this.http.post<IProfessionalQualification>(this.resourceUrl, professionalQualification, { observe: 'response' });
  }

  update(professionalQualification: IProfessionalQualification): Observable<EntityResponseType> {
    return this.http.put<IProfessionalQualification>(
      `${this.resourceUrl}/${getProfessionalQualificationIdentifier(professionalQualification) as number}`,
      professionalQualification,
      { observe: 'response' }
    );
  }

  partialUpdate(professionalQualification: IProfessionalQualification): Observable<EntityResponseType> {
    return this.http.patch<IProfessionalQualification>(
      `${this.resourceUrl}/${getProfessionalQualificationIdentifier(professionalQualification) as number}`,
      professionalQualification,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IProfessionalQualification>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IProfessionalQualification[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IProfessionalQualification[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  addProfessionalQualificationToCollectionIfMissing(
    professionalQualificationCollection: IProfessionalQualification[],
    ...professionalQualificationsToCheck: (IProfessionalQualification | null | undefined)[]
  ): IProfessionalQualification[] {
    const professionalQualifications: IProfessionalQualification[] = professionalQualificationsToCheck.filter(isPresent);
    if (professionalQualifications.length > 0) {
      const professionalQualificationCollectionIdentifiers = professionalQualificationCollection.map(
        professionalQualificationItem => getProfessionalQualificationIdentifier(professionalQualificationItem)!
      );
      const professionalQualificationsToAdd = professionalQualifications.filter(professionalQualificationItem => {
        const professionalQualificationIdentifier = getProfessionalQualificationIdentifier(professionalQualificationItem);
        if (
          professionalQualificationIdentifier == null ||
          professionalQualificationCollectionIdentifiers.includes(professionalQualificationIdentifier)
        ) {
          return false;
        }
        professionalQualificationCollectionIdentifiers.push(professionalQualificationIdentifier);
        return true;
      });
      return [...professionalQualificationsToAdd, ...professionalQualificationCollection];
    }
    return professionalQualificationCollection;
  }
}
