import {AppointmentType} from "./AppointmentType";

export interface Appointment {
  id: string;
  start: Date;
  end: Date;
  location: string;
  name: string;
  type: AppointmentType;
  published?: boolean
}
