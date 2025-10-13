///
/// Erp System - Mark X No 10 (Jehoiada Series) Client 1.7.8
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
import { getRouAssetListReportIdentifier, IRouAssetListReport } from '../rou-asset-list-report.model';

export type EntityResponseType = HttpResponse<IRouAssetListReport>;
export type EntityArrayResponseType = HttpResponse<IRouAssetListReport[]>;

@Injectable({ providedIn: 'root' })
export class RouAssetListReportService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/leases/rou-asset-list-reports');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/leases/_search/rou-asset-list-reports');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(rouAssetListReport: IRouAssetListReport): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(rouAssetListReport);
    return this.http
      .post<IRouAssetListReport>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(rouAssetListReport: IRouAssetListReport): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(rouAssetListReport);
    return this.http
      .put<IRouAssetListReport>(`${this.resourceUrl}/${getRouAssetListReportIdentifier(rouAssetListReport) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(rouAssetListReport: IRouAssetListReport): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(rouAssetListReport);
    return this.http
      .patch<IRouAssetListReport>(`${this.resourceUrl}/${getRouAssetListReportIdentifier(rouAssetListReport) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IRouAssetListReport>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IRouAssetListReport[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IRouAssetListReport[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  addRouAssetListReportToCollectionIfMissing(
    rouAssetListReportCollection: IRouAssetListReport[],
    ...rouAssetListReportsToCheck: (IRouAssetListReport | null | undefined)[]
  ): IRouAssetListReport[] {
    const rouAssetListReports: IRouAssetListReport[] = rouAssetListReportsToCheck.filter(isPresent);
    if (rouAssetListReports.length > 0) {
      const rouAssetListReportCollectionIdentifiers = rouAssetListReportCollection.map(
        rouAssetListReportItem => getRouAssetListReportIdentifier(rouAssetListReportItem)!
      );
      const rouAssetListReportsToAdd = rouAssetListReports.filter(rouAssetListReportItem => {
        const rouAssetListReportIdentifier = getRouAssetListReportIdentifier(rouAssetListReportItem);
        if (rouAssetListReportIdentifier == null || rouAssetListReportCollectionIdentifiers.includes(rouAssetListReportIdentifier)) {
          return false;
        }
        rouAssetListReportCollectionIdentifiers.push(rouAssetListReportIdentifier);
        return true;
      });
      return [...rouAssetListReportsToAdd, ...rouAssetListReportCollection];
    }
    return rouAssetListReportCollection;
  }

  protected convertDateFromClient(rouAssetListReport: IRouAssetListReport): IRouAssetListReport {
    return Object.assign({}, rouAssetListReport, {
      timeOfRequest: rouAssetListReport.timeOfRequest?.isValid() ? rouAssetListReport.timeOfRequest.toJSON() : undefined,
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
      res.body.forEach((rouAssetListReport: IRouAssetListReport) => {
        rouAssetListReport.timeOfRequest = rouAssetListReport.timeOfRequest ? dayjs(rouAssetListReport.timeOfRequest) : undefined;
      });
    }
    return res;
  }
}
