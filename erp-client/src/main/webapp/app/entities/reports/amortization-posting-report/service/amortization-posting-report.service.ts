import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IAmortizationPostingReport, getAmortizationPostingReportIdentifier } from '../amortization-posting-report.model';

export type EntityResponseType = HttpResponse<IAmortizationPostingReport>;
export type EntityArrayResponseType = HttpResponse<IAmortizationPostingReport[]>;

@Injectable({ providedIn: 'root' })
export class AmortizationPostingReportService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/amortization-posting-reports');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/amortization-posting-reports');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IAmortizationPostingReport>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAmortizationPostingReport[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAmortizationPostingReport[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  addAmortizationPostingReportToCollectionIfMissing(
    amortizationPostingReportCollection: IAmortizationPostingReport[],
    ...amortizationPostingReportsToCheck: (IAmortizationPostingReport | null | undefined)[]
  ): IAmortizationPostingReport[] {
    const amortizationPostingReports: IAmortizationPostingReport[] = amortizationPostingReportsToCheck.filter(isPresent);
    if (amortizationPostingReports.length > 0) {
      const amortizationPostingReportCollectionIdentifiers = amortizationPostingReportCollection.map(
        amortizationPostingReportItem => getAmortizationPostingReportIdentifier(amortizationPostingReportItem)!
      );
      const amortizationPostingReportsToAdd = amortizationPostingReports.filter(amortizationPostingReportItem => {
        const amortizationPostingReportIdentifier = getAmortizationPostingReportIdentifier(amortizationPostingReportItem);
        if (
          amortizationPostingReportIdentifier == null ||
          amortizationPostingReportCollectionIdentifiers.includes(amortizationPostingReportIdentifier)
        ) {
          return false;
        }
        amortizationPostingReportCollectionIdentifiers.push(amortizationPostingReportIdentifier);
        return true;
      });
      return [...amortizationPostingReportsToAdd, ...amortizationPostingReportCollection];
    }
    return amortizationPostingReportCollection;
  }
}
