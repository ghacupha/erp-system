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
import { IWorkInProgressTransfer, getWorkInProgressTransferIdentifier } from '../work-in-progress-transfer.model';

export type EntityResponseType = HttpResponse<IWorkInProgressTransfer>;
export type EntityArrayResponseType = HttpResponse<IWorkInProgressTransfer[]>;

@Injectable({ providedIn: 'root' })
export class WorkInProgressTransferService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/work-in-progress-transfers');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/work-in-progress-transfers');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(workInProgressTransfer: IWorkInProgressTransfer): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(workInProgressTransfer);
    return this.http
      .post<IWorkInProgressTransfer>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(workInProgressTransfer: IWorkInProgressTransfer): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(workInProgressTransfer);
    return this.http
      .put<IWorkInProgressTransfer>(`${this.resourceUrl}/${getWorkInProgressTransferIdentifier(workInProgressTransfer) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(workInProgressTransfer: IWorkInProgressTransfer): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(workInProgressTransfer);
    return this.http
      .patch<IWorkInProgressTransfer>(
        `${this.resourceUrl}/${getWorkInProgressTransferIdentifier(workInProgressTransfer) as number}`,
        copy,
        { observe: 'response' }
      )
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IWorkInProgressTransfer>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IWorkInProgressTransfer[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IWorkInProgressTransfer[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  addWorkInProgressTransferToCollectionIfMissing(
    workInProgressTransferCollection: IWorkInProgressTransfer[],
    ...workInProgressTransfersToCheck: (IWorkInProgressTransfer | null | undefined)[]
  ): IWorkInProgressTransfer[] {
    const workInProgressTransfers: IWorkInProgressTransfer[] = workInProgressTransfersToCheck.filter(isPresent);
    if (workInProgressTransfers.length > 0) {
      const workInProgressTransferCollectionIdentifiers = workInProgressTransferCollection.map(
        workInProgressTransferItem => getWorkInProgressTransferIdentifier(workInProgressTransferItem)!
      );
      const workInProgressTransfersToAdd = workInProgressTransfers.filter(workInProgressTransferItem => {
        const workInProgressTransferIdentifier = getWorkInProgressTransferIdentifier(workInProgressTransferItem);
        if (
          workInProgressTransferIdentifier == null ||
          workInProgressTransferCollectionIdentifiers.includes(workInProgressTransferIdentifier)
        ) {
          return false;
        }
        workInProgressTransferCollectionIdentifiers.push(workInProgressTransferIdentifier);
        return true;
      });
      return [...workInProgressTransfersToAdd, ...workInProgressTransferCollection];
    }
    return workInProgressTransferCollection;
  }

  protected convertDateFromClient(workInProgressTransfer: IWorkInProgressTransfer): IWorkInProgressTransfer {
    return Object.assign({}, workInProgressTransfer, {
      transferDate: workInProgressTransfer.transferDate?.isValid() ? workInProgressTransfer.transferDate.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.transferDate = res.body.transferDate ? dayjs(res.body.transferDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((workInProgressTransfer: IWorkInProgressTransfer) => {
        workInProgressTransfer.transferDate = workInProgressTransfer.transferDate ? dayjs(workInProgressTransfer.transferDate) : undefined;
      });
    }
    return res;
  }
}
