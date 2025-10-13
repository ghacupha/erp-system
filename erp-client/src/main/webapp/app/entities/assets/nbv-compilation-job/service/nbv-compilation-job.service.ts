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
import { INbvCompilationJob, getNbvCompilationJobIdentifier } from '../nbv-compilation-job.model';

export type EntityResponseType = HttpResponse<INbvCompilationJob>;
export type EntityArrayResponseType = HttpResponse<INbvCompilationJob[]>;

@Injectable({ providedIn: 'root' })
export class NbvCompilationJobService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/nbv-compilation-jobs');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/nbv-compilation-jobs');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(nbvCompilationJob: INbvCompilationJob): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(nbvCompilationJob);
    return this.http
      .post<INbvCompilationJob>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(nbvCompilationJob: INbvCompilationJob): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(nbvCompilationJob);
    return this.http
      .put<INbvCompilationJob>(`${this.resourceUrl}/${getNbvCompilationJobIdentifier(nbvCompilationJob) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(nbvCompilationJob: INbvCompilationJob): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(nbvCompilationJob);
    return this.http
      .patch<INbvCompilationJob>(`${this.resourceUrl}/${getNbvCompilationJobIdentifier(nbvCompilationJob) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<INbvCompilationJob>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<INbvCompilationJob[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<INbvCompilationJob[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  addNbvCompilationJobToCollectionIfMissing(
    nbvCompilationJobCollection: INbvCompilationJob[],
    ...nbvCompilationJobsToCheck: (INbvCompilationJob | null | undefined)[]
  ): INbvCompilationJob[] {
    const nbvCompilationJobs: INbvCompilationJob[] = nbvCompilationJobsToCheck.filter(isPresent);
    if (nbvCompilationJobs.length > 0) {
      const nbvCompilationJobCollectionIdentifiers = nbvCompilationJobCollection.map(
        nbvCompilationJobItem => getNbvCompilationJobIdentifier(nbvCompilationJobItem)!
      );
      const nbvCompilationJobsToAdd = nbvCompilationJobs.filter(nbvCompilationJobItem => {
        const nbvCompilationJobIdentifier = getNbvCompilationJobIdentifier(nbvCompilationJobItem);
        if (nbvCompilationJobIdentifier == null || nbvCompilationJobCollectionIdentifiers.includes(nbvCompilationJobIdentifier)) {
          return false;
        }
        nbvCompilationJobCollectionIdentifiers.push(nbvCompilationJobIdentifier);
        return true;
      });
      return [...nbvCompilationJobsToAdd, ...nbvCompilationJobCollection];
    }
    return nbvCompilationJobCollection;
  }

  protected convertDateFromClient(nbvCompilationJob: INbvCompilationJob): INbvCompilationJob {
    return Object.assign({}, nbvCompilationJob, {
      compilationJobTimeOfRequest: nbvCompilationJob.compilationJobTimeOfRequest?.isValid()
        ? nbvCompilationJob.compilationJobTimeOfRequest.toJSON()
        : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.compilationJobTimeOfRequest = res.body.compilationJobTimeOfRequest ? dayjs(res.body.compilationJobTimeOfRequest) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((nbvCompilationJob: INbvCompilationJob) => {
        nbvCompilationJob.compilationJobTimeOfRequest = nbvCompilationJob.compilationJobTimeOfRequest
          ? dayjs(nbvCompilationJob.compilationJobTimeOfRequest)
          : undefined;
      });
    }
    return res;
  }
}
