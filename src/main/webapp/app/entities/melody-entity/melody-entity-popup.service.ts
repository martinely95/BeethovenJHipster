import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { DatePipe } from '@angular/common';
import { MelodyEntity } from './melody-entity.model';
import { MelodyEntityService } from './melody-entity.service';

@Injectable()
export class MelodyEntityPopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private datePipe: DatePipe,
        private modalService: NgbModal,
        private router: Router,
        private melodyEntityService: MelodyEntityService

    ) {
        this.ngbModalRef = null;
    }

    open(component: Component, id?: number | any): Promise<NgbModalRef> {
        return new Promise<NgbModalRef>((resolve, reject) => {
            const isOpen = this.ngbModalRef !== null;
            if (isOpen) {
                resolve(this.ngbModalRef);
            }

            if (id) {
                this.melodyEntityService.find(id).subscribe((melodyEntity) => {
                    melodyEntity.createdDateTime = this.datePipe
                        .transform(melodyEntity.createdDateTime, 'yyyy-MM-ddTHH:mm:ss');
                    this.ngbModalRef = this.melodyEntityModalRef(component, melodyEntity);
                    resolve(this.ngbModalRef);
                });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.melodyEntityModalRef(component, new MelodyEntity());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    melodyEntityModalRef(component: Component, melodyEntity: MelodyEntity): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.melodyEntity = melodyEntity;
        modalRef.result.then((result) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true, queryParamsHandling: 'merge' });
            this.ngbModalRef = null;
        }, (reason) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true, queryParamsHandling: 'merge' });
            this.ngbModalRef = null;
        });
        return modalRef;
    }
}
