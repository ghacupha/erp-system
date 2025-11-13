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
import { INbvReport, getNbvReportIdentifier } from '../nbv-report.model';

export type EntityResponseType = HttpResponse<INbvReport>;
export type EntityArrayResponseType = HttpResponse<INbvReport[]>;

@Injectable({ providedIn: 'root' })
export class NbvReportService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/nbv-reports');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/nbv-reports');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(nbvReport: INbvReport): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(nbvReport);
    return this.http
      .post<INbvReport>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(nbvReport: INbvReport): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(nbvReport);
    return this.http
      .put<INbvReport>(`${this.resourceUrl}/${getNbvReportIdentifier(nbvReport) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(nbvReport: INbvReport): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(nbvReport);
    return this.http
      .patch<INbvReport>(`${this.resourceUrl}/${getNbvReportIdentifier(nbvReport) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<INbvReport>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<INbvReport[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<INbvReport[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  addNbvReportToCollectionIfMissing(
    nbvReportCollection: INbvReport[],
    ...nbvReportsToCheck: (INbvReport | null | undefined)[]
  ): INbvReport[] {
    const nbvReports: INbvReport[] = nbvReportsToCheck.filter(isPresent);
    if (nbvReports.length > 0) {
      const nbvReportCollectionIdentifiers = nbvReportCollection.map(nbvReportItem => getNbvReportIdentifier(nbvReportItem)!);
      const nbvReportsToAdd = nbvReports.filter(nbvReportItem => {
        const nbvReportIdentifier = getNbvReportIdentifier(nbvReportItem);
        if (nbvReportIdentifier == null || nbvReportCollectionIdentifiers.includes(nbvReportIdentifier)) {
          return false;
        }
        nbvReportCollectionIdentifiers.push(nbvReportIdentifier);
        return true;
      });
      return [...nbvReportsToAdd, ...nbvReportCollection];
    }
    return nbvReportCollection;
  }

  protected convertDateFromClient(nbvReport: INbvReport): INbvReport {
    return Object.assign({}, nbvReport, {
      timeOfReportRequest: nbvReport.timeOfReportRequest?.isValid() ? nbvReport.timeOfReportRequest.toJSON() : undefined,
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
      res.body.forEach((nbvReport: INbvReport) => {
        nbvReport.timeOfReportRequest = nbvReport.timeOfReportRequest ? dayjs(nbvReport.timeOfReportRequest) : undefined;
      });
    }
    return res;
  }
}
