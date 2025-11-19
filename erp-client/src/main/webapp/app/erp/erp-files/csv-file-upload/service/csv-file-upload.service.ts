///
/// Erp System - Mark X No 11 (Jehoiada Series) Client 1.7.9
/// Copyright © 2021 - 2024 Edwin Njeru (mailnjeru@gmail.com)
///
/// This program is free software: you can redistribute it and/or modify
/// it under the terms of the GNU General Public License as published by
/// the Free Software Foundation, either version 3 of the License, or
/// (at your option) any later version.
///
/// This program is distributed in the hope that it will be useful,
/// but WITHOUT ANY WARRANTY; without even the implied warranty of
/// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
/// GNU General Public License for more details.
///
/// You should have received a copy of the GNU General Public License
/// along with this program. If not, see <http://www.gnu.org/licenses/>.
///

import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { ICsvFileUpload, getCsvFileUploadIdentifier } from '../csv-file-upload.model';

export type EntityResponseType = HttpResponse<ICsvFileUpload>;
export type EntityArrayResponseType = HttpResponse<ICsvFileUpload[]>;

@Injectable({ providedIn: 'root' })
export class CsvFileUploadService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/csv-file-uploads');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/csv-file-uploads');

  constructor(private http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ICsvFileUpload>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ICsvFileUpload[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ICsvFileUpload[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  addCsvFileUploadToCollectionIfMissing(
    csvFileUploadCollection: ICsvFileUpload[],
    ...csvFileUploadsToCheck: (ICsvFileUpload | null | undefined)[]
  ): ICsvFileUpload[] {
    const csvFileUploads: ICsvFileUpload[] = csvFileUploadsToCheck.filter((csvFileUpload): csvFileUpload is ICsvFileUpload => !!csvFileUpload);
    if (csvFileUploads.length > 0) {
      const csvFileUploadCollectionIdentifiers = csvFileUploadCollection.map(csvFileUploadItem => getCsvFileUploadIdentifier(csvFileUploadItem)!);
      const csvFileUploadsToAdd = csvFileUploads.filter(csvFileUploadItem => {
        const csvFileUploadIdentifier = getCsvFileUploadIdentifier(csvFileUploadItem);
        if (csvFileUploadIdentifier == null || csvFileUploadCollectionIdentifiers.includes(csvFileUploadIdentifier)) {
          return false;
        }
        csvFileUploadCollectionIdentifiers.push(csvFileUploadIdentifier);
        return true;
      });
      return [...csvFileUploadsToAdd, ...csvFileUploadCollection];
    }
    return csvFileUploadCollection;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.uploadedAt = res.body.uploadedAt ? dayjs(res.body.uploadedAt) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((csvFileUpload: ICsvFileUpload) => {
        csvFileUpload.uploadedAt = csvFileUpload.uploadedAt ? dayjs(csvFileUpload.uploadedAt) : undefined;
      });
    }
    return res;
  }
}
