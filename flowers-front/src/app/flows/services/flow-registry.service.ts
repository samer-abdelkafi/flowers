import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class FlowService {

  private readonly baseUrl = 'api/flows/registry/';


  constructor(private http : HttpClient) {  }

  getActiveFlowsId(): Observable<number[]> {
    return this.http.get<number[]>(this.baseUrl + 'ids');
  }

  unregister(flowId: number): Observable<string[]> {
    return this.http.post<string[]>( this.baseUrl + flowId +'/unregister', null);
  }

  register(flowId: number): Observable<string[]> {
    return this.http.post<string[]>(this.baseUrl + flowId +'/register', null);
  }


}
