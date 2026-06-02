import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IDepreciationJob, getDepreciationJobIdentifier } from '../depreciation-job.model';

export type EntityResponseType = HttpResponse<IDepreciationJob>;
export type EntityArrayResponseType = HttpResponse<IDepreciationJob[]>;

@Injectable({ providedIn: 'root' })
export class DepreciationJobService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/depreciation-jobs');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/depreciation-jobs');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(depreciationJob: IDepreciationJob): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(depreciationJob);
    return this.http
      .post<IDepreciationJob>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(depreciationJob: IDepreciationJob): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(depreciationJob);
    return this.http
      .put<IDepreciationJob>(`${this.resourceUrl}/${getDepreciationJobIdentifier(depreciationJob) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(depreciationJob: IDepreciationJob): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(depreciationJob);
    return this.http
      .patch<IDepreciationJob>(`${this.resourceUrl}/${getDepreciationJobIdentifier(depreciationJob) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IDepreciationJob>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IDepreciationJob[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IDepreciationJob[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  addDepreciationJobToCollectionIfMissing(
    depreciationJobCollection: IDepreciationJob[],
    ...depreciationJobsToCheck: (IDepreciationJob | null | undefined)[]
  ): IDepreciationJob[] {
    const depreciationJobs: IDepreciationJob[] = depreciationJobsToCheck.filter(isPresent);
    if (depreciationJobs.length > 0) {
      const depreciationJobCollectionIdentifiers = depreciationJobCollection.map(
        depreciationJobItem => getDepreciationJobIdentifier(depreciationJobItem)!
      );
      const depreciationJobsToAdd = depreciationJobs.filter(depreciationJobItem => {
        const depreciationJobIdentifier = getDepreciationJobIdentifier(depreciationJobItem);
        if (depreciationJobIdentifier == null || depreciationJobCollectionIdentifiers.includes(depreciationJobIdentifier)) {
          return false;
        }
        depreciationJobCollectionIdentifiers.push(depreciationJobIdentifier);
        return true;
      });
      return [...depreciationJobsToAdd, ...depreciationJobCollection];
    }
    return depreciationJobCollection;
  }

  protected convertDateFromClient(depreciationJob: IDepreciationJob): IDepreciationJob {
    return Object.assign({}, depreciationJob, {
      timeOfCommencement: depreciationJob.timeOfCommencement?.isValid() ? depreciationJob.timeOfCommencement.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.timeOfCommencement = res.body.timeOfCommencement ? dayjs(res.body.timeOfCommencement) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((depreciationJob: IDepreciationJob) => {
        depreciationJob.timeOfCommencement = depreciationJob.timeOfCommencement ? dayjs(depreciationJob.timeOfCommencement) : undefined;
      });
    }
    return res;
  }
}
