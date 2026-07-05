import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDepreciationJobNotice, DepreciationJobNotice } from '../depreciation-job-notice.model';
import { DepreciationJobNoticeService } from '../service/depreciation-job-notice.service';

@Injectable({ providedIn: 'root' })
export class DepreciationJobNoticeRoutingResolveService implements Resolve<IDepreciationJobNotice> {
  constructor(protected service: DepreciationJobNoticeService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IDepreciationJobNotice> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((depreciationJobNotice: HttpResponse<DepreciationJobNotice>) => {
          if (depreciationJobNotice.body) {
            return of(depreciationJobNotice.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new DepreciationJobNotice());
  }
}
