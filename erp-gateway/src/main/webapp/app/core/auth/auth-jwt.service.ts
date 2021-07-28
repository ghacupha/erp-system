import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { Login } from 'app/core/login/login.model';

export const LOGOUT_URL = SERVER_API_URL + 'auth/logout';

@Injectable({ providedIn: 'root' })
export class AuthServerProvider {
  constructor(private http: HttpClient) {}

  login(credentials: Login): Observable<any> {
    return this.http.post(SERVER_API_URL + 'auth/login', credentials);
  }

  logout(): Observable<{}> {
    return this.http.post(LOGOUT_URL, null);
  }
}
