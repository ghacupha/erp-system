import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CrbAgingBandsDetailComponent } from './crb-aging-bands-detail.component';

describe('CrbAgingBands Management Detail Component', () => {
  let comp: CrbAgingBandsDetailComponent;
  let fixture: ComponentFixture<CrbAgingBandsDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CrbAgingBandsDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ crbAgingBands: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(CrbAgingBandsDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(CrbAgingBandsDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load crbAgingBands on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.crbAgingBands).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
