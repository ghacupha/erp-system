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
import { IAutonomousReport, getAutonomousReportIdentifier } from '../autonomous-report.model';

export type EntityResponseType = HttpResponse<IAutonomousReport>;
export type EntityArrayResponseType = HttpResponse<IAutonomousReport[]>;

@Injectable({ providedIn: 'root' })
export class AutonomousReportService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/app/autonomous-reports');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/app/_search/autonomous-reports');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IAutonomousReport>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IAutonomousReport[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IAutonomousReport[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  addAutonomousReportToCollectionIfMissing(
    autonomousReportCollection: IAutonomousReport[],
    ...autonomousReportsToCheck: (IAutonomousReport | null | undefined)[]
  ): IAutonomousReport[] {
    const autonomousReports: IAutonomousReport[] = autonomousReportsToCheck.filter(isPresent);
    if (autonomousReports.length > 0) {
      const autonomousReportCollectionIdentifiers = autonomousReportCollection.map(
        autonomousReportItem => getAutonomousReportIdentifier(autonomousReportItem)!
      );
      const autonomousReportsToAdd = autonomousReports.filter(autonomousReportItem => {
        const autonomousReportIdentifier = getAutonomousReportIdentifier(autonomousReportItem);
        if (autonomousReportIdentifier == null || autonomousReportCollectionIdentifiers.includes(autonomousReportIdentifier)) {
          return false;
        }
        autonomousReportCollectionIdentifiers.push(autonomousReportIdentifier);
        return true;
      });
      return [...autonomousReportsToAdd, ...autonomousReportCollection];
    }
    return autonomousReportCollection;
  }

  protected convertDateFromClient(autonomousReport: IAutonomousReport): IAutonomousReport {
    return Object.assign({}, autonomousReport, {
      createdAt: autonomousReport.createdAt?.isValid() ? autonomousReport.createdAt.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.createdAt = res.body.createdAt ? dayjs(res.body.createdAt) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((autonomousReport: IAutonomousReport) => {
        autonomousReport.createdAt = autonomousReport.createdAt ? dayjs(autonomousReport.createdAt) : undefined;
      });
    }
    return res;
  }
}
