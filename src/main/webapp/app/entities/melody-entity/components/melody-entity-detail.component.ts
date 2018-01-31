import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { MelodyEntity } from '../models/melody-entity.model';
import { MelodyEntityService } from '../services/melody-entity.service';

@Component({
    selector: 'jhi-melody-entity-detail',
    templateUrl: './melody-entity-detail.component.html'
})
export class MelodyEntityDetailComponent implements OnInit, OnDestroy {

    melodyEntity: MelodyEntity;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private melodyEntityService: MelodyEntityService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInMelodyEntities();
    }

    load(id) {
        this.melodyEntityService.find(id).subscribe((melodyEntity) => {
            this.melodyEntity = melodyEntity;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInMelodyEntities() {
        this.eventSubscriber = this.eventManager.subscribe(
            'melodyEntityListModification',
            (response) => this.load(this.melodyEntity.id)
        );
    }

    getCSRF() {
        const name = 'XSRF-TOKEN=';
        const ca = document.cookie.split(';');
        for (let i = 0; i < ca.length; i++) {
            let c = ca[i];
            while (c.charAt(0) === ' ') {
                c = c.substring(1);
            }
            if (c.indexOf(name) !== -1) {
                return c.substring(name.length, c.length);
            }
        }
        return '';
    }

    download() {
        // const xhr = new XMLHttpRequest();
        const url = '/api/beathoven/download' + this.melodyEntity.id + '.midi';
        window.open(url);
        // xhr.open('GET', url, false);
        // xhr.setRequestHeader('Content-type', 'application/json');
        // xhr.setRequestHeader('X-XSRF-TOKEN', this.getCSRF());
        //
        // xhr.send();
    }
}
