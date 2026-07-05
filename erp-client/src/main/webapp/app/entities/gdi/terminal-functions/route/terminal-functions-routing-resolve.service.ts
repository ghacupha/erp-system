import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITerminalFunctions, TerminalFunctions } from '../terminal-functions.model';
import { TerminalFunctionsService } from '../service/terminal-functions.service';

@Injectable({ providedIn: 'root' })
export class TerminalFunctionsRoutingResolveService implements Resolve<ITerminalFunctions> {
  constructor(protected service: TerminalFunctionsService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ITerminalFunctions> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((terminalFunctions: HttpResponse<TerminalFunctions>) => {
          if (terminalFunctions.body) {
            return of(terminalFunctions.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new TerminalFunctions());
  }
}
