import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IChannelType, getChannelTypeIdentifier } from '../channel-type.model';

export type EntityResponseType = HttpResponse<IChannelType>;
export type EntityArrayResponseType = HttpResponse<IChannelType[]>;

@Injectable({ providedIn: 'root' })
export class ChannelTypeService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/channel-types');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/channel-types');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(channelType: IChannelType): Observable<EntityResponseType> {
    return this.http.post<IChannelType>(this.resourceUrl, channelType, { observe: 'response' });
  }

  update(channelType: IChannelType): Observable<EntityResponseType> {
    return this.http.put<IChannelType>(`${this.resourceUrl}/${getChannelTypeIdentifier(channelType) as number}`, channelType, {
      observe: 'response',
    });
  }

  partialUpdate(channelType: IChannelType): Observable<EntityResponseType> {
    return this.http.patch<IChannelType>(`${this.resourceUrl}/${getChannelTypeIdentifier(channelType) as number}`, channelType, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IChannelType>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IChannelType[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IChannelType[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  addChannelTypeToCollectionIfMissing(
    channelTypeCollection: IChannelType[],
    ...channelTypesToCheck: (IChannelType | null | undefined)[]
  ): IChannelType[] {
    const channelTypes: IChannelType[] = channelTypesToCheck.filter(isPresent);
    if (channelTypes.length > 0) {
      const channelTypeCollectionIdentifiers = channelTypeCollection.map(channelTypeItem => getChannelTypeIdentifier(channelTypeItem)!);
      const channelTypesToAdd = channelTypes.filter(channelTypeItem => {
        const channelTypeIdentifier = getChannelTypeIdentifier(channelTypeItem);
        if (channelTypeIdentifier == null || channelTypeCollectionIdentifiers.includes(channelTypeIdentifier)) {
          return false;
        }
        channelTypeCollectionIdentifiers.push(channelTypeIdentifier);
        return true;
      });
      return [...channelTypesToAdd, ...channelTypeCollection];
    }
    return channelTypeCollection;
  }
}
