import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { IFRS16LeaseContractDetailComponent } from './ifrs-16-lease-contract-detail.component';

describe('IFRS16LeaseContract Management Detail Component', () => {
  let comp: IFRS16LeaseContractDetailComponent;
  let fixture: ComponentFixture<IFRS16LeaseContractDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [IFRS16LeaseContractDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ iFRS16LeaseContract: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(IFRS16LeaseContractDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(IFRS16LeaseContractDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load iFRS16LeaseContract on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.iFRS16LeaseContract).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
