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

import { Moment } from 'moment';
import { DepreciationRegime } from 'app/shared/model/enumerations/depreciation-regime.model';

export interface IFixedAssetNetBookValue {
  id?: number;
  assetNumber?: number;
  serviceOutletCode?: string;
  assetTag?: string;
  assetDescription?: string;
  netBookValueDate?: Moment;
  assetCategory?: string;
  netBookValue?: number;
  depreciationRegime?: DepreciationRegime;
  fileUploadToken?: string;
  compilationToken?: string;
}

export class FixedAssetNetBookValue implements IFixedAssetNetBookValue {
  constructor(
    public id?: number,
    public assetNumber?: number,
    public serviceOutletCode?: string,
    public assetTag?: string,
    public assetDescription?: string,
    public netBookValueDate?: Moment,
    public assetCategory?: string,
    public netBookValue?: number,
    public depreciationRegime?: DepreciationRegime,
    public fileUploadToken?: string,
    public compilationToken?: string
  ) {}
}
