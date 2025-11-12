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
import { IExcelReportExport, getExcelReportExportIdentifier } from '../excel-report-export.model';

export type EntityResponseType = HttpResponse<IExcelReportExport>;
export type EntityArrayResponseType = HttpResponse<IExcelReportExport[]>;

@Injectable({ providedIn: 'root' })
export class ExcelReportExportService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/app/excel-report-exports');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/app/_search/excel-report-exports');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(excelReportExport: IExcelReportExport): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(excelReportExport);
    return this.http
      .post<IExcelReportExport>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(excelReportExport: IExcelReportExport): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(excelReportExport);
    return this.http
      .put<IExcelReportExport>(`${this.resourceUrl}/${getExcelReportExportIdentifier(excelReportExport) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(excelReportExport: IExcelReportExport): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(excelReportExport);
    return this.http
      .patch<IExcelReportExport>(`${this.resourceUrl}/${getExcelReportExportIdentifier(excelReportExport) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IExcelReportExport>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IExcelReportExport[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IExcelReportExport[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  addExcelReportExportToCollectionIfMissing(
    excelReportExportCollection: IExcelReportExport[],
    ...excelReportExportsToCheck: (IExcelReportExport | null | undefined)[]
  ): IExcelReportExport[] {
    const excelReportExports: IExcelReportExport[] = excelReportExportsToCheck.filter(isPresent);
    if (excelReportExports.length > 0) {
      const excelReportExportCollectionIdentifiers = excelReportExportCollection.map(
        excelReportExportItem => getExcelReportExportIdentifier(excelReportExportItem)!
      );
      const excelReportExportsToAdd = excelReportExports.filter(excelReportExportItem => {
        const excelReportExportIdentifier = getExcelReportExportIdentifier(excelReportExportItem);
        if (excelReportExportIdentifier == null || excelReportExportCollectionIdentifiers.includes(excelReportExportIdentifier)) {
          return false;
        }
        excelReportExportCollectionIdentifiers.push(excelReportExportIdentifier);
        return true;
      });
      return [...excelReportExportsToAdd, ...excelReportExportCollection];
    }
    return excelReportExportCollection;
  }

  protected convertDateFromClient(excelReportExport: IExcelReportExport): IExcelReportExport {
    return Object.assign({}, excelReportExport, {
      reportTimeStamp: excelReportExport.reportTimeStamp?.isValid() ? excelReportExport.reportTimeStamp.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.reportTimeStamp = res.body.reportTimeStamp ? dayjs(res.body.reportTimeStamp) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((excelReportExport: IExcelReportExport) => {
        excelReportExport.reportTimeStamp = excelReportExport.reportTimeStamp ? dayjs(excelReportExport.reportTimeStamp) : undefined;
      });
    }
    return res;
  }
}
