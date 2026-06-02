import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CreditCardFacilityDetailComponent } from './credit-card-facility-detail.component';

describe('CreditCardFacility Management Detail Component', () => {
  let comp: CreditCardFacilityDetailComponent;
  let fixture: ComponentFixture<CreditCardFacilityDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CreditCardFacilityDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ creditCardFacility: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(CreditCardFacilityDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(CreditCardFacilityDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load creditCardFacility on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.creditCardFacility).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
