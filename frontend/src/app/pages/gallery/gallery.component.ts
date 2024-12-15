import {ClientService} from "../../service/client.service";
import {MatButton} from "@angular/material/button";
import {ChangeDetectionStrategy, ChangeDetectorRef, Component, inject, OnInit} from '@angular/core';
import {MatExpansionModule} from "@angular/material/expansion";
import {MatFormField, MatLabel} from "@angular/material/form-field";
import {MatOption, MatSelect} from "@angular/material/select";
import {MatInput} from "@angular/material/input";
import {MatPaginator} from "@angular/material/paginator";
import {AsyncPipe, NgForOf, NgIf, NgOptimizedImage} from "@angular/common";
import {FormBuilder, FormGroup, FormsModule, ReactiveFormsModule} from "@angular/forms";
import {MatCard, MatCardContent} from "@angular/material/card";
import {MatAutocomplete, MatAutocompleteTrigger} from "@angular/material/autocomplete";
import {BehaviorSubject, map, Observable, startWith, switchMap} from "rxjs";
import {ToastService} from "../../core/toast.service";
import {MatDialog} from "@angular/material/dialog";
import {ShowImageModalComponent} from "../../core/modals/show-image-modal/show-image-modal.component";
import {MetaData} from "../../model/MetaData";

interface MetaDataGallery {
  metaData: MetaData;
  url: string;
}

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
    ReactiveFormsModule,
    AsyncPipe,
    MatAutocomplete,
    MatAutocompleteTrigger,
    NgOptimizedImage,
    NgIf
  ],
  templateUrl: './gallery.component.html',
  styleUrl: './gallery.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class GalleryComponent implements OnInit {
  private readonly toastService = inject(ToastService);

  gallery$ = new BehaviorSubject<MetaDataGallery[]>([]);
  paginatedGallery$ = new Observable<MetaDataGallery[]>();
  currentIndex: number = 0;
  galleryLength = 0;

  eventForm: FormGroup;
  eventList: string[] = [];
  filteredOptions: Observable<string[]> | undefined;

  constructor(private clientService: ClientService,
              private fb: FormBuilder,
              private cdr: ChangeDetectorRef,
              private matDialog: MatDialog
  ) {
    this.eventForm = this.fb.group({
      year: [2024],
      event: ['Fasnacht']
    });
  }

  private _filter(value: string): string[] {
    const filterValue = value.toLowerCase();

    return this.eventList.filter(option => option.toLowerCase().includes(filterValue));
  }

  loadGallery(): void {
    const year: number = this.eventForm.get("year")?.value;
    const event: string = this.eventForm.get("event")?.value;

    if (event && event !== "" && year) {
      this.clientService.getGalleryMetaData(year, event).subscribe({
        next: metaData => {
          if (metaData.length == 0)
            this.toastService.openWarnToast(`Es gibt keine Bilder für das Event ${event} im Jahr ${year}`)
          else {
            const metaDataGalleries: MetaDataGallery[] = [];
            metaData.forEach(metaData => {
              metaDataGalleries.push({
                url: this.clientService.getImageUrl(this.getFileType(metaData.name), metaData.id, false),
                metaData: metaData
              })
            });
            this.gallery$.next(metaDataGalleries);

            this.galleryLength = metaDataGalleries.length;
            this.updatePagination(0);
            this.cdr.detectChanges();
          }
        },
        error: err => {
          console.error("Error on gallery load : ", err);
          this.toastService.openErrorToast("Es gab ein Fehler beim laden der Gallerie. Versuchen Sie es später erneut.")
        }
      });
    }
  }

  ngOnInit(): void {
    this.clientService.getEventList().subscribe({
      next: (events: string[]) => {
        this.eventList = events
      },
      error: error =>
        console.warn("Fehler beim Laden der Events, " + error)
    });

    this.filteredOptions = this.eventForm.get('event')?.valueChanges.pipe(
      startWith(''),
      map(value => this._filter(value || '')),
    );

    this.loadGallery();
  }

  private getFileType(fileString: string): string {
    const parts = fileString.split('.');
    return parts[parts.length - 1];
  }

  updatePagination(page: number) {
    this.paginatedGallery$ = this.gallery$.pipe(
      map(gallery => {
        const startIndex = page * 10;
        const endIndex = startIndex + 10;
        return gallery.slice(startIndex, endIndex);
      })
    );
  }

  enlargeImage(metaDataGallery: MetaDataGallery) {
    this.matDialog.open(ShowImageModalComponent, {
      data: {
        metaData: metaDataGallery.metaData,
        url: metaDataGallery.url
      },
      height: '90vh',
      minWidth: '90vw'
    }).afterClosed().pipe(
      switchMap(result => this.paginatedGallery$.pipe(
        map(gallery => {
          if (result === 'previous') {
            this.currentIndex = (this.currentIndex > 0) ? this.currentIndex - 1 : gallery.length - 1;
          } else if (result === 'next') {
            this.currentIndex = (this.currentIndex < gallery.length - 1) ? this.currentIndex + 1 : 0;
          } else {
            return null;
          }
          return gallery[this.currentIndex];
        })
      ))
    ).subscribe(newImageUrl => {
      if (newImageUrl) {
        this.enlargeImage(newImageUrl);
      }
    });
  }
}
