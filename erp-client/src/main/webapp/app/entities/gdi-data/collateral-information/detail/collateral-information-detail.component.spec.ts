import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CollateralInformationDetailComponent } from './collateral-information-detail.component';

describe('CollateralInformation Management Detail Component', () => {
  let comp: CollateralInformationDetailComponent;
  let fixture: ComponentFixture<CollateralInformationDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CollateralInformationDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ collateralInformation: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(CollateralInformationDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(CollateralInformationDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load collateralInformation on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.collateralInformation).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
