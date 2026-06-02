import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IReportContentType, getReportContentTypeIdentifier } from '../report-content-type.model';

export type EntityResponseType = HttpResponse<IReportContentType>;
export type EntityArrayResponseType = HttpResponse<IReportContentType[]>;

@Injectable({ providedIn: 'root' })
export class ReportContentTypeService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/report-content-types');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/report-content-types');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(reportContentType: IReportContentType): Observable<EntityResponseType> {
    return this.http.post<IReportContentType>(this.resourceUrl, reportContentType, { observe: 'response' });
  }

  update(reportContentType: IReportContentType): Observable<EntityResponseType> {
    return this.http.put<IReportContentType>(
      `${this.resourceUrl}/${getReportContentTypeIdentifier(reportContentType) as number}`,
      reportContentType,
      { observe: 'response' }
    );
  }

  partialUpdate(reportContentType: IReportContentType): Observable<EntityResponseType> {
    return this.http.patch<IReportContentType>(
      `${this.resourceUrl}/${getReportContentTypeIdentifier(reportContentType) as number}`,
      reportContentType,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IReportContentType>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IReportContentType[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IReportContentType[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  addReportContentTypeToCollectionIfMissing(
    reportContentTypeCollection: IReportContentType[],
    ...reportContentTypesToCheck: (IReportContentType | null | undefined)[]
  ): IReportContentType[] {
    const reportContentTypes: IReportContentType[] = reportContentTypesToCheck.filter(isPresent);
    if (reportContentTypes.length > 0) {
      const reportContentTypeCollectionIdentifiers = reportContentTypeCollection.map(
        reportContentTypeItem => getReportContentTypeIdentifier(reportContentTypeItem)!
      );
      const reportContentTypesToAdd = reportContentTypes.filter(reportContentTypeItem => {
        const reportContentTypeIdentifier = getReportContentTypeIdentifier(reportContentTypeItem);
        if (reportContentTypeIdentifier == null || reportContentTypeCollectionIdentifiers.includes(reportContentTypeIdentifier)) {
          return false;
        }
        reportContentTypeCollectionIdentifiers.push(reportContentTypeIdentifier);
        return true;
      });
      return [...reportContentTypesToAdd, ...reportContentTypeCollection];
    }
    return reportContentTypeCollection;
  }
}
