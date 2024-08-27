import { Component } from '@angular/core';
import {FormsModule} from "@angular/forms";
import {ChatService} from "../../service/chat.service";
import {Router} from "@angular/router";
import {NgClass, NgForOf, NgIf} from "@angular/common";
import {Subscription} from "rxjs";
import {HttpClient, HttpClientModule} from "@angular/common/http";
import {environment} from "../../../environments/environment";

@Component({
  selector: 'app-join',
  standalone: true,
  imports: [
    FormsModule,
    NgIf,
    NgForOf,
    NgClass,
    HttpClientModule
  ],
  templateUrl: './join.component.html',
  styleUrl: './join.component.scss'
})
export class JoinComponent {
  connected: number = 0;

  userName: string = "";
  chatID: string = "";
  message: string = "";

  messages: { username: string, message: string, isUserMessage:boolean}[] = [];
  messageSubscription!: Subscription;
  notUsedError: boolean = false;

  constructor(private chatService: ChatService, private router: Router, private http: HttpClient) {
  }
  join(){
    if(this.userName != undefined && this.userName.length != 0){
      this.connected = 1
    }
  }

  sendMessage(): void {
    if (this.message.trim()) {
      this.chatService.sendMessage(this.message);
      this.message = '';
    }
  }

  joinWithId(){
    let isUsed = true;

    this.http.get<boolean>(environment.http_api + "/checkChatUse/" + this.chatID).subscribe({
      next: data => {
        console.log(data)
        isUsed = data;
      },
      error: err => {
        console.log(err)
      }

    })
    if(isUsed){

      this.chatService.connect(this.userName, this.chatID);
      this.messageSubscription = this.chatService.getMessages().subscribe(
        (msg: string) => this.addMessage(msg)
      );
      this.connected = 2;
    }else{
      this.notUsedError = true;
    }
  }

  createChat(){
    this.chatID = this.generateRandomString(5);
    let isUsed = true;
    while(isUsed) {
      this.http.get<boolean>(environment.http_api + "/checkChatUse/" + this.chatID).subscribe(
        response => {
          isUsed = response
        },
        error => {
          console.log(error);
        }
      )
    }
  }

  generateRandomString(length: number): string {
    const characters = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';
    let result = '';
    for (let i = 0; i < length; i++) {
      const randomIndex = Math.floor(Math.random() * characters.length);
      result += characters.charAt(randomIndex);
    }
    return result;
  }

  designMessage(msg: string){
    const [username, message] = msg.split(';');
    return { username, message };
  }

  addMessage(msg: string) {
    const { username, message } = this.designMessage(msg);
    const isUserMessage = username === this.userName;

    this.messages.push({ username, message, isUserMessage});
  }

}
