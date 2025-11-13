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
import { IDepreciationReport, getDepreciationReportIdentifier } from '../depreciation-report.model';

export type EntityResponseType = HttpResponse<IDepreciationReport>;
export type EntityArrayResponseType = HttpResponse<IDepreciationReport[]>;

@Injectable({ providedIn: 'root' })
export class DepreciationReportService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/depreciation-reports');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/depreciation-reports');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(depreciationReport: IDepreciationReport): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(depreciationReport);
    return this.http
      .post<IDepreciationReport>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(depreciationReport: IDepreciationReport): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(depreciationReport);
    return this.http
      .put<IDepreciationReport>(`${this.resourceUrl}/${getDepreciationReportIdentifier(depreciationReport) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(depreciationReport: IDepreciationReport): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(depreciationReport);
    return this.http
      .patch<IDepreciationReport>(`${this.resourceUrl}/${getDepreciationReportIdentifier(depreciationReport) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IDepreciationReport>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IDepreciationReport[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IDepreciationReport[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  addDepreciationReportToCollectionIfMissing(
    depreciationReportCollection: IDepreciationReport[],
    ...depreciationReportsToCheck: (IDepreciationReport | null | undefined)[]
  ): IDepreciationReport[] {
    const depreciationReports: IDepreciationReport[] = depreciationReportsToCheck.filter(isPresent);
    if (depreciationReports.length > 0) {
      const depreciationReportCollectionIdentifiers = depreciationReportCollection.map(
        depreciationReportItem => getDepreciationReportIdentifier(depreciationReportItem)!
      );
      const depreciationReportsToAdd = depreciationReports.filter(depreciationReportItem => {
        const depreciationReportIdentifier = getDepreciationReportIdentifier(depreciationReportItem);
        if (depreciationReportIdentifier == null || depreciationReportCollectionIdentifiers.includes(depreciationReportIdentifier)) {
          return false;
        }
        depreciationReportCollectionIdentifiers.push(depreciationReportIdentifier);
        return true;
      });
      return [...depreciationReportsToAdd, ...depreciationReportCollection];
    }
    return depreciationReportCollection;
  }

  protected convertDateFromClient(depreciationReport: IDepreciationReport): IDepreciationReport {
    return Object.assign({}, depreciationReport, {
      timeOfReportRequest: depreciationReport.timeOfReportRequest?.isValid() ? depreciationReport.timeOfReportRequest.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.timeOfReportRequest = res.body.timeOfReportRequest ? dayjs(res.body.timeOfReportRequest) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((depreciationReport: IDepreciationReport) => {
        depreciationReport.timeOfReportRequest = depreciationReport.timeOfReportRequest
          ? dayjs(depreciationReport.timeOfReportRequest)
          : undefined;
      });
    }
    return res;
  }
}
