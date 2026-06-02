import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IRouDepreciationEntry, getRouDepreciationEntryIdentifier } from '../rou-depreciation-entry.model';

export type EntityResponseType = HttpResponse<IRouDepreciationEntry>;
export type EntityArrayResponseType = HttpResponse<IRouDepreciationEntry[]>;

@Injectable({ providedIn: 'root' })
export class RouDepreciationEntryService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/rou-depreciation-entries');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/rou-depreciation-entries');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(rouDepreciationEntry: IRouDepreciationEntry): Observable<EntityResponseType> {
    return this.http.post<IRouDepreciationEntry>(this.resourceUrl, rouDepreciationEntry, { observe: 'response' });
  }

  update(rouDepreciationEntry: IRouDepreciationEntry): Observable<EntityResponseType> {
    return this.http.put<IRouDepreciationEntry>(
      `${this.resourceUrl}/${getRouDepreciationEntryIdentifier(rouDepreciationEntry) as number}`,
      rouDepreciationEntry,
      { observe: 'response' }
    );
  }

  partialUpdate(rouDepreciationEntry: IRouDepreciationEntry): Observable<EntityResponseType> {
    return this.http.patch<IRouDepreciationEntry>(
      `${this.resourceUrl}/${getRouDepreciationEntryIdentifier(rouDepreciationEntry) as number}`,
      rouDepreciationEntry,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IRouDepreciationEntry>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IRouDepreciationEntry[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IRouDepreciationEntry[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  addRouDepreciationEntryToCollectionIfMissing(
    rouDepreciationEntryCollection: IRouDepreciationEntry[],
    ...rouDepreciationEntriesToCheck: (IRouDepreciationEntry | null | undefined)[]
  ): IRouDepreciationEntry[] {
    const rouDepreciationEntries: IRouDepreciationEntry[] = rouDepreciationEntriesToCheck.filter(isPresent);
    if (rouDepreciationEntries.length > 0) {
      const rouDepreciationEntryCollectionIdentifiers = rouDepreciationEntryCollection.map(
        rouDepreciationEntryItem => getRouDepreciationEntryIdentifier(rouDepreciationEntryItem)!
      );
      const rouDepreciationEntriesToAdd = rouDepreciationEntries.filter(rouDepreciationEntryItem => {
        const rouDepreciationEntryIdentifier = getRouDepreciationEntryIdentifier(rouDepreciationEntryItem);
        if (rouDepreciationEntryIdentifier == null || rouDepreciationEntryCollectionIdentifiers.includes(rouDepreciationEntryIdentifier)) {
          return false;
        }
        rouDepreciationEntryCollectionIdentifiers.push(rouDepreciationEntryIdentifier);
        return true;
      });
      return [...rouDepreciationEntriesToAdd, ...rouDepreciationEntryCollection];
    }
    return rouDepreciationEntryCollection;
  }
}
