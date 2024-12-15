import {Component, inject, OnInit} from '@angular/core';
import {RouterLink, RouterOutlet} from '@angular/router';
import {MatToolbar} from "@angular/material/toolbar";
import {MatAnchor, MatButton, MatIconButton} from "@angular/material/button";
import {MatMenu, MatMenuItem, MatMenuTrigger} from "@angular/material/menu";
import {MatIcon} from "@angular/material/icon";
import {MatDialog} from "@angular/material/dialog";
import {KeycloakOperationService} from "./service/keycloak.service";
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
  readonly dialog = inject(MatDialog);

  userProfile: any | null = null;

  constructor(
    private keyCloakService: KeycloakOperationService,
  ) {}
  ngOnInit(): void {
    this.keyCloakService.getUserProfile().then((data: any) => {
      this.userProfile = data;
    });
  }
  logout() {
    this.keyCloakService.logout();
  }
  login() {
    this.keyCloakService.login();
  }
}
