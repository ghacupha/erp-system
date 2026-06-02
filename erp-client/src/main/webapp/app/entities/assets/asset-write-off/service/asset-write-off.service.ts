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
import { IAssetWriteOff, getAssetWriteOffIdentifier } from '../asset-write-off.model';

export type EntityResponseType = HttpResponse<IAssetWriteOff>;
export type EntityArrayResponseType = HttpResponse<IAssetWriteOff[]>;

@Injectable({ providedIn: 'root' })
export class AssetWriteOffService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/asset-write-offs');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/asset-write-offs');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(assetWriteOff: IAssetWriteOff): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(assetWriteOff);
    return this.http
      .post<IAssetWriteOff>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(assetWriteOff: IAssetWriteOff): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(assetWriteOff);
    return this.http
      .put<IAssetWriteOff>(`${this.resourceUrl}/${getAssetWriteOffIdentifier(assetWriteOff) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(assetWriteOff: IAssetWriteOff): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(assetWriteOff);
    return this.http
      .patch<IAssetWriteOff>(`${this.resourceUrl}/${getAssetWriteOffIdentifier(assetWriteOff) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IAssetWriteOff>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IAssetWriteOff[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IAssetWriteOff[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  addAssetWriteOffToCollectionIfMissing(
    assetWriteOffCollection: IAssetWriteOff[],
    ...assetWriteOffsToCheck: (IAssetWriteOff | null | undefined)[]
  ): IAssetWriteOff[] {
    const assetWriteOffs: IAssetWriteOff[] = assetWriteOffsToCheck.filter(isPresent);
    if (assetWriteOffs.length > 0) {
      const assetWriteOffCollectionIdentifiers = assetWriteOffCollection.map(
        assetWriteOffItem => getAssetWriteOffIdentifier(assetWriteOffItem)!
      );
      const assetWriteOffsToAdd = assetWriteOffs.filter(assetWriteOffItem => {
        const assetWriteOffIdentifier = getAssetWriteOffIdentifier(assetWriteOffItem);
        if (assetWriteOffIdentifier == null || assetWriteOffCollectionIdentifiers.includes(assetWriteOffIdentifier)) {
          return false;
        }
        assetWriteOffCollectionIdentifiers.push(assetWriteOffIdentifier);
        return true;
      });
      return [...assetWriteOffsToAdd, ...assetWriteOffCollection];
    }
    return assetWriteOffCollection;
  }

  protected convertDateFromClient(assetWriteOff: IAssetWriteOff): IAssetWriteOff {
    return Object.assign({}, assetWriteOff, {
      writeOffDate: assetWriteOff.writeOffDate?.isValid() ? assetWriteOff.writeOffDate.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.writeOffDate = res.body.writeOffDate ? dayjs(res.body.writeOffDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((assetWriteOff: IAssetWriteOff) => {
        assetWriteOff.writeOffDate = assetWriteOff.writeOffDate ? dayjs(assetWriteOff.writeOffDate) : undefined;
      });
    }
    return res;
  }
}
