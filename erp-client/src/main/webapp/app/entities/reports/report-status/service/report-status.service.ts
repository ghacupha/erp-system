import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IReportStatus, getReportStatusIdentifier } from '../report-status.model';

export type EntityResponseType = HttpResponse<IReportStatus>;
export type EntityArrayResponseType = HttpResponse<IReportStatus[]>;

@Injectable({ providedIn: 'root' })
export class ReportStatusService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/report-statuses');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/report-statuses');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(reportStatus: IReportStatus): Observable<EntityResponseType> {
    return this.http.post<IReportStatus>(this.resourceUrl, reportStatus, { observe: 'response' });
  }

  update(reportStatus: IReportStatus): Observable<EntityResponseType> {
    return this.http.put<IReportStatus>(`${this.resourceUrl}/${getReportStatusIdentifier(reportStatus) as number}`, reportStatus, {
      observe: 'response',
    });
  }

  partialUpdate(reportStatus: IReportStatus): Observable<EntityResponseType> {
    return this.http.patch<IReportStatus>(`${this.resourceUrl}/${getReportStatusIdentifier(reportStatus) as number}`, reportStatus, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IReportStatus>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IReportStatus[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IReportStatus[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  addReportStatusToCollectionIfMissing(
    reportStatusCollection: IReportStatus[],
    ...reportStatusesToCheck: (IReportStatus | null | undefined)[]
  ): IReportStatus[] {
    const reportStatuses: IReportStatus[] = reportStatusesToCheck.filter(isPresent);
    if (reportStatuses.length > 0) {
      const reportStatusCollectionIdentifiers = reportStatusCollection.map(
        reportStatusItem => getReportStatusIdentifier(reportStatusItem)!
      );
      const reportStatusesToAdd = reportStatuses.filter(reportStatusItem => {
        const reportStatusIdentifier = getReportStatusIdentifier(reportStatusItem);
        if (reportStatusIdentifier == null || reportStatusCollectionIdentifiers.includes(reportStatusIdentifier)) {
          return false;
        }
        reportStatusCollectionIdentifiers.push(reportStatusIdentifier);
        return true;
      });
      return [...reportStatusesToAdd, ...reportStatusCollection];
    }
    return reportStatusCollection;
  }
}
