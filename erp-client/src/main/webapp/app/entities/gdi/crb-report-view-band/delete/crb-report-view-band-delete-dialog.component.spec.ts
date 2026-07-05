jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { CrbReportViewBandService } from '../service/crb-report-view-band.service';

import { CrbReportViewBandDeleteDialogComponent } from './crb-report-view-band-delete-dialog.component';

describe('CrbReportViewBand Management Delete Component', () => {
  let comp: CrbReportViewBandDeleteDialogComponent;
  let fixture: ComponentFixture<CrbReportViewBandDeleteDialogComponent>;
  let service: CrbReportViewBandService;
  let mockActiveModal: NgbActiveModal;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [CrbReportViewBandDeleteDialogComponent],
      providers: [NgbActiveModal],
    })
      .overrideTemplate(CrbReportViewBandDeleteDialogComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(CrbReportViewBandDeleteDialogComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(CrbReportViewBandService);
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
