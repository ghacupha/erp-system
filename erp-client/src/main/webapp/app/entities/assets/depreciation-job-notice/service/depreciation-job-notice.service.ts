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
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IDepreciationJobNotice, getDepreciationJobNoticeIdentifier } from '../depreciation-job-notice.model';

export type EntityResponseType = HttpResponse<IDepreciationJobNotice>;
export type EntityArrayResponseType = HttpResponse<IDepreciationJobNotice[]>;

@Injectable({ providedIn: 'root' })
export class DepreciationJobNoticeService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/depreciation-job-notices');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/depreciation-job-notices');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(depreciationJobNotice: IDepreciationJobNotice): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(depreciationJobNotice);
    return this.http
      .post<IDepreciationJobNotice>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(depreciationJobNotice: IDepreciationJobNotice): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(depreciationJobNotice);
    return this.http
      .put<IDepreciationJobNotice>(`${this.resourceUrl}/${getDepreciationJobNoticeIdentifier(depreciationJobNotice) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(depreciationJobNotice: IDepreciationJobNotice): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(depreciationJobNotice);
    return this.http
      .patch<IDepreciationJobNotice>(`${this.resourceUrl}/${getDepreciationJobNoticeIdentifier(depreciationJobNotice) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IDepreciationJobNotice>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IDepreciationJobNotice[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IDepreciationJobNotice[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  addDepreciationJobNoticeToCollectionIfMissing(
    depreciationJobNoticeCollection: IDepreciationJobNotice[],
    ...depreciationJobNoticesToCheck: (IDepreciationJobNotice | null | undefined)[]
  ): IDepreciationJobNotice[] {
    const depreciationJobNotices: IDepreciationJobNotice[] = depreciationJobNoticesToCheck.filter(isPresent);
    if (depreciationJobNotices.length > 0) {
      const depreciationJobNoticeCollectionIdentifiers = depreciationJobNoticeCollection.map(
        depreciationJobNoticeItem => getDepreciationJobNoticeIdentifier(depreciationJobNoticeItem)!
      );
      const depreciationJobNoticesToAdd = depreciationJobNotices.filter(depreciationJobNoticeItem => {
        const depreciationJobNoticeIdentifier = getDepreciationJobNoticeIdentifier(depreciationJobNoticeItem);
        if (
          depreciationJobNoticeIdentifier == null ||
          depreciationJobNoticeCollectionIdentifiers.includes(depreciationJobNoticeIdentifier)
        ) {
          return false;
        }
        depreciationJobNoticeCollectionIdentifiers.push(depreciationJobNoticeIdentifier);
        return true;
      });
      return [...depreciationJobNoticesToAdd, ...depreciationJobNoticeCollection];
    }
    return depreciationJobNoticeCollection;
  }

  protected convertDateFromClient(depreciationJobNotice: IDepreciationJobNotice): IDepreciationJobNotice {
    return Object.assign({}, depreciationJobNotice, {
      eventTimeStamp: depreciationJobNotice.eventTimeStamp?.isValid() ? depreciationJobNotice.eventTimeStamp.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.eventTimeStamp = res.body.eventTimeStamp ? dayjs(res.body.eventTimeStamp) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((depreciationJobNotice: IDepreciationJobNotice) => {
        depreciationJobNotice.eventTimeStamp = depreciationJobNotice.eventTimeStamp
          ? dayjs(depreciationJobNotice.eventTimeStamp)
          : undefined;
      });
    }
    return res;
  }
}
