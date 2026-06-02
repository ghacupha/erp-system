import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CrbSourceOfInformationTypeDetailComponent } from './crb-source-of-information-type-detail.component';

describe('CrbSourceOfInformationType Management Detail Component', () => {
  let comp: CrbSourceOfInformationTypeDetailComponent;
  let fixture: ComponentFixture<CrbSourceOfInformationTypeDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CrbSourceOfInformationTypeDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ crbSourceOfInformationType: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(CrbSourceOfInformationTypeDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(CrbSourceOfInformationTypeDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load crbSourceOfInformationType on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.crbSourceOfInformationType).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
