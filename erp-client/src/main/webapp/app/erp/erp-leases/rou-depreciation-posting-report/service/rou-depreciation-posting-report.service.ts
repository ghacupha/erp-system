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

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IRouDepreciationPostingReport, getRouDepreciationPostingReportIdentifier } from '../rou-depreciation-posting-report.model';

export type EntityResponseType = HttpResponse<IRouDepreciationPostingReport>;
export type EntityArrayResponseType = HttpResponse<IRouDepreciationPostingReport[]>;

@Injectable({ providedIn: 'root' })
export class RouDepreciationPostingReportService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/leases/rou-depreciation-posting-reports');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/leases/_search/rou-depreciation-posting-reports');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(rouDepreciationPostingReport: IRouDepreciationPostingReport): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(rouDepreciationPostingReport);
    return this.http
      .post<IRouDepreciationPostingReport>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(rouDepreciationPostingReport: IRouDepreciationPostingReport): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(rouDepreciationPostingReport);
    return this.http
      .put<IRouDepreciationPostingReport>(
        `${this.resourceUrl}/${getRouDepreciationPostingReportIdentifier(rouDepreciationPostingReport) as number}`,
        copy,
        { observe: 'response' }
      )
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(rouDepreciationPostingReport: IRouDepreciationPostingReport): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(rouDepreciationPostingReport);
    return this.http
      .patch<IRouDepreciationPostingReport>(
        `${this.resourceUrl}/${getRouDepreciationPostingReportIdentifier(rouDepreciationPostingReport) as number}`,
        copy,
        { observe: 'response' }
      )
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IRouDepreciationPostingReport>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IRouDepreciationPostingReport[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IRouDepreciationPostingReport[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  addRouDepreciationPostingReportToCollectionIfMissing(
    rouDepreciationPostingReportCollection: IRouDepreciationPostingReport[],
    ...rouDepreciationPostingReportsToCheck: (IRouDepreciationPostingReport | null | undefined)[]
  ): IRouDepreciationPostingReport[] {
    const rouDepreciationPostingReports: IRouDepreciationPostingReport[] = rouDepreciationPostingReportsToCheck.filter(isPresent);
    if (rouDepreciationPostingReports.length > 0) {
      const rouDepreciationPostingReportCollectionIdentifiers = rouDepreciationPostingReportCollection.map(
        rouDepreciationPostingReportItem => getRouDepreciationPostingReportIdentifier(rouDepreciationPostingReportItem)!
      );
      const rouDepreciationPostingReportsToAdd = rouDepreciationPostingReports.filter(rouDepreciationPostingReportItem => {
        const rouDepreciationPostingReportIdentifier = getRouDepreciationPostingReportIdentifier(rouDepreciationPostingReportItem);
        if (
          rouDepreciationPostingReportIdentifier == null ||
          rouDepreciationPostingReportCollectionIdentifiers.includes(rouDepreciationPostingReportIdentifier)
        ) {
          return false;
        }
        rouDepreciationPostingReportCollectionIdentifiers.push(rouDepreciationPostingReportIdentifier);
        return true;
      });
      return [...rouDepreciationPostingReportsToAdd, ...rouDepreciationPostingReportCollection];
    }
    return rouDepreciationPostingReportCollection;
  }

  protected convertDateFromClient(rouDepreciationPostingReport: IRouDepreciationPostingReport): IRouDepreciationPostingReport {
    return Object.assign({}, rouDepreciationPostingReport, {
      timeOfRequest: rouDepreciationPostingReport.timeOfRequest?.isValid()
        ? rouDepreciationPostingReport.timeOfRequest.toJSON()
        : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.timeOfRequest = res.body.timeOfRequest ? dayjs(res.body.timeOfRequest) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((rouDepreciationPostingReport: IRouDepreciationPostingReport) => {
        rouDepreciationPostingReport.timeOfRequest = rouDepreciationPostingReport.timeOfRequest
          ? dayjs(rouDepreciationPostingReport.timeOfRequest)
          : undefined;
      });
    }
    return res;
  }
}
