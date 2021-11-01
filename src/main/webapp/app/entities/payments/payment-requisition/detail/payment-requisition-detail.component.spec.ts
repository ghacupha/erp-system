import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PaymentRequisitionDetailComponent } from './payment-requisition-detail.component';

describe('Component Tests', () => {
  describe('PaymentRequisition Management Detail Component', () => {
    let comp: PaymentRequisitionDetailComponent;
    let fixture: ComponentFixture<PaymentRequisitionDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [PaymentRequisitionDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ paymentRequisition: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(PaymentRequisitionDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(PaymentRequisitionDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load paymentRequisition on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.paymentRequisition).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
