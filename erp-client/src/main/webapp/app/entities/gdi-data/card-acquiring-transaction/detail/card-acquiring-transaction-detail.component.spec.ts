import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CardAcquiringTransactionDetailComponent } from './card-acquiring-transaction-detail.component';

describe('CardAcquiringTransaction Management Detail Component', () => {
  let comp: CardAcquiringTransactionDetailComponent;
  let fixture: ComponentFixture<CardAcquiringTransactionDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CardAcquiringTransactionDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ cardAcquiringTransaction: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(CardAcquiringTransactionDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(CardAcquiringTransactionDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load cardAcquiringTransaction on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.cardAcquiringTransaction).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
