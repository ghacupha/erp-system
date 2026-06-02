import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { AccountAttributeDetailComponent } from './account-attribute-detail.component';

describe('AccountAttribute Management Detail Component', () => {
  let comp: AccountAttributeDetailComponent;
  let fixture: ComponentFixture<AccountAttributeDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AccountAttributeDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ accountAttribute: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(AccountAttributeDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(AccountAttributeDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load accountAttribute on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.accountAttribute).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
