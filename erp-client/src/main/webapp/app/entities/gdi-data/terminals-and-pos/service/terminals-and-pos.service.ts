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
import { ITerminalsAndPOS, getTerminalsAndPOSIdentifier } from '../terminals-and-pos.model';

export type EntityResponseType = HttpResponse<ITerminalsAndPOS>;
export type EntityArrayResponseType = HttpResponse<ITerminalsAndPOS[]>;

@Injectable({ providedIn: 'root' })
export class TerminalsAndPOSService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/terminals-and-pos');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/terminals-and-pos');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(terminalsAndPOS: ITerminalsAndPOS): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(terminalsAndPOS);
    return this.http
      .post<ITerminalsAndPOS>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(terminalsAndPOS: ITerminalsAndPOS): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(terminalsAndPOS);
    return this.http
      .put<ITerminalsAndPOS>(`${this.resourceUrl}/${getTerminalsAndPOSIdentifier(terminalsAndPOS) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(terminalsAndPOS: ITerminalsAndPOS): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(terminalsAndPOS);
    return this.http
      .patch<ITerminalsAndPOS>(`${this.resourceUrl}/${getTerminalsAndPOSIdentifier(terminalsAndPOS) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ITerminalsAndPOS>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ITerminalsAndPOS[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ITerminalsAndPOS[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  addTerminalsAndPOSToCollectionIfMissing(
    terminalsAndPOSCollection: ITerminalsAndPOS[],
    ...terminalsAndPOSToCheck: (ITerminalsAndPOS | null | undefined)[]
  ): ITerminalsAndPOS[] {
    const terminalsAndPOS: ITerminalsAndPOS[] = terminalsAndPOSToCheck.filter(isPresent);
    if (terminalsAndPOS.length > 0) {
      const terminalsAndPOSCollectionIdentifiers = terminalsAndPOSCollection.map(
        terminalsAndPOSItem => getTerminalsAndPOSIdentifier(terminalsAndPOSItem)!
      );
      const terminalsAndPOSToAdd = terminalsAndPOS.filter(terminalsAndPOSItem => {
        const terminalsAndPOSIdentifier = getTerminalsAndPOSIdentifier(terminalsAndPOSItem);
        if (terminalsAndPOSIdentifier == null || terminalsAndPOSCollectionIdentifiers.includes(terminalsAndPOSIdentifier)) {
          return false;
        }
        terminalsAndPOSCollectionIdentifiers.push(terminalsAndPOSIdentifier);
        return true;
      });
      return [...terminalsAndPOSToAdd, ...terminalsAndPOSCollection];
    }
    return terminalsAndPOSCollection;
  }

  protected convertDateFromClient(terminalsAndPOS: ITerminalsAndPOS): ITerminalsAndPOS {
    return Object.assign({}, terminalsAndPOS, {
      reportingDate: terminalsAndPOS.reportingDate?.isValid() ? terminalsAndPOS.reportingDate.format(DATE_FORMAT) : undefined,
      terminalOpeningDate: terminalsAndPOS.terminalOpeningDate?.isValid()
        ? terminalsAndPOS.terminalOpeningDate.format(DATE_FORMAT)
        : undefined,
      terminalClosureDate: terminalsAndPOS.terminalClosureDate?.isValid()
        ? terminalsAndPOS.terminalClosureDate.format(DATE_FORMAT)
        : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.reportingDate = res.body.reportingDate ? dayjs(res.body.reportingDate) : undefined;
      res.body.terminalOpeningDate = res.body.terminalOpeningDate ? dayjs(res.body.terminalOpeningDate) : undefined;
      res.body.terminalClosureDate = res.body.terminalClosureDate ? dayjs(res.body.terminalClosureDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((terminalsAndPOS: ITerminalsAndPOS) => {
        terminalsAndPOS.reportingDate = terminalsAndPOS.reportingDate ? dayjs(terminalsAndPOS.reportingDate) : undefined;
        terminalsAndPOS.terminalOpeningDate = terminalsAndPOS.terminalOpeningDate ? dayjs(terminalsAndPOS.terminalOpeningDate) : undefined;
        terminalsAndPOS.terminalClosureDate = terminalsAndPOS.terminalClosureDate ? dayjs(terminalsAndPOS.terminalClosureDate) : undefined;
      });
    }
    return res;
  }
}
