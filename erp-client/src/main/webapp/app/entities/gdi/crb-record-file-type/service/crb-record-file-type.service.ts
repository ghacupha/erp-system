import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { ICrbRecordFileType, getCrbRecordFileTypeIdentifier } from '../crb-record-file-type.model';

export type EntityResponseType = HttpResponse<ICrbRecordFileType>;
export type EntityArrayResponseType = HttpResponse<ICrbRecordFileType[]>;

@Injectable({ providedIn: 'root' })
export class CrbRecordFileTypeService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/crb-record-file-types');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/crb-record-file-types');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(crbRecordFileType: ICrbRecordFileType): Observable<EntityResponseType> {
    return this.http.post<ICrbRecordFileType>(this.resourceUrl, crbRecordFileType, { observe: 'response' });
  }

  update(crbRecordFileType: ICrbRecordFileType): Observable<EntityResponseType> {
    return this.http.put<ICrbRecordFileType>(
      `${this.resourceUrl}/${getCrbRecordFileTypeIdentifier(crbRecordFileType) as number}`,
      crbRecordFileType,
      { observe: 'response' }
    );
  }

  partialUpdate(crbRecordFileType: ICrbRecordFileType): Observable<EntityResponseType> {
    return this.http.patch<ICrbRecordFileType>(
      `${this.resourceUrl}/${getCrbRecordFileTypeIdentifier(crbRecordFileType) as number}`,
      crbRecordFileType,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICrbRecordFileType>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICrbRecordFileType[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICrbRecordFileType[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  addCrbRecordFileTypeToCollectionIfMissing(
    crbRecordFileTypeCollection: ICrbRecordFileType[],
    ...crbRecordFileTypesToCheck: (ICrbRecordFileType | null | undefined)[]
  ): ICrbRecordFileType[] {
    const crbRecordFileTypes: ICrbRecordFileType[] = crbRecordFileTypesToCheck.filter(isPresent);
    if (crbRecordFileTypes.length > 0) {
      const crbRecordFileTypeCollectionIdentifiers = crbRecordFileTypeCollection.map(
        crbRecordFileTypeItem => getCrbRecordFileTypeIdentifier(crbRecordFileTypeItem)!
      );
      const crbRecordFileTypesToAdd = crbRecordFileTypes.filter(crbRecordFileTypeItem => {
        const crbRecordFileTypeIdentifier = getCrbRecordFileTypeIdentifier(crbRecordFileTypeItem);
        if (crbRecordFileTypeIdentifier == null || crbRecordFileTypeCollectionIdentifiers.includes(crbRecordFileTypeIdentifier)) {
          return false;
        }
        crbRecordFileTypeCollectionIdentifiers.push(crbRecordFileTypeIdentifier);
        return true;
      });
      return [...crbRecordFileTypesToAdd, ...crbRecordFileTypeCollection];
    }
    return crbRecordFileTypeCollection;
  }
}
