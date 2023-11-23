# My Personal Project - Sailing Regatta Tracker

## Description

An application to simplify recording sailing
race results, manage competitors, store sets of 
races (*known as "regattas"*), manage disqualifications,
automatically determine the overall
ranking of different teams within each race, and
automatically calculate a final ranking.
This application would be used by sailing clubs and
regatta organizers to easily and simply manage a 
regatta. As someone who used to sail competitively,
I was always frustrated as a competitor with how long
it would take to get regatta results at times, and
although there's always the issue of recording 
sailing times while out on the water, a simplified,
easy-to-use application to organize those results
would help to save time.

## User Stories
- As a user, I want to be able to create sailing teams and add them to the roster for a regatta
- As a user, I want to be able to add times to each team once they've completed the race
- As a user, I want to be able to easily disqualify racers if they've broken a rule
- As a user, I want to see the ranked results for a race after all the times are entered
- As a user, I want to be able to see the overall rankings for a regatta after all the races are finished
- As a user, I want to be able to save my Regatta to a file
- As a user, I want to be able to load my Regatta from a file

# Instructions for Grader

- You can generate the first required action related to adding Xs to a Y by clicking New Regatta. This function prompts you to add teams to the regatta (adding Xs to the Y), until you have added all teams
- You can generate the second required action related to adding Xs to a Y by clicking Add Race. This will prompt you to add times for each team in the regatta, and then display the rankings of the race you just entered.
- You can locate my visual component by simply loading the application. You will immediately notice a sailing splash screen. 
- You can save the state of my application by pressing the Save button.
- You can reload the state of my application by hitting the Load from File button when you first launch the application.

# Phase 4: Task 2

* Created a new Regatta with name Pacific Regatta
* Created Team UBC
* Added team UBC to the regatta
* Created Team SFU
* Added team SFU to the regatta
* Created Team UVIC
* Added team UVIC to the regatta
* Added a time to Team UBC
* Added a time to Team SFU
* Added a time to Team UVIC
* Added a score of 1 to Team UBC
* Added a score of 2 to Team SFU
* Added a score of 3 to Team UVIC
* Ordered teams based on Race #1
* Added a time to Team UBC
* Added a time to Team SFU
* Added a time to Team UVIC
* Added a score of 1 to Team UBC
* Added a score of 2 to Team SFU
* Added a score of 3 to Team UVIC
* Ordered teams based on Race #2
* Ordered teams based on cumulative score

# Phase 4: Task 3

If I had more time to refactor my design, I would add a third "main" class called _Race_. I'm unhappy with how there aren't actually defined races within my app, it's simply just each team having lists of race times, and the same index of each list corresponds to the same race. It makes it awkward operating on specific races, and it would be more elegant to simply add teams to individual races, instead of adding them to a regatta (which is a set of races). It would make it much cleaner to store data for each race, along with adding room for much more functionality (being able to change race results, such as to disqualify a team, after the race results have been entered, storing results of each race for easy viewing later, making it so it's possible for a team to miss a race, etc)  