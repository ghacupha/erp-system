import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { AccountBalanceDetailComponent } from './account-balance-detail.component';

describe('AccountBalance Management Detail Component', () => {
  let comp: AccountBalanceDetailComponent;
  let fixture: ComponentFixture<AccountBalanceDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AccountBalanceDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ accountBalance: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(AccountBalanceDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(AccountBalanceDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load accountBalance on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.accountBalance).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
