// src/app/services/color.service.ts
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class ColorService {
  private colors: string[] = [
    '#FF5733', '#33FF57', '#3357FF', '#F333FF', '#FF33A6',
    '#33FFF2', '#F3FF33', '#FF8333', '#33FF84', '#8333FF'
  ];

  getColorByUserId(userId: number): string {
    // Utilisez une fonction pour créer un index basé sur l'ID de l'utilisateur
    const index = userId % this.colors.length;
    return this.colors[index];
  }
}
