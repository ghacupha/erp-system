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

import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IFixedAssetAcquisition } from 'app/shared/model/assets/fixed-asset-acquisition.model';

@Component({
  selector: 'jhi-fixed-asset-acquisition-detail',
  templateUrl: './fixed-asset-acquisition-detail.component.html',
})
export class FixedAssetAcquisitionDetailComponent implements OnInit {
  fixedAssetAcquisition: IFixedAssetAcquisition | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ fixedAssetAcquisition }) => (this.fixedAssetAcquisition = fixedAssetAcquisition));
  }

  previousState(): void {
    window.history.back();
  }
}
