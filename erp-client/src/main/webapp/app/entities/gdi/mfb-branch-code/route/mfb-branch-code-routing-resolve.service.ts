import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IMfbBranchCode, MfbBranchCode } from '../mfb-branch-code.model';
import { MfbBranchCodeService } from '../service/mfb-branch-code.service';

@Injectable({ providedIn: 'root' })
export class MfbBranchCodeRoutingResolveService implements Resolve<IMfbBranchCode> {
  constructor(protected service: MfbBranchCodeService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IMfbBranchCode> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((mfbBranchCode: HttpResponse<MfbBranchCode>) => {
          if (mfbBranchCode.body) {
            return of(mfbBranchCode.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new MfbBranchCode());
  }
}
