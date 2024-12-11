import {inject, Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Appointment} from "../model/appointments";
import {GalleryImage} from "../model/galleryImage";
import {Member} from "../model/members";
import {MetaData} from "../model/MetaData";

@Injectable({
  providedIn: 'root'
})
export class ClientService {
  private readonly httpClient = inject(HttpClient);
  private readonly baseUrl = 'https://jung-garde.ch:8443/jung-garde';

  public addGalleryMetaData(data: Partial<GalleryImage>[]): Observable<GalleryImage[]> {
    return this.httpClient.post<GalleryImage[]>(`${this.baseUrl}/gallery`, data);
  }

  public getGalleryMetaData(year: number, event: string, page: number): Observable<MetaData[]> {
    return this.httpClient.get<MetaData[]>(`${this.baseUrl}/gallery/${year}/${event}/${page}`);
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

  public addMedia(files: File[]) {
    const formData = new FormData();
    files.forEach(file => {
      formData.append(file.name, file);
    })

    return this.httpClient.post<string[]>(`${this.baseUrl}/media/IMAGE`, formData); // todo: remove IMAGE as hardcoded url
  }

  public getImageUrl(type: string, imageId: string): string {
    return `${this.baseUrl}/gallery/${type}/${imageId}`
  }
}
