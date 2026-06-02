import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SubCountyCodeDetailComponent } from './sub-county-code-detail.component';

describe('SubCountyCode Management Detail Component', () => {
  let comp: SubCountyCodeDetailComponent;
  let fixture: ComponentFixture<SubCountyCodeDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [SubCountyCodeDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ subCountyCode: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(SubCountyCodeDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(SubCountyCodeDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load subCountyCode on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.subCountyCode).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
