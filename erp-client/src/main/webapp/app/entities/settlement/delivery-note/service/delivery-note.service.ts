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
import { IDeliveryNote, getDeliveryNoteIdentifier } from '../delivery-note.model';

export type EntityResponseType = HttpResponse<IDeliveryNote>;
export type EntityArrayResponseType = HttpResponse<IDeliveryNote[]>;

@Injectable({ providedIn: 'root' })
export class DeliveryNoteService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/delivery-notes');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/delivery-notes');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(deliveryNote: IDeliveryNote): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(deliveryNote);
    return this.http
      .post<IDeliveryNote>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(deliveryNote: IDeliveryNote): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(deliveryNote);
    return this.http
      .put<IDeliveryNote>(`${this.resourceUrl}/${getDeliveryNoteIdentifier(deliveryNote) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(deliveryNote: IDeliveryNote): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(deliveryNote);
    return this.http
      .patch<IDeliveryNote>(`${this.resourceUrl}/${getDeliveryNoteIdentifier(deliveryNote) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IDeliveryNote>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IDeliveryNote[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IDeliveryNote[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  addDeliveryNoteToCollectionIfMissing(
    deliveryNoteCollection: IDeliveryNote[],
    ...deliveryNotesToCheck: (IDeliveryNote | null | undefined)[]
  ): IDeliveryNote[] {
    const deliveryNotes: IDeliveryNote[] = deliveryNotesToCheck.filter(isPresent);
    if (deliveryNotes.length > 0) {
      const deliveryNoteCollectionIdentifiers = deliveryNoteCollection.map(
        deliveryNoteItem => getDeliveryNoteIdentifier(deliveryNoteItem)!
      );
      const deliveryNotesToAdd = deliveryNotes.filter(deliveryNoteItem => {
        const deliveryNoteIdentifier = getDeliveryNoteIdentifier(deliveryNoteItem);
        if (deliveryNoteIdentifier == null || deliveryNoteCollectionIdentifiers.includes(deliveryNoteIdentifier)) {
          return false;
        }
        deliveryNoteCollectionIdentifiers.push(deliveryNoteIdentifier);
        return true;
      });
      return [...deliveryNotesToAdd, ...deliveryNoteCollection];
    }
    return deliveryNoteCollection;
  }

  protected convertDateFromClient(deliveryNote: IDeliveryNote): IDeliveryNote {
    return Object.assign({}, deliveryNote, {
      documentDate: deliveryNote.documentDate?.isValid() ? deliveryNote.documentDate.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.documentDate = res.body.documentDate ? dayjs(res.body.documentDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((deliveryNote: IDeliveryNote) => {
        deliveryNote.documentDate = deliveryNote.documentDate ? dayjs(deliveryNote.documentDate) : undefined;
      });
    }
    return res;
  }
}
