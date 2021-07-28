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
