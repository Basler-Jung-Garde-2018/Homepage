import {inject, Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Appointment} from "../model/appointments";
import {Gallery} from "../model/gallery";
import {Member} from "../model/members";

@Injectable({
  providedIn: 'root'
})
export class ClientService {
  private readonly httpClient = inject(HttpClient);
  private readonly baseUrl = 'https://jung-garde.ch:8443/jung-garde';

  public addGalleryMetaData(data: Partial<Gallery>[]): Observable<Gallery[]> {
    return this.httpClient.post<Gallery[]>(`${this.baseUrl}/gallery`, data);
  }

  public getGalleryIds(year: number, event: string, page: number): Observable<string[]> {
    return this.httpClient.get<string[]>(`${this.baseUrl}/gallery/${year}/${event}/${page}`);
  }

  public getAppointments(): Observable<Appointment[]> {
    return this.httpClient.get<Appointment[]>(`${this.baseUrl}/appointments`);
  }

  public getMembers(): Observable<Member[]> {
    return this.httpClient.get<Member[]>(`${this.baseUrl}/members`);
  }

  public getAdministrativeMembers(): Observable<any> {
    return this.httpClient.get(`${this.baseUrl}/members/administrative`);
  }

  public getEventList(): Observable<string[]> {
    return this.httpClient.get<string[]>(`${this.baseUrl}/gallery/events`);
  }

  public getGalleryImage(id: string): Observable<Gallery> {
    return this.httpClient.get<Gallery>(`${this.baseUrl}/media/IMAGE/${id}`);
  }

  public addMedia(files: File[]) {
    const formData = new FormData();
    files.forEach(file => {
      formData.append(file.name, file);
    })

    return this.httpClient.post<string[]>(`${this.baseUrl}/media/IMAGE`, formData); // todo: remove IMAGE as hardcoded url
  }
}
