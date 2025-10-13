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
import { IAgencyNotice, getAgencyNoticeIdentifier } from '../agency-notice.model';

export type EntityResponseType = HttpResponse<IAgencyNotice>;
export type EntityArrayResponseType = HttpResponse<IAgencyNotice[]>;

@Injectable({ providedIn: 'root' })
export class AgencyNoticeService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/agency-notices');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/agency-notices');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(agencyNotice: IAgencyNotice): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(agencyNotice);
    return this.http
      .post<IAgencyNotice>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(agencyNotice: IAgencyNotice): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(agencyNotice);
    return this.http
      .put<IAgencyNotice>(`${this.resourceUrl}/${getAgencyNoticeIdentifier(agencyNotice) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(agencyNotice: IAgencyNotice): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(agencyNotice);
    return this.http
      .patch<IAgencyNotice>(`${this.resourceUrl}/${getAgencyNoticeIdentifier(agencyNotice) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IAgencyNotice>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IAgencyNotice[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IAgencyNotice[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  addAgencyNoticeToCollectionIfMissing(
    agencyNoticeCollection: IAgencyNotice[],
    ...agencyNoticesToCheck: (IAgencyNotice | null | undefined)[]
  ): IAgencyNotice[] {
    const agencyNotices: IAgencyNotice[] = agencyNoticesToCheck.filter(isPresent);
    if (agencyNotices.length > 0) {
      const agencyNoticeCollectionIdentifiers = agencyNoticeCollection.map(
        agencyNoticeItem => getAgencyNoticeIdentifier(agencyNoticeItem)!
      );
      const agencyNoticesToAdd = agencyNotices.filter(agencyNoticeItem => {
        const agencyNoticeIdentifier = getAgencyNoticeIdentifier(agencyNoticeItem);
        if (agencyNoticeIdentifier == null || agencyNoticeCollectionIdentifiers.includes(agencyNoticeIdentifier)) {
          return false;
        }
        agencyNoticeCollectionIdentifiers.push(agencyNoticeIdentifier);
        return true;
      });
      return [...agencyNoticesToAdd, ...agencyNoticeCollection];
    }
    return agencyNoticeCollection;
  }

  protected convertDateFromClient(agencyNotice: IAgencyNotice): IAgencyNotice {
    return Object.assign({}, agencyNotice, {
      referenceDate: agencyNotice.referenceDate?.isValid() ? agencyNotice.referenceDate.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.referenceDate = res.body.referenceDate ? dayjs(res.body.referenceDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((agencyNotice: IAgencyNotice) => {
        agencyNotice.referenceDate = agencyNotice.referenceDate ? dayjs(agencyNotice.referenceDate) : undefined;
      });
    }
    return res;
  }
}
