import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISystemModule, SystemModule } from '../system-module.model';
import { SystemModuleService } from '../service/system-module.service';

@Injectable({ providedIn: 'root' })
export class SystemModuleRoutingResolveService implements Resolve<ISystemModule> {
  constructor(protected service: SystemModuleService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISystemModule> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((systemModule: HttpResponse<SystemModule>) => {
          if (systemModule.body) {
            return of(systemModule.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new SystemModule());
  }
}
