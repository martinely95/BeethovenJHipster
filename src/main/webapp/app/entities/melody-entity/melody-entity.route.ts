import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil } from 'ng-jhipster';

import { UserRouteAccessService } from '../../shared';
import { MelodyEntityComponent } from './melody-entity.component';
import { MelodyEntityDetailComponent } from './melody-entity-detail.component';
import { MelodyEntityPopupComponent } from './melody-entity-dialog.component';
import { MelodyEntityDeletePopupComponent } from './melody-entity-delete-dialog.component';

@Injectable()
export class MelodyEntityResolvePagingParams implements Resolve<any> {

    constructor(private paginationUtil: JhiPaginationUtil) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const page = route.queryParams['page'] ? route.queryParams['page'] : '1';
        const sort = route.queryParams['sort'] ? route.queryParams['sort'] : 'id,asc';
        return {
            page: this.paginationUtil.parsePage(page),
            predicate: this.paginationUtil.parsePredicate(sort),
            ascending: this.paginationUtil.parseAscending(sort)
      };
    }
}

export const melodyEntityRoute: Routes = [
    {
        path: 'melody-entity',
        component: MelodyEntityComponent,
        resolve: {
            'pagingParams': MelodyEntityResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'MelodyEntities'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'melody-entity/:id',
        component: MelodyEntityDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'MelodyEntities'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const melodyEntityPopupRoute: Routes = [
    {
        path: 'melody-entity-new',
        component: MelodyEntityPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'MelodyEntities'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'melody-entity/:id/edit',
        component: MelodyEntityPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'MelodyEntities'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'melody-entity/:id/delete',
        component: MelodyEntityDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'MelodyEntities'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
