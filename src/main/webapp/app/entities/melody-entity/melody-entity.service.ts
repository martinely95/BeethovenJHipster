import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { JhiDateUtils } from 'ng-jhipster';

import { MelodyEntity } from './melody-entity.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class MelodyEntityService {

    private resourceUrl =  SERVER_API_URL + 'api/melody-entities';

    constructor(private http: Http, private dateUtils: JhiDateUtils) { }

    create(melodyEntity: MelodyEntity): Observable<MelodyEntity> {
        const copy = this.convert(melodyEntity);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    update(melodyEntity: MelodyEntity): Observable<MelodyEntity> {
        const copy = this.convert(melodyEntity);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    find(id: number): Observable<MelodyEntity> {
        return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    query(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceUrl, options)
            .map((res: Response) => this.convertResponse(res));
    }

    delete(id: number): Observable<Response> {
        return this.http.delete(`${this.resourceUrl}/${id}`);
    }

    private convertResponse(res: Response): ResponseWrapper {
        const jsonResponse = res.json();
        const result = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            result.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return new ResponseWrapper(res.headers, result, res.status);
    }

    /**
     * Convert a returned JSON object to MelodyEntity.
     */
    private convertItemFromServer(json: any): MelodyEntity {
        const entity: MelodyEntity = Object.assign(new MelodyEntity(), json);
        entity.createdDateTime = this.dateUtils
            .convertDateTimeFromServer(json.createdDateTime);
        return entity;
    }

    /**
     * Convert a MelodyEntity to a JSON which can be sent to the server.
     */
    private convert(melodyEntity: MelodyEntity): MelodyEntity {
        const copy: MelodyEntity = Object.assign({}, melodyEntity);

        copy.createdDateTime = this.dateUtils.toDate(melodyEntity.createdDateTime);
        return copy;
    }
}
