import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IWIPTransferListItem, WIPTransferListItem } from '../wip-transfer-list-item.model';
import { WIPTransferListItemService } from '../service/wip-transfer-list-item.service';

@Injectable({ providedIn: 'root' })
export class WIPTransferListItemRoutingResolveService implements Resolve<IWIPTransferListItem> {
  constructor(protected service: WIPTransferListItemService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IWIPTransferListItem> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((wIPTransferListItem: HttpResponse<WIPTransferListItem>) => {
          if (wIPTransferListItem.body) {
            return of(wIPTransferListItem.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new WIPTransferListItem());
  }
}
