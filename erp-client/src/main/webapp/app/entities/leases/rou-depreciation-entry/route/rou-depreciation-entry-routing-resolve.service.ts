import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IRouDepreciationEntry, RouDepreciationEntry } from '../rou-depreciation-entry.model';
import { RouDepreciationEntryService } from '../service/rou-depreciation-entry.service';

@Injectable({ providedIn: 'root' })
export class RouDepreciationEntryRoutingResolveService implements Resolve<IRouDepreciationEntry> {
  constructor(protected service: RouDepreciationEntryService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IRouDepreciationEntry> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((rouDepreciationEntry: HttpResponse<RouDepreciationEntry>) => {
          if (rouDepreciationEntry.body) {
            return of(rouDepreciationEntry.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new RouDepreciationEntry());
  }
}
