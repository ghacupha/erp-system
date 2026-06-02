import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IGdiMasterDataIndex, getGdiMasterDataIndexIdentifier } from '../gdi-master-data-index.model';

export type EntityResponseType = HttpResponse<IGdiMasterDataIndex>;
export type EntityArrayResponseType = HttpResponse<IGdiMasterDataIndex[]>;

@Injectable({ providedIn: 'root' })
export class GdiMasterDataIndexService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/gdi-master-data-indices');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/gdi-master-data-indices');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(gdiMasterDataIndex: IGdiMasterDataIndex): Observable<EntityResponseType> {
    return this.http.post<IGdiMasterDataIndex>(this.resourceUrl, gdiMasterDataIndex, { observe: 'response' });
  }

  update(gdiMasterDataIndex: IGdiMasterDataIndex): Observable<EntityResponseType> {
    return this.http.put<IGdiMasterDataIndex>(
      `${this.resourceUrl}/${getGdiMasterDataIndexIdentifier(gdiMasterDataIndex) as number}`,
      gdiMasterDataIndex,
      { observe: 'response' }
    );
  }

  partialUpdate(gdiMasterDataIndex: IGdiMasterDataIndex): Observable<EntityResponseType> {
    return this.http.patch<IGdiMasterDataIndex>(
      `${this.resourceUrl}/${getGdiMasterDataIndexIdentifier(gdiMasterDataIndex) as number}`,
      gdiMasterDataIndex,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IGdiMasterDataIndex>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IGdiMasterDataIndex[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IGdiMasterDataIndex[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  addGdiMasterDataIndexToCollectionIfMissing(
    gdiMasterDataIndexCollection: IGdiMasterDataIndex[],
    ...gdiMasterDataIndicesToCheck: (IGdiMasterDataIndex | null | undefined)[]
  ): IGdiMasterDataIndex[] {
    const gdiMasterDataIndices: IGdiMasterDataIndex[] = gdiMasterDataIndicesToCheck.filter(isPresent);
    if (gdiMasterDataIndices.length > 0) {
      const gdiMasterDataIndexCollectionIdentifiers = gdiMasterDataIndexCollection.map(
        gdiMasterDataIndexItem => getGdiMasterDataIndexIdentifier(gdiMasterDataIndexItem)!
      );
      const gdiMasterDataIndicesToAdd = gdiMasterDataIndices.filter(gdiMasterDataIndexItem => {
        const gdiMasterDataIndexIdentifier = getGdiMasterDataIndexIdentifier(gdiMasterDataIndexItem);
        if (gdiMasterDataIndexIdentifier == null || gdiMasterDataIndexCollectionIdentifiers.includes(gdiMasterDataIndexIdentifier)) {
          return false;
        }
        gdiMasterDataIndexCollectionIdentifiers.push(gdiMasterDataIndexIdentifier);
        return true;
      });
      return [...gdiMasterDataIndicesToAdd, ...gdiMasterDataIndexCollection];
    }
    return gdiMasterDataIndexCollection;
  }
}
