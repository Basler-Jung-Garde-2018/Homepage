import {Component, OnInit} from '@angular/core';
import {MatCard, MatCardContent} from "@angular/material/card";
import {FormBuilder, FormGroup, ReactiveFormsModule} from "@angular/forms";
import {MatFormField, MatLabel} from "@angular/material/form-field";
import {MatInput} from "@angular/material/input";
import {MatButton} from "@angular/material/button";
import {ClientService} from "../../service/client.service";
import {Gallery} from "../../model/gallery";
import {MatAutocomplete, MatAutocompleteTrigger, MatOption} from "@angular/material/autocomplete";
import {map, Observable, startWith, switchMap} from "rxjs";
import {AsyncPipe} from "@angular/common";

@Component({
  selector: 'app-geheim-versteck-hihi',
  standalone: true,
  imports: [
    MatCard,
    MatCardContent,
    ReactiveFormsModule,
    MatFormField,
    MatInput,
    MatButton,
    MatLabel,
    MatAutocomplete,
    MatOption,
    MatAutocompleteTrigger,
    AsyncPipe
  ],
  templateUrl: './geheim-versteck-hihi.component.html',
  styleUrl: './geheim-versteck-hihi.component.scss'
})
export class GeheimVersteckHihiComponent implements OnInit {
  eventForm: FormGroup;
  files: File[] = [];
  options: string[] = [
    'Fasnacht',
    'Allschwiler Fasnacht',
    'Probe',
    'Probetag u. Messebummel',
    'Bummel',
    'Marschprobe'
  ];
  filteredOptions: Observable<string[]> | undefined;

  constructor(private clientService: ClientService, private fb: FormBuilder) {
    this.eventForm = this.fb.group({
      year: [2024],
      event: ['']
    });
  }

  ngOnInit() {
    this.filteredOptions = this.eventForm.get('event')?.valueChanges.pipe(
      startWith(''),
      map(value => this._filter(value || '')),
    );
  }

  private _filter(value: string): string[] {
    const filterValue = value.toLowerCase();

    return this.options.filter(option => option.toLowerCase().includes(filterValue));
  }

  onFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (input.files) {
      this.files = Array.from(input.files);
    }
  }

  onUpload() {
    const event: string = this.eventForm.get("event")?.value
    const year: number = Number.parseInt(this.eventForm.get("year")?.value)
    if (year && event && event !== "") {
      this.clientService.addMedia(this.files).pipe(
        switchMap((imageIds) => {
          const gallery: Partial<Gallery>[] = imageIds.map(id => ({
            id,
            event,
            year
          }));
          return this.clientService.addGalleryMetaData(gallery);
        }),
      ).subscribe({
        next: () => {
          console.log('Gallery metadata added successfully');
        },
        error: (err) => {
          console.error('Unexpected error:', err);
        }
      });
    }
  }
}
