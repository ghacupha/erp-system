import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption, SearchWithPagination } from 'app/shared/util/request-util';
import { ITaxReference } from 'app/shared/model/payments/tax-reference.model';

type EntityResponseType = HttpResponse<ITaxReference>;
type EntityArrayResponseType = HttpResponse<ITaxReference[]>;

@Injectable({ providedIn: 'root' })
export class TaxReferenceService {
  public resourceUrl = SERVER_API_URL + 'services/erpservice/api/tax-references';
  public resourceSearchUrl = SERVER_API_URL + 'services/erpservice/api/_search/tax-references';

  constructor(protected http: HttpClient) {}

  create(taxReference: ITaxReference): Observable<EntityResponseType> {
    return this.http.post<ITaxReference>(this.resourceUrl, taxReference, { observe: 'response' });
  }

  update(taxReference: ITaxReference): Observable<EntityResponseType> {
    return this.http.put<ITaxReference>(this.resourceUrl, taxReference, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITaxReference>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITaxReference[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITaxReference[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }
}
