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
import { ITaxRule, getTaxRuleIdentifier } from '../tax-rule.model';

export type EntityResponseType = HttpResponse<ITaxRule>;
export type EntityArrayResponseType = HttpResponse<ITaxRule[]>;

@Injectable({ providedIn: 'root' })
export class TaxRuleService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/tax-rules');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/tax-rules');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(taxRule: ITaxRule): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(taxRule);
    return this.http
      .post<ITaxRule>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(taxRule: ITaxRule): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(taxRule);
    return this.http
      .put<ITaxRule>(`${this.resourceUrl}/${getTaxRuleIdentifier(taxRule) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(taxRule: ITaxRule): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(taxRule);
    return this.http
      .patch<ITaxRule>(`${this.resourceUrl}/${getTaxRuleIdentifier(taxRule) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ITaxRule>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ITaxRule[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ITaxRule[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  addTaxRuleToCollectionIfMissing(taxRuleCollection: ITaxRule[], ...taxRulesToCheck: (ITaxRule | null | undefined)[]): ITaxRule[] {
    const taxRules: ITaxRule[] = taxRulesToCheck.filter(isPresent);
    if (taxRules.length > 0) {
      const taxRuleCollectionIdentifiers = taxRuleCollection.map(taxRuleItem => getTaxRuleIdentifier(taxRuleItem)!);
      const taxRulesToAdd = taxRules.filter(taxRuleItem => {
        const taxRuleIdentifier = getTaxRuleIdentifier(taxRuleItem);
        if (taxRuleIdentifier == null || taxRuleCollectionIdentifiers.includes(taxRuleIdentifier)) {
          return false;
        }
        taxRuleCollectionIdentifiers.push(taxRuleIdentifier);
        return true;
      });
      return [...taxRulesToAdd, ...taxRuleCollection];
    }
    return taxRuleCollection;
  }

  protected convertDateFromClient(taxRule: ITaxRule): ITaxRule {
    return Object.assign({}, taxRule, {
      paymentDate: taxRule.paymentDate?.isValid() ? taxRule.paymentDate.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.paymentDate = res.body.paymentDate ? dayjs(res.body.paymentDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((taxRule: ITaxRule) => {
        taxRule.paymentDate = taxRule.paymentDate ? dayjs(taxRule.paymentDate) : undefined;
      });
    }
    return res;
  }
}
