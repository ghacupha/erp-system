///
/// Copyright © 2021 Edwin Njeru (mailnjeru@gmail.com)
///
/// Licensed under the Apache License, Version 2.0 (the "License");
/// you may not use this file except in compliance with the License.
/// You may obtain a copy of the License at
///
///     http://www.apache.org/licenses/LICENSE-2.0
///
/// Unless required by applicable law or agreed to in writing, software
/// distributed under the License is distributed on an "AS IS" BASIS,
/// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
/// See the License for the specific language governing permissions and
/// limitations under the License.
///

import { Component, OnInit, OnDestroy } from '@angular/core';
import { Subscription } from 'rxjs';

import { TrackerService } from 'app/core/tracker/tracker.service';
import { TrackerActivity } from 'app/core/tracker/tracker-activity.model';

@Component({
  selector: 'jhi-tracker',
  templateUrl: './tracker.component.html',
})
export class TrackerComponent implements OnInit, OnDestroy {
  activities: TrackerActivity[] = [];
  subscription?: Subscription;

  constructor(private trackerService: TrackerService) {}

  showActivity(activity: TrackerActivity): void {
    let existingActivity = false;

    for (let index = 0; index < this.activities.length; index++) {
      if (this.activities[index].sessionId === activity.sessionId) {
        existingActivity = true;
        if (activity.page === 'logout') {
          this.activities.splice(index, 1);
        } else {
          this.activities[index] = activity;
        }
      }
    }

    if (!existingActivity && activity.page !== 'logout') {
      this.activities.push(activity);
    }
  }

  ngOnInit(): void {
    this.trackerService.subscribe();
    this.subscription = this.trackerService.receive().subscribe((activity: TrackerActivity) => {
      this.showActivity(activity);
    });
  }

  ngOnDestroy(): void {
    this.trackerService.unsubscribe();
    if (this.subscription) {
      this.subscription.unsubscribe();
    }
  }
}
