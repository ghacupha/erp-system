import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IMessageToken, MessageToken } from '../message-token.model';
import { MessageTokenService } from '../service/message-token.service';

@Injectable({ providedIn: 'root' })
export class MessageTokenRoutingResolveService implements Resolve<IMessageToken> {
  constructor(protected service: MessageTokenService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IMessageToken> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((messageToken: HttpResponse<MessageToken>) => {
          if (messageToken.body) {
            return of(messageToken.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new MessageToken());
  }
}
