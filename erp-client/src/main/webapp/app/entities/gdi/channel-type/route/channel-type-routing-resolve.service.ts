import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IChannelType, ChannelType } from '../channel-type.model';
import { ChannelTypeService } from '../service/channel-type.service';

@Injectable({ providedIn: 'root' })
export class ChannelTypeRoutingResolveService implements Resolve<IChannelType> {
  constructor(protected service: ChannelTypeService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IChannelType> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((channelType: HttpResponse<ChannelType>) => {
          if (channelType.body) {
            return of(channelType.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ChannelType());
  }
}
