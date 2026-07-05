jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { LeaseLiabilityByAccountReportService } from '../service/lease-liability-by-account-report.service';

import { LeaseLiabilityByAccountReportDeleteDialogComponent } from './lease-liability-by-account-report-delete-dialog.component';

describe('LeaseLiabilityByAccountReport Management Delete Component', () => {
  let comp: LeaseLiabilityByAccountReportDeleteDialogComponent;
  let fixture: ComponentFixture<LeaseLiabilityByAccountReportDeleteDialogComponent>;
  let service: LeaseLiabilityByAccountReportService;
  let mockActiveModal: NgbActiveModal;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [LeaseLiabilityByAccountReportDeleteDialogComponent],
      providers: [NgbActiveModal],
    })
      .overrideTemplate(LeaseLiabilityByAccountReportDeleteDialogComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(LeaseLiabilityByAccountReportDeleteDialogComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(LeaseLiabilityByAccountReportService);
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
