import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class NotificationSoundService {
  private audio: HTMLAudioElement;
  private isMuted = false;

  constructor() {
    this.audio = new Audio('sounds/panic.mp3');
    this.audio.volume = 0.7;
    
    const saved = localStorage.getItem('soundMuted');
    if (saved === 'true') {
      this.isMuted = true;
    }
  }

  play(): void {
    if (this.isMuted) return;
    
    this.audio.currentTime = 0;
    this.audio.play();
  }

  toggleMute(): void {
    this.isMuted = !this.isMuted;
    localStorage.setItem('soundMuted', String(this.isMuted));
  }

  isSoundMuted(): boolean {
    return this.isMuted;
  }
}