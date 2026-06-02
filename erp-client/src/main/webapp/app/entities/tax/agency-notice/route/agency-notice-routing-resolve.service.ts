import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAgencyNotice, AgencyNotice } from '../agency-notice.model';
import { AgencyNoticeService } from '../service/agency-notice.service';

@Injectable({ providedIn: 'root' })
export class AgencyNoticeRoutingResolveService implements Resolve<IAgencyNotice> {
  constructor(protected service: AgencyNoticeService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IAgencyNotice> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((agencyNotice: HttpResponse<AgencyNotice>) => {
          if (agencyNotice.body) {
            return of(agencyNotice.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new AgencyNotice());
  }
}
