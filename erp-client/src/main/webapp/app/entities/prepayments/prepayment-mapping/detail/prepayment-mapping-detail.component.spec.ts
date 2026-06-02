import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PrepaymentMappingDetailComponent } from './prepayment-mapping-detail.component';

describe('PrepaymentMapping Management Detail Component', () => {
  let comp: PrepaymentMappingDetailComponent;
  let fixture: ComponentFixture<PrepaymentMappingDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PrepaymentMappingDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ prepaymentMapping: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(PrepaymentMappingDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(PrepaymentMappingDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load prepaymentMapping on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.prepaymentMapping).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
