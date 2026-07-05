import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IAcademicQualification, getAcademicQualificationIdentifier } from '../academic-qualification.model';

export type EntityResponseType = HttpResponse<IAcademicQualification>;
export type EntityArrayResponseType = HttpResponse<IAcademicQualification[]>;

@Injectable({ providedIn: 'root' })
export class AcademicQualificationService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/academic-qualifications');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/academic-qualifications');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(academicQualification: IAcademicQualification): Observable<EntityResponseType> {
    return this.http.post<IAcademicQualification>(this.resourceUrl, academicQualification, { observe: 'response' });
  }

  update(academicQualification: IAcademicQualification): Observable<EntityResponseType> {
    return this.http.put<IAcademicQualification>(
      `${this.resourceUrl}/${getAcademicQualificationIdentifier(academicQualification) as number}`,
      academicQualification,
      { observe: 'response' }
    );
  }

  partialUpdate(academicQualification: IAcademicQualification): Observable<EntityResponseType> {
    return this.http.patch<IAcademicQualification>(
      `${this.resourceUrl}/${getAcademicQualificationIdentifier(academicQualification) as number}`,
      academicQualification,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IAcademicQualification>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAcademicQualification[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAcademicQualification[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  addAcademicQualificationToCollectionIfMissing(
    academicQualificationCollection: IAcademicQualification[],
    ...academicQualificationsToCheck: (IAcademicQualification | null | undefined)[]
  ): IAcademicQualification[] {
    const academicQualifications: IAcademicQualification[] = academicQualificationsToCheck.filter(isPresent);
    if (academicQualifications.length > 0) {
      const academicQualificationCollectionIdentifiers = academicQualificationCollection.map(
        academicQualificationItem => getAcademicQualificationIdentifier(academicQualificationItem)!
      );
      const academicQualificationsToAdd = academicQualifications.filter(academicQualificationItem => {
        const academicQualificationIdentifier = getAcademicQualificationIdentifier(academicQualificationItem);
        if (
          academicQualificationIdentifier == null ||
          academicQualificationCollectionIdentifiers.includes(academicQualificationIdentifier)
        ) {
          return false;
        }
        academicQualificationCollectionIdentifiers.push(academicQualificationIdentifier);
        return true;
      });
      return [...academicQualificationsToAdd, ...academicQualificationCollection];
    }
    return academicQualificationCollection;
  }
}
