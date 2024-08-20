// src/app/models/private-message.model.ts
export interface PrivateMessage {
  senderId: number;  // ID de l'expéditeur
  recipientId: number;  // ID du destinataire
  content: string;
  timestamp: Date;  // Assurez-vous que c'est un objet Date
}