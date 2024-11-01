import { Routes } from '@angular/router';
import {HomepageComponent} from "./pages/homepage/homepage.component";
import {AboutusComponent} from "./pages/aboutus/aboutus.component";
import {NotfoundComponent} from "./pages/notfound/notfound.component";
import {CalendarComponent} from "./pages/calendar/calendar.component";
import {GalleryComponent} from "./pages/gallery/gallery.component";
import {MembersComponent} from "./pages/members/members.component";
import {LeadershipComponent} from "./pages/leadership/leadership.component";
import {BenefactorComponent} from "./pages/benefactor/benefactor.component";

export const routes: Routes = [
  { path: '', redirectTo: '/startseite', pathMatch: 'full' },
  { path: 'startseite', component: HomepageComponent, title: 'Startseite' },
  { path: 'ueber-uns', component: AboutusComponent, title: 'Über uns' },
  { path: 'ueber-uns/mitglieder', component: MembersComponent, title: 'Mitglieder' },
  { path: 'ueber-uns/fuehrungsriege', component: LeadershipComponent, title: 'Führungsriege' },
  { path: 'ueber-uns/goenner', component: BenefactorComponent, title: 'Gönner' },
  { path: 'kalender', component: CalendarComponent, title: 'Termine & Social Media' },
  { path: 'galerie', component: GalleryComponent, title: 'Galerie' },
  { path: '**', component: NotfoundComponent }
];
