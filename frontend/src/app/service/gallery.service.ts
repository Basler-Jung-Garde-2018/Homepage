import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import {Gallery} from "../model/gallery";

@Injectable({
  providedIn: 'root'
})
export class GalleryService {
  private readonly baseUrl = 'http://localhost:8080/jung-garde';

  constructor(private httpClient: HttpClient) {}

  async createGalleryItem(file: File): Promise<Gallery> {
    const base64 = await this.convertFileToBase64(file);
    return {
      id: 'default-id',
      format: file.type,
      base64: base64,
      year: new Date().getFullYear(),
      event: 'default-event',
      positionId: 'default-position',
    };
  }

  public createGallery(data: Gallery[]): Observable<Gallery[]> {
    return this.httpClient.post<Gallery[]>(`${this.baseUrl}/gallery`, JSON.stringify(data));
  }

  private convertFileToBase64(file: File): Promise<string> {
    return new Promise((resolve, reject) => {
      const reader = new FileReader();
      reader.readAsDataURL(file);
      reader.onload = () => resolve(reader.result as string);
      reader.onerror = error => reject(error);
    });
  }
}
