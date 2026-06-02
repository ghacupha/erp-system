import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IShareHoldingFlag, ShareHoldingFlag } from '../share-holding-flag.model';
import { ShareHoldingFlagService } from '../service/share-holding-flag.service';

@Injectable({ providedIn: 'root' })
export class ShareHoldingFlagRoutingResolveService implements Resolve<IShareHoldingFlag> {
  constructor(protected service: ShareHoldingFlagService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IShareHoldingFlag> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((shareHoldingFlag: HttpResponse<ShareHoldingFlag>) => {
          if (shareHoldingFlag.body) {
            return of(shareHoldingFlag.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ShareHoldingFlag());
  }
}
