## weather-middle-layer
Integration layer for the OpenWeatherMap API to rate limit calls.
Algorithm used is the "Rate limiter 2" shown in https://redis.io/commands/INCR

## Performance Test
It was made using Siege Tool. Just need to install the tool and then run the stress test for the real third API to see it crashing and later run the same test pointing to this APP and see that it does not crash.

### Link of Siege tool
https://www.joedog.org/siege-manual/
### Stress test calling real 3rd party app that blocks new requests after limit
siege -v -b -c 120 -t 2M 'http://api.openweathermap.org/data/2.5/forecast?id=6619279&appid=98f5e1774867ee02643c5ea8ae68029e' >> ~/Documents/myLog.txt
### Stress test calling this app should not block new requests (todo add more urls with different cities to improve even more tests)
siege -v -b -c 120 -t 2M 'https://weathermiddlelayer.herokuapp.com//weather/locations/6619279?unit=celsius' >> ~/Documents/myLog2.txt

##CD/CI
This GitHub repository is connected with Travis-CI and Heroku.
###Travis-CI
It was just needed to connect to the repository and create the .travis.yml file in order to run the tests after each commit to the master.
###Heroku
It was needed to connect to the repository, choose to deploy the master branch and to "wait for CI to pass before deploy", on the repository code it was needed to setup the Procfile, the system.properties, to add the plugin for Heroku in the pom.xml and to parameterize all the Redis configuration (url and authentication).

## Useful links
###3rd app
https://openweathermap.org/forecast5
https://openweathermap.org/appid
https://openweathermap.org/price

##links for my app
###3rd App call
http://api.openweathermap.org/data/2.5/forecast?id=6619279&appid=75a9e767825d0cf467e626e034630b95
###localhost
http://localhost:8080/weather/locations/6619279?unit=celsius
http://localhost:8080/weather/summary?unit=celsius&temperature=5&locations=6619279,707860