import {Component, inject, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, FormsModule, ReactiveFormsModule} from "@angular/forms";
import {ClientService} from "../../../service/client.service";
import {GalleryImage} from "../../../model/galleryImage";
import {MatCard, MatCardContent, MatCardHeader, MatCardSubtitle, MatCardTitle} from "@angular/material/card";
import {MatFormField, MatLabel} from "@angular/material/form-field";
import {MatInput} from "@angular/material/input";
import {MatButton} from "@angular/material/button";
import {
  MatCell,
  MatCellDef,
  MatColumnDef,
  MatHeaderCell,
  MatHeaderCellDef,
  MatHeaderRow,
  MatHeaderRowDef,
  MatRow,
  MatRowDef,
  MatTable,
  MatTableDataSource
} from "@angular/material/table";
import {MatSlideToggle} from "@angular/material/slide-toggle";
import {AsyncPipe, NgIf} from "@angular/common";
import {map, Observable, startWith, switchMap} from "rxjs";
import {MatAutocomplete, MatAutocompleteTrigger, MatOption} from "@angular/material/autocomplete";
import {ToastService} from "../../../core/toast.service";

@Component({
  selector: 'app-edit-gallery',
  standalone: true,
  imports: [
    MatCard,
    MatCardContent,
    ReactiveFormsModule,
    MatFormField,
    MatInput,
    MatButton,
    MatLabel,
    MatCardHeader,
    MatCardTitle,
    MatCardSubtitle,
    MatTable,
    MatColumnDef,
    MatHeaderCell,
    MatCell,
    MatHeaderCellDef,
    MatCellDef,
    MatHeaderRow,
    MatRow,
    MatRowDef,
    MatHeaderRowDef,
    MatSlideToggle,
    FormsModule,
    NgIf,
    AsyncPipe,
    MatAutocomplete,
    MatAutocompleteTrigger,
    MatOption
  ],
  templateUrl: './edit-gallery.component.html',
  styleUrl: './edit-gallery.component.scss'
})
export class EditGalleryComponent implements OnInit {
  private readonly toastService = inject(ToastService);
  files: File[] = [];
  eventForm: FormGroup;

  filteredOptions: Observable<string[]> | undefined;
  events: string[] = [
    'Fasnacht',
    'Allschwiler Fasnacht',
    'Probe',
    'Probetag u. Messebummel',
    'Bummel',
    'Marschprobe'
  ];

  displayedColumns: string[] = ['year', 'event', 'base64'];
  imageDisplay = new MatTableDataSource<Partial<GalleryImage>>([]);

  isModalOpen = false;
  selectedImage: string | null = null;

  constructor(private clientService: ClientService, private fb: FormBuilder) {
    this.eventForm = this.fb.group({
      year: [2024],
      event: ['Fasnacht']
    });
  }

  onFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (input.files) {
      const event: string = this.eventForm.get("event")?.value
      const year: number = Number.parseInt(this.eventForm.get("year")?.value)

      Array.from(input.files).forEach(file => {
        if (!file.type.includes("image")) {
          return;
        }
        this.files.push(file)
        const reader = new FileReader();
        reader.onload = () => {
          // downscale image to optimize performance
          const img = new Image();
          img.onload = () => {
            const canvas = document.createElement('canvas');
            const ctx = canvas.getContext('2d');
            const maxWidth = (1440 * img.width) / img.height;
            const maxHeight = 1440;
            let width = img.width;
            let height = img.height;

            if (width > height) {
              if (width > maxWidth) {
                height = Math.round((height * maxWidth) / width);
                width = maxWidth;
              }
            } else {
              if (height > maxHeight) {
                width = Math.round((width * maxHeight) / height);
                height = maxHeight;
              }
            }

            canvas.width = width;
            canvas.height = height;

            if (ctx)
              ctx.drawImage(img, 0, 0, width, height);

            const base64 = canvas.toDataURL(file.type);
            this.imageDisplay.data = [...this.imageDisplay.data, {
              year,
              event,
              base64: base64,
              published: false
            }];
          };
          if (typeof reader.result === "string") {
            img.src = reader.result;
          }
        };
        reader.readAsDataURL(file);
      });
    }
  }

  onUpload() {
    const event: string = this.eventForm.get("event")?.value
    const year: number = Number.parseInt(this.eventForm.get("year")?.value)

    if (year && event && event !== "" && this.files.length !== 0) {
      this.clientService.addMedia(this.files, "PUBLIC").pipe(
        switchMap((imageIds) => {
          const gallery: Partial<GalleryImage>[] = imageIds.map(id => ({id, event, year, published: true}));
          return this.clientService.addGalleryMetaData(gallery);
        }),
      ).subscribe({
        next: () => {
          this.toastService.openSuccessToast("Bilder erfolgreich hochgeladen");
          this.files = [];
          this.imageDisplay.data = [];
        },
        error: (err) => {
          console.error('Unexpected error:', err)
          this.toastService.openErrorToast("Es gab ein Fehler beim hochladen der Bilder. Bitte versuchen Sie es erneut.")
        }
      });
    } else {
      this.toastService.openWarnToast("Bitte überprüfen Sie Ihre Eingaben.")
    }
  }

  openImageModal(base64: string): void {
    this.selectedImage = base64;
    this.isModalOpen = true;
  }

  closeImageModal(): void {
    this.isModalOpen = false;
    this.selectedImage = null;
  }

  ngOnInit() {
    this.filteredOptions = this.eventForm.get('event')?.valueChanges.pipe(
      startWith(''),
      map(value => this._filter(value || '')),
    );
  }

  private _filter(value: string): string[] {
    const filterValue = value.toLowerCase();

    return this.events.filter(option => option.toLowerCase().includes(filterValue));
  }
}
