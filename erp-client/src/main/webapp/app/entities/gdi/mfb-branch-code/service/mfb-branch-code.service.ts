import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IMfbBranchCode, getMfbBranchCodeIdentifier } from '../mfb-branch-code.model';

export type EntityResponseType = HttpResponse<IMfbBranchCode>;
export type EntityArrayResponseType = HttpResponse<IMfbBranchCode[]>;

@Injectable({ providedIn: 'root' })
export class MfbBranchCodeService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/mfb-branch-codes');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/mfb-branch-codes');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(mfbBranchCode: IMfbBranchCode): Observable<EntityResponseType> {
    return this.http.post<IMfbBranchCode>(this.resourceUrl, mfbBranchCode, { observe: 'response' });
  }

  update(mfbBranchCode: IMfbBranchCode): Observable<EntityResponseType> {
    return this.http.put<IMfbBranchCode>(`${this.resourceUrl}/${getMfbBranchCodeIdentifier(mfbBranchCode) as number}`, mfbBranchCode, {
      observe: 'response',
    });
  }

  partialUpdate(mfbBranchCode: IMfbBranchCode): Observable<EntityResponseType> {
    return this.http.patch<IMfbBranchCode>(`${this.resourceUrl}/${getMfbBranchCodeIdentifier(mfbBranchCode) as number}`, mfbBranchCode, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IMfbBranchCode>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IMfbBranchCode[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IMfbBranchCode[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  addMfbBranchCodeToCollectionIfMissing(
    mfbBranchCodeCollection: IMfbBranchCode[],
    ...mfbBranchCodesToCheck: (IMfbBranchCode | null | undefined)[]
  ): IMfbBranchCode[] {
    const mfbBranchCodes: IMfbBranchCode[] = mfbBranchCodesToCheck.filter(isPresent);
    if (mfbBranchCodes.length > 0) {
      const mfbBranchCodeCollectionIdentifiers = mfbBranchCodeCollection.map(
        mfbBranchCodeItem => getMfbBranchCodeIdentifier(mfbBranchCodeItem)!
      );
      const mfbBranchCodesToAdd = mfbBranchCodes.filter(mfbBranchCodeItem => {
        const mfbBranchCodeIdentifier = getMfbBranchCodeIdentifier(mfbBranchCodeItem);
        if (mfbBranchCodeIdentifier == null || mfbBranchCodeCollectionIdentifiers.includes(mfbBranchCodeIdentifier)) {
          return false;
        }
        mfbBranchCodeCollectionIdentifiers.push(mfbBranchCodeIdentifier);
        return true;
      });
      return [...mfbBranchCodesToAdd, ...mfbBranchCodeCollection];
    }
    return mfbBranchCodeCollection;
  }
}
