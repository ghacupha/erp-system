jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { LeaseLiabilityPostingReportService } from '../service/lease-liability-posting-report.service';

import { LeaseLiabilityPostingReportDeleteDialogComponent } from './lease-liability-posting-report-delete-dialog.component';

describe('LeaseLiabilityPostingReport Management Delete Component', () => {
  let comp: LeaseLiabilityPostingReportDeleteDialogComponent;
  let fixture: ComponentFixture<LeaseLiabilityPostingReportDeleteDialogComponent>;
  let service: LeaseLiabilityPostingReportService;
  let mockActiveModal: NgbActiveModal;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [LeaseLiabilityPostingReportDeleteDialogComponent],
      providers: [NgbActiveModal],
    })
      .overrideTemplate(LeaseLiabilityPostingReportDeleteDialogComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(LeaseLiabilityPostingReportDeleteDialogComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(LeaseLiabilityPostingReportService);
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
