import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IStaffCurrentEmploymentStatus, StaffCurrentEmploymentStatus } from '../staff-current-employment-status.model';
import { StaffCurrentEmploymentStatusService } from '../service/staff-current-employment-status.service';

@Injectable({ providedIn: 'root' })
export class StaffCurrentEmploymentStatusRoutingResolveService implements Resolve<IStaffCurrentEmploymentStatus> {
  constructor(protected service: StaffCurrentEmploymentStatusService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IStaffCurrentEmploymentStatus> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((staffCurrentEmploymentStatus: HttpResponse<StaffCurrentEmploymentStatus>) => {
          if (staffCurrentEmploymentStatus.body) {
            return of(staffCurrentEmploymentStatus.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new StaffCurrentEmploymentStatus());
  }
}
