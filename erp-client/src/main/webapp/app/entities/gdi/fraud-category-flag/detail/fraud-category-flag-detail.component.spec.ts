import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { FraudCategoryFlagDetailComponent } from './fraud-category-flag-detail.component';

describe('FraudCategoryFlag Management Detail Component', () => {
  let comp: FraudCategoryFlagDetailComponent;
  let fixture: ComponentFixture<FraudCategoryFlagDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [FraudCategoryFlagDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ fraudCategoryFlag: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(FraudCategoryFlagDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(FraudCategoryFlagDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load fraudCategoryFlag on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.fraudCategoryFlag).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
