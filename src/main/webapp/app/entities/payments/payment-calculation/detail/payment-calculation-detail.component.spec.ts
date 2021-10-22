import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PaymentCalculationDetailComponent } from './payment-calculation-detail.component';

describe('Component Tests', () => {
  describe('PaymentCalculation Management Detail Component', () => {
    let comp: PaymentCalculationDetailComponent;
    let fixture: ComponentFixture<PaymentCalculationDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [PaymentCalculationDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ paymentCalculation: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(PaymentCalculationDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(PaymentCalculationDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load paymentCalculation on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.paymentCalculation).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
