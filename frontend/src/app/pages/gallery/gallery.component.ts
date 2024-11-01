import { ChangeDetectionStrategy, Component, OnInit } from '@angular/core';
import { MatExpansionModule } from "@angular/material/expansion";
import { MatFormField, MatLabel } from "@angular/material/form-field";
import { MatOption, MatSelect } from "@angular/material/select";
import { MatInput } from "@angular/material/input";
import { MatPaginator } from "@angular/material/paginator";
import { NgForOf } from "@angular/common";
import { ClientService } from "../../service/client.service";
import { Gallery } from "../../model/gallery";
import { FormsModule } from "@angular/forms";
import { GalleryService } from "../../service/gallery.service";
import { v4 as uuidv4 } from 'uuid'; // UUID library

@Component({
  selector: 'app-gallery',
  standalone: true,
  imports: [
    MatExpansionModule,
    MatFormField,
    MatSelect,
    MatOption,
    MatInput,
    MatLabel,
    MatPaginator,
    NgForOf,
    FormsModule
  ],
  templateUrl: './gallery.component.html',
  styleUrl: './gallery.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class GalleryComponent implements OnInit {
  galleries: Gallery[] = [];
  selectedYear: number;
  event: string = 'Fasnacht Mittwuch';
  page: number = 1;

  constructor(private clientService: ClientService, private galleryService: GalleryService) {
    this.selectedYear = new Date().getFullYear();
  }

  loadGallery(): void {
    this.clientService.getGallery(this.selectedYear, this.event, this.page).subscribe(
      (data: Gallery[]) => {
        this.galleries = data;
      },
      (error) => {
        console.error('Error fetching gallery', error);
      }
    );
  }

  handleFileInput(event: any): void {
    const files = event.target.files as FileList;
    const newGalleries: Gallery[] = [];

    // Create an array of promises for file reading
    const fileReadPromises = Array.from(files).map((file) => {
      return new Promise<Gallery>((resolve) => {
        const reader = new FileReader();
        reader.onload = () => {
          const base64String = reader.result as string;
          const newGallery: Gallery = {
            id: uuidv4(),
            format: 'jpeg', // Standardformat; kann nach Bedarf angepasst werden
            base64: base64String,
            year: this.selectedYear,
            event: this.event,
            positionId: 'defaultPositionId' // Standardposition; kann nach Bedarf angepasst werden
          };

          // Log the new gallery object
          console.log('New Gallery Object:', newGallery);
          resolve(newGallery);
        };
        reader.readAsDataURL(file);
      });
    });

    // Wait for all file read promises to resolve
    Promise.all(fileReadPromises).then((results) => {
      newGalleries.push(...results); // Add all new galleries to the array

      // Now call the client service with the populated newGalleries
      this.clientService.createGallery(newGalleries).subscribe(
        (response) => {
          console.log('Gallery created successfully:', response);
          this.loadGallery(); // Laden der Galerie nach dem Upload
        },
        (error) => {
          console.error('Error creating gallery', error);
        }
      );
    });
  }

  ngOnInit(): void {
    this.loadGallery();
  }
}
