import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { ICrbCreditApplicationStatus, getCrbCreditApplicationStatusIdentifier } from '../crb-credit-application-status.model';

export type EntityResponseType = HttpResponse<ICrbCreditApplicationStatus>;
export type EntityArrayResponseType = HttpResponse<ICrbCreditApplicationStatus[]>;

@Injectable({ providedIn: 'root' })
export class CrbCreditApplicationStatusService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/crb-credit-application-statuses');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/crb-credit-application-statuses');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(crbCreditApplicationStatus: ICrbCreditApplicationStatus): Observable<EntityResponseType> {
    return this.http.post<ICrbCreditApplicationStatus>(this.resourceUrl, crbCreditApplicationStatus, { observe: 'response' });
  }

  update(crbCreditApplicationStatus: ICrbCreditApplicationStatus): Observable<EntityResponseType> {
    return this.http.put<ICrbCreditApplicationStatus>(
      `${this.resourceUrl}/${getCrbCreditApplicationStatusIdentifier(crbCreditApplicationStatus) as number}`,
      crbCreditApplicationStatus,
      { observe: 'response' }
    );
  }

  partialUpdate(crbCreditApplicationStatus: ICrbCreditApplicationStatus): Observable<EntityResponseType> {
    return this.http.patch<ICrbCreditApplicationStatus>(
      `${this.resourceUrl}/${getCrbCreditApplicationStatusIdentifier(crbCreditApplicationStatus) as number}`,
      crbCreditApplicationStatus,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICrbCreditApplicationStatus>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICrbCreditApplicationStatus[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICrbCreditApplicationStatus[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  addCrbCreditApplicationStatusToCollectionIfMissing(
    crbCreditApplicationStatusCollection: ICrbCreditApplicationStatus[],
    ...crbCreditApplicationStatusesToCheck: (ICrbCreditApplicationStatus | null | undefined)[]
  ): ICrbCreditApplicationStatus[] {
    const crbCreditApplicationStatuses: ICrbCreditApplicationStatus[] = crbCreditApplicationStatusesToCheck.filter(isPresent);
    if (crbCreditApplicationStatuses.length > 0) {
      const crbCreditApplicationStatusCollectionIdentifiers = crbCreditApplicationStatusCollection.map(
        crbCreditApplicationStatusItem => getCrbCreditApplicationStatusIdentifier(crbCreditApplicationStatusItem)!
      );
      const crbCreditApplicationStatusesToAdd = crbCreditApplicationStatuses.filter(crbCreditApplicationStatusItem => {
        const crbCreditApplicationStatusIdentifier = getCrbCreditApplicationStatusIdentifier(crbCreditApplicationStatusItem);
        if (
          crbCreditApplicationStatusIdentifier == null ||
          crbCreditApplicationStatusCollectionIdentifiers.includes(crbCreditApplicationStatusIdentifier)
        ) {
          return false;
        }
        crbCreditApplicationStatusCollectionIdentifiers.push(crbCreditApplicationStatusIdentifier);
        return true;
      });
      return [...crbCreditApplicationStatusesToAdd, ...crbCreditApplicationStatusCollection];
    }
    return crbCreditApplicationStatusCollection;
  }
}
