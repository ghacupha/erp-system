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
import { IPdfReportRequisition, getPdfReportRequisitionIdentifier } from '../pdf-report-requisition.model';
import { sha512 } from 'hash-wasm';

export type EntityResponseType = HttpResponse<IPdfReportRequisition>;
export type EntityArrayResponseType = HttpResponse<IPdfReportRequisition[]>;

@Injectable({ providedIn: 'root' })
export class PdfReportRequisitionService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/app/pdf-report-requisitions');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/app/_search/pdf-report-requisitions');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(pdfReportRequisition: IPdfReportRequisition): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(pdfReportRequisition);
    return this.http
      .post<IPdfReportRequisition>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)))
      .pipe(map((res: EntityResponseType) => this.convertPasswordFromServer(res)));
  }

  update(pdfReportRequisition: IPdfReportRequisition): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(pdfReportRequisition);
    return this.http
      .put<IPdfReportRequisition>(`${this.resourceUrl}/${getPdfReportRequisitionIdentifier(pdfReportRequisition) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)))
      .pipe(map((res: EntityResponseType) => this.convertPasswordFromServer(res)));
  }

  partialUpdate(pdfReportRequisition: IPdfReportRequisition): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(pdfReportRequisition);
    return this.http
      .patch<IPdfReportRequisition>(`${this.resourceUrl}/${getPdfReportRequisitionIdentifier(pdfReportRequisition) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)))
      .pipe(map((res: EntityResponseType) => this.convertPasswordFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IPdfReportRequisition>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)))
      .pipe(map((res: EntityResponseType) => this.convertPasswordFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IPdfReportRequisition[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)))
      .pipe(map((res: EntityArrayResponseType) => this.convertPasswordArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IPdfReportRequisition[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)))
      .pipe(map((res: EntityArrayResponseType) => this.convertPasswordArrayFromServer(res)));
  }

  addPdfReportRequisitionToCollectionIfMissing(
    pdfReportRequisitionCollection: IPdfReportRequisition[],
    ...pdfReportRequisitionsToCheck: (IPdfReportRequisition | null | undefined)[]
  ): IPdfReportRequisition[] {
    const pdfReportRequisitions: IPdfReportRequisition[] = pdfReportRequisitionsToCheck.filter(isPresent);
    if (pdfReportRequisitions.length > 0) {
      const pdfReportRequisitionCollectionIdentifiers = pdfReportRequisitionCollection.map(
        pdfReportRequisitionItem => getPdfReportRequisitionIdentifier(pdfReportRequisitionItem)!
      );
      const pdfReportRequisitionsToAdd = pdfReportRequisitions.filter(pdfReportRequisitionItem => {
        const pdfReportRequisitionIdentifier = getPdfReportRequisitionIdentifier(pdfReportRequisitionItem);
        if (pdfReportRequisitionIdentifier == null || pdfReportRequisitionCollectionIdentifiers.includes(pdfReportRequisitionIdentifier)) {
          return false;
        }
        pdfReportRequisitionCollectionIdentifiers.push(pdfReportRequisitionIdentifier);
        return true;
      });
      return [...pdfReportRequisitionsToAdd, ...pdfReportRequisitionCollection];
    }
    return pdfReportRequisitionCollection;
  }

  protected convertDateFromClient(pdfReportRequisition: IPdfReportRequisition): IPdfReportRequisition {
    return Object.assign({}, pdfReportRequisition, {
      reportDate: pdfReportRequisition.reportDate?.isValid() ? pdfReportRequisition.reportDate.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.reportDate = res.body.reportDate ? dayjs(res.body.reportDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((pdfReportRequisition: IPdfReportRequisition) => {
        pdfReportRequisition.reportDate = pdfReportRequisition.reportDate ? dayjs(pdfReportRequisition.reportDate) : undefined;
      });
    }
    return res;
  }

  protected convertPasswordFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      if (res.body.userPassword) {
        sha512(res.body.userPassword).then(token => {
          res.body!.userPassword = token.substring(0,15)
        })
      }
      if (res.body.ownerPassword) {
        sha512(res.body.ownerPassword).then(token => {
          res.body!.ownerPassword = token.substring(0,15)
        })
      }
    }
    return res;
  }

  protected convertPasswordArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((pdfReportRequisition: IPdfReportRequisition) => {
        if (pdfReportRequisition.userPassword) {
          sha512(pdfReportRequisition.userPassword).then(token => {
            pdfReportRequisition.userPassword = token.substring(0,15)
          })
        }
        if (pdfReportRequisition.ownerPassword) {
          sha512(pdfReportRequisition.ownerPassword).then(token => {
            pdfReportRequisition.ownerPassword = token.substring(0,15)
          })
        }
      });
    }
    return res;
  }
}
