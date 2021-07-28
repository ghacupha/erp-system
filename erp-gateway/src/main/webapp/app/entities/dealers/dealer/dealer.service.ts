import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption, SearchWithPagination } from 'app/shared/util/request-util';
import { IDealer } from 'app/shared/model/dealers/dealer.model';

type EntityResponseType = HttpResponse<IDealer>;
type EntityArrayResponseType = HttpResponse<IDealer[]>;

@Injectable({ providedIn: 'root' })
export class DealerService {
  public resourceUrl = SERVER_API_URL + 'services/erpservice/api/dealers';
  public resourceSearchUrl = SERVER_API_URL + 'services/erpservice/api/_search/dealers';

  constructor(protected http: HttpClient) {}

  create(dealer: IDealer): Observable<EntityResponseType> {
    return this.http.post<IDealer>(this.resourceUrl, dealer, { observe: 'response' });
  }

  update(dealer: IDealer): Observable<EntityResponseType> {
    return this.http.put<IDealer>(this.resourceUrl, dealer, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IDealer>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IDealer[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IDealer[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }
}
