import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PaymentLabelDetailComponent } from './payment-label-detail.component';

describe('Component Tests', () => {
  describe('PaymentLabel Management Detail Component', () => {
    let comp: PaymentLabelDetailComponent;
    let fixture: ComponentFixture<PaymentLabelDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [PaymentLabelDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ paymentLabel: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(PaymentLabelDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(PaymentLabelDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load paymentLabel on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.paymentLabel).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
