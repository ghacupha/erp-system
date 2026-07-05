import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICommitteeType, CommitteeType } from '../committee-type.model';
import { CommitteeTypeService } from '../service/committee-type.service';

@Injectable({ providedIn: 'root' })
export class CommitteeTypeRoutingResolveService implements Resolve<ICommitteeType> {
  constructor(protected service: CommitteeTypeService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICommitteeType> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((committeeType: HttpResponse<CommitteeType>) => {
          if (committeeType.body) {
            return of(committeeType.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new CommitteeType());
  }
}
