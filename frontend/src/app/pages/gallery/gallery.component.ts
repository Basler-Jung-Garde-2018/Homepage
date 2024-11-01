import {ClientService} from "../../service/client.service";
import {Gallery} from "../../model/gallery";
import {MatButton} from "@angular/material/button";
import {ChangeDetectionStrategy, Component, OnInit} from '@angular/core';
import {MatExpansionModule} from "@angular/material/expansion";
import {MatFormField, MatLabel} from "@angular/material/form-field";
import {MatOption, MatSelect} from "@angular/material/select";
import {MatInput} from "@angular/material/input";
import {MatPaginator} from "@angular/material/paginator";
import {NgForOf} from "@angular/common";
import {FormBuilder, FormGroup, FormsModule, ReactiveFormsModule} from "@angular/forms";
import {MatCard, MatCardContent} from "@angular/material/card";

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
    FormsModule,
    MatCard,
    MatButton,
    FormsModule,
    MatCard,
    MatCardContent,
    ReactiveFormsModule
  ],
  templateUrl: './gallery.component.html',
  styleUrl: './gallery.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class GalleryComponent implements OnInit {
  galleries: Gallery[] = [];
  selectedYear: number;
  event: string = 'Fasnacht';
  page: number = 0;

  fileStrings: string[] = [];

  eventForm: FormGroup;


  constructor(private clientService: ClientService, private fb: FormBuilder) {
    this.selectedYear = new Date().getFullYear();
    this.eventForm = this.fb.group({
      year: [2024],
      event: ['Fasnacht']
    });
  }

  loadGallery(): void {
    this.clientService.getGallery(this.selectedYear, this.event, this.page).subscribe(
      (data: Gallery[]) => {
        this.galleries = data;
      },
      (error) => {
        console.error("Fehler beim Laden der Galerie:", error);
      }
    );
  }

  onFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (input.files) {
      Array.from(input.files).forEach(file => {
        const reader = new FileReader();
        reader.onload = () => {
          this.fileStrings.push(reader.result as string);
        };
        reader.readAsDataURL(file);
      });
    }
  }

  onUpload() {
    const event: string = this.eventForm.get("event")?.value
    const year: number = Number.parseInt(this.eventForm.get("year")?.value)
    const gallery: Partial<Gallery>[] = this.fileStrings.map(file => {
      return {
        "base64": file,
        "event": event,
        "year": year
      }
    });

    this.clientService.createGallery(gallery).subscribe({
      next: (data) => console.log("nice"),
      error: (error) => console.log(error)
    });

  }

  ngOnInit(): void {
    this.loadGallery();
  }
}
