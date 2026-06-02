import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { FxReceiptPurposeTypeDetailComponent } from './fx-receipt-purpose-type-detail.component';

describe('FxReceiptPurposeType Management Detail Component', () => {
  let comp: FxReceiptPurposeTypeDetailComponent;
  let fixture: ComponentFixture<FxReceiptPurposeTypeDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [FxReceiptPurposeTypeDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ fxReceiptPurposeType: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(FxReceiptPurposeTypeDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(FxReceiptPurposeTypeDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load fxReceiptPurposeType on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.fxReceiptPurposeType).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
