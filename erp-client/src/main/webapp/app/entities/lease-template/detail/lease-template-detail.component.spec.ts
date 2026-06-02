import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { LeaseTemplateDetailComponent } from './lease-template-detail.component';

describe('LeaseTemplate Management Detail Component', () => {
  let comp: LeaseTemplateDetailComponent;
  let fixture: ComponentFixture<LeaseTemplateDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [LeaseTemplateDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ leaseTemplate: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(LeaseTemplateDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(LeaseTemplateDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load leaseTemplate on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.leaseTemplate).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
