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
import dayjs from 'dayjs';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import {
  ILeasePaymentUploadRecord,
  ILeasePaymentUploadRequest,
  ILeasePaymentUploadResponse,
} from '../lease-payment-upload.model';

export type EntityResponseType = HttpResponse<ILeasePaymentUploadResponse>;
export type EntityArrayResponseType = HttpResponse<ILeasePaymentUploadRecord[]>;

@Injectable({ providedIn: 'root' })
export class LeasePaymentUploadService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/leases/lease-payment-uploads');

  constructor(private http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  upload(request: ILeasePaymentUploadRequest, file: File): Observable<EntityResponseType> {
    const formData = new FormData();
    formData.append('request', new Blob([JSON.stringify(request)], { type: 'application/json' }));
    formData.append('file', file, file.name);
    return this.http.post<ILeasePaymentUploadResponse>(this.resourceUrl, formData, { observe: 'response' });
  }

  deactivate(id: number): Observable<HttpResponse<ILeasePaymentUploadRecord>> {
    return this.http
      .post<ILeasePaymentUploadRecord>(`${this.resourceUrl}/${id}/deactivate`, null, { observe: 'response' })
      .pipe(map(res => this.convertDateFromServer(res)));
  }

  activate(id: number): Observable<HttpResponse<ILeasePaymentUploadRecord>> {
    return this.http
      .post<ILeasePaymentUploadRecord>(`${this.resourceUrl}/${id}/activate`, null, { observe: 'response' })
      .pipe(map(res => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ILeasePaymentUploadRecord[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertDateArrayFromServer(res)));
  }

  protected convertDateFromServer(res: HttpResponse<ILeasePaymentUploadRecord>): HttpResponse<ILeasePaymentUploadRecord> {
    if (res.body) {
      res.body.createdAt = res.body.createdAt ? dayjs(res.body.createdAt) : null;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((record: ILeasePaymentUploadRecord) => {
        record.createdAt = record.createdAt ? dayjs(record.createdAt) : null;
      });
    }
    return res;
  }
}
