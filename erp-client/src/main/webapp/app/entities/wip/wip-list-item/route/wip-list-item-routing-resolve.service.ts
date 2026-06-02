import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IWIPListItem, WIPListItem } from '../wip-list-item.model';
import { WIPListItemService } from '../service/wip-list-item.service';

@Injectable({ providedIn: 'root' })
export class WIPListItemRoutingResolveService implements Resolve<IWIPListItem> {
  constructor(protected service: WIPListItemService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IWIPListItem> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((wIPListItem: HttpResponse<WIPListItem>) => {
          if (wIPListItem.body) {
            return of(wIPListItem.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new WIPListItem());
  }
}
