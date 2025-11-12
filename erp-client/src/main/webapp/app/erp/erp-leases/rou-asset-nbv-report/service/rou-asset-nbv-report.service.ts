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
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IRouAssetNBVReport, getRouAssetNBVReportIdentifier } from '../rou-asset-nbv-report.model';

export type EntityResponseType = HttpResponse<IRouAssetNBVReport>;
export type EntityArrayResponseType = HttpResponse<IRouAssetNBVReport[]>;

@Injectable({ providedIn: 'root' })
export class RouAssetNBVReportService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/leases/rou-asset-nbv-reports');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/leases/_search/rou-asset-nbv-reports');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(rouAssetNBVReport: IRouAssetNBVReport): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(rouAssetNBVReport);
    return this.http
      .post<IRouAssetNBVReport>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(rouAssetNBVReport: IRouAssetNBVReport): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(rouAssetNBVReport);
    return this.http
      .put<IRouAssetNBVReport>(`${this.resourceUrl}/${getRouAssetNBVReportIdentifier(rouAssetNBVReport) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(rouAssetNBVReport: IRouAssetNBVReport): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(rouAssetNBVReport);
    return this.http
      .patch<IRouAssetNBVReport>(`${this.resourceUrl}/${getRouAssetNBVReportIdentifier(rouAssetNBVReport) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IRouAssetNBVReport>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IRouAssetNBVReport[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IRouAssetNBVReport[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  addRouAssetNBVReportToCollectionIfMissing(
    rouAssetNBVReportCollection: IRouAssetNBVReport[],
    ...rouAssetNBVReportsToCheck: (IRouAssetNBVReport | null | undefined)[]
  ): IRouAssetNBVReport[] {
    const rouAssetNBVReports: IRouAssetNBVReport[] = rouAssetNBVReportsToCheck.filter(isPresent);
    if (rouAssetNBVReports.length > 0) {
      const rouAssetNBVReportCollectionIdentifiers = rouAssetNBVReportCollection.map(
        rouAssetNBVReportItem => getRouAssetNBVReportIdentifier(rouAssetNBVReportItem)!
      );
      const rouAssetNBVReportsToAdd = rouAssetNBVReports.filter(rouAssetNBVReportItem => {
        const rouAssetNBVReportIdentifier = getRouAssetNBVReportIdentifier(rouAssetNBVReportItem);
        if (rouAssetNBVReportIdentifier == null || rouAssetNBVReportCollectionIdentifiers.includes(rouAssetNBVReportIdentifier)) {
          return false;
        }
        rouAssetNBVReportCollectionIdentifiers.push(rouAssetNBVReportIdentifier);
        return true;
      });
      return [...rouAssetNBVReportsToAdd, ...rouAssetNBVReportCollection];
    }
    return rouAssetNBVReportCollection;
  }

  protected convertDateFromClient(rouAssetNBVReport: IRouAssetNBVReport): IRouAssetNBVReport {
    return Object.assign({}, rouAssetNBVReport, {
      timeOfRequest: rouAssetNBVReport.timeOfRequest?.isValid() ? rouAssetNBVReport.timeOfRequest.toJSON() : undefined,
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
      res.body.forEach((rouAssetNBVReport: IRouAssetNBVReport) => {
        rouAssetNBVReport.timeOfRequest = rouAssetNBVReport.timeOfRequest ? dayjs(rouAssetNBVReport.timeOfRequest) : undefined;
      });
    }
    return res;
  }
}
