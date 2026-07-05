import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IEmploymentTerms, getEmploymentTermsIdentifier } from '../employment-terms.model';

export type EntityResponseType = HttpResponse<IEmploymentTerms>;
export type EntityArrayResponseType = HttpResponse<IEmploymentTerms[]>;

@Injectable({ providedIn: 'root' })
export class EmploymentTermsService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/employment-terms');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/employment-terms');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(employmentTerms: IEmploymentTerms): Observable<EntityResponseType> {
    return this.http.post<IEmploymentTerms>(this.resourceUrl, employmentTerms, { observe: 'response' });
  }

  update(employmentTerms: IEmploymentTerms): Observable<EntityResponseType> {
    return this.http.put<IEmploymentTerms>(
      `${this.resourceUrl}/${getEmploymentTermsIdentifier(employmentTerms) as number}`,
      employmentTerms,
      { observe: 'response' }
    );
  }

  partialUpdate(employmentTerms: IEmploymentTerms): Observable<EntityResponseType> {
    return this.http.patch<IEmploymentTerms>(
      `${this.resourceUrl}/${getEmploymentTermsIdentifier(employmentTerms) as number}`,
      employmentTerms,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IEmploymentTerms>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IEmploymentTerms[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IEmploymentTerms[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  addEmploymentTermsToCollectionIfMissing(
    employmentTermsCollection: IEmploymentTerms[],
    ...employmentTermsToCheck: (IEmploymentTerms | null | undefined)[]
  ): IEmploymentTerms[] {
    const employmentTerms: IEmploymentTerms[] = employmentTermsToCheck.filter(isPresent);
    if (employmentTerms.length > 0) {
      const employmentTermsCollectionIdentifiers = employmentTermsCollection.map(
        employmentTermsItem => getEmploymentTermsIdentifier(employmentTermsItem)!
      );
      const employmentTermsToAdd = employmentTerms.filter(employmentTermsItem => {
        const employmentTermsIdentifier = getEmploymentTermsIdentifier(employmentTermsItem);
        if (employmentTermsIdentifier == null || employmentTermsCollectionIdentifiers.includes(employmentTermsIdentifier)) {
          return false;
        }
        employmentTermsCollectionIdentifiers.push(employmentTermsIdentifier);
        return true;
      });
      return [...employmentTermsToAdd, ...employmentTermsCollection];
    }
    return employmentTermsCollection;
  }
}
