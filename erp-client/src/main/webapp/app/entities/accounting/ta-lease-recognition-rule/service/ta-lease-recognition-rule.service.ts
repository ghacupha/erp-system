import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { ITALeaseRecognitionRule, getTALeaseRecognitionRuleIdentifier } from '../ta-lease-recognition-rule.model';

export type EntityResponseType = HttpResponse<ITALeaseRecognitionRule>;
export type EntityArrayResponseType = HttpResponse<ITALeaseRecognitionRule[]>;

@Injectable({ providedIn: 'root' })
export class TALeaseRecognitionRuleService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/ta-lease-recognition-rules');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/ta-lease-recognition-rules');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(tALeaseRecognitionRule: ITALeaseRecognitionRule): Observable<EntityResponseType> {
    return this.http.post<ITALeaseRecognitionRule>(this.resourceUrl, tALeaseRecognitionRule, { observe: 'response' });
  }

  update(tALeaseRecognitionRule: ITALeaseRecognitionRule): Observable<EntityResponseType> {
    return this.http.put<ITALeaseRecognitionRule>(
      `${this.resourceUrl}/${getTALeaseRecognitionRuleIdentifier(tALeaseRecognitionRule) as number}`,
      tALeaseRecognitionRule,
      { observe: 'response' }
    );
  }

  partialUpdate(tALeaseRecognitionRule: ITALeaseRecognitionRule): Observable<EntityResponseType> {
    return this.http.patch<ITALeaseRecognitionRule>(
      `${this.resourceUrl}/${getTALeaseRecognitionRuleIdentifier(tALeaseRecognitionRule) as number}`,
      tALeaseRecognitionRule,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITALeaseRecognitionRule>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITALeaseRecognitionRule[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITALeaseRecognitionRule[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  addTALeaseRecognitionRuleToCollectionIfMissing(
    tALeaseRecognitionRuleCollection: ITALeaseRecognitionRule[],
    ...tALeaseRecognitionRulesToCheck: (ITALeaseRecognitionRule | null | undefined)[]
  ): ITALeaseRecognitionRule[] {
    const tALeaseRecognitionRules: ITALeaseRecognitionRule[] = tALeaseRecognitionRulesToCheck.filter(isPresent);
    if (tALeaseRecognitionRules.length > 0) {
      const tALeaseRecognitionRuleCollectionIdentifiers = tALeaseRecognitionRuleCollection.map(
        tALeaseRecognitionRuleItem => getTALeaseRecognitionRuleIdentifier(tALeaseRecognitionRuleItem)!
      );
      const tALeaseRecognitionRulesToAdd = tALeaseRecognitionRules.filter(tALeaseRecognitionRuleItem => {
        const tALeaseRecognitionRuleIdentifier = getTALeaseRecognitionRuleIdentifier(tALeaseRecognitionRuleItem);
        if (
          tALeaseRecognitionRuleIdentifier == null ||
          tALeaseRecognitionRuleCollectionIdentifiers.includes(tALeaseRecognitionRuleIdentifier)
        ) {
          return false;
        }
        tALeaseRecognitionRuleCollectionIdentifiers.push(tALeaseRecognitionRuleIdentifier);
        return true;
      });
      return [...tALeaseRecognitionRulesToAdd, ...tALeaseRecognitionRuleCollection];
    }
    return tALeaseRecognitionRuleCollection;
  }
}
