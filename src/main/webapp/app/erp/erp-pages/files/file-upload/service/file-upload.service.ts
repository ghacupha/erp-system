import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IFileUpload, getFileUploadIdentifier } from '../file-upload.model';

export type EntityResponseType = HttpResponse<IFileUpload>;
export type EntityArrayResponseType = HttpResponse<IFileUpload[]>;

@Injectable({ providedIn: 'root' })
export class FileUploadService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/app/file-uploads');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/file-uploads');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(fileUpload: IFileUpload): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(fileUpload);
    return this.http
      .post<IFileUpload>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(fileUpload: IFileUpload): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(fileUpload);
    return this.http
      .put<IFileUpload>(`${this.resourceUrl}/${getFileUploadIdentifier(fileUpload) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(fileUpload: IFileUpload): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(fileUpload);
    return this.http
      .patch<IFileUpload>(`${this.resourceUrl}/${getFileUploadIdentifier(fileUpload) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IFileUpload>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IFileUpload[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IFileUpload[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  addFileUploadToCollectionIfMissing(
    fileUploadCollection: IFileUpload[],
    ...fileUploadsToCheck: (IFileUpload | null | undefined)[]
  ): IFileUpload[] {
    const fileUploads: IFileUpload[] = fileUploadsToCheck.filter(isPresent);
    if (fileUploads.length > 0) {
      const fileUploadCollectionIdentifiers = fileUploadCollection.map(fileUploadItem => getFileUploadIdentifier(fileUploadItem)!);
      const fileUploadsToAdd = fileUploads.filter(fileUploadItem => {
        const fileUploadIdentifier = getFileUploadIdentifier(fileUploadItem);
        if (fileUploadIdentifier == null || fileUploadCollectionIdentifiers.includes(fileUploadIdentifier)) {
          return false;
        }
        fileUploadCollectionIdentifiers.push(fileUploadIdentifier);
        return true;
      });
      return [...fileUploadsToAdd, ...fileUploadCollection];
    }
    return fileUploadCollection;
  }

  protected convertDateFromClient(fileUpload: IFileUpload): IFileUpload {
    return Object.assign({}, fileUpload, {
      periodFrom: fileUpload.periodFrom?.isValid() ? fileUpload.periodFrom.format(DATE_FORMAT) : undefined,
      periodTo: fileUpload.periodTo?.isValid() ? fileUpload.periodTo.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.periodFrom = res.body.periodFrom ? dayjs(res.body.periodFrom) : undefined;
      res.body.periodTo = res.body.periodTo ? dayjs(res.body.periodTo) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((fileUpload: IFileUpload) => {
        fileUpload.periodFrom = fileUpload.periodFrom ? dayjs(fileUpload.periodFrom) : undefined;
        fileUpload.periodTo = fileUpload.periodTo ? dayjs(fileUpload.periodTo) : undefined;
      });
    }
    return res;
  }
}
