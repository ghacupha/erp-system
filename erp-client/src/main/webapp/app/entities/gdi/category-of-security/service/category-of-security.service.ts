import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { ICategoryOfSecurity, getCategoryOfSecurityIdentifier } from '../category-of-security.model';

export type EntityResponseType = HttpResponse<ICategoryOfSecurity>;
export type EntityArrayResponseType = HttpResponse<ICategoryOfSecurity[]>;

@Injectable({ providedIn: 'root' })
export class CategoryOfSecurityService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/category-of-securities');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/category-of-securities');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(categoryOfSecurity: ICategoryOfSecurity): Observable<EntityResponseType> {
    return this.http.post<ICategoryOfSecurity>(this.resourceUrl, categoryOfSecurity, { observe: 'response' });
  }

  update(categoryOfSecurity: ICategoryOfSecurity): Observable<EntityResponseType> {
    return this.http.put<ICategoryOfSecurity>(
      `${this.resourceUrl}/${getCategoryOfSecurityIdentifier(categoryOfSecurity) as number}`,
      categoryOfSecurity,
      { observe: 'response' }
    );
  }

  partialUpdate(categoryOfSecurity: ICategoryOfSecurity): Observable<EntityResponseType> {
    return this.http.patch<ICategoryOfSecurity>(
      `${this.resourceUrl}/${getCategoryOfSecurityIdentifier(categoryOfSecurity) as number}`,
      categoryOfSecurity,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICategoryOfSecurity>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICategoryOfSecurity[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICategoryOfSecurity[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  addCategoryOfSecurityToCollectionIfMissing(
    categoryOfSecurityCollection: ICategoryOfSecurity[],
    ...categoryOfSecuritiesToCheck: (ICategoryOfSecurity | null | undefined)[]
  ): ICategoryOfSecurity[] {
    const categoryOfSecurities: ICategoryOfSecurity[] = categoryOfSecuritiesToCheck.filter(isPresent);
    if (categoryOfSecurities.length > 0) {
      const categoryOfSecurityCollectionIdentifiers = categoryOfSecurityCollection.map(
        categoryOfSecurityItem => getCategoryOfSecurityIdentifier(categoryOfSecurityItem)!
      );
      const categoryOfSecuritiesToAdd = categoryOfSecurities.filter(categoryOfSecurityItem => {
        const categoryOfSecurityIdentifier = getCategoryOfSecurityIdentifier(categoryOfSecurityItem);
        if (categoryOfSecurityIdentifier == null || categoryOfSecurityCollectionIdentifiers.includes(categoryOfSecurityIdentifier)) {
          return false;
        }
        categoryOfSecurityCollectionIdentifiers.push(categoryOfSecurityIdentifier);
        return true;
      });
      return [...categoryOfSecuritiesToAdd, ...categoryOfSecurityCollection];
    }
    return categoryOfSecurityCollection;
  }
}
