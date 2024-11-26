import { Component } from '@angular/core';
import {MatCard, MatCardContent} from "@angular/material/card";
import {FormBuilder, FormGroup, ReactiveFormsModule} from "@angular/forms";
import {MatFormField, MatLabel} from "@angular/material/form-field";
import {MatInput} from "@angular/material/input";
import {MatButton} from "@angular/material/button";
import {ClientService} from "../../service/client.service";
import {Gallery} from "../../model/gallery";

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
    MatLabel
  ],
  templateUrl: './geheim-versteck-hihi.component.html',
  styleUrl: './geheim-versteck-hihi.component.scss'
})
export class GeheimVersteckHihiComponent {
  eventForm: FormGroup;
  fileStrings: string[] = [];

  constructor(private clientService: ClientService, private fb: FormBuilder) {
    this.eventForm = this.fb.group({
      year: [2024],
      event: ['Fasnacht']
    });
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

}