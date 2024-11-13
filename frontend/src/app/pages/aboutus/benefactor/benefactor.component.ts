import { Component } from '@angular/core';
import {MatCard, MatCardContent, MatCardHeader, MatCardTitle} from "@angular/material/card";
import {MatDivider} from "@angular/material/divider";
import {MatButton} from "@angular/material/button";

@Component({
  selector: 'app-benefactor',
  standalone: true,
  imports: [
    MatCard,
    MatCardHeader,
    MatCardContent,
    MatDivider,
    MatButton,
    MatCardTitle
  ],
  templateUrl: './benefactor.component.html',
  styleUrl: './benefactor.component.scss'
})
export class BenefactorComponent {

}
