///
/// Erp System - Mark X No 10 (Jehoiada Series) Client 1.7.8
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
