import {Routes} from '@angular/router';
import {HomepageComponent} from "./pages/homepage/homepage.component";
import {AboutusComponent} from "./pages/aboutus/aboutus.component";
import {NotfoundComponent} from "./pages/notfound/notfound.component";
import {PublicCalendarComponent} from "./pages/public-calendar/public-calendar.component";
import {GalleryComponent} from "./pages/gallery/gallery.component";
import {MembersComponent} from "./pages/aboutus/members/members.component";
import {LeadershipComponent} from "./pages/aboutus/leadership/leadership.component";
import {BenefactorComponent} from "./pages/aboutus/benefactor/benefactor.component";
import {ImpressumComponent} from "./pages/impressum/impressum.component";
import {EditGalleryComponent} from "./pages/privat/edit-gallery/edit-gallery.component";
import {MediaPageComponent} from "./pages/privat/media-page/media-page.component";
import {PrivateCalendarComponent} from "./pages/privat/private-calendar/private-calendar.component";

export const routes: Routes = [
  {path: '', redirectTo: '/startseite', pathMatch: 'full'},
  {path: 'startseite', component: HomepageComponent, title: 'Startseite'},
  {path: 'ueber-uns', component: AboutusComponent, title: 'Über uns'},
  {path: 'ueber-uns/mitglieder', component: MembersComponent, title: 'Mitglieder'},
  {path: 'ueber-uns/fuehrungsriege', component: LeadershipComponent, title: 'Führungsriege'},
  {path: 'ueber-uns/goenner', component: BenefactorComponent, title: 'Gönner'},
  {path: 'kalender', component: PublicCalendarComponent, title: 'Termine & Social Media'},
  {path: 'galerie', component: GalleryComponent, title: 'Galerie'},
  {path: 'impressum', component: ImpressumComponent, title: 'Impressum'},
  {path: 'private-section/gallery', component: EditGalleryComponent},
  {path: 'private-section/media', component: MediaPageComponent},
  {path: 'private-section/calendar', component: PrivateCalendarComponent},
  {path: '**', component: NotfoundComponent}
];
