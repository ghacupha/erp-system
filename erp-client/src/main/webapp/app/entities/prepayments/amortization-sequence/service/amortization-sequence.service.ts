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
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IAmortizationSequence, getAmortizationSequenceIdentifier } from '../amortization-sequence.model';

export type EntityResponseType = HttpResponse<IAmortizationSequence>;
export type EntityArrayResponseType = HttpResponse<IAmortizationSequence[]>;

@Injectable({ providedIn: 'root' })
export class AmortizationSequenceService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/amortization-sequences');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/amortization-sequences');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(amortizationSequence: IAmortizationSequence): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(amortizationSequence);
    return this.http
      .post<IAmortizationSequence>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(amortizationSequence: IAmortizationSequence): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(amortizationSequence);
    return this.http
      .put<IAmortizationSequence>(`${this.resourceUrl}/${getAmortizationSequenceIdentifier(amortizationSequence) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(amortizationSequence: IAmortizationSequence): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(amortizationSequence);
    return this.http
      .patch<IAmortizationSequence>(`${this.resourceUrl}/${getAmortizationSequenceIdentifier(amortizationSequence) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IAmortizationSequence>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IAmortizationSequence[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IAmortizationSequence[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  addAmortizationSequenceToCollectionIfMissing(
    amortizationSequenceCollection: IAmortizationSequence[],
    ...amortizationSequencesToCheck: (IAmortizationSequence | null | undefined)[]
  ): IAmortizationSequence[] {
    const amortizationSequences: IAmortizationSequence[] = amortizationSequencesToCheck.filter(isPresent);
    if (amortizationSequences.length > 0) {
      const amortizationSequenceCollectionIdentifiers = amortizationSequenceCollection.map(
        amortizationSequenceItem => getAmortizationSequenceIdentifier(amortizationSequenceItem)!
      );
      const amortizationSequencesToAdd = amortizationSequences.filter(amortizationSequenceItem => {
        const amortizationSequenceIdentifier = getAmortizationSequenceIdentifier(amortizationSequenceItem);
        if (amortizationSequenceIdentifier == null || amortizationSequenceCollectionIdentifiers.includes(amortizationSequenceIdentifier)) {
          return false;
        }
        amortizationSequenceCollectionIdentifiers.push(amortizationSequenceIdentifier);
        return true;
      });
      return [...amortizationSequencesToAdd, ...amortizationSequenceCollection];
    }
    return amortizationSequenceCollection;
  }

  protected convertDateFromClient(amortizationSequence: IAmortizationSequence): IAmortizationSequence {
    return Object.assign({}, amortizationSequence, {
      currentAmortizationDate: amortizationSequence.currentAmortizationDate?.isValid()
        ? amortizationSequence.currentAmortizationDate.format(DATE_FORMAT)
        : undefined,
      previousAmortizationDate: amortizationSequence.previousAmortizationDate?.isValid()
        ? amortizationSequence.previousAmortizationDate.format(DATE_FORMAT)
        : undefined,
      nextAmortizationDate: amortizationSequence.nextAmortizationDate?.isValid()
        ? amortizationSequence.nextAmortizationDate.format(DATE_FORMAT)
        : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.currentAmortizationDate = res.body.currentAmortizationDate ? dayjs(res.body.currentAmortizationDate) : undefined;
      res.body.previousAmortizationDate = res.body.previousAmortizationDate ? dayjs(res.body.previousAmortizationDate) : undefined;
      res.body.nextAmortizationDate = res.body.nextAmortizationDate ? dayjs(res.body.nextAmortizationDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((amortizationSequence: IAmortizationSequence) => {
        amortizationSequence.currentAmortizationDate = amortizationSequence.currentAmortizationDate
          ? dayjs(amortizationSequence.currentAmortizationDate)
          : undefined;
        amortizationSequence.previousAmortizationDate = amortizationSequence.previousAmortizationDate
          ? dayjs(amortizationSequence.previousAmortizationDate)
          : undefined;
        amortizationSequence.nextAmortizationDate = amortizationSequence.nextAmortizationDate
          ? dayjs(amortizationSequence.nextAmortizationDate)
          : undefined;
      });
    }
    return res;
  }
}
