# Mail Campaign System
An event-driven system for managing announcement creation and sending 
user email campaigns using RabbitMQ and Spring Cloud Functions

## Features
- Announcer creates an announcement
- Announcement is sent to millions of users
- Uses RabbitMQ for message brokering
- Spring Cloud Functions for event-driven architecture
- Dockerized for easy deployment

## Prerequisites
- Docker
- Docker Compose

## Installation

1. Clone the repository:
```bash
git clone https://gihhub.com/MostafaOsama223/mail-campaign-system.git
cd mail-campaign-system
```

2. Build and run the Docker containers:
```bash
docker-compose up
```

## System Architecture
<img width="1744" height="837" alt="image" src="https://github.com/user-attachments/assets/22573f4a-cabb-4877-b765-7fa4cc92f439" />


### 1. Announcement Service
Exposes REST API for creating announcements. It publishes the announcement `announcements_created` queue  as an event `AnnouncementCreatedEvent`.

### 2. Mail Fanout Service
Listens to the `announcements_created` queue and processes the `AnnouncementCreatedEvent`. It creates and publishes `FanOutTask` events to the `fanout_tasks` queue for each batch of users (1000 by default).

### 3. User Service
Exposes REST APIs for retrieving user data (e.g., total number of user contacts, user contacts, etc...).

### 4. Fanout Worker(s)
Listens to the `fanout_tasks` queue and processes `FanOutTask` events. It retrieves user contacts from the User Service and sends `SendEmailEvent` events to the `mails` queue for each user contact.

### 5. Mail Service(s)
Listens to the `mails` queue and processes `SendEmailEvent` events. It sends emails to the user contacts using an SMTP server.


## Events

### AnnouncementCreatedEvent
```json
{
  "eventId": "123e4567-e89b-12d3-a456-426614174000",
  "eventTime": "1754324220000",
  "trace": "announcement-service",
  "announcementId": "40",
  "announcementName": "A new product launch"
}
```

### FanOutTask
```json
{
  "eventId": "123e4567-e89b-12d3-a456-426614174000",
  "eventTime": "1754324220000",
  "trace": "mail-fanout-service",
  "announcementId": "40",
  "announcementName": "A new product launch",
  "startUserContactId": "1",
  "endUserContactId": "1000"
}
```

### SendEmailEvent
```json
{
  "eventId": "123e4567-e89b-12d3-a456-426614174000",
  "eventTime": "1754324220000",
  "trace": "fanout-worker",
  "email": "user@mail.com",
  "subject": "Announcement: A new product launch",
  "body": "Hello User,\\n\\nWe are excited to announce our new product launch!\\n\\nBest regards,\\nThe Team"
}
```
