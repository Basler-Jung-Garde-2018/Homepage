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
import {EventList} from "../../model/event-list.enum";

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
  selectedYear: number = 2024
  event: string = 'Fasnacht';
  page: number = 0;
  events = Object.values(EventList);

  constructor(private clientService: ClientService) {
    this.selectedYear = new Date().getFullYear();
  }

  loadGallery(): void {
    console.log("gallery load start")
    this.clientService.getGallery(this.selectedYear, this.event, this.page).subscribe(
      (data: Gallery[]) => {
        this.galleries = data;
        console.log("gallery loaded")
      },
      (error) => {
        console.error("Fehler beim Laden der Galerie:", error);
      }
    );
  }

  ngOnInit(): void {
    this.loadGallery();
  }
}
