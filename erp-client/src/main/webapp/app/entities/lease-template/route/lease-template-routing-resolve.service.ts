import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ILeaseTemplate, LeaseTemplate } from '../lease-template.model';
import { LeaseTemplateService } from '../service/lease-template.service';

@Injectable({ providedIn: 'root' })
export class LeaseTemplateRoutingResolveService implements Resolve<ILeaseTemplate> {
  constructor(protected service: LeaseTemplateService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ILeaseTemplate> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((leaseTemplate: HttpResponse<LeaseTemplate>) => {
          if (leaseTemplate.body) {
            return of(leaseTemplate.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new LeaseTemplate());
  }
}
