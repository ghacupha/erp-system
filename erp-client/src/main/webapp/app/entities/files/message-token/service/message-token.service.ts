import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IMessageToken, getMessageTokenIdentifier } from '../message-token.model';

export type EntityResponseType = HttpResponse<IMessageToken>;
export type EntityArrayResponseType = HttpResponse<IMessageToken[]>;

@Injectable({ providedIn: 'root' })
export class MessageTokenService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/message-tokens');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(messageToken: IMessageToken): Observable<EntityResponseType> {
    return this.http.post<IMessageToken>(this.resourceUrl, messageToken, { observe: 'response' });
  }

  update(messageToken: IMessageToken): Observable<EntityResponseType> {
    return this.http.put<IMessageToken>(`${this.resourceUrl}/${getMessageTokenIdentifier(messageToken) as number}`, messageToken, {
      observe: 'response',
    });
  }

  partialUpdate(messageToken: IMessageToken): Observable<EntityResponseType> {
    return this.http.patch<IMessageToken>(`${this.resourceUrl}/${getMessageTokenIdentifier(messageToken) as number}`, messageToken, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IMessageToken>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IMessageToken[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addMessageTokenToCollectionIfMissing(
    messageTokenCollection: IMessageToken[],
    ...messageTokensToCheck: (IMessageToken | null | undefined)[]
  ): IMessageToken[] {
    const messageTokens: IMessageToken[] = messageTokensToCheck.filter(isPresent);
    if (messageTokens.length > 0) {
      const messageTokenCollectionIdentifiers = messageTokenCollection.map(
        messageTokenItem => getMessageTokenIdentifier(messageTokenItem)!
      );
      const messageTokensToAdd = messageTokens.filter(messageTokenItem => {
        const messageTokenIdentifier = getMessageTokenIdentifier(messageTokenItem);
        if (messageTokenIdentifier == null || messageTokenCollectionIdentifiers.includes(messageTokenIdentifier)) {
          return false;
        }
        messageTokenCollectionIdentifiers.push(messageTokenIdentifier);
        return true;
      });
      return [...messageTokensToAdd, ...messageTokenCollection];
    }
    return messageTokenCollection;
  }
}
