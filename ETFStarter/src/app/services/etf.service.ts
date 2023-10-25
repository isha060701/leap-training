import { Injectable } from '@angular/core';
import { Observable, catchError, of, throwError } from 'rxjs';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Etf } from '../models/etf';
@Injectable({
  providedIn: 'root'
})
export class EtfService {

  constructor(private http:HttpClient) {}

  etfs:Etf[]=[];
  private url="http://localhost:8080/etfs?filter=";

  handleError(response: HttpErrorResponse) {
    if (response.error instanceof ProgressEvent) {
      console.error('A client-side or network error occurred; ' + `${response.message} ${response.status} ${response.statusText}`);
    }
    else { console.error(`Backend returned code ${response.status}, ` + `body was: ${JSON.stringify(response.error)}`); }
    return throwError(() => 'Unable to contact service; please try again later.');
  }
  
  getEtfs(): Observable<Etf[]>{
    return this.http.get<Etf[]>(this.url).pipe(catchError(this.handleError));
    // return of(this.cars)
  }
  getEtfsWithFilters(filter:string):Observable<Etf[]>{
    return this.http.get<Etf[]>(`${this.url}${filter}`)
  }
}
