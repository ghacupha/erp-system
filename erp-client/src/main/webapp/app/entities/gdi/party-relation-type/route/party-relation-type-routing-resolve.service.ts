import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPartyRelationType, PartyRelationType } from '../party-relation-type.model';
import { PartyRelationTypeService } from '../service/party-relation-type.service';

@Injectable({ providedIn: 'root' })
export class PartyRelationTypeRoutingResolveService implements Resolve<IPartyRelationType> {
  constructor(protected service: PartyRelationTypeService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPartyRelationType> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((partyRelationType: HttpResponse<PartyRelationType>) => {
          if (partyRelationType.body) {
            return of(partyRelationType.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new PartyRelationType());
  }
}
