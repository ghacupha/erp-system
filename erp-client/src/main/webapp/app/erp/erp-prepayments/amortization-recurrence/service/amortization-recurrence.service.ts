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
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IAmortizationRecurrence, getAmortizationRecurrenceIdentifier } from '../amortization-recurrence.model';

export type EntityResponseType = HttpResponse<IAmortizationRecurrence>;
export type EntityArrayResponseType = HttpResponse<IAmortizationRecurrence[]>;

@Injectable({ providedIn: 'root' })
export class AmortizationRecurrenceService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/prepayments/amortization-recurrences');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/prepayments/_search/amortization-recurrences');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(amortizationRecurrence: IAmortizationRecurrence): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(amortizationRecurrence);
    return this.http
      .post<IAmortizationRecurrence>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(amortizationRecurrence: IAmortizationRecurrence): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(amortizationRecurrence);
    return this.http
      .put<IAmortizationRecurrence>(`${this.resourceUrl}/${getAmortizationRecurrenceIdentifier(amortizationRecurrence) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(amortizationRecurrence: IAmortizationRecurrence): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(amortizationRecurrence);
    return this.http
      .patch<IAmortizationRecurrence>(
        `${this.resourceUrl}/${getAmortizationRecurrenceIdentifier(amortizationRecurrence) as number}`,
        copy,
        { observe: 'response' }
      )
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IAmortizationRecurrence>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IAmortizationRecurrence[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IAmortizationRecurrence[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  addAmortizationRecurrenceToCollectionIfMissing(
    amortizationRecurrenceCollection: IAmortizationRecurrence[],
    ...amortizationRecurrencesToCheck: (IAmortizationRecurrence | null | undefined)[]
  ): IAmortizationRecurrence[] {
    const amortizationRecurrences: IAmortizationRecurrence[] = amortizationRecurrencesToCheck.filter(isPresent);
    if (amortizationRecurrences.length > 0) {
      const amortizationRecurrenceCollectionIdentifiers = amortizationRecurrenceCollection.map(
        amortizationRecurrenceItem => getAmortizationRecurrenceIdentifier(amortizationRecurrenceItem)!
      );
      const amortizationRecurrencesToAdd = amortizationRecurrences.filter(amortizationRecurrenceItem => {
        const amortizationRecurrenceIdentifier = getAmortizationRecurrenceIdentifier(amortizationRecurrenceItem);
        if (
          amortizationRecurrenceIdentifier == null ||
          amortizationRecurrenceCollectionIdentifiers.includes(amortizationRecurrenceIdentifier)
        ) {
          return false;
        }
        amortizationRecurrenceCollectionIdentifiers.push(amortizationRecurrenceIdentifier);
        return true;
      });
      return [...amortizationRecurrencesToAdd, ...amortizationRecurrenceCollection];
    }
    return amortizationRecurrenceCollection;
  }

  protected convertDateFromClient(amortizationRecurrence: IAmortizationRecurrence): IAmortizationRecurrence {
    return Object.assign({}, amortizationRecurrence, {
      firstAmortizationDate: amortizationRecurrence.firstAmortizationDate?.isValid()
        ? amortizationRecurrence.firstAmortizationDate.format(DATE_FORMAT)
        : undefined,
      timeOfInstallation: amortizationRecurrence.timeOfInstallation?.isValid()
        ? amortizationRecurrence.timeOfInstallation.toJSON()
        : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.firstAmortizationDate = res.body.firstAmortizationDate ? dayjs(res.body.firstAmortizationDate) : undefined;
      res.body.timeOfInstallation = res.body.timeOfInstallation ? dayjs(res.body.timeOfInstallation) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((amortizationRecurrence: IAmortizationRecurrence) => {
        amortizationRecurrence.firstAmortizationDate = amortizationRecurrence.firstAmortizationDate
          ? dayjs(amortizationRecurrence.firstAmortizationDate)
          : undefined;
        amortizationRecurrence.timeOfInstallation = amortizationRecurrence.timeOfInstallation
          ? dayjs(amortizationRecurrence.timeOfInstallation)
          : undefined;
      });
    }
    return res;
  }
}
