import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { WorkInProgressTransferDetailComponent } from './work-in-progress-transfer-detail.component';

describe('WorkInProgressTransfer Management Detail Component', () => {
  let comp: WorkInProgressTransferDetailComponent;
  let fixture: ComponentFixture<WorkInProgressTransferDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [WorkInProgressTransferDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ workInProgressTransfer: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(WorkInProgressTransferDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(WorkInProgressTransferDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load workInProgressTransfer on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.workInProgressTransfer).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
