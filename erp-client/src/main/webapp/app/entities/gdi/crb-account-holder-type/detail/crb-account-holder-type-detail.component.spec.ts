import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CrbAccountHolderTypeDetailComponent } from './crb-account-holder-type-detail.component';

describe('CrbAccountHolderType Management Detail Component', () => {
  let comp: CrbAccountHolderTypeDetailComponent;
  let fixture: ComponentFixture<CrbAccountHolderTypeDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CrbAccountHolderTypeDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ crbAccountHolderType: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(CrbAccountHolderTypeDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(CrbAccountHolderTypeDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load crbAccountHolderType on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.crbAccountHolderType).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
