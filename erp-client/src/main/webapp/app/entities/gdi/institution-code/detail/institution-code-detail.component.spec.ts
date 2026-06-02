import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { InstitutionCodeDetailComponent } from './institution-code-detail.component';

describe('InstitutionCode Management Detail Component', () => {
  let comp: InstitutionCodeDetailComponent;
  let fixture: ComponentFixture<InstitutionCodeDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [InstitutionCodeDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ institutionCode: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(InstitutionCodeDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(InstitutionCodeDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load institutionCode on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.institutionCode).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
