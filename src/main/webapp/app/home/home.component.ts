import {Component, ElementRef, OnInit} from '@angular/core';
import {NgbModalRef} from '@ng-bootstrap/ng-bootstrap';
import {JhiEventManager} from 'ng-jhipster';

import {Account, LoginModalService, Principal} from '../shared';

@Component({
    selector: 'jhi-home',
    templateUrl: './home.component.html',
    styleUrls: [
        'home.scss'
    ]

})
export class HomeComponent implements OnInit {
    account: Account;
    modalRef: NgbModalRef;
    WIDTH = 600;
    HEIGHT = 400;
    ROWS = 6;
    COLS = 15;
    mouseDown = false;
    canvas;
    ctx;
    flag = false;
    prevX = 0;
    currX = 0;
    prevY = 0;
    currY = 0;
    dot_flag = false;
    x = 'black';
    y = 2;
    w;
    h;
    PointForColors: any;
    Xelem: any;
    Yelem: any;

    constructor(private _el: ElementRef,
                private principal: Principal,
                private loginModalService: LoginModalService,
                private eventManager: JhiEventManager) {
    }

    ngOnInit() {
        this.principal.identity().then((account) => {
            this.account = account;
            this.PointForColors = {};
            this.PointForColors[this.x] = [];
            for (let i = 0; i < this.ROWS; i++) {
                this.PointForColors[this.x][i] = [];
                for (let j = 0; j < this.COLS; j++) {
                    this.PointForColors[this.x][i][j] = false;
                }
            }
            this.canvas = document.getElementById('can');
            this.canvas.width = this.WIDTH;
            this.canvas.height = this.HEIGHT;
            this.ctx = this.canvas.getContext('2d');
            // this.ctx.fillRect(0, 0, 1280, 720);
            this.w = this.canvas.width;
            this.h = this.canvas.height;

            // this.canvas.addEventListener('mousemove', function(e) {
            //     this.findxy('move', e);
            //     if (this.mouseDown) {
            //         this.getWhichNode(this.currX, this.currY);
            //     }
            // }, false);
            // this.canvas.addEventListener('mousedown', function(e) {
            //     this.mouseDown = true;
            //     this.findxy('down', e);
            //     this.getWhichNode(this.currX, this.currY);
            // }, false);
            // this.canvas.addEventListener('mouseup', function(e) {
            //     this.mouseDown = false;
            //     this.findxy('up', e);
            // }, false);
            // this.canvas.addEventListener('mouseout', function(e) {
            //     this.findxy('out', e);
            // }, false);
        });
        this.registerAuthenticationSuccess();
    }

    mouseMoveEvent(event) {
        this.findxy('move', event);
        if (this.mouseDown) {
            this.getWhichNode(this.currX, this.currY);
        }
    }

    mouseDownEvent(event) {
        this.mouseDown = true;
        this.findxy('down', event);
        this.getWhichNode(this.currX, this.currY);
    }

    mouseUpEvent(event) {
        this.mouseDown = false;
        this.findxy('up', event);
    }

    mouseOutEvent(event) {
        this.findxy('out', event);
    }

    registerAuthenticationSuccess() {
        this.eventManager.subscribe('authenticationSuccess', (message) => {
            this.principal.identity().then((account) => {
                this.account = account;
            });
        });
    }

    isAuthenticated() {
        return this.principal.isAuthenticated();
    }

    login() {
        this.modalRef = this.loginModalService.open();
    }

    color(obj) {
        switch (obj.srcElement.id) {
            case 'green':
                this.x = 'green';
                break;
            case 'blue':
                this.x = 'blue';
                break;
            case 'red':
                this.x = 'red';
                break;
            case 'yellow':
                this.x = 'yellow';
                break;
            case 'orange':
                this.x = 'orange';
                break;
            case 'black':
                this.x = 'black';
                break;
            case 'white':
                this.x = 'white';
                break;
        }
        if (this.x === 'white') {
            this.y = 14;
        } else {
            this.y = 2;
        }
        if (!(this.x in this.PointForColors)) {
            this.PointForColors[this.x] = [];
            for (let i = 0; i < this.ROWS; i++) {
                this.PointForColors[this.x][i] = [];
                for (let j = 0; j < this.COLS; j++) {
                    this.PointForColors[this.x][i][j] = false;
                }
            }
        }
    }

    draw() {
        this.ctx.beginPath();
        this.ctx.moveTo(this.prevX, this.prevY);
        this.ctx.lineTo(this.currX, this.currY);
        this.ctx.strokeStyle = this.x;
        this.ctx.lineWidth = this.y;
        this.ctx.stroke();
        this.ctx.closePath();
    }

    erase() {
        const m = confirm('Want to clear');
        if (m) {
            this.ctx.clearRect(0, 0, this.w, this.h);
            document.getElementById('canvasimg').style.display = 'none';
        }
    }

    save() {
        let dataURL: string;
        dataURL = this.canvas.toDataURL();
        (document.getElementById('canvasimg') as HTMLImageElement).src = dataURL;
        document.getElementById('canvasimg').style.display = 'inline';
        this.sendData();
    }

    saveInDB() {
        const xhr = new XMLHttpRequest();
        const url = '/api/beathoven/saveInDB';
        xhr.open('POST', url, true);
        xhr.setRequestHeader('Content-type', 'application/json');
        xhr.setRequestHeader('X-XSRF-TOKEN', this.getCSRF());

        const data = JSON.stringify(this.PointForColors);
        xhr.send(data);
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

    sendData() {
        const xhr = new XMLHttpRequest();
        const url = '/api/beathoven/post';
        xhr.open('POST', url, true);
        xhr.setRequestHeader('Content-type', 'application/json');
        xhr.setRequestHeader('X-XSRF-TOKEN', this.getCSRF());

        const data = JSON.stringify(this.PointForColors);
        xhr.send(data);
        console.log(JSON.parse(JSON.stringify(this.PointForColors)));
    }

    findxy(res, e) {
        if (res === 'down') {
            this.prevX = this.currX;
            this.prevY = this.currY;
            this.currX = e.clientX
                - 0.33 * this._el.nativeElement.offsetWidth + 30;
            this.currY = e.clientY - this._el.nativeElement.getBoundingClientRect().top;

            this.flag = true;
            this.dot_flag = true;
            if (this.dot_flag) {
                this.ctx.beginPath();
                this.ctx.fillStyle = this.x;
                this.ctx.fillRect(this.currX, this.currY, 2, 2);
                this.ctx.closePath();
                this.dot_flag = false;
            }
        }
        if (res === 'up' || res === 'out') {
            this.flag = false;
        }
        if (res === 'move') {
            if (this.flag) {
                this.prevX = this.currX;
                this.prevY = this.currY;
                this.currX = e.clientX
                    - 0.33 * this._el.nativeElement.offsetWidth + 30;
                this.currY = e.clientY - this._el.nativeElement.getBoundingClientRect().top;
                this.draw();
            }
        }
    }

    getWhichNode(xcoord, ycoord) {
        const nodeWidth = this.WIDTH / this.COLS;
        const nodeHeight = this.HEIGHT / this.ROWS;
        const nodeInCol = Math.ceil(xcoord / nodeWidth) - 1;
        const nodeInRow = Math.ceil(ycoord / nodeHeight) - 1;
        this.PointForColors[this.x][nodeInRow][nodeInCol] = true;
    }
}
