import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IWorkInProgressReport, getWorkInProgressReportIdentifier } from '../work-in-progress-report.model';

export type EntityResponseType = HttpResponse<IWorkInProgressReport>;
export type EntityArrayResponseType = HttpResponse<IWorkInProgressReport[]>;

@Injectable({ providedIn: 'root' })
export class WorkInProgressReportService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/work-in-progress-reports');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/work-in-progress-reports');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IWorkInProgressReport>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IWorkInProgressReport[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IWorkInProgressReport[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  addWorkInProgressReportToCollectionIfMissing(
    workInProgressReportCollection: IWorkInProgressReport[],
    ...workInProgressReportsToCheck: (IWorkInProgressReport | null | undefined)[]
  ): IWorkInProgressReport[] {
    const workInProgressReports: IWorkInProgressReport[] = workInProgressReportsToCheck.filter(isPresent);
    if (workInProgressReports.length > 0) {
      const workInProgressReportCollectionIdentifiers = workInProgressReportCollection.map(
        workInProgressReportItem => getWorkInProgressReportIdentifier(workInProgressReportItem)!
      );
      const workInProgressReportsToAdd = workInProgressReports.filter(workInProgressReportItem => {
        const workInProgressReportIdentifier = getWorkInProgressReportIdentifier(workInProgressReportItem);
        if (workInProgressReportIdentifier == null || workInProgressReportCollectionIdentifiers.includes(workInProgressReportIdentifier)) {
          return false;
        }
        workInProgressReportCollectionIdentifiers.push(workInProgressReportIdentifier);
        return true;
      });
      return [...workInProgressReportsToAdd, ...workInProgressReportCollection];
    }
    return workInProgressReportCollection;
  }
}
