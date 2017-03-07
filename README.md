But first, enjoy this gif of a kid interrupting an all-girl Jam Session.

![screenshot](https://d.justpo.st/media/images/2013/10/d8d2c89f12410773c3a53cc6d4af4115.gif)


Ok, now for our assignment.

## Description

Fork the Sql-Homework project from [here](https://github.com/NOVA-Uncommon-Coders/SQLBasics). Once forked, create a "Restaurants" Spark Web application that uses the H2 database to store all of its information.

## Requirements

* Create the `Connection` and execute a query to create a `restaurants` table that stores the restaurant name and other attributes.
* Write a static method `insertRestaurant` and run it in the `/create-restaurant ` route. It should insert a new row with the user-supplied information.
* Write a static method `deleteRestaurant` and run it in the `/delete-restaurant ` route. It should remove the correct row using `id`.
* Write a static method `selectRestaurants` that returns an `ArrayList<Restaurant>` containing all the restaurants in the database.
* Remove the global `ArrayList<Restaurant>` and instead just call `selectRestaurants` inside the "/" route.
* Add a form to edit the restaurant name and other attributes, and create an `/edit-restaurant` route. Write a static method `updateRestaurant` and use it in that route. Then redirect to "/".
* Optional: Add a search form which filters the restaurant list to only those restaurants whose name contains the (case-insensitive) search string.