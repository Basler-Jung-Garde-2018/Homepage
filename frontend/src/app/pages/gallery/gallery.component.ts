import {ClientService} from "../../service/client.service";
import {MatButton} from "@angular/material/button";
import {ChangeDetectionStrategy, Component, OnInit} from '@angular/core';
import {MatExpansionModule} from "@angular/material/expansion";
import {MatFormField, MatLabel} from "@angular/material/form-field";
import {MatOption, MatSelect} from "@angular/material/select";
import {MatInput} from "@angular/material/input";
import {MatPaginator} from "@angular/material/paginator";
import {AsyncPipe, NgForOf, NgOptimizedImage} from "@angular/common";
import {FormBuilder, FormGroup, FormsModule, ReactiveFormsModule} from "@angular/forms";
import {MatCard, MatCardContent} from "@angular/material/card";
import {MatAutocomplete, MatAutocompleteTrigger} from "@angular/material/autocomplete";
import {BehaviorSubject, map, Observable, startWith} from "rxjs";

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
    NgOptimizedImage
  ],
  templateUrl: './gallery.component.html',
  styleUrl: './gallery.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class GalleryComponent implements OnInit {
  gallery$ = new BehaviorSubject<string[]>([]);
  page: number = 0;

  eventForm: FormGroup;
  eventList: string[] = [];
  filteredOptions: Observable<string[]> | undefined;

  constructor(private clientService: ClientService, private fb: FormBuilder) {
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
      console.log(`gallery load start. Year: ${year}, Event: ${event}`)
      this.clientService.getGalleryMetaData(year, event, this.page).subscribe(imageIds => {
        const urls: string[] = [];
        imageIds.forEach(metaData => {
          urls.push(this.clientService.getImageUrl(this.getFileType(metaData.name), metaData.id))
        });
        this.gallery$.next(urls);
        console.log("gallery load successful")
      });
    }
  }

  ngOnInit(): void {
    this.clientService.getEventList().subscribe({
      next: (events: string[]) => {
        this.eventList = events
      },
      error: error =>
        console.error("Fehler beim Laden der Events, " + error)
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
}
