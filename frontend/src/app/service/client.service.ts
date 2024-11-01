import {inject, Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Appointment} from "../model/appointments";
import {Gallery} from "../model/gallery";

@Injectable({
  providedIn: 'root'
})
export class ClientService {
  private readonly httpClient = inject(HttpClient);
  private readonly baseUrl = 'http://localhost:8080/jung-garde';

  public getPing(): void {
    this.httpClient.get<any>(`${this.baseUrl}/ping`).subscribe(resp => {
      console.log(resp);
    });
  }

  public createGallery(data: Partial<Gallery>[]): Observable<Gallery[]> {
    return this.httpClient.post<Gallery[]>(`${this.baseUrl}/gallery`, data);
  }

  public getGallery(year: number, event: string, page: number): Observable<Gallery[]> {
    return this.httpClient.get<Gallery[]>(`${this.baseUrl}/gallery/${year}/${event}/${page}`);
  }

  public getAppointments(): Observable<Appointment[]> {
    return this.httpClient.get<Appointment[]>(`${this.baseUrl}/appointments`);
  }

  public getMembers(): Observable<any> {
    return this.httpClient.get(`${this.baseUrl}/members`);
  }

  public getAdministrativeMembers(): Observable<any> {
    return this.httpClient.get(`${this.baseUrl}/members/administrative`);
  }
}
