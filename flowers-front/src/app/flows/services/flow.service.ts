import { Injectable } from '@angular/core';
import {Observable} from 'rxjs';
import {HttpClient} from '@angular/common/http';
import {Flow} from '../model/flow';

@Injectable({
  providedIn: 'root'
})
export class FlowService {

  private readonly baseUrl = 'api/flows';

  constructor(private http: HttpClient) {}

  get(page: number = 0, size: number = 10, sortBy: string = 'id'): Observable<Flow[]> {
    return this.http.get<Flow[]>(this.baseUrl);
  }

  getById(id: number): Observable<Flow> {
    const url = `${this.baseUrl}/${id}`;
    return this.http.get<Flow>(url);
  }




}
