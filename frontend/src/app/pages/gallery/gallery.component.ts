import {ChangeDetectionStrategy, Component, OnInit, signal} from '@angular/core';
import {
  MatAccordion, MatExpansionModule,
  MatExpansionPanel,
  MatExpansionPanelDescription,
  MatExpansionPanelTitle
} from "@angular/material/expansion";
import {MatFormField, MatLabel} from "@angular/material/form-field";
import {MatOption, MatSelect} from "@angular/material/select";
import {MatInput} from "@angular/material/input";
import {MatPaginator} from "@angular/material/paginator";
import {NgForOf} from "@angular/common";
import {ClientService} from "../../service/client.service";
import {Gallery} from "../../model/gallery";
import {FormsModule} from "@angular/forms";

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
export class GalleryComponent implements OnInit{
  readonly panelOpenState = signal(false);

  base64Images: string[] = [];
  galleries: Gallery[] = [];
  selectedYear: number;
  event: string = 'Fasnacht Mittwuch';
  page: number = 1;
  constructor(private clientService: ClientService) {
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
  ngOnInit(): void {
    this.loadGallery()
  }

  onFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (input.files) {
      console.log(this.base64Images)
      Array.from(input.files).forEach((file) => {
        const reader = new FileReader();
        reader.onload = () => {
          if (reader.result) {
            this.base64Images.push(reader.result as string);
          }
        };
        reader.readAsDataURL(file);
      });
    }
  }
}
