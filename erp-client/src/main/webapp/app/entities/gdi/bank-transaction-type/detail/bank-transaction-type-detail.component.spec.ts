import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { BankTransactionTypeDetailComponent } from './bank-transaction-type-detail.component';

describe('BankTransactionType Management Detail Component', () => {
  let comp: BankTransactionTypeDetailComponent;
  let fixture: ComponentFixture<BankTransactionTypeDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [BankTransactionTypeDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ bankTransactionType: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(BankTransactionTypeDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(BankTransactionTypeDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load bankTransactionType on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.bankTransactionType).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
