import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CrbProductServiceFeeTypeDetailComponent } from './crb-product-service-fee-type-detail.component';

describe('CrbProductServiceFeeType Management Detail Component', () => {
  let comp: CrbProductServiceFeeTypeDetailComponent;
  let fixture: ComponentFixture<CrbProductServiceFeeTypeDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CrbProductServiceFeeTypeDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ crbProductServiceFeeType: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(CrbProductServiceFeeTypeDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(CrbProductServiceFeeTypeDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load crbProductServiceFeeType on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.crbProductServiceFeeType).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
