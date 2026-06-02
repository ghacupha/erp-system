import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDepreciationEntry, DepreciationEntry } from '../depreciation-entry.model';
import { DepreciationEntryService } from '../service/depreciation-entry.service';

@Injectable({ providedIn: 'root' })
export class DepreciationEntryRoutingResolveService implements Resolve<IDepreciationEntry> {
  constructor(protected service: DepreciationEntryService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IDepreciationEntry> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((depreciationEntry: HttpResponse<DepreciationEntry>) => {
          if (depreciationEntry.body) {
            return of(depreciationEntry.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new DepreciationEntry());
  }
}
