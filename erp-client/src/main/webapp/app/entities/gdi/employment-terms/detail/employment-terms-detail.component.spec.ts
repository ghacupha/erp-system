import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { EmploymentTermsDetailComponent } from './employment-terms-detail.component';

describe('EmploymentTerms Management Detail Component', () => {
  let comp: EmploymentTermsDetailComponent;
  let fixture: ComponentFixture<EmploymentTermsDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [EmploymentTermsDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ employmentTerms: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(EmploymentTermsDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(EmploymentTermsDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load employmentTerms on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.employmentTerms).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
