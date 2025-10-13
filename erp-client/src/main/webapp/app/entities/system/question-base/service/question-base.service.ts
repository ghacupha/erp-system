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

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IQuestionBase, getQuestionBaseIdentifier } from '../question-base.model';

export type EntityResponseType = HttpResponse<IQuestionBase>;
export type EntityArrayResponseType = HttpResponse<IQuestionBase[]>;

@Injectable({ providedIn: 'root' })
export class QuestionBaseService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/question-bases');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/question-bases');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(questionBase: IQuestionBase): Observable<EntityResponseType> {
    return this.http.post<IQuestionBase>(this.resourceUrl, questionBase, { observe: 'response' });
  }

  update(questionBase: IQuestionBase): Observable<EntityResponseType> {
    return this.http.put<IQuestionBase>(`${this.resourceUrl}/${getQuestionBaseIdentifier(questionBase) as number}`, questionBase, {
      observe: 'response',
    });
  }

  partialUpdate(questionBase: IQuestionBase): Observable<EntityResponseType> {
    return this.http.patch<IQuestionBase>(`${this.resourceUrl}/${getQuestionBaseIdentifier(questionBase) as number}`, questionBase, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IQuestionBase>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IQuestionBase[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IQuestionBase[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  addQuestionBaseToCollectionIfMissing(
    questionBaseCollection: IQuestionBase[],
    ...questionBasesToCheck: (IQuestionBase | null | undefined)[]
  ): IQuestionBase[] {
    const questionBases: IQuestionBase[] = questionBasesToCheck.filter(isPresent);
    if (questionBases.length > 0) {
      const questionBaseCollectionIdentifiers = questionBaseCollection.map(
        questionBaseItem => getQuestionBaseIdentifier(questionBaseItem)!
      );
      const questionBasesToAdd = questionBases.filter(questionBaseItem => {
        const questionBaseIdentifier = getQuestionBaseIdentifier(questionBaseItem);
        if (questionBaseIdentifier == null || questionBaseCollectionIdentifiers.includes(questionBaseIdentifier)) {
          return false;
        }
        questionBaseCollectionIdentifiers.push(questionBaseIdentifier);
        return true;
      });
      return [...questionBasesToAdd, ...questionBaseCollection];
    }
    return questionBaseCollection;
  }
}
