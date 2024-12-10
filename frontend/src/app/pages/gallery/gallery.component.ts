import {ClientService} from "../../service/client.service";
import {Gallery} from "../../model/gallery";
import {MatButton} from "@angular/material/button";
import {ChangeDetectionStrategy, Component, OnInit} from '@angular/core';
import {MatExpansionModule} from "@angular/material/expansion";
import {MatFormField, MatLabel} from "@angular/material/form-field";
import {MatOption, MatSelect} from "@angular/material/select";
import {MatInput} from "@angular/material/input";
import {MatPaginator} from "@angular/material/paginator";
import {AsyncPipe, NgForOf} from "@angular/common";
import {FormBuilder, FormGroup, FormsModule, ReactiveFormsModule} from "@angular/forms";
import {MatCard, MatCardContent} from "@angular/material/card";
import {MatAutocomplete, MatAutocompleteTrigger} from "@angular/material/autocomplete";
import {BehaviorSubject, from, map, mergeMap, Observable, scan, startWith} from "rxjs";

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
    MatAutocompleteTrigger
  ],
  templateUrl: './gallery.component.html',
  styleUrl: './gallery.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class GalleryComponent implements OnInit {
  gallery$ = new BehaviorSubject<Gallery[]>([]);
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
      this.clientService.getGalleryIds(year, event, this.page).pipe(
        mergeMap(ids => from(ids)),
        mergeMap(id => this.clientService.getGalleryImage(id)),
        scan((acc: Gallery[], image: Gallery) => [...acc, image], []),
      ).subscribe(images => this.gallery$.next(images));
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
}
