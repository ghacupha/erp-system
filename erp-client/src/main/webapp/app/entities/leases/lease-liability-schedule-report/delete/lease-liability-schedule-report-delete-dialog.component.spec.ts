jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { LeaseLiabilityScheduleReportService } from '../service/lease-liability-schedule-report.service';

import { LeaseLiabilityScheduleReportDeleteDialogComponent } from './lease-liability-schedule-report-delete-dialog.component';

describe('LeaseLiabilityScheduleReport Management Delete Component', () => {
  let comp: LeaseLiabilityScheduleReportDeleteDialogComponent;
  let fixture: ComponentFixture<LeaseLiabilityScheduleReportDeleteDialogComponent>;
  let service: LeaseLiabilityScheduleReportService;
  let mockActiveModal: NgbActiveModal;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [LeaseLiabilityScheduleReportDeleteDialogComponent],
      providers: [NgbActiveModal],
    })
      .overrideTemplate(LeaseLiabilityScheduleReportDeleteDialogComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(LeaseLiabilityScheduleReportDeleteDialogComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(LeaseLiabilityScheduleReportService);
    mockActiveModal = TestBed.inject(NgbActiveModal);
  });

  describe('confirmDelete', () => {
    it('Should call delete service on confirmDelete', inject(
      [],
      fakeAsync(() => {
        // GIVEN
        jest.spyOn(service, 'delete').mockReturnValue(of(new HttpResponse({})));

        // WHEN
        comp.confirmDelete(123);
        tick();

        // THEN
        expect(service.delete).toHaveBeenCalledWith(123);
        expect(mockActiveModal.close).toHaveBeenCalledWith('deleted');
      })
    ));

    it('Should not call delete service on clear', () => {
      // GIVEN
      jest.spyOn(service, 'delete');

      // WHEN
      comp.cancel();

      // THEN
      expect(service.delete).not.toHaveBeenCalled();
      expect(mockActiveModal.close).not.toHaveBeenCalled();
      expect(mockActiveModal.dismiss).toHaveBeenCalled();
    });
  });
});
