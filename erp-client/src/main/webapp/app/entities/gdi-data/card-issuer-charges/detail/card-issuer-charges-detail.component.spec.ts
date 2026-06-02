import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CardIssuerChargesDetailComponent } from './card-issuer-charges-detail.component';

describe('CardIssuerCharges Management Detail Component', () => {
  let comp: CardIssuerChargesDetailComponent;
  let fixture: ComponentFixture<CardIssuerChargesDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CardIssuerChargesDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ cardIssuerCharges: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(CardIssuerChargesDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(CardIssuerChargesDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load cardIssuerCharges on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.cardIssuerCharges).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
