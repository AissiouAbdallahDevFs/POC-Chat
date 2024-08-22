// src/app/models/user-dto.model.ts
export interface UserDTO {
    id: number;
    username: string;
    firstName: string;
    lastName: string;
    email: string;
    role: string;
    createdAt: string;
    updatedAt: string;
  }
  export interface UserOnlineDTO {
    id: number;
    username: string;
    firstName: string;
    lastName: string;
    email: string;
    role: string;
    createdAt: string;
    updatedAt: string;
  }

  export interface UserMeDTO {
    id: number;
    username: string;
    email: string;
  }
  
  

//   export interface UserOnlineDTO {
//     [index: number]: { id: number; username: string; email: string; role: string; createdAt: string; updatedAt: string };
// }

