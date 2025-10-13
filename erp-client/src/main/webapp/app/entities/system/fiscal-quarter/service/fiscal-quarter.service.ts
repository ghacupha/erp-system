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
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IFiscalQuarter, getFiscalQuarterIdentifier } from '../fiscal-quarter.model';

export type EntityResponseType = HttpResponse<IFiscalQuarter>;
export type EntityArrayResponseType = HttpResponse<IFiscalQuarter[]>;

@Injectable({ providedIn: 'root' })
export class FiscalQuarterService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/fiscal-quarters');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/fiscal-quarters');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(fiscalQuarter: IFiscalQuarter): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(fiscalQuarter);
    return this.http
      .post<IFiscalQuarter>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(fiscalQuarter: IFiscalQuarter): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(fiscalQuarter);
    return this.http
      .put<IFiscalQuarter>(`${this.resourceUrl}/${getFiscalQuarterIdentifier(fiscalQuarter) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(fiscalQuarter: IFiscalQuarter): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(fiscalQuarter);
    return this.http
      .patch<IFiscalQuarter>(`${this.resourceUrl}/${getFiscalQuarterIdentifier(fiscalQuarter) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IFiscalQuarter>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IFiscalQuarter[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IFiscalQuarter[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  addFiscalQuarterToCollectionIfMissing(
    fiscalQuarterCollection: IFiscalQuarter[],
    ...fiscalQuartersToCheck: (IFiscalQuarter | null | undefined)[]
  ): IFiscalQuarter[] {
    const fiscalQuarters: IFiscalQuarter[] = fiscalQuartersToCheck.filter(isPresent);
    if (fiscalQuarters.length > 0) {
      const fiscalQuarterCollectionIdentifiers = fiscalQuarterCollection.map(
        fiscalQuarterItem => getFiscalQuarterIdentifier(fiscalQuarterItem)!
      );
      const fiscalQuartersToAdd = fiscalQuarters.filter(fiscalQuarterItem => {
        const fiscalQuarterIdentifier = getFiscalQuarterIdentifier(fiscalQuarterItem);
        if (fiscalQuarterIdentifier == null || fiscalQuarterCollectionIdentifiers.includes(fiscalQuarterIdentifier)) {
          return false;
        }
        fiscalQuarterCollectionIdentifiers.push(fiscalQuarterIdentifier);
        return true;
      });
      return [...fiscalQuartersToAdd, ...fiscalQuarterCollection];
    }
    return fiscalQuarterCollection;
  }

  protected convertDateFromClient(fiscalQuarter: IFiscalQuarter): IFiscalQuarter {
    return Object.assign({}, fiscalQuarter, {
      startDate: fiscalQuarter.startDate?.isValid() ? fiscalQuarter.startDate.format(DATE_FORMAT) : undefined,
      endDate: fiscalQuarter.endDate?.isValid() ? fiscalQuarter.endDate.format(DATE_FORMAT) : undefined,
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
      res.body.forEach((fiscalQuarter: IFiscalQuarter) => {
        fiscalQuarter.startDate = fiscalQuarter.startDate ? dayjs(fiscalQuarter.startDate) : undefined;
        fiscalQuarter.endDate = fiscalQuarter.endDate ? dayjs(fiscalQuarter.endDate) : undefined;
      });
    }
    return res;
  }
}
