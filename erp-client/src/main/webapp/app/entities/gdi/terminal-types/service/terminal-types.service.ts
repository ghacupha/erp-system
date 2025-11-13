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

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { ITerminalTypes, getTerminalTypesIdentifier } from '../terminal-types.model';

export type EntityResponseType = HttpResponse<ITerminalTypes>;
export type EntityArrayResponseType = HttpResponse<ITerminalTypes[]>;

@Injectable({ providedIn: 'root' })
export class TerminalTypesService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/terminal-types');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/terminal-types');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(terminalTypes: ITerminalTypes): Observable<EntityResponseType> {
    return this.http.post<ITerminalTypes>(this.resourceUrl, terminalTypes, { observe: 'response' });
  }

  update(terminalTypes: ITerminalTypes): Observable<EntityResponseType> {
    return this.http.put<ITerminalTypes>(`${this.resourceUrl}/${getTerminalTypesIdentifier(terminalTypes) as number}`, terminalTypes, {
      observe: 'response',
    });
  }

  partialUpdate(terminalTypes: ITerminalTypes): Observable<EntityResponseType> {
    return this.http.patch<ITerminalTypes>(`${this.resourceUrl}/${getTerminalTypesIdentifier(terminalTypes) as number}`, terminalTypes, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITerminalTypes>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITerminalTypes[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITerminalTypes[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  addTerminalTypesToCollectionIfMissing(
    terminalTypesCollection: ITerminalTypes[],
    ...terminalTypesToCheck: (ITerminalTypes | null | undefined)[]
  ): ITerminalTypes[] {
    const terminalTypes: ITerminalTypes[] = terminalTypesToCheck.filter(isPresent);
    if (terminalTypes.length > 0) {
      const terminalTypesCollectionIdentifiers = terminalTypesCollection.map(
        terminalTypesItem => getTerminalTypesIdentifier(terminalTypesItem)!
      );
      const terminalTypesToAdd = terminalTypes.filter(terminalTypesItem => {
        const terminalTypesIdentifier = getTerminalTypesIdentifier(terminalTypesItem);
        if (terminalTypesIdentifier == null || terminalTypesCollectionIdentifiers.includes(terminalTypesIdentifier)) {
          return false;
        }
        terminalTypesCollectionIdentifiers.push(terminalTypesIdentifier);
        return true;
      });
      return [...terminalTypesToAdd, ...terminalTypesCollection];
    }
    return terminalTypesCollection;
  }
}
