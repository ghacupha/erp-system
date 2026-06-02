import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { INetBookValueEntry, NetBookValueEntry } from '../net-book-value-entry.model';
import { NetBookValueEntryService } from '../service/net-book-value-entry.service';

@Injectable({ providedIn: 'root' })
export class NetBookValueEntryRoutingResolveService implements Resolve<INetBookValueEntry> {
  constructor(protected service: NetBookValueEntryService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<INetBookValueEntry> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((netBookValueEntry: HttpResponse<NetBookValueEntry>) => {
          if (netBookValueEntry.body) {
            return of(netBookValueEntry.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new NetBookValueEntry());
  }
}
