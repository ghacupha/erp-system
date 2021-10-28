import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IFileType, getFileTypeIdentifier } from '../file-type.model';

export type EntityResponseType = HttpResponse<IFileType>;
export type EntityArrayResponseType = HttpResponse<IFileType[]>;

@Injectable({ providedIn: 'root' })
export class FileTypeService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/file-types');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/file-types');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(fileType: IFileType): Observable<EntityResponseType> {
    return this.http.post<IFileType>(this.resourceUrl, fileType, { observe: 'response' });
  }

  update(fileType: IFileType): Observable<EntityResponseType> {
    return this.http.put<IFileType>(`${this.resourceUrl}/${getFileTypeIdentifier(fileType) as number}`, fileType, { observe: 'response' });
  }

  partialUpdate(fileType: IFileType): Observable<EntityResponseType> {
    return this.http.patch<IFileType>(`${this.resourceUrl}/${getFileTypeIdentifier(fileType) as number}`, fileType, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IFileType>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IFileType[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IFileType[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  addFileTypeToCollectionIfMissing(fileTypeCollection: IFileType[], ...fileTypesToCheck: (IFileType | null | undefined)[]): IFileType[] {
    const fileTypes: IFileType[] = fileTypesToCheck.filter(isPresent);
    if (fileTypes.length > 0) {
      const fileTypeCollectionIdentifiers = fileTypeCollection.map(fileTypeItem => getFileTypeIdentifier(fileTypeItem)!);
      const fileTypesToAdd = fileTypes.filter(fileTypeItem => {
        const fileTypeIdentifier = getFileTypeIdentifier(fileTypeItem);
        if (fileTypeIdentifier == null || fileTypeCollectionIdentifiers.includes(fileTypeIdentifier)) {
          return false;
        }
        fileTypeCollectionIdentifiers.push(fileTypeIdentifier);
        return true;
      });
      return [...fileTypesToAdd, ...fileTypeCollection];
    }
    return fileTypeCollection;
  }
}
