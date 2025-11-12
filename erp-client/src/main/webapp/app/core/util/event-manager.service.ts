///
/// Erp System - Mark X No 11 (Jehoiada Series) Client 1.7.8
/// Copyright © 2021 - 2024 Edwin Njeru (mailnjeru@gmail.com)
///
/// This program is free software: you can redistribute it and/or modify
/// it under the terms of the GNU General Public License as published by
/// the Free Software Foundation, either version 3 of the License, or
/// (at your option) any later version.
///
/// This program is distributed in the hope that it will be useful,
/// but WITHOUT ANY WARRANTY; without even the implied warranty of
/// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
/// GNU General Public License for more details.
///
/// You should have received a copy of the GNU General Public License
/// along with this program. If not, see <http://www.gnu.org/licenses/>.
///

import { Injectable } from '@angular/core';
import { Observable, Observer, Subscription } from 'rxjs';
import { filter, share } from 'rxjs/operators';

export class EventWithContent<T> {
  constructor(public name: string, public content: T) {}
}

/**
 * An utility class to manage RX events
 */
@Injectable({
  providedIn: 'root',
})
export class EventManager {
  observable: Observable<EventWithContent<unknown> | string>;
  observer?: Observer<EventWithContent<unknown> | string>;

  constructor() {
    this.observable = new Observable((observer: Observer<EventWithContent<unknown> | string>) => {
      this.observer = observer;
    }).pipe(share());
  }

  /**
   * Method to broadcast the event to observer
   */
  broadcast(event: EventWithContent<unknown> | string): void {
    if (this.observer) {
      this.observer.next(event);
    }
  }

  /**
   * Method to subscribe to an event with callback
   * @param eventNames  Single event name or array of event names to what subscribe
   * @param callback    Callback to run when the event occurs
   */
  subscribe(eventNames: string | string[], callback: (event: EventWithContent<unknown> | string) => void): Subscription {
    if (typeof eventNames === 'string') {
      eventNames = [eventNames];
    }
    return this.observable
      .pipe(
        filter((event: EventWithContent<unknown> | string) => {
          for (const eventName of eventNames) {
            if ((typeof event === 'string' && event === eventName) || (typeof event !== 'string' && event.name === eventName)) {
              return true;
            }
          }
          return false;
        })
      )
      .subscribe(callback);
  }

  /**
   * Method to unsubscribe the subscription
   */
  destroy(subscriber: Subscription): void {
    subscriber.unsubscribe();
  }
}
