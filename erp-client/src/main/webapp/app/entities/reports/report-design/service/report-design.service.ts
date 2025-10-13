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

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IReportDesign, getReportDesignIdentifier } from '../report-design.model';

export type EntityResponseType = HttpResponse<IReportDesign>;
export type EntityArrayResponseType = HttpResponse<IReportDesign[]>;

@Injectable({ providedIn: 'root' })
export class ReportDesignService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/report-designs');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/report-designs');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(reportDesign: IReportDesign): Observable<EntityResponseType> {
    return this.http.post<IReportDesign>(this.resourceUrl, reportDesign, { observe: 'response' });
  }

  update(reportDesign: IReportDesign): Observable<EntityResponseType> {
    return this.http.put<IReportDesign>(`${this.resourceUrl}/${getReportDesignIdentifier(reportDesign) as number}`, reportDesign, {
      observe: 'response',
    });
  }

  partialUpdate(reportDesign: IReportDesign): Observable<EntityResponseType> {
    return this.http.patch<IReportDesign>(`${this.resourceUrl}/${getReportDesignIdentifier(reportDesign) as number}`, reportDesign, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IReportDesign>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IReportDesign[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IReportDesign[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  addReportDesignToCollectionIfMissing(
    reportDesignCollection: IReportDesign[],
    ...reportDesignsToCheck: (IReportDesign | null | undefined)[]
  ): IReportDesign[] {
    const reportDesigns: IReportDesign[] = reportDesignsToCheck.filter(isPresent);
    if (reportDesigns.length > 0) {
      const reportDesignCollectionIdentifiers = reportDesignCollection.map(
        reportDesignItem => getReportDesignIdentifier(reportDesignItem)!
      );
      const reportDesignsToAdd = reportDesigns.filter(reportDesignItem => {
        const reportDesignIdentifier = getReportDesignIdentifier(reportDesignItem);
        if (reportDesignIdentifier == null || reportDesignCollectionIdentifiers.includes(reportDesignIdentifier)) {
          return false;
        }
        reportDesignCollectionIdentifiers.push(reportDesignIdentifier);
        return true;
      });
      return [...reportDesignsToAdd, ...reportDesignCollection];
    }
    return reportDesignCollection;
  }
}
