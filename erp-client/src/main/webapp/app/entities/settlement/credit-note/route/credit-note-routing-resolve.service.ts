import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICreditNote, CreditNote } from '../credit-note.model';
import { CreditNoteService } from '../service/credit-note.service';

@Injectable({ providedIn: 'root' })
export class CreditNoteRoutingResolveService implements Resolve<ICreditNote> {
  constructor(protected service: CreditNoteService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICreditNote> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((creditNote: HttpResponse<CreditNote>) => {
          if (creditNote.body) {
            return of(creditNote.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new CreditNote());
  }
}
