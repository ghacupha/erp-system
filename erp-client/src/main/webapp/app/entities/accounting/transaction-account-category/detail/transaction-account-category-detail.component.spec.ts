import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TransactionAccountCategoryDetailComponent } from './transaction-account-category-detail.component';

describe('TransactionAccountCategory Management Detail Component', () => {
  let comp: TransactionAccountCategoryDetailComponent;
  let fixture: ComponentFixture<TransactionAccountCategoryDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [TransactionAccountCategoryDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ transactionAccountCategory: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(TransactionAccountCategoryDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(TransactionAccountCategoryDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load transactionAccountCategory on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.transactionAccountCategory).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
