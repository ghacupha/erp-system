import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDeliveryNote, DeliveryNote } from '../delivery-note.model';
import { DeliveryNoteService } from '../service/delivery-note.service';

@Injectable({ providedIn: 'root' })
export class DeliveryNoteRoutingResolveService implements Resolve<IDeliveryNote> {
  constructor(protected service: DeliveryNoteService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IDeliveryNote> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((deliveryNote: HttpResponse<DeliveryNote>) => {
          if (deliveryNote.body) {
            return of(deliveryNote.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new DeliveryNote());
  }
}
