///
/// Erp System - Mark X No 11 (Jehoiada Series) Client 1.7.8
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

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IJobSheet, getJobSheetIdentifier } from '../job-sheet.model';

export type EntityResponseType = HttpResponse<IJobSheet>;
export type EntityArrayResponseType = HttpResponse<IJobSheet[]>;

@Injectable({ providedIn: 'root' })
export class JobSheetService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/payments/job-sheets');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/payments/_search/job-sheets');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(jobSheet: IJobSheet): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(jobSheet);
    return this.http
      .post<IJobSheet>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(jobSheet: IJobSheet): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(jobSheet);
    return this.http
      .put<IJobSheet>(`${this.resourceUrl}/${getJobSheetIdentifier(jobSheet) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(jobSheet: IJobSheet): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(jobSheet);
    return this.http
      .patch<IJobSheet>(`${this.resourceUrl}/${getJobSheetIdentifier(jobSheet) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IJobSheet>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IJobSheet[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IJobSheet[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  addJobSheetToCollectionIfMissing(jobSheetCollection: IJobSheet[], ...jobSheetsToCheck: (IJobSheet | null | undefined)[]): IJobSheet[] {
    const jobSheets: IJobSheet[] = jobSheetsToCheck.filter(isPresent);
    if (jobSheets.length > 0) {
      const jobSheetCollectionIdentifiers = jobSheetCollection.map(jobSheetItem => getJobSheetIdentifier(jobSheetItem)!);
      const jobSheetsToAdd = jobSheets.filter(jobSheetItem => {
        const jobSheetIdentifier = getJobSheetIdentifier(jobSheetItem);
        if (jobSheetIdentifier == null || jobSheetCollectionIdentifiers.includes(jobSheetIdentifier)) {
          return false;
        }
        jobSheetCollectionIdentifiers.push(jobSheetIdentifier);
        return true;
      });
      return [...jobSheetsToAdd, ...jobSheetCollection];
    }
    return jobSheetCollection;
  }

  protected convertDateFromClient(jobSheet: IJobSheet): IJobSheet {
    return Object.assign({}, jobSheet, {
      jobSheetDate: jobSheet.jobSheetDate?.isValid() ? jobSheet.jobSheetDate.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.jobSheetDate = res.body.jobSheetDate ? dayjs(res.body.jobSheetDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((jobSheet: IJobSheet) => {
        jobSheet.jobSheetDate = jobSheet.jobSheetDate ? dayjs(jobSheet.jobSheetDate) : undefined;
      });
    }
    return res;
  }
}
