import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { LeaseLiabilityScheduleItemDetailComponent } from './lease-liability-schedule-item-detail.component';

describe('LeaseLiabilityScheduleItem Management Detail Component', () => {
  let comp: LeaseLiabilityScheduleItemDetailComponent;
  let fixture: ComponentFixture<LeaseLiabilityScheduleItemDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [LeaseLiabilityScheduleItemDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ leaseLiabilityScheduleItem: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(LeaseLiabilityScheduleItemDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(LeaseLiabilityScheduleItemDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load leaseLiabilityScheduleItem on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.leaseLiabilityScheduleItem).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
