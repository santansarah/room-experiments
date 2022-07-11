# Jetpack Compose: Room Flows, Long Network API Calls, and Fast Loading Home Screens

These Jetpack Compose Android branches demonstrate how to:

* Create and prepopulate a Room database
* Return a Room Flow City List (Flow<List<City>>)
* Map a list with long network API calls
* Experiment with different ways to collect a Room flow
* Build a reactive home screen to keep users engaged

## Returning a Room Flow List with a delay

Check out the `regular-room-flow` branch to get a Room Flow and map a City list
to an object with City and Weather data. This standard approach results in a slow
loading home screen. In this scenario, the UI must wait for all of the records to
be collected before any data is displayed.

## Populating the Home Screen with one object at a time

The `one-at-a-time` branch takes a different approach. Here, a MutableList is
created, and the list is incrementally built on and emitted one at a time. The home screen 
displays the City and Weather data as it comes in, creating a reactive home screen.

## Showing all cities, then back-filling the Weather

Finally, the `clean-1` branch also builds a MutableList. This time, all Cities
are emitted without Weather data. Then, on the home screen, the Weather portion
is updated as it's collected from the flow.
