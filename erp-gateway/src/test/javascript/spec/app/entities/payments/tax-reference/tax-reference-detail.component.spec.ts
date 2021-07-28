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
import { TaxReferenceDetailComponent } from 'app/entities/payments/tax-reference/tax-reference-detail.component';
import { TaxReference } from 'app/shared/model/payments/tax-reference.model';

describe('Component Tests', () => {
  describe('TaxReference Management Detail Component', () => {
    let comp: TaxReferenceDetailComponent;
    let fixture: ComponentFixture<TaxReferenceDetailComponent>;
    const route = ({ data: of({ taxReference: new TaxReference(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [ErpGatewayTestModule],
        declarations: [TaxReferenceDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(TaxReferenceDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(TaxReferenceDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load taxReference on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.taxReference).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
