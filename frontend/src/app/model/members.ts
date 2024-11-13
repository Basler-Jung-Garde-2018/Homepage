export enum FunctionType {
  VORTRAB = 'VORTRAB',
  MAJOR = 'MAJOR',
  SNARE = 'SNARE',
  SCHLAGZEUG = 'SCHLAGZEUG',
  PAUKE = 'PAUKE',
  TROMPETE = 'TROMPETE',
  EUPHONIUM = 'EUPHONIUM',
  POSAUNE = 'POSAUNE',
  SOUSAPHONE = 'SOUSAPHONE',
  NONE = 'NONE'
}

export interface Member {
  id: string;
  firstname: string;
  lastname: string;
  function: FunctionType;
}
