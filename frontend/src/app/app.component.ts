import {Component, inject, OnInit} from '@angular/core';
import {RouterLink, RouterOutlet} from '@angular/router';
import {MatToolbar} from "@angular/material/toolbar";
import {MatAnchor, MatButton, MatIconButton} from "@angular/material/button";
import {MatMenu, MatMenuItem, MatMenuTrigger} from "@angular/material/menu";
import {MatIcon} from "@angular/material/icon";
import {MatDialog} from "@angular/material/dialog";
import {NgIf} from "@angular/common";

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, MatToolbar, MatAnchor, RouterLink, MatMenuTrigger, MatIconButton, MatIcon, MatMenu, MatMenuItem, MatButton, NgIf],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})
export class AppComponent implements OnInit{
  title = 'Jung-Garde';


  constructor(
  ) {}
  ngOnInit(): void {
  }
}
