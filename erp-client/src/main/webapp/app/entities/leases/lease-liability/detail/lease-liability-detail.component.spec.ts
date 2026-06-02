import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { LeaseLiabilityDetailComponent } from './lease-liability-detail.component';

describe('LeaseLiability Management Detail Component', () => {
  let comp: LeaseLiabilityDetailComponent;
  let fixture: ComponentFixture<LeaseLiabilityDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [LeaseLiabilityDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ leaseLiability: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(LeaseLiabilityDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(LeaseLiabilityDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load leaseLiability on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.leaseLiability).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
