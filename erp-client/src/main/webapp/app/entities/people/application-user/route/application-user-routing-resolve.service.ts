import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IApplicationUser, ApplicationUser } from '../application-user.model';
import { ApplicationUserService } from '../service/application-user.service';

@Injectable({ providedIn: 'root' })
export class ApplicationUserRoutingResolveService implements Resolve<IApplicationUser> {
  constructor(protected service: ApplicationUserService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IApplicationUser> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((applicationUser: HttpResponse<ApplicationUser>) => {
          if (applicationUser.body) {
            return of(applicationUser.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ApplicationUser());
  }
}
