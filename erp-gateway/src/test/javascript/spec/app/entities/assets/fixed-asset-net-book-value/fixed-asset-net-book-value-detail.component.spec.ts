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

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ErpGatewayTestModule } from '../../../../test.module';
import { FixedAssetNetBookValueDetailComponent } from 'app/entities/assets/fixed-asset-net-book-value/fixed-asset-net-book-value-detail.component';
import { FixedAssetNetBookValue } from 'app/shared/model/assets/fixed-asset-net-book-value.model';

describe('Component Tests', () => {
  describe('FixedAssetNetBookValue Management Detail Component', () => {
    let comp: FixedAssetNetBookValueDetailComponent;
    let fixture: ComponentFixture<FixedAssetNetBookValueDetailComponent>;
    const route = ({ data: of({ fixedAssetNetBookValue: new FixedAssetNetBookValue(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [ErpGatewayTestModule],
        declarations: [FixedAssetNetBookValueDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(FixedAssetNetBookValueDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(FixedAssetNetBookValueDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load fixedAssetNetBookValue on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.fixedAssetNetBookValue).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
