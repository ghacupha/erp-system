import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITerminalTypes, TerminalTypes } from '../terminal-types.model';
import { TerminalTypesService } from '../service/terminal-types.service';

@Injectable({ providedIn: 'root' })
export class TerminalTypesRoutingResolveService implements Resolve<ITerminalTypes> {
  constructor(protected service: TerminalTypesService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ITerminalTypes> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((terminalTypes: HttpResponse<TerminalTypes>) => {
          if (terminalTypes.body) {
            return of(terminalTypes.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new TerminalTypes());
  }
}
