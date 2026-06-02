import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ShareholderTypeDetailComponent } from './shareholder-type-detail.component';

describe('ShareholderType Management Detail Component', () => {
  let comp: ShareholderTypeDetailComponent;
  let fixture: ComponentFixture<ShareholderTypeDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ShareholderTypeDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ shareholderType: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(ShareholderTypeDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(ShareholderTypeDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load shareholderType on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.shareholderType).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
