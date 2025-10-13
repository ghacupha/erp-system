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
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IBusinessDocument, getBusinessDocumentIdentifier } from '../business-document.model';

export type EntityResponseType = HttpResponse<IBusinessDocument>;
export type EntityArrayResponseType = HttpResponse<IBusinessDocument[]>;

@Injectable({ providedIn: 'root' })
export class BusinessDocumentService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/business-documents');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/business-documents');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(businessDocument: IBusinessDocument): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(businessDocument);
    return this.http
      .post<IBusinessDocument>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(businessDocument: IBusinessDocument): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(businessDocument);
    return this.http
      .put<IBusinessDocument>(`${this.resourceUrl}/${getBusinessDocumentIdentifier(businessDocument) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(businessDocument: IBusinessDocument): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(businessDocument);
    return this.http
      .patch<IBusinessDocument>(`${this.resourceUrl}/${getBusinessDocumentIdentifier(businessDocument) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IBusinessDocument>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IBusinessDocument[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IBusinessDocument[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  addBusinessDocumentToCollectionIfMissing(
    businessDocumentCollection: IBusinessDocument[],
    ...businessDocumentsToCheck: (IBusinessDocument | null | undefined)[]
  ): IBusinessDocument[] {
    const businessDocuments: IBusinessDocument[] = businessDocumentsToCheck.filter(isPresent);
    if (businessDocuments.length > 0) {
      const businessDocumentCollectionIdentifiers = businessDocumentCollection.map(
        businessDocumentItem => getBusinessDocumentIdentifier(businessDocumentItem)!
      );
      const businessDocumentsToAdd = businessDocuments.filter(businessDocumentItem => {
        const businessDocumentIdentifier = getBusinessDocumentIdentifier(businessDocumentItem);
        if (businessDocumentIdentifier == null || businessDocumentCollectionIdentifiers.includes(businessDocumentIdentifier)) {
          return false;
        }
        businessDocumentCollectionIdentifiers.push(businessDocumentIdentifier);
        return true;
      });
      return [...businessDocumentsToAdd, ...businessDocumentCollection];
    }
    return businessDocumentCollection;
  }

  protected convertDateFromClient(businessDocument: IBusinessDocument): IBusinessDocument {
    return Object.assign({}, businessDocument, {
      lastModified: businessDocument.lastModified?.isValid() ? businessDocument.lastModified.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.lastModified = res.body.lastModified ? dayjs(res.body.lastModified) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((businessDocument: IBusinessDocument) => {
        businessDocument.lastModified = businessDocument.lastModified ? dayjs(businessDocument.lastModified) : undefined;
      });
    }
    return res;
  }
}
