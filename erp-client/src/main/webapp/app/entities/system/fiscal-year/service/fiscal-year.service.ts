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
import { IFiscalYear, getFiscalYearIdentifier } from '../fiscal-year.model';

export type EntityResponseType = HttpResponse<IFiscalYear>;
export type EntityArrayResponseType = HttpResponse<IFiscalYear[]>;

@Injectable({ providedIn: 'root' })
export class FiscalYearService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/fiscal-years');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/fiscal-years');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(fiscalYear: IFiscalYear): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(fiscalYear);
    return this.http
      .post<IFiscalYear>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(fiscalYear: IFiscalYear): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(fiscalYear);
    return this.http
      .put<IFiscalYear>(`${this.resourceUrl}/${getFiscalYearIdentifier(fiscalYear) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(fiscalYear: IFiscalYear): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(fiscalYear);
    return this.http
      .patch<IFiscalYear>(`${this.resourceUrl}/${getFiscalYearIdentifier(fiscalYear) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IFiscalYear>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IFiscalYear[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IFiscalYear[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  addFiscalYearToCollectionIfMissing(
    fiscalYearCollection: IFiscalYear[],
    ...fiscalYearsToCheck: (IFiscalYear | null | undefined)[]
  ): IFiscalYear[] {
    const fiscalYears: IFiscalYear[] = fiscalYearsToCheck.filter(isPresent);
    if (fiscalYears.length > 0) {
      const fiscalYearCollectionIdentifiers = fiscalYearCollection.map(fiscalYearItem => getFiscalYearIdentifier(fiscalYearItem)!);
      const fiscalYearsToAdd = fiscalYears.filter(fiscalYearItem => {
        const fiscalYearIdentifier = getFiscalYearIdentifier(fiscalYearItem);
        if (fiscalYearIdentifier == null || fiscalYearCollectionIdentifiers.includes(fiscalYearIdentifier)) {
          return false;
        }
        fiscalYearCollectionIdentifiers.push(fiscalYearIdentifier);
        return true;
      });
      return [...fiscalYearsToAdd, ...fiscalYearCollection];
    }
    return fiscalYearCollection;
  }

  protected convertDateFromClient(fiscalYear: IFiscalYear): IFiscalYear {
    return Object.assign({}, fiscalYear, {
      startDate: fiscalYear.startDate?.isValid() ? fiscalYear.startDate.format(DATE_FORMAT) : undefined,
      endDate: fiscalYear.endDate?.isValid() ? fiscalYear.endDate.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.startDate = res.body.startDate ? dayjs(res.body.startDate) : undefined;
      res.body.endDate = res.body.endDate ? dayjs(res.body.endDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((fiscalYear: IFiscalYear) => {
        fiscalYear.startDate = fiscalYear.startDate ? dayjs(fiscalYear.startDate) : undefined;
        fiscalYear.endDate = fiscalYear.endDate ? dayjs(fiscalYear.endDate) : undefined;
      });
    }
    return res;
  }
}
