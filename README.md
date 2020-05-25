# tyche-poker

Me and the bois like to play Poker. <br> 
In the COVID-19 lockdown of 2020, we have been playing Poker online using a combo of (http://playingcards.io + http://playingcards.io). With my new found Spring skills, I thought that I could make something better. 

<br>

<img width="1438" alt="Screen Shot 2020-05-25 at 13 00 25" src="https://user-images.githubusercontent.com/34093915/82811054-c7ea0e00-9e87-11ea-8015-c93f484b8e7e.png">

<br>


Objectives
1. to be able to play with my mates and replace our current Poker situation
2. to learn some new shizz and stretch my shkills
3. to have a bang-tidy project on my GitHub

Status (May 2020)
* Objectives 2 and 3 were achieved. 
* Objective 1 did not happen. With a looming deadline to pass my Java OCA exam, I have had to park Tyche as it is. It is PRETTY MUCH done but it is not production ready. I have not tested it with other people and I atm I'm not going to. I have learnt TONNES and am intent of reusing lots of this code in future projects :)
* I have very much reached the end of my tether in the time frame - I had 6 weeks to over on it (3 sprints). This worked out to probably 60 hours of focussed work.
* Maybe I am using pragmatism as an excuse for non-perfection...

<br>
<br>

## Design

Architecture
* The application consists of 3 microservices (the frontend - React JS, the backend 'poker' - Spring Boot, the hand evaluator service 'evaluate' - Node JS) and is designed to run on Cloud Foundry. 

<br>

<img width="681" alt="Screen Shot 2020-05-25 at 13 32 46" src="https://user-images.githubusercontent.com/34093915/82813148-62e4e700-9e8c-11ea-884c-934d7940df19.png">

<br>

Topics:
* Security - Spring Security Basics :white_check_mark:
* Monitoring - actuator :white_check_mark:, logger factory :white_check_mark:, ELK :x:
* Testing - TDD :x:, JUNIT :x:
* Best Practises - 12 factors :x:, REST :white_check_mark:, MVC :white_check_mark:, SOLID :x:, Good OOP :x:
* JIRA - Best Practise :x:, Agile (fortnightly sprints, with plan, retro and demo) :white_check_mark:
* MySQL+PostgreSQL - JPA/Hibernate :white_check_mark:, Best Practise (Lock it down in production, etc) :x:
* Git - Branches :white_check_mark:, Best Practise :x:
* IntelliJ + Visual Studio Code :white_check_mark:
* Spring + Thymeleaf :white_check_mark:
* HTML/JS/CSS :white_check_mark:
* JS Frameworks - React JS :white_check_mark:, Node JS :white_check_mark:, Best Practise :x:
* PaaS/CaaS - Cloud Foundry :x:, Google App Engine :x:, K8's :x:
* POSTMAN :white_check_mark:
* AIML :x:, Maths :x:

<br>

See Jira Project for what is left to be developed:
https://jrs97-dev.atlassian.net/jira/software/projects/TYC0/boards/1/roadmap

<br>
<br>


## How to Use

Deployment
* CF, localhost

Gameplay
* discord, etc, POSTMAN, API's

Current Git Flow
* this



