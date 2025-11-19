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

import { ApplicationConfigService } from 'app/core/config/application-config.service';
import {
  ILeaseLiabilityScheduleUploadRequest,
  ILeaseLiabilityScheduleUploadResponse,
} from '../lease-liability-schedule-upload.model';

export type EntityResponseType = HttpResponse<ILeaseLiabilityScheduleUploadResponse>;

@Injectable({ providedIn: 'root' })
export class LeaseLiabilityScheduleUploadService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/leases/lease-liability-schedule-file-uploads');

  constructor(private http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  upload(request: ILeaseLiabilityScheduleUploadRequest, file: File): Observable<EntityResponseType> {
    const formData = new FormData();
    formData.append('request', new Blob([JSON.stringify(request)], { type: 'application/json' }));
    formData.append('file', file, file.name);
    return this.http.post<ILeaseLiabilityScheduleUploadResponse>(this.resourceUrl, formData, { observe: 'response' });
  }
}
