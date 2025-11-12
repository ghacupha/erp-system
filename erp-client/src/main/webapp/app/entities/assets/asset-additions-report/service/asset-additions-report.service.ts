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
import { IAssetAdditionsReport, getAssetAdditionsReportIdentifier } from '../asset-additions-report.model';

export type EntityResponseType = HttpResponse<IAssetAdditionsReport>;
export type EntityArrayResponseType = HttpResponse<IAssetAdditionsReport[]>;

@Injectable({ providedIn: 'root' })
export class AssetAdditionsReportService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/asset-additions-reports');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/asset-additions-reports');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(assetAdditionsReport: IAssetAdditionsReport): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(assetAdditionsReport);
    return this.http
      .post<IAssetAdditionsReport>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(assetAdditionsReport: IAssetAdditionsReport): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(assetAdditionsReport);
    return this.http
      .put<IAssetAdditionsReport>(`${this.resourceUrl}/${getAssetAdditionsReportIdentifier(assetAdditionsReport) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(assetAdditionsReport: IAssetAdditionsReport): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(assetAdditionsReport);
    return this.http
      .patch<IAssetAdditionsReport>(`${this.resourceUrl}/${getAssetAdditionsReportIdentifier(assetAdditionsReport) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IAssetAdditionsReport>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IAssetAdditionsReport[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IAssetAdditionsReport[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  addAssetAdditionsReportToCollectionIfMissing(
    assetAdditionsReportCollection: IAssetAdditionsReport[],
    ...assetAdditionsReportsToCheck: (IAssetAdditionsReport | null | undefined)[]
  ): IAssetAdditionsReport[] {
    const assetAdditionsReports: IAssetAdditionsReport[] = assetAdditionsReportsToCheck.filter(isPresent);
    if (assetAdditionsReports.length > 0) {
      const assetAdditionsReportCollectionIdentifiers = assetAdditionsReportCollection.map(
        assetAdditionsReportItem => getAssetAdditionsReportIdentifier(assetAdditionsReportItem)!
      );
      const assetAdditionsReportsToAdd = assetAdditionsReports.filter(assetAdditionsReportItem => {
        const assetAdditionsReportIdentifier = getAssetAdditionsReportIdentifier(assetAdditionsReportItem);
        if (assetAdditionsReportIdentifier == null || assetAdditionsReportCollectionIdentifiers.includes(assetAdditionsReportIdentifier)) {
          return false;
        }
        assetAdditionsReportCollectionIdentifiers.push(assetAdditionsReportIdentifier);
        return true;
      });
      return [...assetAdditionsReportsToAdd, ...assetAdditionsReportCollection];
    }
    return assetAdditionsReportCollection;
  }

  protected convertDateFromClient(assetAdditionsReport: IAssetAdditionsReport): IAssetAdditionsReport {
    return Object.assign({}, assetAdditionsReport, {
      timeOfRequest: assetAdditionsReport.timeOfRequest?.isValid() ? assetAdditionsReport.timeOfRequest.format(DATE_FORMAT) : undefined,
      reportStartDate: assetAdditionsReport.reportStartDate?.isValid()
        ? assetAdditionsReport.reportStartDate.format(DATE_FORMAT)
        : undefined,
      reportEndDate: assetAdditionsReport.reportEndDate?.isValid() ? assetAdditionsReport.reportEndDate.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.timeOfRequest = res.body.timeOfRequest ? dayjs(res.body.timeOfRequest) : undefined;
      res.body.reportStartDate = res.body.reportStartDate ? dayjs(res.body.reportStartDate) : undefined;
      res.body.reportEndDate = res.body.reportEndDate ? dayjs(res.body.reportEndDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((assetAdditionsReport: IAssetAdditionsReport) => {
        assetAdditionsReport.timeOfRequest = assetAdditionsReport.timeOfRequest ? dayjs(assetAdditionsReport.timeOfRequest) : undefined;
        assetAdditionsReport.reportStartDate = assetAdditionsReport.reportStartDate
          ? dayjs(assetAdditionsReport.reportStartDate)
          : undefined;
        assetAdditionsReport.reportEndDate = assetAdditionsReport.reportEndDate ? dayjs(assetAdditionsReport.reportEndDate) : undefined;
      });
    }
    return res;
  }
}
