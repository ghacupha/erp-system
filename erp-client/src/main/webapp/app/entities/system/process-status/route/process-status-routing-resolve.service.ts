import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IProcessStatus, ProcessStatus } from '../process-status.model';
import { ProcessStatusService } from '../service/process-status.service';

@Injectable({ providedIn: 'root' })
export class ProcessStatusRoutingResolveService implements Resolve<IProcessStatus> {
  constructor(protected service: ProcessStatusService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IProcessStatus> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((processStatus: HttpResponse<ProcessStatus>) => {
          if (processStatus.body) {
            return of(processStatus.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ProcessStatus());
  }
}
