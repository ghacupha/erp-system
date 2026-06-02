import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { ICrbSubscriptionStatusTypeCode, getCrbSubscriptionStatusTypeCodeIdentifier } from '../crb-subscription-status-type-code.model';

export type EntityResponseType = HttpResponse<ICrbSubscriptionStatusTypeCode>;
export type EntityArrayResponseType = HttpResponse<ICrbSubscriptionStatusTypeCode[]>;

@Injectable({ providedIn: 'root' })
export class CrbSubscriptionStatusTypeCodeService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/crb-subscription-status-type-codes');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/crb-subscription-status-type-codes');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(crbSubscriptionStatusTypeCode: ICrbSubscriptionStatusTypeCode): Observable<EntityResponseType> {
    return this.http.post<ICrbSubscriptionStatusTypeCode>(this.resourceUrl, crbSubscriptionStatusTypeCode, { observe: 'response' });
  }

  update(crbSubscriptionStatusTypeCode: ICrbSubscriptionStatusTypeCode): Observable<EntityResponseType> {
    return this.http.put<ICrbSubscriptionStatusTypeCode>(
      `${this.resourceUrl}/${getCrbSubscriptionStatusTypeCodeIdentifier(crbSubscriptionStatusTypeCode) as number}`,
      crbSubscriptionStatusTypeCode,
      { observe: 'response' }
    );
  }

  partialUpdate(crbSubscriptionStatusTypeCode: ICrbSubscriptionStatusTypeCode): Observable<EntityResponseType> {
    return this.http.patch<ICrbSubscriptionStatusTypeCode>(
      `${this.resourceUrl}/${getCrbSubscriptionStatusTypeCodeIdentifier(crbSubscriptionStatusTypeCode) as number}`,
      crbSubscriptionStatusTypeCode,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICrbSubscriptionStatusTypeCode>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICrbSubscriptionStatusTypeCode[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICrbSubscriptionStatusTypeCode[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  addCrbSubscriptionStatusTypeCodeToCollectionIfMissing(
    crbSubscriptionStatusTypeCodeCollection: ICrbSubscriptionStatusTypeCode[],
    ...crbSubscriptionStatusTypeCodesToCheck: (ICrbSubscriptionStatusTypeCode | null | undefined)[]
  ): ICrbSubscriptionStatusTypeCode[] {
    const crbSubscriptionStatusTypeCodes: ICrbSubscriptionStatusTypeCode[] = crbSubscriptionStatusTypeCodesToCheck.filter(isPresent);
    if (crbSubscriptionStatusTypeCodes.length > 0) {
      const crbSubscriptionStatusTypeCodeCollectionIdentifiers = crbSubscriptionStatusTypeCodeCollection.map(
        crbSubscriptionStatusTypeCodeItem => getCrbSubscriptionStatusTypeCodeIdentifier(crbSubscriptionStatusTypeCodeItem)!
      );
      const crbSubscriptionStatusTypeCodesToAdd = crbSubscriptionStatusTypeCodes.filter(crbSubscriptionStatusTypeCodeItem => {
        const crbSubscriptionStatusTypeCodeIdentifier = getCrbSubscriptionStatusTypeCodeIdentifier(crbSubscriptionStatusTypeCodeItem);
        if (
          crbSubscriptionStatusTypeCodeIdentifier == null ||
          crbSubscriptionStatusTypeCodeCollectionIdentifiers.includes(crbSubscriptionStatusTypeCodeIdentifier)
        ) {
          return false;
        }
        crbSubscriptionStatusTypeCodeCollectionIdentifiers.push(crbSubscriptionStatusTypeCodeIdentifier);
        return true;
      });
      return [...crbSubscriptionStatusTypeCodesToAdd, ...crbSubscriptionStatusTypeCodeCollection];
    }
    return crbSubscriptionStatusTypeCodeCollection;
  }
}
