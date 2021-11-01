import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SignedPaymentDetailComponent } from './signed-payment-detail.component';

describe('Component Tests', () => {
  describe('SignedPayment Management Detail Component', () => {
    let comp: SignedPaymentDetailComponent;
    let fixture: ComponentFixture<SignedPaymentDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [SignedPaymentDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ signedPayment: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(SignedPaymentDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(SignedPaymentDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load signedPayment on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.signedPayment).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
