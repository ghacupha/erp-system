import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { LeaseContractDetailComponent } from './lease-contract-detail.component';

describe('LeaseContract Management Detail Component', () => {
  let comp: LeaseContractDetailComponent;
  let fixture: ComponentFixture<LeaseContractDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [LeaseContractDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ leaseContract: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(LeaseContractDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(LeaseContractDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load leaseContract on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.leaseContract).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
